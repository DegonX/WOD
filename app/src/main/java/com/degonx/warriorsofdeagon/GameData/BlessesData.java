package com.degonx.warriorsofdeagon.GameData;

import com.degonx.warriorsofdeagon.Enums.Stats;

public class BlessesData {

    public static float getBlessStats(Stats stat, int level) {
        switch (stat) {
            case ATTACK:
            case DEFENCE:
            case BONUS_XP:
                return 10 * level;
            case HP:
            case MP:
                return 100 * level;
            case CRITICAL_RATE:
                return 0.1f * level;
            case CRITICAL_DAMAGE:
                return 0.2f * level;
            default:
                return 0;
        }
    }
}
