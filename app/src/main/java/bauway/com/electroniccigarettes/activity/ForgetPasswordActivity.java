package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaotaotao on 2017/8/10.
 * 找回密码
 */

public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.et_reset_email)
    EditText mEtResetEmail;
    @BindView(R.id.bt_reset)
    Button mBtReset;
    @BindView(R.id.bt_return)
    ImageButton mBtReturn;

    @Override
    protected int getLayoutRes() {
        return R.layout.forget_password_activity;
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
        String loginEmail = userRxPreferences.getString(MyConstants2.LOGIN_EMAIL).get();
        if (!TextUtils.isEmpty(loginEmail)) {
            mEtResetEmail.setText(loginEmail);
            mEtResetEmail.setSelection(mEtResetEmail.getText().toString().length());
        }


    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_return, R.id.bt_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_reset:
                resetPwd();
                break;
        }
    }

    private void resetPwd() {
        final String email = mEtResetEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtils.showShort(R.string.plz_input_email);
            return;
        }
        DialogUtil.progressDialog(mContext, getString(R.string.send_reset_pwd_info), false);
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(R.string.already_send_reset_email);
                    userRxPreferences.getString(MyConstants2.LOGIN_EMAIL).set(email);
                    ForgetPasswordActivity.this.finish();
                } else {
                    LogUtils.d(e.getErrorCode() + "." + e.getMessage());
                    ToastUtils.showShort(R.string.email_address_err);
                }
                DialogUtil.hide();
            }
        });
    }
}
