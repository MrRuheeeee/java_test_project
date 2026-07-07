package me.mr_ruheeeee.java_tower_defense;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {

    public Vector3d pos;
    public Vector3d movement;
    public boolean alive = true;
    public boolean reachedEnd = false;
    public float progress = 0; // track the enemy's progress along the path
    public int health;
    public int currentLevel;

    private float speed;
    private final Paint paint = new Paint();
    private int waypointIndex = 0;

    static final Vector3d[] WAYPOINTS = {
            new Vector3d(-20, 500, 0), // start (outside, left)
            new Vector3d(220, 500, 0), // right
            new Vector3d(220, 60, 0),  // up
            new Vector3d(580, 60, 0),  // right
            new Vector3d(580, 220, 0), // down
            new Vector3d(460, 220, 0), // left
            new Vector3d(460, 420, 0), // down
            new Vector3d(660, 420, 0), // right
            new Vector3d(660, 620, 0), // down (outside = finish)
    };

    public Enemy(int health, int currentLevel) {
        this.pos = WAYPOINTS[0].cpy();
        this.health = health;
        this.currentLevel = currentLevel;
        this.movement = WAYPOINTS[1].cpy().sub(pos).nor();
        setSpeedAndColor();
    }

    private void setSpeedAndColor() {
        if (this.health <= 0) {
            this.alive = false;
        } else if (this.health == 1) {
            paint.setColor(TDColors.RED.color);
            this.speed = 1.8f;
        } else if (this.health == 2) {
            paint.setColor(TDColors.ORANGE.color);
            this.speed = 2.2f;
        } else if (this.health == 3) {
            paint.setColor(TDColors.YELLOW.color);
            this.speed = 2.8f;
        } else if (this.health == 4) {
            paint.setColor(TDColors.BLUE.color);
            this.speed = 4.0f;
        } else if (this.health <= (this.currentLevel / 2)) {
            paint.setColor(TDColors.BROWN.color);
            this.speed = 2.3f;
        } else if (this.health <= (10 * (this.currentLevel + 1))) {
            paint.setColor(TDColors.SKY_BLUE.color);
            this.speed = 1.4f;
        } else if (this.health <= (20 * (this.currentLevel + 1))) {
            paint.setColor(TDColors.DARK_GRAY_MED.color);
            this.speed = 1.0f;
        } else {
            // shouldn't happen, but just in case - white so it stands out
            paint.setColor(TDColors.WHITE.color);
            this.speed = 0.5f;
        }
        this.speed *= (1.0f + (0.01f * this.currentLevel));

        // re-apply direction with the (possibly changed) speed
        movement = movement.cpy().nor().scl(speed);
    }

    public void move() {
        progress += speed;
        Vector3d destination = WAYPOINTS[Math.min(waypointIndex + 1, WAYPOINTS.length - 1)];
        double distance = pos.dst(destination);

        if (distance < speed) {
            pos = destination.cpy();
            waypointIndex++;
            if (waypointIndex >= WAYPOINTS.length - 1) {
                reachedEnd = true;
                alive = false; // enemy reached the end of the path
            } else {
                movement = WAYPOINTS[waypointIndex + 1].cpy().sub(pos).nor().scl(speed);
            }
        } else {
            pos.add(movement);
        }
    }

    public void hit(int bulletDamage) {
        this.health -= bulletDamage;
        setSpeedAndColor(); // update speed & color after taking damage
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) pos.x, (float) pos.y, 15, paint);
    }
}