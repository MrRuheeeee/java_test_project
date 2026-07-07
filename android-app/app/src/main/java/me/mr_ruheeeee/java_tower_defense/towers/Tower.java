package me.mr_ruheeeee.java_tower_defense.towers;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import me.mr_ruheeeee.java_tower_defense.Bullet;
import me.mr_ruheeeee.java_tower_defense.Enemy;
import me.mr_ruheeeee.java_tower_defense.TDColors;
import me.mr_ruheeeee.java_tower_defense.Vector3d;

public abstract class Tower {

    public Vector3d pos;
    public boolean alive = true;

    protected final Paint outerPaint = new Paint();
    protected final Paint innerPaint = new Paint();
    protected final Paint rangePaint = new Paint();

    String type;
    int cooldown = 0;
    int range;
    int bulletDamage;

    float bulletSpeed = 1f;
    int bulletSize = 7;
    int bulletHealth = 1;
    int cooldownTime = 40;
    int innerColorRGB = 250;
    int currentTowerLevel = 0;

    public Tower(
            Vector3d pos,
            String type,
            TDColors color,
            int range,
            float bulletSpeed,
            int bulletSize,
            int bulletHealth,
            int bulletDamage,
            int cooldownTime
    ) {
        this.pos = pos;
        this.type = type;
        this.range = range;
        this.bulletSpeed = bulletSpeed;
        this.bulletSize = bulletSize;
        this.bulletHealth = bulletHealth;
        this.bulletDamage = bulletDamage;
        this.cooldownTime = cooldownTime;

        outerPaint.setColor(color.color);
        innerPaint.setColor(TDColors.WHITE.color);
        rangePaint.setColor(TDColors.WHITE.color);
        rangePaint.setAlpha(20); // faint range circle, 0-255 scale
    }

    // every tower type must implement its own targeting/shooting behavior
    public abstract void shoot(List<Enemy> enemies, List<Bullet> bullets);

    // every tower type must implement its own upgrade effect
    public abstract boolean upgrade();

    public int getLevel() {
        return currentTowerLevel;
    }

    public void incLevel() {
        currentTowerLevel++;
    }

    // TODO: predictive aiming at a moving enemy - not used yet, needs testing
    public Vector3d getAimSpot(Enemy enemy, double bulletSpeed) {
        // pass - not implemented yet, towers currently aim directly at the enemy's position
        return enemy.pos;
    }

    public void drawRange(Canvas canvas) {
        float centerX = (float) pos.x + 20;
        float centerY = (float) pos.y + 20;
        canvas.drawCircle(centerX, centerY, range, rangePaint);
    }

    public void draw(Canvas canvas) {
        float drawX = (float) pos.x;
        float drawY = (float) pos.y;
        canvas.drawRect(drawX, drawY, drawX + 40, drawY + 40, outerPaint);
        canvas.drawOval(drawX + 10, drawY + 10, drawX + 30, drawY + 30, innerPaint);
    }
}