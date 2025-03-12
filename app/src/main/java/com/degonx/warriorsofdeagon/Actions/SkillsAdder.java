package com.degonx.warriorsofdeagon.Actions;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Database.SkillsDB;

import java.util.List;

public class SkillsAdder {

    static SkillsDB skillsDB;
    static int charID;

    public static void newCharSkills(int CharID, SkillsDB skillsdb, List<Elements> charElements) {

        //fister skills
        skillsdb.registerSkill(CharID, SkillsEnum.FISTERS_RAGE, 1);
        skillsdb.registerSkill(CharID, SkillsEnum.GROUND_PUNCH, 1);

        //add weapon mastery skills
        skillsdb.registerSkill(CharID, SkillsEnum.SPEAR_MASTERY, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.POLEAXE_MASTERY, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.HEAVY_SWORD_MASTERY, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.CHOKUTO_MASTERY, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.DAGGERS_MASTERY, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.WARHAMMER_MASTERY, 0);

        //add active skills
        skillsdb.registerSkill(CharID, SkillsEnum.POWER_BOOST, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.DAMAGE_UP, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.DEFENSE_UP, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.CRITICAL_UP, 0);

        //add passive skills
        skillsdb.registerSkill(CharID, SkillsEnum.MORE_DAMAGE, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.MORE_DEFENSE, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.MORE_HP, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.MORE_MP, 0);
        skillsdb.registerSkill(CharID, SkillsEnum.EXTRA_ATTACK, 0);

        //add element skills by chosen element
        for (Elements element : charElements)
            addElementSkills(CharID, skillsdb, element, charElements);
    }

    public static void addElementSkills(int CharID, SkillsDB skillsdb, Elements element, List<Elements> charElements) {
        charID = CharID;
        skillsDB = skillsdb;
        skillsDB.registerSkill(CharID, SkillsEnum.valueOf(element.toString().toUpperCase() + "_KNOWLEDGE"), 0);
        if (element == Elements.Fire)
            fireSkills();
        else if (element == Elements.Ice)
            iceSkills();
        else if (element == Elements.Energy)
            energySkills(charElements);
        else if (element == Elements.Blood)
            bloodSkills();
        else if (element == Elements.Darkness)
            darknessSkills(charElements);
    }

    private static void fireSkills() {
        skillsDB.registerSkill(charID, SkillsEnum.BURN, 0);
        checkSkillExist(charID, SkillsEnum.AVOID);
    }

    private static void iceSkills() {
        skillsDB.registerSkill(charID, SkillsEnum.FREEZE, 0);
    }

    private static void energySkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Darkness))
            checkSkillExist(charID, SkillsEnum.DARKNESS_EXPLOSION);
        skillsDB.registerSkill(charID, SkillsEnum.HEAL, 0);
        skillsDB.registerSkill(charID, SkillsEnum.MANA_REGENERATE, 0);
        skillsDB.registerSkill(charID, SkillsEnum.ENERGY_HEALING, 0);
    }

    private static void bloodSkills() {
        skillsDB.registerSkill(charID, SkillsEnum.BLOOD_WEAPONS_MASTERY, 0);
        skillsDB.registerSkill(charID, SkillsEnum.BLOOD_BOOST, 0);
        skillsDB.registerSkill(charID, SkillsEnum.BLOOD_HEALING, 0);
    }

    private static void darknessSkills(List<Elements> charEle) {
        if (charEle.contains(Elements.Energy))
            checkSkillExist(charID, SkillsEnum.DARKNESS_EXPLOSION);
        checkSkillExist(charID, SkillsEnum.AVOID);
    }

    //check if character already have the skill
    private static void checkSkillExist(int CharID, SkillsEnum skill) {
        if (!skillsDB.CheckIfSkillExist(skill.toString(), CharID))
            skillsDB.registerSkill(CharID, skill, 0);
    }
}
