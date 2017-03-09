package com.example.random.coolweather.db;

import org.litepal.crud.DataSupport;

import java.net.IDN;

/**
 * Created by random on 17/3/9.
 */

public class db_City extends DataSupport{

    private String city;

    private String num;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
