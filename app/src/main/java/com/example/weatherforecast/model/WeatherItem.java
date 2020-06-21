package com.example.weatherforecast.model;

import androidx.annotation.NonNull;

import com.example.weatherforecast.R;

import java.io.Serializable;
import java.util.Calendar;

public class WeatherItem implements Serializable {

    private String mWeather;
    private int mWeatherId;
    private String mTempMax;
    private String mTempMin;
    private String mHumidity;
    private String mPressure;
    private String mWindSpeed;
    private String mWindDir;
    private String mDate;
    private String mWeekDate;

    private static final String[] weekdays = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };
    private static final String[] months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    public String getWeather() {
        return mWeather;
    }

    public void setWeather(String weather) {
        mWeather = weather;
    }

    public int getWeatherId() {
        return mWeatherId;
    }

    public void setWeatherId(int weatherId) {
        mWeatherId = weatherId;
    }

    public String getTempMax() {
        return mTempMax;
    }

    public void setTempMax(String tempMax) {
        mTempMax = tempMax;
    }

    public String getTempMin() {
        return mTempMin;
    }

    public void setTempMin(String tempMin) {
        mTempMin = tempMin;
    }

    public String getHumidity() {
        return mHumidity;
    }

    public void setHumidity(String humidity) {
        mHumidity = humidity;
    }

    public String getPressure() {
        return mPressure;
    }

    public void setPressure(String pressure) {
        mPressure = pressure;
    }

    public String getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        mWindSpeed = windSpeed;
    }

    public String getWindDir() {
        return mWindDir;
    }

    public void setWindDir(String windDir) {
        mWindDir = windDir;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getWeekDate() {
        return mWeekDate;
    }

    public void setWeekDate(String weekDate) {
        mWeekDate = weekDate;
    }

    public String getTopDate() {
        return mWeekDate + ", " + mDate;
    }

    //获取顶部大图标
    public int getTopIcon() {
        if (mWeatherId == 100) {
            return R.drawable.art_clear;
        } else if (mWeatherId == 101 || mWeatherId == 102 || mWeatherId == 104) {
            return R.drawable.art_clouds;
        } else if (mWeatherId == 103) {
            return R.drawable.art_light_clouds;
        } else if (mWeatherId == 300 || mWeatherId == 305|| mWeatherId == 306 || mWeatherId == 314 || mWeatherId == 399) {
            return R.drawable.art_light_rain;
        } else if ((mWeatherId >= 301 && mWeatherId <= 304) || mWeatherId == 307 || mWeatherId == 308 || (mWeatherId != 314 && mWeatherId >= 310 && mWeatherId <= 318)) {
            return R.drawable.art_rain;
        } else if (mWeatherId >= 400 && mWeatherId <= 499) {
            return R.drawable.art_snow;
        } else if (mWeatherId >= 500 && mWeatherId <= 515) {
            return R.drawable.art_fog;
        } else {
            return R.drawable.art_clouds;
        }
    }

    //获取小图标
    public int getIcon() {
        if (mWeatherId == 100) {
            return R.drawable.ic_clear;
        } else if (mWeatherId == 101 || mWeatherId == 102 || mWeatherId == 104) {
            return R.drawable.ic_cloudy;
        } else if (mWeatherId == 103) {
            return R.drawable.ic_light_clouds;
        } else if (mWeatherId == 300 || mWeatherId == 305|| mWeatherId == 306 || mWeatherId == 314 || mWeatherId == 399) {
            return R.drawable.ic_light_rain;
        } else if ((mWeatherId >= 301 && mWeatherId <= 304) || mWeatherId == 307 || mWeatherId == 308 || (mWeatherId != 314 && mWeatherId >= 310 && mWeatherId <= 318)) {
            return R.drawable.ic_rain;
        } else if (mWeatherId >= 400 && mWeatherId <= 499) {
            return R.drawable.ic_snow;
        } else if (mWeatherId >= 500 && mWeatherId <= 515) {
            return R.drawable.ic_fog;
        } else {
            return R.drawable.ic_cloudy;
        }
    }

    public void makeDate(String Date, int index) {
        String[] s = Date.split("[ :-]");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(s[0]), Integer.parseInt(s[1]) - 1, Integer.parseInt(s[2]));
        int mk = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        setDate(months[mk] + " " + day);

        if (index == 0) {
            setWeekDate("Today");
        } else if (index == 1) {
            setWeekDate("Tomorrow");
        } else {
            int k = calendar.get(Calendar.DAY_OF_WEEK);
            setWeekDate(weekdays[k - 1]);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public String toShareString(String city) {
        return "The weather of " + city + " on " + mDate + " is " + mWeather + ". " +
                "The temperature ranges from " + mTempMin + " to " + mTempMax + ". " +
                "The humidity is " + mHumidity + ", " +
                "the pressure is " + mPressure + ", " +
                "and the wind is " + mWindDir + ".";
    }
}
