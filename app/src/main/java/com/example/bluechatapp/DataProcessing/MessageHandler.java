package com.example.bluechatapp.DataProcessing;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.bluechatapp.BlueTooth.BluetoothChatService;
import com.example.bluechatapp.BlueTooth.Constants;
import com.example.bluechatapp.Static.Utils.ThreadPoolUtils;
import com.example.bluechatapp.Static.Utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageHandler extends Handler {
    private WeakReference<Activity> mWeakReference;
    private static ExecutorService singleThreadPool = Executors.newSingleThreadExecutor() ;

    public MessageHandler(Activity activity) {
        mWeakReference = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        final Activity activity = mWeakReference.get();
        if (activity != null) {
            // TODO: 2021/8/2 test
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            break;
                        case BluetoothChatService.STATE_CONNECTING:{
                            msg.what = Constants.MESSAGE_WRITE ;
                            break;
                        }
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    Bundle data = msg.getData();
                    String recieveStr = new String(data.getString("BTdata"));
                    if (recieveStr != null) {
                        DataPreProgressThread dataPreThread = new DataPreProgressThread (recieveStr, activity);
//                        ThreadPoolUtils.newInstance().getSingleThreadPool().execute(dataPreThread);
                        singleThreadPool.submit(dataPreThread) ;
//                        System.out.println(recieveStr);
                    }
//                    if (savingflag) {
//                        singleThreadExecutor.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    // notice1
//                                    savaFileToSD(filename, recieveStr);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (activity != null) {
                        ToastUtil.showShortMessage(activity,"Connected to"+ mConnectedDeviceName);
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        ToastUtil.showShortMessage(activity,msg.getData().getString(Constants.TOAST));
                    }
                    break;
            }

        }
    }
}
