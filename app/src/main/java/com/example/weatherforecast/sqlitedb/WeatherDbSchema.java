package com.example.weatherforecast.sqlitedb;

public class WeatherDbSchema {
    public static final class WeatherTable {
        public static final String NAME = "Weather";

        public static final class Cols {
            public static final String WEATHER = "weather";
            public static final String WEATHER_ID = "weather_id";
            public static final String TEMP_MAX = "temp_max";
            public static final String TEMP_MIN = "temp_min";
            public static final String HUMIDITY = "humidity";
            public static final String PRESSURE = "pressure";
            public static final String WIND_SPEED = "wind_speed";
            public static final String WIND_DIR = "wind_dir";
            public static final String DATE = "date";
            public static final String WEEK_DATE = "week_date";
        }
    }
}
