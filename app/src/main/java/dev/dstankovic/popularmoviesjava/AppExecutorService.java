package dev.dstankovic.popularmoviesjava;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutorService {

    private static AppExecutorService instance;
    private static int cores;

    public static AppExecutorService getInstance() {
        if (instance == null) {
            instance = new AppExecutorService();
        }
        return instance;
    }

    private final ScheduledExecutorService mScheduledExecutorService =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    public ScheduledExecutorService getScheduledExecutorService() {
        return mScheduledExecutorService;
    }
}
