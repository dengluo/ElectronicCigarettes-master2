package bauway.com.electroniccigarettes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.common.MyConstants2;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/8/11.
 * 账户信息
 */

public class AppUserInfoActivity extends BaseActivity {
    @BindView(R.id.action_back)
    ImageView action_back;
    @BindView(R.id.textView2)
    TextView mTextView2;
    @BindView(R.id.tv_user_email)
    TextView mTvUserEmail;
    @BindView(R.id.textView3)
    TextView mTextView3;
    @BindView(R.id.tv_product_name)
    TextView mTvProductName;
    @BindView(R.id.bt_change_pwd)
    Button mBtChangePwd;

    @Override
    protected int getLayoutRes() {
        return R.layout.app_user_info_activity;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        String email = userRxPreferences.getString(MyConstants2.LOGIN_EMAIL).get();
        if (TextUtils.isEmpty(email)) {
            mTvUserEmail.setText(R.string.no_set);
        } else {
            mTvUserEmail.setText(email);
        }
        String productName = userRxPreferences.getString(MyConstants2.SELECT_PRODUCT_NAME).get();
        if (TextUtils.isEmpty(productName)) {
            mTvProductName.setText(R.string.no_set);
        } else {
            mTvProductName.setText(productName);
        }

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @OnClick({R.id.action_back, R.id.bt_change_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                this.finish();
                break;
            case R.id.bt_change_pwd:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
        }
    }
}
