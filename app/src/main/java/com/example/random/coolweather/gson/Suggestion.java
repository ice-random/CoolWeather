package com.example.random.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.io.Flushable;

/**
 * Created by random on 17/2/16.
 */

public class Suggestion {
    //    suggestion: {
//        air: {
//            brf: "中",
//                    txt: "气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"},
//        comf: {
//            brf: "较舒适",
//                    txt: "白天会有降雨，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。"},
//        cw: {
//            brf: "不宜",
//                    txt: "不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},
//        drsg: {
//            brf: "较冷",
//                    txt: "建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},
//        flu: {
//            brf: "极易发",
//                    txt: "将有一次强降温过程，且空气湿度较大，极易发生感冒，请特别注意增加衣服保暖防寒。"},
//        sport: {
//            brf: "较不宜",
//                    txt: "有降水，且风力较强，推荐您在室内进行低强度运动；若坚持户外运动，请注意保暖并携带雨具。"},
//        trav: {
//            brf: "适宜",
//                    txt: "有降水，虽然风稍大，但温度适宜，适宜旅游，可不要错过机会呦！"},
//        uv: {
//            brf: "最弱",
//                    txt: "属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}
//    }
    @SerializedName("air")
    public Air air;

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("drsg")
    public Drsg dressing;

    public Flu flu;

    public Sport sport;

    @SerializedName("trav")
    public Travel travel;

    @SerializedName("uv")
    public Ultraviolet ultraviolet;


    public class Comfort {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Sport {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Air {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Drsg {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Flu {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Travel {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }

    public class Ultraviolet {
        @SerializedName("brf")
        public String brief;
        @SerializedName("txt")
        public String info;
    }
}
