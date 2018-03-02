package com.bauway.alarm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.activity.ConnectDevice;
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

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/12/14.
 * <p>
 * 防丢器设置
 */

public class TrackerSet extends BaseFragment {
    public static final int REQUEST_CODE_BIND = 1;
    public static final int REQUEST_PICK_RING = 2;

    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.line1)
    View           mLine1;
    @BindView(R.id.num_picker_hour)
    MyNumberPicker mNumPickerHour;
    @BindView(R.id.num_picker_hour_unit)
    MyNumberPicker mNumPickerHourUnit;
    @BindView(R.id.num_picker_min)
    MyNumberPicker mNumPickerMin;
    @BindView(R.id.num_picker_min_unit)
    MyNumberPicker mNumPickerMinUnit;
    @BindView(R.id.device_voltage)
    TextView       mDeviceVoltage;
    @BindView(R.id.bt_switch_vibration)
    SwitchCompat   mBtSwitchVibration;
    @BindView(R.id.bt_switch)
    LinearLayout   mBtSwitch;
    @BindView(R.id.alarm_ring)
    TextView       mTvAlarmRing;
    @BindView(R.id.switch_alarm)
    SwitchCompat   mSwitchAlarm;
    @BindView(R.id.tv_bind_status)
    TextView       mTvBindStatus;
    @BindView(R.id.bt_bind_device)
    TextView       mBtBindDevice;
    @BindView(R.id.tv_calibration)
    TextView tv_calibration;

    private String[] num, hourMinutes;

    private SmaManager  mSmaManager;
    private SmaCallback mSmaCallback;

    private int hour;
    private int min;
    private Float f1 = 0.0f;
    private Float f2 = 0.0f;

    public static TrackerSet newInstance() {
        return new TrackerSet();
    }

    private Ringtone mRingtone;
    private Vibrator mVibrator = null;

    @Override
    protected void initView(View mView) {
        num = Tools.getNum(0, 100);
        mNumPickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerHour.setDisplayedValues(num);
        mNumPickerHour.setMinValue(0);
        mNumPickerHour.setMaxValue(num.length - 1);
        mNumPickerHour.setValue(0);
        mNumPickerHour.setWrapSelectorWheel(true);

        mNumPickerHourUnit.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerHourUnit.setDisplayedValues(new String[]{"Hour"});
        mNumPickerHourUnit.setMinValue(0);
        mNumPickerHourUnit.setWrapSelectorWheel(false);

        hourMinutes = Tools.getHourMinutes();
        mNumPickerMin.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerMin.setDisplayedValues(hourMinutes);
        mNumPickerMin.setMinValue(0);
        mNumPickerMin.setMaxValue(hourMinutes.length - 1);
        mNumPickerMin.setValue(0);
        mNumPickerMin.setWrapSelectorWheel(true);

        mNumPickerMinUnit.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mNumPickerMinUnit.setDisplayedValues(new String[]{"min"});
        mNumPickerMinUnit.setMinValue(0);
        mNumPickerMinUnit.setWrapSelectorWheel(false);

        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerHour);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerHourUnit);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerMin);
        MyNumberPickerTools.setNumBerPickerStyle(mContext, mNumPickerMinUnit);

        mBtSwitchVibration.setChecked(PreferencesUtils.getBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT_VIBRATION));
        mSwitchAlarm.setChecked(PreferencesUtils.getBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT));
        String title = PreferencesUtils.getString(mContext, MyConstants.PHONE_ALERT_RING_TITLE);
        if (!TextUtils.isEmpty(title)) {
            mTvAlarmRing.setText(title);
        }
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

        mBtSwitchVibration.setOnCheckedChangeListener(new MyListener2());
        mSwitchAlarm.setOnCheckedChangeListener(new MyListener2());
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.f_tracker_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onReadVoltage(final float voltage) {
                mDeviceVoltage.post(new Runnable() {

                    @Override
                    public void run() {
                        mDeviceVoltage.setText(getString(R.string.voltage_num, String.valueOf(voltage)));
                    }
                });
            }

            @Override
            public void onReadRssi(float rssi) {
                f1 = rssi;
                Log.e("rssi:",rssi+"-");
                ring(f1 < f2);
                vibration(f1 < f2);
            }

            @Override
            public void onReadWaringRssi(float wrssi) {
                f2 = wrssi;
                Log.e("wrssi:",wrssi+"-");
            }
        });
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

    private void setAlarm() {
        if (mSmaManager.isConnected) {
            String time = Tools.parseTime2(hour, min);
            mSmaManager.write(SmaManager.SET.TIME, time);
//            int oclock = ((hour + am * 12) << 8) | min;
//            PreferencesUtils.putInt(mContext, MyConstants.ALARM_OCLOCK, oclock);
        }
    }

    private void ring(boolean action) {
        if (action) {
            if (PreferencesUtils.getBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT)) {
                String uri = PreferencesUtils.getString(mContext, MyConstants.PHONE_ALERT_RING_URI);
                if (TextUtils.isEmpty(uri)) {
                    uri = RingtoneManager.getActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_ALARM)
                                         .toString();
                }
                Ringtone ringtone = RingtoneManager.getRingtone(mContext, Uri.parse(uri));
                if (mRingtone == null || !mRingtone.getTitle(mContext).equals(ringtone.getTitle(mContext))) {
                    mRingtone = ringtone;
                }
                if (!mRingtone.isPlaying()) {
                    mRingtone.play();
                }
            }
        } else {
            if (mRingtone != null && mRingtone.isPlaying()) {
                mRingtone.stop();
            }
        }
    }

    private void vibration(boolean action) {
        if (action) {
            if (PreferencesUtils.getBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT)) {
                if (PreferencesUtils.getBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT_VIBRATION)) {
                    if (mVibrator == null) {
                        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    }
                    mVibrator.vibrate(new long[]{0, 1000}, 0);
                }
            }
        } else {
            if (mVibrator != null) {
                mVibrator.cancel();
            }
        }
    }

