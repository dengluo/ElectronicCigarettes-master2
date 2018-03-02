package com.bauway.alarm.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.bean.User;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.PreferencesUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhaotaotao on 2017/12/15.
 */

public class Register extends BaseActivity {


    @BindView(R.id.bt_return)
    Button mBtReturn;
    @BindView(R.id.et_account_email)
    TextInputEditText mEtAccountEmail;
    @BindView(R.id.ll_email)
    LinearLayout mLlEmail;
    @BindView(R.id.et_account_phone)
    TextInputEditText mEtAccountPhone;
    @BindView(R.id.ll_phone)
    LinearLayout mLlPhone;
    @BindView(R.id.et_code)
    TextInputEditText mEtCode;
    @BindView(R.id.bt_get_code)
    TextView mBtGetCode;
    @BindView(R.id.ll_verify_code)
    LinearLayout mLlVerifyCode;
    @BindView(R.id.et_pwd)
    TextInputEditText mEtPwd;
    @BindView(R.id.ll_pwd)
    LinearLayout mLlPwd;
    @BindView(R.id.bt_switch_login_type)
    TextView mBtSwitchLoginType;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.title_name)
    TextView mTitleName;

    @Override
    protected int getLayoutRes() {
        return R.layout.register;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mEtPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //完成
                    onViewClicked(mBtRegister);
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mTitleName.setText(R.string.register);
        mBtRegister.setText(R.string.register);
        //默认为邮箱注册,调用切换方法时，采用反向指标
        switchRegisterType(2);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_return, R.id.bt_get_code, R.id.bt_switch_login_type, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                startLoginActivity();
                this.finish();
                break;
            case R.id.bt_get_code:
                getCode(mEtAccountPhone.getText().toString(), true);
                break;
            case R.id.bt_switch_login_type:
                switchRegisterType(PreferencesUtils.getInt(mContext, MyConstants.REGISTER_TYPE, 1));
                break;
            case R.id.bt_register:
                register();
                break;
        }
    }

    private void register() {
        int type = PreferencesUtils.getInt(mContext, MyConstants.REGISTER_TYPE, 1);
        if (MyConstants.REGISTER_EMAIL == type) {
            //邮箱注册
            final String email = mEtAccountEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                ToastUtils.showShort(R.string.plz_input_email);
                return;
            }
            if (!RegexUtils.isEmail(email)) {
                ToastUtils.showShort(R.string.plz_input_sure_email_address);
                return;
            }
            String pwd = mEtPwd.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showShort(R.string.plz_input_pwd);
                return;
            }
            DialogUtil.progressDialog(mContext, getString(R.string.register_now), false);
            User user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setPassword(pwd);
            user.setApp_name(AppUtils.getAppName());
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        ToastUtils.showShort(R.string.email_register_success);
                        PreferencesUtils.putString(mContext, MyConstants.LOGIN_EMAIL, email);
                        startLoginActivity();
                        Register.this.finish();
                    } else {
                        LogUtils.d(e.getErrorCode() + ":" + e.getMessage());
                        if (202 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.email_already_register);
                        } else {
                            ToastUtils.showShort(R.string.register_failure);
                        }
                        DialogUtil.hide();
                    }
                }
            });
        } else {
            //手机号注册
            final String phone = mEtAccountPhone.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                ToastUtils.showShort(R.string.plz_input_phone_num);
                return;
            }
            String pwd = mEtPwd.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                ToastUtils.showShort(R.string.plz_input_pwd);
                return;
            }
            String verifyCode = mEtCode.getText().toString();
            if (TextUtils.isEmpty(verifyCode)) {
                ToastUtils.showShort(R.string.plz_input_code);
                return;
            }
            DialogUtil.progressDialog(mContext, getString(R.string.register_now), false);
            User user = new User();
            user.setMobilePhoneNumber(phone);
            user.setUsername(phone);
            user.setPassword(pwd);
            user.setApp_name(AppUtils.getAppName());
            user.signOrLogin(verifyCode, new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        ToastUtils.showShort(R.string.register_success);
                        PreferencesUtils.putString(mContext, MyConstants.LOGIN_PHONE, phone);
                        startLoginActivity();
                        Register.this.finish();
                    } else {
                        LogUtils.d(e.getErrorCode() + ":" + e.getMessage());
                        if (202 == e.getErrorCode()) {
                            ToastUtils.showShort(R.string.phone_already_register);
                        } else {
                            ToastUtils.showShort(R.string.register_failure);
                        }
                    }
                    DialogUtil.hide();
                }
            });
        }
    }


    private void switchRegisterType(int type) {
        if (MyConstants.REGISTER_EMAIL == type) {
            //邮箱注册，切换为手机注册
            mBtSwitchLoginType.setText(R.string.email_register);
            mLlEmail.setVisibility(View.GONE);
            mLlPhone.setVisibility(View.VISIBLE);
            mLlPhone.requestFocus();
            mLlVerifyCode.setVisibility(View.VISIBLE);
            PreferencesUtils.putInt(mContext, MyConstants.REGISTER_TYPE, 2);

        } else {
            //手机注册，切换为邮箱注册
            mBtSwitchLoginType.setText(getString(R.string.phone_register));
            mLlPhone.setVisibility(View.GONE);
            mLlEmail.setVisibility(View.VISIBLE);
            mLlEmail.requestFocus();
            mLlVerifyCode.setVisibility(View.GONE);
            PreferencesUtils.putInt(mContext, MyConstants.REGISTER_TYPE, 1);
        }
    }


    @Override
    protected void updateCountDown(Long aLong) {
        super.updateCountDown(aLong);
        if (!this.isDestroyed()) {
            mBtGetCode.setText(String.format("%s%s", String.valueOf(aLong), getString(R.string.second)));
        }
    }

    @Override
    protected void updateCountDownEnd() {
        super.updateCountDownEnd();
        if (!this.isDestroyed()) {
            mBtGetCode.setText(R.string.get_verify_code);
        }
    }

}
