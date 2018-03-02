package com.bauway.alarm.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bauway.alarm.BaseApplication;
import com.bauway.alarm.MyApplication;
import com.bauway.alarm.R;
import com.bauway.alarm.activity.Login;
import com.bauway.alarm.bean.User;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.PreferencesUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by zhaotaotao on 2017/12/13.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    public BaseApplication myApplication;
    public Context mContext;
    public Unbinder unbinder;
    private ProgressDialog mDialog;
    protected final PublishSubject<ActivityEvent> lifePublishSubject = PublishSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        lifePublishSubject.onNext(ActivityEvent.CREATE);
        myApplication = MyApplication.getInstance();
        setContentView(getLayoutRes());
        unbinder = ButterKnife.bind(this);
        init(savedInstanceState);
        initView();
        initData();
        initEvent();
        initComplete(savedInstanceState);
        myApplication.addActivity(this);
    }


    protected abstract int getLayoutRes();

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void init(Bundle savedInstanceState);

    protected User getUserEntity() {
        return BmobUser.getCurrentUser(User.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifePublishSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        lifePublishSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        lifePublishSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifePublishSubject.onNext(ActivityEvent.START);
    }


    @Override
    protected void onDestroy() {
        myApplication.removeActivity(this);
        unbinder.unbind();
        lifePublishSubject.onNext(ActivityEvent.DESTROY);
        if (mDialog != null) {
            mDialog.dismiss();
        }
        DialogUtil.hide();
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


    /**
     * 请求验证码
     *
     * @param phone       手机号
     * @param isCheckUser 是否反向检测查询用户数据的，true，号码存在不发送验证码；false,号码存在发送验证码
     */
    protected void getCode(final String phone, final boolean isCheckUser) {
        Boolean isSendCode = PreferencesUtils.getBoolean(mContext, MyConstants.SMS_CODE_TIMING, false);
        if (isSendCode) {
            //已发验证码
            ToastUtils.showShort(R.string.code_is_send_plz_check);
        } else {

            BmobQuery<User> query = new BmobQuery<User>();
            //查询playerName叫“比目”的数据
            query.addWhereEqualTo("username", phone);
            //返回50条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(1);
            //执行查询方法
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if (e == null) {
                        LogUtils.d("号码查询成功，共" + object.size());
                        if (isCheckUser) {
                            //注册
                            if (object.size() > 0) {
                                //号码存在，不发送验证码
                                ToastUtils.showShort(R.string.phone_already_register);
                            } else {
                                getVerifyCode(phone, isCheckUser);
                            }
                        } else {
                            //重置密码
                            if (object.size() > 0) {
                                //号码存在，发送验证码
                                getVerifyCode(phone, isCheckUser);
                            } else {
                                ToastUtils.showShort(R.string.phone_no_exist);
                            }
                        }

                    } else {
                        LogUtils.d("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        ToastUtils.showShort(R.string.code_is_send_error_plz_try);
                    }
                }
            });
        }
    }

    protected void getVerifyCode(String phone, boolean isCheckUser) {
        DialogUtil.progressDialog(mContext, getString(R.string.code_is_send), false);
        if (RegexUtils.isMobileSimple(phone)) {
            //手机号
            String template;
            if (isCheckUser) {
                template = MyConstants.SIGNUP;
            } else {
                template = MyConstants.FORGET;
            }
            BmobSMS.requestSMSCode(phone, template, new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e == null) {
                        LogUtils.d("短信发送成功，短信ID：" + integer);
                        ToastUtils.showShort(R.string.code_is_send_plz_check);
                        timeCountDown();
                    } else {
                        LogUtils.d("短信验证码请求失败：" + e.toString());
                        ToastUtils.showShort(R.string.code_is_send_error_plz_try);
                    }
                    DialogUtil.hide();
                }
            });
        } else {
            //非手机号
            ToastUtils.showShort(R.string.phone_err);
        }
    }

    /**
     * 倒计时
     */
    public void timeCountDown() {
        PreferencesUtils.putBoolean(mContext, MyConstants.SMS_CODE_TIMING, true);
        Flowable<Long> longFlowable = Flowable.intervalRange(0, 60 + 1, 0, 1, TimeUnit.SECONDS);
        longFlowable.compose(this.bindUntilEvent(ActivityEvent.DESTROY));
        longFlowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.d(this.toString(), aLong);
                        aLong = 60 - aLong;
                        if (aLong <= 0) {
                            PreferencesUtils.putBoolean(mContext, MyConstants.SMS_CODE_TIMING, false);
                            //最后一秒，那么转换显示为 获取验证码
                            updateCountDownEnd();
                        } else {
                            updateCountDown(aLong);
                        }

                    }
                });
    }

    /**
     * 更新倒计时
     *
     * @param aLong long
     */
    protected void updateCountDown(Long aLong) {

    }

    /**
     * 倒计时结束
     */
    protected void updateCountDownEnd() {

    }

    /**
     * 更新本地用户信息
     * 注意：需要先登录，否则会报9024错误
     *
     * @see cn.bmob.v3.helper.ErrorCode
     */
    protected void fetchUserInfo() {
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    LogUtils.d("最新的用户信息：" + s);
                } else {
                    LogUtils.d("更新用户信息失败：" + e.toString());
                }
            }
        });
    }

    protected void startLoginActivity() {
        startActivity(new Intent(this, Login.class));
    }


}
