package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {
    public static final int TYPE_CURRENT = 0;
    public static final int TYPE_FORECAST = 1;

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<Weather> sample;

    public WeatherAdapter(Context context, ArrayList<Weather> data) {
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
    public Weather getItem(int position) {
        return sample.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return sample.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        int viewType = getItemViewType(position) ;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            Weather listViewItem = sample.get(position);

            switch (viewType) {
                case TYPE_CURRENT:
                    convertView =mLayoutInflater.inflate(R.layout.list_item_current, parent, false);
                    TextView main = (TextView) convertView.findViewById(R.id.current_main);
                    TextView description =(TextView)convertView.findViewById(R.id.current_description);
                    TextView temp = (TextView)convertView.findViewById(R.id.current_temp);
                    TextView feesLike = (TextView)convertView.findViewById(R.id.current_feelsLike);
                    TextView minTemp =(TextView)convertView.findViewById(R.id.current_minTemp);
                    TextView maxTemp =(TextView)convertView.findViewById(R.id.current_maxTemp);
                    TextView humidity =(TextView)convertView.findViewById(R.id.current_humidity);
                    TextView windSpeed =(TextView)convertView.findViewById(R.id.current_windSpeed);
                    TextView uvi =(TextView)convertView.findViewById(R.id.current_uvi);

                    main.setText(sample.get(position).getCurrent_main());
                    description.setText(sample.get(position).getCurrent_description());
                    temp.setText(sample.get(position).getCurrent_temp());
                    feesLike.setText(sample.get(position).getCurrent_feelsLike());
                    minTemp.setText(sample.get(position).getCurrent_minTemp());
                    maxTemp.setText(sample.get(position).getCurrent_maxTemp());
                    humidity.setText(sample.get(position).getCurrent_humidity());
                    windSpeed.setText(sample.get(position).getCurrent_windSpeed());
                    uvi.setText(sample.get(position).getCurrent_uvi());
                    break;

                case TYPE_FORECAST:
                    convertView = inflater.inflate(R.layout.list_item_forecast, parent, false);

                    TextView forecast_main = (TextView) convertView.findViewById(R.id.forecast_main);
                    TextView date = (TextView)convertView.findViewById(R.id.forecast_date);
                    TextView forecast_minTemp = (TextView)convertView.findViewById(R.id.forecast_min_temp);
                    TextView forecast_maxTemp =(TextView)convertView.findViewById(R.id.forecast_max_temp);

                    forecast_main.setText(sample.get(position).getForecast_main());
                    date.setText(sample.get(position).getForecast_week());
                    forecast_minTemp.setText(sample.get(position).getForecast_minTemp());
                    forecast_maxTemp.setText(sample.get(position).getForecast_maxTemp());
            }
        }
        return convertView;

//        convertView = mLayoutInflater.inflate(R.layout.list_item_current, null);

//
//        TextView main = (TextView) view.findViewById(R.id.current_main);
//        TextView description =(TextView)view.findViewById(R.id.current_description);
//        TextView temp = (TextView)view.findViewById(R.id.current_temp);
//        TextView feesLike = (TextView)view.findViewById(R.id.current_feelsLike);
//        TextView minTemp =(TextView)view.findViewById(R.id.current_minTemp);
//        TextView maxTemp =(TextView)view.findViewById(R.id.current_maxTemp);
//        TextView humidity =(TextView)view.findViewById(R.id.current_humidity);
//        TextView windSpeed =(TextView)view.findViewById(R.id.current_windSpeed);
//        TextView uvi =(TextView)view.findViewById(R.id.current_uvi);
//
//        TextView forecast_main = (TextView) view.findViewById(R.id.forecast_main);
//        TextView date = (TextView)view.findViewById(R.id.forecast_date);
//        TextView forecast_minTemp = (TextView)view.findViewById(R.id.forecast_min_temp);
//        TextView forecast_maxTemp =(TextView)view.findViewById(R.id.forecast_max_temp);
//
//        main.setText(sample.get(position).getCurrent_main());
//        description.setText(sample.get(position).getCurrent_description());
//        temp.setText(sample.get(position).getCurrent_temp());
//        feesLike.setText(sample.get(position).getCurrent_feelsLike());
//        minTemp.setText(sample.get(position).getCurrent_minTemp());
//        maxTemp.setText(sample.get(position).getCurrent_maxTemp());
//        humidity.setText(sample.get(position).getCurrent_humidity());
//        windSpeed.setText(sample.get(position).getCurrent_windSpeed());
//        uvi.setText(sample.get(position).getCurrent_uvi());
//
//        forecast_main.setText(sample.get(position).getForecast_main());
//        date.setText(sample.get(position).getForecast_week());
//        forecast_minTemp.setText(sample.get(position).getForecast_minTemp());
//        forecast_maxTemp.setText(sample.get(position).getForecast_maxTemp());
//
//
//
//        return view;
    }

}
