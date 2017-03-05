package com.example.random.coolweather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by random on 17/3/5.
 */

public class LBSGetCity {
//    public String city;
//
//    public LocationClient mLocationClient;
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            city = (String) msg.obj;
//        }
//    };
//
//    public void getPermission(){
////        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(new MyLocationListener());
//        List<String> permissionList = new ArrayList<>();
//        if (ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.
//                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.
//                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(LBSActivity.this, Manifest.
//                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//        if (!permissionList.isEmpty()) {
//            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//            ActivityCompat.requestPermissions(LBSActivity.this, permissions, 1);
//        } else {
//            requestLocation();
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults.length > 0) {
//                    for (int result : grantResults) {
//                        if (result != PackageManager.PERMISSION_GRANTED) {
////                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
//                            finish();
//                            return;
//                        }
//                    }
//                    requestLocation();
//                } else {
////                    Toast.makeText(this, "发现未知错误", Toast.LENGTH_SHORT).show();
////                    finish();
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void requestLocation() {
//        initLocation();
//        mLocationClient.start();
//    }
//
//    private void initLocation() {
//        LocationClientOption option = new LocationClientOption();
//        option.setScanSpan(1000);
//        option.setIsNeedAddress(true);
//        mLocationClient.setLocOption(option);
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public class MyLocationListener implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation bdLocation) {
//            String mCity = bdLocation.getCity();
//            String mCounty = bdLocation.getDistrict();
//            Log.d("MainActivity", "城市名：" + mCity);
//            Log.d("MainActivity", "区名：" + mCounty);
//            Message msg = new Message();
//            msg.obj = mCity;
//            handler.sendMessage(msg);
//            msg.obj = mCounty;
//            handler.sendMessage(msg);
//        }
//
//        @Override
//        public void onConnectHotSpotMessage(String s, int i) {
//
//        }
//    }
}
