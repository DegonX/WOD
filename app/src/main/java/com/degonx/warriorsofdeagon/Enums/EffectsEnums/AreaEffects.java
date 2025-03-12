package com.degonx.warriorsofdeagon.Enums.EffectsEnums;

public enum AreaEffects {
    None(0, "", ""),
    Overheat(30, "Overheat started", "Overheat ended"),
    DarknessFog(30, "Darkness fog is covering the area", "Darkness fog has faded");
    final int Time;
    final String startMessage;
    final String stopMessage;

    AreaEffects(int time, String startMessage, String stopMessage) {
        Time = time;
        this.startMessage = startMessage;
        this.stopMessage = stopMessage;
    }

    public int getTime() {
        return Time;
    }

    public String getStartMessage() {
        return startMessage;
    }

    public String getStopMessage() {
        return stopMessage;
    }
}
