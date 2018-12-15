package com.jehuty.moodtracker.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jehuty.moodtracker.model.MoodHistory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Utils {
   public static ArrayList<MoodHistory> getMoodsFromPrefs(Context context) {
       SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

       String historyJson =  prefs.getString(Constants.PREF_KEY_HISTORY, null);

       if (historyJson != null) {
           Type listType = new TypeToken<ArrayList<MoodHistory>>() {
           }.getType();
           return new Gson().fromJson(historyJson, listType);
       }

       return new ArrayList<>();
   }
}
