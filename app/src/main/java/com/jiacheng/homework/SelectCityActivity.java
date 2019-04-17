package com.jiacheng.homework;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
    private ImageView ivBtnBack;
    private TextView tvSelectCityName;
    private TextView tvSelectCityCode;
    private ClearEditText mClearText;
    private TextView titleName;

    private ListView myList;
    private List<City> cityList;
    private List<String> cityName;
    private ArrayAdapter<String> myAdapter;

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
        titleName = findViewById(R.id.title_name);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String cityName = sharedPreferences.getString("main_city_name", "101210701");
        titleName.setText("当前城市："+cityName);

        cityList = ((MyApplication) getApplication()).getCityList();

        mClearText = findViewById(R.id.setSearchCity);
    }

    private void processListener() {
        prepareList();
        listenListCity();
        listenBtnBackClick();

        listClearEdit();
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
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSelectCityCode.getText().length() <= 0 || tvSelectCityName.getText().length() <= 0) {
                    Toast.makeText(SelectCityActivity.this, "城市未更新", Toast.LENGTH_SHORT).show();
                }

                Intent intent = getIntent();
                intent.putExtra("cityname", tvSelectCityName.getText().toString());
                intent.putExtra("citycode", tvSelectCityCode.getText().toString());
                setResult(0, intent);
                finish();
            }
        });
    }

    private void listClearEdit() {
        mClearText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCity(s.toString());
                myList.setAdapter(myAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (mClearText.hasFocus()) {
                    mClearText.clearFocus();  //它这个有两层，一层键盘弹掉，下一层闪烁光标消失。
                    return true;
                } else {
                    if (tvSelectCityCode.getText().length() <= 0 || tvSelectCityName.getText().length() <= 0) {
                        Toast.makeText(SelectCityActivity.this, "城市未更新", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = getIntent();
                    intent.putExtra("cityname", tvSelectCityName.getText().toString());
                    intent.putExtra("citycode", tvSelectCityCode.getText().toString());
                    setResult(0, intent);
                    finish();
                    return true;
                }
            }
        }

        return false;
//        return super.onKeyDown(keyCode, event);
    }

    private void filterCity(String filterString) {
        cityName.clear();
        if (TextUtils.isEmpty(filterString)) {
            for (City city: cityList) {
                cityName.add(city.getCity() + " " + city.getNumber());
            }
        } else {
            for (City city : cityList) {
                if (city.getCity().contains(filterString)) {
                    cityName.add(city.getCity() + " " + city.getNumber());
                }
            }
            myAdapter.notifyDataSetChanged();
        }
    }

}
