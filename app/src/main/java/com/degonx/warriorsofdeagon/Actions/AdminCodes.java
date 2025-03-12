package com.degonx.warriorsofdeagon.Actions;

import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.SkillsDB;
import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;

public class AdminCodes {

    static Game game;
    static Character Char;
    static AccountsDB accDB;
    static CharactersDB charDB;
    static SkillsDB skillDB;

    public static void setAdminData(Game game, Character Char, AccountsDB accDB) {
        AdminCodes.game = game;
        AdminCodes.Char = Char;
        AdminCodes.accDB = accDB;
        charDB = Char.getCharDB();
        skillDB = Char.getSkillDB();
    }

    //testing codes
    public static void useCode(String code) {
        switch (code) {
            case "leveluptest":
                charDB.updateCharacterLevelUp(Char.getCharID(), 50, 250);
                break;

            case "leveluptest2":
                charDB.updateCharacterLevelUp(Char.getCharID(), 70, 350);
                break;

            case "leveluptest3":
                charDB.updateCharacterLevelUp(Char.getCharID(), 100, 500);
                break;

            case "addores":
                accDB.updateOres(Char.getAccountID(), 500000);
                break;

            case "droptest":
                addAdminSkill(SkillsEnum.DROP_TEST);
                break;

            case "nompcost":
                addAdminSkill(SkillsEnum.SKILLS_TEST);
                break;

            case "lordtest":
                addAdminSkill(SkillsEnum.LORD_TEST);
                break;

            case "blesstest":
                addAdminSkill(SkillsEnum.BLESS_TEST);
                charDB.updateBlessPoints(Char.getCharID(), 500000);
                break;

            case "godmode":
                addAdminSkill(SkillsEnum.GOD_MODE);
                break;

            case "stopmobs":
                addAdminSkill(SkillsEnum.STOP_MOBS);
                break;

            case "speed":
                addAdminSkill(SkillsEnum.CHAR_SPEED);
                break;

            case "1damage":
                addAdminSkill(SkillsEnum.DAMAGE1);
                break;

            case "alladminskills":
                addAdminSkill(SkillsEnum.DROP_TEST);
                addAdminSkill(SkillsEnum.SKILLS_TEST);
                addAdminSkill(SkillsEnum.LORD_TEST);
                addAdminSkill(SkillsEnum.BLESS_TEST);
                addAdminSkill(SkillsEnum.GOD_MODE);
                addAdminSkill(SkillsEnum.STOP_MOBS);
                addAdminSkill(SkillsEnum.CHAR_SPEED);
                addAdminSkill(SkillsEnum.DAMAGE1);
                break;

            default:
                Toasts.makeToast(game, "invalid code");
        }
    }

    private static void addAdminSkill(SkillsEnum skill) {
        if (!skillDB.CheckIfSkillExist(skill.toString(), Char.getCharID())) {
            skillDB.registerSkill(Char.getCharID(), skill, 99);
        }
    }
}
