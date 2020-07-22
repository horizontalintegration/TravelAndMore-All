package com.example.myapplication;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static MyApp instance;
    public static MyApp getInstance() {
        return instance;
    }

    private static Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private static Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            //Crashlytics.setString("available_memory", "5784");
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
        }
    };

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();

        //Crashlytics.start(this);

        // Second, cache a reference to default uncaught exception handler
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        // Third, set custom UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);

    }
}

