package com.bestmafen.smablelib.entity;

/**
 * This class is used to send the time of your cellphone to a Bluetooth device.
 */
public class SmaTime implements ISmaCmd {
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;

    @Override
    public byte[] toByteArray() {
        byte[] extra = new byte[4];
        extra[0] = (byte) ((month >> 2) | (year << 2));
        extra[1] = (byte) ((hour >> 4) | (day << 1) | (month << 6));
        extra[2] = (byte) ((minute >> 2) | (hour << 4));
        extra[3] = (byte) (second | (minute << 6));
        return extra;
    }
}
