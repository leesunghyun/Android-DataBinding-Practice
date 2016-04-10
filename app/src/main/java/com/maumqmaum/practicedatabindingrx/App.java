package com.maumqmaum.practicedatabindingrx;

import android.app.Application;

import com.maumqmaum.practicedatabindingrx.logger.ExtDebugTree;
import com.maumqmaum.practicedatabindingrx.util.PreferenceUtil;

import timber.log.Timber;

public class App extends Application {

    private static App app;

    private static PreferenceUtil preferenceUtil;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        initPreferenceUtil();
        Timber.plant(new ExtDebugTree());
    }

    private void initPreferenceUtil() {
        preferenceUtil = new PreferenceUtil();
    }

    public static PreferenceUtil getPreferenceUtil() {
        return preferenceUtil;
    }

}
