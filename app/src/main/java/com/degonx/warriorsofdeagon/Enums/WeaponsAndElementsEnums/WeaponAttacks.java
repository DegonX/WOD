package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.WeaponEffects;
import com.degonx.warriorsofdeagon.Interfaces.Attack;

public enum WeaponAttacks implements Attack {

    FISTS_PUNCH(1, "Punch", 40, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    FISTS_BULLET_PUNCH(2, "Bullet Punch", 90, 60, 1, AttacksType.SingleShortFrontAndPush, ElementalEffects.Chance, WeaponEffects.None),
    FISTS_MULTI_PUNCH(3, "Multi Punch", 35, 80, 4, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    FISTS_RAGE_PUNCH(4, "Rage Punch", 200, 120, 1, AttacksType.ShortFrontAndPush, ElementalEffects.Chance, WeaponEffects.None),

    SPEAR_SWING(1, "Swing", 30, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    SPEAR_STAB(2, "Stab", 70, 30, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    SPEAR_TRIPLE_STAB(3, "Triple Stab", 40, 60, 3, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    SPEAR_WHIRL(4, "Whirl", 20, 120, 10, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    POLEAXE_SWING(1, "Swing", 30, -10, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    POLEAXE_TRIPLE_STAB(2, "Triple Stab", 30, 50, 3, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    POLEAXE_SLASH_SLAM(3, "Slash Slam", 75, 85, 2, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    POLEAXE_GROUND_SMACK(4, "Ground Smack", 200, 120, 1, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    HEAVY_SWORD_HEAVY_SLASH(1, "Heavy Slash", 35, -10, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    HEAVY_SWORD_SLASH_SLAM(2, "Slash Slam", 45, 45, 2, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    HEAVY_SWORD_SPIN(3, "Spin", 30, 100, 5, AttacksType.ShortSurround, ElementalEffects.Chance, WeaponEffects.None),
    HEAVY_SWORD_GROUND_SMACK(4, "Ground Smack", 200, 120, 1, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    CHOKUTO_SLASH(1, "Slash", 20, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    CHOKUTO_STAB(2, "Stab", 50, 20, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    CHOKUTO_TRIPLE_SLASH(3, "Triple Slash", 40, 60, 3, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    CHOKUTO_SLASH_THROUGH(4, "Slash Through", 170, 80, 1, AttacksType.ShortFrontAndMove, ElementalEffects.Chance, WeaponEffects.None),

    DAGGERS_SLASHES(1, "Slashes", 10, -10, 2, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    DAGGERS_X_SLASHES(2, "X Slashes", 30, 40, 2, AttacksType.ShortFrontAndMove, ElementalEffects.Chance, WeaponEffects.None),
    DAGGERS_OCTUPLE_SLASHES(3, "Octuple Slashes", 15, 80, 8, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    DAGGERS_SPIN_SLASHES(4, "Spin Slashes", 30, 90, 6, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    WARHAMMER_SWING(1, "Swing", 35, -5, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    WARHAMMER_SMASH(2, "Smash", 80, 40, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    WARHAMMER_SLAM(3, "Slam", 140, 70, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    WARHAMMER_GROUND_SMACK(4, "Ground Smack", 190, 110, 1, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    BLOOD_WEAPONS_FIST(1, "Fist", 40, -10, 1, AttacksType.SingleShortFront, ElementalEffects.None, WeaponEffects.None),
    BLOOD_WEAPONS_CLAW(2, "Claw", 90, 50, 1, AttacksType.ShortFront, ElementalEffects.None, WeaponEffects.None),
    BLOOD_WEAPONS_DRAIN(3, "Drain", 150, 90, 1, AttacksType.SingleShortFront, ElementalEffects.None, WeaponEffects.Drain),
    BLOOD_WEAPONS_CRASH(4, "Crash", 200, 120, 1, AttacksType.SingleFarFrontAndPull, ElementalEffects.None, WeaponEffects.None);

    public final int attackNumber;
    public final String attackName;
    public final int attackDamage;
    public final int attackManaCost;
    public final int attackTimes;
    public final AttacksType attackType;
    public final ElementalEffects elementalEffect;
    public final WeaponEffects weaponEffect;
    public static float bonusDamage;
    public static int weaponDamage;

    WeaponAttacks(int attackNumber, String attackName, int attackDamage, int attackManaCost, int attackTimes, AttacksType attackType, ElementalEffects elementalEffect, WeaponEffects weaponEffect) {
        this.attackNumber = attackNumber;
        this.attackName = attackName;
        this.attackDamage = attackDamage;
        this.attackManaCost = attackManaCost;
        this.attackTimes = attackTimes;
        this.attackType = attackType;
        this.elementalEffect = elementalEffect;
        this.weaponEffect = weaponEffect;
    }

    @Override
    public int attackDamage() {
        return (int) ((attackDamage * bonusDamage) * (attackNumber + (0.01 * weaponDamage)));
    }

    @Override
    public int attackTimes() {
        return attackTimes;
    }

    @Override
    public int attackManaCost() {
        return attackManaCost;
    }

    @Override
    public AttacksType attackType() {
        return attackType;
    }

    @Override
    public ElementalEffects elementalEffect() {
        return elementalEffect;
    }
}
