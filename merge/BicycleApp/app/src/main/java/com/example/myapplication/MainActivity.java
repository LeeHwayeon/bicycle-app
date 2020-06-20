package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ListView;
import android.text.TextWatcher;
import android.text.Editable;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;

    private NaverMap naverMap;
    Switch roadSwitch;
    private Button btnStart, btnEnd;
    private TextView tv_id, tv_pass, tv_gender, tv_age, tv_gu;

    Context context;

    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;


    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tv_id = findViewById(R.id.tv_id);
        tv_pass = findViewById(R.id.tv_pass);
        tv_gender = findViewById(R.id.tv_gender);
        tv_age = findViewById(R.id.tv_age);
        tv_gu = findViewById(R.id.tv_gu);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String userGender = intent.getStringExtra("userGender");
        String userAge = intent.getStringExtra("userAge");
        String userGu = intent.getStringExtra("userGu");

        tv_id.setText(userID);
        tv_pass.setText(userPassword);
        tv_gender.setText(userGender);
        tv_age.setText(userAge);
        tv_gu.setText(userGu);


        //특정시간에 사고주의알림
        new AlarmHATT(getApplicationContext()).Alarm();

        TextView textView = (TextView)findViewById(R.id.textView);
        String resultText = "값이없음";

        try {
            resultText = new AccidentParser().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        textView.setText(resultText);


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

        //자전거 사고다발지역표시
        //setUpAccidentMap();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //지도 view
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.favorite:
            Toast.makeText(this, "즐겨찾기", Toast.LENGTH_SHORT).show();
            Intent favorite = new Intent(MainActivity.this, FavoritActivity.class);
            startActivity(favorite);
            return true;
            case R.id.search:
                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
                return true;
            case R.id.home:
                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
                Intent home= new Intent(MainActivity.this, MainActivity.class);
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



    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;

        //사용자에게 먼저 위치 정보를 허용할지 물어봄
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        //현위치 ui
        uiSettings.setLocationButtonEnabled(true);

        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setMap(naverMap);


    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }

    /*
        //자전거 사고 정보
        public void setUpAccidentMap() {
            parser parser = new parser();
            ArrayList<AccidentDTO> accidentDTO = new ArrayList<AccidentDTO>();
            try {
                accidentDTO = parser.apiParserSearch();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(int i=0; i<accidentDTO.size(); i++){
                for(AccidentDTO entity : accidentDTO) {
                    Marker ac_marker = new Marker();

                    ac_marker.setPosition(new LatLng(entity.getLongitude(), entity.getLatitude()));
                    ac_marker.setCaptionText(entity.getSpot_nm());

                    ac_marker.setMap(naverMap);
                }
            }
        }
    */
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