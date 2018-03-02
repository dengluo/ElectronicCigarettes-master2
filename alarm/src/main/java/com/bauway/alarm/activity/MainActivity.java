package com.bauway.alarm.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.fragment.AlarmSet;
import com.bauway.alarm.fragment.MoreSet;
import com.bauway.alarm.fragment.TrackerSet;
import com.bauway.alarm.util.PreferencesUtils;
import com.bauway.alarm.util.Tools;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    private List<Fragment>      tabFragments;
    private ContentPagerAdapter contentAdapter;

    private TabLayout.Tab tabAt;
    private ImageView     ivTab;
    private TextView      tvTab;

    private SmaManager  mSmaManager;
    private SmaCallback mSmaCallback;

    @Override
    protected int getLayoutRes() {
        return R.layout.main;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        fetchUserInfo();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        int deviceType = PreferencesUtils.getInt(mContext, MyConstants.DEVICE_TYPE, 0);
        if (deviceType == 0) {
            startActivity(new Intent(this, DeviceSelector.class));
            this.finish();
        } else {
            initContent(deviceType);
            initTab(deviceType);
        }
        switch (deviceType) {
            case 0:

                break;
            case 1:
                //防丢器

                break;
            case 2:
                //游戏闹钟
                break;
        }
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().init(this).addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (isConnected) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    String time = Tools.parseTime(hour, minute, second);
                    mSmaManager.write(SmaManager.SET.TIME, time);
                }
            }
        });
        mSmaManager.connect(true);
    }

    @Override
    protected void onDestroy() {
        mSmaManager.removeSmaCallback(mSmaCallback);
        mSmaManager.exit();
        super.onDestroy();
    }

    private void initContent(int deviceType) {
        tabFragments = new ArrayList<>();
        switch (deviceType) {
            case 1:
                //防丢器
                tabFragments.add(TrackerSet.newInstance());
                break;
            case 2:
                //游戏闹钟
                tabFragments.add(AlarmSet.newInstance());
                break;
        }
        tabFragments.add(MoreSet.newInstance());
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentAdapter);
    }

    private void initTab(int deviceType) {
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setSelectedTabIndicatorHeight(0);
        ViewCompat.setElevation(mTabLayout, 10);
        mTabLayout.setupWithViewPager(mViewPager);
        switch (deviceType) {
            case 1:
                //防丢器
                tabAt = mTabLayout.getTabAt(0);
                if (tabAt != null) {
                    tabAt.setCustomView(R.layout.item_tab_layout_custom);
                    ivTab = tabAt.getCustomView().findViewById(R.id.iv_tab);
                    tvTab = tabAt.getCustomView().findViewById(R.id.tv_tab);
                    ivTab.setImageResource(R.drawable.selector_track_tab);
                    tvTab.setText(R.string.track);
                }
                break;
            case 2:
                //游戏闹钟
                tabAt = mTabLayout.getTabAt(0);
                if (tabAt != null) {
                    tabAt.setCustomView(R.layout.item_tab_layout_custom);
                    ivTab = tabAt.getCustomView().findViewById(R.id.iv_tab);
                    tvTab = tabAt.getCustomView().findViewById(R.id.tv_tab);
                    ivTab.setImageResource(R.drawable.selector_alarm_tab);
                    tvTab.setText(R.string.clock);
                }
                break;
        }

        tabAt = mTabLayout.getTabAt(1);
        if (tabAt != null) {
            tabAt.setCustomView(R.layout.item_tab_layout_custom);
            ivTab = tabAt.getCustomView().findViewById(R.id.iv_tab);
            tvTab = tabAt.getCustomView().findViewById(R.id.tv_tab);
            ivTab.setImageResource(R.drawable.selector_more_tab);
            tvTab.setText(R.string.me);
        }
        mTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }
    }
}
