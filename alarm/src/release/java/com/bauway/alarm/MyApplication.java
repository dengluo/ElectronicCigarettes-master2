package com.bauway.alarm;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by zhaotaotao on 2017/8/9.
 * Application
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initBugly();
    }

    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "c7802b564f", true);
    }
}
