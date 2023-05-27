package com.degonx.warriorsofdeagon.AttackAndSkills;


import static com.degonx.warriorsofdeagon.AttackAndSkills.AttacksActions.executeAttackSkill;

import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.UI.CharacterUI;
import com.degonx.warriorsofdeagon.UI.GameUI;

import java.util.List;

public class SkillsActions {

    //active skills nad potions actions - negative s.skillID are potions ,(-10 is close to close skillview tab)
    public static void activeSkillsActions(Skills skill, Character Char, CharacterUI charUI, GameUI gameUI, List<Mobs> mobs) {
        if (Char.getCharHP() > 0) {

            //close skills view
            if (skill.skillID == -10)
                charUI.showSkillView();
            else if (skill.skillID < 0 && skill.skillID > -10) {
                //check if potion available nad use it

                //hp potion
                if (skill.skillID == -1)
                    Char.usePotion(1);

                    //mp potion
                else if (skill.skillID == -2)
                    Char.usePotion(2);

            } else {

                //skills - check if char have enough mp , reduce mp and use the skill
                //companion
                if (skill.skillID == 300000 && Char.getCharMP() >= 80)
                    Char.summonComp(skill.skillLevel);

                    //active
                else if (skill.skillID == 300001 && Char.getCharMP() >= 100)
                    Char.activatedSkillsAdd(0, 5 * skill.skillLevel, 120, 100);
                else if (skill.skillID == 300002 && Char.getCharMP() >= 100)
                    Char.activatedSkillsAdd(1, 10 * skill.skillLevel, 120, 100);
                else if (skill.skillID == 300003 && Char.getCharMP() >= 100)
                    Char.activatedSkillsAdd(2, 10 * skill.skillLevel, 120, 100);
                else if (skill.skillID == 300004 && Char.getCharMP() >= 100)
                    Char.activatedSkillsAdd(3, skill.skillLevel, 120, 100);
                else if (skill.skillID == 330001 && Char.getCharMP() >= 50)
                    Char.addCharHPMP(10 * skill.skillLevel, -100);
                else if (skill.skillID == 330002 && Char.getCharHP() >= 50)
                    Char.addCharHPMP(-100, 10 * skill.skillLevel);

                    //attacks
                else if (skill.skillID == 535001 && Char.getCharMP() >= 500)
                    executeAttackSkill(600 + (20 * skill.skillLevel), 500, 1, Char, mobs);
                else if (skill.skillID == 512601 && Char.getCharMP() >= 600) {
                    executeAttackSkill(300 + (20 * skill.skillLevel), 600, 3, Char, mobs);
                    for (Mobs m : mobs) {
                        m.attemptToAddElementalEffect(ElementalEffects.Burn);
                        m.attemptToAddElementalEffect(ElementalEffects.Freeze);
                        m.attemptToAddElementalEffect(ElementalEffects.Paralyze);
                    }
                } else if (skill.skillID == 500000 && Char.getCharMP() >= 500)
                    executeAttackSkill(500 + (20 * skill.skillLevel), 500, 1, Char, mobs);

                    //GM
                else if (skill.skillID == 399900) {
                    //GM skill - god mode
                    if (Char.getGMSkill(4) == 0)
                        Char.setGMSkills(4, 1);
                    else
                        Char.setGMSkills(4, 0);
                } else if (skill.skillID == 399901) {
                    //GM skill - stop mobs
                    if (Char.getGMSkill(5) == 0)
                        Char.setGMSkills(5, 1);
                    else
                        Char.setGMSkills(5, 0);
                } else
                    gameUI.setChat("not enough MP to use this skill or its already activated");
            }
        }
    }
}