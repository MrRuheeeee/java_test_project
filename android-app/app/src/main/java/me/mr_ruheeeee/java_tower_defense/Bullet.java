package me.mr_ruheeeee.java_tower_defense;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

public class Bullet {

    Vector3d pos;
    Vector3d movement;
    float speed;
    int size;
    int health;
    int damage;
    boolean hit = false;

    private final Paint fillPaint = new Paint();
    private final Paint outlinePaint = new Paint();
    private Enemy lastHitEnemy = null;

    public Bullet(Vector3d pos, Vector3d destination, float speed, TDColors color, int size, int health, int damage) {
        this.pos = pos.cpy();
        this.speed = speed;
        this.movement = destination.cpy().sub(this.pos).nor().scl(this.speed);
        this.size = size;
        this.health = health;
        this.damage = damage;

        fillPaint.setColor(color.color);
        outlinePaint.setColor(TDColors.BLACK.color);
        outlinePaint.setStyle(Paint.Style.STROKE);
    }

    // returns true if the bullet should be removed
    public boolean bulletInteraction(List<Enemy> enemies) {
        this.hit = false;

        for (Enemy enemy : enemies) {
            if (this.pos.dst(enemy.pos) < (15 + (this.size / 2f))) {
                if (enemy == lastHitEnemy) {
                    return false; // already registered this collision
                }
                lastHitEnemy = enemy;
                enemy.hit(damage);
                this.health--;
                this.hit = true;
                return this.health <= 0;
            }
        }

        lastHitEnemy = null;
        // remove the bullet if it flew off the 800x600 field
        return pos.x <= -10 || pos.x >= 810 || pos.y <= -10 || pos.y >= 610;
    }

    public void move() {
        pos.add(movement);
    }

    public void draw(Canvas canvas) {
        float left = (float) pos.x - size / 2f;
        float top = (float) pos.y - size / 2f;
        canvas.drawOval(left, top, left + size, top + size, fillPaint);
        canvas.drawOval(left, top, left + size, top + size, outlinePaint);
    }
}
