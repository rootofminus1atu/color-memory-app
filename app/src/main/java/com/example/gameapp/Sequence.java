package com.example.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayout.LayoutParams;


public class Sequence extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sequence);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        GameData gameData = GameData.getGameData(getIntent());

        FlexboxLayout squaresLayout = findViewById(R.id.squares_layout);
        for (SquareKind squareKind : gameData.getSequence()) {
            View square = new View(this);

            int sizeDp = 32;
            float density = getResources().getDisplayMetrics().density;
            int sizeInPixels = (int) (sizeDp * density + 0.5f);

            LayoutParams params = new LayoutParams(
                    sizeInPixels,
                    sizeInPixels
            );

            int margin = 16;
            params.setMargins(margin, margin, margin, margin);

            square.setBackgroundColor(squareKind.getColor());

            square.setLayoutParams(params);
            squaresLayout.addView(square);
        }

        ((TextView)findViewById(R.id.title)).setText("Memorize " + gameData.getSquaresAmount() + " Colors");

        findViewById(R.id.ready_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, Play.class);
            GameData.putGameData(intent, gameData);
            startActivity(intent);
        });
    }
}