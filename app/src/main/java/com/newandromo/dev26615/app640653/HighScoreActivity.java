// File: HighScoreActivity.java
package com.newandromo.dev26615.app640653;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {
    private LinearLayout scoresContainer;
    private HighScoreManager scoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scoresContainer = findViewById(R.id.scoresContainer);
        Button backBtn = findViewById(R.id.btnBack);

        scoreManager = new HighScoreManager(this);
        displayHighScores();

        backBtn.setOnClickListener(v -> finish());
    }

    private void displayHighScores() {
        scoresContainer.removeAllViews();
        List<HighScoreManager.ScoreEntry> scores = scoreManager.getTopScores(10);

        if (scores.isEmpty()) {
            TextView noScores = new TextView(this);
            noScores.setText("No high scores yet!\nStart playing to set records!");
            noScores.setTextSize(20);
            noScores.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            noScores.setPadding(20, 40, 20, 20);
            scoresContainer.addView(noScores);
            return;
        }

        for (int i = 0; i < scores.size(); i++) {
            HighScoreManager.ScoreEntry entry = scores.get(i);

            LinearLayout scoreRow = new LinearLayout(this);
            scoreRow.setOrientation(LinearLayout.HORIZONTAL);
            scoreRow.setPadding(30, 20, 30, 20);

            TextView rank = new TextView(this);
            rank.setText("#" + (i + 1));
            rank.setTextSize(24);
            rank.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            rank.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            TextView score = new TextView(this);
            score.setText(String.valueOf(entry.score));
            score.setTextSize(24);
            score.setTextColor(getResources().getColor(android.R.color.black));
            score.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 2));

            TextView date = new TextView(this);
            date.setText(entry.date);
            date.setTextSize(18);
            date.setTextColor(getResources().getColor(android.R.color.darker_gray));
            date.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 2));

            scoreRow.addView(rank);
            scoreRow.addView(score);
            scoreRow.addView(date);

            scoresContainer.addView(scoreRow);

            // Add divider
            if (i < scores.size() - 1) {
                android.view.View divider = new android.view.View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2));
                divider.setBackgroundColor(getResources().getColor(
                        android.R.color.darker_gray));
                scoresContainer.addView(divider);
            }
        }
    }
}