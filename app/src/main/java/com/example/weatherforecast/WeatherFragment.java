package com.example.weatherforecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherforecast.model.WeatherItem;

public class WeatherFragment extends VisibleFragment {

    private static final String ARG_WEATHER_ITEM = "weather_item";

    private WeatherItem mWeatherItem;

    private TextView mWeather;
    private TextView mDate;
    private TextView mWeekDate;
    private TextView mTempMax;
    private TextView mTempMin;
    private TextView mHumidity;
    private TextView mPressure;
    private TextView mWind;
    private ImageView mWeatherIcon;

    public static WeatherFragment newInstance(WeatherItem weatherItem) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEATHER_ITEM, weatherItem);

        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherItem = (WeatherItem) getArguments().getSerializable(ARG_WEATHER_ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_page, container, false);
        mWeather = (TextView) view.findViewById(R.id.page_weather);
        mDate = (TextView) view.findViewById(R.id.page_date);
        mWeekDate = (TextView) view.findViewById(R.id.page_week_date);
        mTempMax = (TextView) view.findViewById(R.id.page_temp_max);
        mTempMin = (TextView) view.findViewById(R.id.page_temp_min);
        mHumidity = (TextView) view.findViewById(R.id.page_humidity);
        mPressure = (TextView) view.findViewById(R.id.page_pressure);
        mWind = (TextView) view.findViewById(R.id.page_wind);
        mWeatherIcon = (ImageView) view.findViewById(R.id.page_weather_icon);

        updateUI();

        return view;
    }

    //更新页面
    private void updateUI() {
        mWeather.setText(mWeatherItem.getWeather());
        mDate.setText(mWeatherItem.getDate());
        mWeekDate.setText(mWeatherItem.getWeekDate());
        mTempMax.setText(mWeatherItem.getTempMax());
        mTempMin.setText(mWeatherItem.getTempMin());
        mHumidity.setText(mWeatherItem.getHumidity());
        mPressure.setText(mWeatherItem.getPressure());
        mWind.setText(String.format("%s %s", mWeatherItem.getWindSpeed(), mWeatherItem.getWindDir()));
        mWeatherIcon.setImageResource(mWeatherItem.getIcon());
    }
}
