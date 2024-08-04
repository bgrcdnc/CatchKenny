package com.bugracdnc.catchkenny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class GameActivity extends AppCompatActivity {
    final int HIGHSCORE_ERROR = -1, HIGHSCORE_DEF = 0;
    TextView countdownText, scoreText, highscoreText;
    ImageView[] kennyImages;
    SharedPreferences sp;
    CatchKennyGame kenny;

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

        kenny = new CatchKennyGame(getStoredHighscore(sp), kennyImages, this::finishGame, this::updateCountdownText);
        highscoreText.setText(String.format(Locale.getDefault(), "Highscore: %s", kenny.getHighscore()));

        restartGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartGame();
    }

    public void restartGame() {
        if (!kenny.isRunning()) {
            kenny.restart();
            updateScoreText();
            updateHighscoreText();
        }
    }

    public void finishGame() {
        updateHighscoreText();
        updateScoreText();
        kenny.stop();
        Intent toGameOver = new Intent(this, GameOverActivity.class);
        toGameOver.putExtra("score", kenny.getScore());
        toGameOver.putExtra("highscore", kenny.getHighscore());
        startActivity(toGameOver);
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
        if (sharedPref != null) {
            if (sharedPref.contains("highscore")) {
                return sharedPref.getInt("highscore", HIGHSCORE_DEF);
            }
        }
        return HIGHSCORE_ERROR;
    }

    public void increaseScore(View view) {
        if (kenny.isRunning()) {
            kenny.incScore();
            checkHighscore();
            updateScoreText();
        }
    }

    private void updateText(TextView tv, String format, int val) {
        tv.setText(String.format(Locale.getDefault(), format, val));
    }

    private void updateCountdownText() {
        updateText(countdownText, "Time Left: %ds", kenny.getRemainingTime()+1);
    }

    public void updateScoreText() {
        updateText(scoreText, "Score: %d", kenny.getScore());
    }

    private void updateHighscoreText() {
        updateText(highscoreText, "Highscore: %d", kenny.getHighscore());
    }
}