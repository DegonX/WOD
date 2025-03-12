package com.degonx.warriorsofdeagon.Enums.AreaEnums;

import com.degonx.warriorsofdeagon.R;

public enum Mob {

    Mob1_Dungeon1(R.drawable.mob1, "normal", 1, 1, 15, 10, 2),
    Mob2_Dungeon2(R.drawable.mob2, "normal", 4, 3, 35, 25, 2),
    Boss1_Dungeon10(R.drawable.boss1, "boss", 7, 5, 60, 45, 0);


    final int mobImage;
    final String mobType;
    final int mobBaseHP;
    final int mobBaseXP;
    final int mobBaseAtk;
    final int mobBaseDef;
    final int mobBaseSpeed;

    Mob(int mobImage, String mobType, int mobBaseHP, int mobBaseXP, int mobBaseAtk, int mobBaseDef, int mobBaseSpeed) {
        this.mobImage = mobImage;
        this.mobType = mobType;
        this.mobBaseHP = mobBaseHP;
        this.mobBaseXP = mobBaseXP;
        this.mobBaseAtk = mobBaseAtk;
        this.mobBaseDef = mobBaseDef;
        this.mobBaseSpeed = mobBaseSpeed;
    }

    public int getMobImage() {
        return mobImage;
    }

    public String getMobType() {
        return mobType;
    }

    public int getMobBaseHP() {
        return mobBaseHP;
    }

    public int getMobBaseXP() {
        return mobBaseXP;
    }

    public int getMobBaseAtk() {
        return mobBaseAtk;
    }

    public int getMobBaseDef() {
        return mobBaseDef;
    }

    public int getMobBaseSpeed() {
        return mobBaseSpeed;
    }
}
