package com.bauway.alarm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.activity.ConnectDevice;
import com.bauway.alarm.activity.RingSet;
import com.bauway.alarm.base.BaseFragment;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.interfaces.DialogCallback;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.MyNumberPicker.MyNumberPicker;
import com.bauway.alarm.util.MyNumberPicker.MyNumberPickerTools;
import com.bauway.alarm.util.PreferencesUtils;
import com.bauway.alarm.util.Tools;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

import static java.lang.String.valueOf;

/**
 * Created by zhaotaotao on 2017/12/14.
 */

public class AlarmSet extends BaseFragment {
    public static final int REQUEST_CODE_BIND = 1;
    public static final int REQUEST_CODE_RING = 2;

    @BindView(R.id.num_picker_hour)
    MyNumberPicker mNumPickerHour;
    @BindView(R.id.num_picker_min)
    MyNumberPicker mNumPickerMin;
    @BindView(R.id.num_picker_am_pm)
    MyNumberPicker mNumPickerAmPm;
    //    @BindView(R.id.tv_alarm_time)
//    TextView       mTvAlarmTime;
    @BindView(R.id.bt_switch_alarm)
    SwitchCompat   mBtSwitchAlarm;
    @BindView(R.id.bt_switch_vibration)
    SwitchCompat   mBtSwitchVibration;
    @BindView(R.id.tv_ring_name)
    TextView       mTvRingName;
    @BindView(R.id.switch_ring)
    SwitchCompat   mSwitchRing;
    @BindView(R.id.bt_scale_one)
    TextView       mBtScaleOne;
    @BindView(R.id.bt_scale_two)
    TextView       mBtScaleTwo;
    @BindView(R.id.bt_scale_three)
    TextView       mBtScaleThree;
    @BindView(R.id.tv_bind_status)
    TextView       mTvBindStatus;
    @BindView(R.id.bt_bind_device)
    TextView       mBtBindDevice;
    Unbinder unbinder;

    private String[] num, hourMinutes;
    private String[] dayTime = {"AM", "PM"};
    private int hour;
    private int min;
    private int am;

    private SmaManager  mSmaManager;
    private SmaCallback mSmaCallback;

    public static AlarmSet newInstance() {
        return new AlarmSet();
    }

