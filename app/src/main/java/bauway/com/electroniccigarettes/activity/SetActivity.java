package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.adapter.SpinnerAdapter;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.common.MyConstants2;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 设定
 */

public class SetActivity extends BaseActivity {

    @BindView(R.id.action_back)
    ImageView mBtReturn1;
    @BindView(R.id.relativeLayout1)
    RelativeLayout mRelativeLayout1;
    @BindView(R.id.bt_user_number_subtract)
    ImageButton mBtUserNumberSubtract;
    @BindView(R.id.et_set_use_number)
    EditText mEtSetUseNumber;
    @BindView(R.id.bt_user_number_add)
    ImageButton mBtUserNumberAdd;
    @BindView(R.id.textView4)
    TextView mTextView4;
    @BindView(R.id.bt_loop_number_subtract)
    ImageButton mBtLoopNumberSubtract;
    @BindView(R.id.et_set_loop_number_plan)
    EditText mEtSetLoopNumberPlan;
    @BindView(R.id.bt_loop_number_add)
    ImageButton mBtLoopNumberAdd;
    @BindView(R.id.bt_temp_subtract)
    ImageButton mBtTempSubtract;
    @BindView(R.id.et_temp)
    EditText mEtTemp;
    @BindView(R.id.bt_temp_add)
    ImageButton mBtTempAdd;
    @BindView(R.id.bt_time_subtract)
    ImageButton mBtTimeSubtract;
    @BindView(R.id.et_time)
    EditText mEtTime;
    @BindView(R.id.bt_time_add)
    ImageButton mBtTimeAdd;
    @BindView(R.id.et_price)
    EditText mEtPrice;
    @BindView(R.id.spinner_money_select)
    Spinner mSpinnerMoneySelect;
    @BindView(R.id.bt_switch_shake)
    SwitchCompat mBtSwitchShake;
//    @BindView(R.id.bt_save_set)
//    Button mBtSaveSet;
    @BindView(R.id.main_content)
    ConstraintLayout mMainContent;
    private SpinnerAdapter mSpinnerAdapter;
    @BindArray(R.array.money_type)
    public String[] mStrings;

    private Integer mPuffPerDay;
    private Integer mPuffPerLoop;
    private Integer mTemperature;
    private Integer mDurationPerLoop;
//    private int mPricePerPacket;
//    private int mCurrencyType;
//    private boolean isVibrationEnabled;

    private SmaManager mSmaManager;

