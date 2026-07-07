package me.mr_ruheeeee.java_tower_defense;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.mr_ruheeeee.java_tower_defense.towers.ArrowTower;
import me.mr_ruheeeee.java_tower_defense.towers.CannonTower;
import me.mr_ruheeeee.java_tower_defense.towers.MagicTower;
import me.mr_ruheeeee.java_tower_defense.towers.SuperTower;
import me.mr_ruheeeee.java_tower_defense.towers.Tower;

public class GameView extends View {

    // region set variables
    private static final int GRID_SIZE = 40;
    private static final int FIELD_WIDTH = 800;
    private static final int FIELD_HEIGHT = 600;
    private static final int GAME_WIDTH = 960;
    private static final int GAME_HEIGHT = 600;
    private static final int TICK_MS = 33;
    private final Paint backgroundPaint;
    private final Paint pathPaint;
    private final Paint gridPaint;
    private final List<RectF> pathRects;
    private boolean[][] occupied;

    // --- game entities ---
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>();
    private final List<Bullet> bullets = new ArrayList<>();
    private Tower selectedTower = null;

    // --- game loop ---
    private final Handler handler = new Handler(Looper.getMainLooper());
    private float currentScale = 1f;

    // --- game state ---
    private boolean isRunning = false;
    private int speedMultiplier = 1;
    private int money = 50;
    private String selectedShopType = "";

    // --- dynamic prices, rise every time they're used, like the original ---
    private int towerPriceArrow = 30;
    private int towerPriceCannon = 40;
    private int towerPriceMagic = 60;
    private int towerPriceSuper = 80;
    private int sellPrice = 50;
    private int upgradePrice = 50;

    // --- wave / level system ---
    private final List<Integer> spawnQueue = new ArrayList<>();
    private final Random random = new Random();
    private boolean increaseLevel = true;
    private int spawnCooldown = 0;
    private int[] currentLevelConfig = {0, 0, 0, 0, 0, 0, 0};
    private int currentLevel = 0;
    private boolean gameOver = false;
    private boolean looseGame = false;

    private final int[][] levels = {
            {}, // level 0 skipped
            {5, 0, 0, 0, 0, 0, 0},
            {7, 0, 0, 0, 0, 0, 0},
            {8, 0, 0, 0, 0, 0, 0},
            {14, 1, 0, 0, 0, 0, 0},
            {24, 2, 0, 0, 0, 0, 0},
            {16, 6, 0, 0, 0, 0, 0},
            {14, 8, 1, 0, 0, 0, 0},
            {12, 7, 12, 0, 0, 0, 0},
            {16, 4, 3, 0, 0, 0, 0},
            {14, 5, 4, 0, 0, 0, 0},
            {18, 8, 5, 0, 0, 0, 0},
            {9, 4, 4, 1, 0, 0, 0},
            {8, 4, 5, 2, 0, 0, 0},
            {4, 12, 8, 4, 0, 0, 0},
            {9, 9, 10, 0, 0, 0, 0},
            {5, 5, 3, 8, 0, 0, 0},
            {10, 10, 10, 10, 0, 0, 0},
            {5, 3, 1, 0, 1, 0, 0},
            {12, 3, 1, 0, 0, 0, 0},
            {8, 4, 2, 0, 1, 0, 0},
            {10, 5, 3, 0, 1, 0, 0},
            {18, 8, 1, 0, 2, 0, 0},
            {10, 3, 5, 0, 0, 0, 0},
            {22, 4, 6, 0, 3, 0, 0},
            {3, 9, 3, 0, 0, 0, 0},
            {2, 2, 2, 2, 0, 0, 0},
            {15, 10, 5, 5, 0, 0, 0},
            {10, 10, 10, 10, 5, 0, 0},
            {3, 3, 3, 3, 3, 0, 0},
            {5, 8, 4, 3, 0, 0, 0},
            {25, 9, 4, 0, 6, 0, 0},
            {1, 3, 6, 7, 10, 0, 0},
            {0, 6, 7, 9, 0, 0, 0},
            {6, 7, 6, 7, 0, 0, 0},
            {6, 7, 67, 6, 7, 0, 0},
            {20, 20, 20, 10, 10, 0, 0},
            {0, 20, 20, 10, 10, 0, 0},
            {0, 0, 20, 20, 10, 0, 0},
            {0, 0, 0, 20, 20, 0, 0},
            {0, 0, 0, 0, 0, 1, 0},
            {99, 10, 6, 7, 5, 0, 0},
            {10, 20, 15, 25, 15, 0, 0},
            {5, 5, 5, 5, 5, 1, 0},
            {20, 8, 2, 10, 30, 0, 0},
            {10, 10, 0, 35, 8, 0, 0},
            {50, 50, 0, 0, 0, 2, 0},
            {6, 7, 67, 6, 7, 0, 0},
            {16, 22, 26, 34, 38, 1, 0},
            {30, 20, 10, 10, 20, 2, 0},
            {0, 0, 0, 0, 0, 0, 1},
            {0, 10, 10, 20, 10, 4, 0},
            {8, 50, 40, 30, 70, 1, 0},
            {0, 0, 0, 0, 0, 1, 1},
    };

