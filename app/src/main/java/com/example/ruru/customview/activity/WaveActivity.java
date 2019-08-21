package com.example.ruru.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ruru.customview.R;
import com.example.ruru.customview.view.WaveView;

public class WaveActivity extends AppCompatActivity {

    private WaveView waveview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);

        waveview = findViewById(R.id.waveview);
        waveview.setOnWaveAnimationListener(new WaveView.OnWaveAnimationListener() {
            @Override
            public void OnWaveAnimation(float y) {

            }
        });
        waveview.setOnWaveAnimationListener(y -> {
        });
    }
}
