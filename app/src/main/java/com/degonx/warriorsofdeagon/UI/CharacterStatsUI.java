package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.WeaponAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.WeaponsAndElementsData;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.R;

import java.util.List;
import java.util.Locale;

@SuppressLint("SetTextI18n")
public class CharacterStatsUI {

    private final TextView statsCharLevelTxt, charStatTxt, statsSelectedAttackName, statsSelectedAttackInfo;
    private final ConstraintLayout statsLay, atkInfoStatsLayout;
    private final LinearLayout weaponStatsLayout, elementStatsLayout;
    private final TableRow weaponStatsTR, elementStatsTR;
    private final Button[] statsAttackInfoBtn = new Button[7];
    private String[] attacksDescriptions;

    private Weapons statsSelectedWeapon;
    private Elements statsSelectedElement;

    private final Character Char;
    private final Game game;

    public CharacterStatsUI(Game game, Character character, List<Skills> charWeapSkills, List<Elements> charEle) {
        this.game = game;
        this.Char = character;

        //find all stats screen views
        statsLay = game.findViewById(R.id.statspageL);
        ImageView sCharimg = game.findViewById(R.id.scharim);
        TextView sCharInfo = game.findViewById(R.id.scharinfo);
        statsCharLevelTxt = game.findViewById(R.id.scharlevel);
        charStatTxt = game.findViewById(R.id.scharStats);
        atkInfoStatsLayout = game.findViewById(R.id.statsAtkInfoL);
        weaponStatsLayout = game.findViewById(R.id.weaponStatsLayout);
        elementStatsLayout = game.findViewById(R.id.elementStatsLayout);
        statsSelectedAttackName = game.findViewById(R.id.statsatkname);
        statsSelectedAttackInfo = game.findViewById(R.id.statsatkinfo);
        statsAttackInfoBtn[0] = game.findViewById(R.id.statsatk1);
        statsAttackInfoBtn[1] = game.findViewById(R.id.statsatk2);
        statsAttackInfoBtn[2] = game.findViewById(R.id.statsatk3);
        statsAttackInfoBtn[3] = game.findViewById(R.id.statsatk4);
        statsAttackInfoBtn[4] = game.findViewById(R.id.statsatk5);
        statsAttackInfoBtn[5] = game.findViewById(R.id.statsatk6);
        statsAttackInfoBtn[6] = game.findViewById(R.id.statsatk7);

        weaponStatsTR = game.findViewById(R.id.weapStatsTR);
        elementStatsTR = game.findViewById(R.id.eleStatsTR);

        //set attack info buttons
        statsAttackInfoBtn[0].setOnClickListener(ad -> WepEleInfo(0));
        statsAttackInfoBtn[1].setOnClickListener(ad -> WepEleInfo(1));
        statsAttackInfoBtn[2].setOnClickListener(ad -> WepEleInfo(2));
        statsAttackInfoBtn[3].setOnClickListener(ad -> WepEleInfo(3));
        statsAttackInfoBtn[4].setOnClickListener(ad -> WepEleInfo(4));
        statsAttackInfoBtn[5].setOnClickListener(ad -> WepEleInfo(5));
        statsAttackInfoBtn[6].setOnClickListener(ad -> WepEleInfo(6));

        //set character image and shows stats when clicked
        sCharimg.setImageResource(R.drawable.character);
        sCharimg.setOnClickListener(sho -> {
            //show character stats and hide attacks view
            atkInfoStatsLayout.setVisibility(View.GONE);
            charStatTxt.setVisibility(View.VISIBLE);
        });
        sCharInfo.setText(Char.getCharName());

        //show character usable weapons and element(weapon by mastery skill)
        for (Skills wm : charWeapSkills)
            if (wm.skillLevel > 0)
                createInfoButton(true, SkillsEnum.valueOf(wm.skillID).getSkillType().split("-")[1]);

        for (Elements en : charEle)
            createInfoButton(false, en.toString());
    }

