package me.mr_ruheeeee.java_tower_defense.towers;

import java.util.List;

import me.mr_ruheeeee.java_tower_defense.Bullet;
import me.mr_ruheeeee.java_tower_defense.Enemy;
import me.mr_ruheeeee.java_tower_defense.TDColors;
import me.mr_ruheeeee.java_tower_defense.Vector3d;

public class ArrowTower extends Tower {

    public ArrowTower(Vector3d pos) {
        super(
                pos,
                "Arrow",
                TDColors.RED,
                140,
                10f,
                7,
                1,
                1,
                41
        );
    }

    @Override
    public void shoot(List<Enemy> enemies, List<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // pick the enemy furthest along the path that's still in range
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
                    TDColors.WHITE,
                    bulletSize,
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
            this.bulletSpeed += 0.13f;
            this.range += 3;

            if (getLevel() == 2) {
                this.bulletHealth++;
            } else if (getLevel() == 7) {
                this.bulletDamage++;
            }
            return true;
        }
        return false;
    }
}