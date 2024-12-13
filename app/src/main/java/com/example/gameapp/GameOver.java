package com.example.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GameOver extends AppCompatActivity {
    boolean alreadySaved = false;

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

        GameData gameData = GameData.getGameData(getIntent());

        Db db = new Db(this);
        boolean isAcceptableScore = isInTopScores(db, gameData.getScore()) && gameData.getScore() > 0;

        TextView nameInput = findViewById(R.id.name_input);

        if (isAcceptableScore) {
            nameInput.setVisibility(View.VISIBLE);
            findViewById(R.id.save_score_button).setVisibility(View.VISIBLE);
        }

        ((TextView)findViewById(R.id.score_text)).setText("Your score was: " + gameData.getScore());

        findViewById(R.id.save_score_button).setOnClickListener(v -> {
            if (!isAcceptableScore) {
                return;
            }

            if (alreadySaved) {
                Toast.makeText(this, "Your score was already saved!", Toast.LENGTH_SHORT).show();
                return;
            }

            String playerName = nameInput.getText().toString().trim();

            if (playerName.isEmpty()) {
                Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
                return;
            }

            db.insertScore(gameData.getScore(), playerName);
            Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HighScore.class);
            startActivity(intent);
        });

        findViewById(R.id.play_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, Sequence.class);
            GameData.putGameData(intent);  // no gameData passed - new one will be created
            startActivity(intent);
        });

        findViewById(R.id.menu_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.high_score_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, HighScore.class);
            startActivity(intent);
        });
    }

    private boolean isInTopScores(Db db, int score) {
        ArrayList<ScoreEntry> topScores = db.getTopPlayers(5);

        if (topScores.size() < 5) {
            return true;
        }

        return topScores.stream().anyMatch(entry -> score > entry.getScore());
    }
}