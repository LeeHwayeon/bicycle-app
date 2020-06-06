package com.example.bicycleapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    NaverMap map;
    Switch roadSwitch;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.enableDefaults();

        //자전거 도로
        roadSwitch = (Switch) findViewById(R.id.roadSwitch);
        roadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Toast.makeText(MainActivity.this, "자전거도로 표시", Toast.LENGTH_SHORT).show();
                    //자전거 도로 표시 on
                    map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);

                }
                else{
                    //자전거 도로 표시 off
                    map.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, false);

                }
            }
        });

        //지도 view
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        //자전거 사고정보
        setUpAccidentMap();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //자전거 대여소 정보
        setUpRentalMap();

        //위치
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        map = naverMap;

        //위치설정
        map.setLocationSource(locationSource);

        //위치 오버레이 설정
        LocationOverlay locationOverlay = map.getLocationOverlay();
        locationOverlay.setVisible(true);

        //사용자 인터페이스
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);  //현위치 버튼

    }

    //자전거 사고 정보
    public void setUpAccidentMap() {
        AccidentParser parser = new AccidentParser();
        ArrayList<AccidentDTO> accidentDTO = new ArrayList<AccidentDTO>();
        try {
            accidentDTO = parser.apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<accidentDTO.size(); i++){
            for(AccidentDTO entity : accidentDTO) {
                Marker ac_marker = new Marker();
                LocationOverlay locationOverlay = map.getLocationOverlay();

                ac_marker.setPosition(new LatLng(entity.getLongitude(), entity.getLatitude()));

                ac_marker.setIcon(MarkerIcons.BLACK);
                ac_marker.setIconTintColor(Color.RED);

                ac_marker.setCaptionText(entity.getAccident());
                ac_marker.setCaptionTextSize(16);

                ac_marker.setMap(map);
            }
        }
    }

    //자전거 대여소 정보
    public void setUpRentalMap() {
        RentalParser parser = new RentalParser();
        ArrayList<RentalDTO> rentalDTO = new ArrayList<RentalDTO>();
        try {
            rentalDTO = parser.apiParserSearch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<rentalDTO.size(); i++){
            for(RentalDTO entity : rentalDTO) {
                Marker re_marker = new Marker();
                LocationOverlay locationOverlay = map.getLocationOverlay();

                re_marker.setPosition(new LatLng(entity.getLongitude(), entity.getLatitude()));

                re_marker.setIcon(MarkerIcons.BLACK);
                re_marker.setIconTintColor(Color.BLUE);

                re_marker.setCaptionText(entity.getStationName());
                re_marker.setCaptionTextSize(16);

                re_marker.setMap(map);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                map.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

}
