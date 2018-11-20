package com.jehuty.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jehuty.moodtracker.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LinearLayout layout1 = findViewById(R.id.Layout1);
        layout1.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout1.getLayoutParams();
        params.weight = 2;
        layout1.setLayoutParams(params);
    }
}
