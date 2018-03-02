package bauway.com.electroniccigarettes.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tbruyelle.rxpermissions2.RxPermissions;

import bauway.com.electroniccigarettes.MyApplication;
import bauway.com.electroniccigarettes.bean.User;
import bauway.com.electroniccigarettes.common.MyConstants2;
import bauway.com.electroniccigarettes.util.PreferencesUtils;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zhaotaotao on 2017/8/9.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public MyApplication myApplication;
    public Context mContext;
    public Unbinder unbinder;
    public RxPermissions rxPermissions;
    public RxSharedPreferences userRxPreferences;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        rxPermissions = new RxPermissions(this);
        myApplication = MyApplication.getInstance();
        setContentView(getLayoutRes());
        unbinder = ButterKnife.bind(this);
        initSP();
        init(savedInstanceState);
        initView();
        initData();
        initEvent();
        initComplete(savedInstanceState);
        myApplication.addActivity(this);
    }

    private void initSP() {
        if (userRxPreferences == null) {
            SharedPreferences preferences = mContext.getSharedPreferences(MyConstants2.USER_INFO, Context.MODE_PRIVATE);
            userRxPreferences = RxSharedPreferences.create(preferences);
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void init(Bundle savedInstanceState);

    protected User getUserEntity() {
        return PreferencesUtils.getEntity(mContext, User.class);
    }

    @Override
    protected void onDestroy() {
        myApplication.removeActivity(this);
        unbinder.unbind();
        if (mDialog != null) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

    protected void showProgress(String msg) {
        if (TextUtils.isEmpty(msg)) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } else {
            if (mDialog == null) {
                mDialog = new ProgressDialog(mContext);
                mDialog.setCancelable(false);
            }
            mDialog.setMessage(msg);
            mDialog.show();
        }
    }

    protected void showProgress(int stringRes) {
        showProgress(getString(stringRes));
    }

    protected void jumpWeb(String uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}
