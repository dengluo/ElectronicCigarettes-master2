package com.bestmafen.smablelib.entity;

public class SmaSleep {
    public static class Mode {
        public static final int START = 0X11;
        public static final int END = 0X22;
        public static final int DEEP = 0X01;
        public static final int LIGHT = 0X02;
        public static final int AWAKE = 0X03;
    }

    public long id;
    public String date;
    public long time;
    public int mode;
    public int soft;
    public int strong;
    public String account;
    public int synced;

    @Override
    public String toString() {
        return "SmaSleep{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", mode=" + mode +
                ", strong=" + strong +
                ", soft=" + soft +
                ", account='" + account + '\'' +
                ", synced=" + synced +
                '}';
    }
}
