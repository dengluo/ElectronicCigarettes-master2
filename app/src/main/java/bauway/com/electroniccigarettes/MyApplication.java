package bauway.com.electroniccigarettes;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;

import com.bestmafen.smablelib.component.SimpleSmaCallback;
import com.bestmafen.smablelib.component.SmaManager;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedList;
import java.util.List;

import bauway.com.electroniccigarettes.common.MyConstants2;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by zhaotaotao on 2017/8/9.
 * Application
 */
public class MyApplication extends MultiDexApplication {

    public static boolean LOG_SWITCH = true; //log和debug模式的开关
    private static MyApplication application;
    private static List<Activity> activityList = new LinkedList<>();
    public RxSharedPreferences userRxPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initPf();
        initBLE();
        initDebug();
        initUtils();
        initBmob();
        initBugly();
//        initBle();
    }

    private void initPf() {
        SharedPreferences preferences = getSharedPreferences(MyConstants2.USER_INFO, Context.MODE_PRIVATE);
        userRxPreferences = RxSharedPreferences.create(preferences);
    }

    private void initBLE() {
        SmaManager.getInstance().addSmaCallback(new SimpleSmaCallback() {

            @Override
            public void onConnected(BluetoothDevice device, boolean isConnected) {

            }

            @Override
            public void onReadTemperature(int temperature) {
                userRxPreferences.getInteger(MyConstants2.SP_CURRENT_TEMPERATURE).set(temperature);
            }

            @Override
            public void onReadVoltage(float voltage) {
                userRxPreferences.getFloat(MyConstants2.SP_LAST_VOLTAGE).set(voltage);
            }

            @Override
            public void onCharging(float voltage) {
                userRxPreferences.getFloat(MyConstants2.SP_LAST_VOLTAGE).set(voltage);

                long end = userRxPreferences.getLong(MyConstants2.SP_CHARGING_END).get();
                long now = System.currentTimeMillis();
                if (now - end > 1000 * 10) {
                    userRxPreferences.getLong(MyConstants2.SP_CHARGING_START).set(now);
                }
                userRxPreferences.getLong(MyConstants2.SP_CHARGING_END).set(System.currentTimeMillis());
            }

            @Override
            public void onReadChargeCount(int count) {
//                userRxPreferences.getLong(MyConstants2.SP_CHARGING_START).set(0L);
//                userRxPreferences.getLong(MyConstants2.SP_CHARGING_END).set(System.currentTimeMillis());
                userRxPreferences.getInteger(MyConstants2.SP_CHARGE_COUNT).set(count);
            }

            @Override
            public void onReadPuffCount(int puff) {
                userRxPreferences.getInteger(MyConstants2.SP_PUFF_COUNT).set(puff);
            }
        });
    }

    private void initDebug() {
        //根据是否是Debug模式，决定日志部分的开关
        ApplicationInfo applicationInfo = this.getApplicationInfo();
        LOG_SWITCH = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void initUtils() {
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(LOG_SWITCH)
                .setGlobalTag(MyConstants2.LOG_TAG)
                .setLog2FileSwitch(false)
                .setBorderSwitch(true);
        ToastUtils.setBgResource(R.drawable.shape_toast_blue_bg);
    }

    public static MyApplication getInstance() {
        return application;
    }

    private void initBmob() {
        BmobConfig bmobConfig = new BmobConfig
                .Builder(this)
                .setApplicationId("205b9c5c2836c849625dab18dc4316b6")
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(MyConstants2.HTTP_TIME_OUT)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(bmobConfig);
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "c7802b564f", true);
//        CrashReport.testJavaCrash();
    }

//    private void initBle(){
//        SmaManager.getInstance().init(this).connect(true);
//    }

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
