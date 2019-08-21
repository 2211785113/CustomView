package com.example.ruru.customview.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.ruru.customview.MainActivity;
import com.example.ruru.customview.R;
import com.example.ruru.customview.view.RoundProgressBar;

public class ProgressActivity extends AppCompatActivity implements View.OnClickListener {

    //仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
    private RoundProgressBar mRoundProgressBar;
    private static final String TAG = "welcome";
    private int mProgress = 0;

    //主线程接收到子线程发送过来的消息
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRoundProgressBar.setProgress(mProgress);
            Log.d(TAG, mRoundProgressBar.getProgress() + "");
            Log.d(TAG, mRoundProgressBar.getMax() + "");
            if (mProgress >= 100) {
                Intent it = new Intent(ProgressActivity.this, MainActivity.class);
                startActivity(it);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mRoundProgressBar = (RoundProgressBar) findViewById(R.id.progressBar);
        //进入欢迎界面后圆形滚动条开始滚动
        initData();
        //当点击滚动条上文字跳过的时候实现页面直接跳转
        mRoundProgressBar.setOnClickListener(this);
    }

    private void initData() {
        //开启子线程开始耗时操作
        new Thread() {
            @Override
            public void run() {
                while (mProgress < 100) {
                    mProgress = mProgress + 2;
                    //子线程给主线程发送消息更新UI
                    handler.sendEmptyMessage(0);
                    SystemClock.sleep(500);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.progressBar: {
                Intent it = new Intent(ProgressActivity.this, MainActivity.class);
                startActivity(it);
            }
        }
    }
}
