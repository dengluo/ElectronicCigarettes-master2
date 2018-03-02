package com.bauway.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.bean.User;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.util.PreferencesUtils;

/**
 * Created by zhaotaotao on 2017/12/13.
 * 启动页
 */
public class Launcher extends BaseActivity {
    @Override
    protected int getLayoutRes() {
        return R.layout.launcher;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity();
            }
        }, 2 * 1000);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        PreferencesUtils.putBoolean(mContext, MyConstants.SMS_CODE_TIMING, false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    private void startActivity() {
        User user = getUserEntity();
        if (user != null) {
            int type = PreferencesUtils.getInt(mContext, MyConstants.DEVICE_TYPE);
            if (type <= 0) {
                startActivity(new Intent(this, DeviceSelector.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
            startActivity(new Intent(this, Login.class));
        }
        this.finish();
    }
}
