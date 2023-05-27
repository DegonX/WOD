package com.degonx.warriorsofdeagon.Enums.EffectsEnums;

public enum AreaEffects {
    None(0, "", ""),
    Overheat(30, "Overheat started", "Overheat ended"),
    DarknessFog(30, "Darkness fog is covering the area", "Darkness fog has faded"),
    LightningStorm(30, "Lightning storm started", "The lightning storm stopped"),
    Whirlwind(30, "Whirlwind surrounds you", "Whirl wind stopped"),
    Tornado(15, "Tornado formed in the middle of the area", "Tornado deformed");

    public final int Time;
    public final String startMessage;
    public final String stopMessage;

    AreaEffects(int time, String startMessage, String stopMessage) {
        Time = time;
        this.startMessage = startMessage;
        this.stopMessage = stopMessage;
    }

}
