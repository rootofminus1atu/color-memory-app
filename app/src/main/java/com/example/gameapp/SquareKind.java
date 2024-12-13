package com.example.gameapp;

import android.graphics.Color;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public enum SquareKind {
    NE, NW, SW, SE;

    private static final Random random = new Random();

    private static final Map<SquareKind, View> squareKindViewMap = new HashMap<>();

    public void setView(View view) {
        squareKindViewMap.put(this, view);
    }

    public View getView() {
        return squareKindViewMap.get(this);
    }

    public int getColor() {
        switch (this) {
            case NE:
                return Color.RED;
            case NW:
                return Color.GREEN;
            case SW:
                return Color.BLUE;
            case SE:
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }

    public static SquareKind getRandom() {
        SquareKind[] values = SquareKind.values();
        return values[random.nextInt(values.length)];
    }
}
