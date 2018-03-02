package com.bauway.alarm.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bauway.alarm.R;
import com.bauway.alarm.base.BaseActivity;
import com.bestmafen.easeblelib.entity.EaseDevice;
import com.bestmafen.easeblelib.scanner.EaseScanCallback;
import com.bestmafen.easeblelib.scanner.IEaseScanner;
import com.bestmafen.easeblelib.scanner.ScanOption;
import com.bestmafen.easeblelib.scanner.ScannerFactory;
import com.bestmafen.easeblelib.util.L;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhaotaotao on 2017/12/15.
 * 扫描、绑定设备
 */

public class ConnectDevice extends BaseActivity {
    private static long BIND_PERIOD = 40000;

    @BindView(R.id.bt_return)
    Button         mBtReturn;
    @BindView(R.id.bt_scan)
    Button         mBtScan;
    @BindView(R.id.rl1)
    RelativeLayout mRl1;
    @BindView(R.id.line1)
    View           mLine1;
    @BindView(R.id.list_scan)
    ListView       mListScan;
    private CommonAdapter mAdapter;
    private List<EaseDevice> mDevices = new ArrayList<>();

    private SmaManager        mSmaManager;
    private SmaCallback       mSmaCallback;
    private IEaseScanner      mScanner;
    private BroadcastReceiver mReceiver;

    private volatile boolean isFailed;
    private Handler  mHandler            = new Handler();
    private Runnable mCancelBindRunnable = new Runnable() {

        @Override
        public void run() {
            L.e("mCancelBindRunnable -> run");
            isFailed = true;
            mSmaManager.close(true);
            mSmaManager.unbind();

            showProgress(null);
        }
    };

    @Override
    protected int getLayoutRes() {
        return R.layout.connect_device;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mListScan.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFailed = false;
                mScanner.startScan(false);
                showProgress(R.string.loading);
                mSmaManager.bindWithDevice(mDevices.get(position).device);
                postBindFailDelay();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mListScan.setAdapter(mAdapter = new CommonAdapter<EaseDevice>(mContext, R.layout.item_device, mDevices) {

            @Override
            protected void convert(ViewHolder viewHolder, EaseDevice item, int position) {
                viewHolder.setText(R.id.tv_name, item.device.getName());
                viewHolder.setText(R.id.tv_address, item.device.getAddress());
            }
        });
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onConnected(final BluetoothDevice device, boolean isConnected) {
                if (!isFailed && isConnected) {
                    mSmaManager.setNameAndAddress(device.getName(), device.getAddress());
                    mSmaManager.mEaseConnector.setAddress(device.getAddress());
//                    startActivity(new Intent(mContext, MainActivity.class));
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        mScanner = ScannerFactory.createScanner().setScanOption(new ScanOption().scanPeriod(8000)/*.specifyName(mProduct
                .bleName)*/).setEaseScanCallback(new EaseScanCallback() {

            @Override
            public void onDeviceFound(EaseDevice device) {
                if (mDevices.contains(device)) {
                    mDevices.set(mDevices.indexOf(device), device);
                } else {
                    mDevices.add(device);
                }
                Collections.sort(mDevices);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBluetoothDisabled() {
                ToastUtils.showShort(R.string.please_enable_the_bluetooth);
            }

            @Override
            public void onScanStart(boolean start) {
                if (start) {
                    mDevices.clear();
                    mAdapter.notifyDataSetChanged();
                    mBtScan.setText(R.string.searching);
                } else {
                    mBtScan.setText(R.string.search_again);
                }
            }
        });

        registerReceiver(mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(intent.getAction(), BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    switch (state) {
                        case BluetoothAdapter.STATE_OFF:
                            mDevices.clear();
                            mAdapter.notifyDataSetChanged();
                            mBtScan.setText(R.string.search_again);
                            break;
                    }
                }
            }
        }, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mSmaManager.removeSmaCallback(mSmaCallback);
        mScanner.exit();

        unregisterReceiver(mReceiver);
        if (TextUtils.isEmpty(mSmaManager.getNameAndAddress()[1])) {
            mSmaManager.unbind();
        }
        super.onDestroy();
    }

    private void postBindFailDelay() {
        mHandler.removeCallbacks(mCancelBindRunnable);
        mHandler.postDelayed(mCancelBindRunnable, BIND_PERIOD);
    }

    @OnClick({R.id.bt_return, R.id.bt_scan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_scan:
                mScanner.startScan(!mScanner.isScanning);
                break;
        }
    }
}
