package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class RentalActivity extends AppCompatActivity {
    private static final String TAG = "GpsInfo";
    public static final int THREAD_HANDLER_SUCCESS_INFO = 1;
    TextView tv_currentWeather;
    TextView tv_WeatherInfo;
    TextView GpsTextView;


    boolean ResumeFlag = false;


    RentalManager mRental;

    String rackTotCnt;
    String stationName;
    String parkingBikeTotCnt;
    String stationLatitude;
    String stationLongitude;

    RentalActivity mThis;

    // 현재
    ArrayList<CurrentRental> mCurrentRental;
    ArrayList<ContentValues> mCurrentRentalData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental);
        GpsTextView = (TextView) findViewById(R.id.GpsTextView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

        Initialize();

        ListView listView = (ListView)findViewById(R.id.listView);
        final RentalAdapter rentalAdapter = new RentalAdapter(this, mCurrentRental);
        for (int i = 0; i<8; i++) {
            if(i == 0) {
                listView.setAdapter(rentalAdapter);
                Log.d("list", String.valueOf(listView));
            }
            else {

            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rental_list:
                Toast.makeText(this, "대여소 리스트", Toast.LENGTH_SHORT).show();
                Intent favorite = new Intent(this, RentalActivity.class);
                startActivity(favorite);
                return true;
            case R.id.rental_marker:
                Toast.makeText(this, "대여소", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(this, RentActivity.class);
                startActivity(search);
                return true;
            case R.id.weather:
                Toast.makeText(this, "날씨", Toast.LENGTH_SHORT).show();
                Intent weather = new Intent(this, WeatherActivity.class);
                startActivity(weather);
                return true;
            case R.id.home:
                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
        }
        return super.onOptionsItemSelected(item);
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


    public void Initialize() {
        tv_currentWeather = (TextView) findViewById(R.id.tv_currentRental);
        tv_WeatherInfo = (TextView) findViewById(R.id.tv_WeatherInfo);
        mCurrentRental = new ArrayList<>();
        mThis = this;
        mRental = new RentalManager(rackTotCnt, stationName, parkingBikeTotCnt, stationLatitude, stationLongitude, mThis);
        mRental.run();
        Log.d("mWeather 실행", String.valueOf(mRental));
    }


    public String CurrentPrintValue() {
        String currentData = "";
        for(int i = 0; i < mCurrentRental.size(); i++) {
            currentData = currentData
                    + "rackTotCnt"+mCurrentRental.get(i).rackTotCnt + " / "
                    + "stationName"+mCurrentRental.get(i).stationName + "\r\n"
                    + "parkingBikeTotCnt"+ mCurrentRental.get(i).parkingBikeTotCnt + "℃ / "
                    + "stationLatitude" + mCurrentRental.get(i).stationLatitude + "℃ \r\n"
                    + "stationLongitude" + mCurrentRental.get(i).stationLongitude;
            currentData = currentData + "\r\n" + "----------------------------------------------" + "\r\n";
            Log.d("currentData", currentData);
        }
        return currentData;
    }

    public void DataToInformation() {
        for (int i = 0; i < mCurrentRentalData.size(); i++) {
            mCurrentRental.add(new CurrentRental(
                    String.valueOf(mCurrentRentalData.get(i).get("rackTotCnt")),
                    String.valueOf(mCurrentRentalData.get(i).get("stationName")),
                    String.valueOf(mCurrentRentalData.get(i).get("parkingBikeTotCnt")),
                    String.valueOf(mCurrentRentalData.get(i).get("stationLatitude")),
                    String.valueOf(mCurrentRentalData.get(i).get("stationLongitude"))
            ));
            Log.d("mCurrentRentalData", String.valueOf(mCurrentRental));
        }

    }

    public Handler handler = new Handler() {
        @SuppressLint("LongLogTag")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case THREAD_HANDLER_SUCCESS_INFO:
                    mRental.getRental();
                    Log.d("handler mWeather.getWeather() 작동 결과", String.valueOf(mRental.getRental()));

                    // 현재
                    mCurrentRentalData = mRental.getRental();
                    Log.d("mCurrentWeatherData 작동 결과", String.valueOf(mCurrentRentalData));
                    if (mCurrentRentalData.size() == 0)
                        tv_currentWeather.setText("데이터가 없습니다");


                    DataToInformation(); // 자료 클래스로 저장,

                    String current = "";
                    String forecast = "";

                    current = CurrentPrintValue();

                    tv_currentWeather.setText(current);
                    break;
                default:
                    break;
            }
        }
    };

}