package com.degonx.warriorsofdeagon.Objects;


public class Equipments {

    public int equipmentPK;
    public int equipID;
    public int equipSpot;
    public int equipUPT;
    public int equipType;
    public int equipHP;
    public int equipMP;
    public int equipDMG;
    public int equipDEF;

    public Equipments(int equipmentPK, int equipID, int equipSpot, int equipUPT, int equipHP, int equipMP, int equipDMG, int equipDEF) {
        this.equipmentPK = equipmentPK;
        this.equipID = equipID;
        this.equipSpot = equipSpot;
        this.equipUPT = equipUPT;
        this.equipHP = equipHP;
        this.equipMP = equipMP;
        this.equipDMG = equipDMG;
        this.equipDEF = equipDEF;
        if (equipID > 100)
            equipType = equipID - 100;
        else
            equipType = equipID;
    }
}