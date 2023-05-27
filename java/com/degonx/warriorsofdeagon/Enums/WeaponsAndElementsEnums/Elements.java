package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.R;

public enum Elements {
    NONE(0, 0, null, null, null),
    Fire(1, R.drawable.fire, ElementAttacks.FIRE_PHOENIX, ElementAttacks.FIRE_RUSH, ElementAttacks.FIRE_FIRE_BLAST),
    Ice(2, R.drawable.ice, ElementAttacks.ICE_ICICLES, ElementAttacks.ICE_ICE_STARS, ElementAttacks.ICE_ICEBERG),
    Energy(3, R.drawable.energy, ElementAttacks.ENERGY_SPHERE, ElementAttacks.ENERGY_ENERGY_BEAM, ElementAttacks.ENERGY_ENERGY_BLAST),
    Blood(4, R.drawable.blood, ElementAttacks.BLOOD_BLOOD_SPEARS, ElementAttacks.BLOOD_BLOOD_CHAINS, ElementAttacks.BLOOD_NEEDLES_RAIN),
    Darkness(5, R.drawable.darkness, ElementAttacks.DARKNESS_RIPER_HANDS, ElementAttacks.DARKNESS_FEEDING, ElementAttacks.DARKNESS_DARK_BEAM),
    Lightning(6, R.drawable.lightning, ElementAttacks.LIGHTNING_LIGHTNING_SPEAR, ElementAttacks.LIGHTNING_JOLTS, ElementAttacks.LIGHTNING_LIGHTNING_STORM),
    Earth(7, R.drawable.earth, ElementAttacks.EARTH_ROCK_BULLETS, ElementAttacks.EARTH_PILLARS, ElementAttacks.EARTH_METEOR),
    Wind(8, R.drawable.wind, ElementAttacks.WIND_WIND_SLASHES, ElementAttacks.WIND_WHIRLWIND, ElementAttacks.WIND_TORNADO);

    final public int elementTypeNum;
    final public int elementIcon;
    final public ElementAttacks[] Attacks = new ElementAttacks[3];

    Elements(int elementTypeNum, int elementIcon, ElementAttacks attack1, ElementAttacks attack2, ElementAttacks attack3) {
        this.elementTypeNum = elementTypeNum;
        this.elementIcon = elementIcon;
        Attacks[0] = attack1;
        Attacks[1] = attack2;
        Attacks[2] = attack3;
    }
}
