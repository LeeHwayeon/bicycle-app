package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RentalAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<CurrentRental> sample;

    public RentalAdapter(Context context, ArrayList<CurrentRental> data) {
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
    public CurrentRental getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.list_item_current_rental, null);

        TextView rackTotCnt = (TextView) view.findViewById(R.id.rackTotCnt);
        TextView stationName =(TextView)view.findViewById(R.id.stationName);
        TextView parkingBikeTotCnt = (TextView)view.findViewById(R.id.parkingBikeTotCnt);
        TextView stationLatitude = (TextView)view.findViewById(R.id.stationLatitude);
        TextView stationLongitude =(TextView)view.findViewById(R.id.stationLongitude);

        rackTotCnt.setText(sample.get(position).getRackTotCnt());
        stationName.setText(sample.get(position).getStationName());
        parkingBikeTotCnt.setText(sample.get(position).getParkingBikeTotCnt());
        stationLatitude.setText(sample.get(position).getStationLatitude());
        stationLongitude.setText(sample.get(position).getStationLongitude());


        return view;
    }

}
