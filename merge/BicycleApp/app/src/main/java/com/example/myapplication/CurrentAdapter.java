package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrentAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CurrentWeather> sample;

    public CurrentAdapter(Context context, ArrayList<CurrentWeather> data) {
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
    public CurrentWeather getItem(int position) {
        return sample.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item_current, null);

        TextView main = (TextView) view.findViewById(R.id.current_main);
        TextView description =(TextView)view.findViewById(R.id.current_description);
        TextView temp = (TextView)view.findViewById(R.id.current_temp);
        TextView feesLike = (TextView)view.findViewById(R.id.current_feelsLike);
        TextView minTemp =(TextView)view.findViewById(R.id.current_minTemp);
        TextView maxTemp =(TextView)view.findViewById(R.id.current_maxTemp);
        TextView humidity =(TextView)view.findViewById(R.id.current_humidity);
        TextView windSpeed =(TextView)view.findViewById(R.id.current_windSpeed);
        TextView uvi =(TextView)view.findViewById(R.id.current_uvi);

        main.setText(sample.get(position).getCurrent_main());
        description.setText(sample.get(position).getCurrent_description());
        temp.setText(sample.get(position).getCurrent_temp());
        feesLike.setText(sample.get(position).getCurrent_feelsLike());
        minTemp.setText(sample.get(position).getCurrent_minTemp());
        maxTemp.setText(sample.get(position).getCurrent_maxTemp());
        humidity.setText(sample.get(position).getCurrent_humidity());
        windSpeed.setText(sample.get(position).getCurrent_windSpeed());
        uvi.setText(sample.get(position).getCurrent_uvi());

        return view;
    }

}