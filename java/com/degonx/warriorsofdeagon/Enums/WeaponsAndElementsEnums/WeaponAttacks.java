package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.WeaponEffects;
import com.degonx.warriorsofdeagon.Interfaces.Attack;

public enum WeaponAttacks implements Attack {

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
    BLOOD_WEAPONS_CRASH(4, "Crash", 200, 120, 1, AttacksType.SingleFarFrontAndPull, ElementalEffects.None, WeaponEffects.None),

    SCYTHE_SWING(1, "Swing", 30, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    SCYTHE_CUT(2, "Cut", 80, 35, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    SCYTHE_SPIN(3, "Spin", 30, 100, 5, AttacksType.ShortSurround, ElementalEffects.Chance, WeaponEffects.None),
    SCYTHE_WHIRL(4, "Whirl", 20, 120, 10, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    TRIDENT_SWING(1, "Swing", 30, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    TRIDENT_STAB(2, "Stab", 70, 30, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    TRIDENT_SLASH_SLAM(3, "Slash Slam", 60, 65, 2, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    TRIDENT_WHIRL(4, "Whirl", 19, 110, 10, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    DOUBLE_BLADED_NAGINATA_SWING(1, "Swing", 25, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    DOUBLE_BLADED_NAGINATA_STAB(2, "Stab", 50, 20, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    DOUBLE_BLADED_NAGINATA_SPIN(3, "Spin", 20, 50, 5, AttacksType.ShortSurround, ElementalEffects.Chance, WeaponEffects.None),
    DOUBLE_BLADED_NAGINATA_WHIRL(4, "Whirl", 18, 100, 10, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    WHIP_SWORD_WHIPSLASH(1, "Whip Slash", 25, -10, 1, AttacksType.MidFront, ElementalEffects.Chance, WeaponEffects.None),
    WHIP_SWORD_FAR_STAB(2, "Far Stab", 50, 35, 1, AttacksType.MidFront, ElementalEffects.Chance, WeaponEffects.None),
    WHIP_SWORD_WHIP_AWAY(3, "Whip Away", 55, 70, 2, AttacksType.MidFrontAndPush, ElementalEffects.Chance, WeaponEffects.None),
    WHIP_SWORD_SLASH_TORNADO(4, "Slash Tornado", 20, 100, 8, AttacksType.MidSurround, ElementalEffects.Chance, WeaponEffects.None),

    BOW_ARROW(1, "Arrow", 20, -10, 1, AttacksType.SingleMidFront, ElementalEffects.Chance, WeaponEffects.None),
    BOW_POWER_ARROW(2, "Power Arrow", 60, 30, 1, AttacksType.FarFront, ElementalEffects.Chance, WeaponEffects.None),
    BOW_QUADRUPLE_ARROW(3, "Quadruple Arrow", 30, 60, 4, AttacksType.SingleFarFront, ElementalEffects.Chance, WeaponEffects.None),
    BOW_ARROWS_RAIN(4, "Arrows Rain", 30, 100, 6, AttacksType.FullAreaAttack, ElementalEffects.Chance, WeaponEffects.None),

    STAFF_DRAIN(1, "Drain", 20, -30, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    STAFF_SMACK(2, "Smack", 50, 10, 1, AttacksType.ShortFront, ElementalEffects.Chance, WeaponEffects.None),
    STAFF_MAGIC_SPHERES(3, "Magic Spheres", 35, 40, 4, AttacksType.FarFront, ElementalEffects.Chance, WeaponEffects.None),
    STAFF_MAGIC_ARROWS(4, "Magic Arrows", 40, 100, 5, AttacksType.FullAreaAttack, ElementalEffects.Chance, WeaponEffects.None),

    FISTS_PUNCH(1, "Punch", 40, -10, 1, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    FISTS_BULLET_PUNCH(2, "Bullet Punch", 90, 60, 1, AttacksType.SingleShortFrontAndPush, ElementalEffects.Chance, WeaponEffects.None),
    FISTS_MULTI_PUNCH(3, "Multi Punch", 35, 80, 4, AttacksType.SingleShortFront, ElementalEffects.Chance, WeaponEffects.None),
    FISTS_RAGE_PUNCH(4, "Rage Punch", 200, 120, 1, AttacksType.ShortFrontAndPush, ElementalEffects.Chance, WeaponEffects.None);

    public final int attackNumber;
    public final String attackName;
    public final int attackDamage;
    public final int attackManaCost;
    public final int attackTimes;
    public final AttacksType attackType;
    public final ElementalEffects elementalEffect;
    public final WeaponEffects weaponEffect;
    public static int bonusDamage;

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
    public int attackNumber() {
        return attackNumber;
    }

    @Override
    public int attackDamage() {
        return attackDamage + bonusDamage;
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
