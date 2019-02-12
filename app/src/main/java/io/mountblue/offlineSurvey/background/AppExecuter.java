package io.mountblue.offlineSurvey.background;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecuter {

    private static final Object LOCK = new Object();
    private static AppExecuter sInstance;
    private final Executor diskIO;

    private AppExecuter(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecuter getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecuter(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }
}

