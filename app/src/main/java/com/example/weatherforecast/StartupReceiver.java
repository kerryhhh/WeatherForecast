package com.example.weatherforecast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.weatherforecast.sqlitedb.SettingsLab;

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());

        boolean isOn = new SettingsLab(context).isAlarmOn();
        WeatherService.setServiceAlarm(context, isOn);
    }
}
