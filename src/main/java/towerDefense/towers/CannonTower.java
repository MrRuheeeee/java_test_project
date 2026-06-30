package towerDefense.towers;

import java.awt.Color;
import java.util.ArrayList;
import towerDefense.Bullet;
import towerDefense.Enemy;
import towerDefense.TD_Colors;
import towerDefense.Vector3d;

public class CannonTower extends Tower {
    
    public CannonTower(Vector3d pos) {
        super(
            pos, 
            "Cannon", 
            TD_Colors.GRAY, 
            167,
            16.5f,
            7,
            1,
            1,
            50
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
                TD_Colors.GRAY, 
                6, this.bulletHealth,
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
            this.cooldownTime -= 3;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 1.05f;
            this.range += 6;
            // 2 times (1, 5)
            if ((getLevel() % 5) == 1) {
                this.bulletHealth += 1;
                this.bulletSize++;
            } else if (getLevel() == 9) {
                this.bulletDamage++;
            }
            return true;
        }
        return false;        
    }
}