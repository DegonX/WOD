package com.degonx.warriorsofdeagon.GameData;

public class BlessesData {

    public static double getBlessBaseValue(String name, int level) {
        switch (name) {
            case "Attack":
            case "Defense":
                return 10 * level;
            case "HP":
            case "MP":
                return 100 * level;
            case "Critical Rate":
                return 0.3 * level;
            case "Critical Damage":
                return level;
            case "XP Bonus":
                return 0.1 * level;
            default:
                return 0;
        }
    }

    public static int blessLevelCost(int lv) {
        int blc = 0;
        if (lv <= 5)
            blc = 1;
        else if (lv < 11)
            blc = 2;
        else if (lv < 21)
            blc = 3;
        else if (lv < 30)
            blc = 4;
        return ((100 + 25 * lv) * blc);
    }
}
