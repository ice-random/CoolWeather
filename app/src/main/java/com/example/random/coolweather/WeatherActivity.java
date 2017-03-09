package com.example.random.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.random.coolweather.gson.Weather;
import com.example.random.coolweather.gson.dailyForecast;
import com.example.random.coolweather.gson.hourlyForecast;
import com.example.random.coolweather.service.AutoUpdateService;
import com.example.random.coolweather.util.HttpUtil;
import com.example.random.coolweather.util.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout dforecastLayout;

    private LinearLayout hforecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    public DrawerLayout drawerLayout;

    private ImageButton choose_area;

    private Button btn_Nav;

    private TextView tv_sunrise, tv_sunset, tv_comfort, tv_carWash,
            tv_dressing, tv_sport, tv_travel, tv_ultraviolet, tv_air, tv_flu;

    private TextView tv_show_sunrise, tv_show_sunset, tv_show_comfort, tv_show_carWash,
            tv_show_dressing, tv_show_sport, tv_show_travel, tv_show_ultraviolet, tv_show_air, tv_show_flu;

    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;

    private String weatherString;

    private SharedPreferences prefs;

    private String weatherId;

    private DrawerLayout drawer_layout;

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //隐藏toolbar的标题
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.category);
        }
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer_layout.closeDrawers();
                return true;
            }
        });


