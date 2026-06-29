package towerDefense;

import java.awt.*;

public enum TD_Colors {
    RED(Color.RED),
    GRAY(Color.LIGHT_GRAY),
    DARKGREY(Color.DARK_GRAY),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    WHITE(Color.WHITE),
    ORANGE(Color.ORANGE),
    YELLOW(Color.YELLOW),
    SKY_BLUE(Color.decode("#87CEEB")),
    BROWN(Color.decode("#A52A2A")),
    DARK_GREEN(Color.decode("#32C864")),
    DARK_GRAY(Color.decode("#141414")),
    CUSTOM_ORANGE(Color.decode("#FF6400")),
    GOLD_YELLOW(Color.decode("#FFC800")),
    ;

    public Color color;

    TD_Colors(Color green) {
        this.color = green;
    }
}
