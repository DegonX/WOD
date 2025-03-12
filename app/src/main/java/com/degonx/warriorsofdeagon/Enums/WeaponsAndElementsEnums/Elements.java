package com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.R;

public enum Elements {
    NONE(0, 0, null, null, null, null),
    Fire(1, R.drawable.fire, SkillsEnum.FIRE_KNOWLEDGE, ElementAttacks.FIRE_PHOENIX, ElementAttacks.FIRE_RUSH, ElementAttacks.FIRE_FIRE_BLAST),
    Ice(2, R.drawable.ice, SkillsEnum.ICE_KNOWLEDGE, ElementAttacks.ICE_ICICLES, ElementAttacks.ICE_ICE_STARS, ElementAttacks.ICE_ICEBERG),
    Energy(3, R.drawable.energy, SkillsEnum.ENERGY_KNOWLEDGE, ElementAttacks.ENERGY_SPHERE, ElementAttacks.ENERGY_ENERGY_BEAM, ElementAttacks.ENERGY_ENERGY_BLAST),
    Blood(4, R.drawable.blood, SkillsEnum.BLOOD_KNOWLEDGE, ElementAttacks.BLOOD_BLOOD_SPEARS, ElementAttacks.BLOOD_BLOOD_CHAINS, ElementAttacks.BLOOD_NEEDLES_RAIN),
    Darkness(5, R.drawable.darkness, SkillsEnum.DARKNESS_KNOWLEDGE, ElementAttacks.DARKNESS_RIPER_HANDS, ElementAttacks.DARKNESS_FEEDING, ElementAttacks.DARKNESS_DARK_BEAM);
    final public int elementNum;
    final public int elementIcon;
    final public SkillsEnum elementSkill;
    final public ElementAttacks[] Attacks = new ElementAttacks[3];

    Elements(int elementNum, int elementIcon, SkillsEnum elementSkill, ElementAttacks attack1, ElementAttacks attack2, ElementAttacks attack3) {
        this.elementNum = elementNum;
        this.elementIcon = elementIcon;
        this.elementSkill = elementSkill;
        Attacks[0] = attack1;
        Attacks[1] = attack2;
        Attacks[2] = attack3;
    }
}
