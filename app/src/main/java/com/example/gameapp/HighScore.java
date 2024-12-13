package com.example.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.ListIterator;

public class HighScore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_high_score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Db db = new Db(this);
        ArrayList<ScoreEntry> topScores = db.getTopPlayers(5);

        LinearLayout scoresLayout = findViewById(R.id.scores_layout);
        ListIterator<ScoreEntry> iterator = topScores.listIterator();
        while (iterator.hasNext()) {
            int index = iterator.nextIndex();  // Get the current index
            ScoreEntry entry = iterator.next();  // Get the next ScoreEntry
            TextView scoreView = new TextView(this);
            scoreView.setText((index + 1) + ". " + entry.getName() + ": " + entry.getScore());
            scoreView.setTextSize(18);
            scoresLayout.addView(scoreView);
        }

        findViewById(R.id.play_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, Sequence.class);
            GameData.putGameData(intent);  // no gameData passed - new one will be created
            startActivity(intent);
        });

        findViewById(R.id.menu_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}