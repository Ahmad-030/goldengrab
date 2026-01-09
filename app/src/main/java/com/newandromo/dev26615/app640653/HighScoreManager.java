// File: HighScoreManager.java
package com.newandromo.dev26615.app640653;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HighScoreManager {
    private static final String TAG = "HighScoreManager";
    private static final String PREFS_NAME = "GoldenGrabHighScores";
    private static final String SCORES_KEY = "scores";
    private SharedPreferences prefs;

    public HighScoreManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void addScore(int score) {
        List<ScoreEntry> scores = getTopScores(100);

        String date = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                .format(new Date());

        ScoreEntry newEntry = new ScoreEntry(score, date);
        scores.add(newEntry);

        Log.d(TAG, "Adding new score: " + score + " on " + date);

        // Sort by score descending
        Collections.sort(scores, (a, b) -> b.score - a.score);

        // Keep only top 100
        if (scores.size() > 100) {
            scores = scores.subList(0, 100);
        }

        saveScores(scores);

        Log.d(TAG, "Score saved successfully. Total scores: " + scores.size());
    }

    public List<ScoreEntry> getTopScores(int limit) {
        String scoresData = prefs.getString(SCORES_KEY, "");
        List<ScoreEntry> scores = new ArrayList<>();

        Log.d(TAG, "Reading scores from SharedPreferences: " + scoresData);

        if (!scoresData.isEmpty()) {
            String[] entries = scoresData.split(";");
            for (String entry : entries) {
                String[] parts = entry.split(",", 2); // Split only on first comma
                if (parts.length == 2) {
                    try {
                        int score = Integer.parseInt(parts[0].trim());
                        String date = parts[1].trim();
                        scores.add(new ScoreEntry(score, date));
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing score entry: " + entry, e);
                    }
                }
            }
        }

        Collections.sort(scores, (a, b) -> b.score - a.score);

        if (scores.size() > limit) {
            return scores.subList(0, limit);
        }
        return scores;
    }

    private void saveScores(List<ScoreEntry> scores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            ScoreEntry entry = scores.get(i);
            sb.append(entry.score).append(",").append(entry.date);
            if (i < scores.size() - 1) {
                sb.append(";");
            }
        }
        String scoresString = sb.toString();
        prefs.edit().putString(SCORES_KEY, scoresString).apply();
        Log.d(TAG, "Saved scores to SharedPreferences: " + scoresString);
    }

    public void clearAllScores() {
        prefs.edit().remove(SCORES_KEY).apply();
        Log.d(TAG, "All scores cleared");
    }

    public static class ScoreEntry {
        public int score;
        public String date;

        public ScoreEntry(int score, String date) {
            this.score = score;
            this.date = date;
        }
    }
}