package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.AreaEffects;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Interfaces.Attack;

public enum ElementAttacks implements Attack {

    FIRE_PHOENIX(1, "Phoenix", 280, 150, 1, AttacksType.Homing, ElementalEffects.Burn, AreaEffects.None, 4),
    FIRE_RUSH(2, "Rush", 350, 200, 1, AttacksType.ShortFrontAndPush, ElementalEffects.Burn, AreaEffects.None, 7),
    FIRE_FIRE_BLAST(3, "Fire Blast", 470, 300, 1, AttacksType.MidSurroundAndPush, ElementalEffects.Burn, AreaEffects.Overheat, 40),

    ICE_ICICLES(1, "Icicles", 50, 150, 5, AttacksType.Homing, ElementalEffects.Freeze, AreaEffects.None, 4),
    ICE_ICE_STARS(2, "Ice Stars", 350, 200, 1, AttacksType.FarFront, ElementalEffects.Freeze, AreaEffects.None, 7),
    ICE_ICEBERG(3, "Iceberg", 430, 300, 1, AttacksType.FullAreaAttack, ElementalEffects.Freeze, AreaEffects.None, 20),

    ENERGY_SPHERE(1, "Sphere", 300, 150, 1, AttacksType.Homing, ElementalEffects.None, AreaEffects.None, 4),
    ENERGY_ENERGY_BEAM(2, "Energy Beam", 350, 200, 1, AttacksType.FarFront, ElementalEffects.None, AreaEffects.None, 8),
    ENERGY_ENERGY_BLAST(3, "Energy Blast", 450, 300, 1, AttacksType.MidSurroundAndPush, ElementalEffects.None, AreaEffects.None, 25),

    BLOOD_BLOOD_SPEARS(1, "Blood Spears", 60, 150, 5, AttacksType.Homing, ElementalEffects.None, AreaEffects.None, 4),
    BLOOD_BLOOD_CHAINS(2, "Blood Chains", 120, 200, 3, AttacksType.FarFront, ElementalEffects.None, AreaEffects.None, 8),
    BLOOD_NEEDLES_RAIN(3, "Needles Rain", 50, 300, 10, AttacksType.FullAreaAttack, ElementalEffects.None, AreaEffects.None, 25),

    DARKNESS_RIPER_HANDS(1, "Riper Hands", 300, 150, 1, AttacksType.MidSurround, ElementalEffects.None, AreaEffects.None, 5),
    DARKNESS_FEEDING(2, "Feeding", 350, 200, 1, AttacksType.FullAreaAttack, ElementalEffects.None, AreaEffects.None, 8),
    DARKNESS_DARK_BEAM(3, "Dark Beam", 500, 300, 1, AttacksType.FullAreaAttack, ElementalEffects.None, AreaEffects.DarknessFog, 40),

    LIGHTNING_LIGHTNING_SPEAR(1, "Lightning Spear", 250, 150, 1, AttacksType.FarFront, ElementalEffects.Paralyze, AreaEffects.None, 5),
    LIGHTNING_JOLTS(2, "Jolts", 320, 200, 1, AttacksType.MidSurround, ElementalEffects.Paralyze, AreaEffects.None, 8),
    LIGHTNING_LIGHTNING_STORM(3, "Lightning Storm", 400, 300, 1, AttacksType.FullAreaAttack, ElementalEffects.Paralyze, AreaEffects.LightningStorm, 35),

    EARTH_ROCK_BULLETS(1, "Rock Bullets", 45, 150, 5, AttacksType.FarFront, ElementalEffects.None, AreaEffects.None, 3),
    EARTH_PILLARS(2, "Pillars", 120, 200, 3, AttacksType.MidSurround, ElementalEffects.None, AreaEffects.None, 7),
    EARTH_METEOR(3, "Meteor", 480, 300, 1, AttacksType.FullAreaAttack, ElementalEffects.None, AreaEffects.None, 20),

    WIND_WIND_SLASHES(1, "Wind Slashes", 80, 150, 3, AttacksType.FarFront, ElementalEffects.None, AreaEffects.None, 3),
    WIND_WHIRLWIND(2, "Whirlwind", 300, 200, 1, AttacksType.MidSurroundAndPush, ElementalEffects.None, AreaEffects.Whirlwind, 30),
    WIND_TORNADO(3, "Tornado", 400, 300, 1, AttacksType.FullAreaAttack, ElementalEffects.None, AreaEffects.Tornado, 45);

    public final int attackNumber;
    public final String attackName;
    public final int attackDamage;
    public final int attackManaCost;
    public final int attackTimes;
    public final AttacksType attackType;
    public final ElementalEffects elementalEffect;
    public final AreaEffects areaEffect;
    public final int attackCooldown;
    public static int bonusDamage;

    ElementAttacks(int attackNumber, String attackName, int attackDamage, int attackManaCost, int attackTimes, AttacksType attackType, ElementalEffects elementalEffect, AreaEffects areaEffect, int attackCooldown) {
        this.attackNumber = attackNumber;
        this.attackName = attackName;
        this.attackDamage = attackDamage;
        this.attackManaCost = attackManaCost;
        this.attackTimes = attackTimes;
        this.attackType = attackType;
        this.elementalEffect = elementalEffect;
        this.areaEffect = areaEffect;
        this.attackCooldown = attackCooldown;
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