//    private Runnable4StopRing mRunnable4StopRing;
//
//    private class Runnable4StopRing implements Runnable {
//        @Override
//        public void run() {
//            stopRing();
//        }
//    }
//
//    private Runnable4StopVibration mRunnable4StopVibration;
//
//    private class Runnable4StopVibration implements Runnable {
//        @Override
//        public void run() {
//            stopVibration();
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_BIND:
                    updateBindStatus(true);
                    break;
                case REQUEST_PICK_RING:
                    Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    PreferencesUtils.putString(mContext, MyConstants.PHONE_ALERT_RING_URI, uri.toString());
                    Ringtone ringtone = RingtoneManager.getRingtone(mContext, uri);
                    PreferencesUtils.putString(mContext, MyConstants.PHONE_ALERT_RING_TITLE, ringtone.getTitle(mContext));
                    mTvAlarmRing.setText(ringtone.getTitle(mContext));
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSmaManager.removeSmaCallback(mSmaCallback);
        ring(false);
        vibration(false);
    }

    private class MyListener implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldValue, int newValue) {
            if (picker == mNumPickerHour) {
                hour = newValue;
            } else if (picker == mNumPickerMin) {
                min = newValue;
            }
            setAlarm();
        }
    }

    private class MyListener2 implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton button, boolean checked) {
            if (button == mBtSwitchVibration) {
                PreferencesUtils.putBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT_VIBRATION, checked);
            } else if (button == mSwitchAlarm) {
                PreferencesUtils.putBoolean(mContext, MyConstants.ENABLE_PHONE_ALERT, checked);
            }
        }
    }

    @OnClick({R.id.bt_switch_vibration, R.id.bt_switch,
            R.id.bt_bind_device, R.id.tv_calibration, R.id.switch_alarm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_switch_vibration:
                break;
            case R.id.bt_switch:
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
//                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置报警铃声");
                startActivityForResult(intent, REQUEST_PICK_RING);
                break;
            case R.id.switch_alarm:
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
            case R.id.tv_calibration:
                mSmaManager.write(SmaManager.SET.CALIBRATION);
                if (!mSmaManager.isConnected) return;
                break;
        }
    }
}
