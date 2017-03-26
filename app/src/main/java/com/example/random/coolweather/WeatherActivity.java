package com.example.random.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;

    @BindView(R.id.title_city)
    TextView titleCity;

    @BindView(R.id.degree_text)
    TextView degreeText;

    @BindView(R.id.weather_info_text)
    TextView weatherInfoText;

    @BindView(R.id.dforecast_layout)
    LinearLayout dforecastLayout;

    @BindView(R.id.hforecast_layout)
    LinearLayout hforecastLayout;

    @BindView(R.id.aqi_text)
    TextView aqiText;

    @BindView(R.id.pm25_text)
    TextView pm25Text;

    @BindView(R.id.tv_sunrise)
    TextView tv_sunrise;

    @BindView(R.id.tv_sunset)
    TextView tv_sunset;

    @BindView(R.id.tv_comfort)
    TextView tv_comfort;

    @BindView(R.id.tv_carWash)
    TextView tv_carWash;

    @BindView(R.id.tv_dressing)
    TextView tv_dressing;

    @BindView(R.id.tv_sport)
    TextView tv_sport;

    @BindView(R.id.tv_travel)
    TextView tv_travel;

    @BindView(R.id.tv_ultraviolet)
    TextView tv_ultraviolet;

    @BindView(R.id.tv_air)
    TextView tv_air;

    @BindView(R.id.tv_flu)
    TextView tv_flu;


    @BindView(R.id.tv_show_sunrise)
    TextView tv_show_sunrise;

    @BindView(R.id.tv_show_sunset)
    TextView tv_show_sunset;

    @BindView(R.id.tv_show_comfort)
    TextView tv_show_comfort;

    @BindView(R.id.tv_show_carWash)
    TextView tv_show_carWash;

    @BindView(R.id.tv_show_dressing)
    TextView tv_show_dressing;

    @BindView(R.id.tv_show_sport)
    TextView tv_show_sport;

    @BindView(R.id.tv_show_travel)
    TextView tv_show_travel;

    @BindView(R.id.tv_show_ultraviolet)
    TextView tv_show_ultraviolet;

    @BindView(R.id.tv_show_air)
    TextView tv_show_air;

    @BindView(R.id.tv_show_flu)
    TextView tv_show_flu;

    @BindView(R.id.bing_pic_img)
     ImageView bingPicImg;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_editCity)
    Button btn_editCity;

    private String weatherString;

    private SharedPreferences prefs;

    private String weatherId;

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
        ButterKnife.bind(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //隐藏toolbar的标题
        btn_editCity.setOnClickListener(this);
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
            weatherId = cityId;
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
            int hour = prefs.getInt("hour", 0);
            if (hour != 0) {
                Intent intent = new Intent(this, AutoUpdateService.class);
                intent.putExtra("hour", hour);
                startService(intent);
            }
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editCity:
                //清空pref中的内容，切换城市
                SharedPreferences.Editor editor = PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this)
                        .edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
