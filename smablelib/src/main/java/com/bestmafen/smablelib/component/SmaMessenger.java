package com.bestmafen.smablelib.component;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

import com.bestmafen.easeblelib.util.EaseUtils;
import com.bestmafen.easeblelib.util.L;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

/**
 * This class is used to communication with Bluetooth device.
 */
class SmaMessenger {
    /**
     * The queue holds normal messages,such as reading a characteristic,writing a characteristic and enabling or disabling a
     * notification.
     */
    private LinkedBlockingQueue<SmaMessage> mMessages = new LinkedBlockingQueue<>();

    /**
     * The semaphore to keep every small package of data sent to device is processed sequentially.
     */
    private Semaphore mPackageSemaphore = new Semaphore(1);

    private volatile boolean isExit = false;

    /**
     * The type of messages.
     */
    enum MessageType {
        /**
         * to write a characteristic
         */
        WRITE,

        /**
         * to read a characteristic
         */
        READ,

        /**
         * to enable or disable a notification
         */
        NOTIFY
    }

    SmaMessenger() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (!isExit) {
                        SmaMessage message = mMessages.take();
                        if (message.mGatt == null || message.mCharacteristic == null) continue;

                        switch (message.mType) {
                            case READ:
                                L.v("SmaMessenger acquire mPackageSemaphore -> " + mPackageSemaphore.availablePermits() +
                                        " " + ", " + message.toString());
                                mPackageSemaphore.acquire();
                                message.mGatt.readCharacteristic(message.mCharacteristic);
                                break;
                            case NOTIFY:
                                BluetoothGattDescriptor descriptor = message.mCharacteristic.getDescriptor(UUID.fromString
                                        ("00002902-0000-1000-8000-00805f9b34fb"));
                                if (descriptor == null) continue;

                                L.v("SmaMessenger acquire mPackageSemaphore -> " + mPackageSemaphore.availablePermits() +
                                        " , " + message.toString());
                                mPackageSemaphore.acquire();
                                message.mGatt.setCharacteristicNotification(message.mCharacteristic, message.mData != null);
                                descriptor.setValue(message.mData != null ? BluetoothGattDescriptor
                                        .ENABLE_NOTIFICATION_VALUE
                                        : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                                message.mGatt.writeDescriptor(descriptor);
                                break;
                            case WRITE:
                                L.v("SmaMessenger acquire mPackageSemaphore -> " + mPackageSemaphore.availablePermits() +
                                        " , " + message.toString());
                                mPackageSemaphore.acquire();
                                message.mCharacteristic.setValue(message.mData);
                                message.mGatt.writeCharacteristic(message.mCharacteristic);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Add a message to the queue {@link SmaMessenger#mMessages}
     *
     * @param message the message to be added
     */
    synchronized void addMessage(SmaMessage message) {
        try {
            L.v("SmaMessenger -> addMessage >>>> " + message.toString());
            mMessages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove all tasks in task queue.
     */
    synchronized void clearAllTask() {
        mMessages.clear();
        L.v("SmaMessenger clearAllTask 之前 -> " + mPackageSemaphore.availablePermits());
        if (mPackageSemaphore.availablePermits() == 0) {
            mPackageSemaphore.release();
        }
        L.v("SmaMessenger clearAllTask 之后 -> " + mPackageSemaphore.availablePermits());
    }

    void releasePackageSemaphore() {
        mPackageSemaphore.release();
        L.v("SmaMessenger -> release mPackageSemaphore " + mPackageSemaphore.availablePermits());
    }

    void exit() {
        isExit = true;
    }

    static class SmaMessage {
        /**
         * GATT
         */
        BluetoothGatt mGatt;

        /**
         * characteristic
         */
        BluetoothGattCharacteristic mCharacteristic;

        /**
         * if type of the message is {@link MessageType#WRITE},this field is the data will be sent to device;
         * if type of the message is {@link MessageType#NOTIFY},enable if this is not null,disable if is null.
         * if type of the message is {@link MessageType#READ},this field is useless.
         */
        byte[] mData;

        MessageType mType;

        SmaMessage(BluetoothGatt gatt, BluetoothGattCharacteristic bc, byte[] data, MessageType type) {
            this.mGatt = gatt;
            this.mCharacteristic = bc;
            this.mData = data;
            this.mType = type;
        }

        @Override
        public String toString() {
            return "SmaMessage{" +
                    "mType=" + mType +
                    ", mData=" + EaseUtils.byteArray2HexString(mData) +
                    '}';
        }
    }
}
