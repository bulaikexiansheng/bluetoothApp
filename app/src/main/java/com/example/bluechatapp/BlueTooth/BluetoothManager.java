package com.example.bluechatapp.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Set;

public class BluetoothManager {
    public static final boolean CHANGED = true ;
    public static final boolean UNCHANGED = false ;
    private static final  String TAG = "BluetoothManager";
    private BluetoothAdapter mBluetoothAdapter ;
    private Context context ;

    public BluetoothManager(Context context) {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ;
    }

    /**
     * 设备是否支持蓝牙  true为支持
     * @return
     */
    public boolean isSupportBlue(){
        return mBluetoothAdapter!=null ;
    }
    /**
     * 蓝牙是否打开   true为打开
     * @return
     */
    public boolean isBlueEnable(){
        return isSupportBlue() && mBluetoothAdapter.isEnabled();
    }
    /**
     * 自动打开蓝牙（异步：蓝牙不会立刻就处于开启状态）
     * 这个方法打开蓝牙不会弹出提示
     */
    public void openBlueAsyn(){
        if (isSupportBlue()) {
            mBluetoothAdapter.enable();
        }
    }
    /**
    * 关闭蓝牙
    * */
    public void shutdownBlue(){
        if (isSupportBlue())
            mBluetoothAdapter.disable() ;
    }

    /**
     * 扫描的方法 返回true 扫描成功
     * 通过接收广播获取扫描到的设备
     * @return
     */
    public boolean doDiscovery() {
        if (!isBlueEnable()) {
            Log.e(TAG, "Bluetooth not enable!");
            return false;
        }
        //当前是否在扫描，如果是就取消当前的扫描，重新扫描
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        //此方法是个异步操作，一般搜索12秒
        return mBluetoothAdapter.startDiscovery();
    }
    /**
     * 取消扫描蓝牙
     * @return  true 为取消成功
     */
    public boolean cancelDiscovery(){
        if (isSupportBlue()){
            return mBluetoothAdapter.cancelDiscovery();
        }
        return true;
    }
    /**
     * 获得已经配对的设备
     */
    public Set<BluetoothDevice> getBoundedDevices(){
        return mBluetoothAdapter.getBondedDevices() ;
    }
    /**
     * 通知更改UI
     * @param state
     */
    public void notifyManager(boolean state){
        if (state == CHANGED) {
            // TODO: 2021/9/12 更新列表，更新完列表后就改变状态

        }else{
            // TODO: 2021/9/12 不更新列表
        }
    }

    /**
     *
     */
    public void registerReceiver(){
        // 蓝牙扫描广播注册
        IntentFilter filter1 = new IntentFilter(android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        IntentFilter filter2 = new IntentFilter(android.bluetooth.BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BroadcastReceiver scanBlueReceiver = new ScanBlueReceiver(new ScanBlueReceiver.ScanBlueCallBack() {
            @Override
            public void onScanStarted() {
                // TODO: 2021/9/13 扫描开始 
            }

            @Override
            public void onScanning(BluetoothDevice device) {
                // TODO: 2021/9/13 扫描正在进行 
            }

            @Override
            public void onScanFinished() {
                // TODO: 2021/9/13 扫描结束
            }
        }) ;
        context.registerReceiver(scanBlueReceiver,filter1);
        context.registerReceiver(scanBlueReceiver,filter2);
        context.registerReceiver(scanBlueReceiver,filter3);

        // 接受配对结果广播注册
        IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        PinBlueReceiver pinBlueReceiver = new PinBlueReceiver(new PinBlueReceiver.PinBlueCallBack() {
            @Override
            public void onBondFail(BluetoothDevice device) {

            }

            @Override
            public void onBonding(BluetoothDevice device) {

            }

            @Override
            public void onBondSuccess(BluetoothDevice device) {

            }

            @Override
            public void onBondRequest() {

            }
        }) ;
        context.registerReceiver(pinBlueReceiver,filter4);
        context.registerReceiver(pinBlueReceiver,filter5);
    }
    /**
     * 配对（配对成功与失败通过广播返回）
     * @param device
     */
    public void pin(BluetoothDevice device){
        if (device == null){
            Log.e(TAG, "bond device null");
            return;
        }
        if (!isBlueEnable()){
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //配对之前把扫描关闭
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        //判断设备是否配对，没有配对在配，配对了就不需要配了
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to bond:" + device.getName());
            try {
                Method createBondMethod = device.getClass().getMethod("createBond");
                Boolean returnValue = (Boolean) createBondMethod.invoke(device);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to bond fail!");
            }
        }
    }
    /**
     * 取消配对（取消配对成功与失败通过广播返回 也就是配对失败）
     * @param device
     */
    public void cancelPinBule(BluetoothDevice device){
        if (device == null){
            Log.d(TAG, "cancel bond device null");
            return;
        }
        if (!isBlueEnable()){
            Log.e(TAG, "Bluetooth not enable!");
            return;
        }
        //判断设备是否配对，没有配对就不用取消了
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            Log.d(TAG, "attemp to cancel bond:" + device.getName());
            try {
                Method removeBondMethod = device.getClass().getMethod("removeBond");
                Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
                returnValue.booleanValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "attemp to cancel bond fail!");
            }
        }
    }
}
