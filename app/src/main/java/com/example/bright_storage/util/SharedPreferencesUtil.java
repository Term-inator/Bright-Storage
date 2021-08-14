package com.example.bright_storage.util;

import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private static SharedPreferences sharedPreferences;

    public static void setSharedPreferences(SharedPreferences sharedPreferences){
        SharedPreferencesUtil.sharedPreferences = sharedPreferences;
    }

    public static void putString(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue){
        return sharedPreferences.getString(key, defValue);
    }

}
