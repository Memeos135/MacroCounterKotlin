package com.example.macrocounterremaster.room;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutors {

    private static final Object LOCK = new Object();
    private static DatabaseExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private DatabaseExecutors(Executor diskIO, Executor mainThread, Executor networkIO) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
        this.networkIO = networkIO;
    }

    public static DatabaseExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DatabaseExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

    public Executor getMainThread() {
        return mainThread;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }
}

