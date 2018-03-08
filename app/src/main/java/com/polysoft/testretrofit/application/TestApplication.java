package com.polysoft.testretrofit.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.polysoft.testretrofit.base.BaseActivity;
import com.polysoft.testretrofit.config.ConstantConfig;

import java.util.ArrayList;
import java.util.List;

import shortbread.Shortbread;

/**
 * Created by yang on 2018/3/5.
 */

public class TestApplication extends MultiDexApplication {
    private static final String TAG = TestApplication.class.getSimpleName();

    //存放已经打开的activity
    public static List<BaseActivity> activityList;

    private static TestApplication mInstance;

    public static TestApplication getInstance() {
        if (mInstance == null) {
            synchronized (TestApplication.class) {
                if (mInstance == null) {
                    mInstance = new TestApplication();
                }
            }
        }
        return mInstance;
    }

    public static List<BaseActivity> getActivityInstance() {
        if (activityList == null) {
            synchronized (TestApplication.class) {
                if (activityList == null) {
                    activityList = new ArrayList<>();
                }
            }
        }
        return activityList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLogger();
        Shortbread.create(this);
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, String tag) {
                return ConstantConfig.DEBUG;
            }
        });
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }





}
