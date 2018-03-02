package com.bauway.alarm.activity;

import android.content.Intent;
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
import com.bauway.alarm.util.DateUtils;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.PreferencesUtils;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaotaotao on 2017/12/13.
 */

public class Login extends BaseActivity {
    @BindView(R.id.linearLayout1)
    LinearLayout mLinearLayout1;
    @BindView(R.id.et_account)
    TextInputEditText mEtAccount;
    @BindView(R.id.et_pwd)
    TextInputEditText mEtPwd;
    @BindView(R.id.bt_forget_pwd)
    TextView mBtForgetPwd;
    @BindView(R.id.linearLayout2)
    LinearLayout mLinearLayout2;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.linearLayout3)
    LinearLayout mLinearLayout3;

    @Override
    protected int getLayoutRes() {
        return R.layout.login;
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
                    onViewClicked(mBtLogin);
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

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferencesUtils.getInt(mContext, MyConstants.REGISTER_TYPE, 1)
                == MyConstants.REGISTER_EMAIL) {
            mEtAccount.setText(PreferencesUtils.getString(mContext, MyConstants.LOGIN_EMAIL, ""));
        } else {
            mEtAccount.setText(PreferencesUtils.getString(mContext, MyConstants.LOGIN_PHONE, ""));
        }
        mEtAccount.setSelection(mEtAccount.getText().length());
    }

    @OnClick({R.id.bt_forget_pwd,
            R.id.bt_login, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_forget_pwd:
                startActivity(new Intent(this, ForgetPassword.class));
                break;
            case R.id.bt_login:
                String account = mEtAccount.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    ToastUtils.showShort(R.string.plz_input_account);
                    return;
                }
                String pwd = mEtPwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    ToastUtils.showShort(R.string.plz_input_pwd);
                    return;
                }
                DialogUtil.progressDialog(mContext, getString(R.string.login_now), false);
                BmobUser.loginByAccount(account, pwd, new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (user != null) {
                            LogUtils.d("登录成功：" + user.getEmailVerified());
                            if (user.getEmailVerified() == null || user.getEmailVerified()) {
                                //邮箱已验证或者手机号登录
                                ToastUtils.showShort(R.string.login_success);
                                startActivity();
                            } else {
                                //邮箱未验证
                                BmobUser.logOut();
                                ToastUtils.showShort(R.string.plz_check_email);
                                //判断账号更新时间，如果超过1小时，则再次发送验证邮件
                                long timeSpan = TimeUtils.getTimeSpan(System.currentTimeMillis(), DateUtils.getServiceDate(user.getUpdatedAt()), TimeConstants.MSEC);
                                if (timeSpan > (1000 * 60 * 60)) {
                                    BmobUser.requestEmailVerify(user.getEmail(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            LogUtils.d("重新发送注册邮件");
                                        }
                                    });
                                }
                            }
                        } else {
                            LogUtils.d("登录失败：" + e.toString());
                            ToastUtils.showShort(R.string.login_failure);
                        }
                        DialogUtil.hide();
                    }
                });
                break;
            case R.id.bt_register:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void startActivity() {
        startActivity(new Intent(this, DeviceSelector.class));
        this.finish();
    }
}
