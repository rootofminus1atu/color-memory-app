package com.example.gameapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.play_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, Sequence.class);
            GameData.putGameData(intent);  // no gameData passed - new one will be created
            startActivity(intent);
        });

        findViewById(R.id.high_score_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, HighScore.class);
            startActivity(intent);
        });
    }
}