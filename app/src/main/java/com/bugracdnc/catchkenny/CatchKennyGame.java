package com.bugracdnc.catchkenny;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import java.util.concurrent.ThreadLocalRandom;

public class CatchKennyGame {
    private final View[] Clickables;
    private final Handler Handler;
    private int Highscore, Score, DefaultTime = 15, RemainingTime;
    private boolean Running;
    private Runnable GameTicker;

    public CatchKennyGame(int highscore, View[] clickables, Runnable gameFinish, Runnable gameUpdates) {
        Highscore = highscore;
        Clickables = clickables;
        Handler = new Handler(Looper.getMainLooper());
        GameTicker = () -> {
            RemainingTime--;
            if (RemainingTime == -1) {
                Running = false;
                gameFinish.run();
            } else {
                pickRandomClickable();
                gameUpdates.run();
                Handler.postDelayed(GameTicker, 1000);
            }
        };
    }

    private void hideAllClickables() {
        for(View v:Clickables) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    private void pickRandomClickable() {
        hideAllClickables();
        Clickables[ThreadLocalRandom.current().nextInt(0, 9)].setVisibility(View.VISIBLE);
    }

    public void incScore() {
        Score++;
    }

    public void restart() {
        RemainingTime = DefaultTime;
        Score = 0;
        start();
    }

    public void start() {
        Running = true;
        Handler.post(GameTicker);
    }

    public void stop() {
        Running = false;
        hideAllClickables();
        Handler.removeCallbacks(GameTicker);
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

    public void setDefaultTime(int defaultTime) {
        DefaultTime = defaultTime;
    }

    public int getRemainingTime() {
        return RemainingTime;
    }
}
