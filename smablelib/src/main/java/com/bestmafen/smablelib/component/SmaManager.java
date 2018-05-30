package com.bestmafen.smablelib.component;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.bestmafen.easeblelib.connector.EaseConnector;
import com.bestmafen.easeblelib.util.EaseUtils;
import com.bestmafen.easeblelib.util.L;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This is a core class which we used to manager a Bluetooth device.
 */
public class SmaManager {
    /**
     * UUID of the main {@link BluetoothGattService}.
     */
    public static final String UUID_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";

    /**
     * UUID of the {@link BluetoothGattCharacteristic} for writing.
     */
    public static final String UUID_CHARACTER_WRITE = "0000fff2-0000-1000-8000-00805f9b34fb";

    /**
     * UUID of the {@link BluetoothGattCharacteristic} for reading.
     */
    public static final String UUID_CHARACTER_READ = "0000fff1-0000-1000-8000-00805f9b34fb";

    public static final String SP_DEVICE_NAME    = "sp_device_name";
    public static final String SP_DEVICE_ADDRESS = "sp_device_address";

    public static final byte DATA_ERROR = 0x07;

    public static final class SET {
        public static final byte ENABLE_ALARM       = 0x01;
        public static final byte DISABLE_ALARM      = 0x02;
        public static final byte TIME               = 0x03;
        public static final byte ALARM_OCLOCK       = 0x04;
        public static final byte GOAL_VOICE_MODE    = 0x05;
        public static final byte GOAL_SCORE_RATIO   = 0x06;
        public static final byte ENABLE_GOAL_LIGHT  = 0x0B;
        public static final byte DISABLE_GOAL_LIGHT = 0x0C;
        public static final byte DISABLE_GOAL_VOICE = 0x0D;
        public static final byte ENABLE_GOAL_VOICE  = 0x0E;
        public static final byte ALERT_TIME         = 0x11;
        //        public static final byte INCREASE_TEMPERATURE_BY_1 = 0x10;
//        public static final byte DECREASE_TEMPERATURE_BY_1 = 0x15;
        public static final byte TIME_PER_LOOP      = 0x17;
        public static final byte PUFF_PER_LOOP      = 0x19;
        public static final byte TEMPERATURE        = 0x1C;
        //public static final byte VIBRATION_SWITCH = 0x21;
        //public static final byte STOP = 0x25;
        public static final byte CALIBRATION = 0x26;
        public static final byte VIBRATION_DISABLED = 0x27;
        public static final byte VIBRATION_ENABLED  = 0x28;
        public static final byte INTO_OTA           = 0x5A;
    }

    public static final class BACK {
        public static final byte Waring_RSSI   = 0x11;
        public static final byte VOLTAGE       = 0x1F;
        public static final byte RSSI          = 0x0F;
        public static final byte PUFF          = 0x20;
        public static final byte SMOKE_COUNT   = 0x23;
        public static final byte BEING_DORMANT = 0x1A;
        public static final byte CHARGING      = 0x1D;
        public static final byte NTC           = 0x2B;
        public static final byte TEMPERATURE   = 0x2D;
        public static final byte CHARGE_COUNT  = 0x1B;
    }

    private         Context mContext;
    public volatile boolean isConnected;
    private List<SmaCallback> mSmaCallbacks     = new ArrayList<>();
    private BluetoothAdapter  sBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private SharedPreferences        mPreferences;
    private SharedPreferences.Editor mEditor;

