package com.example.myweather;

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
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class WeatherManager extends Thread {
    private String str, receiveMsg;
    String lon, lat;

    ArrayList<ContentValues> ManageWeather;
    MainActivity mContext;

    public ArrayList<ContentValues> getWeather() {
        return ManageWeather;
    }

    public WeatherManager(String lon, String lat, MainActivity mContext) {
        this.lon = lon;
        this.lat = lat;
        this.mContext = mContext;
    }


    public ArrayList<ContentValues> GetOpenWeather(String lon, String lat) {

        ArrayList<ContentValues> WeatherTotalValue = new ArrayList<>();
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
            JSONObject jObject = new JSONObject(receiveMsg).getJSONObject("current");
            JSONArray JArray = new JSONObject(receiveMsg).getJSONArray("daily");
            String current_icon = jObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            String current_main = jObject.getJSONArray("weather").getJSONObject(0).getString("main");
            String current_description = jObject.getJSONArray("weather").getJSONObject(0).getString("description");
            String current_temp = jObject.getString("temp");
            String current_feelsLike = jObject.getString("feels_like");
            String current_humidity = jObject.getString("humidity");
            String current_windSpeed = jObject.getString("wind_speed");
            String current_uvi = jObject.getString("uvi");
            String current_minTemp = JArray.getJSONObject(0).getJSONObject("temp").getString("min");
            String current_maxTemp = JArray.getJSONObject(0).getJSONObject("temp").getString("max");

            ContentValues WeatherContent = new ContentValues();
            WeatherContent.put("current_icon", current_icon);
            WeatherContent.put("current_main", current_main);
            WeatherContent.put("current_description", current_description);
            WeatherContent.put("current_temp", current_temp);
            WeatherContent.put("current_feelsLike", current_feelsLike);
            WeatherContent.put("current_humidity", current_humidity);
            WeatherContent.put("current_windSpeed", current_windSpeed);
            WeatherContent.put("current_uvi", current_uvi);
            WeatherContent.put("current_minTemp", current_minTemp);
            WeatherContent.put("current_maxTemp", current_maxTemp);

            WeatherTotalValue.add(WeatherContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray JArray = new JSONObject(receiveMsg).getJSONArray("daily");

            for (int i = 1; i < 8; i++) {
                long forecast_date = JArray.getJSONObject(i).getLong("dt");
                String forecast_icon = JArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                String forecast_main = JArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                String forecast_minTemp = JArray.getJSONObject(i).getJSONObject("temp").getString("min");
                String forecast_maxTemp = JArray.getJSONObject(i).getJSONObject("temp").getString("max");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String forecast_week = Instant.ofEpochSecond(forecast_date).atZone(ZoneId.of("GMT+9")).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

                ContentValues WeatherContent = new ContentValues();
                WeatherContent.put("forecast_week", forecast_week);
                WeatherContent.put("forecast_main", forecast_main);
                WeatherContent.put("forecast_minTemp", forecast_minTemp);
                WeatherContent.put("forecast_maxTemp", forecast_maxTemp);

                WeatherTotalValue.add(WeatherContent);
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("result", String.valueOf(WeatherTotalValue));
        return WeatherTotalValue;
    }



    @Override
    public void run() {
        super.run();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ManageWeather = GetOpenWeather(lon, lat);
        //위도,경도 전달하여 날씨 정보 가져와 ManageWeather 저장
        mContext.handler.sendEmptyMessage(mContext.THREAD_HANDLER_SUCCESS_INFO);
        //Thread 작업 종료, UI 작업을 위해 MainHandler에 Message보냄    }
    }
}
