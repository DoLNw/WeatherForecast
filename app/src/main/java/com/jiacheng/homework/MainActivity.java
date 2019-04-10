package com.jiacheng.homework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    private static final int UPDATE_TODAY_WEATHER = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather)msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    private void updateTodayWeather(TodayWeather todayWeather) {
        tvCityName.setText(todayWeather.getCity());
        tvCity.setText(todayWeather.getCity() + "天气");
        tvTime.setText(todayWeather.getUpdateTime() + "发布");
        tvHumidity.setText(todayWeather.getHumidity());
        tvWeek.setText(todayWeather.getDate());
        tvPmData.setText(todayWeather.getPM2_5());
        tvPmQuality.setText(todayWeather.getQuality());
        tvTemperature.setText(todayWeather.getMinimumTemperature() + "~" + todayWeather.getMaximumTemperature());
        tvClimate.setText(todayWeather.getType());
        tvWind.setText("风力" + todayWeather.getWindSpeed());

        Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_LONG).show();
    }

    private ImageView btnUpdate;
    private TextView tvCityName;
    private TextView tvCity;
    private TextView tvTime;
    private TextView tvHumidity;
    private TextView tvWeek;
    private TextView tvPmData;
    private TextView tvPmQuality;
    private TextView tvTemperature;
    private TextView tvClimate;
    private TextView tvWind;
    private ImageView imgPm;
    private ImageView imgWeather;

    private ImageView imgSelectCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        eventBinding();

        checkNetState();
    }

    private void initView() {
        btnUpdate = findViewById(R.id.title_update_btn);

        tvCityName = findViewById(R.id.title_city_name);
        tvCity = findViewById(R.id.city);
        tvTime = findViewById(R.id.time);
        tvHumidity = findViewById(R.id.humidity);
        tvWeek = findViewById(R.id.week_today);
        tvPmData = findViewById(R.id.pm_data);
        tvPmQuality = findViewById(R.id.pm2_5_quality);
        tvTemperature = findViewById(R.id.temperature);
        tvClimate = findViewById(R.id.climate);
        tvWind = findViewById(R.id.wind);

        imgPm = findViewById(R.id.pm2_5_img);
        imgWeather = findViewById(R.id.weather_img);

        imgSelectCity = findViewById(R.id.title_city_manager);

        tvCityName.setText("N/A");
        tvCity.setText("N/A");
        tvTime.setText("N/A");
        tvHumidity.setText("N/A");
        tvWeek.setText("N/A");
        tvPmData.setText("N/A");
        tvPmQuality.setText("N/A");
        tvTemperature.setText("N/A");
        tvClimate.setText("N/A");
        tvWind.setText("N/A");
    }

    private void eventBinding() {
        btnUpdate.setOnClickListener(this);
        imgSelectCity.setOnClickListener(this);
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
                Toast.makeText(MainActivity.this, "无法连接网络", Toast.LENGTH_LONG).show();
            }
        } else if(v.getId() == R.id.title_city_manager) {
            Intent intent = new Intent(this, SelectCityActivity.class);
            startActivity(intent);
        }
    }

    private void queryWeather(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;

        Log.d("MiniWeather", address);

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;

                TodayWeather todayWeather = null;

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

                    todayWeather = parseXML(responseStr);
                    if (todayWeather != null) {
                        Log.d("MiniWeather", todayWeather.toString());
                    }
                    //我觉得这四句话应该卸载if里面比较好吧?
                    Message msg = new Message();
                    msg.what = UPDATE_TODAY_WEATHER;
                    msg.obj = todayWeather;
                    mHandler.sendMessage(msg);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    con.disconnect();
                }

            }

        }).start();

    }

    private TodayWeather parseXML(String xmlData) {
        TodayWeather todayWeather = null;

        int windDirection = 0;
        int windSpeed = 0;
        int dateCount = 0;
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
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                //解析城市
                                xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                //解析更新时间
                                xmlPullParser.next();
                                todayWeather.setUpdateTime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                //解析湿度
                                xmlPullParser.next();
                                todayWeather.setHumidity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                //解析温度
                                xmlPullParser.next();
                                todayWeather.setTemperature(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                //解析PM2.5
                                xmlPullParser.next();
                                todayWeather.setPM2_5(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                //解析空气质量
                                xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && windDirection==0) {
                                //解析风向
                                xmlPullParser.next();
                                todayWeather.setWindDirection(xmlPullParser.getText());
                                windDirection++;
                            } else if (xmlPullParser.getName().equals("fengli") && windSpeed==0) {
                                //解析风力
                                xmlPullParser.next();
                                todayWeather.setWindSpeed(xmlPullParser.getText());
                                windSpeed++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount==0) {
                                //解析日期
                                xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && maxmumTemperature==0) {
                                //解析最高温度
                                xmlPullParser.next();
                                todayWeather.setMaximumTemperature(xmlPullParser.getText());
                                maxmumTemperature++;
                            } else if (xmlPullParser.getName().equals("low") && minimunTemperature==0) {
                                //解析最低温度
                                xmlPullParser.next();
                                todayWeather.setMinimumTemperature(xmlPullParser.getText());
                                minimunTemperature++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount==0) {
                                //解析天气情况
                                xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
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

        return todayWeather;
    }

    private void checkNetState() {
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE) {
            Log.d("MiniWeather", "网络OK");
            Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
        } else {
            Log.d("MiniWeather", "无法连接网络");
            Toast.makeText(MainActivity.this, "无法连接网络", Toast.LENGTH_LONG).show();
        }
    }
}
