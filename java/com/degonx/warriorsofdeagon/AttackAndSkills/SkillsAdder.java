package com.degonx.warriorsofdeagon.AttackAndSkills;

import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Database.SkillsDB;

import java.util.List;

public class SkillsAdder {

    static SkillsDB skillsDB;
    static int charID;

    public static void newCharSkills(int CharID, SkillsDB skillsdb, List<Elements> charElements) {
        skillsdb.registerSkill(CharID, 100000, 1);
        skillsdb.registerSkill(CharID, 500000, 1);
        for (int wm = 1; wm < 14; wm++)
            if (wm != 7)
                skillsdb.registerSkill(CharID, 100000 + wm, 0);

        skillsdb.registerSkill(CharID, 300000, 0);
        for (int b = 1; b <= 4; b++)
            skillsdb.registerSkill(CharID, 400000 + b, 0);

        for (Elements element : charElements)
            addElementSkills(CharID, skillsdb, element, charElements);
    }

    public static void addElementSkills(int CharID, SkillsDB skillsdb, Elements element, List<Elements> charElements) {
        charID = CharID;
        skillsDB = skillsdb;
        skillsDB.registerSkill(CharID, 200000 + element.elementTypeNum, 0);
        if (element == Elements.Fire)
            fireSkills(charElements);
        else if (element == Elements.Ice)
            iceSkills(charElements);
        else if (element == Elements.Energy)
            energySkills(charElements);
        else if (element == Elements.Blood)
            bloodSkills();
        else if (element == Elements.Darkness)
            darknessSkills(charElements);
        else if (element == Elements.Lightning)
            lightningSkills(charElements);
        else if (element == Elements.Earth)
            earthSkills();
        else if (element == Elements.Wind)
            windSkills();
    }

    private static void fireSkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Ice) && charEle.contains(Elements.Lightning))
            skillsDB.registerSkill(charID, 512601, 0);
        skillsDB.registerSkill(charID, 410001, 0);
        checkSkillExist(charID, 300002);
        checkSkillExist(charID, 300004);
        checkSkillExist(charID, 415801);
    }

    private static void iceSkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Fire) && charEle.contains(Elements.Ice))
            skillsDB.registerSkill(charID, 512601, 0);
        skillsDB.registerSkill(charID, 420001, 0);
        checkSkillExist(charID, 300001);
        checkSkillExist(charID, 300003);
        checkSkillExist(charID, 300004);
        checkSkillExist(charID, 400005);
    }

    private static void energySkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Darkness))
            checkSkillExist(charID, 535001);
        skillsDB.registerSkill(charID, 330001, 0);
        skillsDB.registerSkill(charID, 330002, 0);
        skillsDB.registerSkill(charID, 430001, 0);
        checkSkillExist(charID, 400001);
        checkSkillExist(charID, 400002);
        checkSkillExist(charID, 400004);
    }

    private static void bloodSkills() {
        skillsDB.registerSkill(charID, 100007, 0);
        skillsDB.registerSkill(charID, 440001, 0);
        skillsDB.registerSkill(charID, 440002, 0);
        checkSkillExist(charID, 300001);
        checkSkillExist(charID, 300002);
        checkSkillExist(charID, 300003);
        checkSkillExist(charID, 300004);
        checkSkillExist(charID, 400005);
    }

    private static void darknessSkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Energy))
            checkSkillExist(charID, 535001);
        checkSkillExist(charID, 200001);
        checkSkillExist(charID, 200004);
        checkSkillExist(charID, 400005);
        checkSkillExist(charID, 415801);
    }

    private static void lightningSkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Fire) && charEle.contains(Elements.Ice))
            skillsDB.registerSkill(charID, 512601, 0);
        skillsDB.registerSkill(charID, 460001, 0);
        checkSkillExist(charID, 300001);
        checkSkillExist(charID, 300004);
        checkSkillExist(charID, 400005);
    }

    private static void earthSkills() {
        checkSkillExist(charID, 300001);
        checkSkillExist(charID, 300003);
        checkSkillExist(charID, 400005);
    }

    private static void windSkills() {
        checkSkillExist(charID, 300002);
        checkSkillExist(charID, 300004);
        checkSkillExist(charID, 400005);
        checkSkillExist(charID, 415801);
    }

    //check if character already have the skill
    private static void checkSkillExist(int CharID, int SID) {
        if (!skillsDB.CheckIfSkillExist(SID, CharID))
            skillsDB.registerSkill(CharID, SID, 0);
    }
}
