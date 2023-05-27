package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.R;

public enum Weapons {

    Fists(0, R.drawable.redbutton, WeaponAttacks.FISTS_PUNCH, WeaponAttacks.FISTS_BULLET_PUNCH, WeaponAttacks.FISTS_MULTI_PUNCH, WeaponAttacks.FISTS_RAGE_PUNCH),
    Spear(1, R.drawable.spear, WeaponAttacks.SPEAR_SWING, WeaponAttacks.SPEAR_STAB, WeaponAttacks.SPEAR_TRIPLE_STAB, WeaponAttacks.SPEAR_WHIRL),
    Poleaxe(2, R.drawable.poleaxe, WeaponAttacks.POLEAXE_SWING, WeaponAttacks.POLEAXE_TRIPLE_STAB, WeaponAttacks.POLEAXE_SLASH_SLAM, WeaponAttacks.POLEAXE_GROUND_SMACK),
    Heavy_Sword(3, R.drawable.heavysword, WeaponAttacks.HEAVY_SWORD_HEAVY_SLASH, WeaponAttacks.HEAVY_SWORD_SLASH_SLAM, WeaponAttacks.HEAVY_SWORD_SPIN, WeaponAttacks.HEAVY_SWORD_GROUND_SMACK),
    Chokuto(4, R.drawable.chokuto, WeaponAttacks.CHOKUTO_SLASH, WeaponAttacks.CHOKUTO_STAB, WeaponAttacks.CHOKUTO_TRIPLE_SLASH, WeaponAttacks.CHOKUTO_SLASH_THROUGH),
    Daggers(5, R.drawable.daggers, WeaponAttacks.DAGGERS_SLASHES, WeaponAttacks.DAGGERS_X_SLASHES, WeaponAttacks.DAGGERS_OCTUPLE_SLASHES, WeaponAttacks.DAGGERS_SPIN_SLASHES),
    Warhammer(6, R.drawable.warhammer, WeaponAttacks.WARHAMMER_SWING, WeaponAttacks.WARHAMMER_SMASH, WeaponAttacks.WARHAMMER_SLAM, WeaponAttacks.WARHAMMER_GROUND_SMACK),
    Blood_Weapons(7, R.drawable.bloodweapons, WeaponAttacks.BLOOD_WEAPONS_FIST, WeaponAttacks.BLOOD_WEAPONS_CLAW, WeaponAttacks.BLOOD_WEAPONS_DRAIN, WeaponAttacks.BLOOD_WEAPONS_CRASH),
    Scythe(8, R.drawable.scythe, WeaponAttacks.SCYTHE_SWING, WeaponAttacks.SCYTHE_CUT, WeaponAttacks.SCYTHE_SPIN, WeaponAttacks.SCYTHE_WHIRL),
    Trident(9, R.drawable.trident, WeaponAttacks.TRIDENT_SWING, WeaponAttacks.TRIDENT_STAB, WeaponAttacks.TRIDENT_SLASH_SLAM, WeaponAttacks.TRIDENT_WHIRL),
    Double_Bladed_Naginata(10, R.drawable.doublebladednaginata, WeaponAttacks.DOUBLE_BLADED_NAGINATA_SWING, WeaponAttacks.DOUBLE_BLADED_NAGINATA_STAB, WeaponAttacks.DOUBLE_BLADED_NAGINATA_SPIN, WeaponAttacks.DOUBLE_BLADED_NAGINATA_WHIRL),
    Whip_Sword(11, R.drawable.whipsword, WeaponAttacks.WHIP_SWORD_WHIPSLASH, WeaponAttacks.WHIP_SWORD_FAR_STAB, WeaponAttacks.WHIP_SWORD_WHIP_AWAY, WeaponAttacks.WHIP_SWORD_SLASH_TORNADO),
    Bow(12, R.drawable.bow, WeaponAttacks.BOW_ARROW, WeaponAttacks.BOW_POWER_ARROW, WeaponAttacks.BOW_QUADRUPLE_ARROW, WeaponAttacks.BOW_ARROWS_RAIN),
    Staff(13, R.drawable.staff, WeaponAttacks.STAFF_DRAIN, WeaponAttacks.STAFF_SMACK, WeaponAttacks.STAFF_MAGIC_SPHERES, WeaponAttacks.STAFF_MAGIC_ARROWS);

    final public int weaponTypeNum;
    final public int weaponIcon;
    final public WeaponAttacks[] Attacks = new WeaponAttacks[4];

    Weapons(int weaponTypeNum, int weaponIcon, WeaponAttacks attack1, WeaponAttacks attack2, WeaponAttacks attack3, WeaponAttacks attack4) {
        this.weaponTypeNum = weaponTypeNum;
        this.weaponIcon = weaponIcon;
        Attacks[0] = attack1;
        Attacks[1] = attack2;
        Attacks[2] = attack3;
        Attacks[3] = attack4;
    }
}
