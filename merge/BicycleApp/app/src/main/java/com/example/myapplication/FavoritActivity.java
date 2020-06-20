package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class FavoritActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.BLACK);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.favorite:
                Toast.makeText(this, "즐겨찾기", Toast.LENGTH_SHORT).show();
                Intent favorite = new Intent(FavoritActivity.this, FavoritActivity.class);
                startActivity(favorite);
                return true;
            case R.id.home:
                Toast.makeText(this, "홈", Toast.LENGTH_SHORT).show();
                Intent home= new Intent(FavoritActivity.this, MainActivity.class);
                startActivity(home);
                return true;
            case R.id.search:
                Toast.makeText(this, "검색", Toast.LENGTH_SHORT).show();
                Intent search = new Intent(FavoritActivity.this, SearchActivity.class);
                startActivity(search);
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
