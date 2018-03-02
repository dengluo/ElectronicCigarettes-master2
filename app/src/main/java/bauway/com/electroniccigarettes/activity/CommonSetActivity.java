package bauway.com.electroniccigarettes.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.common.MyConstants2;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 通用指数
 */

public class CommonSetActivity extends BaseActivity {

    @BindView(R.id.tv_loop_time)
    TextView mTvLoopTime;
    @BindView(R.id.tv_dcv)
    TextView mTvDcv;
    @BindView(R.id.tv_charge_count)
    TextView mTvChargeCount;
    @BindView(R.id.tv_charge_tv)
    TextView mTvChargeTv;
    @BindView(R.id.tv_temp)
    TextView mTvTemp;
    @BindView(R.id.tv_use_num)
    TextView mTvUseNum;
    @BindView(R.id.bt_switch)
    SwitchCompat mBtSwitch;
    @BindView(R.id.bt_app_user)
    Button mBtAppUser;

    private SmaManager mSmaManager;
    private SmaCallback mSmaCallback;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");

    @Override
    protected int getLayoutRes() {
        return R.layout.common_set_activity;
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
        mTvLoopTime.setText(String.valueOf(userRxPreferences.getInteger(MyConstants2.SP_DURATION_PER_LOOP, MyConstants2
                .DEFAULT_DURATION_PER_LOOP).get()));
        mTvChargeCount.setText(String.valueOf(userRxPreferences.getInteger(MyConstants2.SP_CHARGE_COUNT, 0).get()));

        updateChargeTime();
        mTvDcv.setText(String.valueOf(userRxPreferences.getFloat(MyConstants2.SP_LAST_VOLTAGE, 0f).get()));
        mTvTemp.setText(String.valueOf(userRxPreferences.getInteger(MyConstants2.SP_CURRENT_TEMPERATURE, MyConstants2
                .DEFAULT_TEMPERATURE).get()));
        mTvUseNum.setText(String.valueOf(userRxPreferences.getInteger(MyConstants2.SP_PUFF_COUNT, 0).get()));
        mBtSwitch.setChecked(userRxPreferences.getBoolean(MyConstants2.SP_IS_VIBRATION_ENABLED, MyConstants2
                .DEFAULT_VIBRATION_ENABLED).get());
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {

            }

            @Override
            public void onReadVoltage(final float voltage) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mTvDcv.setText(String.valueOf(voltage));
                    }
                });
            }

            @Override
            public void onCharging(final float voltage) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mTvDcv.setText(String.valueOf(voltage));
                        updateChargeTime();
                    }
                });
            }

            @Override
            public void onReadTemperature(final int temperature) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mTvTemp.setText(String.valueOf(temperature));
                    }
                });
            }

            @Override
            public void onReadPuffCount(final int puff) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mTvUseNum.setText(String.valueOf(puff));
                    }
                });
            }

            @Override
            public void onReadChargeCount(final int count) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mTvChargeCount.setText(String.valueOf(count));
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        mSmaManager.removeSmaCallback(mSmaCallback);
        super.onDestroy();
    }

    private long updateTime = 0L;

    private void updateChargeTime() {
        long now = System.currentTimeMillis();
//        L.w("updateChargeTime -> now = " + now + " , updateTime = " + updateTime);
        if (now - updateTime < 1000 * 60) return;

        updateTime = now;
        long start = userRxPreferences.getLong(MyConstants2.SP_CHARGING_START).get();
        long end = userRxPreferences.getLong(MyConstants2.SP_CHARGING_END).get();
//        L.w("updateChargeTime -> start = " + start + " , end = " + end);
        if (start != 0 && end != 0 && end > start) {
            mTvChargeTv.setText(mDateFormat.format(new Date(start)) + "-" + mDateFormat.format(new Date(end)));
        }
    }

    @OnClick({R.id.bt_return_1, R.id.bt_switch, R.id.bt_app_user})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return_1:
                this.finish();
                break;
            case R.id.bt_switch:
                break;
            case R.id.bt_app_user:
                startActivity(new Intent(this, AppUserInfoActivity.class));
                break;
        }
    }
}
