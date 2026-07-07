package me.mr_ruheeeee.java_tower_defense.towers;

import java.util.List;

import me.mr_ruheeeee.java_tower_defense.Bullet;
import me.mr_ruheeeee.java_tower_defense.Enemy;
import me.mr_ruheeeee.java_tower_defense.TDColors;
import me.mr_ruheeeee.java_tower_defense.Vector3d;

public class MagicTower extends Tower {

    public MagicTower(Vector3d pos) {
        super(
                pos,
                "Magic",
                TDColors.BLUE,
                115,
                6.7f,
                15,
                3,
                3,
                160
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
                    TDColors.WHITE,
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
            innerPaint.setARGB(255, innerColorRGB, innerColorRGB, innerColorRGB);
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