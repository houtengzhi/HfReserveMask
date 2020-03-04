package com.yechy.hfreservemask;

import android.app.Application;
import android.content.Context;

/**
 * Created by cloud on 2020-02-23.
 */
public class HfApp extends Application {

    private static HfApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
