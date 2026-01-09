// File: GetStartedActivity.java
package com.newandromo.dev26615.app640653;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        Button startGameBtn = findViewById(R.id.btnStartGame);
        Button highScoresBtn = findViewById(R.id.btnHighScores);
        Button aboutBtn = findViewById(R.id.btnAbout);

        startGameBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, GameActivity.class);
            startActivity(intent);
        });

        highScoresBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivity.this, HighScoreActivity.class);
            startActivity(intent);
        });

        aboutBtn.setOnClickListener(v -> showAboutDialog());
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        Button closeBtn = dialog.findViewById(R.id.btnClose);
        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // Exit app on back press from main menu
        finishAffinity();
    }
}