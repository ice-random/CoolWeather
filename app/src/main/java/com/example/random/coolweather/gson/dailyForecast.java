package com.example.random.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by random on 17/3/5.
 */

public class dailyForecast {
    //        "astro": {   //天文数值
//                "mr": "04:19",   //月升时间
//                "ms": "18:07",   //月落时间
//                "sr": "05:41",   //日出时间
//                "ss": "18:47"    //日落时间
//                  },
    @SerializedName("astro")
    public Astronomy astronomy;

    public class Astronomy {
        @SerializedName("sr")
        public String sunRise;
        @SerializedName("ss")
        public String sunSet;
    }

    //        "cond": {   //天气状况
//                "code_d": "100",   //白天天气状况代码
//                "code_n": "104",  //夜间天气状况代码
//                "txt_d": "晴",   //白天天气状况描述
//                "txt_n": "阴"   //夜间天气状况描述
//                 },
    @SerializedName("cond")
    public Condition condition;

    public class Condition {
        @SerializedName("txt_d")
        public String day;
        @SerializedName("txt_n")
        public String night;
    }

    //"date": "2016-08-31",  //预报日期
    public String date;
    //            "hum": "17",  //相对湿度（%）
//            "pcpn": "0.0",  //降水量（mm）
//            "pop": "1",  //降水概率
    public String pop;
//            "pres": "997",  //气压

    //        "tmp": {   //温度
    //                "max": "33",   //最高温度
    //                "min": "19"   //最低温度
    //                },
    @SerializedName("tmp")
    public Temperature temperature;

    public class Temperature {
        public String max;
        public String min;
    }
//                "vis": "10",   //能见度（km）
//                "deg": "342",   //风向（360度）
//                "dir": "北风",  //风向
//                "sc": "3-4",   //风力等级
//                "spd": "10"   //风速（kmph）
//                      }
    public Wind wind;

    public class Wind {
        @SerializedName("dir")
        public String direction;
        @SerializedName("sc")
        public String power;
    }
}
