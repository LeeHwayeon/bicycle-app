package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class NewSearchActivity extends AppCompatActivity {

    private static String TAG = "myapplication_MainActivity";

    private TextView mTextViewResult;
    String mJsonString;

    // php 서버로 부터 받아온 사고 데이터를 저장할 리스트
    ArrayList<Double> stationName_list;


    ListView mlistView;

    private static final String TAG_JSON="row";
    private static final String TAG_stationName = "stationName";
    ArrayList<HashMap<String, String>> mArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_search);

        GetData task = new GetData();
        task.execute("http://openapi.seoul.go.kr:8088/4f4867556d636f6e37304375746e63/json/bikeList/1/200/");
        stationName_list = new ArrayList<>();

        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        mlistView = (ListView) findViewById(R.id.listView_main_list);
        mTextViewResult.setVisibility(View.INVISIBLE);
        mArrayList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(NewSearchActivity.this,
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

    private void showResult(){

        //데이터를 담아놓을 리스트를 초기화한다.
        stationName_list.clear();

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                String stationName = item.getString(TAG_stationName);

                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(TAG_stationName, stationName);

                mArrayList.add(hashMap);
            }
            ListAdapter adapter = new SimpleAdapter(
                    NewSearchActivity.this, mArrayList, R.layout.item_list,
                    new String[]{TAG_stationName},
                    new int[]{R.id.list_address}

            );

            mlistView.setAdapter(adapter);
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.favorite:
                Toast.makeText(this, "즐겨찾기", Toast.LENGTH_SHORT).show();
                Intent favorite = new Intent(NewSearchActivity.this, FavoriteActivity.class);
                startActivity(favorite);
                return true;
            case R.id.search:
                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(NewSearchActivity.this, NewSearchActivity.class);
                startActivity(search);
                return true;
            case R.id.weather:
                Toast.makeText(this, "날씨", Toast.LENGTH_SHORT).show();
                Intent weather= new Intent(NewSearchActivity.this, WeatherActivity.class);
                startActivity(weather);
                return true;
            case R.id.home:
                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
                Intent home= new Intent(NewSearchActivity.this, MainActivity.class);
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


