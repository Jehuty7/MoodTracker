package com.jehuty.moodtracker.model;

/**
 * This class will contains all information for display Moods in the Main Activity
 */

public class MoodUI {

    int smileyResource; // The smiley of the Mood
    int backgroundColor; // The background color of the Mood
    int position; // The position of the Mood

    public MoodUI(int smileyResource, int backgroundColor, int position) {
        this.smileyResource = smileyResource;
        this.backgroundColor = backgroundColor;
        this.position = position;

    }

    public int getSmileyResource() {
        return smileyResource;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getPosition() {
        return position;
    }


}

