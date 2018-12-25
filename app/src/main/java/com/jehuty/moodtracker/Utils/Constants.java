package com.jehuty.moodtracker.Utils;

import com.jehuty.moodtracker.R;

public class Constants {

    public static final int DEFAULT_MOOD_POSITION = 3; // Default MoodUI when app starts
    public static final int MAX_HISTORY_MOODS = 7; // Maximum size of Arraylist contains Moods history
    public static final String PREF_KEY_POSITION = "PREF_KEY_POSITION"; // Default position Key for SharedPreferences
    public static final String PREF_KEY_HISTORY = "PREF_KEY_HISTORY"; // Default Arraylist history Key for SharedPreferences
    public static final String PREF_KEY_STANDBY_MOOD = "PREF_KEY_STANBY_MOOD"; // Default StandbyMood Key for SharedPreferences
    public static final int[] historyColors = {R.color.faded_red, R.color.warm_grey, R.color.cornflower_blue_65,
            R.color.light_sage, R.color.banana_yellow}; // all colors used in the History Activity to display history bars
}
