package com.degonx.warriorsofdeagon.Actions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksAction;
import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksCount;
import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksDirection;
import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksRanges;
import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.MobsType;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.WeaponEffects;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.WeaponAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Interfaces.Attack;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;

import java.util.List;
import java.util.Random;

public class AttacksActions {

    private static final Random Ran = new Random();

    //check if character facing target mob
    public static boolean checkCharFront(Character Char, Mobs m) {
        return Char.getCharSide() == 1 && m.getMobX() > Char.getCharX() ||
                Char.getCharSide() == -1 && m.getMobX() < Char.getCharX();
    }

    //check mob Y is the character attack range
    private static boolean checkYRange(Character Char, AttacksType attack, Mobs m) {

        //short range
        if (attack.getAttacksRange() != AttacksRanges.MID && attack.getAttackDirection() != AttacksDirection.SURROUND)
            return Char.getCharY() < m.getMobY() + (m.getMobHeight() * 0.7) && Char.getCharY() + (Char.getCharHeight() * 0.2) > m.getMobY();

            //mid surround range
        else
            return Char.getCharY() < m.getMobY() + (m.getMobHeight() * 0.9) && Char.getCharY() + (Char.getCharHeight() * 0.5) > m.getMobY();
    }

    //check mob X is the character attack range
    private static boolean checkXRange(Character Char, AttacksType attack, Mobs m) {

        //short range
        if (attack.getAttacksRange() == AttacksRanges.SHORT)
            return Char.getCharX() + Char.getCharWidth() + 35 > m.getMobX() && Char.getCharX() - 35 < m.getMobX() + m.getMobWidth();

            //mid range
        else if (attack.getAttacksRange() == AttacksRanges.MID)
            return Char.getCharX() + Char.getCharWidth() + 185 > m.getMobX() && Char.getCharX() - 185 < m.getMobX() + m.getMobWidth();

            //far range
        else
            return Char.getCharX() + Char.getCharWidth() + 710 > m.getMobX() && Char.getCharX() - 710 < m.getMobX() + m.getMobWidth();
    }

    //check if mob in the range of the attack
    public static void rangeChecker(Attack attack, Elements charEle, Character Char, List<Mobs> mobs) {

        if (attack.attackManaCost() > 0 && Char.getGMSkill(1) != 1)
            Char.addCharMP(-attack.attackManaCost());

        //check attack range
        for (Mobs m : mobs) {
            if (!m.isMobIsDead()) {

                //check if mobs are in range of the attack
                if (attack.attackType().getAttackDirection() == AttacksDirection.FRONT && checkCharFront(Char, m) && checkXRange(Char, attack.attackType(), m) && checkYRange(Char, attack.attackType(), m) ||
                        attack.attackType().getAttackDirection() == AttacksDirection.SURROUND && checkXRange(Char, attack.attackType(), m) && checkYRange(Char, attack.attackType(), m) ||
                        attack.attackType() == AttacksType.FullAreaAttack) {
                    damageEnhance(attack, Char, m, charEle);

                    //move mobs (push \ pull attack)
                    if (attack.attackType().getAttackAction() == AttacksAction.PULL)
                        moveMob(Char, false, m);
                    else if (attack.attackType().getAttackAction() == AttacksAction.PUSH_BACK)
                        moveMob(Char, true, m);

                    //single attack
                    if (attack.attackType().getAttackCount() == AttacksCount.SINGLE)
                        break;
                }
            }
        }

        //move character (move through attack)
        if (attack.attackType().getAttackAction() == AttacksAction.MOVE_THROUGH)
            moveChar(Char);
    }

    private static void moveMob(Character Char, boolean push, Mobs m) {
        int newX = 0;
        int newY = 0;


        //push mob
        if (push) {

            //push mob right\left
            if (Char.getCharX() + (Char.getCharWidth() * 0.6) < m.getMobX())
                newX = 110;
            else if (Char.getCharX() > m.getMobX() + (m.getMobWidth()) * 0.6)
                newX = -110;

            //push mob up\down
            if (Char.getCharY() + (Char.getCharHeight() * 0.6) < (m.getMobY() + m.getMobY() * 1.3))
                newY = 80;
            else if (Char.getCharY() > m.getMobY() + (m.getMobHeight() * 0.6))
                newY = -80;

            m.moveMob(newX, newY);
        } else {

            //pull mob to character
            newX = Char.getCharX() - 250;
            if (Char.getCharX() + (Char.getCharWidth() / 2) < m.getMobX())
                newX = Char.getCharX() + 250;

            m.setMobXY(newX, -1);
        }
    }

    //move character forward when used move through attack
    private static void moveChar(Character Char) {
        int newX = Char.getCharX() + Char.getCharWidth() + 170;
        if (Char.getCharSide() == -1)
            newX = Char.getCharX() - Char.getCharWidth() - 170;

        Char.setCharXY(newX, -1);
        Char.changeCharSide();
    }