//        btn_Nav= (Button) findViewById(R.id.btn_Nav);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
//        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        dforecastLayout = (LinearLayout) findViewById(R.id.dforecast_layout);
        hforecastLayout = (LinearLayout) findViewById(R.id.hforecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        tv_sunrise = (TextView) findViewById(R.id.tv_sunrise);
        tv_sunset = (TextView) findViewById(R.id.tv_sunset);
        tv_comfort = (TextView) findViewById(R.id.tv_comfort);
        tv_carWash = (TextView) findViewById(R.id.tv_carWash);
        tv_dressing = (TextView) findViewById(R.id.tv_dressing);
        tv_sport = (TextView) findViewById(R.id.tv_sport);
        tv_travel = (TextView) findViewById(R.id.tv_travel);
        tv_ultraviolet = (TextView) findViewById(R.id.tv_ultraviolet);
        tv_air = (TextView) findViewById(R.id.tv_air);
        tv_flu = (TextView) findViewById(R.id.tv_flu);
        tv_show_sunrise = (TextView) findViewById(R.id.tv_show_sunrise);
        tv_show_sunset = (TextView) findViewById(R.id.tv_show_sunset);
        tv_show_comfort = (TextView) findViewById(R.id.tv_show_comfort);
        tv_show_carWash = (TextView) findViewById(R.id.tv_show_carWash);
        tv_show_dressing = (TextView) findViewById(R.id.tv_show_dressing);
        tv_show_sport = (TextView) findViewById(R.id.tv_show_sport);
        tv_show_travel = (TextView) findViewById(R.id.tv_show_travel);
        tv_show_ultraviolet = (TextView) findViewById(R.id.tv_show_ultraviolet);
        tv_show_air = (TextView) findViewById(R.id.tv_show_air);
        tv_show_flu = (TextView) findViewById(R.id.tv_show_flu);
        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        tv_show_sunrise.setTypeface(font);
        tv_show_sunrise.setTypeface(font);
        tv_show_sunset.setTypeface(font);
        tv_show_comfort.setTypeface(font);
        tv_show_carWash.setTypeface(font);
        tv_show_dressing.setTypeface(font);
        tv_show_sport.setTypeface(font);
        tv_show_travel.setTypeface(font);
        tv_show_ultraviolet.setTypeface(font);
        tv_show_air.setTypeface(font);
        tv_show_flu.setTypeface(font);
//        btn_Nav.setTypeface(font);
//        btn_Nav.setText(R.string.category);

        tv_show_sunrise.setText(getResources().getString(R.string.sun_rise));
        tv_show_sunset.setText(getResources().getString(R.string.sun_set));
        tv_show_comfort.setText(getResources().getString(R.string.comfort));
        tv_show_carWash.setText(getResources().getString(R.string.wash_car));
        tv_show_dressing.setText(getResources().getString(R.string.dressing));
        tv_show_sport.setText(getResources().getString(R.string.sport));
        tv_show_travel.setText(getResources().getString(R.string.travel));
        tv_show_ultraviolet.setText(getResources().getString(R.string.ultraviolet));
        tv_show_air.setText(getResources().getString(R.string.air));
        tv_show_flu.setText(getResources().getString(R.string.flu));

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        weatherString = prefs.getString("weather", null);

        Intent intent = this.getIntent();
        String cityId = intent.getStringExtra("cityId");
        Log.d("WeatherActivity", "cityId199: " + cityId);
        if (cityId != null) {
            requestWeather(cityId);
        } else {
            if (weatherString != null) {
                //有缓存时直接解析天气数据
                Weather weather = Utility.handleWeatherResponse(weatherString);
                weatherId = weather.basic.weatherId;
                showWeatherInfo(weather);
            } else {
                //无缓存时去服务器查询天气
                weatherId = getIntent().getStringExtra("weather_id");
                weatherLayout.setVisibility(View.INVISIBLE);
                requestWeather(weatherId);
            }
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     *
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = "https://free-api.heweather.com/v5/weather?city=" + weatherId +
                "&key=e2a9d389ea074e8f99d2ee2d69f9e744";
        Log.d("WeatherActivity", "weatherUrl" + weatherUrl);
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                final String responseText;
                try {
                    responseText = response.body().string();

                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && "ok".equals(weather.status)) {
                                SharedPreferences.Editor editor = PreferenceManager
                                        .getDefaultSharedPreferences(WeatherActivity.this)
                                        .edit();
                                weatherString = prefs.getString("weather", null);
                                editor.putString("weather", responseText);
                                editor.apply();
                                showWeatherInfo(weather);
                                swipeRefresh.setRefreshing(false);
                            } else {
                                Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                                swipeRefresh.setRefreshing(false);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loadBingPic();
    }


    /**
     * 处理并展示Weather实体类中的数据
     *
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        if (weather != null && "ok".equals(weather.status)) {
            String cityName = weather.basic.cityName;
            String degree = weather.now.temperature + "°C";
            String weatherInfo = weather.now.condition.info;
            titleCity.setText(cityName);
            degreeText.setText(degree);
            weatherInfoText.setText(weatherInfo);
            dforecastLayout.removeAllViews();
            for (dailyForecast forecast : weather.dforecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.dforecast_item, dforecastLayout, false);
                TextView dateText = (TextView) view.findViewById(R.id.date_text);
                TextView infoText = (TextView) view.findViewById(R.id.info_text);
                TextView maxText = (TextView) view.findViewById(R.id.max_text);
                TextView minText = (TextView) view.findViewById(R.id.min_text);
                dateText.setText(forecast.date);
                infoText.setText(forecast.condition.day);
                maxText.setText(forecast.temperature.max + "°C");
                minText.setText(forecast.temperature.min + "°C");
                dforecastLayout.addView(view);
            }
            hforecastLayout.removeAllViews();
            for (hourlyForecast forecast : weather.hforecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.hforecast_item, hforecastLayout, false);
                TextView timeText = (TextView) view.findViewById(R.id.tv_time);
                TextView txtText = (TextView) view.findViewById(R.id.tv_txt);
                TextView tmpText = (TextView) view.findViewById(R.id.tv_tmp);
                timeText.setText(forecast.date);
                txtText.setText(forecast.cond.txt);
                tmpText.setText(forecast.tmp + "°C");
                hforecastLayout.addView(view);
            }
            if (weather.aqi != null) {
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }
            tv_sunrise.setText(weather.dforecastList.get(0).astronomy.sunRise);
            tv_sunset.setText(weather.dforecastList.get(0).astronomy.sunSet);
            tv_comfort.setText(weather.suggestion.comfort.brief);
            tv_carWash.setText(weather.suggestion.carWash.brief);
            tv_dressing.setText(weather.suggestion.dressing.brief);
            tv_sport.setText(weather.suggestion.sport.brief);
            tv_travel.setText(weather.suggestion.travel.brief);
            tv_ultraviolet.setText(weather.suggestion.ultraviolet.brief);
            tv_air.setText(weather.suggestion.air.brief);
            tv_flu.setText(weather.suggestion.flu.brief);
            weatherLayout.setVisibility(View.VISIBLE);
            //启动后台更新天气服务
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }


    public void setWeatherId(String id) {
        weatherId = id;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }
}
