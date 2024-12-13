package com.example.gameapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Db extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "game.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SCORE = "score";
    private static final String COLUMN_NAME = "name";

    public Db(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCORE + " INTEGER, " +
                COLUMN_NAME + " TEXT)";
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public ArrayList<ScoreEntry>  getTopPlayers(int limit) {
        ArrayList<ScoreEntry> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCORES, new String[]{COLUMN_SCORE, COLUMN_NAME}, null, null, null, null, COLUMN_SCORE + " DESC", String.valueOf(limit));

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));

                    scores.add(new ScoreEntry(score, name));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return scores;
    }

    public void insertScore(int score, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_SCORES + " (" + COLUMN_SCORE + ", " + COLUMN_NAME + ") VALUES (" + score + ", '" + name + "')");
    }

    public void removeLatestScores(int amount) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the latest 2 scores by their id (assuming 'id' is auto-incremented)
        String deleteQuery = "DELETE FROM " + TABLE_SCORES +
                " WHERE " + COLUMN_ID + " IN (SELECT " + COLUMN_ID +
                " FROM " + TABLE_SCORES + " ORDER BY " + COLUMN_ID + " DESC LIMIT " + amount + ")";

        db.execSQL(deleteQuery);
    }
}
