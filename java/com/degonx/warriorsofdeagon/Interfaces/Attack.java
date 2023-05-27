package com.degonx.warriorsofdeagon.Interfaces;


import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;

public interface Attack {

    int attackNumber();

    int attackDamage();

    int attackTimes();

    int attackManaCost();

    AttacksType attackType();

    ElementalEffects elementalEffect();

}
