package com.bauway.alarm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.activity.AboutUS;
import com.bauway.alarm.activity.ChangePassword;
import com.bauway.alarm.activity.DeviceSelector;
import com.bauway.alarm.activity.FeedBack;
import com.bauway.alarm.activity.Login;
import com.bauway.alarm.base.BaseFragment;
import com.bauway.alarm.bean.User;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.interfaces.DialogCallback;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.PreferencesUtils;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by zhaotaotao on 2017/12/14.
 */

public class MoreSet extends BaseFragment {

    @BindView(R.id.iv_user)
    ImageView mIvUser;
    @BindView(R.id.tv_user_account)
    TextView mTvUserAccount;
    @BindView(R.id.bt_user_info)
    RelativeLayout mBtUserInfo;
    @BindView(R.id.bt_use_help)
    RelativeLayout mBtUseHelp;
    @BindView(R.id.tv_version_num)
    TextView mTvVersionNum;
    @BindView(R.id.bt_about_us)
    RelativeLayout mBtAboutUs;
    @BindView(R.id.bt_switch_device)
    RelativeLayout mBtSwitchDevice;
    @BindView(R.id.bt_logout)
    Button mBtLogout;

    public static MoreSet newInstance() {
        return new MoreSet();
    }

    @Override
    protected void initView(View mView) {
        mTvVersionNum.setText(String.valueOf("v" + AppUtils.getAppVersionName()));
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        mTvUserAccount.setText(user.getUsername());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.f_more_set;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }


    @OnClick({R.id.bt_user_info, R.id.bt_use_help,
            R.id.bt_about_us, R.id.bt_switch_device, R.id.bt_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_user_info:
                startActivity(new Intent(mContext, ChangePassword.class));
                break;
            case R.id.bt_use_help:
                startActivity(new Intent(mContext, FeedBack.class));
                break;
            case R.id.bt_about_us:
                startActivity(new Intent(mContext, AboutUS.class));
                break;
            case R.id.bt_switch_device:
                DialogUtil.defaultDialog(mContext, getString(R.string.confirm_unbind_device), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //确认解绑
                                SmaManager.getInstance().unbind();
                                PreferencesUtils.putInt(mContext, MyConstants.DEVICE_TYPE, -1);
                                startActivity(new Intent(getActivity(), DeviceSelector.class));
                                getActivity().finish();
                            }
                        });
                break;
            case R.id.bt_logout:
                closeApp();
                startActivity(new Intent(mContext, Login.class));
                getActivity().finish();
                break;
        }
    }

}
