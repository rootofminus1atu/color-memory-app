package com.example.gameapp;

import androidx.annotation.NonNull;

public class ScoreEntry {
    private final int score;
    private final String name;

    public ScoreEntry(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "ScoreEntry{name='" + name + "', score=" + score + "}";
    }
}
