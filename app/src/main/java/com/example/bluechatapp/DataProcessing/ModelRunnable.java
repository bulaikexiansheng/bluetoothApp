package com.example.bluechatapp.DataProcessing;

import android.app.Activity;
import android.os.Looper;

public class ModelRunnable implements Runnable{
    private Activity activity;
    private static PredictHandler predictHandler ;
    public ModelRunnable(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void run() {
        Looper.prepare();
        predictHandler = new PredictHandler(this.activity);
        Looper.loop();
    }

    public static PredictHandler getPredictHandler() {
        return predictHandler;
    }
}
