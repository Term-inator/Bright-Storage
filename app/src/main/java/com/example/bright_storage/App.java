package com.example.bright_storage;

import android.app.Application;
import android.util.Log;

import com.example.bright_storage.exception.handler.ExceptionHandler;

import org.xutils.x;

public class App extends Application {

    private static final String TAG = "App";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        // database
        x.Ext.init(this);
        x.Ext.setDebug(false);
        // register exception handle
        new ExceptionHandler(getApplicationContext()).register();
        //
//        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=0c1d0b77");
    }
}
