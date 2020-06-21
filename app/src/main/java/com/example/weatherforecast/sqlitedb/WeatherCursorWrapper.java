package com.example.weatherforecast.sqlitedb;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.weatherforecast.sqlitedb.WeatherDbSchema.WeatherTable;
import com.example.weatherforecast.model.WeatherItem;


public class WeatherCursorWrapper extends CursorWrapper {
    public WeatherCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WeatherItem getWeatherItem() {
        String weather = getString(getColumnIndex(WeatherTable.Cols.WEATHER));
        int weatherId = getInt(getColumnIndex(WeatherTable.Cols.WEATHER_ID));
        String tempMax = getString(getColumnIndex(WeatherTable.Cols.TEMP_MAX));
        String tempMin = getString(getColumnIndex(WeatherTable.Cols.TEMP_MIN));
        String humidity = getString(getColumnIndex(WeatherTable.Cols.HUMIDITY));
        String pressure = getString(getColumnIndex(WeatherTable.Cols.PRESSURE));
        String windSpeed = getString(getColumnIndex(WeatherTable.Cols.WIND_SPEED));
        String windDir = getString(getColumnIndex(WeatherTable.Cols.WIND_DIR));
        String date = getString(getColumnIndex(WeatherTable.Cols.DATE));
        String weekDate = getString(getColumnIndex(WeatherTable.Cols.WEEK_DATE));

        WeatherItem weatherItem = new WeatherItem();
        weatherItem.setWeather(weather);
        weatherItem.setWeatherId(weatherId);
        weatherItem.setTempMax(tempMax);
        weatherItem.setTempMin(tempMin);
        weatherItem.setHumidity(humidity);
        weatherItem.setPressure(pressure);
        weatherItem.setWindSpeed(windSpeed);
        weatherItem.setWindDir(windDir);
        weatherItem.setDate(date);
        weatherItem.setWeekDate(weekDate);

        return weatherItem;
    }
}