    @Override
    protected void initView(View mView) {
        int oclock = PreferencesUtils.getInt(mContext, MyConstants.ALARM_OCLOCK, 0);
        hour = (oclock >> 8) % 12;
        min = oclock & 0xff;
        am = (oclock >> 8) / 12;
        num = Tools.getNum(0, 11);
        mNumPickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerHour.setDisplayedValues(num);
        mNumPickerHour.setMinValue(0);
        mNumPickerHour.setMaxValue(num.length - 1);
        mNumPickerHour.setValue(hour);
        mNumPickerHour.setWrapSelectorWheel(true);

        hourMinutes = Tools.getHourMinutes();
        mNumPickerMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerMin.setDisplayedValues(hourMinutes);
        mNumPickerMin.setMinValue(0);
        mNumPickerMin.setMaxValue(hourMinutes.length - 1);
        mNumPickerMin.setValue(min);
        mNumPickerMin.setWrapSelectorWheel(true);

        mNumPickerAmPm.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerAmPm.setDisplayedValues(dayTime);
        mNumPickerAmPm.setMinValue(0);
        mNumPickerAmPm.setMaxValue(dayTime.length - 1);
        mNumPickerAmPm.setValue(am);
        mNumPickerAmPm.setWrapSelectorWheel(false);

        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerHour);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerMin);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerAmPm);

        mBtSwitchAlarm.setChecked(PreferencesUtils.getBoolean(mContext, MyConstants.ALARM_ENABLED));
        mBtSwitchVibration.setChecked(PreferencesUtils.getBoolean(mContext, MyConstants.GOAL_LIGHT_ENABLED));
        mSwitchRing.setChecked(PreferencesUtils.getBoolean(mContext, MyConstants.GOAL_VOICE_ENABLED));

        updateRingUI(PreferencesUtils.getInt(mContext, MyConstants.GOAL_VOICE_MODE, 0));
        updateRatioUI(PreferencesUtils.getInt(mContext, MyConstants.GOAL_SCORE_RATIO, 1));
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        boolean bind = !(TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0]));
        updateBindStatus(bind);
    }

    @Override
    protected void initEvent() {
        mNumPickerHour.setOnValueChangedListener(new MyListener());
        mNumPickerMin.setOnValueChangedListener(new MyListener());
        mNumPickerAmPm.setOnValueChangedListener(new MyListener());

        mBtSwitchAlarm.setOnCheckedChangeListener(new MyListener2());
        mBtSwitchVibration.setOnCheckedChangeListener(new MyListener2());
        mSwitchRing.setOnCheckedChangeListener(new MyListener2());
    }

    @Override
    protected void initData() {
        updateRingSelect();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.f_alarm_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void notConnected() {
                ToastUtils.showShort(R.string.device_not_connected);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_BIND:
                    updateBindStatus(true);
                    break;
                case REQUEST_CODE_RING:
                    //更新用户选择闹钟铃声
                    updateRingSelect();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSmaManager.removeSmaCallback(mSmaCallback);
    }

    @OnClick({R.id.bt_switch_alarm, R.id.bt_switch_vibration,
            R.id.switch_ring, R.id.bt_scale_one, R.id.bt_scale_two,
            R.id.bt_scale_three, R.id.bt_bind_device, R.id.tv_ring_name})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_switch_alarm:
                break;
            case R.id.bt_switch_vibration:
                break;
            case R.id.switch_ring:

                break;
            case R.id.tv_ring_name:
                startActivityForResult(new Intent(mContext, RingSet.class), REQUEST_CODE_RING);
                break;
            case R.id.bt_scale_one:
                setScoreRatio(1);
                break;
            case R.id.bt_scale_two:
                setScoreRatio(2);
                break;
            case R.id.bt_scale_three:
                setScoreRatio(3);
                break;
            case R.id.bt_bind_device:
                if (TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0])) {
                    startActivityForResult(new Intent(mContext, ConnectDevice.class), REQUEST_CODE_BIND);
                } else {
                    DialogUtil.defaultDialog(mContext, getString(R.string.confirm_unbind_device), null, null, new
                            DialogCallback() {

                                @Override
                                public void execute(Object dialog, Object content) {
                                    //确认解绑
                                    mSmaManager.unbind();
                                    updateBindStatus(false);
                                }
                            });
                }
                break;
        }
    }

    protected void updateRingSelect() {
        int ringNum = PreferencesUtils.getInt(mContext, MyConstants.GOAL_VOICE_MODE, 0);
        mSmaManager.write(SmaManager.SET.GOAL_VOICE_MODE, Tools.parseInt(ringNum));
        updateRingUI(ringNum);
    }

    private void updateRingUI(int ringNum) {
        if (ringNum == 0) {
            mTvRingName.setText(R.string.default_ring);
        } else {
            mTvRingName.setText(String.format("%s%s", getString(R.string.ring), valueOf(ringNum)));
        }
    }

    private void setScoreRatio(int ratio) {
        mSmaManager.write(SmaManager.SET.GOAL_SCORE_RATIO, Tools.parseInt(ratio));
        if (!mSmaManager.isConnected) return;

        PreferencesUtils.putInt(mContext, MyConstants.GOAL_SCORE_RATIO, ratio);
        updateRatioUI(ratio);
    }

    private void updateRatioUI(int ratio) {
        switch (ratio) {
            case 1:
                clearSelected();
                mBtScaleOne.setBackgroundResource(R.drawable.shape_white_rectangle_left);
                mBtScaleOne.setTextColor(ContextCompat.getColor(mContext, R.color.black_1));
                break;
            case 2:
                clearSelected();
                mBtScaleTwo.setBackgroundResource(R.drawable.shape_white_rectangle_middle);
                mBtScaleTwo.setTextColor(ContextCompat.getColor(mContext, R.color.black_1));
                break;
            case 3:
                clearSelected();
                mBtScaleThree.setBackgroundResource(R.drawable.shape_white_rectangle_right);
                mBtScaleThree.setTextColor(ContextCompat.getColor(mContext, R.color.black_1));
                break;
        }
    }

    private void clearSelected() {
        mBtScaleOne.setBackgroundResource(R.drawable.shape_white_tran_rectangle_left);
        mBtScaleOne.setTextColor(ContextCompat.getColor(mContext, R.color.white_1));

        mBtScaleTwo.setBackgroundResource(R.drawable.shape_white_tran_rectangle_middle);
        mBtScaleTwo.setTextColor(ContextCompat.getColor(mContext, R.color.white_1));

        mBtScaleThree.setBackgroundResource(R.drawable.shape_white_tran_rectangle_right);
        mBtScaleThree.setTextColor(ContextCompat.getColor(mContext, R.color.white_1));
    }

    private void updateBindStatus(boolean bind) {
        if (bind) {
            mTvBindStatus.setText(mSmaManager.getNameAndAddress()[0]);
            mBtBindDevice.setText(R.string.unbind);
        } else {
            mTvBindStatus.setText(R.string.no_bind_device);
            mBtBindDevice.setText(R.string.bind);
        }
    }

