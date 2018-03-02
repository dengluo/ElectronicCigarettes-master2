package com.bestmafen.smablelib.entity;

public class SmaSport {
    public static class Mode {
        //平时的运动状态
        public static final int SIT = 0X10;
        public static final int WALK = 0X11;
        public static final int RUN = 0X12;
        //锻炼模式
        public static final int START = 0X20;
        public static final int GOING = 0X22;
        public static final int END = 0X2F;
        //i-MED锻炼模式
        public static final int START_IMED_6 = 0X30;
        public static final int START_IMED_12 = 0X31;
        public static final int END_IMED = 0X3F;
    }

    public long id;
    public String date;
    public long time;
    public int mode;
    public int step;
    public float distance;
    public float calorie;
    public String account;
    public int synced;

    @Override
    public String toString() {
        return "SmaSport{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", mode=" + mode +
                ", step=" + step +
                ", distance=" + distance +
                ", calorie=" + calorie +
                ", account='" + account + '\'' +
                ", synced=" + synced +
                '}';
    }
}
