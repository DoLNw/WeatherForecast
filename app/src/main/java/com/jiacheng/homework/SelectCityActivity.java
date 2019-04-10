package com.jiacheng.homework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
    ImageView ivBtnBack;
    TextView tvSelectCityName;
    TextView tvSelectCityCode;

    ListView myList;
    List<City> cityList;
    List<String> cityName;
    ArrayAdapter<String> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);

        initViews();

        processListener();
    }

    private void initViews() {
        ivBtnBack = findViewById(R.id.title_back);
        myList = findViewById(R.id.city_list);
        tvSelectCityName = findViewById(R.id.tvSelectCityName);
        tvSelectCityCode = findViewById(R.id.tvSelectCityCode);
        cityList = ((MyApplication) getApplication()).getCityList();
    }

    private void processListener() {
        prepareList();
        listenListCity();
        listenBtnBackClick();
    }

    private void prepareList() {
        cityName = new ArrayList<>();

        for (City city: cityList) {
            cityName.add(city.getCity() + " " + city.getNumber());
        }

        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityName);

        myList.setAdapter(myAdapter);
    }

    private void listenListCity() {
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sSelect[] = cityName.get(position).split(" ");
                Log.i("MiniWeather", sSelect.toString());

                tvSelectCityName.setText(sSelect[0]);
                tvSelectCityCode.setText(sSelect[1]);
            }
        });
    }

    private void listenBtnBackClick() {

    }



}
