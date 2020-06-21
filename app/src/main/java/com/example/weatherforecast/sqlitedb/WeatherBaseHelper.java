package com.example.weatherforecast.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.weatherforecast.sqlitedb.WeatherDbSchema.WeatherTable;

public class WeatherBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "weatherBase.db";

    public WeatherBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + WeatherTable.NAME + "(" +
                "id integer primary key autoincrement, " +
                WeatherTable.Cols.WEATHER + ", " +
                WeatherTable.Cols.WEATHER_ID + ", " +
                WeatherTable.Cols.TEMP_MAX + ", " +
                WeatherTable.Cols.TEMP_MIN + ", " +
                WeatherTable.Cols.HUMIDITY + ", " +
                WeatherTable.Cols.PRESSURE + ", " +
                WeatherTable.Cols.WIND_SPEED + ", " +
                WeatherTable.Cols.WIND_DIR + ", " +
                WeatherTable.Cols.DATE + ", " +
                WeatherTable.Cols.WEEK_DATE +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
