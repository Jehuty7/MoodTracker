package com.jehuty.moodtracker.model;

import java.io.Serializable;

/**
 * This class will contains all informations needed to save a Mood
 */

public class MoodHistory extends Object implements Serializable{

    private int position; // Position of the Mood
    private String comment; // Comment saved by User
    private long date; // Date saved

    public MoodHistory(int position, String comment, long date) {
        this.position = position;
        this.comment = comment;
        this.date = date;
    }

    public MoodHistory(int position, long date) {
        this(position, "", date);
    }

    public MoodHistory(int position) {
        this(position, 0);
    }

    public MoodHistory(MoodHistory moodHistory) {
        this(moodHistory.position, moodHistory.comment, moodHistory.date);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
