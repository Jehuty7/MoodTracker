package com.jehuty.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jehuty.moodtracker.Utils.Constants;
import com.jehuty.moodtracker.Utils.Utils;
import com.jehuty.moodtracker.model.MoodHistory;
import com.jehuty.moodtracker.model.MoodUI;
import com.jehuty.moodtracker.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ImageButton mCommentaryButton;
    private ImageButton mHistoryButton;
    private ImageView mSmiley;
    private RelativeLayout mBackgroundLayout;
    private GestureDetector mGestureDetector;
    private SharedPreferences mPreferences;
    private ArrayList<MoodUI> mListMoodUI = new ArrayList<>();
    private ArrayList<MoodHistory> history = new ArrayList<>();
    private MoodHistory mCurrentMood;
    private MoodHistory standbyMood;
    private String mMood;
    private String mShare;
    private int mPosition;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initButtons();
        initMoods();
        mPosition = Constants.DEFAULT_MOOD_POSITION;
        mCurrentMood = new MoodHistory(mPosition);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mGestureDetector = new GestureDetector(this, new GestureListener());
        mBackgroundLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });
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
            addCommentary();
            }

        if (view.getId() == R.id.activity_main_button_share) {

            shareCurrentMood();
        }
    }

    private void initMoods() {
        MoodUI superHappyMoodUI = new MoodUI(R.drawable.smiley_super_happy, R.color.banana_yellow, 4);
        MoodUI happyMoodUI = new MoodUI(R.drawable.smiley_happy, R.color.light_sage, 3);
        MoodUI normalMoodUI = new MoodUI(R.drawable.smiley_normal, R.color.cornflower_blue_65, 2);
        MoodUI disappointedMoodUI = new MoodUI(R.drawable.smiley_disappointed, R.color.warm_grey, 1);
        MoodUI sadMoodUI = new MoodUI(R.drawable.smiley_sad, R.color.faded_red, 0);

        mListMoodUI.add(sadMoodUI);
        mListMoodUI.add(disappointedMoodUI);
        mListMoodUI.add(normalMoodUI);
        mListMoodUI.add(happyMoodUI);
        mListMoodUI.add(superHappyMoodUI);
    }

    private void setScreenFromMood(int position) {

        mSmiley.setImageResource(mListMoodUI.get(position).getSmileyResource());
        mBackgroundLayout.setBackgroundColor(ContextCompat.getColor(this, mListMoodUI.get(position).getBackgroundColor()));
    }

    private void changeMood() {

        if (mPosition >= mListMoodUI.size()) {
            mPosition = 0;
        } else if (mPosition < 0) {
            mPosition = mListMoodUI.size() - 1;
        }
        setScreenFromMood(mPosition);
    }

    /**
     * Life Cycle
     */
    @Override
    protected void onResume() {
        super.onResume();


        history = Utils.getMoodsFromPrefs(this);

        String standbyMoodJson = mPreferences.getString(Constants.PREF_KEY_STANBY_MOOD, null);

        Gson gson = new Gson();

        MoodHistory standbyMood = gson.fromJson(standbyMoodJson, MoodHistory.class);


        compareCalendar();

        System.out.println("MainActivity onResume standbymood:" + standbyMood);
        System.out.println("MainActivvity history onResume: " + history);

    }

    @Override
    protected void onPause() {
        super.onPause();

        long now = System.currentTimeMillis();
        mCurrentMood.setDate(now);
        mCurrentMood.setPosition(mPosition);

        standbyMood = mCurrentMood;

        Gson gson = new Gson();

        String historyJson = gson.toJson(history);
        String standbyMoodJson = gson.toJson(standbyMood);

        mPreferences.edit().putInt(Constants.PREF_KEY_POSITION, mPosition).putString(Constants.PREF_KEY_HISTORY, historyJson)
                .putString(Constants.PREF_KEY_STANBY_MOOD, standbyMoodJson).apply();

        System.out.println("onPause standbymood :" + standbyMood);
        //if (history.size() > 0) {
        //    compareCalendar();
        //} else {
        //    cyclingHistoryMoods();
        //}


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Gesture
     */

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

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

    /**
     * Methods
     */

    public void onSwipeTop() {
        --mPosition;
        changeMood();
        mCurrentMood.setPosition(mPosition);

    }

    public void onSwipeBottom() {
        ++mPosition;
        changeMood();
        mCurrentMood.setPosition(mPosition);

    }

    private void addCommentary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_commentary, null);
        final EditText editText = view.findViewById(R.id.commentEditTxt);

        builder.setView(view)
                .setTitle(R.string.alert_dialog_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = editText.getText().toString();
                        mCurrentMood.setComment(comment);
                    }

                })
                .create()
                .show();
    }


    public void cyclingHistoryMoods() {
        history.add(new MoodHistory(standbyMood));

        if (history.size() > Constants.MAX_HISTORY_MOODS) {
            history.remove(0);
        }
    }

    public void compareCalendar() {
        long now = System.currentTimeMillis();

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(now);
        if (standbyMood == null) {
            //nothing
        } else calendar2.setTimeInMillis(standbyMood.getDate());

        int currentMoodDay = calendar1.get(Calendar.DAY_OF_YEAR);
        int standbyMoodDay = calendar2.get(Calendar.DAY_OF_YEAR);
        int currentMoodYear = calendar1.get(Calendar.YEAR);
        int standbyMoodYear = calendar2.get(Calendar.YEAR);


        if (currentMoodYear > standbyMoodYear) {
            cyclingHistoryMoods();
        } else if (currentMoodDay > standbyMoodDay) {
            cyclingHistoryMoods();
        }
    }


    public void shareCurrentMood(){


        if (mCurrentMood.getPosition() == 0) {
            mMood = getResources().getString(R.string.sad_share);
        } else if (mCurrentMood.getPosition() == 1){
            mMood = getResources().getString(R.string.disapointed_share);
        } else if (mCurrentMood.getPosition() == 2){
            mMood = getResources().getString(R.string.normal_share);
        } else if (mCurrentMood.getPosition() == 3){
            mMood = getResources().getString(R.string.happy_share);
        } else if (mCurrentMood.getPosition() == 4){
            mMood = getResources().getString(R.string.super_happy_share);
        }


        String shareBody = getResources().getString(R.string.share) + mMood + " " + mCurrentMood.getComment();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }
}





