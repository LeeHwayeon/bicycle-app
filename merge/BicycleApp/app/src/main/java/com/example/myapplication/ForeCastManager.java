package com.example.myapplication;

import android.content.ContentValues;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ForeCastManager extends Thread {
    // Json 으로 연결하기 확인
    private String str, receiveMsg;
    String lon, lat;

    ArrayList<ContentValues> mWeather;
    WeatherActivity mContext;

    public ArrayList<ContentValues> getmWeather() {
        return mWeather;
    }

    public ForeCastManager(String lon, String lat, WeatherActivity mContext) {
        this.lon = lon;
        this.lat = lat;
        this.mContext = mContext;
    }


    public ArrayList<ContentValues> GetOpenWeather(String lon, String lat) {

        ArrayList<ContentValues> mTotalValue = new ArrayList<ContentValues>();
        String key = "246251bec7c2af08b8dcaeff9d294365";
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?" +
                    "lat=" + lat +
                    "&lon=" + lon +
                    "&appid=" + key +
                    "&exclude=minutely,hourly" +
                    "&units=metric");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", key);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray forecastArray = new JSONObject(receiveMsg).getJSONArray("daily");
            for (int i = 1; i < 8; i++) {
                String forecast_main = forecastArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                String forecast_minTemp = forecastArray.getJSONObject(i).getJSONObject("temp").getString("min");
                String forecast_maxTemp = forecastArray.getJSONObject(i).getJSONObject("temp").getString("max");

                ContentValues mContent = new ContentValues();
                mContent.put("weather_Name", forecast_main);
                mContent.put("temp_Min", forecast_minTemp);
                mContent.put("temp_Max", forecast_maxTemp);

                mTotalValue.add(mContent);
                Log.i("mContet", String.valueOf(mContent));
                Log.i("mTotalValue", String.valueOf(mTotalValue));

            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("result", String.valueOf(mTotalValue));
        return mTotalValue;
    }

    @Override
    public void run() {
        super.run();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mWeather = GetOpenWeather(lon, lat);
        //위도,경도 전달하여 날씨 정보 가져와 mWeather에 저장
        mContext.handler.sendEmptyMessage(mContext.THREAD_HANDLER_SUCCESS_INFO);
        //Thread 작업 종료, UI 작업을 위해 MainHandler에 Message보냄    }
    }

}
