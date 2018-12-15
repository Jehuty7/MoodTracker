package com.jehuty.moodtracker.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jehuty.moodtracker.R;
import com.jehuty.moodtracker.Utils.Constants;
import com.jehuty.moodtracker.Utils.Utils;
import com.jehuty.moodtracker.model.MoodHistory;
import com.jehuty.moodtracker.model.MoodUI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    //ArrayList<MoodHistory> history = MainActivity.getHistory()

    /*public SharedPreferences mPreferences;


    String historyJson = getPreferences(MODE_PRIVATE).getString(Constants.PREF_KEY_HISTORY,null);

    Type listType = new TypeToken<ArrayList<MoodHistory>>(){}.getType();
    List<MoodHistory> history = new Gson().fromJson(historyJson, listType);*/
    ArrayList<MoodHistory> history;

    private ImageButton commentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        /*String historyJson = getPreferences(MODE_PRIVATE).getString(Constants.PREF_KEY_HISTORY,null);
        Type listType = new TypeToken<ArrayList<MoodHistory>>(){}.getType();
        List<MoodHistory> history = new Gson().fromJson(historyJson, listType);*/

        history = Utils.getMoodsFromPrefs(this);
        System.out.println("HistoryActivity: "+history);

        commentButton = findViewById(R.id.commentButton);

        int historySize = history.size();

        displayHistory();

       // int diffDay = 10;
       // if (diffDay == 1) System.out.println("hier");
       // if (diffDay == 2 ) System.out.println("Avant-hier");
       // if (diffDay >= 3) System.out.println(getString(R.string.x_days_ago, diffDay));

    }

    @Override
    protected void onResume() {
        super.onResume();


        System.out.println("history resume" + history);
    }

    public void displayHistory() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        LayoutInflater inflater = LayoutInflater.from(this);

        int[] historyColors = {R.color.faded_red, R.color.warm_grey, R.color.cornflower_blue_65,
                R.color.light_sage, R.color.banana_yellow};
        float[] paddingWeight = {(float) 0.25, (float) 0.45, (float) 1.2, (float) 2.5, 9};

        for (int i = 0; i < Constants.MAX_HISTORY_MOODS; i++) {
            View view = inflater.inflate(R.layout.history_item, null);
            View paddingView = view.findViewById(R.id.paddingView);
            ImageButton commentBtn = view.findViewById(R.id.commentButton);

            if (history.size() > i) {
                final MoodHistory historyMood = history.get(i);
                int historyMoodPosition = historyMood.getPosition();

                view.setBackgroundColor(getResources().getColor(historyColors[historyMoodPosition]));

                TextView txtView = view.findViewById(R.id.dayTxt);
                txtView.setText("Item numéro " + (i + 1));

                paddingView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, paddingWeight[historyMoodPosition]));

                if (historyMood.getComment().equals("")) {
                    commentBtn.setVisibility(View.INVISIBLE);
                } else commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(HistoryActivity.this, historyMood.getComment(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                view.setVisibility(View.INVISIBLE);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            linearLayout.addView(view, params);
        }

    }


    public void displayDayString() {

        long now = System.currentTimeMillis();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(now);

    }


}








