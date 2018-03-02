package com.bauway.alarm.bean;

/**
 * Created by zhaotaotao on 2017/12/19.
 */

public class RingBean {

    String ringName;
    String ringPath;
    boolean isSelected;

    public RingBean(String ringName, String ringPath, boolean isSelected) {
        this.ringName = ringName;
        this.ringPath = ringPath;
        this.isSelected = isSelected;
    }

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingPath() {
        return ringPath;
    }

    public void setRingPath(String ringPath) {
        this.ringPath = ringPath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
