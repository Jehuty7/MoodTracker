package com.jehuty.moodtracker.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jehuty.moodtracker.R;
import com.jehuty.moodtracker.model.MoodHistory;

import java.util.ArrayList;


import static com.jehuty.moodtracker.controller.MainActivity.history;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<MoodHistory> history = MainActivity.getHistory();



    int historySize = history.size();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        displayHistory();

       //LinearLayout layout1 = findViewById(R.id.Layout1);
       //layout1.setVisibility(View.VISIBLE);

       //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout1.getLayoutParams();
       //params.weight = 2;
       //layout1.setLayoutParams(params);




    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("history resume" + history);
    }




   public void displayHistory(){
        LinearLayout layout1 = findViewById(R.id.Layout1);
        LinearLayout layout2 = findViewById(R.id.Layout2);
        LinearLayout layout3 = findViewById(R.id.Layout3);
        LinearLayout layout4 = findViewById(R.id.Layout4);
        LinearLayout layout5 = findViewById(R.id.Layout5);
        LinearLayout layout6 = findViewById(R.id.Layout6);
        switch (historySize) { case 1:
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                layout3.setVisibility(View.INVISIBLE);
                layout4.setVisibility(View.INVISIBLE);
                layout5.setVisibility(View.INVISIBLE);
                layout6.setVisibility(View.INVISIBLE);
                break; case 2:
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                layout3.setVisibility(View.INVISIBLE);
                layout4.setVisibility(View.INVISIBLE);
                layout5.setVisibility(View.INVISIBLE);
                break; case 3:
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                layout3.setVisibility(View.INVISIBLE);
                layout4.setVisibility(View.INVISIBLE);
                break; case 4:
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                layout3.setVisibility(View.INVISIBLE);
                break; case 5:
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                break; case 6:
                layout1.setVisibility(View.INVISIBLE);
                break; }
                }

}

