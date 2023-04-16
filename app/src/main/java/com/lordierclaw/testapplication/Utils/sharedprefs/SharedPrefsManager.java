package com.lordierclaw.testapplication.Utils.sharedprefs;

import android.content.Context;

public class SharedPrefsManager {

    public static class LocalDataKey {
        public static final String IS_GOING_TO_EAT = "IS_GOING_TO_EAT";
        public static final String TRANSACTION = "TRANSACTION";
    }

    private static SharedPrefsManager instance;
    private SharedPrefs sharedPrefs;

    private SharedPrefsManager(){}

    private SharedPrefsManager(Context mContext) {
        sharedPrefs = new SharedPrefs(mContext);
    }

    public static void init(Context mContext) {
        instance = new SharedPrefsManager(mContext);
    }

    public static SharedPrefsManager getInstance() {
        if (instance == null) instance = new SharedPrefsManager();
        return instance;
    }

    public SharedPrefs getData() {
        return sharedPrefs;
    }

    public boolean getPrediction() {
        if (sharedPrefs != null)  return sharedPrefs.getBoolean(LocalDataKey.IS_GOING_TO_EAT);
        return false;
    }

    public void setPrediction(boolean value) {
        sharedPrefs.putBoolean(LocalDataKey.IS_GOING_TO_EAT, value);
    }

    public void setTransaction(long value) {
        sharedPrefs.putLong(LocalDataKey.TRANSACTION, value);
    }

    public long getTransaction() {
        return sharedPrefs.getLong(LocalDataKey.TRANSACTION);
    }

    public void clear() {
        sharedPrefs.clear();
    }
}
