package com.example.bluechatapp.DataProcessing;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DataPreProgressThread implements Runnable {
    private String receivestr;
    private final WeakReference<Activity> reference;
    public static int flag ;
    public DataPreProgressThread(String s, Activity activity) {
        receivestr = s;
        reference = new WeakReference<Activity>(activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void run() {
        ArrayList<String> mArrayList = analyze(receivestr);
        DataCombinedThread dataCombinedThread = new DataCombinedThread(receivestr, mArrayList);
        if (reference.get() != null)
            dataCombinedThread.run();
    }
    private static ArrayList<String> analyze(String str) {
        byte[] readbuf = str.getBytes();
        int p1 = 0;
        int p2 = p1;
        flag = 0;
        ByteBuffer b1 = ByteBuffer.allocate(2000);
        ArrayList<String> arrayList = new ArrayList<>();
        if (readbuf != null && b1.remaining() > readbuf.length && readbuf.length > 0)
            b1.put(readbuf);
        b1.clear();
        while (b1.remaining() > 0 && flag < 1998 && b1.position() < str.length() - 1) {
            while (b1.remaining() > 0 && b1.position() < str.length() - 1) {
                b1.get();
                flag += 1;
                if (b1.position() >= str.length() - 1 || b1.remaining() <= 0)
                    break;
                else if (str.charAt(b1.position()) == 'D')
                    break;
            }
            p2 = b1.position();
            if (p2 >= p1)
                arrayList.add(str.substring(p1, p2 + 1));
            p1 = p2 + 1;
        }
        if (arrayList.isEmpty())
            return null;
        else
            return arrayList;
    }
}
