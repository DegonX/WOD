package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.R;

public enum Weapons {

    Fists(0, R.drawable.redbutton, SkillsEnum.FISTERS_RAGE, WeaponAttacks.FISTS_PUNCH, WeaponAttacks.FISTS_BULLET_PUNCH, WeaponAttacks.FISTS_MULTI_PUNCH, WeaponAttacks.FISTS_RAGE_PUNCH),
    Spear(1, R.drawable.spear, SkillsEnum.SPEAR_MASTERY, WeaponAttacks.SPEAR_SWING, WeaponAttacks.SPEAR_STAB, WeaponAttacks.SPEAR_TRIPLE_STAB, WeaponAttacks.SPEAR_WHIRL),
    Poleaxe(2, R.drawable.poleaxe, SkillsEnum.POLEAXE_MASTERY, WeaponAttacks.POLEAXE_SWING, WeaponAttacks.POLEAXE_TRIPLE_STAB, WeaponAttacks.POLEAXE_SLASH_SLAM, WeaponAttacks.POLEAXE_GROUND_SMACK),
    Heavy_Sword(3, R.drawable.heavysword, SkillsEnum.HEAVY_SWORD_MASTERY, WeaponAttacks.HEAVY_SWORD_HEAVY_SLASH, WeaponAttacks.HEAVY_SWORD_SLASH_SLAM, WeaponAttacks.HEAVY_SWORD_SPIN, WeaponAttacks.HEAVY_SWORD_GROUND_SMACK),
    Chokuto(4, R.drawable.chokuto, SkillsEnum.CHOKUTO_MASTERY, WeaponAttacks.CHOKUTO_SLASH, WeaponAttacks.CHOKUTO_STAB, WeaponAttacks.CHOKUTO_TRIPLE_SLASH, WeaponAttacks.CHOKUTO_SLASH_THROUGH),
    Daggers(5, R.drawable.daggers, SkillsEnum.DAGGERS_MASTERY, WeaponAttacks.DAGGERS_SLASHES, WeaponAttacks.DAGGERS_X_SLASHES, WeaponAttacks.DAGGERS_OCTUPLE_SLASHES, WeaponAttacks.DAGGERS_SPIN_SLASHES),
   Blood_Weapons(6, R.drawable.bloodweapons, SkillsEnum.BLOOD_WEAPONS_MASTERY, WeaponAttacks.BLOOD_WEAPONS_FIST, WeaponAttacks.BLOOD_WEAPONS_CLAW, WeaponAttacks.BLOOD_WEAPONS_DRAIN, WeaponAttacks.BLOOD_WEAPONS_CRASH);

    final public int weaponNum;
    final public int weaponIcon;
    final public SkillsEnum weaponSkill;
    final public WeaponAttacks[] Attacks = new WeaponAttacks[4];

    Weapons(int weaponNum, int weaponIcon, SkillsEnum weaponSkill, WeaponAttacks attack1, WeaponAttacks attack2, WeaponAttacks attack3, WeaponAttacks attack4) {
        this.weaponNum = weaponNum;
        this.weaponIcon = weaponIcon;
        this.weaponSkill = weaponSkill;
        Attacks[0] = attack1;
        Attacks[1] = attack2;
        Attacks[2] = attack3;
        Attacks[3] = attack4;
    }
}
