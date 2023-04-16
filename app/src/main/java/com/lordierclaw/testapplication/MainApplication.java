package com.lordierclaw.testapplication;

import android.app.Application;

import com.lordierclaw.testapplication.Utils.sharedprefs.SharedPrefsManager;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefsManager.init(getApplicationContext());
    }
}
