package com.leef.customview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    CircleLoadingView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (CircleLoadingView) findViewById(R.id.progress);
        progress.setCenterBitmap(R.mipmap.ic_launcher);
        progress.setArcColor(Color.GREEN);
        progress.setArcWidth(3);
    }

}
