package com.degonx.warriorsofdeagon.Objects;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;

public class Skills {

    public String skillID;
    public int skillLevel;
    public String skillType;
    public boolean skillInTab;

    public Skills(String skillID, int skillLevel, int inTab) {
        this.skillID = skillID;
        this.skillLevel = skillLevel;
        if (skillLevel >= 0)
            skillType = SkillsEnum.valueOf(skillID).getSkillType();
        else
            skillType = "OTHER";
        skillInTab = inTab == 1;
    }
}
