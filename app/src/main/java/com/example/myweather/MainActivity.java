package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GpsInfo";
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;

    TextView GpsTextView;
    TextView current_main;
    TextView current_description;
    TextView current_temp;
    TextView current_feelsLike;
    TextView current_minTemp;
    TextView current_maxTemp;
    TextView current_humidity;
    TextView current_windSpeed;
    TextView current_uvi;

    ImageView current_icon;
    ImageView forecast_icon;

    boolean ResumeFlag = false;

    private GpsInfo gps;

    WeatherManager mWeather;

    String lon; // 위도
    String lat;  // 경도
    String addr;  // 주소

    MainActivity mThis;

    // 현재
    ArrayList<CurrentWeather> mCurrentWeather;
    ArrayList<ContentValues> mCurrentWeatherData;

    // 예보
    ArrayList<ForecastWeather> mForecastWeather;
    ArrayList<ContentValues> mForecastWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GpsTextView = (TextView) findViewById(R.id.GpsTextView);

        GetGps();
        Initialize();

        // 여기서부터 listView 를 구현하기 위해서 추가함.
        ListView forecastListView = (ListView)findViewById(R.id.listView);
        final ForecastAdapter forecastAdapter = new ForecastAdapter(this,mForecastWeather);
        forecastListView.setAdapter(forecastAdapter);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
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
        gps = new GpsInfo(MainActivity.this, mThis);
        Log.d(TAG, "gps = new GetGps()");
        // GPS 사용유무 가져오기

        if (gps.isGetLocation()) {
            Log.d(TAG, "isGetLocation is true");
            lat = String.valueOf(gps.getLatitude());
            lon = String.valueOf(gps.getLongitude());
            addr = gps.getAddress();

            String address = "lon=" + lon + "lat" + lat + addr;
            GpsTextView.setText(address);


        } else {
            // GPS 를 사용할수 없으므로
            Log.d(TAG, "isGetLocation is false");
            gps.stopUsingGPS(MainActivity.this);
            showSettingsAlert();
        }
    }

    public void Initialize() {
//        tv_currentWeather = (TextView) findViewById(R.id.tv_currentWeather);
//        tv_WeatherInfo = (TextView) findViewById(R.id.tv_WeatherInfo);

        current_main = (TextView) findViewById(R.id.current_main);
        current_description = (TextView) findViewById(R.id.current_description);
        current_temp = (TextView) findViewById(R.id.current_temp);
        current_feelsLike =(TextView) findViewById(R.id.current_feelsLike);
        current_humidity = (TextView) findViewById(R.id.current_humidity);
        current_windSpeed = (TextView) findViewById(R.id.current_windSpeed);
        current_uvi = (TextView) findViewById(R.id.current_uvi);
        current_minTemp = (TextView) findViewById(R.id.current_minTemp);
        current_maxTemp =(TextView) findViewById(R.id.current_maxTemp);
        current_icon = (ImageView) findViewById(R.id.list_item_icon);

//        setImage(mCurrentWeather.g);
//        setImage(mCurrentWeather.get(0).getCurrent_icon(), current_icon);

        mForecastWeather = new ArrayList<>();
        mCurrentWeather = new ArrayList<>();
        mThis = this;
        mWeather = new WeatherManager(lon, lat, mThis);
        mWeather.run();
        Log.d("mWeather 실행", String.valueOf(mWeather));
        Log.d("mForecastWeather 실행", String.valueOf(mForecastWeather));
        Log.d("mCurrentWeather 실행", String.valueOf(mCurrentWeather));

    }

    public void DataToInformation() {
        for (int i = 0; i < mCurrentWeatherData.size(); i++) {
            mCurrentWeather.add(new CurrentWeather(
                    String.valueOf(mCurrentWeatherData.get(i).get("current_icon")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_main")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_description")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_temp")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_feelsLike")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_humidity")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_windSpeed")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_uvi")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_minTemp")),
                    String.valueOf(mCurrentWeatherData.get(i).get("current_maxTemp"))
            ));
            Log.d("mCurrentWeather", String.valueOf(mCurrentWeather));
        }

        for (int i = 0; i < mForecastWeatherData.size(); i++) {
            mForecastWeather.add(new ForecastWeather(
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_icon")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_week")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_main")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_minTemp")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_maxTemp"))
            ));
            Log.d("mForecastWeather", String.valueOf(mForecastWeather));
        }
    }

    public void setImage(String icon, ImageView img) {
        if (icon.equals("01d")) {
            img.setImageResource(R.drawable.d01);
        } else if (icon.equals("01n")) {
            img.setImageResource(R.drawable.n01);
        } else if (icon.equals("02d")) {
            img.setImageResource(R.drawable.d02);
        } else if (icon.equals("02n")) {
            img.setImageResource(R.drawable.n02);
        } else if (icon.equals("03d")) {
            img.setImageResource(R.drawable.d03);
        } else if (icon.equals("03n")) {
            img.setImageResource(R.drawable.n03);
        } else if (icon.equals("04d")) {
            img.setImageResource(R.drawable.d04);
        } else if (icon.equals("04n")) {
            img.setImageResource(R.drawable.n04);
        } else if (icon.equals("09d")) {
            img.setImageResource(R.drawable.d09);
        } else if (icon.equals("09n")) {
            img.setImageResource(R.drawable.n09);
        } else if (icon.equals("10d")) {
            img.setImageResource(R.drawable.d10);
        } else if (icon.equals("10n")) {
            img.setImageResource(R.drawable.sun);
        } else if (icon.equals("11d")) {
            img.setImageResource(R.drawable.d11);
        } else if (icon.equals("11n")) {
            img.setImageResource(R.drawable.n11);
        } else if (icon.equals("13d")) {
            img.setImageResource(R.drawable.d13);
        } else if (icon.equals("13n")) {
            img.setImageResource(R.drawable.n13);
        } else if (icon.equals("50d")) {
            img.setImageResource(R.drawable.d50);
        } else if (icon.equals("50n")) {
            img.setImageResource(R.drawable.n50);
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO:
                    mWeather.getWeather();
//                    Log.d("handler mWeather.getWeather() 작동 결과", String.valueOf(mWeather.getWeather()));

                    // 현재
                    mCurrentWeatherData = mWeather.getWeather();
//                    Log.d("mCurrentWeatherData 작동 결과", String.valueOf(mCurrentWeatherData));
//                    if (mCurrentWeatherData.size() == 0)
//                        tv_currentWeather.setText("데이터가 없습니다");

                    // 예보
                    mForecastWeatherData = mWeather.getWeather();
//                    Log.d("mForecastWeatherData 작동 결과", String.valueOf(mForecastWeatherData));
//                    Log.d("mForecastWeatherData size", String.valueOf(mForecastWeatherData.size()));
//                    if (mForecastWeatherData.size() == 0)
//                        tv_WeatherInfo.setText("데이터가 없습니다");

                    DataToInformation(); // 자료 클래스로 저장,

                    // 현재 날씨 띄우는 view
                    String main = mCurrentWeather.get(0).current_main;
                    String description = mCurrentWeather.get(0).current_description;
                    String temp = mCurrentWeather.get(0).current_temp;
                    String feelsLike = mCurrentWeather.get(0).current_feelsLike;
                    String humidity = mCurrentWeather.get(0).current_humidity;
                    String windSpeed = mCurrentWeather.get(0).current_windSpeed;
                    String uvi = mCurrentWeather.get(0).current_uvi;
                    String minTemp = mCurrentWeather.get(0).current_minTemp;
                    String maxTemp = mCurrentWeather.get(0).current_maxTemp;
                    String icon = mCurrentWeather.get(0).current_icon;

                    current_main.setText(main);
                    current_description.setText(description);
                    current_temp.setText(temp);
                    current_feelsLike.setText(feelsLike);
                    current_humidity.setText(humidity);
                    current_windSpeed.setText(windSpeed);
                    current_uvi.setText(uvi);
                    current_minTemp.setText(minTemp);
                    current_maxTemp.setText(maxTemp);

//                    setImage(icon, current_icon);

                    Log.d("current_icon", icon);
                    if (icon.equals("10n")) {
                        current_icon.setImageResource(R.drawable.sun);
                    }
                    current_icon.setImageResource(R.drawable.sun);



                    //DataChangedToHangeul();
                    break;
                default:
                    break;
            }
        }
    };

    public void showSettingsAlert() {
        Log.d(TAG, "Dialog start");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

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
}