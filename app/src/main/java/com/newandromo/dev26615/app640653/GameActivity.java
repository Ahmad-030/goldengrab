// File: GameActivity.java
package com.newandromo.dev26615.app640653;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    private GameView gameView;
    private TextView scoreText;
    private int currentScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreText = findViewById(R.id.scoreText);
        gameView = findViewById(R.id.gameView);

        gameView.setGameActivity(this);
        updateScore(0);
    }

    public void updateScore(int score) {
        currentScore = score;
        runOnUiThread(() -> scoreText.setText("Score: " + score));
    }

    public void showGameOverDialog() {
        runOnUiThread(() -> {
            gameView.pauseGame();

            // Save high score IMMEDIATELY when game ends
            HighScoreManager manager = new HighScoreManager(this);
            Log.d(TAG, "Game Over - Saving score: " + currentScore);
            manager.addScore(currentScore);

            // Verify it was saved
            int totalScores = manager.getTopScores(100).size();
            Log.d(TAG, "Total scores now saved: " + totalScores);

            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_game_over);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            TextView finalScore = dialog.findViewById(R.id.finalScoreText);
            Button restartBtn = dialog.findViewById(R.id.btnRestart);
            Button menuBtn = dialog.findViewById(R.id.btnMenu);
            Button viewScoresBtn = dialog.findViewById(R.id.btnViewScores);

            finalScore.setText("Your Score: " + currentScore);

            restartBtn.setOnClickListener(v -> {
                dialog.dismiss();
                restartGame();
            });

            menuBtn.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });

            viewScoresBtn.setOnClickListener(v -> {
                dialog.dismiss();
                Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
                startActivity(intent);
                finish();
            });

            dialog.show();
        });
    }

    private void restartGame() {
        gameView.resetGame();
        updateScore(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resumeGame();
    }
}