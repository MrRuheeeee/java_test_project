package me.mr_ruheeeee.java_tower_defense.towers;

import java.util.List;

import me.mr_ruheeeee.java_tower_defense.Bullet;
import me.mr_ruheeeee.java_tower_defense.Enemy;
import me.mr_ruheeeee.java_tower_defense.TDColors;
import me.mr_ruheeeee.java_tower_defense.Vector3d;

public class CannonTower extends Tower {

    public CannonTower(Vector3d pos) {
        super(
                pos,
                "Cannon",
                TDColors.LIGHT_GRAY, // was TD_Colors.GRAY in the original
                167,
                16.5f,
                7,
                1,
                1,
                50
        );
    }

    @Override
    public void shoot(List<Enemy> enemies, List<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Enemy target = null;
        for (Enemy enemy : enemies) {
            boolean inRange = pos.dst(enemy.pos) < this.range;
            boolean betterTarget = target == null || enemy.progress > target.progress;
            if (inRange && betterTarget) {
                target = enemy;
            }
        }

        if (target != null) {
            Vector3d shootFrom = pos.cpy().add(20, 20, 0);
            bullets.add(new Bullet(
                    shootFrom,
                    target.pos,
                    this.bulletSpeed,
                    TDColors.LIGHT_GRAY,
                    6,
                    this.bulletHealth,
                    this.bulletDamage
            ));
            cooldown = this.cooldownTime;
        }
    }

    @Override
    public boolean upgrade() {
        if (this.innerColorRGB > 0) {
            incLevel();
            this.innerColorRGB -= 25;
            this.cooldownTime -= 3;
            innerPaint.setARGB(255, innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 1.05f;
            this.range += 6;

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