    //increase or decrease damage by char element and mob type
    private static double changeDamageByType(Elements charEle, MobsType mobType) {

        Log.i(TAG, "Char Element: " + charEle + ", Mob Type: " + mobType);

        if (charEle == Elements.Fire && mobType == MobsType.Darkness || charEle == Elements.Fire && mobType == MobsType.Metal || charEle == Elements.Ice && mobType == MobsType.Water ||
                charEle == Elements.Energy && mobType == MobsType.Earth || charEle == Elements.Energy && mobType == MobsType.Lightning || charEle == Elements.Energy && mobType == MobsType.Light ||
                charEle == Elements.Blood && mobType == MobsType.Water || charEle == Elements.Blood && mobType == MobsType.Earth || charEle == Elements.Darkness && mobType == MobsType.Lightning ||
                charEle == Elements.Darkness && mobType == MobsType.Light)
            return 1.25;
        else if (charEle == Elements.Fire && mobType == MobsType.Fire || charEle == Elements.Fire && mobType == MobsType.Water || charEle == Elements.Fire && mobType == MobsType.Earth ||
                charEle == Elements.Ice && mobType == MobsType.Fire || charEle == Elements.Ice && mobType == MobsType.Lightning || charEle == Elements.Ice && mobType == MobsType.Metal ||
                charEle == Elements.Energy && mobType == MobsType.Darkness || charEle == Elements.Blood && mobType == MobsType.Fire || charEle == Elements.Blood && mobType == MobsType.Lightning ||
                charEle == Elements.Blood && mobType == MobsType.Metal || charEle == Elements.Darkness && mobType == MobsType.Fire || charEle == Elements.Darkness && mobType == MobsType.Darkness)
            return 0.75;
        return 1;
    }

    //improve the damage
    public static void damageEnhance(Attack attack, Character Char, Mobs m, Elements charEle) {

        int damage = (int) (attack.attackDamage() * Char.getBlessDamage());

        //recharge MP
        if (attack.attackManaCost() < 0)
            Char.addCharMP(-attack.attackManaCost());

        //change damage if character uses attack with element (not a skill)
        if (charEle != Elements.NONE)
            damage *= changeDamageByType(charEle, m.getMobType());

        //if char use blood weapons
        if (Char.getCharCurrentWeaponType() == Weapons.Blood_Weapons)
            damage += Char.getPassiveSkillsAdd(5);

        //chance to attack once more
        int atktimes = attack.attackTimes();

        if (Ran.nextInt(100) + 1 < Char.getPassiveSkillsAdd(4))
            atktimes++;

        mobTakeDamage(damage, atktimes, attack, Char, m);
    }

    //rises or reduce character damage by mob defense
    private static double changeDamageByDefense(double charDamage, int mobDefense) {
        double damage = charDamage / mobDefense;
        if (damage > 1.3)
            damage = 1.3;
        else if (damage < 0.8)
            damage = 0.8;
        return damage;
    }

    //attack mob
    private static void mobTakeDamage(int damage, int atktimes, Attack attack, Character Char, Mobs m) {
        double finalDamage;

        //attack the amount of atktimes
        for (int at = 0; at < atktimes; at++) {

            //add character damage and increases\decreases character final damage by mob defense
            finalDamage = (damage + Char.statsMixer(1)) * (Ran.nextDouble() * (1.3 - 0.8) + 0.8) * changeDamageByDefense(damage, m.getMobDefense());

            //attempt to do critical damage and show damage above the mob
            if (Ran.nextInt(100) + 1 <= Char.criticalRateMixer()) {
                finalDamage *= 1.5 + (double) Char.statsMixer(5) / 100;
                Char.getGameUI().damageMobText(finalDamage, true, m, at);
            } else
                Char.getGameUI().damageMobText(finalDamage, false, m, at);

            //reduce mobHP by damage
            m.removeMobHP((int) finalDamage);

            //blood drain - heal HP
            if (attack.getClass() == WeaponAttacks.class && ((WeaponAttacks) attack).weaponEffect == WeaponEffects.Drain)
                Char.addCharHP((int) (finalDamage / 80));

            //stop attack if mob died
            if (m.getMobHP() <= 0)
                break;
        }

        //collect lord power for every hit mob getting
        Char.addLordPoints();

        //check attack result
        m.mobAttackedResult();

        //if mob still alive after attack
        if (!m.isMobIsDead())
            m.mobAttacked(attack.elementalEffect());
    }

    public static void executeAttackSkill(int damage, int manaCost, int atktimes, Character Char, List<Mobs> mobs) {
        //reduce mana
        Char.addCharMP(-manaCost);

        for (Mobs m : mobs) {
            //attack the amount atktimes
            for (int at = 0; at < atktimes; at++) {

                //improve skill attack damage
                int finalDamage = (int) (damage * (Ran.nextDouble() * (1.5 - 0.9) + 0.9)) + Char.getCharDamage() * 3;

                //show damage above the mob
                Char.getGameUI().damageMobText(finalDamage, false, m, at);

                //reduce mobHP by damage
                m.removeMobHP(finalDamage);

                //stop attack if mob died
                if (m.getMobHP() <= 0)
                    break;
            }
            //check attack result
            m.mobAttackedResult();
        }
    }
}
