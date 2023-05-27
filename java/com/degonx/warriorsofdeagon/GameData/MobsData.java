package com.degonx.warriorsofdeagon.GameData;

import com.degonx.warriorsofdeagon.R;

public class MobsData {

    public static int getMobsImg(int areaID) {
        switch (areaID) {
            case 1:
                return R.drawable.mob1;
            case 2:
                return R.drawable.mob2;
            case 10:
                return R.drawable.boss1;
        }
        return 0;
    }

    //return mobID + hp mult + xp mult + atk + def + speed
    public static int[] getDataMobs(int areaID) {
        switch (areaID) {
            case 1:
                return new int[]{1, 1, 1, 15, 10, 2};
            case 2:
                return new int[]{2, 4, 3, 35, 25, 2};
            case 10:
                return new int[]{10, 7, 5, 60, 45, 0};
        }
        return new int[]{0, 0, 0, 0, 0, 0};
    }
}
