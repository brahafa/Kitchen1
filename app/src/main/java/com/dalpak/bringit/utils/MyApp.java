package com.dalpak.bringit.utils;

import android.app.Application;
import android.os.Build;
import android.webkit.CookieSyncManager;

public class MyApp extends Application {

    private static MyApp _instance;

    public static MyApp get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        _instance = this;
        SharedPrefs.loadPrefs(this);
    }


}

