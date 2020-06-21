package com.example.weatherforecast.sqlitedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weatherforecast.sqlitedb.WeatherDbSchema.WeatherTable;
import com.example.weatherforecast.model.WeatherItem;

import java.util.ArrayList;
import java.util.List;

public class WeatherLab {

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public WeatherLab(Context context) {
        mContext =context;
        mDatabase = new WeatherBaseHelper(mContext).getWritableDatabase();
    }

    public void addWeatherItems(List<WeatherItem> weatherItems) {

        //清空先前数据
        mDatabase.execSQL("delete from " + WeatherTable.NAME);
        mDatabase.execSQL("UPDATE sqlite_sequence SET seq = 0 WHERE name = ?", new String[] {WeatherTable.NAME});

        for (WeatherItem weatherItem : weatherItems) {
            ContentValues values = getContentValues(weatherItem);
            mDatabase.insert(WeatherTable.NAME, null, values);
        }
    }

    public void updateWeatherItem(WeatherItem weatherItem) {

    }

    public WeatherItem getTodayWeather() {
        WeatherCursorWrapper cursorWrapper = queryWeather(WeatherTable.Cols.WEEK_DATE + " = ?", new String[] {"Today"});
        return cursorWrapper.getWeatherItem();
    }


    public List<WeatherItem> getWeatherItems() {
        List<WeatherItem> weatherItems = new ArrayList<>();

        WeatherCursorWrapper cursorWrapper = queryWeather(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                weatherItems.add(cursorWrapper.getWeatherItem());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return weatherItems;
    }

    private static ContentValues getContentValues(WeatherItem weatherItem) {
        ContentValues values = new ContentValues();
        values.put(WeatherTable.Cols.WEATHER, weatherItem.getWeather());
        values.put(WeatherTable.Cols.WEATHER_ID, weatherItem.getWeatherId());
        values.put(WeatherTable.Cols.TEMP_MAX, weatherItem.getTempMax());
        values.put(WeatherTable.Cols.TEMP_MIN, weatherItem.getTempMin());
        values.put(WeatherTable.Cols.HUMIDITY, weatherItem.getHumidity());
        values.put(WeatherTable.Cols.PRESSURE, weatherItem.getPressure());
        values.put(WeatherTable.Cols.WIND_SPEED, weatherItem.getWindSpeed());
        values.put(WeatherTable.Cols.WIND_DIR, weatherItem.getWindDir());
        values.put(WeatherTable.Cols.DATE, weatherItem.getDate());
        values.put(WeatherTable.Cols.WEEK_DATE, weatherItem.getWeekDate());

        return values;
    }

    private WeatherCursorWrapper queryWeather(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                WeatherTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new WeatherCursorWrapper(cursor);
    }
}