    private SmaMessenger  mSmaMessenger;
    public  EaseConnector mEaseConnector;
    private boolean       isExit;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), BluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                L.d("SmaManager -> BluetoothAdapter.ACTION_STATE_CHANGED , state = " + state);
                switch (state) {
                    case BluetoothAdapter.STATE_ON:
                        String address = getNameAndAddress()[1];
                        if (!TextUtils.isEmpty(address))
                            mEaseConnector.setAddress(address).connect(true);
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
//                        isLoggedIn = false;
//                        isOta = false;
//                        mEaseConnector.connect(false);
                        break;
                }
            }
        }
    };

    public static synchronized SmaManager getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static SmaManager sInstance = new SmaManager();
    }

    private SmaManager() {
        super();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public SmaManager init(Context context) {
        isExit = false;
        mContext = context.getApplicationContext();
        mContext.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mPreferences.edit();

        mSmaMessenger = new SmaMessenger();
        mEaseConnector = new EaseConnector(mContext).setGattCallback(new BluetoothGattCallback() {

            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                L.d("SmaManager onConnectionStateChange -> status = " + status + ",newState = " + newState);
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        L.d("SmaManager STATE_CONNECTED");
                        gatt.discoverServices();
                        clearAllTask();
                    }
                }

                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    L.d("SmaManager STATE_DISCONNECTED");
                    mEaseConnector.closeConnect(!sBluetoothAdapter.isEnabled());
                    mEaseConnector.connect(sBluetoothAdapter.isEnabled() && !isExit);

                    if (isConnected) {
                        for (SmaCallback bc : mSmaCallbacks) {
                            bc.onConnected(gatt.getDevice(), false);
                        }
                    }

                    isConnected = false;
                    clearAllTask();
                }
            }

            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                L.d("SmaManager onServicesDiscovered");
                isConnected = true;
                mEaseConnector.connect(false);
                mSmaMessenger.addMessage(new SmaMessenger.SmaMessage(mEaseConnector.mGatt, mEaseConnector
                        .getGattCharacteristic(UUID_SERVICE, UUID_CHARACTER_READ), new byte[0], SmaMessenger.MessageType
                        .NOTIFY));
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onConnected(gatt.getDevice(), true);
                }
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                L.d("SmaManager onDescriptorWrite");
                mSmaMessenger.releasePackageSemaphore();
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                L.d("SmaManager onCharacteristicRead " + EaseUtils.byteArray2HexString(characteristic.getValue()));
            }

            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                byte[] data = characteristic.getValue();
                L.d("SmaManager onCharacteristicWrite " + EaseUtils.byteArray2HexString(data));
                mSmaMessenger.releasePackageSemaphore();
                for (SmaCallback callback : mSmaCallbacks) {
                    callback.onWrite(data);
                }
            }

            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                if (characteristic.getUuid().equals(UUID.fromString(UUID_CHARACTER_READ))) {
                    byte[] data = characteristic.getValue();
                    if (data == null) return;

                    L.d("SmaManager onCharacteristicChanged " + EaseUtils.byteArray2HexString(data));
                    parseData(data);
                    for (SmaCallback callback : mSmaCallbacks) {
                        callback.onRead(data);
                    }
                }
            }
        });

        return this;
    }

    /**
     * See {@link EaseConnector#connect(boolean)}
     *
     * @param connect
     */
    public void connect(boolean connect) {
        L.d("SmaManager -> connect " + connect);
        if (connect) {
            String address = getNameAndAddress()[1];
            if (TextUtils.isEmpty(address) || !sBluetoothAdapter.isEnabled() || isConnected) return;

            mEaseConnector.setAddress(address).connect(true);
        } else {
            mEaseConnector.connect(false);
        }
    }

    /**
     * See {@link EaseConnector#closeConnect(boolean)}
     *
     * @param stopReconnect
     */
    public void close(boolean stopReconnect) {
        isConnected = false;
        mEaseConnector.closeConnect(stopReconnect);
    }

    /**
     * Exit and release resource.
     */
    public void exit() {
        L.d("SmaManager -> exit");
        isExit = true;
        mContext.unregisterReceiver(mReceiver);
        mSmaMessenger.exit();
        mEaseConnector.exit();
        isConnected = false;
    }

    /**
     * See {@link EaseConnector#setDevice(BluetoothDevice)}
     *
     * @param device
     */
    public void bindWithDevice(BluetoothDevice device) {
        L.d("SmaManager -> bindWithDevice " + device.getName() + " " + device.getAddress());
        mEaseConnector.setDevice(device);
        mEaseConnector.connect(true);
    }

    /**
     * Write data to device to bind with it,or unbind with the bond device and clear all its information saved in local.
     */
    public void unbind() {
        L.d("SmaManager -> unbind");
        String address = getNameAndAddress()[1];
        if (!TextUtils.isEmpty(address)) {
            removeDevice(address);
        }

        setNameAndAddress("", "");
        mEaseConnector.setAddress("");
        mEaseConnector.setDevice(null);
        mEaseConnector.closeConnect(true);
        isConnected = false;
    }

    /**
     * Parse the data received.
     */
    private synchronized void parseData(byte[] data) {
        if (data == null || data.length != 11) return;
        if (data[1] == 0x8) return;

        byte cmd = data[1];
        byte[] valuePart = Arrays.copyOfRange(data, 2, 8);
        StringBuilder sb = new StringBuilder();
        for (byte b : valuePart) {
            sb.append((char) b);
        }
        float value = Float.parseFloat(sb.toString());
        L.d("parseData " + EaseUtils.byteArray2HexString(data) + " , valuePart = " + sb.toString());
        switch (cmd) {
            case BACK.Waring_RSSI:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadWaringRssi(value);
                }
                break;
            case BACK.RSSI:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadRssi(value);
                }
                break;
            case BACK.BEING_DORMANT:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onBeingDormant();
                }
                break;
            case BACK.CHARGING:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onCharging(value);
                }
                break;
            case BACK.NTC:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onNtcSwitch(false);
                }
                break;
            case BACK.PUFF:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadPuffCount((int) value);
                }
                break;
            case BACK.SMOKE_COUNT:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadSmokeCount((int) value);
                }
                break;
            case BACK.TEMPERATURE:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadTemperature((int) value);
                }
                break;
            case BACK.VOLTAGE:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadVoltage(value);
                }
                break;
            case BACK.CHARGE_COUNT:
                for (SmaCallback bc : mSmaCallbacks) {
                    bc.onReadChargeCount((int) value);
                }
                break;
        }
    }

    public String[] getNameAndAddress() {
        if (mPreferences == null) {
            return null;
        }
        return new String[]{mPreferences.getString(SP_DEVICE_NAME, ""), mPreferences.getString(SP_DEVICE_ADDRESS, "")};
    }

    public void setNameAndAddress(String name, String address) {
        mEditor.putString(SP_DEVICE_NAME, name).apply();
        mEditor.putString(SP_DEVICE_ADDRESS, address).apply();
    }

    /**
     * 利用反射
     * 将设备从手机蓝牙设置中的已配对设备中移除，防止三星J5008等手机解除绑定后，在手机蓝牙设置中没有清除已配对设备信息，导致再次连接时会被动断开。
     *
     * @param address 设备地址
     */
    private void removeDevice(String address) {
        try {
            BluetoothDevice device = sBluetoothAdapter.getRemoteDevice(address);
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            boolean success = (Boolean) m.invoke(device, (Object[]) null);
            L.d("removeDevice -> " + success);
        } catch (Exception e) {
            L.w("removeDevice = " + e.getMessage());
        }
    }

    /**
     * Add a callback to the list of callbacks to receive the result of communication with Bluetooth device.
     *
     * @param cb the callback to be added
     * @return This SmaManager object.
     */
    public SmaManager addSmaCallback(SmaCallback cb) {
        mSmaCallbacks.add(cb);
        return this;
    }

    /**
     * Remove a callback from the list of callbacks.
     *
     * @param cb the callback to be removed
     */
    public void removeSmaCallback(SmaCallback cb) {
        mSmaCallbacks.remove(cb);
    }

    public void write(byte cmd, String value) {
        if (TextUtils.isEmpty(value)) return;

        L.d("write -> " + value);
        byte[] extra = new byte[6];
        Arrays.fill(extra, (byte) '0');
        for (int i = 0, l = value.length(); i < l; i++) {
            if (i < 6) {
                extra[6 - l + i] = (byte) value.charAt(i);
            }
        }

        write(cmd, extra);
    }

    public void write(byte cmd) {
        write(cmd, new byte[6]);
    }

    /**
     * 写入命令到设备
     *
     * @param cmd   命令
     * @param value 值，长度为6
     */
    public void write(byte cmd, byte[] value) {
        if (!isConnected) {
            for (SmaCallback callback : mSmaCallbacks) {
                callback.notConnected();
            }
            return;
        }

        if (value.length != 6) {
            throw new IllegalArgumentException("value的长度必须为6");
        }

        byte[] data = new byte[11];
        System.arraycopy(value, 0, data, 2, 6);
        data[0] = 0x02;
        data[1] = cmd;
        data[9] = 0x0D;
        data[10] = 0x0A;

        int sum = 0;
        for (int i = 0; i < data.length; i++) {
            if (i != 8) {
                sum += (data[i] & 0xff);
            }
        }
        data[8] = (byte) (sum & 0xff);

        mSmaMessenger.addMessage(new SmaMessenger.SmaMessage(mEaseConnector.mGatt, mEaseConnector.getGattCharacteristic
                (UUID_SERVICE, UUID_CHARACTER_WRITE), data, SmaMessenger.MessageType.WRITE));
    }

    /**
     * Remove all tasks in task queue.We call this method when the device get connected or disconnected,even thought the
     * tasks
     * have not been processed at that moment.
     */
    public void clearAllTask() {
        L.d("SmaManager clearAllTask");
        mSmaMessenger.clearAllTask();
    }
}
