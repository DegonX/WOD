package com.degonx.warriorsofdeagon.Enums.EffectsEnums;

public enum ElementalEffects {
    None(0),
    Burn(1),
    Freeze(2),
    Chance(3);

    private final int effectNum;

    ElementalEffects(int effectNum) {
        this.effectNum = effectNum;
    }

    public int getEffectNum() {
        return effectNum;
    }
}
