package towerDefense;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import towerDefense.towers.Tower;

public class GameLogic {

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Tower> tower = new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int money = 0;
    public int sellPrice = 50;
    public int upgradePrice = 50;
    public int towerPrice = 0;
    public int towerPriceArrow = 0;
    public int towerPriceCannon = 40;
    public int towerPriceMagic = 60;
    public int towerPriceSuper = 80;
    public Timer timer;
    public int timerSpeed = 33;
    public int currentLevel = 0;

    private final ArrayList<TD_Colors> spawnQueue = new ArrayList<>();
    private final Random random = new Random();
    private boolean increaseLevel = true;
    private int spawnCooldown = 0;
    private int[] currentLevelConfig = {0, 0, 0, 0, 0, 0, 0};
    public int [][] levels = {
        // skip first level, cause i`m sloppy
        {}, // red, orange, yellow, blue
        {4, 0, 0, 0, 0, 0, 0},       // lvl 1
        {6, 0, 0, 0, 0, 0, 0},       // lvl 2
        {7, 0, 0, 0, 0, 0, 0},       // lvl 3
        {3, 2, 0, 0, 0, 0, 0},       // lvl 4
        {5, 4, 0, 0, 0, 0, 0},       // lvl 5
        {5, 9, 1, 0, 0, 0, 0},       // lvl 6
        {8, 8, 3, 0, 0, 0, 0},      // lvl 7
        {16,4, 4, 0, 0, 0, 0},      // lvl 8
        {14,5, 5, 0, 0, 0, 0},      // lvl 9
        {12,6, 6, 1, 0, 0, 0},      // lvl 10
        {9, 4, 4, 0, 0, 0, 0},       // lvl 11
        {8, 4, 5, 2, 0, 0, 0},       // lvl 12
        {4, 12,8, 4, 0, 0, 0},      // lvl 13
        {9, 9, 10,0, 0, 0, 0},      // lvl 14
        {5, 5, 3, 8, 0, 0, 0},       // lvl 15
        {1, 1, 1, 1, 0, 0, 0},       // lvl 16   
        {10,10,10,10,0, 0, 0},     // lvl 17
        {5, 3, 1, 0, 1, 0, 0},       // lvl 18
        {12,3, 1, 0, 0, 0, 0},      // lvl 19
        {8, 4, 2, 0, 1, 0, 0},       // lvl 20
        {10,5, 3, 0, 1, 0, 0},      // lvl 21
        {18,8, 1, 0, 2, 0, 0},      // lvl 22
        {10,3, 5, 0, 0, 0, 0},      // lvl 23
        {22,4, 6, 0, 3, 0, 0},      // lvl 24
        {3, 9, 3, 0, 0, 0, 0},       // lvl 25
        {2, 2, 2, 2, 0, 0, 0},       // lvl 26
        {15,10,5, 5, 0, 0, 0},     // lvl 27
        {10,10,10,10,5, 0, 0},     // lvl 28
        {3, 3, 3, 3, 3, 0, 0},       // lvl 29
        {5, 8, 4, 3, 0, 0, 0},       // lvl 30
        {25,9, 4, 0, 6, 0, 0},      // lvl 31
        {1, 3, 6, 7, 10,0, 0},       // lvl 32
        {0, 6, 7, 9, 0, 0, 0},       // lvl 33
        {6, 7, 6, 7, 0, 0, 0},       // lvl 34
        {6, 7, 67,6, 7, 0, 0},      // lvl 35
        {20,20,20,10,10,0, 0},       // lvl 36
        {0, 50,50,10,10,0, 0},       // lvl 37
        {0, 0, 50,50,10,0, 0},       // lvl 38
        {0, 0, 0, 50,50,0, 0},       // lvl 39
        {0, 0, 0, 0, 0, 1, 0},       // lvl 40
        {0, 0, 0, 0, 0, 0, 1},       // lvl 41

    };

    private final GamePanel panel;

    // constructor
    public GameLogic(GamePanel panel) {
        this.panel = panel;
    }

    private int calculateCooldown(Color c) {
        int base;
        if (c == Color.RED) {
            base = 150;
        } else if (c == Color.ORANGE) {
            base = 200;
        } else if (c == Color.YELLOW) {
            base = 250;
        } else if (c == Color.BLUE) { // blue
            base = 300;
        } else if (c == TD_Colors.BROWN.color) {
            base = 350;
        } else if (c == TD_Colors.SKY_BLUE.color) {
            base = 15000;
        } else if (c == Color.DARK_GRAY) {
            base = 20000;
        } else {
            base = 67; // shouldnt exist
        }

        // cooldown between bloons
        int randomNumber = random.nextInt(10); // 0-10
        int cooldown = Math.max(5, (base / (currentLevel + 5)) + randomNumber);

        return cooldown;
    }

    private void spawnLevel() {
        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        if (spawnQueue.isEmpty()) {
            if (increaseLevel) {
                if (!enemies.isEmpty()) {
                    return;
                }
                
                currentLevel++;
                money += 10;

                if (currentLevel >= levels.length) {
                    GamePanel.gameOver = true;
                    GamePanel.looseGame = false;
                    return;
                }
            } else {
                // cooldown between phases
                int randomCooldownNumber = random.nextInt(10);
                spawnCooldown = (600 / (currentLevel + 5)) + randomCooldownNumber;
            }
            increaseLevel = !increaseLevel;

            currentLevelConfig = levels[currentLevel % levels.length];

            spawnQueue.clear();

            TD_Colors[] colors = {
                TD_Colors.RED, 
                TD_Colors.ORANGE, 
                TD_Colors.YELLOW, 
                TD_Colors.BLUE, 
                TD_Colors.BROWN, 
                TD_Colors.SKY_BLUE, 
                TD_Colors.DARKGREY,
            };
            for (int i = 0; i < currentLevelConfig.length; i++) {
                for (int j = 0; j < currentLevelConfig[i]; j++) {
                spawnQueue.add(colors[i]);
                }
            }
            Collections.shuffle(spawnQueue, random);

        } else {
            TD_Colors next = spawnQueue.removeFirst();
            enemies.add(new Enemy(next, currentLevel));
            spawnCooldown = calculateCooldown(next.color);
        }

    }

    public void start() {
        timer = new Timer(timerSpeed, e -> {
            spawnLevel();
            // Gegner bewegen
            for (Enemy enemy : enemies) {
                enemy.move();
            }

            // Tower shot
            for (Tower t : tower) {
                t.shoot(enemies, bullets);
            }

            // Bullets movement + collision
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                bullet.move();
                boolean endBullet = bullet.bulletInteraction(enemies);
                if (bullet.hit) {
                    money++;
                    enemies.removeIf(enemy -> !enemy.alive);
                }
                if (endBullet) {
                    bullets.remove(i);
                }
            }

            panel.repaint();
        });
    }

}