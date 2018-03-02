package com.bestmafen.smablelib.component;

import android.bluetooth.BluetoothDevice;

/**
 * This class provides empty implementations of the methods from {@link SmaCallback}.Override this if you only care about
 * a few
 * of the available callback methods.
 */
public class SimpleSmaCallback implements SmaCallback {

    @Override
    public void onConnected(BluetoothDevice device, boolean isConnected) {

    }

    @Override
    public void notConnected() {

    }

    @Override
    public void onReadRssi(float rssi) {

    }

    @Override
    public void onReadWaringRssi(float wrssi) {

    }

    @Override
    public void onReadVoltage(float voltage) {

    }

    @Override
    public void onReadPuffCount(int puff) {

    }

    @Override
    public void onReadSmokeCount(int count) {

    }

    @Override
    public void onBeingDormant() {

    }

    @Override
    public void onCharging(float voltage) {

    }

    @Override
    public void onNtcSwitch(boolean ntc) {

    }

    @Override
    public void onReadTemperature(int temperature) {

    }

    @Override
    public void onWrite(byte[] data) {

    }

    @Override
    public void onRead(byte[] data) {

    }

    @Override
    public void onReadChargeCount(int count) {

    }
}
