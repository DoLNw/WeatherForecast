package com.jiacheng.homework;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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

                    parseXML(responseStr);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    con.disconnect();
                }

            }

        }).start();

    }

    private void parseXML(String xmlData) {
        int windDirection = 0;
        int windSpeed = 0;
        int dataCount = 0;
        int maxmumTemperature = 0;
        int minimunTemperature = 0;
        int typeCount = 0;

        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();

            Log.d("MiniWeather", "parseXML");

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //处理文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")) {
                            //解析城市
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "city:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            //解析更新时间
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "updatetime:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            //解析湿度
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "湿度:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            //解析温度
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "温度:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            //解析PM2.5
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "PM2.5:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("uquality")) {
                            //解析空气质量
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "空气质量:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengxiang")) {
                            //解析风向
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "风向:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengli")) {
                            //解析风力
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "风力:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("data")) {
                            //解析日期
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "日期:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("high")) {
                            //解析最高温度
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "高温:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("low")) {
                            //解析最低温度
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "最低温度:"+xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("type")) {
                            //解析天气情况
                            eventType = xmlPullParser.next();
                            Log.d("MiniWeather", "天气情况:"+xmlPullParser.getText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
