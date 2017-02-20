package com.agenthun.eseallite;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.agenthun.eseallite.utils.update.UpdateConfig;


/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/4 上午6:48.
 */
public class App extends Application {
    private static final String TAG = "App";
    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: application");
        mApplicationContext = this;

        /***
         * 初始化版本升级模块
         */
//        UpdateConfig.initGet(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
