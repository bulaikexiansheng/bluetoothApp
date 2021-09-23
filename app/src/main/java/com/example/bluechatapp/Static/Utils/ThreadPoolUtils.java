package com.example.bluechatapp.Static.Utils;

import android.os.Bundle;

import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class ThreadPoolUtils {
    private static ThreadPoolUtils threadPoolUtils = null ;
    private static ExecutorService predictSinglePool ;
    private final ExecutorService cashedThreadPool = null ;
    private final ExecutorService fixedThreadPool = null  ;
    private final ExecutorService singleThreadPool ;
    private ThreadPoolUtils() {
        singleThreadPool = Executors.newSingleThreadScheduledExecutor() ;
    }

    public static ThreadPoolUtils newInstance() {
        return (threadPoolUtils!=null)?threadPoolUtils:(threadPoolUtils = new ThreadPoolUtils()) ;
    }

    public ExecutorService getSingleThreadPool() {
        return singleThreadPool;
    }

    public static ExecutorService getPredictSinglePool() {
        return (predictSinglePool=Executors.newSingleThreadExecutor());
    }
}
