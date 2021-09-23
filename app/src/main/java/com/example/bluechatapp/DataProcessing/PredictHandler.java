package com.example.bluechatapp.DataProcessing;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.chaquo.python.PyException;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.bluechatapp.BlueTooth.Constants;
import com.example.bluechatapp.Static.Utils.ThreadPoolUtils;
import com.example.bluechatapp.Static.Utils.ToastUtil;


public class PredictHandler extends Handler {
    private WeakReference<Activity> mWeakReference;
    private Queue<Integer> queue_result;
    private static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    private static PythonExecutor pyExecutor = PythonExecutor.getExecutor();

    public PredictHandler(Activity activity) {
        mWeakReference = new WeakReference<Activity>(activity);
        queue_result = new LinkedList<Integer>();
    }

    @Override
    public void handleMessage(Message msg) {
        final Activity activity = mWeakReference.get();
        if (activity != null) {
            switch (msg.what) {
                // 展示数据
                case Constants.MESSAGE_desperate:
                    break;
                // 运行模型
                case Constants.MESSAGE_runmodel:
                    // 提取对应的数据
//                        String[] array_bx = msg.getData().getStringArray("bx");
//                        String[] array_by = msg.getData().getStringArray("by");
//                        String[] array_bz = msg.getData().getStringArray("bz");
                    String[] array_gx = msg.getData().getStringArray("gx");
                    String[] array_gy = msg.getData().getStringArray("gy");
                    String[] array_gz = msg.getData().getStringArray("gz");
//
//                        String[] array_ax = msg.getData().getStringArray("ax");
//                        String[] array_ay = msg.getData().getStringArray("ay");
//                        String[] array_az = msg.getData().getStringArray("az");
//
//                        String[] array_nx = msg.getData().getStringArray("nx");
//                        String[] array_ny = msg.getData().getStringArray("ny");
//                        String[] array_nz = msg.getData().getStringArray("nz");
//
//                        String[] array_qz = msg.getData().getStringArray("qz");
//                        String[] array_qx = msg.getData().getStringArray("qx");
//                        String[] array_qy = msg.getData().getStringArray("qy");
//                        String[] array_qw = msg.getData().getStringArray("qw");
                    int number = msg.getData().getInt("int");
                    long time = msg.getData().getLong("time");
                    Runnable predict = new Runnable() {
                        @Override
                        public void run() {

                            try {
                                // TODO: 2021/8/2 对数据发送给Python端进行处理 start
                                long start = System.currentTimeMillis();
                                int result;
                                // 返回result结果
                                result = pyExecutor.execute(array_gx, array_gy, array_gz);
//
//                                if (result!=4){
//                                    System.out.println("ResultTime:"+result+" consumed Time: "+(""+(System.currentTimeMillis()-start)));
//                                }
                                // TODO: 2021/8/2 end
                                // 根据result判断行为
                                if (!(queue_result.contains(result) || result == 4)) {
                                    if (!(result == 4)) {
//                                        singleThreadExecutor3.shutdown();
//                                        singleThreadExecutor3 = Executors.newSingleThreadExecutor() ;
                                        activity.runOnUiThread(new Runnable() {
                                            //                                        @Override
                                            public void run() {
                                                switch (result) {
                                                    case 0:
//                                                    if (isPlayer_show) {
//                                                        if (media_play.getVisibility() == View.VISIBLE)
//                                                            media_play.callOnClick();
//                                                        else
//                                                            media_pause.callOnClick();
//                                                    }
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "head_down");
//                                                    else {
//                                                        new Thread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Gesture.down_moving();
//                                                            }
//                                                        }).start();
//                                                    }
//
                                                        break;
                                                    case 1:
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "head_up");
//                                                    else {
//                                                        new Thread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Gesture.up_moving();
//                                                            }
//                                                        }).start();
//                                                    }
                                                        break;
                                                    case 2:
//                                                    if (isPlayer_show) {
//                                                        try {
//                                                            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_DOWN;
//                                                            Runtime runtime = Runtime.getRuntime();
//                                                            Process proc = runtime.exec(keyCommand);
//
//                                                            Log.i(TAG, "OnVolumeAddKey: 音量减按下");
//                                                        } catch (IOException e) {
//                                                            // TODO Auto-generated catch block
//                                                            e.printStackTrace();
//                                                        }
//                                                    }
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "head_turn_left");
//                                                    else {
//                                                        new Thread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Gesture.right_moving();
//                                                            }
//                                                        }).start();
//                                                    }
                                                        break;
                                                    case 3:
//                                                    if (isPlayer_show) {
//                                                        String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_UP;
//                                                        Runtime runtime = Runtime.getRuntime();
//                                                        try {
//                                                            Process proc = runtime.exec(keyCommand);
//                                                        } catch (IOException e) {
//                                                            e.printStackTrace();
//                                                        }
//
//                                                        Log.i(TAG, "OnVolumeAddKey: 音量加按下");
//                                                    }
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "head_turn_right");
//                                                    else {
//                                                        new Thread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Gesture.left_moving();
//                                                            }
//                                                        }).start();
//                                                    }
                                                        break;
//                                                case 4:
//                                                    if (!map_show)
//                                                        ToastUtil.showShortMessage(activity,"still");
//                                                    break;
                                                    case 5:
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "body_turn_left");
//
                                                        break;
                                                    case 6:
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "body_turn_right");
                                                        break;
                                                    case 7:
//                                                    if (isPlayer_show) {
//                                                        media_previous.callOnClick();
//                                                    }
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "head_lean_left");
//
//                                                    else {
//                                                        new Thread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Gesture.bigger();
//                                                            }
//                                                        }).start();
//                                                    }
                                                        break;
                                                    case 8:
//                                                    if (isPlayer_show) {
//                                                        media_next.callOnClick();
//                                                    }
//                                                    if (!map_show)
                                                        ToastUtil.showShortMessage(activity, "head_lean_right");
//
//                                                    else {
//                                                        new Thread(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                Gesture.smaller();
//                                                            }
//                                                        }).start();
//                                                    }
                                                        break;
                                                }
                                            }
                                        });
                                    }
                                    // TODO: 2021/8/17 params2
                                    if (queue_result.size() < 5)
                                        queue_result.offer(result);
                                    else {
                                        queue_result.poll();
                                        queue_result.offer(result);
                                    }
                                }
                            } catch (PyException e) {
                                System.out.println("郭异常");
                            }
                        }
                    };
                    singleThreadPool.submit(predict);
                    break;
            }

        }
    }
}
