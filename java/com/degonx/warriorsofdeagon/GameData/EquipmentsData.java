package com.degonx.warriorsofdeagon.GameData;

import com.degonx.warriorsofdeagon.R;

/* how to give equipment an ID

    the first digits is the type of the equip
    1 - helmet, 2 - shirt, 3 - pants, 4 - glove, 5 - shoes, weapons - 100+
    the other digits are for ordering, testing equips have only 1 number as ID (1~5)
    */

public class EquipmentsData {

    public static int getEquipmentImage(int equipID) {
        switch (equipID) {
            case 1:
                return R.drawable.helmet;
            case 2:
                return R.drawable.shirt;
            case 3:
                return R.drawable.pants;
            case 4:
                return R.drawable.glove;
            case 5:
                return R.drawable.shoes;
            case 101:
                return R.drawable.spear1;
            case 102:
                return R.drawable.poleaxe1;
            case 103:
                return R.drawable.heavysword1;
            case 104:
                return R.drawable.chokuto1;
            case 105:
                return R.drawable.daggers1;
            case 106:
                return R.drawable.warhammer1;
            case 108:
                return R.drawable.scythe1;
            case 109:
                return R.drawable.trident1;
            case 110:
                return R.drawable.doublebladednaginata1;
            case 111:
                return R.drawable.whipsword1;
            case 112:
                return R.drawable.bow1;
            case 113:
                return R.drawable.staff1;
            default:
                return 0;
        }
    }

    public static int getUnEquippedImage(int equipType) {
        switch (equipType) {
            case 1:
                return R.drawable.ehelmet;
            case 2:
                return R.drawable.eshirt;
            case 3:
                return R.drawable.epants;
            case 4:
                return R.drawable.egloves;
            case 5:
                return R.drawable.eshoes;
            default:
                return R.drawable.eweapon;
        }
    }

    public static String getEquipmentName(int equipID) {
        switch (equipID) {
            case 1:
                return "DegonX's Helmet";
            case 2:
                return "DegonX's Shirt";
            case 3:
                return "DegonX's Pants";
            case 4:
                return "DegonX's Gloves";
            case 5:
                return "DegonX's Shoes";
            case 101:
                return "Spear";
            case 102:
                return "Poleaxe";
            case 103:
                return "Heavy Sword";
            case 104:
                return "Chokuto";
            case 105:
                return "Daggers";
            case 106:
                return "Warhammer";
            case 108:
                return "Scythe";
            case 109:
                return "Trident";
            case 110:
                return "Double Bladed Naginata";
            case 111:
                return "Whip Sword";
            case 112:
                return "Bow";
            case 113:
                return "Staff";
            default:
                return null;
        }
    }
}
