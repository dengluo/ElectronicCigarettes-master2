package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.bean.User;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhaotaotao on 2017/8/10.
 * 注册
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_register_email)
    EditText mEtRegisterEmail;
    @BindView(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @BindView(R.id.et_register_pwd_again)
    EditText mEtRegisterPwdAgain;
    @BindView(R.id.bt_register)
    Button mBtRegister;
    @BindView(R.id.bt_return)
    ImageButton mBtReturn;

    @Override
    protected int getLayoutRes() {
        return R.layout.register_activity;
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
            mEtRegisterEmail.setTag(emailHistory);
            mEtRegisterEmail.setSelection(mEtRegisterEmail.getText().toString().length());
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_return, R.id.bt_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_register:
                register();
                break;
        }
    }

    private void register() {
        final String email = mEtRegisterEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showShort(R.string.plz_input_email);
            return;
        }
        String pwd = mEtRegisterPwd.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShort(R.string.plz_input_pwd);
            return;
        }
        String pwdAgain = mEtRegisterPwdAgain.getText().toString().trim();
        if (TextUtils.isEmpty(pwdAgain)) {
            ToastUtils.showShort(R.string.plz_input_pwd_again);
            return;
        }
        if (!pwd.equals(pwdAgain)) {
            ToastUtils.showShort(R.string.pwd_input_diff);
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
                    ToastUtils.showShort(R.string.register_success_plz_check_email);
                    userRxPreferences.getString(MyConstants2.LOGIN_EMAIL).set(email);
                    RegisterActivity.this.finish();
                } else {
                    LogUtils.d(e.getErrorCode() + ":" + e.getMessage());
                    if (203 == e.getErrorCode()) {
                        ToastUtils.showShort(R.string.email_already_register);
                    } else {
                        ToastUtils.showShort(R.string.register_failure);
                    }
                }
                DialogUtil.hide();
            }
        });
    }
}
