package com.bauway.alarm.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.bean.User;
import com.bauway.alarm.util.DialogUtil;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaotaotao on 2017/12/15.
 */

public class ChangePassword extends BaseActivity {

    @BindView(R.id.bt_return)
    Button mBtReturn;
    @BindView(R.id.et_login_pwd)
    TextInputEditText mEtLoginPwd;
    @BindView(R.id.et_login_new_pwd)
    TextInputEditText mEtLoginNewPwd;
    @BindView(R.id.et_login_new_pwd_again)
    TextInputEditText mEtLoginNewPwdAgain;
    @BindView(R.id.bt_save_pwd)
    Button mBtSavePwd;

    @Override
    protected int getLayoutRes() {
        return R.layout.change_pwd;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mEtLoginNewPwdAgain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    //完成
                    onViewClicked(mBtSavePwd);
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


    @OnClick({R.id.bt_return, R.id.bt_save_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_save_pwd:
                String oldPwd = mEtLoginPwd.getText().toString();
                if (TextUtils.isEmpty(oldPwd)) {
                    ToastUtils.showShort(R.string.plz_input_pwd);
                    return;
                }
                String newPwd = mEtLoginNewPwd.getText().toString();
                if (TextUtils.isEmpty(newPwd)) {
                    ToastUtils.showShort(R.string.input_new_pwd);
                    return;
                }
                String newPwdAgain = mEtLoginNewPwdAgain.getText().toString();
                if (TextUtils.isEmpty(newPwdAgain)) {
                    ToastUtils.showShort(R.string.input_new_pwd_again);
                    return;
                }
                if (!newPwd.equals(newPwdAgain)) {
                    ToastUtils.showShort(R.string.new_pwd_discord);
                    return;
                }
                DialogUtil.progressDialog(mContext, getString(R.string.modify_now), false);
                BmobUser bmobUser = new BmobUser();
                bmobUser.setPassword(newPwd);
                User user = BmobUser.getCurrentUser(User.class);
                bmobUser.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtils.showShort(R.string.pwd_modify_success);
                            ChangePassword.this.finish();
                        } else {
                            ToastUtils.showShort(R.string.pwd_modify_failure);
                            LogUtils.d(e.toString());
                        }
                        DialogUtil.hide();
                    }
                });
                break;
        }
    }
}
