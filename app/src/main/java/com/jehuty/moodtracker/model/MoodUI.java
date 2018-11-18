package com.jehuty.moodtracker.model;



public class MoodUI {

    int smileyResource;
    int backgroundColor;
    int position;




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

