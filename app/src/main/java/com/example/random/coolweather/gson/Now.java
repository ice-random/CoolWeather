package com.example.random.coolweather.gson;

import android.view.Window;

import com.google.gson.annotations.SerializedName;

/**
 * Created by random on 17/2/15.
 */

public class Now {
    //    "now": {  //实况天气
//        "cond": {  //天气状况
//            "code": "104",  //天气状况代码
//                    "txt": "阴"  //天气状况描述
//        },
//                "fl": "11",  //体感温度
//                "hum": "31",  //相对湿度（%）
//                "pcpn": "0",  //降水量（mm）
//                "pres": "1025",  //气压
//                "tmp": "13",  //温度
//                "vis": "10",  //能见度（km）
//         "wind": {  //风力风向
//              "deg": "40",  //风向（360度）
//              "dir": "东北风",  //风向
//              "sc": "4-5",  //风力
//              "spd": "24"  //风速（kmph）
//        }
//    },
    //体感温度
    @SerializedName("fl")
    public String sensibleTemperature;

    //相对湿度
    @SerializedName("hum")
    public String humidity;

    //能见度
    @SerializedName("vis")
    public String visibility;

    //风力风向
    public Wind wind;

    //温度
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public Condition condition;

    public class Condition {
        @SerializedName("code")
        public String conditionCode;
        @SerializedName("txt")
        public String info;
    }

    public class Wind {
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String power;
    }
}
