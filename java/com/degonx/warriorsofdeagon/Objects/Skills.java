package com.degonx.warriorsofdeagon.Objects;

public class Skills {

    public int skillID;
    public int skillLevel;
    public boolean skillInTab = false;

    public Skills(int skillID, int skillLevel, int inTab) {
        this.skillID = skillID;
        this.skillLevel = skillLevel;
        if (inTab == 1)
            this.skillInTab = true;
    }
}
