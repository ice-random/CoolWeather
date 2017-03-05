package com.example.random.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by random on 17/2/16.
 */

public class Weather {
    //    HeWeather5: [
//    {
//        aqi: {},
//        basic: {},
//        daily_forecast: [],
//        hourly_forecast: [],
//        now: {},
//        status: "ok",
//                suggestion: {}
//    }
//    ]
    public Alarm alarms;
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<dailyForecast> dforecastList;
    @SerializedName("hourly_forecast")
    public List<hourlyForecast> hforecastList;
}
