package bauway.com.electroniccigarettes.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.bean.Product;
import bauway.com.electroniccigarettes.common.MyConstants2;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static bauway.com.electroniccigarettes.R.id.lv;

/**
 * Created by zhaotaotao on 2017/8/10.
 * 绑定设备
 */

public class BindDeviceActivity extends BaseActivity {
    private static long BIND_PERIOD = 40000;

    @BindView(R.id.bt_return)
    ImageButton mBtReturn;
    @BindView(R.id.relativeLayout2)
    RelativeLayout mRelativeLayout2;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(lv)
    ListView mLv;
    private DeviceAdapter mDeviceAdapter;
    @BindView(R.id.bt_search)
    Button mBtSearch;

    private SmaManager mSmaManager;
    private SmaCallback mSmaCallback;
    private IEaseScanner mScanner;
    private BroadcastReceiver mReceiver;

    private volatile boolean isFailed;
    private Handler mHandler = new Handler();
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
    private Product mProduct;

    @Override
    protected int getLayoutRes() {
        return R.layout.bind_device_activity;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        onViewClicked(mBtSearch);
    }

    @Override
    protected void initEvent() {
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isFailed = false;
                mScanner.startScan(false);
                showProgress(R.string.loading);
                mSmaManager.bindWithDevice(mDeviceAdapter.get(position).device);
                postBindFailDelay();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mLv.setAdapter(mDeviceAdapter = new DeviceAdapter());
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mProduct = getIntent().getParcelableExtra(MyConstants2.PRODUCT);
        mSmaManager = SmaManager.getInstance().addSmaCallback(mSmaCallback = new SimpleSmaCallback() {

            @Override
            public void onConnected(final BluetoothDevice device, boolean isConnected) {
                if (!isFailed && isConnected) {
                    mSmaManager.setNameAndAddress(device.getName(), device.getAddress());
                    mSmaManager.mEaseConnector.setAddress(device.getAddress());
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                }
            }
        });

        mScanner = ScannerFactory.createScanner().setScanOption(new ScanOption().scanPeriod(8000)/*.specifyName(mProduct
                .bleName)*/)
                .setEaseScanCallback(new EaseScanCallback() {

                    @Override
                    public void onDeviceFound(EaseDevice device) {
                        mDeviceAdapter.add(device);
                    }

                    @Override
                    public void onBluetoothDisabled() {
                        ToastUtils.showShortSafe(R.string.please_enable_the_bluetooth);
                    }

                    @Override
                    public void onScanStart(boolean start) {
                        if (start) {
                            mDeviceAdapter.clear();
                            mBtSearch.setText(R.string.searching);
                        } else {
                            mBtSearch.setText(R.string.search_again);
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
                            mDeviceAdapter.clear();
                            mBtSearch.setText(R.string.search_again);
                            break;
                    }
                }
            }
        }, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    private void postBindFailDelay() {
        mHandler.removeCallbacks(mCancelBindRunnable);
        mHandler.postDelayed(mCancelBindRunnable, BIND_PERIOD);
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

    @OnClick({R.id.bt_return, R.id.bt_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_return:
                this.finish();
                break;
            case R.id.bt_search:
                mScanner.startScan(!mScanner.isScanning);
                break;
        }
    }

    private class DeviceAdapter extends BaseAdapter {
        private List<EaseDevice> mDevices = new ArrayList<>();

        @Override
        public int getCount() {
            return mDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return mDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            ViewHolder vh;
            EaseDevice device = mDevices.get(position);
            if (v == null) {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
                vh = new ViewHolder(v);
            } else {
                vh = (ViewHolder) v.getTag();
            }

            vh.tv_name.setText(device.device.getName());
            vh.tv_address.setText(device.device.getAddress());
//            vh.iv_rssi.setImageLevel(-device.rssi);
            return v;
        }

        public void add(EaseDevice device) {
            if (mDevices.contains(device)) {
                mDevices.set(mDevices.indexOf(device), device);
            } else {
                mDevices.add(device);
            }
            Collections.sort(mDevices);
            notifyDataSetChanged();
        }

        public void clear() {
            mDevices.clear();
            notifyDataSetChanged();
        }

        public EaseDevice get(int position) {
            return (EaseDevice) getItem(position);
        }
    }

    class ViewHolder {
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_address)
        TextView tv_address;
//        ImageView iv_rssi;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
//            this.tv_name = v.findViewById(R.id.tv_name);
//            this.tv_address = v.findViewById(R.id.tv_address);
//            this.iv_rssi = (ImageView) v.findViewById(R.id.iv_rssi);
            v.setTag(this);
        }
    }
}