    // --- UI hitboxes (same coordinates as the original Swing buttons) ---
    private final RectF menuPanel = new RectF(803, 3, 957, 597);
    private final RectF startStopButton = new RectF(810, 10, 840, 40);
    private final RectF speedButton = new RectF(845, 10, 875, 40);
    private final RectF buyArrowButton = new RectF(810, 45, 950, 75);
    private final RectF buyCannonButton = new RectF(810, 85, 950, 115);
    private final RectF buyMagicButton = new RectF(810, 125, 950, 155);
    private final RectF buySuperButton = new RectF(810, 165, 950, 195);
    private final RectF upgradeButton = new RectF(810, 565, 875, 590);
    private final RectF sellButton = new RectF(885, 565, 950, 590);

    // --- UI paints ---
    private final Paint panelBgPaint = solid(TDColors.DARK_GRAY_DEEP.color, Paint.Style.FILL);
    private final Paint panelFramePaint = solid(TDColors.BLUE.color, Paint.Style.STROKE);
    private final Paint activeBgPaint = solid(TDColors.DARK_GRAY_MED.color, Paint.Style.FILL);
    private final Paint frameGreenPaint = solid(TDColors.DARK_GREEN.color, Paint.Style.STROKE);
    private final Paint frameOrangePaint = solid(TDColors.CUSTOM_ORANGE.color, Paint.Style.STROKE);
    private final Paint frameLightGrayPaint = solid(TDColors.LIGHT_GRAY.color, Paint.Style.STROKE);
    private final Paint iconWhitePaint = solid(TDColors.WHITE.color, Paint.Style.FILL);
    private final Paint iconArrowPaint = solid(TDColors.RED.color, Paint.Style.FILL);
    private final Paint iconCannonPaint = solid(TDColors.LIGHT_GRAY.color, Paint.Style.FILL);
    private final Paint iconMagicPaint = solid(TDColors.BLUE.color, Paint.Style.FILL);
    private final Paint iconSuperPaint = solid(TDColors.GREEN.color, Paint.Style.FILL);
    private final Paint moneyTextPaint = solid(TDColors.GOLD_YELLOW.color, Paint.Style.FILL);
    private final Paint startStopIconPaint = solid(TDColors.DARK_GREEN.color, Paint.Style.FILL);
    private final Paint upgradeArrowPaint = solid(TDColors.DARK_GREEN.color, Paint.Style.FILL);
    private final Paint sellArrowPaint = solid(TDColors.CUSTOM_ORANGE.color, Paint.Style.FILL);
    private final Paint labelPaint = new Paint();
    private final Paint labelOutlinePaint = new Paint();

    // endregion

