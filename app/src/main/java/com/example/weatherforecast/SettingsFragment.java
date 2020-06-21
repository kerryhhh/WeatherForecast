package com.example.weatherforecast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.model.SettingItem;
import com.example.weatherforecast.sqlitedb.SettingsLab;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends VisibleFragment {

    private RecyclerView mSettingsRecyclerView;
    private SettingAdapter mSettingAdapter;
    private List<SettingItem> mSettingItems = new ArrayList<>();

    private SettingsLab mSettingsLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettingsLab = new SettingsLab(getActivity());
        mSettingItems = createItems();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mSettingsRecyclerView = (RecyclerView) view.findViewById(R.id.settings_list);
        mSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
//        if (mSettingAdapter == null) {
//            mSettingAdapter = new SettingAdapter(mSettingItems);
//            mSettingsRecyclerView.setAdapter(mSettingAdapter);
//        } else {
//            mSettingAdapter.notifyDataSetChanged();
//        }
        mSettingAdapter = new SettingAdapter(mSettingItems);
        mSettingsRecyclerView.setAdapter(mSettingAdapter);
    }

    private List<SettingItem> createItems() {
        List<SettingItem> settingItems = new ArrayList<>();
        settingItems.add(new SettingItem("Location", mSettingsLab.getLocation(), false));
        settingItems.add(new SettingItem("Temperature Units", mSettingsLab.getTemperatureUnits(), false));
        settingItems.add(new SettingItem("Weather Notifications", (WeatherService.isServiceAlarmOn(getActivity()) ? "Enable" : "Disable"), true));
        return settingItems;
    }

    private class SettingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private SettingItem mSettingItem;

        private TextView mTitleTextView;
        private TextView mDetailTextView;
        private CheckBox mCheckBox;

        public SettingHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_setting_item_title);
            mDetailTextView = (TextView) itemView.findViewById(R.id.list_setting_item_detail);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.list_setting_item_checkbox);
        }

        public void bindSetting(SettingItem settingItem) {
            mSettingItem = settingItem;
            mTitleTextView.setText(mSettingItem.getTitle());
            mDetailTextView.setText(mSettingItem.getDetail());
            mCheckBox.setVisibility(mSettingItem.isCheckable() ? View.VISIBLE : View.INVISIBLE);
            if (mSettingItem.isCheckable()) {
                mCheckBox.setChecked(mSettingsLab.isAlarmOn());
            }
        }

        @Override
        public void onClick(View v) {
            onItemClick(getBindingAdapterPosition());
//            mSettingAdapter.notifyDataSetChanged();
        }

        private void onItemClick(int position) {
            switch (position) {
                case 0:
                    onSettingCity();
                    break;
                case 1:
                    onChoosingUnits();
                    break;
                case 2:
                    onCheckBoxChanged();
                    break;
            }
        }

        private void onSettingCity() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Setting City");
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_setting_city, null);
            builder.setView(view);

            final EditText mCity = view.findViewById(R.id.set_city_cityName);
            final Button mSummit = view.findViewById(R.id.set_city_submit);
            final Button mCancel = view.findViewById(R.id.set_city_cancel);
            final AlertDialog dialog = builder.show();

            mCity.setHint(mSettingsLab.getLocation());

            mSummit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String city = mCity.getText().toString().trim();
                    if (city.equals("")) {
                        Toast.makeText(getActivity(), "Empty value is not allow!", Toast.LENGTH_SHORT).show();
                    } else {
                        mSettingsLab.setLocation(city);
                        mSettingItems.get(0).setDetail(city);
                        mSettingAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                }
            });

            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        private void onChoosingUnits() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose Temperature Units");
            final String[] options = {"Metric", "Imperial"};
            int index = mSettingsLab.getTemperatureUnits().equals("Metric") ? 0 : 1;
            builder.setSingleChoiceItems(options, index, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String result = (which == 0 ? "Metric" : "Imperial");
                    mSettingsLab.setTemperatureUnits(result);
                    mSettingItems.get(1).setDetail(result);
                    dialog.dismiss();
                    mSettingAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        private void onCheckBoxChanged() {
            boolean shouldStartAlarm = !WeatherService.isServiceAlarmOn(getActivity());
            WeatherService.setServiceAlarm(getActivity(), shouldStartAlarm);
//            mSettingItem.setChecked(shouldStartAlarm);
            mSettingItem.setDetail(shouldStartAlarm ? "Enabled" : "Disabled");
            mSettingAdapter.notifyDataSetChanged();
        }
    }

    private class SettingAdapter extends RecyclerView.Adapter<SettingHolder> {
        private List<SettingItem> mSettingItems;

        public SettingAdapter(List<SettingItem> settingItems) {
            mSettingItems = settingItems;
        }

        @NonNull
        @Override
        public SettingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.setting_item, parent,false);
            return new SettingHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SettingHolder holder, int position) {
            SettingItem settingItem = mSettingItems.get(position);
            holder.bindSetting(settingItem);
        }

        @Override
        public int getItemCount() {
            return mSettingItems.size();
        }
    }
}
