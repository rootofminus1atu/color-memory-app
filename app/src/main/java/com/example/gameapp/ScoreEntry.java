package com.example.gameapp;

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

    @Override
    public String toString() {
        return "ScoreEntry{name='" + name + "', score=" + score + "}";
    }
}
