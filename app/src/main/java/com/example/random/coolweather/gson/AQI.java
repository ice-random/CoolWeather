package com.example.random.coolweather.gson;

/**
 * Created by random on 17/2/15.
 */

public class AQI {
//
//    "city": {
//        "aqi": "60",
//        "co": "0",
//        "no2": "14",
//        "o3": "95",
//        "pm10": "67",
//        "pm25": "15",
//        "qlty": "良",  //共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
//        "so2": "10"
//    }
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
        public String quality;
    }
}
