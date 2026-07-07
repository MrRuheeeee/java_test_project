package me.mr_ruheeeee.java_tower_defense;

import android.graphics.Color;

public enum TDColors {

    WHITE(Color.rgb(255, 255, 255)),
    BLACK(Color.rgb(0, 0, 0)),
    RED(Color.rgb(255, 0, 0)),
    BLUE(Color.rgb(0, 0, 255)),
    GREEN(Color.rgb(0, 255, 0)),
    BROWN(Color.rgb(165, 42, 42)),
    ORANGE(Color.rgb(255, 200, 0)),
    YELLOW(Color.rgb(255, 255, 0)),
    LIGHT_GRAY(Color.rgb(192, 192, 192)),
    DARK_GRAY_MED(Color.rgb(64, 64, 64)),
    PATH_GRAY(Color.rgb(128, 128, 128)),
    SKY_BLUE(Color.rgb(135, 206, 235)),
    DARK_GREEN(Color.rgb(50, 200, 100)),
    DARK_GRAY_DEEP(Color.rgb(20, 20, 20)),
    CUSTOM_ORANGE(Color.rgb(255, 100, 0)),
    GOLD_YELLOW(Color.rgb(255, 200, 0)),
    FIELD_GREEN(Color.rgb(34, 177, 76)),
    GRID_LINE_OVERLAY(Color.argb(30, 0, 0, 0)),
    END_SCREEN_OVERLAY(Color.argb(167, 0, 0, 0));

    public final int color;

    TDColors(int color) {
        this.color = color;
    }
}
