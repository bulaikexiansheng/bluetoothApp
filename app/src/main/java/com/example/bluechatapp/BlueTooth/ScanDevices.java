package com.example.bluechatapp.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScanDevices {
    // 蓝牙适配器
    private BluetoothAdapter bluetoothAdapter ;
    // 配对设备
    private Set<BluetoothDevice> pairedDevices ;
    private List<BluetoothDevice> devices ;
    // 上下文
    private Context context ;
    // 蓝牙观察者
    private BluetoothManager manager ;
    public ScanDevices(BluetoothAdapter bluetoothAdapter, Context context,BluetoothManager manager) {
        this.bluetoothAdapter = bluetoothAdapter;
        this.context = context;
        this.manager = manager ;
    }

    public void scan(){
        // 获取已经配对设备
        pairedDevices = bluetoothAdapter.getBondedDevices();
        devices.addAll(pairedDevices) ;
        manager.notifyManager(BluetoothManager.CHANGED); // 通知观察者
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction() ;
                // 获取未配对设备
                if (BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    manager.notifyManager(BluetoothManager.CHANGED);
                }
            }
        } ;
        context.registerReceiver(mReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
        BluetoothAdapter.getDefaultAdapter().startDiscovery(); //开始扫描设备
    }
}
