package com.bauway.alarm.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhaotaotao on 2017/8/10.
 */

public class User extends BmobUser {

    private String app_msg;
    private String app_name;

    public String getApp_msg() {
        return app_msg;
    }

    public void setApp_msg(String app_msg) {
        this.app_msg = app_msg;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    @Override
    public String toString() {
        return "User{" +
                "app_msg='" + app_msg + '\'' +
                ", app_name='" + app_name + '\'' +
                '}';
    }
}
