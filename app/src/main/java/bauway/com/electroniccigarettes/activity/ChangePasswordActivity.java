package bauway.com.electroniccigarettes.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.bean.User;
import bauway.com.electroniccigarettes.util.DialogUtil;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 变更密码
 */

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.bt_return)
    ImageButton mBtReturn;
    @BindView(R.id.relativeLayout4)
    RelativeLayout mRelativeLayout4;
    @BindView(R.id.et_old_pwd)
    EditText mEtOldPwd;
    @BindView(R.id.textInputLayout)
    TextInputLayout mTextInputLayout;
    @BindView(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.textInputLayout1)
    TextInputLayout mTextInputLayout1;
    @BindView(R.id.et_new_pwd_again)
    EditText mEtNewPwdAgain;
    @BindView(R.id.textInputLayout2)
    TextInputLayout mTextInputLayout2;
    @BindView(R.id.bt_confirm)
    Button mBtConfirm;

    @Override
    protected int getLayoutRes() {
        return R.layout.change_pwd_activity;
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

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_return, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_confirm:
                String oldPwd = mEtOldPwd.getText().toString().trim();
                if (TextUtils.isEmpty(oldPwd)) {
                    ToastUtils.showShort(R.string.plz_input_old_pwd);
                    return;
                }
                String newPwd = mEtNewPwd.getText().toString().trim();
                if (TextUtils.isEmpty(newPwd)) {
                    ToastUtils.showShort(R.string.plz_input_new_pwd);
                    return;
                }
                String newPwdAgain = mEtNewPwdAgain.getText().toString().trim();
                if (TextUtils.isEmpty(newPwdAgain)) {
                    ToastUtils.showShort(R.string.plz_input_pwd_again);
                    return;
                }
                if (!newPwd.equals(newPwdAgain)) {
                    ToastUtils.showShort(R.string.pwd_input_diff);
                    return;
                }
                DialogUtil.progressDialog(mContext, getString(R.string.change_pwd_now), false);
                BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtils.showShort(R.string.change_pwd_success);
                            ChangePasswordActivity.this.finish();
                        } else {
                            ToastUtils.showShort(R.string.change_pwd_success);
                            LogUtils.d(e.getErrorCode() + "," + e.getMessage());
                        }
                        DialogUtil.hide();
                    }
                });
                BmobUser bmobUser = new BmobUser();
                bmobUser.setPassword(newPwd);
                User user = BmobUser.getCurrentUser(User.class);
                bmobUser.update(user.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            ToastUtils.showShort(R.string.change_pwd_success);
                            ChangePasswordActivity.this.finish();
                        } else {
                            ToastUtils.showShort(R.string.change_pwd_failure);
                            LogUtils.d(e.toString());
                        }
                        DialogUtil.hide();
                    }
                });
                break;
        }
    }
}