    @Override
    protected int getLayoutRes() {
        return R.layout.set_activity;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mEtSetUseNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtils.showShort(R.string.parameter_not_null);
                } else {
                    mPuffPerDay = Integer.valueOf(temp);
                    if (mPuffPerDay < MyConstants2.USE_NUM_MIN || mPuffPerDay > MyConstants2.USE_NUM_MAX) {
                        //超出范围的值，不下发指令
                        ToastUtils.showShort(getString(R.string.every_day_use_num_plan)
                                + getString(R.string.use_num_plan_suggest)
                                + MyConstants2.USE_NUM_MIN + "~" + MyConstants2.USE_NUM_MAX
                                + getString(R.string.between));
                    } else {
                        userRxPreferences.getInteger(MyConstants2.SP_PUFF_PER_DAY).set(mPuffPerDay);
                    }
                }
            }
        });

        mEtSetLoopNumberPlan.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }

                String temp = s.toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtils.showShort(R.string.parameter_not_null);
                } else {
                    mPuffPerLoop = Integer.valueOf(temp);
                    if (mPuffPerLoop < MyConstants2.LOOP_NUM_MIN || mPuffPerLoop > MyConstants2.LOOP_NUM_MAX) {
                        //超出范围的值，不下发指令
                        ToastUtils.showShort(getString(R.string.loop_use_num_plan)
                                + getString(R.string.use_num_plan_suggest)
                                + MyConstants2.LOOP_NUM_MIN + "~" + MyConstants2.LOOP_NUM_MAX
                                + getString(R.string.between));
                    } else {
                        userRxPreferences.getInteger(MyConstants2.SP_PUFF_PER_LOOP).set(mPuffPerLoop);
                        mSmaManager.write(SmaManager.SET.PUFF_PER_LOOP, String.valueOf(mPuffPerLoop));
                    }
                }
            }
        });

        mEtTemp.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }

                String temp = s.toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtils.showShort(R.string.parameter_not_null);
                } else {
                    mTemperature = Integer.valueOf(temp);
                    if (mTemperature < MyConstants2.TEMP_MIN || mTemperature > MyConstants2.TEMP_MAX) {
                        //超出范围的值，不下发指令
                        ToastUtils.showShort(getString(R.string.temp_set)
                                + getString(R.string.use_num_plan_suggest)
                                + MyConstants2.TEMP_MIN + "~" + MyConstants2.TEMP_MAX
                                + getString(R.string.between));
                    } else {
                        userRxPreferences.getInteger(MyConstants2.SP_TEMPERATURE).set(mTemperature);
                        mSmaManager.write(SmaManager.SET.TEMPERATURE, String.valueOf(mTemperature));
                    }
                }
            }
        });

        mEtTime.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }

                String temp = s.toString();
                if (TextUtils.isEmpty(temp)) {
                    ToastUtils.showShort(R.string.parameter_not_null);
                } else {
                    mDurationPerLoop = Integer.valueOf(temp);
                    if (mDurationPerLoop < MyConstants2.TIME_MIN || mDurationPerLoop > MyConstants2.TIME_MAX) {
                        //超出范围的值，不下发指令
                        ToastUtils.showShort(getString(R.string.work_time_set)
                                + getString(R.string.use_num_plan_suggest)
                                + MyConstants2.TIME_MIN + "~" + MyConstants2.TIME_MAX
                                + getString(R.string.between));
                    } else {
                        userRxPreferences.getInteger(MyConstants2.SP_DURATION_PER_LOOP).set(mDurationPerLoop);
                        mSmaManager.write(SmaManager.SET.TIME_PER_LOOP, String.valueOf(mDurationPerLoop));
                    }
                }
            }
        });

        mEtPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    ToastUtils.showShort(R.string.parameter_not_null);
                } else {
                    int price = Integer.parseInt(editable.toString());
                    userRxPreferences.getInteger(MyConstants2.SP_PRICE_PER_PACKET).set(price);
                }
            }
        });

        mSpinnerMoneySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userRxPreferences.getInteger(MyConstants2.SP_CURRENCY_TYPE).set(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mBtSwitchShake.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }

                mSmaManager.write(b ? SmaManager.SET.VIBRATION_ENABLED : SmaManager.SET.VIBRATION_DISABLED);
                userRxPreferences.getBoolean(MyConstants2.SP_IS_VIBRATION_ENABLED).set(b);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mEtSetUseNumber.setText(String.valueOf(mPuffPerDay = userRxPreferences.getInteger(MyConstants2.SP_PUFF_PER_DAY,
                MyConstants2.DEFAULT_PUFF_PER_DAY).get()));
        mEtSetLoopNumberPlan.setText(String.valueOf(mPuffPerLoop = userRxPreferences.getInteger(MyConstants2
                .SP_PUFF_PER_LOOP, MyConstants2.DEFAULT_PUFF_PER_LOOP).get()));
        mEtTemp.setText(String.valueOf(mTemperature = userRxPreferences.getInteger(MyConstants2.SP_TEMPERATURE,
                MyConstants2.DEFAULT_TEMPERATURE).get()));
        mEtTime.setText(String.valueOf(mDurationPerLoop = userRxPreferences.getInteger(MyConstants2.SP_DURATION_PER_LOOP,
                MyConstants2.DEFAULT_DURATION_PER_LOOP).get()));
        mEtPrice.setText(String.valueOf(/*mPricePerPacket =*/ userRxPreferences.getInteger(MyConstants2.SP_PRICE_PER_PACKET,
                MyConstants2.DEFAULT_PRICE_PER_PACKET).get()));

        mSpinnerAdapter = new SpinnerAdapter(mContext, mStrings);
        mSpinnerMoneySelect.setAdapter(mSpinnerAdapter);
        mSpinnerMoneySelect.setSelection(/*mCurrencyType =*/ userRxPreferences.getInteger(MyConstants2.SP_CURRENCY_TYPE,
                MyConstants2.DEFAULT_CURRENCY_TYPE).get());

        mBtSwitchShake.setChecked(/*isVibrationEnabled =*/ userRxPreferences.getBoolean(MyConstants2.SP_IS_VIBRATION_ENABLED,
                MyConstants2.DEFAULT_VIBRATION_ENABLED).get());
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance();
    }

    @OnClick({R.id.action_back, R.id.bt_user_number_subtract, R.id.bt_user_number_add,
            R.id.bt_loop_number_subtract, R.id.bt_loop_number_add, R.id.bt_temp_subtract,
            R.id.bt_temp_add, R.id.bt_time_subtract, R.id.bt_time_add, /*R.id.bt_save_set,*/
            R.id.btn_ota})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                this.finish();
                break;
            case R.id.bt_user_number_subtract:
                if (getUserNumData()) return;
                if (mPuffPerDay > MyConstants2.USE_NUM_MIN) {
                    mPuffPerDay--;
                    mEtSetUseNumber.setText(String.valueOf(mPuffPerDay));
                }
                break;
            case R.id.bt_user_number_add:
                if (getUserNumData()) return;
                if (mPuffPerDay < MyConstants2.USE_NUM_MAX) {
                    mPuffPerDay++;
                    mEtSetUseNumber.setText(String.valueOf(mPuffPerDay));
                }
                break;
            case R.id.bt_loop_number_subtract:
                if (getLoopData()) return;
                if (mPuffPerLoop > MyConstants2.LOOP_NUM_MIN) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        return;
                    }

                    mPuffPerLoop--;
                    mEtSetLoopNumberPlan.setText(String.valueOf(mPuffPerLoop));
                }
                break;
            case R.id.bt_loop_number_add:
                if (getLoopData()) return;
                if (mPuffPerLoop < MyConstants2.LOOP_NUM_MAX) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        return;
                    }

                    mPuffPerLoop++;
                    mEtSetLoopNumberPlan.setText(String.valueOf(mPuffPerLoop));
                }
                break;
            case R.id.bt_temp_subtract:
                if (getTempData()) return;

                if (mTemperature > MyConstants2.TEMP_MIN) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        return;
                    }

                    mTemperature--;
                    mEtTemp.setText(String.valueOf(mTemperature));
                }
                break;
            case R.id.bt_temp_add:
                if (getTempData()) return;

                if (mTemperature < MyConstants2.TEMP_MAX) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        return;
                    }

                    mTemperature++;
                    mEtTemp.setText(String.valueOf(mTemperature));
                }
                break;
            case R.id.bt_time_subtract:
                if (getTime()) return;

                if (mDurationPerLoop > MyConstants2.TIME_MIN + 5) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        return;
                    }
                    mDurationPerLoop -= 5;
                    mEtTime.setText(String.valueOf(mDurationPerLoop));
                }
                break;
            case R.id.bt_time_add:
                if (getTime()) return;

                if (mDurationPerLoop < MyConstants2.TIME_MAX - 5) {
                    if (!mSmaManager.isConnected) {
                        ToastUtils.showShortSafe(R.string.device_not_connected);
                        return;
                    }

                    mDurationPerLoop += 5;
                    mEtTime.setText(String.valueOf(mDurationPerLoop));
                }
                break;
            case R.id.btn_ota:
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }

                mSmaManager.write(SmaManager.SET.INTO_OTA);
                break;
        }
    }

    private boolean getTime() {
        String time = mEtTime.getText().toString().trim();
        if (TextUtils.isEmpty(time)) {
            ToastUtils.showShort(R.string.parameter_not_null);
            return true;
        }
        mDurationPerLoop = Integer.valueOf(time);
        return false;
    }

    private boolean getTempData() {
        String temp = mEtTemp.getText().toString().trim();
        if (TextUtils.isEmpty(temp)) {
            ToastUtils.showShort(R.string.parameter_not_null);
            return true;
        }
        mTemperature = Integer.valueOf(temp);
        return false;
    }

    private boolean getLoopData() {
        String loopNum = mEtSetLoopNumberPlan.getText().toString().trim();
        if (TextUtils.isEmpty(loopNum)) {
            ToastUtils.showShort(R.string.parameter_not_null);
            return true;
        }
        mPuffPerLoop = Integer.valueOf(loopNum);
        return false;
    }

    private boolean getUserNumData() {
        String userNum = mEtSetUseNumber.getText().toString().trim();
        if (TextUtils.isEmpty(userNum)) {
            ToastUtils.showShort(R.string.parameter_not_null);
            return true;
        }
        mPuffPerDay = Integer.valueOf(userNum);
        return false;
    }
}
