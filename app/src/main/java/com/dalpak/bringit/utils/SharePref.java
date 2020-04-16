package com.dalpak.bringit.utils;


import android.content.Context;
import android.content.SharedPreferences;

public  class SharePref {
    private static SharePref sharePref;
    private SharedPreferences sharedPreferences;

    public static SharePref getInstance(Context context) {
        if (sharePref == null) {
            sharePref = new SharePref(context);
        }
        return sharePref;
    }

    private SharePref(Context context) {
        sharedPreferences = context.getSharedPreferences("NamedPreference", Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public void saveData(String key, long value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }

    public void saveData(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public boolean getBooleanData(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public long getLongData(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
