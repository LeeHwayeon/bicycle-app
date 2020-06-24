package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;

    private NaverMap naverMap;
    Switch roadSwitch;

    Context context;

    private static String TAG = "myapplication_MainActivity";

    private static final String TAG_JSON="webnautes";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_SAGOGUNSU = "sagogunsu";
    private static final String TAG_SAMANGJASU ="samangjasu";
    private static final String TAG_LONGITUDE="longitude";
    private static final String TAG_LATITUDE ="latitude";

    private TextView mTextViewResult;
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;

    double latitudeValue;
    double longitudeValue;

    // php 서버로 부터 받아온 사고 데이터를 저장할 리스트
    ArrayList<Double> lat_list;
    ArrayList<Double> lng_list;
    ArrayList<String> sago_list;

    //사고 마커를 관리하는 객체를 담을 리스트
    ArrayList<Marker> markers_list;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //사고정보
        GetData task = new GetData();
        task.execute("http://congping2.dothome.co.kr/bb.php");
        lat_list = new ArrayList<>();
        lng_list = new ArrayList<>();
        sago_list = new ArrayList<>();
        markers_list = new ArrayList<>();

        //지도 view
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        mTextViewResult.setVisibility(View.INVISIBLE);
        mArrayList = new ArrayList<>();


        //특정시간에 사고주의알림
        new AlarmHATT(getApplicationContext()).Alarm();

//        TextView textView = (TextView)findViewById(R.id.textView);
//        String resultText = "값이없음";
//
//        try {
//            resultText = new AccidentParser().execute().get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//        textView.setText(resultText);


        //자전거 도로
        roadSwitch = (Switch) findViewById(R.id.roadSwitch);
        roadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Toast.makeText(MainActivity.this, "자전거도로 표시", Toast.LENGTH_SHORT).show();
                    //자전거 도로 표시 on
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);
                }
                else{
                    //자전거 도로 표시 off
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, false);
                }
            }
        });

        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);
    }

    //자전거 사고
    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        //백그라운드 실행
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "response  - " + result);

            if (result == null){
                mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                //사고 정보 매핑하는 JSON
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

    //사고정보 매핑
    private void showResult(){

        //데이터를 담아놓을 리스트를 초기화한다.
        lat_list.clear();
        lng_list.clear();
        sago_list.clear();

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String address = item.getString(TAG_ADDRESS);
                String sagogunsu = item.getString(TAG_SAGOGUNSU);
                String samangjasu = item.getString(TAG_SAMANGJASU);
                String longitude = item.getString(TAG_LONGITUDE);
                String latitude = item.getString(TAG_LATITUDE);

                //위도경도는 숫자로 바꿔줌
                latitudeValue = Double.parseDouble(latitude); //converting string latitude value to double
                longitudeValue = Double.parseDouble(longitude); //converting string longitude value to double

                //마커리스트담는 변수들
                lat_list.add(latitudeValue);
                lng_list.add(longitudeValue);
                sago_list.add(sagogunsu);

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
                for(Marker marker : markers_list){
                    marker.setMap(null);
                }
                markers_list.clear();

                InfoWindow infoWindow = new InfoWindow();

                // 가져온 데이터의 수 만큼 마커 객체를 만들어 표시한다.
                for(int i= 0 ; i< lat_list.size(); i++) {
                    // 값 추출
                    double lat = lat_list.get(i);
                    double lng = lng_list.get(i);
                    String sagogunsu = sago_list.get(i);

                    // 생성할 마커의 정보를 가지고 있는 객체를 생성
                    Marker marker = new Marker();
                    marker.setPosition(new LatLng(lat, lng));
//                    marker.setIcon(MarkerIcons.BLACK);
//                    marker.setIconTintColor(Color.RED);  //마커색깔
                    marker.setIconPerspectiveEnabled(true);
                    marker.setWidth(Marker.SIZE_AUTO);
                    marker.setHeight(Marker.SIZE_AUTO);
                    marker.setWidth(60);
                    marker.setHeight(80);
                    marker.setCaptionText("사고건수 : " + sagogunsu);
                    marker.setCaptionOffset(5);  //마커와 텍스간의 거리
                    marker.setCaptionAligns(Align.Top);  //텍스트 위치 마커 위로
                    marker.setCaptionTextSize(10);  //텍스트 사이즈 설정
                    marker.setIcon(OverlayImage.fromResource(R.drawable.accident));

                    // 마커를 지도에 표시한다.
                    markers_list.add(marker);
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
            case R.id.favorite:
                Toast.makeText(this, "즐겨찾기", Toast.LENGTH_SHORT).show();
                Intent favorite = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(favorite);
                return true;
            case R.id.search:
                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(MainActivity.this, RentalActivity.class);
                startActivity(search);
                return true;
            case R.id.weather:
                Toast.makeText(this, "날씨", Toast.LENGTH_SHORT).show();
                Intent weather= new Intent(MainActivity.this, WeatherActivity.class);
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
        this.naverMap = naverMap;

        //사용자에게 먼저 위치 정보를 허용할지 물어봄
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        //현위치 ui
        uiSettings.setLocationButtonEnabled(true);

        //사고정보 리스트 마커
        for (Marker marker : markers_list) {
            marker.setMap(naverMap);
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

    //알림
    public class AlarmHATT {
        private Context context;

        public AlarmHATT(Context context) {
            this.context = context;
        }

        public void Alarm() {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();

            //사고많은 6시 퇴근시간에 알림창 띄우기
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 18, 0, 0);

            //알람 예약
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
        }
    }


}
