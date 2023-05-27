package com.degonx.warriorsofdeagon.GameData;

import com.degonx.warriorsofdeagon.R;

public class AreasData {

    public static int getAreaScreen(int areaID) {
        switch (areaID) {
            case 1:
                return R.drawable.area1;
            case 2:
                return R.drawable.area2;
            case 10:
                return R.drawable.area10;
        }
        return 0;
    }

    public static int getAreaMobsCount(int areaID) {
        switch (areaID) {
            case 1:
                return 3;
            case 2:
                return 5;
            case 10:
                return 1;
        }
        return 0;
    }

    public static int[] getAreaPortals(int areaID) {
        //return - in portal index + to which area + out portal index

        switch (areaID) {
            case 1:
                return new int[]{1, 2, 0};
            case 2:
                return new int[]{0, 1, 1, 1, 10, 0};
            case 10:
                return new int[]{0, 2, 1};
        }
        return new int[]{0, 0, 0};
    }
}
