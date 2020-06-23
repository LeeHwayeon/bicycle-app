package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GpsInfo";
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    TextView tv_currentWeather;
    TextView tv_WeatherInfo;
    TextView GpsTextView;

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


//    ArrayList<Weather> ListWeather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GpsTextView = (TextView) findViewById(R.id.GpsTextView);

        GetGps();
        Initialize();

//        Log.d("mForecastWeatherData", String.valueOf(mForecastWeatherData.size()));



        // 여기서부터 listView 를 구현하기 위해서 추가함.
//        ListView forecastListView = (ListView)findViewById(R.id.listView);
//        final ForecastAdapter forecastAdapter = new ForecastAdapter(this,mForecastWeather);
//
//        forecastListView.setAdapter(forecastAdapter);

//        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id){
//                Toast.makeText(getApplicationContext(),
//                        forecastAdapter.getItem(position).getForecast_week(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });

//        ListView currentListView = (ListView)findViewById(R.id.listView);
//        final CurrentAdapter currentAdapter = new CurrentAdapter(this, mCurrentWeather);
//
//        currentListView.setAdapter(currentAdapter);


//        ListView WeatherListView = (ListView)findViewById(R.id.listView);
//        final Weather[] items = new Weather[8];
//
//        for (int i = 0; i < ListWeather.size(); i++) {
//            if (i == 0) {
//                items[i] = new Weather("current " + i, WeatherAdapter.TYPE_CURRENT);
//            } else {
//                items[i] = new Weather("forecast " + i, WeatherAdapter.TYPE_FORECAST);
//            }
//        }

//
//        ListView WeatherListView = (ListView)findViewById(R.id.listView);
//        final WeatherAdapter weatherAdapter= new WeatherAdapter(this,ListWeather);
//         WeatherListView.setAdapter(weatherAdapter);


        ListView listView = (ListView)findViewById(R.id.listView);
        final CurrentAdapter currentAdapter = new CurrentAdapter(this, mCurrentWeather);
        final ForecastAdapter forecastAdapter = new ForecastAdapter(this, mForecastWeather);
        for (int i = 0; i<8; i++) {
            if(i == 0) {
                listView.setAdapter(currentAdapter);
                Log.d("list", String.valueOf(listView));
            }
            else {
                listView.setAdapter(forecastAdapter);
                Log.d("list", String.valueOf(listView));
            }
        }

//        listView.setAdapter(currentAdapter);


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

            String address = "위도 : " + lat + "\n경도 : " + lon + "\n주소 : " + addr;
            GpsTextView.setText(address);


        } else {
            // GPS 를 사용할수 없으므로
            Log.d(TAG, "isGetLocation is false");
            gps.stopUsingGPS(MainActivity.this);
            showSettingsAlert();
        }
    }

    public void Initialize() {
        tv_currentWeather = (TextView) findViewById(R.id.tv_currentWeather);
        tv_WeatherInfo = (TextView) findViewById(R.id.tv_WeatherInfo);
        mForecastWeather = new ArrayList<>();
        mCurrentWeather = new ArrayList<>();
        mThis = this;
        mWeather = new WeatherManager(lon, lat, mThis);
        mWeather.run();
        Log.d("mWeather 실행", String.valueOf(mWeather));
    }


    // 읽어온 현재 날씨 출력
    public String CurrentPrintValue() {
        String currentData = "";
        for(int i = 0; i < 1; i++) {
            currentData = currentData
                    + mCurrentWeather.get(i).current_main + " / "
                    + mCurrentWeather.get(i).current_description + "\r\n"
                    + "온도 "+ mCurrentWeather.get(i).current_temp + "℃ / "
                    + "체감온도 " + mCurrentWeather.get(i).current_feelsLike + "℃ \r\n"
                    + "습도 " + mCurrentWeather.get(i).current_humidity + "% / "
                    + "풍속 " + mCurrentWeather.get(i).current_windSpeed + " m/s / "
                    + "자외선 " + mCurrentWeather.get(i).current_uvi + "\r\n"
                    + "최저 온도 " + mCurrentWeather.get(i).current_minTemp + "℃ / "
                    + "최고 온도 " + mCurrentWeather.get(i).current_maxTemp + "℃";
            currentData = currentData + "\r\n" + "----------------------------------------------" + "\r\n";
            Log.d("currentData", currentData);
        }
        return currentData;
    }

    // 읽어온 예보 날씨 출력
    public String ForecastPrintValue() {
        String forecastData = "";
        for (int i = 1; i < mForecastWeather.size(); i++) {
            forecastData = forecastData
                    + mForecastWeather.get(i).getForecast_week() + "\r\n"
                    + mForecastWeather.get(i).getForecast_main() + " "
                    + mForecastWeather.get(i).getForecast_minTemp() + " "
                    + mForecastWeather.get(i).getForecast_maxTemp();

            forecastData = forecastData + "\r\n" + "----------------------------------------------" + "\r\n";
            Log.d("forecastData의 결과", forecastData);
        }
        return forecastData;
    }


    public void DataToInformation() {
        for (int i = 0; i < mCurrentWeatherData.size(); i++) {
            mCurrentWeather.add(new CurrentWeather(
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
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_week")),
//                    week[i] = String.valueOf(mForecastWeatherData.get(i).get("forecast_week")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_main")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_minTemp")),
                    String.valueOf(mForecastWeatherData.get(i).get("forecast_maxTemp"))
            ));
            Log.d("mForecastWeather", String.valueOf(mForecastWeather));
        }

    }

//    for (int i = 0; i < mForecastWeatherData.size(); i++) {
//        week[i] = String.valueOf(mForecastWeatherData.get(i).get("forecast_week"));
//        Log.d("mForecastWeather", String.valueOf(week[i]));
//    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO:
                    mWeather.getWeather();
                    Log.d("handler mWeather.getWeather() 작동 결과", String.valueOf(mWeather.getWeather()));

                    // 현재
                    mCurrentWeatherData = mWeather.getWeather();
                    Log.d("mCurrentWeatherData 작동 결과", String.valueOf(mCurrentWeatherData));
                    if (mCurrentWeatherData.size() == 0)
                        tv_currentWeather.setText("데이터가 없습니다");

                    // 예보
                    mForecastWeatherData = mWeather.getWeather();
                    Log.d("mForecastWeatherData 작동 결과", String.valueOf(mForecastWeatherData));
                    Log.d("mForecastWeatherData size", String.valueOf(mForecastWeatherData.size()));
                    if (mForecastWeatherData.size() == 0)
                        tv_WeatherInfo.setText("데이터가 없습니다");

                    DataToInformation(); // 자료 클래스로 저장,

                    String current = "";
                    String forecast = "";

//                    DataChangedToHangeul();
                    current = CurrentPrintValue();
                    forecast = ForecastPrintValue();

                    tv_currentWeather.setText(current);
                    tv_WeatherInfo.setText(forecast);
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