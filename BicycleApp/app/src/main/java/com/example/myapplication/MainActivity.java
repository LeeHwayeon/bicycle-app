package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv_id, tv_pass, tv_gender, tv_age, tv_gu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}