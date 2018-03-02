package com.bauway.alarm;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zhaotaotao on 2017/8/9.
 * Application
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Stetho.initializeWithDefaults(this);
    }
}
