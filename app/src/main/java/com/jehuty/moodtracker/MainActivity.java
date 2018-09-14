package com.jehuty.moodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageButton mCommentaryButton;
    private ImageButton mHistoryButton;
    private ImageView mSmileyHappyDefault;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();

        //Display History screen when User click on History Button
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historyActivityIntent = new Intent (MainActivity.this, HistoryActivity.class);
                startActivity(historyActivityIntent);
            }
        });
    }


    private void initButtons(){

    mCommentaryButton = (ImageButton) findViewById(R.id.activity_main_button_commentary);
    mHistoryButton = (ImageButton) findViewById(R.id.activity_main_button_history);
    mSmileyHappyDefault = (ImageView) findViewById(R.id.activity_main_smiley_happy_default);
}
}