package com.szulcmaciej.wobble;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Lenovo on 2017-06-06.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
