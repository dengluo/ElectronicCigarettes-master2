package com.bestmafen.smablelib.entity;

public class SmaHeartRate {
    public static class Type {
        public static final int NORMAL = 0x00;
        public static final int RESTING = 0x01;
        public static final int EXERCISE = 0x02;
        public static final int EXERCISE_IMED = 0x03;
    }

    public long id;
    public int type;
    public String date;
    public long time;
    public int value;
    public String account;
    public int synced;

    @Override
    public String toString() {
        return "SmaHeartRate{" +
                "id=" + id +
                ", type=" + type +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", value=" + value +
                ", account='" + account + '\'' +
                ", synced=" + synced +
                '}';
    }
}
