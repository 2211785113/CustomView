package com.example.ruru.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ruru.customview.R;
import com.example.ruru.customview.view.CircleView;

public class CircleActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_circle);

    CircleView circleView = findViewById(R.id.circleView);
    circleView.setImageResource(R.mipmap.ic_launcher);
  }
}
