package com.example.ruru.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ruru.customview.R;


/**
 * Activity负责活动
 * View只负责：测量，布局，绘制
 */
public class ClockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
    }
}
