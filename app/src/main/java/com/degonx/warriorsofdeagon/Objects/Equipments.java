package com.degonx.warriorsofdeagon.Objects;


import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEffects;
import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEnum;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;

public class Equipments {

    public int equipmentPK;
    public String equipmentID;
    public String equipmentType;
    public int equipmentEquippedSlot;
    public int equipmentInventorySlot;
    public int equipmentUpgradeTimes;
    public int equipmentHP;
    public int equipmentMP;
    public int equipmentDMG;
    public int equipmentDEF;
    public EquipmentsEffects equipmentEffect;

    public Equipments(int equipmentPK, String equipmentID, int equipmentEquippedSlot, int equipmentInventorySlot, int equipmentUpgradeTimes, int equipmentHP, int equipmentMP, int equipmentDMG, int equipmentDEF) {
        this.equipmentPK = equipmentPK;
        this.equipmentID = equipmentID;
        this.equipmentEquippedSlot = equipmentEquippedSlot;
        this.equipmentInventorySlot = equipmentInventorySlot;
        this.equipmentUpgradeTimes = equipmentUpgradeTimes;
        this.equipmentHP = equipmentHP;
        this.equipmentMP = equipmentMP;
        this.equipmentDMG = equipmentDMG;
        this.equipmentDEF = equipmentDEF;
        equipmentType = EquipmentsEnum.valueOf(equipmentID).getEquipmentType();
        equipmentEffect = EquipmentsEnum.valueOf(equipmentID).getEquipmentEffects();
    }

    public Weapons getWeaponByEquipmentType() {
        switch (equipmentType) {
            case "weapon-Spear":
                return Weapons.Spear;
            case "weapon-Poleaxe":
                return Weapons.Poleaxe;
            case "weapon-Heavy_Sword":
                return Weapons.Heavy_Sword;
            case "weapon-Chokuto":
                return Weapons.Chokuto;
            case "weapon-Daggers":
                return Weapons.Daggers;
            case "weapon-Warhammer":
                return Weapons.Warhammer;
        }
        return Weapons.Blood_Weapons;
    }

    public int getEquipmentSlotIndex() {
        switch (equipmentType) {
            case "armor-Helmet":
                return 0;
            case "armor-Shirt":
                return 1;
            case "armor-Pants":
                return 2;
            case "armor-Gloves":
                return 3;
            case "armor-Shoes":
                return 4;
            case "accessory-Ring":
                return 5;
            case "accessory-Pendent":
                return 6;
            case "accessory-Earring":
                return 7;
            case "accessory-Bracelet":
                return 8;
        }
        return -1;
    }
}