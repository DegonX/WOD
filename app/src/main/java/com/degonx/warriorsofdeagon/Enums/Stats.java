package com.degonx.warriorsofdeagon.Enums;

public enum Stats {
    HP("HP"),
    MP("MP"),
    ATTACK("Attack"),
    DEFENCE("Defence"),
    CRITICAL_RATE("Critical Rate"),
    CRITICAL_DAMAGE("Critical Damage"),
    BONUS_XP("Bonus XP");

    public final String statName;

    Stats(String name) {
        statName = name;
    }

}
