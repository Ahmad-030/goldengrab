// File: GameActivity.java
package com.newandromo.dev26615.app640653;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
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

            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_game_over);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            TextView finalScore = dialog.findViewById(R.id.finalScoreText);
            Button restartBtn = dialog.findViewById(R.id.btnRestart);
            Button menuBtn = dialog.findViewById(R.id.btnMenu);

            finalScore.setText("Your Score: " + currentScore);

            // Save high score
            HighScoreManager manager = new HighScoreManager(this);
            manager.addScore(currentScore);

            restartBtn.setOnClickListener(v -> {
                dialog.dismiss();
                restartGame();
            });

            menuBtn.setOnClickListener(v -> {
                dialog.dismiss();
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