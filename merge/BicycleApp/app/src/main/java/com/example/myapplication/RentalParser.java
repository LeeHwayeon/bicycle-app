package com.example.myapplication;

import android.os.AsyncTask;
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

//json 파싱
public class RentalParser extends AsyncTask<String, Void, String> {

    String clientKey = "4f4867556d636f6e37304375746e63";
    private String str, receiveMsg;

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("http://openapi.seoul.go.kr:8088/"+clientKey+
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

        String rackTotCnt = ""; //거치대 개수
        String stationName = ""; //대여소이름
        String parkingBikeTotCnt = ""; //자전거 주차 총 건수
        String stationLatitude = ""; //위도
        String stationLongitude = ""; //경도

//        List<String> spot_nm = new ArrayList<>();
//        List<Integer> occrrnc_cnt = new ArrayList<>();
//        List<Integer> dth_dnv_cnt = new ArrayList<>();
//        List<Double> la_crd = new ArrayList<>();
//        List<Double> lo_crd = new ArrayList<>();


        String[] bicyclearr = new String[5];
        try {

//            ArrayList<AccidentParser> accidentParsers = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(receiveMsg).getJSONObject("rentBikeStatus");
            JSONArray jsonArray = jsonObject.getJSONArray("row");

            Log.i("넘어온 결과1", String.valueOf(jsonObject));
            Log.i("넘어온 결과2", String.valueOf(jsonArray));

//            for(int i=0; i<jsonArray.length(); i++){
//                JSONObject accident = jsonArray.getJSONObject(i);
//
//                String addr = accident.getString("spot_nm");
//                Integer sago = accident.getInt("occrrnc_cnt");
//                Integer death = accident.getInt("dth_dnv_cnt");
//                Double latitude = accident.getDouble("la_crd"); //String sLAT = subJsonObject.getString("latitude");
//                Double longitude = accident.getDouble("lo_crd"); //String sLNG = subJsonObject.getString("longitude");
//
//                spot_nm.add(accident.getString("spot_nm"));
//                occrrnc_cnt.add(accident.getInt("occrrnc_cnt"));
//                dth_dnv_cnt.add(accident.getInt("dth_dnv_cnt"));
//                la_crd.add(accident.getDouble("la_crd"));
//                lo_crd.add(accident.getDouble("lo_crd"));
//
//            }

            rackTotCnt = jsonArray.getJSONObject(0).getString("rackTotCnt");
            stationName = jsonArray.getJSONObject(0).getString("stationName");
            parkingBikeTotCnt = jsonArray.getJSONObject(0).getString("parkingBikeTotCnt");
            stationLatitude = jsonArray.getJSONObject(0).getString("stationLatitude");
            stationLongitude = jsonArray.getJSONObject(0).getString("stationLongitude");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String msg = "거치대 개수: " + rackTotCnt + "\n 대여소 이름: " + stationName + "\n 자전거 주차 총 건수: " + parkingBikeTotCnt +
                "\n 위도: " + stationLatitude + "\n 경도: " + stationLongitude;
        return msg;
    }
}

