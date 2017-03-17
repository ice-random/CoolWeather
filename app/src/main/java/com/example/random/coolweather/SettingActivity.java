package com.example.random.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.random.coolweather.service.AutoUpdateService;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    Button btn_back;
    RadioGroup rg_renovate;

    RadioButton renovate_none;
    RadioButton renovate_three;
    RadioButton renovate_six;
    RadioButton renovate_halfDay;
    RadioButton renovate_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        btn_back = (Button) findViewById(R.id.btn_back);
        rg_renovate = (RadioGroup) findViewById(R.id.rg_renovate);
        renovate_none = (RadioButton) findViewById(R.id.renovate_none);
        renovate_three = (RadioButton) findViewById(R.id.renovate_three);
        renovate_six = (RadioButton) findViewById(R.id.renovate_six);
        renovate_halfDay = (RadioButton) findViewById(R.id.renovate_halfDay);
        renovate_day = (RadioButton) findViewById(R.id.renovate_day);
        btn_back.setOnClickListener(this);
        rg_renovate.setOnCheckedChangeListener(this);
        rg_renovate.check(R.id.renovate_none);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int hour = 0;
        switch (checkedId) {
            case R.id.renovate_none:
                hour = 0;
                sendHour(hour);
                break;
            case R.id.renovate_three:
                hour = 3;
                sendHour(hour);
                break;
            case R.id.renovate_six:
                hour = 6;
                sendHour(hour);
                break;
            case R.id.renovate_halfDay:
                hour = 12;
                sendHour(hour);
                break;
            case R.id.renovate_day:
                hour = 24;
                sendHour(hour);
                break;
            default:
                break;
        }

    }

    private void sendHour(int hour) {
        if (hour != 0) {
            Intent intent = new Intent(this, AutoUpdateService.class);
            intent.putExtra("hour", hour);
            startService(intent);
            Log.d("SettingActivity","启动了服务"+hour);
        }
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(SettingActivity.this)
                .edit();
        editor.putInt("hour", hour);
        editor.apply();
    }
}
