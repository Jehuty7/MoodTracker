package com.jehuty.moodtracker.controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.jehuty.moodtracker.Utils.Constants;
import com.jehuty.moodtracker.model.MoodHistory;
import com.jehuty.moodtracker.model.MoodUI;
import com.jehuty.moodtracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private ImageButton mCommentaryButton;
    private ImageButton mHistoryButton;
    private ImageView mSmiley;
    private RelativeLayout mBackgroundLayout;
    private GestureDetector mGestureDetector;
    private SharedPreferences mPreferences;
    ArrayList<MoodUI> mListMoodUI = new ArrayList<>();
    ArrayList<MoodHistory> history = new ArrayList<>();
    private MoodHistory mCurrentMood;


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

        mPreferences = getPreferences(MODE_PRIVATE);

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

        for (MoodHistory mood : history) {
            System.out.println(" ---> onResume: " + mood);
        }
        System.out.println(mPreferences.getInt(Constants.PREF_KEY_POSITION, mPosition));

    }

    @Override
    protected void onPause() {
        super.onPause();

        long now = System.currentTimeMillis();
        mCurrentMood.setDate(now);
        mCurrentMood.setPosition(mPosition);

        if (history.size() > 0) {
            compareCalendar();
        } else {
            cyclingHistoryMoods();
        }


        //Gson gson = new Gson();
        //String historyJson = gson.toJson(history);
        //System.out.println(historyJson);
        //mPreferences.edit().putInt(Constants.PREF_KEY_POSITION, mPosition).putString(Constants.PREF_KEY_HISTORY,historyJson).apply();
        //System.out.println(mPreferences.getString(moodToJson,moodToJson));

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
        history.add(new MoodHistory(mCurrentMood));

        if (history.size() >= Constants.MAX_HISTORY_MOODS) {
            history.remove(0);
        }
    }

    public void compareCalendar() {

        int lastPos = history.size() - 1;
        MoodHistory lastMood = history.get(lastPos);

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(mCurrentMood.getDate());
        calendar2.setTimeInMillis(lastMood.getDate());

        int currentMoodDay = calendar1.get(Calendar.DAY_OF_YEAR);
        int lastMoodDay = calendar2.get(Calendar.DAY_OF_YEAR);
        int currentMoodYear = calendar1.get(Calendar.YEAR);
        int lastMoodYear = calendar2.get(Calendar.YEAR);

        System.out.println(currentMoodDay);
        System.out.println(lastMoodDay);

        if (currentMoodYear > lastMoodYear) {
            cyclingHistoryMoods();
        } else {
            if (currentMoodDay > lastMoodDay) {
                cyclingHistoryMoods();
            } else if (currentMoodDay == lastMoodDay) {
                history.remove(lastPos);
                cyclingHistoryMoods();
            }

        }
    }

}



