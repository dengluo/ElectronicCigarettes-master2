package com.bauway.alarm.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.bean.FeedBackInfo;
import com.bauway.alarm.util.DialogUtil;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by zhaotaotao on 2017/12/20.
 */

public class FeedBack extends BaseActivity {
    @BindView(R.id.bt_return)
    Button mBtReturn;
    @BindView(R.id.et_feed_back_content)
    EditText mEtFeedBackContent;
    @BindView(R.id.bt_submit)
    Button mBtSubmit;

    @Override
    protected int getLayoutRes() {
        return R.layout.feed_back;
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


    @OnClick({R.id.bt_return, R.id.bt_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_submit:
                String feedBack = mEtFeedBackContent.getText().toString().trim();
                if (TextUtils.isEmpty(feedBack)) {
                    ToastUtils.showShort(R.string.plz_input_feed_back);
                    return;
                }
                DialogUtil.progressDialog(mContext, getString(R.string.send_is_now), false);
                FeedBackInfo feedBackInfo = new FeedBackInfo(BmobUser.getCurrentUser().getObjectId(), feedBack);
                feedBackInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            ToastUtils.showShort(R.string.thanks_you_feed_back);
                            FeedBack.this.finish();
                        } else {
                            ToastUtils.showShort(R.string.fedd_back_send_failure);
                        }
                        DialogUtil.hide();
                    }
                });
                break;
        }
    }
}
