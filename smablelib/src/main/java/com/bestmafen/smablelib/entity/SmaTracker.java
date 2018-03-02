package com.bestmafen.smablelib.entity;

/**
 * Created by Administrator on 2017/3/20.
 */

public class SmaTracker {
    public long id;
    public String account;
    public String date;
    public long time;
    public double latitude;
    public double longitude;
    public int synced;

    @Override
    public String toString() {
        return "SmaTracker{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", synced=" + synced +
                '}';
    }
}
