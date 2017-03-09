package com.example.random.coolweather;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.random.coolweather.Adapter.CityAdapter;
import com.example.random.coolweather.db.db_City;
import com.example.random.coolweather.gson.City;
import com.example.random.coolweather.gson.Weather;
import com.example.random.coolweather.util.HttpUtil;
import com.example.random.coolweather.util.Utility;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    private SearchView sv_search_city;

    private List<db_City> cityList = new ArrayList<>();

    private TextView tv_empty_city;

//    private String[] cityData={"温州"};

    private ListView lv_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null)!=null){
            Intent intent=new Intent(MainActivity.this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        cityList = new ArrayList<>();
        tv_empty_city = (TextView) findViewById(R.id.tv_empty_city);
        sv_search_city = (SearchView) findViewById(R.id.sv_search_city);
        lv_city = (ListView) findViewById(R.id.lv_city);
        sv_search_city.setOnQueryTextListener(this);
        Connector.getDatabase();
        loadCity();
        lv_city.setOnItemClickListener(this);
    }


    private void loadCity() {
        cityList = DataSupport.findAll(db_City.class);
        CityAdapter adapter = new CityAdapter(MainActivity.this, R.layout.city_item, cityList);
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
        searchCity();
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
    private void searchCity() {

        String cityName = sv_search_city.getQuery().toString();
        Log.d("MainActivity", cityName);
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
                    Log.d("MainActivity", "onResponse:responseText" + responseText);
                    final City city = Utility.handleCityResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (city != null && "ok".equals(city.status)) {
                                db_City cityData = new db_City();
                                cityData.setNum(city.basic.ID);
                                cityData.setCity(city.basic.cityName);
                                Log.d("MainActivity", "138" + city.basic.cityName);
                                cityData.save();
//                                Log.d("MainActivity", "显示该城市信息是否保存，true保存，false不保存" + String.valueOf(a));
                                Log.d("MainActivity", "查询第一条数据的城市名" + DataSupport.findFirst(db_City.class).getCity());
                                loadCity();
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
     * listview的监听器
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        db_City city=cityList.get(position);
        Intent intent=new Intent(MainActivity.this, WeatherActivity.class);
        Log.d("MainActivity","cityId"+city.getNum());
        Log.d("MainActivity","cityId"+city.getCity());
        intent.putExtra("cityId",city.getNum());
        startActivity(intent);
        finish();
    }
}
