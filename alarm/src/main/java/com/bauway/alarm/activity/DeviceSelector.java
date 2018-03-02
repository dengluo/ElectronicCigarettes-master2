package com.bauway.alarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.util.PreferencesUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/12/14.
 * 设备选择
 */

public class DeviceSelector extends BaseActivity {
    @BindView(R.id.iv1)
    ImageView        mIv1;
    @BindView(R.id.iv2)
    ImageView        mIv2;
    @BindView(R.id.bt_tracker)
    ConstraintLayout mBtTracker;
    @BindView(R.id.iv3)
    ImageView        mIv3;
    @BindView(R.id.iv4)
    ImageView        mIv4;
    @BindView(R.id.bt_game_alarm)
    ConstraintLayout mBtGameAlarm;

    @Override
    protected int getLayoutRes() {
        return R.layout.device_selector;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick({R.id.bt_tracker, R.id.bt_game_alarm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_tracker:
                PreferencesUtils.putInt(mContext, MyConstants.DEVICE_TYPE, MyConstants.TRACKER_TYPE);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.bt_game_alarm:
                PreferencesUtils.putInt(mContext, MyConstants.DEVICE_TYPE, MyConstants.ALARM_TYPE);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
    }
}
