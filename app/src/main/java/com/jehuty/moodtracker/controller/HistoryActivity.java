package com.jehuty.moodtracker.controller;


import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jehuty.moodtracker.R;
import com.jehuty.moodtracker.Utils.Constants;
import com.jehuty.moodtracker.Utils.Utils;
import com.jehuty.moodtracker.model.MoodHistory;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<MoodHistory> history; // Arraylist contains all Moods saved by User
                                            // We get this history by Shared Preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        history = Utils.getMoodsFromPrefs(this); // Method made in Utils class to get history from Shared Preferences

        displayHistory();
    }

    /**
     * This method a little bit too long is for displaying history in the history activity
     * This will create bars dynamically in terms of the size of the Arraylist History,
     * will make a Toast message if a comment is save in a Mood
     */
    public void displayHistory() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        float[] paddingWeight = {0.4f, 0.78f, 1.55f, 4f, 1000f}; // This list contains all weights for padding views

        for (int i = 0; i < Constants.MAX_HISTORY_MOODS; i++) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.history_item, null);
            View paddingView = view.findViewById(R.id.paddingView); // This simple view will padding dynamically all bars in terms of mood saved
                                                                    // More sad is the mood, more this view will make an important padding
            ImageButton commentBtn = view.findViewById(R.id.commentButton); // This button will appears if a comment is saved by User in a Mood

            if (history.size() > i) {
                final MoodHistory historyMood = history.get(i); // We get the Moods in each cycle of "i"
                int historyMoodPosition = historyMood.getPosition(); // We get the position of each Moods to display bars in terms of this position

                /**
                 * Here, we compare dates of each Moods to the current date, to display in a string,
                 * the differences between the date of a mood and the current day
                 */
                long moodDate = history.get(i).getDate();
                long now = System.currentTimeMillis();

                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar1.setTimeInMillis(now);
                calendar2.setTimeInMillis(moodDate);

                int currentDay = calendar1.get(Calendar.DAY_OF_YEAR);
                int moodDay = calendar2.get(Calendar.DAY_OF_YEAR);
                int diffDays = currentDay - moodDay;

                TextView txtView = view.findViewById(R.id.dayTxt);
                updateHistoryTxt(txtView, diffDays); // Here, we update the Textview in terms of day difference

                /**
                 * Here, we display the correct color and size of each bars in terms of moods saved
                 */
                view.setBackgroundColor(getResources().getColor(Constants.historyColors[historyMoodPosition]));
                paddingView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, paddingWeight[historyMoodPosition]));

                /**
                 * Here, we simply display or not the comment button in terms of there's a comment or not
                 */
                if (historyMood.getComment().equals("")) {
                    commentBtn.setVisibility(View.INVISIBLE);
                } else commentBtn.setOnClickListener(new View.OnClickListener() {

                    /**
                     * Here, we create a Toast message on the comment button to display the comment saved by User
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(HistoryActivity.this, historyMood.getComment(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                view.setVisibility(View.INVISIBLE);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            linearLayout.addView(view, params);
        }
    }

    /**
     * This Method will display the difference of days between the current day and the Mood date
     * @param txtView Display the difference in days
     * @param diffDays contains the difference between the current day and the moods dates
     */
    private void updateHistoryTxt(TextView txtView, int diffDays) {
        String[] numbersArray = getResources().getStringArray(R.array.numbers);
        switch (diffDays) {
            case 1:
                txtView.setText(getString(R.string.yesterday));
                break;
            case 2:
                txtView.setText(getString(R.string.day_before_yesterday));
                break;
            case 3: case 4: case 5: case 6:
                txtView.setText(getString(R.string.x_days_ago, numbersArray[diffDays]));
                break;
            case 7:
                txtView.setText(getString(R.string.one_week_ago));
                break;
            default:
                txtView.setText(getString(R.string.more_than_a_week));
        }
    }
}








