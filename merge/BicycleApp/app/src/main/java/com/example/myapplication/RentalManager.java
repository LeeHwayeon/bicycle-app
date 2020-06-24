package com.example.myapplication;

import android.annotation.SuppressLint;
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

public class RentalManager extends Thread {
    private String str, receiveMsg;

    String rackTotCnt;
    String stationName;
    String parkingBikeTotCnt;
    String stationLatitude;
    String stationLongitude;

    ArrayList<ContentValues> ManageRental;
    RentalActivity mContext;

    public ArrayList<ContentValues> getRental() {
        return ManageRental;
    }

    public RentalManager(String rackTotCnt, String stationName, String parkingBikeTotCnt, String stationLatitude, String stationLongitude, RentalActivity mContext) {

        this.rackTotCnt = rackTotCnt;
        this.stationName = stationName;
        this.parkingBikeTotCnt = parkingBikeTotCnt;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;

        this.mContext = mContext;
    }


    @SuppressLint("LongLogTag")
    public ArrayList<ContentValues> GetOpenRental(String rackTotCnt, String stationName, String parkingBikeTotCnt, String stationLatitude, String stationLongitude) {

        ArrayList<ContentValues> RentalTotalValue = new ArrayList<>();
        String clientKey = "4f4867556d636f6e37304375746e63";
        try {
            URL url = new URL("http://openapi.seoul.go.kr:8088/"+clientKey+
                    "/json/bikeList/1/200/");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", clientKey);

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
            JSONObject jsonObject = new JSONObject(receiveMsg).getJSONObject("rentBikeStatus");
            JSONArray jsonArray = jsonObject.getJSONArray("row");

            rackTotCnt = jsonArray.getJSONObject(0).getString("rackTotCnt");
            stationName = jsonArray.getJSONObject(0).getString("stationName");
            parkingBikeTotCnt = jsonArray.getJSONObject(0).getString("parkingBikeTotCnt");
            stationLatitude = jsonArray.getJSONObject(0).getString("stationLatitude");
            stationLongitude = jsonArray.getJSONObject(0).getString("stationLongitude");

            ContentValues RentalContent = new ContentValues();
            RentalContent.put("rackTotCnt", rackTotCnt);
            RentalContent.put("stationName", stationName);
            RentalContent.put("parkingBikeTotCnt", parkingBikeTotCnt);
            RentalContent.put("stationLatitude", stationLatitude);
            RentalContent.put("stationLongitude", stationLongitude);


            RentalTotalValue.add(RentalContent);

            Log.i("current RentalContent", String.valueOf(RentalContent));
            Log.i("current RentalTotalValue", String.valueOf(RentalTotalValue));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("result", String.valueOf(RentalTotalValue));
        return RentalTotalValue;
    }



    @Override
    public void run() {
        super.run();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ManageRental = GetOpenRental(rackTotCnt, stationName, parkingBikeTotCnt, stationLatitude, stationLongitude);
        //위도,경도 전달하여 날씨 정보 가져와 ManageRental 저장
        mContext.handler.sendEmptyMessage(mContext.THREAD_HANDLER_SUCCESS_INFO);
        //Thread 작업 종료, UI 작업을 위해 MainHandler에 Message보냄    }
    }
}