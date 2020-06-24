package com.example.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ForecastAdapter extends BaseAdapter{
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ForecastWeather> sample;

    public ForecastAdapter(Context context, ArrayList<ForecastWeather> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ForecastWeather getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item_forecast, null);

        TextView main = (TextView) view.findViewById(R.id.forecast_main);
        TextView date = (TextView)view.findViewById(R.id.forecast_date);
        TextView minTemp = (TextView)view.findViewById(R.id.forecast_min_temp);
        TextView maxTemp =(TextView)view.findViewById(R.id.forecast_max_temp);

        main.setText(sample.get(position).getForecast_main());
        date.setText(sample.get(position).getForecast_week());
        minTemp.setText(sample.get(position).getForecast_minTemp());
        maxTemp.setText(sample.get(position).getForecast_maxTemp());

        return view;
    }

}
