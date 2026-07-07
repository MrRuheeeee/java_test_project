package towerDefense;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Bullet {
    Vector3d pos;
    Vector3d movement;
    // float targetX, targetY;
    float speed;
    Color color;
    int size;
    int health;
    int damage;
    // double directionX, directionY;
    boolean hit = false;
    Enemy lastHitEnemy = null;

    public Bullet(Vector3d pos, Vector3d destination, float speed, TD_Colors color, int size, int health, int damage) {
        this.pos = pos.cpy();
        this.speed = speed;
        this.movement = destination.cpy().sub(this.pos).nor().scl(this.speed);
        this.color = color.color;
        this.size = size;
        this.health = health;
        this.damage = damage;
    }

    public boolean bulletInteraction(ArrayList<Enemy> enemies) {
        this.hit = false;

        for (Enemy enemy : enemies) {
            if (this.pos.dst(enemy.pos) < (15 + (this.size / 2))) { // Collision radius of 25 pixels
                if (enemy == lastHitEnemy) {
                    return false; // Already registered this collision
                }

                lastHitEnemy = enemy;
                enemy.hit(damage);
                this.health--;
                this.hit = true;

                return this.health <= 0;// Remove the bullet
            }
        }

        lastHitEnemy = null;
        // Remove the bullet if it goes off-screen
        // Keep the bullet if it didn't hit anything
        return pos.x <= -10 || pos.x >= 810 || pos.y <= -10 || pos.y >= 610;
    }

    public void move() {
        pos.add(movement);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)pos.x - (this.size / 2), (int)pos.y - (this.size / 2), this.size, this.size);
        g.setColor(Color.BLACK);
        g.drawRoundRect((int)pos.x - (this.size / 2), (int)pos.y - (this.size / 2), this.size, this.size, 20, 20);
    }

    @Override
    public String toString() {
        return "Bullet[pos:"+pos.toString()+", move:"+movement.toString()+"]";
    }
}