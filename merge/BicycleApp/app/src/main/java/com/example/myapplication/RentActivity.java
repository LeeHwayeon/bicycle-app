package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RentActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;

    private NaverMap naver;

    Context context;

    private static String TAG = "myapplication_RentActivity";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LONGITUDE="longitude";
    private static final String TAG_LATITUDE ="latitude";

    private TextView TextViewResult;
    ArrayList<HashMap<String, String>> ArrayList;
    String JsonString;

    double latValue;
    double longValue;

    // php 서버로 부터 받아온 대여소 데이터를 저장할 리스트
    ArrayList<Double> latitude_list;
    ArrayList<Double> longitude_list;
    ArrayList<String> name_list;

    //대여소 마커를 관리하는 객체를 담을 리스트
    ArrayList<Marker> rental_list;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //대여소 정보
        GetRental getRental = new GetRental();
        getRental.execute("http://congping2.dothome.co.kr/aa.php");
        latitude_list = new ArrayList<>();
        longitude_list = new ArrayList<>();
        name_list = new ArrayList<>();
        rental_list = new ArrayList<>();

        //지도 view
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextViewResult = (TextView) findViewById(R.id.textView_main_result);
        TextViewResult.setVisibility(View.INVISIBLE);
        ArrayList = new ArrayList<>();

        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
    }

    //자전거 대여소
    private class GetRental extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(RentActivity.this,
                    "Please Wait", null, true, true);
        }

        //백그라운드 실행
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            TextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){
                TextViewResult.setText(errorString);
            }
            else {
                JsonString = result;
                //대여소 정보 매핑하는 JSON
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    //대여소 정보 매핑
    private void showResult(){

        //데이터를 담아놓을 리스트를 초기화한다.
        latitude_list.clear();
        longitude_list.clear();
        name_list.clear();

        try {
            JSONObject rentalObject = new JSONObject(JsonString);
            JSONArray rentalArray = rentalObject.getJSONArray(TAG_JSON);

            for(int i=0;i<rentalArray.length();i++){

                JSONObject rent = rentalArray.getJSONObject(i);

                String address = rent.getString(TAG_ADDRESS);
                String name = rent.getString(TAG_NAME);
                String longitude = rent.getString(TAG_LONGITUDE);
                String latitude = rent.getString(TAG_LATITUDE);

                //위도경도는 숫자로 바꿔줌
                latValue = Double.parseDouble(latitude); //converting string latitude value to double
                longValue = Double.parseDouble(longitude); //converting string longitude value to double

                //마커리스트담는 변수들
                latitude_list.add(latValue);
                longitude_list.add(longValue);
                name_list.add(name);

            }
            showMarker();
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


    // 지도에 마커를 표시한다
    public void showMarker(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 백그라운드 스레드
                for(Marker rental_marker : rental_list){
                    rental_marker.setMap(null);
                }
                rental_list.clear();

                // 가져온 데이터의 수 만큼 마커 객체를 만들어 표시한다.
                for(int i= 0 ; i< 200; i++) {
                    // 값 추출
                    double Lat = latitude_list.get(i);
                    double Lng = longitude_list.get(i);
                    String name = name_list.get(i);

                    // 생성할 마커의 정보를 가지고 있는 객체를 생성
                    Marker rental_marker = new Marker();
                    rental_marker.setPosition(new LatLng(Lat, Lng));

                    rental_marker.setIconPerspectiveEnabled(true);
                    rental_marker.setWidth(Marker.SIZE_AUTO);
                    rental_marker.setHeight(Marker.SIZE_AUTO);
                    rental_marker.setWidth(60);
                    rental_marker.setHeight(80);
                    rental_marker.setCaptionText(name);
                    rental_marker.setCaptionOffset(5);  //마커와 텍스간의 거리
                    rental_marker.setCaptionAligns(Align.Top);  //텍스트 위치 마커 위로
                    rental_marker.setCaptionTextSize(11);  //텍스트 사이즈 설정
//                    rental_marker.setIcon(OverlayImage.fromResource(R.drawable.rent_bike));

                    // 마커를 지도에 표시한다.
                    rental_list.add(rental_marker);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.rental_list:
                Toast.makeText(this, "대여소 리스트", Toast.LENGTH_SHORT).show();
                Intent list = new Intent(this, RentalActivity.class);
                startActivity(list);
                return true;
            case R.id.rental_marker:
                Toast.makeText(this, "대여소", Toast.LENGTH_SHORT).show();
                Intent rental_marker = new Intent(this, RentActivity.class);
                startActivity(rental_marker);
                return true;
            case R.id.weather:
                Toast.makeText(this, "날씨", Toast.LENGTH_SHORT).show();
                Intent weather= new Intent(this, WeatherActivity.class);
                startActivity(weather);
                return true;
            case R.id.home:
                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
                Intent home= new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naver = naverMap;

        //사용자에게 먼저 위치 정보를 허용할지 물어봄
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        //현위치 ui
        uiSettings.setLocationButtonEnabled(true);

        //대여소 정보 리스트 마커
        for (Marker rental_marker : rental_list) {
            rental_marker.setMap(naverMap);
        }
    }


    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }


}
