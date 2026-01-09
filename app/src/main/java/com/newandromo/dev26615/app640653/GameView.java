// File: GameView.java
package com.newandromo.dev26615.app640653;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    private Paint basketPaint, coinPaint, bgPaint;
    private float basketX, basketY;
    private float basketWidth = 150;
    private float basketHeight = 40;
    private ArrayList<Coin> coins;
    private Random random;
    private GameActivity gameActivity;
    private Thread gameThread;
    private boolean isRunning = false;
    private int score = 0;
    private long lastCoinSpawn = 0;
    private int coinSpawnInterval = 700; // milliseconds

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        basketPaint = new Paint();
        basketPaint.setColor(Color.rgb(139, 69, 19));
        basketPaint.setStyle(Paint.Style.FILL);

        coinPaint = new Paint();
        coinPaint.setColor(Color.rgb(255, 215, 0));
        coinPaint.setStyle(Paint.Style.FILL);

        bgPaint = new Paint();
        bgPaint.setColor(Color.rgb(135, 206, 250));

        coins = new ArrayList<>();
        random = new Random();

        basketY = 0; // Will be set in onSizeChanged
    }

    public void setGameActivity(GameActivity activity) {
        this.gameActivity = activity;
        startGame();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        basketX = w / 2 - basketWidth / 2;
        basketY = h - basketHeight - 50;
    }

    private void startGame() {
        if (!isRunning) {
            isRunning = true;
            gameThread = new Thread(() -> {
                while (isRunning) {
                    long currentTime = System.currentTimeMillis();

                    if (currentTime - lastCoinSpawn > coinSpawnInterval) {
                        spawnCoin();
                        lastCoinSpawn = currentTime;

                        // Gradually increase difficulty
                        if (score > 0 && score % 5 == 0 && coinSpawnInterval > 800) {
                            coinSpawnInterval -= 50;
                        }
                    }

                    updateGame();
                    postInvalidate();

                    try {
                        Thread.sleep(16); // ~60 FPS
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            gameThread.start();
        }
    }

    private void spawnCoin() {
        float coinSize = 60;
        float x = random.nextFloat() * (getWidth() - coinSize);
        coins.add(new Coin(x, -coinSize, coinSize));
    }

    private void updateGame() {
        ArrayList<Coin> coinsToRemove = new ArrayList<>();

        for (Coin coin : coins) {
            coin.y += 5 + (score * 0.1f); // Speed increases with score

            // Check if caught
            if (coin.y + coin.size >= basketY &&
                    coin.y <= basketY + basketHeight &&
                    coin.x + coin.size >= basketX &&
                    coin.x <= basketX + basketWidth) {
                coinsToRemove.add(coin);
                score++;
                if (gameActivity != null) {
                    gameActivity.updateScore(score);
                }
            }

            // Check if missed
            if (coin.y > getHeight()) {
                isRunning = false;
                if (gameActivity != null) {
                    gameActivity.showGameOverDialog();
                }
                break;
            }
        }

        coins.removeAll(coinsToRemove);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background
        canvas.drawRect(0, 0, getWidth(), getHeight(), bgPaint);

        // Draw basket
        canvas.drawRoundRect(basketX, basketY, basketX + basketWidth,
                basketY + basketHeight, 10, 10, basketPaint);

        // Draw coins
        for (Coin coin : coins) {
            canvas.drawCircle(coin.x + coin.size/2, coin.y + coin.size/2,
                    coin.size/2, coinPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE ||
                event.getAction() == MotionEvent.ACTION_DOWN) {
            basketX = event.getX() - basketWidth / 2;

            // Keep basket in bounds
            if (basketX < 0) basketX = 0;
            if (basketX + basketWidth > getWidth()) {
                basketX = getWidth() - basketWidth;
            }
        }
        return true;
    }

    public void pauseGame() {
        isRunning = false;
    }

    public void resumeGame() {
        if (!isRunning && gameThread != null && !gameThread.isAlive()) {
            startGame();
        }
    }

    public void resetGame() {
        score = 0;
        coins.clear();
        coinSpawnInterval = 1500;
        lastCoinSpawn = 0;
        startGame();
    }

    private static class Coin {
        float x, y, size;

        Coin(float x, float y, float size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
    }
}