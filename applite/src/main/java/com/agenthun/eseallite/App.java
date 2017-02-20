package com.agenthun.eseallite;

import android.app.Application;
import android.util.Log;

import com.agenthun.eseallite.utils.update.UpdateConfig;


/**
 * @project ESeal
 * @authors agenthun
 * @date 16/3/4 上午6:48.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UpdateConfig.initGet(this);
    }

}
