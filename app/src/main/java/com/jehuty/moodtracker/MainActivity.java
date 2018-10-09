package com.jehuty.moodtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ImageButton mCommentaryButton;
    private ImageButton mHistoryButton;
    private ImageView mSmiley;
    private RelativeLayout mBackgroundLayout;
    private  GestureDetector mGestureDetector;
    private SharedPreferences mPreferences;
    ArrayList<Mood> listMood = new ArrayList<>();
    private int mPosition;

    public static final String PREF_KEY_POSITION = "PREF_KEY_POSITION";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initMoods();
        mPosition = 3;

        mPreferences = getPreferences(MODE_PRIVATE);

        mGestureDetector = new GestureDetector(this, new GestureListener());
        mBackgroundLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                System.out.println("onTouch");
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });

        System.out.println("MainActivity::onCreate()");

    }

    private void initButtons() {
        mCommentaryButton = findViewById(R.id.activity_main_button_commentary);
        mHistoryButton = findViewById(R.id.activity_main_button_history);
        mSmiley = findViewById(R.id.activity_main_smiley);
        mBackgroundLayout = findViewById(R.id.backgroundLayout);
    }

    public void buttonClicked(View view) {
        if (view.getId() == R.id.activity_main_button_history) {
            Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyActivityIntent);
        }

        if (view.getId() == R.id.activity_main_button_commentary) {
            ++mPosition;
            if (mPosition >= listMood.size()) {
                mPosition = 0;
            }
            setScreenFromMood(mPosition);
        }
    }


    private void initMoods() {
        Mood superHappyMood = new Mood(R.drawable.smiley_super_happy, R.color.banana_yellow, 4);
        Mood happyMood = new Mood(R.drawable.smiley_happy, R.color.light_sage, 3);
        Mood normalMood = new Mood(R.drawable.smiley_normal, R.color.cornflower_blue_65, 2);
        Mood disappointedMood = new Mood(R.drawable.smiley_disappointed, R.color.warm_grey, 1);
        Mood sadMood = new Mood(R.drawable.smiley_sad, R.color.faded_red, 0);

        listMood.add(sadMood);
        listMood.add(disappointedMood);
        listMood.add(normalMood);
        listMood.add(happyMood);
        listMood.add(superHappyMood);
    }

    private void setScreenFromMood(int position) {
                mSmiley.setImageResource(listMood.get(position).getSmileyResource());
        mBackgroundLayout.setBackgroundColor(ContextCompat.getColor(this, listMood.get(position).getBackgroundColor()));
    }

    private void changeMood() {

        if (mPosition >= listMood.size()) {
            mPosition = 0;
        } else if (mPosition < 0) {
            mPosition = listMood.size() - 1;
        }

        setScreenFromMood(mPosition);
    }
    /**
     Life Cycle
     */
    @Override
    protected void onResume(){
        super.onResume();

        System.out.println("MainActivity::onResume()");
        System.out.println(mPreferences.getInt(PREF_KEY_POSITION, mPosition));
    }

    @Override
    protected void onPause(){
        super.onPause();

        System.out.println("MainActivity::onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();

        System.out.println("MainActivity::onStop()");

        mPreferences.edit().putInt(PREF_KEY_POSITION, mPosition).apply();
        System.out.println(mPreferences.getInt(PREF_KEY_POSITION, mPosition));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        System.out.println("MainActivity::onDestroy()");
    }

    /**
    Gesture
     */

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20;

        @Override
        public boolean onDown (MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("onFling");
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                if (Math.abs(diffY) > SWIPE_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeTop() {
        System.out.println("on swipe top");
        --mPosition;
        changeMood();
    }

    public void onSwipeBottom() {
        System.out.println("on swipe bottom");
        ++mPosition;
        changeMood();
    }
}

