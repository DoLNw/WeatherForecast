package com.jiacheng.homework;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUpdate = findViewById(R.id.title_update_btn);
        btnUpdate.setOnClickListener(this);

        checkNetState();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101210701");
            Log.d("MiniWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("MiniWeather", "网络OK");
                queryWeather(cityCode);
            } else {
                Log.d("MiniWeather", "无法连接网络");
                Toast.makeText(MainActivity.this, "无法连接网络", Toast.LENGTH_LONG);
            }
        }
    }

    private void queryWeather(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;

        Log.d("MiniWeather", address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);

                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();

                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("MiniWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("MiniWeather", responseStr);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    con.disconnect();
                }

            }
        }).start();

    }

    private void checkNetState() {
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("MiniWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG);
        } else {
            Log.d("MiniWeather", "无法连接网络");
            Toast.makeText(MainActivity.this, "无法连接网络", Toast.LENGTH_LONG);
        }
    }
}
