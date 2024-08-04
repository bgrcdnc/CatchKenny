package com.bugracdnc.catchkenny;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class GameOverActivity extends AppCompatActivity {
    TextView scoreText, highscoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        scoreText = findViewById(R.id.scoreText);
        highscoreText = findViewById(R.id.highscore);

        Intent passedIntent = getIntent();
        int score = passedIntent.getIntExtra("score", 0),
                highscore = passedIntent.getIntExtra("highscore", 0);

        if(score == highscore) {
            TextView newHighscore = findViewById(R.id.newHighscore);
            newHighscore.setVisibility(View.VISIBLE);
        }

        scoreText.setText(String.format(Locale.getDefault(),"Your Score: %d", score));
        highscoreText.setText(String.format(Locale.getDefault(),"Highscore: %d", highscore));
    }

    public void returnToGame(View view) {
        this.finish();
    }
}