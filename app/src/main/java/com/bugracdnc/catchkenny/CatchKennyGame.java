package com.bugracdnc.catchkenny;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.concurrent.ThreadLocalRandom;

public class CatchKennyGame {
    private final View[] Clickables;
    private int Highscore, Score;
    private boolean Running;
    CountDownTimer Timer;
    private final Handler pickRandomHandler;
    Runnable randomizer;
    int visible = 0;

    public CatchKennyGame(int highscore, View[] clickables, CountDownTimer timer) {
        Highscore = highscore;
        Clickables = clickables;
        Timer = timer;
        pickRandomHandler = new Handler(Looper.getMainLooper());
        randomizer = this::pickRandomClickable;
    }

    public void incScore() {
        pickRandomHandler.removeCallbacks(randomizer);
        Score++;
        pickRandomClickable();
    }

    public void restart() {
        Score = 0;
        start();
    }

    public void start() {
        Running = true;
        Timer.start();
        pickRandomClickable();
    }

    public void stop() {
        Running = false;
        hideAllClickables();
        pickRandomHandler.removeCallbacks(this::pickRandomClickable);
    }

    private void hideAllClickables() {
        for (View v : Clickables) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void pickRandomClickable() {
        hideAllClickables();
        int rand = ThreadLocalRandom.current().nextInt(0, 9);
        while(rand == visible) { rand = ThreadLocalRandom.current().nextInt(0, 9); }
        visible = rand;
        Clickables[visible].setVisibility(View.VISIBLE);
        pickRandomHandler.postDelayed(randomizer, 1000);
    }

    public boolean isRunning() {
        return Running;
    }

    public int getHighscore() {
        return Highscore;
    }

    public void setHighscore(int highscore) {
        Highscore = highscore;
    }

    public int getScore() {
        return Score;
    }
}
