package com.example.android.popmovies;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by yuxia on 9/2/18.
 */

public class MovieExecutor {

    private static final Object LOCK = new Object();
    private static MovieExecutor sInstance;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    private MovieExecutor(Executor diskIO, Executor networkIO, Executor mainThread)
    {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static MovieExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MovieExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
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
        private android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
