package com.example.random.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.random.coolweather.gson.City;
import com.example.random.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utility {


    /**
     * 将返回的JSON数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();

            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解析查询城市返回的城市信息
     */
    public static City handleCityResponse(String response){
        Log.d("Utility",response);
        Log.d("Utility", String.valueOf(!TextUtils.isEmpty(response)));
        if (!TextUtils.isEmpty(response)){
            try {
                JSONObject jsonObject=new JSONObject(response);
                Log.d("MainActivity",jsonObject.getJSONArray("HeWeather5").getJSONObject(0).toString());
                return new Gson().fromJson(jsonObject.getJSONArray("HeWeather5").getJSONObject(0).toString(),City.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
