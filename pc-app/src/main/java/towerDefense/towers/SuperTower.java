package towerDefense.towers;

import java.awt.Color;
import java.util.ArrayList;
import towerDefense.Bullet;
import towerDefense.Enemy;
import towerDefense.TD_Colors;
import towerDefense.Vector3d;

public class SuperTower extends Tower {
    
    public SuperTower(Vector3d pos) {
        super(
            pos,
            "Super",
            TD_Colors.GREEN,
            100,
            4.5f,
            5,
            1,
            1,
            15
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
            bullets.add(new Bullet(
                p, 
                first.pos, 
                this.bulletSpeed, 
                TD_Colors.BLUE, 
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
            this.cooldownTime -= 1;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 0.35f;
            this.range += 1;
            // 4 times (1, 4, 7, 10)
            if ((getLevel() % 3) == 1) {
                this.bulletSize += 1;
                this.range += 2;
            }
            if (getLevel() == 10) {
                this.bulletDamage++;
            }
            return true;
        }
        return false;        
    }
}