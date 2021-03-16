package com.example.bright_storage;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

public class App extends Application {

    private static final String TAG = "App";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        x.Ext.init(this);
        x.Ext.setDebug(false);

    }
}
