package com.example.ruru.customview.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.ruru.customview.R;
import com.example.ruru.customview.view.PullRefreshLayout;

public class PullRefreshLayoutActivity extends AppCompatActivity {

    /*private SwipeRefreshLayout srl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    srl.setRefreshing(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void initViewSrl() {
        srl = findViewById(R.id.srl);
        //延时3秒后关闭，不能用休眠，因为休眠就不能刷新拉。
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);
                    }
                }, 3000);

            }
        });
    }*/

    private PullRefreshLayout prl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_refresh_layout);

//        initViewSrl();
        initViewPrl();
    }

    private void initViewPrl() {
        prl = findViewById(R.id.prl);
        prl.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //view里提供了postDelayed就不要用handler
                prl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prl.setRefreshing(false);
                    }
                }, 4000);
            }
        });
    }
}
