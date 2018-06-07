package bauway.com.electroniccigarettes.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bestmafen.easeblelib.util.EaseUtils;
import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import bauway.com.electroniccigarettes.BuildConfig;
import bauway.com.electroniccigarettes.R;
import bauway.com.electroniccigarettes.adapter.SpinnerAdapter;
import bauway.com.electroniccigarettes.base.BaseActivity;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.interfaces.DialogCallback;
import bauway.com.electroniccigarettes.util.DialogUtil;
import bauway.com.electroniccigarettes.util.PreferencesUtils;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;

/**
 * Created by danny on 2017/8/11.
 * 设定
 */

public class MeActivity extends BaseActivity {

    @BindView(R.id.action_back)
    ImageView action_back;
    @BindView(R.id.ll_me_evaluate)
    LinearLayout ll_me_evaluate;
    @BindView(R.id.ll_me_notes)
    LinearLayout ll_me_notes;
    @BindView(R.id.ll_me_concern_us)
    LinearLayout ll_me_concern_us;
    @BindView(R.id.ll_me_unbind_device)
    LinearLayout ll_me_unbind_device;
    @BindView(R.id.ll_me_ota)
    LinearLayout ll_me_ota;

    private SmaManager mSmaManager;
    private DateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss");
    private SmaCallback mSmaCallback;

    @Override
    protected int getLayoutRes() {
        return R.layout.me_activity;
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
        ll_me_unbind_device.setVisibility(TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0]) && TextUtils.isEmpty(mSmaManager.getNameAndAddress()[1]) ? View.GONE :
                View.VISIBLE);
    }

    /**
     * @param type 0写 1读
     * @param data content
     */
    private synchronized void append(final String type, final byte[] data) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                mTvDebug.append(getTimeStr() + "  " + type + "\n");
//                mTvDebug.append(EaseUtils.byteArray2HexString(data));
//                mTvDebug.append("  " + getValue(data));
//                mTvDebug.append("\n\n");
            }
        });
    }

    private synchronized void append(final String value) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
//                mTvDebug.append(getTimeStr() + "\n");
//                mTvDebug.append("  " + value);
//                mTvDebug.append("\n\n");
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

    private void exitApp() {
        BmobUser.logOut();
        PreferencesUtils.clearEntity(mContext);
        ToastUtils.cancel();
        myApplication.exit();
        startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ll_me_unbind_device.setVisibility(TextUtils.isEmpty(mSmaManager.getNameAndAddress()[0]) ? View.GONE :
                View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        SmaManager.getInstance().unbind();
        ll_me_unbind_device.setVisibility(View.GONE);
        super.onDestroy();
    }

    @OnClick({R.id.action_back, R.id.ll_me_evaluate, R.id.ll_me_notes, R.id.ll_me_concern_us, R.id.ll_me_unbind_device, R.id.ll_me_ota, R.id.iv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.action_back:
                this.finish();
                break;
            case R.id.ll_me_evaluate:
                startActivity(new Intent(this, EvaluationActivity.class));
                break;
            case R.id.ll_me_notes:
                startActivity(new Intent(this, NotesActivity.class));
                break;
            case R.id.ll_me_concern_us:
                startActivity(new Intent(this, ConcernUsActivity.class));
                break;
            case R.id.ll_me_unbind_device:
                DialogUtil.defaultDialog(mContext, getString(R.string.confirm_unbind_device), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //确认解绑
                                SmaManager.getInstance().unbind();
                                ll_me_unbind_device.setVisibility(View.GONE);
                            }
                        });
                break;
            case R.id.ll_me_ota:
                if (!mSmaManager.isConnected) {
                    ToastUtils.showShortSafe(R.string.device_not_connected);
                    return;
                }
                mSmaManager.write(SmaManager.SET.INTO_OTA);
                break;
            case R.id.iv_cancel:
                DialogUtil.defaultDialog(mContext, getString(R.string.confirm_log_out_app), null, null, new
                        DialogCallback() {

                            @Override
                            public void execute(Object dialog, Object content) {
                                //确认退出
                                exitApp();
                            }
                        });
                break;
        }
    }
}
