package towerDefense.towers;

import java.awt.Color;
import java.util.ArrayList;
import towerDefense.Bullet;
import towerDefense.Enemy;
import towerDefense.TD_Colors;
import towerDefense.Vector3d;

public class MagicTower extends Tower {
    
    public MagicTower(Vector3d pos) {
        super(
            pos,
            "Magic",
            TD_Colors.BLUE,
            115,
            6.7f,
            15,
            3,
            3,
            160
        );
    }

    @Override
    public void shoot(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Enemy first = null; // Track the first enemy in range
        for (Enemy enemy : enemies) {
            boolean inRange = pos.dst(enemy.pos) < this.range;
            boolean firstEnemy = first == null || enemy.progress > first.progress;
            if (inRange && firstEnemy) {
                first = enemy;
            }
        }

        if (first != null) {
            Vector3d p = pos.cpy().add(20, 20,0);
            // Vector3d aim = getAimSpot(first, 6.7f);
            bullets.add(new Bullet(
                p, 
                first.pos, 
                this.bulletSpeed, 
                TD_Colors.WHITE, 
                this.bulletSize, 
                this.bulletHealth,
                this.bulletDamage
            ));
            cooldown = this.cooldownTime; // Reset cooldown
        }
    }  

    @Override
    public boolean Upgrade() {
        if (this.innerColorRGB > 0) {
            incLevel();
            this.innerColorRGB -= 25;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.cooldownTime -= 7;
            this.bulletSpeed += 0.15f;
            if ((getLevel() % 2) == 1) {
                this.bulletHealth += 1;
                this.bulletDamage++;
            }
            this.bulletDamage++;
            this.range += 5;
            this.bulletSize += 1;
            return true;
        }
        return false;        
    }
}