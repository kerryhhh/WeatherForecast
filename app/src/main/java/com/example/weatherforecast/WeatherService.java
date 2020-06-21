package com.example.weatherforecast;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weatherforecast.model.Forecast;
import com.example.weatherforecast.model.WeatherItem;
import com.example.weatherforecast.sqlitedb.SettingsLab;
import com.example.weatherforecast.sqlitedb.WeatherLab;

import java.util.List;

public class WeatherService extends IntentService {
    private static final String TAG = "WeatherService";

    private static final long WEATHER_INTERVAL = AlarmManager.INTERVAL_HOUR * 5; //5 hour
    private static final int WEATHER_INTERVAL_TEST = 1000 * 60; //1 minute

    public static final String ACTION_SHOW_NOTIFICATION = "com.example.weatherforecast.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "com.example.weatherforecast.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    private WeatherLab mWeatherLab;

    public static Intent newIntent(Context context) {
        return new Intent(context, WeatherService.class);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = WeatherService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), WEATHER_INTERVAL_TEST, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
        new SettingsLab(context).setAlarmOn(isOn);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = WeatherService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public WeatherService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        Log.d(TAG, "Received an intent: " + intent);
        mWeatherLab = new WeatherLab(this);

        List<WeatherItem> weatherItems = new Forecast(this).getWeatherItems("Guangzhou");
        WeatherItem todayWeather = weatherItems.get(0);
        mWeatherLab.addWeatherItems(weatherItems);

        Intent i = MainActivity.newIntent(this);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);

//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationChannel notificationChannel = new NotificationChannel("my_channel", "my_channel", NotificationManager.IMPORTANCE_LOW);

        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("WeatherForecast")
                .setSmallIcon(todayWeather.getTopIcon())
                .setContentTitle("WeatherForecast")
                .setContentText(todayWeather.getWeather() + " High: " + todayWeather.getTempMax() + " Low: " + todayWeather.getTempMin())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        showBackgroundNotification(0, notification);
    }

    private void showBackgroundNotification(int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_SHOW_NOTIFICATION);
        i.putExtra(REQUEST_CODE, requestCode);
        i.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }
}
