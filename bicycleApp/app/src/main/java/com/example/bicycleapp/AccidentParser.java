package com.example.bicycleapp;

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
public class AccidentParser extends AsyncTask<String, Void, String> {

    String clientKey = "i7hUwTM8Zfmbt9KD8A%2Bgeh1N61Yl%2FY50rxRFI%2BiR0psYWixhym4%2BfpIKX0hiAB%2Fs";    private String str, receiveMsg;

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL("http://taas.koroad.or.kr/data/rest/frequentzone/bicycle?authKey="+clientKey+
                    "&searchYearCd=2019038&sido=11&guGun=680&type=json");

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

        String spot_nm = ""; //다발지역 지점의 위치
        String occrrnc_cnt = ""; //발생건수
        String dth_dnv_cnt = ""; //사망자수
        String lo_crd = ""; //경도
        String la_crd = ""; //위도

//        List<String> spot_nm = new ArrayList<>();
//        List<Integer> occrrnc_cnt = new ArrayList<>();
//        List<Integer> dth_dnv_cnt = new ArrayList<>();
//        List<Double> la_crd = new ArrayList<>();
//        List<Double> lo_crd = new ArrayList<>();


        String[] accidentarr = new String[5];
        try {

//            ArrayList<AccidentParser> accidentParsers = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(receiveMsg).getJSONObject("items");
            JSONArray jsonArray = jsonObject.getJSONArray("item");

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

            spot_nm = jsonArray.getJSONObject(0).getString("spot_nm");
            occrrnc_cnt = jsonArray.getJSONObject(0).getString("occrrnc_cnt");
            dth_dnv_cnt = jsonArray.getJSONObject(0).getString("dth_dnv_cnt");
            lo_crd = jsonArray.getJSONObject(0).getString("lo_crd");
            la_crd = jsonArray.getJSONObject(0).getString("la_crd");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String msg = "위치:" + spot_nm + "\n사고건수: " + occrrnc_cnt + "\n사망자수" + dth_dnv_cnt +
                "\n경도: " + lo_crd + "\n위도: " + la_crd;
        return msg;
    }
}