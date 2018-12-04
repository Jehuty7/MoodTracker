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
import com.jehuty.moodtracker.R;
import com.jehuty.moodtracker.Utils.Constants;
import com.jehuty.moodtracker.model.MoodHistory;
import com.jehuty.moodtracker.model.MoodUI;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<MoodHistory> history = MainActivity.getHistory();

    int historySize = history.size();

    private ImageButton commentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        commentButton = findViewById(R.id.commentButton);

        int historySize = history.size();

        displayHistory();

        }

        //LinearLayout layout1 = findViewById(R.id.Layout1);

       //LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout1.getLayoutParams();
       //params.weight = 2;
       //layout1.setLayoutParams(params);



    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("history resume" + history);
    }

    public void displayHistory() {

        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        //LinearLayout parentLayout = findViewById(R.id.parentLayout);


        LayoutInflater inflater = LayoutInflater.from(this);

        int[] historyColors = {R.color.faded_red,R.color.warm_grey,R.color.cornflower_blue_65,
                R.color.light_sage, R.color.banana_yellow} ;
        float[] paddingWeight = {10, 9, 8, 7, 6};

        for (int i = 0; i < Constants.MAX_HISTORY_MOODS; i++) {
            View view = inflater.inflate(R.layout.history_item, null);
            View paddingView = view.findViewById(R.id.paddingView);
            ImageButton commentBtn = view.findViewById(R.id.commentButton);

            if (historySize > i) {
                final MoodHistory historyMood = history.get(i);
                int historyMoodPosition = historyMood.getPosition();

                view.setBackgroundColor(getResources().getColor(historyColors[historyMoodPosition]));

                TextView txtView = view.findViewById(R.id.dayTxt);
                txtView.setText("Item numéro " + (i + 1));

                paddingView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, paddingWeight[historyMoodPosition]));

                if (historyMood.getComment().equals("")) {
                    commentBtn.setVisibility(View.GONE);
                }
                else commentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(HistoryActivity.this, historyMood.getComment(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
                view.setVisibility(View.INVISIBLE);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            linearLayout.addView(view, params);
            }

    }



   /* public void buttonClicked(View view){
        if (view.getId() == R.id.commentButton){
            Toast.makeText(this, "Commentaire", Toast.LENGTH_LONG).show();
        }
    }*/


}








