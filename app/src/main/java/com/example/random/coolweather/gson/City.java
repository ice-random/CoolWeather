package com.example.random.coolweather.gson;


import com.google.gson.annotations.SerializedName;

/**
 * Created by random on 17/2/12.
 */

public class City {

    public Basic basic;

    public class Basic {
        @SerializedName("city")
        public String cityName;
        @SerializedName("id")
        public String ID;

    }

    public String status;

}
