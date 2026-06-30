package towerDefense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.*;
import towerDefense.towers.Tower;

public class GameLogic {

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Tower> tower = new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int money = 50;
    public int sellPrice = 50;
    public int upgradePrice = 50;
    public int towerPrice = 0;
    public int towerPriceArrow = 30;
    public int towerPriceCannon = 40;
    public int towerPriceMagic = 60;
    public int towerPriceSuper = 80;
    public Timer timer;
    public int timerSpeed = 33;
    public int currentLevel = 0;

    private final ArrayList<Integer> spawnQueue = new ArrayList<>();
    private final Random random = new Random();
    private boolean increaseLevel = true;
    private int spawnCooldown = 0;
    private int[] currentLevelConfig = {0, 0, 0, 0, 0, 0, 0};
    public int [][] levels = {
        // skip first level, cause i`m sloppy
        {}, // red, orange, yellow, blue, skyeblue, darkgrey
        {5, 0, 0, 0, 0, 0, 0},       // lvl 1
        {7, 0, 0, 0, 0, 0, 0},       // lvl 2
        {8, 0, 0, 0, 0, 0, 0},       // lvl 3
        {14,1, 0, 0, 0, 0, 0},       // lvl 4
        {24,2, 0, 0, 0, 0, 0},       // lvl 5
        {16,6, 0, 0, 0, 0, 0},       // lvl 6
        {14,8, 1, 0, 0, 0, 0},      // lvl 7
        {12,7, 12, 0, 0, 0, 0},      // lvl 8
        {16,4, 3, 0, 0, 0, 0},      // lvl 9
        {14,5, 4, 0, 0, 0, 0},      // lvl 10
        {18,8, 5, 0, 0, 0, 0},       // lvl 11
        {9, 4, 4, 1, 0, 0, 0},       // lvl 12
        {8, 4, 5, 2, 0, 0, 0},      // lvl 13
        {4, 12,8, 4, 0, 0, 0},      // lvl 14
        {9, 9, 10,0, 0, 0, 0},       // lvl 15
        {5, 5, 3, 8, 0, 0, 0},       // lvl 16   
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
        {0, 20,20,10,10,0, 0},       // lvl 37
        {0, 0, 20,20,10,0, 0},       // lvl 38
        {0, 0, 0, 20,20,0, 0},       // lvl 39
        {0, 0, 0, 0, 0, 1, 0},       // lvl 40
        {99,10,6, 7, 5, 0, 0},       // lvl 41
        {10,20,15,25,15,0, 0},       // lvl 42
        {5, 5, 5, 5, 5, 1, 0},       // lvl 43
        {20,8, 2, 10,30,0, 0},       // lvl 44
        {10,10,0, 35,8, 0, 0},       // lvl 45
        {50,50,0, 0, 0, 2, 0},       // lvl 46
        {6, 7, 67,6, 7, 0, 0},       // lvl 47
        {16,22,26,34,38,1, 0},       // lvl 48
        {30,20,10,10,20,2, 0},       // lvl 49
        {0, 0, 0, 0, 0, 0, 1},       // lvl 50
        {0,10,10,20,10, 4, 0},       // lvl 51
        {8,50,40,30,70, 1, 0},       // lvl 52
        {0, 0, 0, 0, 0, 1, 1},       // lvl 53

    };

    private final GamePanel panel;

    // constructor
    public GameLogic(GamePanel panel) {
        this.panel = panel;
    }

    private int calculateCooldown(int health) {
        int base;
        if (health == 1) {
            base = 150;
        } else if (health == 2) {
            base = 200;
        } else if (health == 3) {
            base = 250;
        } else if (health == 4) { // blue
            base = 300;
        } else if (health <= 10) {
            base = 350;
        } else if (health == (currentLevel * 10)) {
            base = 15000;
        } else if (health == (currentLevel * 20)) {
            base = 40000;
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

                money += currentLevel * 13;
                currentLevel++;

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

            int[] enemyHealth = {
                1,
                2,
                3,
                4,
                this.currentLevel / 2,
                10 * currentLevel,
                20 * currentLevel,
            };

            for (int i = 0; i < currentLevelConfig.length; i++) {
                for (int j = 0; j < currentLevelConfig[i]; j++) {
                spawnQueue.add(enemyHealth[i]);
                }
            }
            Collections.shuffle(spawnQueue, random);

        } else {
            int next = spawnQueue.removeFirst();
            enemies.add(new Enemy(next, currentLevel));
            spawnCooldown = calculateCooldown(next);
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