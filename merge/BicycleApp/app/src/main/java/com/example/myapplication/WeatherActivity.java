package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "GpsInfo";
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    TextView tv_WeatherInfo;
    TextView GpsTextView;

    boolean ResumeFlag = false;

    private GpsInfo gps;

    ForeCastManager mForeCast;

    String lon; // 좌표 설정
    String lat;  // 좌표 설정
    String juso;  // 주소 설정

    WeatherActivity mThis;
    ArrayList<ContentValues> mWeatherData;
    ArrayList<WeatherInfo> mWeatherInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        GpsTextView = (TextView) findViewById(R.id.GpsTextView);

        GetGps();
        Initialize();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();

        // GetGps();
        //Initialize();
        Log.d(TAG, "onResume() if문 밖");
        ResumeFlag = true;
        Log.d(TAG, "onResume() 정상 종료");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    public void GetGps() {
        gps = new GpsInfo(WeatherActivity.this, mThis);
        Log.d(TAG, "gps = new GetGps()");
        // GPS 사용유무 가져오기

        if (gps.isGetLocation()) {
            Log.d(TAG, "isGetLocation is true");
            lat = String.valueOf(gps.getLatitude());
            lon = String.valueOf(gps.getLongitude());
            juso = gps.getAddress();

            String address = "위도 : " + lat + "\n경도 : " + lon + "\n주소 : " + juso;
            GpsTextView.setText(address);


        } else {
            // GPS 를 사용할수 없으므로
            Log.d(TAG, "isGetLocation is false");
            gps.stopUsingGPS(WeatherActivity.this);
            showSettingsAlert();
        }
    }

    public void Initialize() {
        tv_WeatherInfo = (TextView) findViewById(R.id.tv_WeatherInfo);
        mWeatherInformation = new ArrayList<>();
        mThis = this;
        mForeCast = new ForeCastManager(lon, lat, mThis);
        mForeCast.run();
        Log.d("ForeCastMaber의 실행", String.valueOf(mForeCast));
    }

    //읽어온 날씨 값을 출력해주는 메써드
    @SuppressLint("LongLogTag")
    public String PrintValue() {
        String mData = "";
        for (int i = 0; i < mWeatherInformation.size(); i++) {
            Log.d("mWeatherInfomation.size() 확인", String.valueOf(mWeatherInformation.size()));
            mData = mData
                    + mWeatherInformation.get(i).getWeather_Name() + "\r\n"
                    + "Max: " + mWeatherInformation.get(i).getTemp_Max() + "℃"
                    + " /Min: " + mWeatherInformation.get(i).getTemp_Min() + "℃" + "\r\n";

            mData = mData + "\r\n" + "----------------------------------------------" + "\r\n";
            Log.d("mWeatherInfomation.get(i).getWeather_Name()", String.valueOf(mWeatherInformation.get(1).getWeather_Name()));
            Log.d("mData의 결과", mData);

        }
        return mData;
    }
    public void DataToInformation() {
        for (int i = 0; i < mWeatherData.size(); i++) {
            mWeatherInformation.add(new WeatherInfo(
                    String.valueOf(mWeatherData.get(i).get("weather_Name")),
                    String.valueOf(mWeatherData.get(i).get("temp_Min")),
                    String.valueOf(mWeatherData.get(i).get("temp_Max"))
            ));
        }
    }

    public Handler handler = new Handler() {
        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO:
                    mForeCast.getmWeather();
                    Log.d("handler mForeCast.getmWeather() 작동 결과", String.valueOf(mForeCast.getmWeather()));
                    mWeatherData = mForeCast.getmWeather();
                    Log.d("mWeatherData 작동 결과", String.valueOf(mWeatherData));
                    if (mWeatherData.size() == 0)
                        tv_WeatherInfo.setText("데이터가 없습니다");

                    DataToInformation(); // 자료 클래스로 저장,

                    String data = "";
                    data = PrintValue();
                    Log.i("handler 작동 결과", data);

                    tv_WeatherInfo.setText(data);
                    break;
                default:
                    break;
            }
        }
    };

    public void showSettingsAlert() {
        Log.d(TAG, "Dialog start");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeatherActivity.this);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 사용이 설정되지 않았습니다..\n 설정창으로 가시겠습니까?");
        Log.d(TAG, "Dialog 창 설정");
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        // OK 를 누르게 되면 설정창으로 이동합니다.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Dialog settings");
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        Log.d(TAG, "Dialog 창 설정 완료");
        alertDialog.show();
        Log.d(TAG, "Dialog 창 띄움");
    };


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.favorite:
                Toast.makeText(this, "즐겨찾기", Toast.LENGTH_SHORT).show();
                Intent favorite = new Intent(WeatherActivity.this, FavoriteActivity.class);
                startActivity(favorite);
                return true;
            case R.id.search:
                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(WeatherActivity.this, SearchActivity.class);
                startActivity(search);
                return true;
            case R.id.weather:
                Toast.makeText(this, "날씨", Toast.LENGTH_SHORT).show();
                Intent weather= new Intent(WeatherActivity.this, WeatherActivity.class);
                startActivity(weather);
                return true;
            case R.id.home:
                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
                Intent home= new Intent(WeatherActivity.this, MainActivity.class);
                startActivity(home);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
