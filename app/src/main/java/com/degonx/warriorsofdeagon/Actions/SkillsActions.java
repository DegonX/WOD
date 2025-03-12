package com.degonx.warriorsofdeagon.Actions;


import static com.degonx.warriorsofdeagon.Actions.AttacksActions.executeAttackSkill;

import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.UI.CharacterUI;
import com.degonx.warriorsofdeagon.UI.GameUI;

import java.util.List;

public class SkillsActions {

    //active skills nad potions actions
    public static void activeSkillsActions(Skills skill, Character Char, CharacterUI charUI, GameUI gameUI, List<Mobs> mobs) {
        if (Char.getCharHP() > 0) {
            switch (skill.skillID) {

                //close skills view
                case "SKILL_MENU_BUTTON":
                    charUI.showSkillView();
                    break;

                //use hp potion
                case "HP_POTION_SKILL":
                    Char.usePotion("HP");
                    break;

                //use mp potion
                case "MP_POTION_SKILL":
                    Char.usePotion("MP");
                    break;


                //skills - check if char have enough mp , reduce mp and use the skill
                case "POWER_BOOST":
                    if (Char.getCharMP() >= 100)
                        Char.activatedSkillsAdd(0, 5 * skill.skillLevel, 120, 100);
                    break;
                case "DAMAGE_UP":
                    if (Char.getCharMP() >= 100)
                        Char.activatedSkillsAdd(1, 10 * skill.skillLevel, 120, 100);
                    break;
                case "DEFENSE_UP":
                    if (Char.getCharMP() >= 100)
                        Char.activatedSkillsAdd(2, 10 * skill.skillLevel, 120, 100);
                    break;
                case "CRITICAL_UP":
                    if (Char.getCharMP() >= 100)
                        Char.activatedSkillsAdd(3, skill.skillLevel, 120, 100);
                    break;
                case "HEAL":
                    if (Char.getCharMP() >= 50)
                        Char.addCharHPMP(10 * skill.skillLevel, -100);
                    break;
                case "MANA_REGENERATE":
                    if (Char.getCharHP() >= 50)
                        Char.addCharHPMP(-100, 10 * skill.skillLevel);
                    break;
                case "DARKNESS_EXPLOSION":
                    if (Char.getCharMP() >= 500)
                        executeAttackSkill(600 + (20 * skill.skillLevel), 900, 1, Char, mobs);
                    break;
                case "GROUND_PUNCH":
                    if (Char.getCharMP() >= 500)
                        executeAttackSkill(400 + (20 * skill.skillLevel), 700, 1, Char, mobs);
                    break;
                case "GOD_MODE":
                    if (Char.getGMSkill(4) == 0)
                        Char.setGMSkills(4, 1);
                    else
                        Char.setGMSkills(4, 0);
                    break;
                case "STOP_MOBS":
                    if (Char.getGMSkill(5) == 0)
                        Char.setGMSkills(5, 1);
                    else
                        Char.setGMSkills(5, 0);
                    break;

                case "CHAR_SPEED":
                    if (Char.getCharSpeed() == 1)
                        Char.setCharSpeed(2);
                    else
                        Char.setCharSpeed(1);
                    break;

                case "DAMAGE1":
                    if (Char.getGMSkill(6) == 0)
                        Char.setGMSkills(6, 1);
                    else
                        Char.setGMSkills(6, 0);
                    break;
                default:
                    gameUI.setChat("not enough MP to use this skill or it's already activated");
                    break;

            }
        }
    }
}