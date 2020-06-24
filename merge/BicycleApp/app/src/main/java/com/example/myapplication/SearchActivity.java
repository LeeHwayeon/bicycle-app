//package com.example.myapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.EditText;
//import android.widget.ListView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class SearchActivity extends AppCompatActivity {
//
//    private List<String> list;          // 데이터를 넣은 리스트변수
//    private ListView listView;          // 검색을 보여줄 리스트변수
//    private EditText editSearch;        // 검색어를 입력할 Input 창
//    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
//    private ArrayList<String> arraylist;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setBackgroundColor(Color.BLACK);
//
//        editSearch = (EditText) findViewById(R.id.editSearch);
//        listView = (ListView) findViewById(R.id.listView);
//
//        // 리스트를 생성한다.
//        list = new ArrayList<String>();
//
//        // 검색에 사용할 데이터을 미리 저장한다.
//        settingList();
//
//        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
//        arraylist = new ArrayList<String>();
//        arraylist.addAll(list);
//
//        // 리스트에 연동될 아답터를 생성한다.
//        adapter = new SearchAdapter(list, this);
//
//        // 리스트뷰에 아답터를 연결한다.
//        listView.setAdapter(adapter);
//
//        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
//        editSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // input창에 문자를 입력할때마다 호출된다.
//                // search 메소드를 호출한다.
//                String text = editSearch.getText().toString();
//                search(text);
//            }
//        });
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.favorite:
//                Toast.makeText(this, "즐겨찾기", Toast.LENGTH_SHORT).show();
//                Intent favorite = new Intent(SearchActivity.this, FavoriteActivity.class);
//                startActivity(favorite);
//                return true;
//            case R.id.search:
//                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
//                Intent search = new Intent(SearchActivity.this, SearchActivity.class);
//                startActivity(search);
//                return true;
//            case R.id.weather:
//                Toast.makeText(this, "날씨", Toast.LENGTH_SHORT).show();
//                Intent weather= new Intent(SearchActivity.this, WeatherActivity.class);
//                startActivity(weather);
//                return true;
//            case R.id.home:
//                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
//                Intent home= new Intent(SearchActivity.this, MainActivity.class);
//                startActivity(home);
//                return true;
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    // 검색을 수행하는 메소드
//    public void search(String charText) {
//
//        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
//        list.clear();
//
//        // 문자 입력이 없을때는 모든 데이터를 보여준다.
//        if (charText.length() == 0) {
//            list.addAll(arraylist);
//        }
//        // 문자 입력을 할때..
//        else
//        {
//            // 리스트의 모든 데이터를 검색한다.
//            for(int i = 0;i < arraylist.size(); i++)
//            {
//                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
//                if (arraylist.get(i).toLowerCase().contains(charText))
//                {
//                    // 검색된 데이터를 리스트에 추가한다.
//                    list.add(arraylist.get(i));
//                }
//            }
//        }
//        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
//        adapter.notifyDataSetChanged();
//    }
//
//    // 검색에 사용될 데이터를 리스트에 추가한다.
//    private void settingList(){
//        list.add("서울숲관리사무소자전거대여소");
//        list.add("뚝섬유원지역1번출구앞자전거대여소");
//        list.add("여의도역4번출구옆자전거대여소");
//        list.add("합정역5번출구앞자전거대여소");
//        list.add("시청역1번출구뒤자전거대여소");
//        list.add("서초4동주민센터자전거대여소");
//        list.add("노들역1번출구자전대여소");
//        list.add("으뜸공원자전거대여소");
//        list.add("양서중학교옆자전거대여소");
//        list.add("양카라공원앞자전거대여소");
//        list.add("합정역5번출구앞자전거대여소");
//        list.add("베르가모앞자전거대여소");
//        list.add("마포어린이공원자전거대여");
//    }
//}
//
