package io.mountblue.offlineSurvey;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        Stetho.initializeWithDefaults(this);
    }
}
