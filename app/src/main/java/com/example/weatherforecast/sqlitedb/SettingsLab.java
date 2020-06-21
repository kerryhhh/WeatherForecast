package com.example.weatherforecast.sqlitedb;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsLab {
    private static final String TAG = "SettingsLab";

    private static final String SHARED_PREFERENCE_FILE_NAME = "SettingsData";

    private static final String KEY_LOCATION = "location";
    private static final String KEY_TEMPERATURE_UNITS = "temperature_unit";
    private static final String KEY_IS_ALARM_ON = "is_alarm_on";

    private SharedPreferences mSharedPreferences;

    public SettingsLab(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setLocation(String city) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_LOCATION, city);
        editor.apply();
    }

    public String getLocation() {
        return mSharedPreferences.getString(KEY_LOCATION, "Guangzhou");
    }

    public void setTemperatureUnits(String units) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(KEY_TEMPERATURE_UNITS, units);
        editor.apply();
    }

    public String getTemperatureUnits() {
        return mSharedPreferences.getString(KEY_TEMPERATURE_UNITS, "Metric");
    }

    public void setAlarmOn(boolean isOn) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEY_IS_ALARM_ON, isOn);
        editor.apply();
    }

    public boolean isAlarmOn() {
        return mSharedPreferences.getBoolean(KEY_IS_ALARM_ON, false);
    }
}
