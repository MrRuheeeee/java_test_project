package me.mr_ruheeeee.java_tower_defense.towers;

import java.util.List;

import me.mr_ruheeeee.java_tower_defense.Bullet;
import me.mr_ruheeeee.java_tower_defense.Enemy;
import me.mr_ruheeeee.java_tower_defense.TDColors;
import me.mr_ruheeeee.java_tower_defense.Vector3d;

public class SuperTower extends Tower {

    public SuperTower(Vector3d pos) {
        super(
                pos,
                "Super",
                TDColors.GREEN,
                100,
                4.5f,
                5,
                1,
                1,
                15
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
                    TDColors.BLUE,
                    this.bulletSize,
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
            this.cooldownTime -= 1;
            innerPaint.setARGB(255, innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 0.35f;
            this.range += 1;

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