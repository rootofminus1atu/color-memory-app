package com.example.gameapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Iterator;

public class Play extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private View blob, neSquare, nwSquare, swSquare, seSquare;
    private float blobX, blobY;

    private int insetTop, insetBottom, insetLeft, insetRight;

    private GameData gameData;
    private Iterator<SquareKind> sequenceIterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            insetTop = systemBars.top;
            insetBottom = systemBars.bottom;
            insetLeft = systemBars.left;
            insetRight = systemBars.right;

            return insets;
        });

        gameData = GameData.getGameData(getIntent());
        sequenceIterator = gameData.getSequenceIterator();

        System.out.println(gameData.getSequence());

        blob = findViewById(R.id.blob);

        neSquare = findViewById(R.id.ne_square);
        nwSquare = findViewById(R.id.nw_square);
        swSquare = findViewById(R.id.sw_square);
        seSquare = findViewById(R.id.se_square);

        SquareKind.NE.setView(neSquare);
        SquareKind.NW.setView(nwSquare);
        SquareKind.SW.setView(swSquare);
        SquareKind.SE.setView(seSquare);

        neSquare.setBackgroundColor(SquareKind.NE.getColor());
        nwSquare.setBackgroundColor(SquareKind.NW.getColor());
        swSquare.setBackgroundColor(SquareKind.SW.getColor());
        seSquare.setBackgroundColor(SquareKind.SE.getColor());

        final View parentLayout = findViewById(R.id.main);
        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                resetBlobPosition();
                parentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        ((TextView)findViewById(R.id.level_title)).setText("Level " + (gameData.getLevel() + 1));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];

            float speedX = 14;
            float speedY = 24;

            float deltaX = y * speedY;
            float deltaY = x * speedX;

            blobX += deltaX;
            blobY += deltaY;

            blobX = Math.max(insetLeft, Math.min(blobX, ((View) blob.getParent()).getWidth() - blob.getWidth() - insetRight));
            blobY = Math.max(insetTop, Math.min(blobY, ((View) blob.getParent()).getHeight() - blob.getHeight() - insetBottom));

            blob.setX(blobX);
            blob.setY(blobY);

            View collidedSquare = getCollidedSquare(blob, neSquare, nwSquare, swSquare, seSquare);
            if (collidedSquare != null) {
                handleCollision(collidedSquare);
            }
        }
    }

    private void handleCollision(View collidedSquare) {
        if (!sequenceIterator.hasNext()) {
            // safety check so that we don't crash
            return;
        }

        SquareKind toBeHit = sequenceIterator.next();
        System.out.println(toBeHit);

        if (collidedSquare != toBeHit.getView()) {
            gameOver();
            return;
        }

        if (!sequenceIterator.hasNext()) {
            progress();
            return;
        }

        resetBlobPosition();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private View getCollidedSquare(View blob, View... squares) {
        Rect blobRect = new Rect();
        Rect squareRect = new Rect();
        blob.getHitRect(blobRect);

        for (View square : squares) {
            square.getHitRect(squareRect);
            if (Rect.intersects(blobRect, squareRect)) {
                return square;
            }
        }
        return null;
    }

    private void progress() {
        gameData.nextLevel();
        Intent intent = new Intent(Play.this, Sequence.class);
        GameData.putGameData(intent, gameData);
        startActivity(intent);
    }

    private void gameOver() {
        Intent intent = new Intent(Play.this, GameOver.class);
        GameData.putGameData(intent, gameData);
        startActivity(intent);
    }

    private void resetBlobPosition() {
        blobX = ((View) blob.getParent()).getWidth() / 2 - blob.getWidth() / 2;
        blobY = ((View) blob.getParent()).getHeight() / 2 - blob.getHeight() / 2;
        blob.setX(blobX);
        blob.setY(blobY);
    }
}