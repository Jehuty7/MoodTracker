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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jehuty.moodtracker.Utils.Constants;
import com.jehuty.moodtracker.Utils.Utils;
import com.jehuty.moodtracker.model.MoodHistory;
import com.jehuty.moodtracker.model.MoodUI;
import com.jehuty.moodtracker.R;

import java.util.ArrayList;
import java.util.Calendar;

/***
 * This is the main activity that is first run and showing the moods.
 * The user can change the moods by swiping up or down and a comment can be made if we click on
 * the "comment button" which is at the bottom left-hand of the screen
 */
public class MainActivity extends AppCompatActivity {


    private ImageView mSmiley; // Dynamic Smiley in center of main Activity change in terms of Moods
    private RelativeLayout mBackgroundLayout; // Dynamic background of main Activity change in terms of Moods
    private GestureDetector mGestureDetector; // This gestureDetector will detect swipes
    private SharedPreferences mPreferences; // Shared Preferences to save and load history and Moods
    private ArrayList<MoodUI> mListMoodUI = new ArrayList<>(); // This Arraylist contains objects MoodUI to display dynamically Smiley and backgrounds
    private ArrayList<MoodHistory> history = new ArrayList<>(); // This Arraylist stack Moods saved by User
    private MoodHistory mCurrentMood; // Current Mood displayed
    private MoodHistory standbyMood; // This is a Mood in standby contains all information of a mCurrentMood, to save it at 0:00am
    private int mPosition; // A position of Moods to dynamically display Moods correctly


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
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

    /**
     * We initialize the widgets here
     */
    private void initWidgets() {
        mSmiley = findViewById(R.id.activity_main_smiley);
        mBackgroundLayout = findViewById(R.id.backgroundLayout);
    }

    /**
     * We handle the buttons clicked. We get the reference of the view (which corresponds to a button)
     * thanks to its id
     *
     * @param view the button
     */
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

    /**
     * We instantiate all 5 Moods and add them to an Arraylist
     */
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

    /**
     * Here's a method to change dynamically smiley and background in terms of current position
     *
     * @param position of the current Mood
     */
    private void setScreenFromMood(int position) {

        mSmiley.setImageResource(mListMoodUI.get(position).getSmileyResource());
        mBackgroundLayout.setBackgroundColor(ContextCompat.getColor(this, mListMoodUI.get(position).getBackgroundColor()));
    }

    /**
     * Method to cycle Moods
     */
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

        history = Utils.getMoodsFromPrefs(this); // get the history from SharedPreferences
        String standbyMoodJson = mPreferences.getString(Constants.PREF_KEY_STANDBY_MOOD, null);

        Gson gson = new Gson();
        standbyMood = gson.fromJson(standbyMoodJson, MoodHistory.class);

        compareCalendar();
    }

    @Override
    protected void onPause() {
        super.onPause();

        long now = System.currentTimeMillis();
        mCurrentMood.setDate(now); // here, in onPause, we set the current time to mCurrentMood
        mCurrentMood.setPosition(mPosition);

        standbyMood = mCurrentMood;

        Gson gson = new Gson();
        String historyJson = gson.toJson(history);
        String standbyMoodJson = gson.toJson(standbyMood);

        mPreferences.edit().putInt(Constants.PREF_KEY_POSITION, mPosition).putString(Constants.PREF_KEY_HISTORY, historyJson)
                .putString(Constants.PREF_KEY_STANDBY_MOOD, standbyMoodJson).apply();
    }

    /**
     * Gesture
     */

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 20; // This will change de distance needed of the swipe for the gesture detector to trigger methods

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * This method will compare e1 and e2 to trigger a swipe down or up
         * @param e1 start point of the gesture
         * @param e2 end point of the gesture
         * @param velocityX not used here
         * @param velocityY not used here
         * @return
         */
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
     * Gesture Methods
     */

    /**
     * If a swipe top is triggered, Moods will be more sad
     */
    public void onSwipeTop() {
        --mPosition;
        changeMood();
        mCurrentMood.setPosition(mPosition);

    }

    /**
     * If a swipe bottom is triggered, Moods will be more happy
     */
    public void onSwipeBottom() {
        ++mPosition;
        changeMood();
        mCurrentMood.setPosition(mPosition);

    }

    /**
     * We create an AlertDialog to add a comment to mCurrentMood
     */
    private void addCommentary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_commentary, null);
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

    /**
     * We add standbyMood to history and reset standbyMood
     * We also write a condition to remmove the first saved Mood in history to add an other one and
     * get only 7 Moods saved in history
     */
    public void cyclingHistoryMoods() {
        history.add(new MoodHistory(standbyMood));
        standbyMood = null;
        mPreferences.edit().putString(Constants.PREF_KEY_STANDBY_MOOD, null).apply();
        mCurrentMood = new MoodHistory(mPosition);

        if (history.size() > Constants.MAX_HISTORY_MOODS) {
            history.remove(0);
        }
    }
/**
 * Here we compare standbyMood Date to the current Date to add standbyMood in history only if
 * standbyMood is older than the current Date
 */
    public void compareCalendar() {
        long now = System.currentTimeMillis();

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(now);
        if (standbyMood != null)
            calendar2.setTimeInMillis(standbyMood.getDate());

        int currentMoodDay = calendar1.get(Calendar.DAY_OF_YEAR);
        int standbyMoodDay = calendar2.get(Calendar.DAY_OF_YEAR);
        int currentMoodYear = calendar1.get(Calendar.YEAR);
        int standbyMoodYear = calendar2.get(Calendar.YEAR);

        if (currentMoodYear > standbyMoodYear || (currentMoodYear == standbyMoodYear && currentMoodDay > standbyMoodDay)) {
            cyclingHistoryMoods();
        }
    }

    /**
     * We share the mCurrentMood (Mood and comment) with a SharingIntent
     */
    public void shareCurrentMood() {
        String[] shareTxtArray = getResources().getStringArray(R.array.mood_share_txt);

        String mood = shareTxtArray[mCurrentMood.getPosition()];

        String shareBody = getString(R.string.share) + mood + " " + mCurrentMood.getComment();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.subject));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }
}





