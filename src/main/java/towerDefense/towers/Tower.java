package towerDefense.towers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import towerDefense.Bullet;
import towerDefense.Enemy;
import towerDefense.TD_Colors;
import towerDefense.Vector3d;

public abstract class Tower {

    public Vector3d pos;
    public boolean alive = true;

    Color outerColor;
    Color innerColor;
    String type;
    int cooldown = 0; // Cooldown timer for tower attacks
    int range;
    
    float bulletSpeed = 1f;
    int bulletSize = 7;
    int bulletHealth = 1;
    int cooldownTime = 40;
    int innerColorRGB = 250;
    int currentTowerLevel = 0;
    
    public Tower(Vector3d pos, String type, TD_Colors color, int range) {
        this.pos = pos;
        this.type = type;
        this.outerColor = color.color;
        this.innerColor = TD_Colors.WHITE.color;
        this.range = range;
        
        this.cooldown = 0; // Initialize cooldown
    }

    // functions, all towers need:
    public abstract void shoot(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets);

    public abstract boolean Upgrade();

    public int getLevel() {
        return currentTowerLevel;
    }

    public void incLevel() {
        currentTowerLevel++;
    }

    // not yet used
    public Vector3d getAimSpot(Enemy enemy, double bulletSpeed) {
        Vector3d totarget = this.pos.cpy().sub(enemy.pos);

        double a = enemy.movement.dot(enemy.movement) - (bulletSpeed * bulletSpeed);
        double b = 2 * enemy.movement.dot(totarget);
        double c = totarget.dot(totarget);

        double p = -b / (2 * a);
        double q = Math.sqrt((b * b) - 4 * a * c) / (2 * a);

        double t1 = p - q;
        double t2 = p + q;
        double t;

        if (t1 > t2 && t2 > 0)
        {
            t = t2;
        }
        else
        {
            t = t1;
        }

        return enemy.pos.cpy().add(enemy.movement).scl(t);
    }

    public void drawRange(Graphics g) {
        if (g instanceof Graphics2D g2d) {
            Composite oldComposite = g2d.getComposite();
            Color oldColor = g2d.getColor();

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
            g2d.setColor(new Color(255, 255, 255));
            int centerX = (int) pos.x + 20;
            int centerY = (int) pos.y + 20;
            int diameter = range * 2;
            g2d.fillOval(centerX - range, centerY - range, diameter, diameter);

            g2d.setComposite(oldComposite);
            g2d.setColor(oldColor);
        }
    }

    public void draw(Graphics g) {
        g.setColor(this.outerColor);
        int drawX = (int)pos.x;
        int drawY = (int)pos.y;
        g.fillRect(drawX, drawY, 40, 40);
        g.setColor(innerColor);
        g.fillOval(drawX + 10, drawY + 10, 20, 20);
    }
}