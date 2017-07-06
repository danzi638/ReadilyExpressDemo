package com.example.jiayin.readilyexpressdemo.community.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jiayin.readilyexpressdemo.R;

import org.litepal.LitePalApplication;

public class WeatherAcitivty extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LitePalApplication.initialize(WeatherAcitivty.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_acitivty);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null) != null){
            Intent intent = new Intent(this,WeatherSeeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
