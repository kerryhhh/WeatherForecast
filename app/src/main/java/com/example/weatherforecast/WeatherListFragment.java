package com.example.weatherforecast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.model.Forecast;
import com.example.weatherforecast.model.WeatherItem;
import com.example.weatherforecast.sqlitedb.SettingsLab;
import com.example.weatherforecast.sqlitedb.WeatherLab;

import java.util.ArrayList;
import java.util.List;

public class WeatherListFragment extends VisibleFragment {

    private static final String TAG = "WeatherListFragment";

    private RecyclerView mWeatherRecyclerView;
    private WeatherItemAdapter mWeatherItemAdapter;
    private List<WeatherItem> mWeatherItems = new ArrayList<>();
    private Callbacks mCallbacks;

    private WeatherLab mWeatherLab;
    private SettingsLab mSettingsLab;

    private int mPosition;

    //回调接口
    //供activity继承实现
    public interface Callbacks {
        boolean isPhone();
        void onWeatherUpdate();
        void onWeatherItemSelected(WeatherItem weatherItem);
//        void onWeatherItemSelected(int position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mWeatherLab = new WeatherLab(getActivity());
        mSettingsLab = new SettingsLab(getActivity());

//        updateWeather();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);

        mWeatherRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_weather_list);
        mWeatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openMapLocation() {
        String cityName = Uri.encode(mSettingsLab.getLocation());
        Uri location = Uri.parse("geo:0,0?q=" + cityName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(location);
        startActivity(Intent.createChooser(intent, "Open with third-part Map Application"));
    }

    private void openSettings() {
        Intent intent = SettingsActivity.newIntent(getActivity());
        startActivity(intent);
    }

    //更新当前界面
    private void updateUI() {
//        if (mWeatherItemAdapter == null) {
//            mWeatherItemAdapter = new WeatherItemAdapter(mWeatherItems);
//            mWeatherRecyclerView.setAdapter(mWeatherItemAdapter);
//        } else {
//            mWeatherItemAdapter.notifyDataSetChanged();
//        }
        mWeatherItemAdapter = new WeatherItemAdapter(mWeatherItems);
        mWeatherRecyclerView.setAdapter(mWeatherItemAdapter);
        mCallbacks.onWeatherUpdate();
    }

    public void updateWeather() {
        Toast.makeText(getActivity(), "Updating weather information now.", Toast.LENGTH_SHORT).show();
        new GetWeatherTask(mSettingsLab.getLocation()).execute();
    }

    public WeatherItem getTodayWeather() {
        if (!mWeatherItems.isEmpty()) {
            return mWeatherItems.get(0);
        } else {
            return null;
        }
    }

    //Dialog
    //提示网络连接失败
    private void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Network Error");
        builder.setMessage("");
        builder.setNegativeButton("Use Local information", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mWeatherItems = mWeatherLab.getWeatherItems();
                updateUI();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateWeather();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private class WeatherItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeatherItem mWeatherItem;

        private TextView mItemWeather;
        private TextView mItemWeekDate;
        private TextView mItemTempMax;
        private TextView mItemTempMin;
        private ImageView mItemWeatherIcon;

        public WeatherItemHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mItemWeather = (TextView) itemView.findViewById(R.id.weather_item_weather);
            mItemWeekDate = (TextView) itemView.findViewById(R.id.weather_item_weekdate);
            mItemTempMax = (TextView) itemView.findViewById(R.id.weather_item_temp_max);
            mItemTempMin = (TextView) itemView.findViewById(R.id.weather_item_temp_min);
            mItemWeatherIcon = (ImageView) itemView.findViewById(R.id.weather_item_icon);
        }

        public void bindWeatherItem(WeatherItem weatherItem) {
            mWeatherItem = weatherItem;
            mItemWeather.setText(mWeatherItem.getWeather());
            mItemWeekDate.setText(mWeatherItem.getWeekDate());
            mItemTempMax.setText(mWeatherItem.getTempMax());
            mItemTempMin.setText(mWeatherItem.getTempMin());
            mItemWeatherIcon.setImageResource(mWeatherItem.getIcon());
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onWeatherItemSelected(mWeatherItem);
            mPosition = getBindingAdapterPosition();
            mWeatherItemAdapter.notifyDataSetChanged();
        }
    }

    class WeatherItemAdapter extends RecyclerView.Adapter<WeatherItemHolder> {
        private List<WeatherItem> mWeatherItems;

        public WeatherItemAdapter(List<WeatherItem> weatherItems) {
            mWeatherItems = weatherItems;
        }

        @NonNull
        @Override
        public WeatherItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.weather_item, parent, false);
            return new WeatherItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WeatherItemHolder holder, int position) {
            WeatherItem weatherItem = mWeatherItems.get(position);
            holder.bindWeatherItem(weatherItem);
            if (position == mPosition && !mCallbacks.isPhone()) {
                holder.itemView.setBackgroundColor(getResources().getColor(R.color.colorLight));
            } else {
//                holder.itemView.setBackgroundColor(getResources().getColor(R.color.page_background));
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public int getItemCount() {
            return mWeatherItems.size();
        }
    }


    //后台线程
    //用于获取天气信息
    private class GetWeatherTask extends AsyncTask<Void, Void, List<WeatherItem>> {

        private String mLocation;

        public GetWeatherTask(String location) {
            mLocation = location;
        }

        @Override
        protected List<WeatherItem> doInBackground(Void... voids) {
            List<WeatherItem> weatherItems = new Forecast(getActivity()).getWeatherItems(mLocation);
            if (weatherItems != null) {
                mWeatherLab.addWeatherItems(weatherItems);
            }
            return weatherItems;
        }

        @Override
        protected void onPostExecute(List<WeatherItem> weatherItems) {
            super.onPostExecute(weatherItems);
            if (weatherItems == null) {
                //weatherItems为空，即网络连接失败
                showNetworkErrorDialog();
            } else {
                mWeatherItems = weatherItems;
                updateUI();
            }
        }
    }
}
