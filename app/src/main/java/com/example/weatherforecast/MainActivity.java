package com.example.weatherforecast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weatherforecast.model.WeatherItem;

public class MainActivity extends SingleFragmentActivity implements WeatherListFragment.Callbacks {

    private static final String TAG = "MainActivity";
    private static final String EXTRA_WEATHER_ITEM = "com.example.weatherforecast.weather_item";

    private TextView mTopWeather;
    private TextView mTopDate;
    private TextView mTopTempMax;
    private TextView mTopTempMin;
    private ImageView mTopWeatherIcon;
    private RelativeLayout mTopLayout;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new WeatherListFragment();
    }

    //设置mainActivity的layout_xml
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果为手机，则需要显示顶部栏
        if (isPhone()) {
            mTopWeather = (TextView) findViewById(R.id.top_weather);
            mTopDate = (TextView) findViewById(R.id.top_date);
            mTopTempMax = (TextView) findViewById(R.id.top_temp_max);
            mTopTempMin = (TextView) findViewById(R.id.top_temp_min);
            mTopWeatherIcon = (ImageView) findViewById(R.id.top_weather_icon);
            mTopLayout = (RelativeLayout) findViewById(R.id.layout_top);
            //设置顶部layout点击响应，用于更新天气信息
            mTopLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WeatherListFragment fragment = (WeatherListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment.updateWeather();
                }
            });
        }
    }

    //回调函数
    //WeatherListFragment更新天气信息后，需更新MainActivity的内容
    @Override
    public void onWeatherUpdate() {
        WeatherListFragment fragment = (WeatherListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        WeatherItem weatherItem = fragment.getTodayWeather();
        //分类讨论
        if (isPhone()) {
            updateTopUI(weatherItem);
        } else {
            updateDetailUI(weatherItem);
        }
    }

    //回调函数
    //点击响应
    @Override
    public void onWeatherItemSelected(WeatherItem weatherItem) {
        if (weatherItem == null) {
            return;
        }
        if(isPhone()) {
            Intent intent = WeatherActivity.newIntent(this, weatherItem);
            startActivity(intent);
        } else {
            updateDetailUI(weatherItem);
        }
    }

    //回调函数
    //判断当前设备类型
    @Override
    public boolean isPhone() {
        return findViewById(R.id.fragment_detail) == null;
    }

    //更新顶部天气栏
    //只有手机需要使用该方法
    private void updateTopUI(WeatherItem weatherItem) {
        if (weatherItem == null) {
            return;
        }
        mTopWeather.setText(weatherItem.getWeather());
        mTopDate.setText(weatherItem.getTopDate());
        mTopTempMax.setText(weatherItem.getTempMax());
        mTopTempMin.setText(weatherItem.getTempMin());
        mTopWeatherIcon.setImageResource(weatherItem.getTopIcon());
    }

    //更新Detail
    //只有平板需要使用该方法
    private void updateDetailUI(WeatherItem weatherItem) {
        if (weatherItem == null) {
            return;
        }
        Fragment fragment = WeatherFragment.newInstance(weatherItem);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_detail, fragment).commit();
    }
}
