package bauway.com.electroniccigarettes.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bestmafen.easeblelib.util.EaseUtils;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import bauway.com.electroniccigarettes.BuildConfig;
import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.interfaces.DialogCallback;
import bauway.com.electroniccigarettes.util.DialogUtil;
import bauway.com.electroniccigarettes.util.PreferencesUtils;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView      mImageView;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.bt_product_list)
    Button         mBtProductList;
    @BindView(R.id.bt_common_set)
    Button         mBtCommonSet;
    @BindView(R.id.bt_set)
    Button         mBtSet;
    @BindView(R.id.bt_evaluation)
    Button         mBtEvaluation;
    @BindView(R.id.bt_notification)
    Button         mBtNotification;
    @BindView(R.id.bt_info)
    Button         mBtInfo;
    @BindView(R.id.bt_user_number)
    Button         mBtUserNumber;
    @BindView(R.id.bt_concern_us)
    Button         mBtConcernUs;
    @BindView(R.id.relativeLayout3)
    RelativeLayout mRelativeLayout3;
    @BindView(R.id.main_linearLayout)
    LinearLayout   mMainLinearLayout;
    @BindView(R.id.bt_log_out)
    Button         mBtLogOut;
    @BindView(R.id.bt_unbind)
    Button         mBtUnbind;
    @BindView(R.id.tv_app_version)
    TextView       mTvAppVersion;

    private SmaManager mSmaManager;
    //    private DebugViewManager mDebugViewManager;
    private TextView   mTvDebug;
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComplete(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
//            mDebugViewManager.showDebugWindow();
            findViewById(R.id.v_debug).setVisibility(View.VISIBLE);
            mTvDebug = (TextView) findViewById(R.id.tv_debug);
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        mTvAppVersion.setText(String.valueOf("v" + AppUtils.getAppVersionName()));
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mSmaManager = SmaManager.getInstance().init(this).addSmaCallback(new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {
                if (BuildConfig.DEBUG) {
                    append("  ->  isConnected " + isConnected);
                }
            }

            @Override
            public void onWrite(byte[] data) {
                if (BuildConfig.DEBUG) {
                    append("  ->  onWrite", data);
                }
            }

            @Override
            public void onRead(byte[] data) {
                if (BuildConfig.DEBUG) {
                    append("  ->  onRead", data);
                }
            }
        });
        mSmaManager.connect(true);
        if (BuildConfig.DEBUG) {
//            mDebugViewManager = new DebugViewManager(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBtUnbind.setVisibility(TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0]) ? View.GONE :
                View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        mSmaManager.exit();
        super.onDestroy();
    }

    @OnClick({R.id.bt_product_list, R.id.bt_common_set, R.id.bt_set,
            R.id.bt_evaluation, R.id.bt_notification, R.id.bt_info,
            R.id.bt_user_number, R.id.bt_concern_us, R.id.bt_log_out,
            R.id.bt_unbind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_product_list:
                if (!TextUtils.isEmpty(SmaManager.getInstance().getNameAndAddress()[1])) {
                    ToastUtils.showShort(R.string.already_bind);
                    return;
                }
                startActivity(new Intent(this, ProductSelectActivity.class));
                break;
            case R.id.bt_common_set:
                startActivity(new Intent(this, CommonSetActivity.class));
                break;
            case R.id.bt_set:
                startActivity(new Intent(this, SetActivity.class));
                break;
            case R.id.bt_evaluation:
                startActivity(new Intent(this, EvaluationActivity.class));
                break;
            case R.id.bt_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.bt_info:
                startActivity(new Intent(this, NotesActivity.class));
                break;
            case R.id.bt_user_number:
                startActivity(new Intent(this, UserUseNumberActivity.class));
                break;
            case R.id.bt_concern_us:
                startActivity(new Intent(this, ConcernUsActivity.class));
                break;
            case R.id.bt_log_out:
                DialogUtil.defaultDialog(mContext, getString(R.string.confirm_log_out_app), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //确认退出
                                exitApp();
                            }
                        });
                break;
            case R.id.bt_unbind:
                DialogUtil.defaultDialog(mContext, getString(R.string.confirm_unbind_device), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //确认解绑
                                SmaManager.getInstance().unbind();
                                mBtUnbind.setVisibility(View.GONE);
                            }
                        });
                break;
        }
    }

    private void exitApp() {
        BmobUser.logOut();
        PreferencesUtils.clearEntity(mContext);
        ToastUtils.cancel();
        myApplication.exit();
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

    /**
     * @param type 0写 1读
     * @param data content
     */
    private synchronized void append(final String type, final byte[] data) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTvDebug.append(getTimeStr() + "  " + type + "\n");
                mTvDebug.append(EaseUtils.byteArray2HexString(data));
                mTvDebug.append("  " + getValue(data));
                mTvDebug.append("\n\n");
            }
        });
    }

    private synchronized void append(final String value) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mTvDebug.append(getTimeStr() + "\n");
                mTvDebug.append("  " + value);
                mTvDebug.append("\n\n");
            }
        });
    }

    public String getTimeStr() {
        return mDateFormat.format(new Date());
    }

    private String getValue(@NonNull byte[] extra) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] valuePart = Arrays.copyOfRange(extra, 2, 8);
            for (byte b : valuePart) {
                sb.append((char) b);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
