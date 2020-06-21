package com.example.weatherforecast.model;

public class SettingItem {
    private String mTitle;
    private String mDetail;
    private boolean mIsCheckable;
//    private boolean mIsChecked;

    public SettingItem(String title, String detail, boolean isCheckable) {
        mTitle = title;
        mDetail = detail;
        mIsCheckable =isCheckable;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public boolean isCheckable() {
        return mIsCheckable;
    }

    public void setCheckable(boolean checkable) {
        mIsCheckable = checkable;
    }

//    public boolean isChecked() {
//        return mIsChecked;
//    }
//
//    public void setChecked(boolean checked) {
//        mIsChecked = checked;
//    }
}