    private static Paint solid(int color, Paint.Style style) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(style);
        p.setStrokeWidth(2.5f);
        p.setAntiAlias(true);
        return p;
    }

    public GameView(Context context) {
        super(context);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(TDColors.FIELD_GREEN.color);

        pathPaint = new Paint();
        pathPaint.setColor(TDColors.PATH_GRAY.color);

        gridPaint = new Paint();
        gridPaint.setColor(TDColors.GRID_LINE_OVERLAY.color);

        labelPaint.setAntiAlias(true);
        labelPaint.setFakeBoldText(true);

        labelOutlinePaint.setAntiAlias(true);
        labelOutlinePaint.setFakeBoldText(true);
        labelOutlinePaint.setColor(TDColors.BLACK.color);
        labelOutlinePaint.setStyle(Paint.Style.STROKE);
        labelOutlinePaint.setStrokeWidth(4f);

        pathRects = new ArrayList<>();
        initPathRects();

        startGameLoop();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        blockStarterOccupied();
    }

    private void startGameLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    update();
                    if (speedMultiplier == 2) {
                        update();
                    }
                }
                invalidate();
                handler.postDelayed(this, TICK_MS);
            }
        }, TICK_MS);
    }

    private void update() {
        if (gameOver) {
            return;
        }

        if (money > 99_999) {
            money = 99_999;
        }

        spawnLevel();

        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.reachedEnd) {
                gameOver = true;
                looseGame = true;
            }
        }

        for (Tower tower : towers) {
            tower.shoot(enemies, bullets);
        }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            bullet.move();
            boolean endBullet = bullet.bulletInteraction(enemies);
            if (bullet.hit) {
                if (money >= 99_999) {
                    money = 99_999;
                }else {
                    money++;
                }
                enemies.removeIf(enemy -> !enemy.alive);
            }
            if (endBullet) {
                bullets.remove(i);
            }
        }
    }

    // region wave system
    private int calculateCooldown(int health) {
        int base;
        if (health == 1) {
            base = 150;
        } else if (health == 2) {
            base = 200;
        } else if (health == 3) {
            base = 250;
        } else if (health == 4) {
            base = 300;
        } else if (health <= 10) {
            base = 350;
        } else if (health == (currentLevel * 10)) {
            base = 15000;
        } else if (health == (currentLevel * 20)) {
            base = 40000;
        } else {
            base = 67; // shouldn't happen
        }

        int randomNumber = random.nextInt(10);
        return Math.max(5, (base / (currentLevel + 5)) + randomNumber);
    }

    private void spawnLevel() {
        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        if (currentLevel >= levels.length) {
            gameOver = true;
            looseGame = false;
            currentLevel = levels.length - 1;
            return;
        }

        if (spawnQueue.isEmpty()) {
            if (increaseLevel) {
                if (!enemies.isEmpty()) {
                    return; // wait until the current wave has fully cleared
                }
                money += currentLevel * 13;
                currentLevel++;

                if (currentLevel >= levels.length) {
                    // NOTE: no win screen yet - just stop spawning further waves
                    gameOver = true;
                    looseGame = false;
                    currentLevel = levels.length - 1;
                    return;
                }
            } else {
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
            int next = spawnQueue.remove(0);
            enemies.add(new Enemy(next, currentLevel));
            spawnCooldown = calculateCooldown(next);
        }
    }

    // endregion

    // region drawing

    private void initPathRects() {
        pathRects.add(new RectF(0, 480, 240, 520));
        pathRects.add(new RectF(200, 40, 240, 480));
        pathRects.add(new RectF(200, 40, 600, 80));
        pathRects.add(new RectF(560, 40, 600, 240));
        pathRects.add(new RectF(440, 200, 560, 240));
        pathRects.add(new RectF(440, 200, 480, 400));
        pathRects.add(new RectF(440, 400, 640, 440));
        pathRects.add(new RectF(640, 400, 680, 600));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        currentScale = Math.min((float) getWidth() / GAME_WIDTH, (float) getHeight() / GAME_HEIGHT);

        canvas.save();
        canvas.scale(currentScale, currentScale);


        for (RectF rect : pathRects) {
            canvas.drawRect(rect, pathPaint);
        }

        drawGrid(canvas);

        for (Tower tower : towers) {
            tower.draw(canvas);
        }
        if (selectedTower != null) {
            selectedTower.drawRange(canvas);
        }
        for (Bullet bullet : bullets) {
            bullet.draw(canvas);
        }
        for (Enemy enemy : enemies) {
            enemy.draw(canvas);
        }

        drawLevelDisplay(canvas);
        drawUI(canvas);

        if (gameOver) {
            drawEndScreen(canvas);
        }

        canvas.restore();
    }

    private void drawGrid(Canvas canvas) {
        for (int x = 0; x <= FIELD_WIDTH; x += GRID_SIZE) {
            canvas.drawLine(x, 0, x, FIELD_HEIGHT, gridPaint);
        }
        for (int y = 0; y <= FIELD_HEIGHT; y += GRID_SIZE) {
            canvas.drawLine(0, y, FIELD_WIDTH, y, gridPaint);
        }
    }

    private void drawLevelDisplay(Canvas canvas) {
        String text = currentLevel + "/" + (levels.length - 1);

        labelOutlinePaint.setTextAlign(Paint.Align.LEFT);
        labelOutlinePaint.setTextSize(16f);
        canvas.drawText(text, 765, 20, labelOutlinePaint);

        labelPaint.setColor(TDColors.WHITE.color);
        labelPaint.setTextAlign(Paint.Align.LEFT);
        labelPaint.setTextSize(16f);
        canvas.drawText(text, 765, 20, labelPaint);
    }

    private void drawUI(Canvas canvas) {
        canvas.drawRoundRect(menuPanel, 20, 20, panelBgPaint);
        canvas.drawRoundRect(menuPanel, 20, 20, panelFramePaint);

        drawStartStopButton(canvas);
        drawSpeedButton(canvas);

        drawTowerBuyButton(canvas, buyArrowButton, iconArrowPaint, "Arrow", towerPriceArrow);
        drawTowerBuyButton(canvas, buyCannonButton, iconCannonPaint, "Cannon", towerPriceCannon);
        drawTowerBuyButton(canvas, buyMagicButton, iconMagicPaint, "Magic", towerPriceMagic);
        drawTowerBuyButton(canvas, buySuperButton, iconSuperPaint, "Super", towerPriceSuper);

        drawUpgradeButton(canvas);
        drawSellButton(canvas);

        moneyTextPaint.setTextSize(16f);
        moneyTextPaint.setFakeBoldText(true);
        moneyTextPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(money + "$", 940, 30, moneyTextPaint);
    }

    private void drawEndScreen(Canvas canvas) {
        Paint overlay = new Paint();
        overlay.setColor(TDColors.END_SCREEN_OVERLAY.color);
        canvas.drawRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT, overlay);

        Paint bigText = new Paint();
        bigText.setAntiAlias(true);
        bigText.setFakeBoldText(true);
        bigText.setTextSize(67f);
        bigText.setTextAlign(Paint.Align.CENTER);

        String finalText;
        if (looseGame) {
            bigText.setColor(TDColors.CUSTOM_ORANGE.color);
            finalText = "YOU LOOSE!";
        } else {
            bigText.setColor(TDColors.DARK_GREEN.color);
            finalText = "YOU WIN!";
        }

        canvas.drawText(finalText, FIELD_WIDTH / 2f, FIELD_HEIGHT / 2f, bigText);
    }

    private void drawStartStopButton(Canvas canvas) {
        canvas.drawRoundRect(startStopButton, 20, 20, panelBgPaint);
        canvas.drawRoundRect(startStopButton, 20, 20, frameGreenPaint);

        float left = startStopButton.left;
        float top = startStopButton.top;

        if (isRunning) {
            RectF pauseRect = new RectF(left + 5f, top + 5f, left + 25f, top + 25f);
            canvas.drawRoundRect(pauseRect, 6.67f, 6.67f, startStopIconPaint);
        } else {
            Path shape = new Path();
            shape.moveTo(left + 5.83f, top + 5.83f);
            shape.lineTo(left + 5.83f, top + 25f);
            shape.lineTo(left + 25f, top + 15f);
            shape.close();
            canvas.drawPath(shape, startStopIconPaint);
        }
    }

    private void drawSpeedButton(Canvas canvas) {
        canvas.drawRoundRect(speedButton, 20, 20, panelBgPaint);
        canvas.drawRoundRect(speedButton, 20, 20, frameGreenPaint);

        labelPaint.setColor(TDColors.DARK_GREEN.color);
        labelPaint.setTextAlign(Paint.Align.CENTER);
        labelPaint.setTextSize(16f);
        float textY = speedButton.centerY() - ((labelPaint.descent() + labelPaint.ascent()) / 2);
        canvas.drawText(speedMultiplier + "x", speedButton.centerX(), textY, labelPaint);
    }

    private void drawTowerBuyButton(Canvas canvas, RectF button, Paint iconColor, String towerType, int price) {
        if (selectedShopType.equals(towerType)) {
            canvas.drawRoundRect(button, 20, 20, activeBgPaint);
        }

        Paint frame;
        if (money >= price) {
            frame = frameGreenPaint;
        } else {
            frame = frameOrangePaint;
        }
        canvas.drawRoundRect(button, 20, 20, frame);

        float left = button.left;
        float top = button.top;
        canvas.drawRect(left + 7, top + 5, left + 27, top + 25, iconColor);
        canvas.drawOval(left + 12, top + 10, left + 22, top + 20, iconWhitePaint);

        labelPaint.setColor(frame.getColor());
        labelPaint.setTextAlign(Paint.Align.RIGHT);
        labelPaint.setTextSize(14f);
        String text = price + "$";
        float textY = button.centerY() - ((labelPaint.descent() + labelPaint.ascent()) / 2);
        canvas.drawText(text, button.right - 10, textY, labelPaint);
    }

    private void drawUpgradeButton(Canvas canvas) {
        if (selectedShopType.equals("Upgrade")) {
            canvas.drawRoundRect(upgradeButton, 20, 20, activeBgPaint);
        }

        Paint frame;
        if (money >= upgradePrice) {
            frame = frameGreenPaint;
        } else {
            frame = frameOrangePaint;
        }
        canvas.drawRoundRect(upgradeButton, 20, 20, frame);

        float left = upgradeButton.left;
        float top = upgradeButton.top;
        Path arrow = new Path();
        arrow.moveTo(left + 11, top + 7);
        arrow.lineTo(left + 7, top + 16);
        arrow.lineTo(left + 15, top + 16);
        arrow.close();
        canvas.drawPath(arrow, upgradeArrowPaint);

        labelPaint.setColor(frame.getColor());
        labelPaint.setTextAlign(Paint.Align.RIGHT);
        labelPaint.setTextSize(13f);
        float textY = upgradeButton.centerY() - ((labelPaint.descent() + labelPaint.ascent()) / 2);
        canvas.drawText(upgradePrice + "$", upgradeButton.right - 10, textY, labelPaint);
    }

    private void drawSellButton(Canvas canvas) {
        if (selectedShopType.equals("Sell")) {
            canvas.drawRoundRect(sellButton, 20, 20, activeBgPaint);
        }
        canvas.drawRoundRect(sellButton, 20, 20, frameLightGrayPaint);

        float left = sellButton.left;
        float top = sellButton.top;
        Path arrow = new Path();
        arrow.moveTo(left + 11, top + 16);
        arrow.lineTo(left + 7, top + 7);
        arrow.lineTo(left + 15, top + 7);
        arrow.close();
        canvas.drawPath(arrow, sellArrowPaint);

        labelPaint.setColor(TDColors.LIGHT_GRAY.color);
        labelPaint.setTextAlign(Paint.Align.RIGHT);
        labelPaint.setTextSize(13f);
        float textY = sellButton.centerY() - ((labelPaint.descent() + labelPaint.ascent()) / 2);
        canvas.drawText(sellPrice + "$", sellButton.right - 10, textY, labelPaint);
    }

    // endregion

    // region input handling

    private boolean isBlocked(int gridX, int gridY) {
        int gx = gridX / GRID_SIZE;
        int gy = gridY / GRID_SIZE;

        if (gx < 0 || gy < 0 || gx >= (GAME_WIDTH / GRID_SIZE) || gy >= (GAME_HEIGHT / GRID_SIZE)) {
            return true;
        }
        return occupied[gx][gy];
    }

    private void blockStarterOccupied() {
        int cols = GAME_WIDTH / GRID_SIZE;
        int rows = GAME_HEIGHT / GRID_SIZE;
        System.out.println(cols);
        System.out.println(rows);

        occupied = new boolean[cols][rows];

        for (int gx = 0; gx < cols; gx++) {
            for (int gy = 0; gy < rows; gy++) {
                RectF cellRect = new RectF(gx * GRID_SIZE, gy * GRID_SIZE,
                        gx * GRID_SIZE + GRID_SIZE, gy * GRID_SIZE + GRID_SIZE);
                for (RectF pathRect : pathRects) {
                    if (RectF.intersects(cellRect, pathRect)) {
                        occupied[gx][gy] = true;
                        break;
                    }
                }
                if (RectF.intersects(cellRect, menuPanel)) {
                    occupied[gx][gy] = true;
                }
            }
        }

        for (int gy = 0; gy < occupied[0].length; gy++) {
            StringBuilder row = new StringBuilder();
            for (int gx = 0; gx < occupied.length; gx++) {
                row.append(occupied[gx][gy] ? "#" : ".");
            }
            System.out.println(row);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver || event.getAction() != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }

        float fieldX = event.getX() / currentScale;
        float fieldY = event.getY() / currentScale;

        if (handleUiTouch(fieldX, fieldY)) {
            invalidate();
            return true;
        }

        placeTowerAt(fieldX, fieldY);
        return true;
    }

    private boolean handleUiTouch(float x, float y) {
        if (startStopButton.contains(x, y)) {
            isRunning = !isRunning;
            return true;
        }
        if (speedButton.contains(x, y)) {
            speedMultiplier = (speedMultiplier == 1) ? 2 : 1;
            return true;
        }
        if (buyArrowButton.contains(x, y)) {
            if (selectedShopType.equals("Arrow")) {
                selectedShopType = "";
            } else {
                selectedShopType = "Arrow";
            }
            return true;
        }
        if (buyCannonButton.contains(x, y)) {
            if (selectedShopType.equals("Cannon")) {
                selectedShopType = "";
            } else {
                selectedShopType = "Cannon";
            }
            return true;
        }
        if (buyMagicButton.contains(x, y)) {
            if (selectedShopType.equals("Magic")) {
                selectedShopType = "";
            } else {
                selectedShopType = "Magic";
            }
            return true;
        }
        if (buySuperButton.contains(x, y)) {
            if (selectedShopType.equals("Super")) {
                selectedShopType = "";
            } else {
                selectedShopType = "Super";
            }
            return true;
        }
        if (upgradeButton.contains(x, y)) {
            if (selectedShopType.equals("Upgrade")) {
                selectedShopType = "";
            } else {
                selectedShopType = "Upgrade";
            }
            return true;
        }
        if (sellButton.contains(x, y)) {
            if (selectedShopType.equals("Sell")) {
                selectedShopType = "";
            } else {
                selectedShopType = "Sell";
            }
            return true;
        }
        return false;
    }

    private void placeTowerAt(float fieldX, float fieldY) {
        int gridX = (int) (fieldX / GRID_SIZE) * GRID_SIZE;
        int gridY = (int) (fieldY / GRID_SIZE) * GRID_SIZE;
        int gx = (int) fieldX / GRID_SIZE;
        int gy = (int) fieldY / GRID_SIZE;

        Vector3d tapPos = new Vector3d(gridX, gridY, 0);

        // tapped an existing tower -> select/deselect it instead of placing
        for (Tower t : towers) {
            if (t.pos.idt(tapPos)) {
                selectedTower = (selectedTower == t) ? null : t;
                if (selectedShopType.equals("Upgrade") && money >= upgradePrice) {
                    if (t.upgrade()) {
                        money -= upgradePrice;
                        upgradePrice += 1;
                        return;
                    }
                } else if (selectedShopType.equals("Sell")) {
                    money += sellPrice;
                    sellPrice += 10;
                    towers.remove(t);
                    selectedTower = null;
                    occupied[gx][gy] = false;
                    return;
                }
                return;
            }
        }

        if (isBlocked(gridX, gridY)) {
            return;
        }

        gx = (int) fieldX / GRID_SIZE;
        gy = (int) fieldY / GRID_SIZE;
        if (!occupied[gx][gy]) {
            if (selectedShopType.equals("Arrow")) {
                if (money >= towerPriceArrow) {
                    towers.add(new ArrowTower(tapPos));
                    money -= towerPriceArrow;
                    towerPriceArrow += 30;
                    occupied[gx][gy] = true;
                } else {
                    System.out.println("no money broke boyyyy");
                }
            } else if (selectedShopType.equals("Cannon")) {
                if (money >= towerPriceCannon) {
                    towers.add(new CannonTower(tapPos));
                    money -= towerPriceCannon;
                    towerPriceCannon += 40;
                    occupied[gx][gy] = true;
                } else {
                    System.out.println("no money broke boyyyy");
                }
            } else if (selectedShopType.equals("Magic")) {
                if (money >= towerPriceMagic) {
                    towers.add(new MagicTower(tapPos));
                    money -= towerPriceMagic;
                    towerPriceMagic += 60;
                    occupied[gx][gy] = true;
                } else {
                    System.out.println("no money broke boyyyy");
                }
            } else if (selectedShopType.equals("Super")) {
                if (money >= towerPriceSuper) {
                    towers.add(new SuperTower(tapPos));
                    money -= towerPriceSuper;
                    towerPriceSuper += 80;
                    occupied[gx][gy] = true;
                } else {
                    System.out.println("no money broke boyyyy");
                }
            }

        }
    }

    // endregion

}