package com.example.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weatherforecast.model.WeatherItem;
import com.example.weatherforecast.sqlitedb.SettingsLab;


public class WeatherActivity extends SingleFragmentActivity {

    private static final String TAG = "WeatherActivity";

    private static final String EXTRA_WEATHER_ITEM = "com.example.weatherforecast.weather_item";

    private WeatherItem mWeatherItem;

    //供其他activity启动WeatherActivity
    //需在intent中传入weatherItem
    public static Intent newIntent(Context packageContext, WeatherItem weatherItem) {
        Intent intent = new Intent(packageContext, WeatherActivity.class);
        intent.putExtra(EXTRA_WEATHER_ITEM, weatherItem);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        mWeatherItem = (WeatherItem) getIntent().getSerializableExtra(EXTRA_WEATHER_ITEM);
        return WeatherFragment.newInstance(mWeatherItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_weather_page, menu);
        setTitle(R.string.title_detail);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_map_location:
                openMapLocation();
                return true;
            case R.id.menu_settings:
                openSettings();
                return true;
            case R.id.menu_share:
                shareWithApp();
                return true;
            case R.id.menu_share_with_message:
                shareWithMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMapLocation() {
        String cityName = Uri.encode(new SettingsLab(this).getLocation());
        Uri location = Uri.parse("geo:0,0?q=" + cityName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(location);
        startActivity(Intent.createChooser(intent, "Open with third-part Map Application"));
    }

    private void openSettings() {
        Intent intent = SettingsActivity.newIntent(this);
        startActivity(intent);
    }

    private void shareWithApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mWeatherItem.toShareString(new SettingsLab(this).getLocation()));
        startActivity(Intent.createChooser(intent, "Share Weather"));
    }

    private void shareWithMessage() {
        Uri uri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", mWeatherItem.toShareString(new SettingsLab(this).getLocation()));
        startActivity(intent);
    }
}
