package com.bugracdnc.catchkenny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class GameActivity extends AppCompatActivity {
    final int HIGHSCORE_DEF = 0;
    TextView countdownText, scoreText, highscoreText;
    ImageView[] kennyImages;
    SharedPreferences sp;
    CatchKennyGame kenny;
    int timeDef = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initGlobals();

        kenny = new CatchKennyGame(getStoredHighscore(sp), kennyImages, new CountDownTimer(timeDef * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                updateHighscoreText();
                updateScoreText();
                kenny.stop();
                Intent toGameOver = new Intent(GameActivity.this, GameOverActivity.class);
                toGameOver.putExtra("score", kenny.getScore());
                toGameOver.putExtra("highscore", kenny.getHighscore());
                startActivity(toGameOver);
            }
        });

        draw();
        kenny.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartGame();
    }

    private void draw() {
        updateScoreText();
        updateHighscoreText();
    }

    private void initGlobals() {
        countdownText = findViewById(R.id.countdownTimerText);
        scoreText = findViewById(R.id.scoreText);
        highscoreText = findViewById(R.id.highscore);

        kennyImages = new ImageView[9];
        kennyImages[0] = findViewById(R.id.kennyImage0);
        kennyImages[1] = findViewById(R.id.kennyImage1);
        kennyImages[2] = findViewById(R.id.kennyImage2);
        kennyImages[3] = findViewById(R.id.kennyImage3);
        kennyImages[4] = findViewById(R.id.kennyImage4);
        kennyImages[5] = findViewById(R.id.kennyImage5);
        kennyImages[6] = findViewById(R.id.kennyImage6);
        kennyImages[7] = findViewById(R.id.kennyImage7);
        kennyImages[8] = findViewById(R.id.kennyImage8);

        sp = getPreferences(MODE_PRIVATE);
    }

    private void restartGame() {
        if (!kenny.isRunning()) {
            kenny.restart();
            updateScoreText();
            updateHighscoreText();
        }
    }

    private void checkHighscore() {
        if (kenny.getScore() > kenny.getHighscore()) {
            setHighscore(kenny.getScore(), sp);
            updateHighscoreText();
        }
    }

    private void setHighscore(int s, SharedPreferences sharedPref) {
        kenny.setHighscore(s);
        if (sharedPref != null)
            sharedPref.edit().putInt("highscore", kenny.getHighscore()).apply();
    }

    private int getStoredHighscore(SharedPreferences sharedPref) {
        if (sharedPref == null) {
            sharedPref = getPreferences(MODE_PRIVATE);
        }

        if (sharedPref.contains("highscore")) {
            return sharedPref.getInt("highscore", HIGHSCORE_DEF);
        } else {
            setHighscore(HIGHSCORE_DEF, sharedPref);
            return HIGHSCORE_DEF;
        }
    }

    public void increaseScore(View view) {
        if (kenny.isRunning()) {
            kenny.incScore();
            checkHighscore();
            updateScoreText();
        }
    }

    private void updateText(@NonNull TextView tv, String format, int val) {
        tv.setText(String.format(Locale.getDefault(), format, val));
    }

    private void updateCountdownText(long millisUntilFinished) {
        updateText(countdownText, "Time Left: %ds", (int) (millisUntilFinished / 1000));
    }

    private void updateScoreText() {
        updateText(scoreText, "Score: %d", kenny.getScore());
    }

    private void updateHighscoreText() {
        updateText(highscoreText, "Highscore: %d", kenny.getHighscore());
    }

}