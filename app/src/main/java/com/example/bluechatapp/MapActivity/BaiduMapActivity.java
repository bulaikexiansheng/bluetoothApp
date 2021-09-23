package com.example.bluechatapp.MapActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.bluechatapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 组件声明
     */
    private FloatingActionButton backBtn ;
    private FloatingActionButton backMyLocationBtn ;
    private MapView mMapView = null;
    // 地图总控制器
    private BaiduMap baiduMap ;
    private boolean isFirstLocate = true ;
    private LocationClient mLocationClient ;
    private BDLocation currentLocation ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        // 初始化控件view
        findViewForComponent() ;
        registerOnClickEvents() ;
        permissionRequest();
    }

    private void registerOnClickEvents() {
        backBtn.setOnClickListener(this);
        backMyLocationBtn.setOnClickListener(this);
    }

    // 初始化组件、配置组件
    private void findViewForComponent() {
        backBtn = findViewById(R.id.map_back_btn) ;
        backMyLocationBtn = findViewById(R.id.map_back_to_src_btn) ;
        mMapView = findViewById(R.id.baidu_mapview);
        baiduMap = mMapView.getMap() ;
        baiduMap.setMyLocationEnabled(true) ;
        mLocationClient = new LocationClient(getApplicationContext()) ;
        mLocationClient.registerLocationListener(new MyLocationListener());
    }
    private void navigationToCurrentLocation(BDLocation location){
        if (isFirstLocate){
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude()) ;
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll) ;
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f) ;
            baiduMap.animateMapStatus(update);
            isFirstLocate = false ;
        }
        currentLocation = location ;
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder() ;
        locationBuilder.latitude(location.getLatitude()) ;
        locationBuilder.longitude(location.getLongitude()) ;
        MyLocationData locationData = locationBuilder.build() ;
        baiduMap.setMyLocationData(locationData) ;
    }
    private void permissionRequest(){
        List<String> permissionList = new ArrayList<>();
        //如果没有启动下面权限，就询问用户让用户打开
        if(ContextCompat.checkSelfPermission(BaiduMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(BaiduMapActivity.this,Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED)
        {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(BaiduMapActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(BaiduMapActivity.this, permissions, 1);
        }
        else {
            requestLocation();
        }
    }
    /*初始化函数，并启动位置客户端LocationClient*/
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }
    /*初始化函数*/
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }
    /*只有同意打开相关权限才可以开启本程序*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            currentLocation = bdLocation ;
            if (bdLocation.getLocType()==BDLocation.TypeGpsLocation||bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
                navigationToCurrentLocation(bdLocation);
            }
        }
    }
    private void chooseMyLocation(double la,double lo) {
        LatLng latLng = new LatLng(la, lo);
        MapStatusUpdate msu  = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mMapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_back_btn:{
                finish();
                break ;
            }
            case R.id.map_back_to_src_btn:{
                chooseMyLocation(currentLocation.getLatitude(),currentLocation.getLongitude());
                break ;
            }
            default:
                break ;
        }
    }
}