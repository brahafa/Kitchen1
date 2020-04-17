package com.dalpak.bringit.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {
    private static SharedPreferences sPrefs;

    public static void loadPrefs(Context context) {
        sPrefs = context.getSharedPreferences("POSPreference", Context.MODE_PRIVATE);
    }

    public static void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public static void saveData(String key, long value) {
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.apply();
    }

    public static void saveData(String key, boolean value) {
        SharedPreferences.Editor prefsEditor = sPrefs.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.apply();
    }

    public static boolean getBooleanData(String key) {
        return sPrefs.getBoolean(key, false);
    }

    public static long getLongData(String key) {
        return sPrefs.getLong(key, -1);
    }

    public static String getData(String key) {
        if (sPrefs != null) {
            return sPrefs.getString(key, "");
        }
        return "";
    }
}
