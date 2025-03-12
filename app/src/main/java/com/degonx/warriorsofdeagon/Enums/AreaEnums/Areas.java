package com.degonx.warriorsofdeagon.Enums.AreaEnums;

import com.degonx.warriorsofdeagon.R;

public enum Areas {

    StarterCamp("Starter Camp", R.drawable.camp1, "safe_town", 2530, 1500, 1, 4, 0),
    Dungeon1("Flaming Dungeon 1", R.drawable.area1, "mobs_area", 900, 1600, 2, 0, 3),
    Dungeon2("Flaming Dungeon 2", R.drawable.area2, "mobs_area", 900, 1600, 2, 0, 5),
    Dungeon10("Flaming Dungeon 10", R.drawable.area10, "boss_area", 850, 1000, 1, 0, 1);


    final String areaName;
    final int areaBG;
    final String areaType;
    final int areaSpawnX;
    final int areaSpawnY;
    final int portalCount;
    final int npcCount;
    final int mobsCount;

    Areas(String areaName, int areaBG, String areaType, int areaSpawnX, int areaSpawnY, int portalCount, int npcCount, int mobsCount) {
        this.areaName = areaName;
        this.areaBG = areaBG;
        this.areaType = areaType;
        this.portalCount = portalCount;
        this.areaSpawnX = areaSpawnX;
        this.areaSpawnY = areaSpawnY;
        this.npcCount = npcCount;
        this.mobsCount = mobsCount;
    }

    public int getAreaBG() {
        return areaBG;
    }

    public String getAreaType() {
        return areaType;
    }

    public int getAreaSpawnX() {
        return areaSpawnX;
    }

    public int getAreaSpawnY() {
        return areaSpawnY;
    }

    public int getNpcCount() {
        return npcCount;
    }

    public int getPortalCount() {
        return portalCount;
    }

    public int getMobsCount() {
        return mobsCount;
    }
}