    //create weapon or element button
    public void createInfoButton(boolean weapon, String name) {
        Button newBtn = new Button(game);
        if (weapon) {
            weaponStatsTR.addView(newBtn);
            newBtn.setBackgroundResource(Weapons.valueOf(name).weaponIcon);
        } else {
            elementStatsTR.addView(newBtn);
            newBtn.setBackgroundResource(Elements.valueOf(name).elementIcon);
        }
        newBtn.getLayoutParams().height = 130;
        newBtn.getLayoutParams().width = 130;

        //show element or weapon information when clicked
        newBtn.setOnClickListener(ai -> {
            //hide character stats and show attacks views
            atkInfoStatsLayout.setVisibility(View.VISIBLE);
            charStatTxt.setVisibility(View.GONE);
            AttackInfo(weapon, name);
        });
    }

    //show stats page and set character data
    public void showStatsPage() {
        //show character data
        statsLay.setVisibility(View.VISIBLE);
        statsCharLevelTxt.setText("Level:" + Char.getCharLevel());
        String charStatsStr = "HP:" + (Char.getCharMaxHP() + Char.getEquipAdd(2)) + "\nMP:" + (Char.getCharMaxMP() + Char.getEquipAdd(3));
        charStatsStr += "\nAttack:" + (Char.getCharAttack() + Char.getEquipAdd(0)) + "\nDefense:" + (Char.getCharDefense() + Char.getEquipAdd(1));
        charStatsStr += "\nCritical Rate:" + String.format(Locale.ENGLISH, "%.2f", Char.getCharCriticalRate()) + "\nCritical Damage:" + Char.getCharCriticalDamage();
        charStatTxt.setText(charStatsStr);
    }

    //show weapons \ elements attack and their data or show character stats
    private void AttackInfo(boolean weapon, String name) {
        //show weapon \ element attacks

        //set\reset views
        statsSelectedAttackName.setText("");
        statsSelectedAttackInfo.setText("");

        //weapons attacks
        if (weapon) {
            //show weapon attack views and hide element attacks views
            weaponStatsLayout.setVisibility(View.VISIBLE);
            elementStatsLayout.setVisibility(View.GONE);

            statsSelectedWeapon = Weapons.valueOf(name);
            attacksDescriptions = WeaponsAndElementsData.getWeaponAtkDescription(statsSelectedWeapon);

            //set weapons attacks names on buttons
            for (WeaponAttacks wa : statsSelectedWeapon.Attacks)
                statsAttackInfoBtn[wa.attackNumber - 1].setText(wa.attackName);

            //elements attacks
        } else {
            //hide weapon attack views and show element attacks views
            weaponStatsLayout.setVisibility(View.GONE);
            elementStatsLayout.setVisibility(View.VISIBLE);

            statsSelectedElement = Elements.valueOf(name);
            attacksDescriptions = WeaponsAndElementsData.getElementAtkDescription(statsSelectedElement);

            //set element attacks names on buttons
            for (ElementAttacks ea : statsSelectedElement.Attacks)
                statsAttackInfoBtn[ea.attackNumber + 3].setText(ea.attackName);
        }
    }

    private void WepEleInfo(int atk) {
        //set title attack name
        statsSelectedAttackName.setText(statsAttackInfoBtn[atk].getText().toString());

        //set weapon attack data
        if (atk < 4)
            statsSelectedAttackInfo.setText(attacksDescriptions[atk] +
                    "\n\nBase Damage:" + statsSelectedWeapon.Attacks[atk].attackDamage +
                    "\nMP Cost:" + statsSelectedWeapon.Attacks[atk].attackManaCost +
                    "\nAttack Times:" + statsSelectedWeapon.Attacks[atk].attackTimes +
                    "\nUnlock at Weapon Skill Level:" + WeaponsAndElementsData.getUnlockLevel(atk + 1));

            //set element attack data
        else
            statsSelectedAttackInfo.setText(attacksDescriptions[atk - 4] +
                    "\n\nBase Damage:" + statsSelectedElement.Attacks[atk - 4].attackDamage +
                    "\nMP Cost:" + statsSelectedElement.Attacks[atk - 4].attackManaCost +
                    "\nAttack Times:" + statsSelectedElement.Attacks[atk - 4].attackTimes +
                    "\nCooldown Time:" + statsSelectedElement.Attacks[atk - 4].attackCooldown +
                    "\nUnlock at Element Skill Level:" + WeaponsAndElementsData.getUnlockLevel(atk + 1));
    }
}