package com.example.random.coolweather;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.random.coolweather.Adapter.CityAdapter;
import com.example.random.coolweather.customizeView.SearchViewCustom;
import com.example.random.coolweather.db.db_City;
import com.example.random.coolweather.gson.City;
import com.example.random.coolweather.util.HttpUtil;
import com.example.random.coolweather.util.Utility;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {

    private static final String TAG = "MainActivity";

    @BindView(R.id.sv_search_city)
    SearchView sv_search_city;

    private List<db_City> cityList = new ArrayList<>();

    @BindView(R.id.lv_city)
    ListView lv_city;

    private CityAdapter adapter;

    private db_City cityData;


    /**
     * 搜索栏没有显示
     */
    private static final int NOT_STARTED_SEARCH = 0;

    /**
     * 搜索栏显示出来，未输入文字，已经准备好搜索
     */
    private static final int READY_SEARCH = 1;

    /**
     * 搜索栏内输入了文字，点击搜索按钮即可搜索城市名
     */
    private static final int SEARCHING = 2;

    /**
     * 搜索栏状态
     */
    private static int search_status = NOT_STARTED_SEARCH;

    private LocationClient mLocationClient;

    @BindView(R.id.btn_location_client)
    FloatingActionButton btn_location_client;

    @BindView(R.id.btn_search)
    ImageButton btn_search;

    @BindView(R.id.mCoordinatorLayout)
    View mCoordinatorLayout;

    @BindView(R.id.SearchView)
    SearchViewCustom searchViewCustom;

    @BindView(R.id.btn_back)
    ImageButton btn_back;

    @BindView(R.id.et_input)
    EditText ed_input;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.btn_delete)
    ImageButton btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.search);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather", null) != null) {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        cityList = new ArrayList<>();
        ButterKnife.bind(this);
        btn_back.setOnClickListener(this);
        btn_location_client.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        lv_city.setOnItemClickListener(this);
        lv_city.setOnItemLongClickListener(this);
        sv_search_city.setOnQueryTextListener(this);
        ed_input.setOnEditorActionListener(this);
        ed_input.addTextChangedListener(this);
        btn_delete.setOnClickListener(this);
        /**
         * 创建数据库
         */
        Connector.getDatabase();
        /**
         * 加载已经保存的城市列表
         */
        loadCity();

        /**
         * 检查定位权限
         */
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }


    /**
     * 开始定位
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    /**
     * 定位初始化
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    /**
     * 从SQLite中加载城市列表
     */
    private void loadCity() {
        cityList = DataSupport.findAll(db_City.class);
        adapter = new CityAdapter(MainActivity.this, R.layout.city_item, cityList);
        lv_city.setAdapter(adapter);
    }

    /**
     * 当点击搜索按钮时执行的方法
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        String cityName = sv_search_city.getQuery().toString();
        searchCity(cityName, null);
        return false;
    }


    /**
     * 当文字改变的时候执行的方法
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    /**
     * 查询城市
     */
    private void searchCity(String cityName, String districtName) {
        Log.d(TAG, "searchCity: " + cityName);
        String url = "https://free-api.heweather.com/v5//search?city="
                + cityName + "&key=e2a9d389ea074e8f99d2ee2d69f9e744";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "获取城市失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    final String responseText = response.body().string();
                    Log.d(TAG, "onResponse: responseText" + responseText);
                    final City city = Utility.handleCityResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (city != null && "ok".equals(city.status)) {
                                List<db_City> cities = DataSupport
                                        .where("city = ?", city.basic.cityName)
                                        .find(db_City.class);
                                if (cities.size() == 0) {
                                    cityData = new db_City();
                                    cityData.setNum(city.basic.ID);
                                    cityData.setCity(city.basic.cityName);
                                    cityData.save();
                                    Log.d(TAG, "run: " + "查询第一条数据的城市名" + DataSupport.findFirst(db_City.class).getCity());
                                    loadCity();
                                } else {
                                    Toast.makeText(MainActivity.this, "已存在该城市", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "获取城市失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 若权限未同意则退出
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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
                    Toast.makeText(this, "发现未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }


    /**
     * listView的监听器
     * 当点击某个城市时进入天气界面
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        db_City city = cityList.get(position);
        Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
        Log.d("MainActivity", "cityId" + city.getNum());
        Log.d("MainActivity", "cityId" + city.getCity());
        intent.putExtra("cityId", city.getNum());
        startActivity(intent);
        finish();
    }


    /**
     * 长按删除listView中的天气数据
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(db_City.class, "City = ?", cityList.get(position).getCity());
                        adapter.notifyDataSetChanged();
                        loadCity();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location_client:
                requestLocation();
                break;
            case R.id.btn_search:
                if (search_status == NOT_STARTED_SEARCH) {
                    //搜索栏已经出现
                    search_status = READY_SEARCH;
                    ChangeSearchStatus();
                } else {
                    search_status = NOT_STARTED_SEARCH;
                    ChangeSearchStatus();
                    String cityName = ed_input.getText().toString();
                    searchCity(cityName, null);
                }
                break;
            case R.id.btn_back:
                //搜索栏消失
                search_status = NOT_STARTED_SEARCH;
                ChangeSearchStatus();
                break;
            case R.id.btn_delete:
                search_status = READY_SEARCH;
                ChangeSearchStatus();
            default:
                break;
        }
    }

    private void ChangeSearchStatus() {
        if (search_status == NOT_STARTED_SEARCH) {
            //搜索栏未显示
            tv_title.setVisibility(VISIBLE);
            searchViewCustom.setVisibility(View.INVISIBLE);
        } else if (search_status == READY_SEARCH) {
            //搜索栏刚刚显示出来，尚未输入文字
            tv_title.setVisibility(View.INVISIBLE);
            searchViewCustom.setVisibility(VISIBLE);
            btn_delete.setVisibility(View.INVISIBLE);
            ed_input.setText("");
        } else if (search_status == SEARCHING) {
            btn_delete.setVisibility(VISIBLE);
        }
    }

    /**
     * 点击软键盘上的搜索按钮，搜索城市
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            Log.d(TAG, "onEditorAction: v.getText()" + v.getText());
            String cityName = v.getText().toString();
            searchCity(cityName, null);
            search_status=NOT_STARTED_SEARCH;
            ChangeSearchStatus();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (search_status==NOT_STARTED_SEARCH){
            finish();
        }else {
            search_status=NOT_STARTED_SEARCH;
            ChangeSearchStatus();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d(TAG, "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, "onTextChanged: ");
        if (ed_input.getText().toString().equals("")) {
            btn_delete.setVisibility(View.INVISIBLE);
        } else {
            btn_delete.setVisibility(VISIBLE);
        }
    }

    /**
     * 当输入框中有文字，显示删除按钮。
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged: ");
    }

    /**
     * 位置监听器
     */
    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String mCity = bdLocation.getCity();
            String mDistrict = bdLocation.getDistrict();
            searchCity(mCity, mDistrict);
            Snackbar.make(mCoordinatorLayout, "您定位在" + mCity + mDistrict, Snackbar.LENGTH_SHORT).show();
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }


}
