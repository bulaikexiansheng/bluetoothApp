package com.example.bluechatapp.DataProcessing;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.RequiresApi;

import com.example.bluechatapp.BlueTooth.Constants;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataCombinedThread implements Runnable {
    private String receivestr;
    private ArrayList<String> mArrayList;
    private long startTime;
    private long endTime;
    private static Queue<String> queue_gx = new LinkedList<String>();
    private static Queue<String> queue_gy = new LinkedList<String>();
    private static Queue<String> queue_gz = new LinkedList<String>();
    private static Queue<String> queue_bx = new LinkedList<String>();
    private static Queue<String> queue_by = new LinkedList<String>();
    private static Queue<String> queue_bz = new LinkedList<String>();
    private final int DATASIZE = 20 ;
    private PredictHandler predictHandler ;
    public DataCombinedThread(String s, ArrayList<String> ss) {
        receivestr = s;
        mArrayList = ss;
        startTime = System.currentTimeMillis();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void run() {
//        chart_update = (chart_update + 1) % 5;
        if (mArrayList != null && mArrayList.size() > 0) {
//                System.out.println(mArrayList.size());
            for (int i = 0; i < mArrayList.size(); i++) {
                if (mArrayList.get(i) != null) {
                    // 正则表达式提取蓝牙数据
                    String regEx = "[a-zA-Z:\\s]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(mArrayList.get(i));
                    String s = m.replaceAll("").trim();
                    // TODO: 2021/8/2 gx,gy,gz 三个队列，当三个队列全满20的时候才发送数据给python，发送过后立刻清空 start
                    if (mArrayList.get(i).contains("GX")) {
                        queue_gx.offer(s);
                    } else if (mArrayList.get(i).contains("GY")) {
                        queue_gy.offer(s);
                    } else if (mArrayList.get(i).contains("GZ")) {
                        queue_gz.offer(s);
                    } else if (mArrayList.get(i).contains("BX")) {
                        queue_bx.offer(s);
                    } else if (mArrayList.get(i).contains("BY")) {
                        queue_by.offer(s);
                    } else if (mArrayList.get(i).contains("BZ")) {
                        queue_bz.offer(s);
                    }
//                        else if (mArrayList.get(i).contains("AX")) {
//                            queue_ax.offer(s);
//                        } else if (mArrayList.get(i).contains("AY")) {
//                            queue_ay.offer(s);
//                        } else if (mArrayList.get(i).contains("AZ")) {
//                            queue_az.offer(s);
//                        } else if (mArrayList.get(i).contains("NX")) {
//                            queue_nx.offer(s);
//                        } else if (mArrayList.get(i).contains("NY")) {
//                            queue_ny.offer(s);
//                        }
//                        else if (mArrayList.get(i).contains("NZ")) {
//                            queue_nz.offer(s);
//                        } else if (mArrayList.get(i).contains("QX")) {
//                            queue_qx.offer(s);
//                        }
//                        else if (mArrayList.get(i).contains("QY")) {
//                            queue_qy.offer(s);
//                        }
//                        else if (mArrayList.get(i).contains("QZ")) {
//                            queue_qz.offer(s);
//                        }else if (mArrayList.get(i).contains("QW")) {
//                            queue_qw.offer(s);
//                        }

                    // 更新图表
//                    if (chart_update == 0) {
//                        Message msg = new Message();
//                        msg.what = MESSAGE_desperate;
//                        Bundle bundle = new Bundle();
//                        bundle.putString("output", mArrayList.get(i));
//                        msg.setData(bundle);
//                        mhandler2.sendMessage(msg);
//                        EventBus.getDefault().post(mArrayList.get(i));
//                    }
                    // 三个全满20，发送数据
                    // TODO: 2021/8/17 params1
                    if (queue_gx.size() == DATASIZE && queue_gy.size() == DATASIZE && queue_gz.size() == DATASIZE) {
                        // 十六个通道
                        ArrayList<String> list_bx = new ArrayList<String>(queue_bx);
                        ArrayList<String> list_by = new ArrayList<String>(queue_by);
                        ArrayList<String> list_bz = new ArrayList<String>(queue_bz);
                        ArrayList<String> list_gx = new ArrayList<String>(queue_gx);
                        ArrayList<String> list_gy = new ArrayList<String>(queue_gy);
                        ArrayList<String> list_gz = new ArrayList<String>(queue_gz);
//

//                            ArrayList<String> list_az = new ArrayList<String>(queue_az);
//                            ArrayList<String> list_ax = new ArrayList<String>(queue_ax);
//                            ArrayList<String> list_ay = new ArrayList<String>(queue_ay);
//
//                            ArrayList<String> list_qz = new ArrayList<String>(queue_qz);
//                            ArrayList<String> list_qx = new ArrayList<String>(queue_qx);
//                            ArrayList<String> list_qy = new ArrayList<String>(queue_qy);
//                            ArrayList<String> list_qw = new ArrayList<String>(queue_qw);
//
//                            ArrayList<String> list_nx = new ArrayList<String>(queue_nx);
//                            ArrayList<String> list_ny = new ArrayList<String>(queue_ny);
//                            ArrayList<String> list_nz = new ArrayList<String>(queue_nz);
                        String[] array_bx = list_bx.toArray(new String[DATASIZE]);
                        String[] array_by = list_by.toArray(new String[DATASIZE]);
                        String[] array_bz = list_bz.toArray(new String[DATASIZE]);
                        String[] array_gx = list_gx.toArray(new String[DATASIZE]);
                        String[] array_gy = list_gy.toArray(new String[DATASIZE]);
                        String[] array_gz = list_gz.toArray(new String[DATASIZE]);
//
//                            String[] array_ax = list_ax.toArray(new String[DATASIZE]);
//                            String[] array_ay = list_ay.toArray(new String[DATASIZE]);
//                            String[] array_az = list_az.toArray(new String[DATASIZE]);
//                            String[] array_nx = list_nx.toArray(new String[DATASIZE]);
//                            String[] array_ny = list_ny.toArray(new String[DATASIZE]);
//                            String[] array_nz = list_nz.toArray(new String[DATASIZE]);
//
//                            String[] array_qz = list_qz.toArray(new String[DATASIZE]);
//                            String[] array_qx = list_qx.toArray(new String[DATASIZE]);
//                            String[] array_qy = list_qy.toArray(new String[DATASIZE]);
//                            String[] array_qw = list_qw.toArray(new String[DATASIZE]);

//                            System.out.println("god" + Thread.currentThread() + array_gy[0] + "-" + queue_gy.peek() + "-" + i);
                        Message mm = new Message();
                        mm.what = Constants.MESSAGE_runmodel;
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("bx", array_bx);
                        bundle.putStringArray("by", array_by);
                        bundle.putStringArray("bz", array_bz);
                        bundle.putStringArray("gx", array_gx);
                        bundle.putStringArray("gy", array_gy);
                        bundle.putStringArray("gz", array_gz);
//
//                            bundle.putStringArray("ax", array_ax);
//                            bundle.putStringArray("ay", array_ay);
//                            bundle.putStringArray("az", array_az);
//
//                            bundle.putStringArray("nx", array_nx);
//                            bundle.putStringArray("ny", array_ny);
//                            bundle.putStringArray("nz", array_nz);
//
//                            bundle.putStringArray("qx", array_qx);
//                            bundle.putStringArray("qy", array_qy);
//                            bundle.putStringArray("qz", array_qz);
//                            bundle.putStringArray("qw", array_qw);
                        bundle.putInt("int", i);
                        bundle.putLong("time", System.currentTimeMillis());
                        mm.setData(bundle);
                        predictHandler = ModelRunnable.getPredictHandler() ;
                        if (predictHandler.getLooper().getQueue().isIdle()) {
                            predictHandler.sendMessage(mm);
                        }
                        queue_gx.clear();
                        queue_gy.clear();
                        queue_gz.clear();
                        queue_bx.clear();
                        queue_by.clear();
                        queue_bz.clear();
//
//                            queue_ax.clear();
//                            queue_ay.clear();
//                            queue_az.clear();
//                            queue_nx.clear();
//                            queue_ny.clear();
//                            queue_nz.clear();
//                            queue_qz.clear();
//                            queue_qx.clear();
//                            queue_qy.clear();
//                            queue_qw.clear();
                        // TODO: 2021/8/4 end
                    }
                }
            }
        }
    }
}
