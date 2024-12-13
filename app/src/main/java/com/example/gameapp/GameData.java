package com.example.gameapp;

import android.content.Intent;
import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class GameData implements Serializable {
    private static final int DEFAULT_COLORS_AMOUNT = 4;
    private static final int INCREMENT_AMOUNT = 2;

    private int level = 0;
    private final ArrayList<SquareKind> sequence = new ArrayList<SquareKind>();

    public GameData() {
        for (int i = 0; i < DEFAULT_COLORS_AMOUNT; i++) {
            sequence.add(SquareKind.getRandom());
        }
    }

    public int getLevel() {
        return level;
    }

    public void nextLevel() {
        level += 1;

        for (int i = 0; i < INCREMENT_AMOUNT; i++) {
            sequence.add(SquareKind.getRandom());
        }
    }

    public int getSquaresAmount() {
        return DEFAULT_COLORS_AMOUNT + INCREMENT_AMOUNT * level;
    }

    // fail score
    // 0 -> 0
    // 1 -> 4
    // 2 -> 6

    public int getScore() {
        return level == 0 ? 0 : getSquaresAmount() - INCREMENT_AMOUNT;
    }

    public ArrayList<SquareKind> getSequence() {
        return sequence;
    }

    public Iterator<SquareKind> getSequenceIterator() {
        return sequence.iterator();
    }


    // helpers for passing the game data between activities

    public static GameData getGameData(@NonNull Intent intent) {
        GameData gameData = (GameData) intent.getSerializableExtra("gameData");

        if (gameData == null) {
            return new GameData();
        }

        return gameData;
    }

    public static void putGameData(@NonNull Intent intent) {
        GameData gameData = new GameData();
        intent.putExtra("gameData", gameData);
    }

    public static void putGameData(@NonNull Intent intent, @NonNull GameData gameData) {
        intent.putExtra("gameData", gameData);
    }
}
