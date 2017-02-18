package com.example.random.coolweather.gson;

/**
 * Created by random on 17/2/15.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
