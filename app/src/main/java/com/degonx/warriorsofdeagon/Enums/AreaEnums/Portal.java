package com.degonx.warriorsofdeagon.Enums.AreaEnums;

public enum Portal {

    StarterCamp_Portal1(Areas.StarterCamp, Areas.Dungeon1, 3320, 870, "Dungeon1_Portal1"),
    Dungeon1_Portal1(Areas.Dungeon1, Areas.StarterCamp, 365, 1580, "StarterCamp_Portal1"),
    Dungeon1_Portal2(Areas.Dungeon1, Areas.Dungeon2, 3730, 840, "Dungeon2_Portal1"),
    Dungeon2_Portal1(Areas.Dungeon2, Areas.Dungeon1, 460, 1620, "Dungeon1_Portal2"),
    Dungeon2_Portal2(Areas.Dungeon2, Areas.Dungeon10, 3680, 1240, "Dungeon10_Portal1"),
    Dungeon10_Portal1(Areas.Dungeon10, Areas.Dungeon2, 400, 1000, "Dungeon2_Portal2");

    final Areas portalArea;
    final Areas portalTo;
    final int portalX;
    final int portalY;
    final String portalDestination;

    Portal(Areas portalArea, Areas portalTo, int portalX, int portalY, String portalDestination) {
        this.portalArea = portalArea;
        this.portalTo = portalTo;
        this.portalX = portalX;
        this.portalY = portalY;
        this.portalDestination = portalDestination;
    }

    public Areas getPortalArea() {
        return portalArea;
    }

    public Areas getPortalTo() {
        return portalTo;
    }

    public int getPortalX() {
        return portalX;
    }

    public int getPortalY() {
        return portalY;
    }

    public String getPortalDestination() {
        return portalDestination;
    }

}
