package com.example.weatherforecast.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.weatherforecast.sqlitedb.SettingsLab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Forecast {

    private static final String TAG = "Forecast";
    private static final String API_KEY = "b27f5e0831764ca6a917ba2da3525a2e";
    private static final Uri ENDPOINT = Uri
            .parse("https://free-api.heweather.net/s6/weather/forecast/")
            .buildUpon()
            .appendQueryParameter("key", API_KEY)
            .build();

    private Context mContext;
    private SettingsLab mSettingsLab;

    public Forecast(Context context) {
        mContext =context;
        mSettingsLab = new SettingsLab(mContext);
    }

    //以字节形式读取网页信息
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //防止因页面跳转而导致的301响应
        String redirect = connection.getHeaderField("Location");
        while (redirect != null) {
            connection = (HttpURLConnection) new URL(redirect).openConnection();
            redirect = connection.getHeaderField("Location");
        }

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    //以字符形式读取网页信息
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<WeatherItem> getWeatherItems(String location) {
        String url = buildUrl(location);
        Log.d(TAG, url);
        return fetchWeatherItem(url);
    }

    private List<WeatherItem> fetchWeatherItem(String url) {
        List<WeatherItem> weatherItems = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseWeatherItems(weatherItems, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
            return null;
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
            return null;
        }
        return weatherItems;
    }

    private String buildUrl(String location) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("location", location);
        if (mSettingsLab.getTemperatureUnits().equals("Imperial")) {
            uriBuilder.appendQueryParameter("unit", "i");
        }
        return uriBuilder.build().toString();
    }

    private void parseWeatherItems(List<WeatherItem> weatherItems, JSONObject jsonBody) throws IOException, JSONException {
        JSONArray HeWeather6 = jsonBody.getJSONArray("HeWeather6");
        JSONArray daily_forecast = HeWeather6.getJSONObject(0).getJSONArray("daily_forecast");
        for (int i = 0; i < daily_forecast.length(); i++) {
            WeatherItem weatherItem = new WeatherItem();
            JSONObject forecast = daily_forecast.getJSONObject(i);
            weatherItem.setWeather(forecast.getString("cond_txt_d"));
            weatherItem.setWeatherId(Integer.parseInt(forecast.getString("cond_code_d")));
            weatherItem.setTempMax(forecast.getString("tmp_max") + (mSettingsLab.getTemperatureUnits().equals("Metric") ? "°C" :"°F"));
            weatherItem.setTempMin(forecast.getString("tmp_min") + (mSettingsLab.getTemperatureUnits().equals("Metric") ? "°C" :"°F"));
            weatherItem.setHumidity(forecast.getString("hum") + " %");
            weatherItem.setPressure(forecast.getString("pres") + " hPa");
            weatherItem.setWindSpeed(forecast.getString("wind_spd") + " km/h");
            weatherItem.setWindDir(forecast.getString("wind_dir"));
            weatherItem.makeDate(forecast.getString("date"), i);
            weatherItems.add(weatherItem);
        }
    }
}