//    private void updateAlarmTime() {
//        if (TextUtils.isEmpty(hour)) {
//            hour = "00";
//        }
//        if (hour.length() == 1) {
//            hour = "0" + hour;
//        }
//        if (TextUtils.isEmpty(min)) {
//            min = "00";
//        }
//        mTvAlarmTime.setText(String.format("%s:%s", hour, min));
//    }

    private void setAlarm() {
        String time = Tools.parseTime(hour + am * 12, min);
        mSmaManager.write(SmaManager.SET.ALARM_OCLOCK, time);
        if (mSmaManager.isConnected) {
            int oclock = ((hour + am * 12) << 8) | min;
            PreferencesUtils.putInt(mContext, MyConstants.ALARM_OCLOCK, oclock);
        }
    }

    private class MyListener implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldValue, int newValue) {
            if (picker == mNumPickerHour) {
                hour = newValue;
            } else if (picker == mNumPickerMin) {
                min = newValue;
            } else if (picker == mNumPickerAmPm) {
                am = newValue;
            }
            setAlarm();
        }
    }

    private class MyListener2 implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton button, boolean checked) {
            if (button == mBtSwitchAlarm) {
                if (checked) {
                    mSmaManager.write(SmaManager.SET.ENABLE_ALARM);
                } else {
                    mSmaManager.write(SmaManager.SET.DISABLE_ALARM);
                }
            } else if (button == mBtSwitchVibration) {
                if (checked) {
                    mSmaManager.write(SmaManager.SET.ENABLE_GOAL_LIGHT);
                } else {
                    mSmaManager.write(SmaManager.SET.DISABLE_GOAL_LIGHT);
                }
            } else if (button == mSwitchRing) {
                if (checked) {
                    mSmaManager.write(SmaManager.SET.ENABLE_GOAL_VOICE);
                } else {
                    mSmaManager.write(SmaManager.SET.DISABLE_GOAL_VOICE);
                }
            }

            if (mSmaManager.isConnected) {
                if (button == mBtSwitchAlarm) {
                    PreferencesUtils.putBoolean(mContext, MyConstants.ALARM_ENABLED, checked);
                } else if (button == mBtSwitchVibration) {
                    PreferencesUtils.putBoolean(mContext, MyConstants.GOAL_LIGHT_ENABLED, checked);
                } else if (button == mSwitchRing) {
                    PreferencesUtils.putBoolean(mContext, MyConstants.GOAL_VOICE_ENABLED, checked);
                }
            } else {
                button.setChecked(!checked);
            }
        }
    }
}
