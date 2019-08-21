package com.example.ruru.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ruru.customview.activity.CircleActivity;
import com.example.ruru.customview.activity.ClockActivity;
import com.example.ruru.customview.activity.HorizontalSVActivity;
import com.example.ruru.customview.activity.PullRefreshLayoutActivity;
import com.example.ruru.customview.activity.SearchActivity;
import com.example.ruru.customview.activity.TextMoveActivity;
import com.example.ruru.customview.activity.ProgressActivity;
import com.example.ruru.customview.activity.WaveActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //注意：这里必须定义为public
    public void clock(View v) {
        Intent intent = new Intent(this, ClockActivity.class);
        startActivity(intent);
    }

    public void circle(View v) {
        Intent intent = new Intent(this, CircleActivity.class);
        startActivity(intent);
    }

    public void progress(View v) {
        Intent intent = new Intent(this, ProgressActivity.class);
        startActivity(intent);
    }

    public void myview(View v) {
        Intent intent = new Intent(this, TextMoveActivity.class);
        startActivity(intent);
    }

    public void searchedittext(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    public void pullrefreshlayout(View v) {
        Intent intent = new Intent(this, PullRefreshLayoutActivity.class);
        startActivity(intent);
    }

    public void horizontalscrollviewex(View v) {
        Intent intent = new Intent(this, HorizontalSVActivity.class);
        startActivity(intent);
    }

    public void waveview(View v) {
        Intent intent = new Intent(this, WaveActivity.class);
        startActivity(intent);
    }
}
