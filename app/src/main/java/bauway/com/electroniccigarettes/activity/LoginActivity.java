package bauway.com.electroniccigarettes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.bean.User;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.util.DateUtils;
import bauway.com.electroniccigarettes.util.DialogUtil;
import bauway.com.electroniccigarettes.util.PreferencesUtils;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaotaotao on 2017/8/9.
 * 登录
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_login_email)
    EditText mEtLoginEmail;
    @BindView(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @BindView(R.id.bt_login)
    Button mBtLogin;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.bt_forget_pwd)
    TextView mBtForgetPwd;

    @Override
    protected int getLayoutRes() {
        return R.layout.login_activity;
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
        String emailHistory = userRxPreferences.getString(MyConstants2.LOGIN_EMAIL).get();
        if (!TextUtils.isEmpty(emailHistory)) {
            mEtLoginEmail.setTag(emailHistory);
            mEtLoginEmail.setSelection(mEtLoginEmail.getText().toString().length());
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_login, R.id.bt_register, R.id.bt_forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.bt_forget_pwd:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
        }
    }

    private void login() {
        final String email = mEtLoginEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showShort(R.string.plz_input_email);
            return;
        }
        String pwd = mEtLoginPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort(R.string.plz_input_pwd);
        }
        DialogUtil.progressDialog(mContext, getString(R.string.login_now), false);

        BmobUser.loginByAccount(email, pwd, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (user.getEmailVerified()) {
                        //存储用户账号信息
                        userRxPreferences.getString(MyConstants2.LOGIN_EMAIL).set(email);
                        PreferencesUtils.putEntity(mContext, user);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        LoginActivity.this.finish();
                    } else {
                        //邮箱未验证，提醒用户验证，同时判断账号更新时间，如果超过10分钟，则再次发送验证邮件
                        ToastUtils.showShort(R.string.email_need_check);
                        long timeSpan = TimeUtils.getTimeSpan(System.currentTimeMillis(), DateUtils.getServiceDate(user.getUpdatedAt()), TimeConstants.MSEC);
                        if (timeSpan > (1000 * 60 * 10)) {
                            BmobUser.requestEmailVerify(user.getEmail(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {

                                }
                            });
                        }
                    }
                } else {
                    LogUtils.d(e.getErrorCode() + ":" + e.getMessage());
                    ToastUtils.showShort(R.string.email_pwd_input_err);
                }
                DialogUtil.hide();
            }
        });
    }
}
