package com.bauway.alarm.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bauway.alarm.BaseApplication;
import com.bauway.alarm.R;
import com.bauway.alarm.common.MyConstants;
import com.bauway.alarm.util.DialogUtil;
import com.bauway.alarm.util.PreferencesUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;

/**
 * Created by zhaotaotao on 2016/11/9.
 * fragment
 */
public abstract class BaseFragment extends Fragment {

    public BaseApplication myApplication;
    public Context mContext;
    public Unbinder unBinder;
    public View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        myApplication = BaseApplication.getInstance();
        init(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutRes(), container, false);
        unBinder = ButterKnife.bind(this, mView);
        initView(mView);
        initData();
        initEvent();
        initComplete(savedInstanceState);
        return mView;
    }




    protected abstract void initView(View mView);

    protected abstract void initComplete(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void initData();


    protected abstract int getLayoutRes();

    protected abstract void init(Bundle savedInstanceState);

    /**
     * 初始化设置刷新控件
     */
    protected void refreshViewInitView(SwipeRefreshLayout swipeRefresh) {
        //设置下拉刷新控件的背景
        swipeRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(mContext, R.color.white_1));
        //设置控件圆圈的颜色
        swipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(mContext, R.color.blue_2)
        );
    }




    @Override
    public void onDestroy() {
        DialogUtil.hide();
        super.onDestroy();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if(unBinder!=null){
            unBinder.unbind();
            unBinder = null;
        }
        super.onDestroyView();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void closeApp() {
        BmobUser.logOut();
        PreferencesUtils.putBoolean(mContext, MyConstants.SMS_CODE_TIMING, false);
    }

}
