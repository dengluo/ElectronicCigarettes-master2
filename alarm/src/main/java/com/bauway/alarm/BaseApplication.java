package com.bauway.alarm;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;

import com.bauway.alarm.common.MyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by zhaotaotao on 2017/8/9.
 * Application
 */
public class BaseApplication extends MultiDexApplication {
    public static boolean LOG_SWITCH = true; //log和debug模式的开关

    private static BaseApplication application;
    private static List<Activity> activityList = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initDebug();
        initUtils();
        initBmob();
    }

    private void initDebug() {
        //根据是否是Debug模式，决定日志部分的开关
        ApplicationInfo applicationInfo = this.getApplicationInfo();
        LOG_SWITCH = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void initUtils() {
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(LOG_SWITCH)
                .setGlobalTag(MyConstants.LOG_TAG)
                .setLog2FileSwitch(false)
                .setBorderSwitch(true);
//        ToastUtils.setBgResource(R.drawable.shape_toast_blue_bg);
    }

    public static BaseApplication getInstance() {
        return application;
    }

    private void initBmob() {
        BmobConfig bmobConfig = new BmobConfig
                .Builder(this)
                .setApplicationId("a9800a2ecef343e8ce95ff1ec2b14db4")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(bmobConfig);
    }

    /**
     * 添加Activity到容器中
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
        activityList.clear();
    }
}
