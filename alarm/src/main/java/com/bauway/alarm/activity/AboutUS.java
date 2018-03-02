package com.bauway.alarm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/12/15.
 */

public class AboutUS extends BaseActivity {
    @BindView(R.id.bt_return)
    Button mBtReturn;
    @BindView(R.id.bt_youtube)
    Button mBtYoutube;
    @BindView(R.id.bt_ins)
    Button mBtIns;
    @BindView(R.id.bt_facebook)
    ImageButton mBtFacebook;
    @BindView(R.id.bt_tw)
    ImageButton mBtTw;

    @Override
    protected int getLayoutRes() {
        return R.layout.about_us;
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


    @OnClick({R.id.bt_return, R.id.bt_youtube, R.id.bt_ins, R.id.bt_facebook, R.id.bt_tw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_youtube:
                jumpWeb("https://www.youtube.com/c/zhounicociggo/");
                break;
            case R.id.bt_ins:
                jumpWeb("https://www.instagram.com/herbstick/");
                break;
            case R.id.bt_facebook:
                jumpWeb("https://www.facebook.com/bauway.ecigarette/");
                break;
            case R.id.bt_tw:
                jumpWeb("https://twitter.com/Herbstick/");
                break;
        }
    }
}
