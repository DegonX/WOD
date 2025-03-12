package com.degonx.warriorsofdeagon.Objects;

import android.os.Handler;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Mob;
import com.degonx.warriorsofdeagon.UI.GameUI;

public class Bosses extends Mobs {

    private int bossMaxMana, bossMana;
    private int charX, charY;
    private int bossAtkNum, bossTimeToAtk = 0;
    private final Handler bossHandler = new Handler();

    public Bosses(Mob mobData, int mobX, int mobY, Areas Area, GameUI gameUI) {
        super(mobData, mobX, mobY, Area, gameUI);
        bossMaxMana = bossMana = 1000 * (charLevel / 10 + 1);
    }

    private void bossWaveAttack() {
        //boss attempt to use wave attack, if fail boss will try to do normal attack
        if (mobRan.nextInt(10) + 1 > 7 && bossMana >= 1000 && bossTimeToAtk == 0) {
            //prepare wave attack and use in 5 seconds
            bossMana -= 1000;
            bossTimeToAtk = 6;
            bossAtkNum = 2;
            gameUI.setChat("boss is about to use wave attack, you have 5 seconds to escape");
            bossAttackRan.run();
        } else
            bossNormalAttack();
    }

    private void bossNormalAttack() {
        if (bossTimeToAtk == 0) {
            //get character current location
            charX = Char.getCharX();
            charY = Char.getCharY();
            bossTimeToAtk = 2;
            bossAtkNum = 1;
            bossAttackRan.run();
        }
    }

    //timer for boss attack
    private final Runnable bossAttackRan = new Runnable() {
        @Override
        public void run() {
            if (!game.Pause && Char.getCharHP() > 0 && !mobIsDead && bossTimeToAtk > 0) {
                if (--bossTimeToAtk == 0)
                    bossAttack();
                bossHandler.postDelayed(bossAttackRan, 1000);
            }
        }
    };

    private void bossAttack() {
        if (bossAtkNum == 1) {
            if (Char.getCharX() - charX < 25 && Char.getCharX() - charX > -25 && Char.getCharY() - charY < 25 && Char.getCharY() - charY > -25)
                damageChar(2);

        } else if (bossAtkNum == 2) {
            //use wave attack, damage character only if in damage area
            gameUI.setChat("boss used wave attack");
            if (Char.getCharX() > 400)
                damageChar(3);
        }
    }

    public void addMana() {
        //add random amount of mana and attempt to use wave attack
        bossMana += mobRan.nextInt(100 - 50) + 50;
        if (bossMana > bossMaxMana)
            bossMana = bossMaxMana;
        bossWaveAttack();
    }

    //set mana after respawn
    protected void respawnBoss() {
        bossMaxMana = bossMana = 1000 * (charLevel / 10 + 1);
        bossTimeToAtk = 0;
    }

    //resume boss attack timer
    protected void resumeBossAttack() {
        if (bossTimeToAtk > 0)
            bossAttackRan.run();
    }
}
