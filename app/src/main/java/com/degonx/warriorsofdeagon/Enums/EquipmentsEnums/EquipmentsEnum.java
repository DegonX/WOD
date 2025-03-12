package com.degonx.warriorsofdeagon.Enums.EquipmentsEnums;

import com.degonx.warriorsofdeagon.R;

public enum EquipmentsEnum {
    DEGONX_HELMET("armor-Helmet", R.drawable.helmet, "DegonX's Helmet", 0, 20, 0, 100, EquipmentsEffects.NONE),
    DEGONX_SHIRT("armor-Shirt", R.drawable.shirt, "DegonX's Shirt", 0, 25, 100, 0, EquipmentsEffects.NONE),
    DEGONX_PANTS("armor-Pants", R.drawable.pants, "DegonX's Pants", 0, 25, 100, 0, EquipmentsEffects.NONE),
    DEGONX_GLOVES("armor-Gloves", R.drawable.glove, "DegonX's Gloves", 10, 15, 0, 100, EquipmentsEffects.NONE),
    DEGONX_SHOES("armor-Shoes", R.drawable.shoes, "DegonX's Shoes", 0, 20, 0, 0, EquipmentsEffects.NONE),
    DRAMON_RING("accessory-Ring", R.drawable.ring1, "Dramon's Ring", 10, 0, 0, 30, EquipmentsEffects.MP_REGEN),
    DRAMON_PENDENT("accessory-Pendent", R.drawable.pendent1, "Dramon's Pendent", 0, 0, 20, 20, EquipmentsEffects.HP_REGEN),
    DRAMON_EARRING("accessory-Earring", R.drawable.earrings1, "Dramon's Earring", 10, 0, 10, 10, EquipmentsEffects.HP_REGEN),
    DRAMON_BRACELET("accessory-Bracelet", R.drawable.bracelet1, "Dramon's Bracelet", 10, 5, 5, 5, EquipmentsEffects.MP_REGEN),
    SPEAR("weapon-Spear", R.drawable.spear1, "Spear", 50, 0, 0, 0, EquipmentsEffects.NONE),
    POLEAXE("weapon-Poleaxe", R.drawable.poleaxe1, "Poleaxe", 55, 0, 0, 0, EquipmentsEffects.NONE),
    HEAVY_SWORD("weapon-Heavy_Sword", R.drawable.heavysword1, "Heavy Sword", 60, 0, 0, 0, EquipmentsEffects.NONE),
    CHOKUTO("weapon-Chokuto", R.drawable.chokuto1, "Chokuto", 40, 0, 0, 0, EquipmentsEffects.NONE),
    DAGGERS("weapon-Daggers", R.drawable.daggers1, "Daggers", 40, 0, 0, 0, EquipmentsEffects.NONE),
    WARHAMMER("weapon-Warhammer", R.drawable.warhammer1, "Warhammer", 55, 0, 0, 0, EquipmentsEffects.NONE);

    final String equipmentType;
    final int equipmentImage;
    final String equipmentName;
    final int equipmentBaseDamage;
    final int equipmentBaseDefense;
    final int equipmentBaseHP;
    final int equipmentBaseMP;

    final EquipmentsEffects equipmentEffects;

    EquipmentsEnum(String equipmentType, int equipmentImage, String equipmentName, int equipmentBaseDamage, int equipmentBaseDefense, int equipmentBaseHP, int equipmentBaseMP, EquipmentsEffects equipmentEffects) {
        this.equipmentType = equipmentType;
        this.equipmentImage = equipmentImage;
        this.equipmentName = equipmentName;
        this.equipmentBaseDamage = equipmentBaseDamage;
        this.equipmentBaseDefense = equipmentBaseDefense;
        this.equipmentBaseHP = equipmentBaseHP;
        this.equipmentBaseMP = equipmentBaseMP;
        this.equipmentEffects = equipmentEffects;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public int getEquipmentImage() {
        return equipmentImage;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public int getEquipmentBaseDamage() {
        return equipmentBaseDamage;
    }

    public int getEquipmentBaseDefense() {
        return equipmentBaseDefense;
    }

    public int getEquipmentBaseHP() {
        return equipmentBaseHP;
    }

    public int getEquipmentBaseMP() {
        return equipmentBaseMP;
    }

    public EquipmentsEffects getEquipmentEffects() {
        return equipmentEffects;
    }
}
