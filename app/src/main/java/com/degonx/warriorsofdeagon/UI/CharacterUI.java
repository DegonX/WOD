package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.degonx.warriorsofdeagon.Adapters.SkillsAdapter;
import com.degonx.warriorsofdeagon.Actions.SkillsActions;
import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.WeaponAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Interfaces.Attack;
import com.degonx.warriorsofdeagon.Objects.Equipments;
import com.degonx.warriorsofdeagon.VisionAndSound.JoyStickClass;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.R;

import java.util.List;

@SuppressLint("SetTextI18n")
public class CharacterUI {

    private final TextView charNameNLvtxt, charHPTxt, charMPTxt, charXPTxt;
    private final Button[] atksBtn = new Button[7];
    private final Button[] weaponSelect = new Button[6];
    private final Button weaponBtn, elementBtn, lordBtn, skillTabBtn;
    private final ImageView[] activeSkillsIMG = new ImageView[4];
    private final ImageView charIMG;
    private final TableRow selectWeapon;
    private final GridView skillsGrid;
    private final SkillsAdapter skillsAdapter;
    private final LinearLayout skillsTab;
    private final List<int[]> attacksCooldownList;
    private final Character Char;

    public CharacterUI(Character Char, Game game, GameUI gameui, List<Elements> charEle, List<int[]> attacksCooldownList) {
        this.Char = Char;
        this.attacksCooldownList = attacksCooldownList;

        //create image for character
        charIMG = new ImageView(game);
        gameui.addRemoveFromGameView(charIMG, true);
        charIMG.getLayoutParams().height = 400;
        charIMG.getLayoutParams().width = 150;
        setCharacterImage(false, Elements.NONE);
        setCharImageLocation(Char.getCharX(), Char.getCharY());

        //load other buttons
        weaponBtn = game.findViewById(R.id.cweapon);
        elementBtn = game.findViewById(R.id.celement);
        lordBtn = game.findViewById(R.id.lord);
        selectWeapon = game.findViewById(R.id.selectweapon);
        skillsTab = game.findViewById(R.id.AskillsView);

        //show lord button if character level 70 and have element fire or darkness
        if (Char.getCharLevel() >= 70)
            if (charEle.contains(Elements.Fire) || charEle.contains(Elements.Darkness)) {
                lordBtn.setVisibility(View.VISIBLE);
                setLordBtn();
            }

        //set buttons onclick
        weaponBtn.setOnClickListener(cw -> setWeaponSelect());
        elementBtn.setOnClickListener(cw -> Char.changeElement());
        lordBtn.setOnClickListener(cw -> Char.activeLordMode());

        //load attack buttons and set their actions
        atksBtn[0] = game.findViewById(R.id.atk1);
        atksBtn[1] = game.findViewById(R.id.atk2);
        atksBtn[2] = game.findViewById(R.id.atk3);
        atksBtn[3] = game.findViewById(R.id.atk4);
        atksBtn[4] = game.findViewById(R.id.atk5);
        atksBtn[5] = game.findViewById(R.id.atk6);
        atksBtn[6] = game.findViewById(R.id.atk7);
        for (int b = 0; b < 7; b++) {
            int finalB = b;
            atksBtn[b].setOnClickListener(ab -> Char.startAttack(finalB));
        }

        //load character data texts
        charNameNLvtxt = game.findViewById(R.id.charnameNlv);
        charHPTxt = game.findViewById(R.id.charhp);
        charMPTxt = game.findViewById(R.id.charmp);
        charXPTxt = game.findViewById(R.id.charxp);

        //find and load skills tab views
        skillsGrid = game.findViewById(R.id.skillGrid);
        skillsAdapter = new SkillsAdapter(game, Char.getCharTabSkills());
        skillsGrid.setAdapter(skillsAdapter);
        skillTabBtn = game.findViewById(R.id.skillOpen);
    }

    public void setupEquippedWeaponSelection(Game game, Equipments[] equippedWeapons, int bloodSkillLv) {
        int ws = 3;
        if (bloodSkillLv > 0)
            ws++;

        for (int i = 0; i < ws; i++) {
            //create new weapon selection button
            weaponSelect[i] = new Button(game);
            selectWeapon.addView(weaponSelect[i]);
            weaponSelect[i].getLayoutParams().width = 130;
            weaponSelect[i].getLayoutParams().height = 130;
            weaponSelect[i].setPadding(1, 2, 1, 2);

            //if weapon equipped
            if (i < 3 && equippedWeapons[i] != null)
                weaponSelect[i].setBackgroundResource(Weapons.valueOf(equippedWeapons[i].equipmentType.split("-")[1]).weaponIcon);
                //if blood weapons available
            else if (i == 3)
                weaponSelect[i].setBackgroundResource(R.drawable.bloodweapons);
            else
                //if not equipped
                weaponSelect[i].setVisibility(View.GONE);
            int finalI = i;

            //set weapon
            weaponSelect[i].setOnClickListener(view -> Char.setWeapon(finalI + 1));
        }
    }

    public void showHideWeaponSelectButtons(boolean show, int Ind, Weapons weapon) {
        if (show) {
            weaponSelect[Ind].setVisibility(View.VISIBLE);
            weaponSelect[Ind].setBackgroundResource(weapon.weaponIcon);
        } else
            weaponSelect[Ind].setVisibility(View.GONE);
    }

    //add blood weapons button to weapon selection tab
    public void addBloodWeapons(Game game) {
        weaponSelect[5] = new Button(game);
        selectWeapon.addView(weaponSelect[5]);
        weaponSelect[5].getLayoutParams().width = 130;
        weaponSelect[5].getLayoutParams().height = 130;
        weaponSelect[5].setPadding(1, 2, 1, 2);
        weaponSelect[5].setBackgroundResource(R.drawable.bloodweapons);
        weaponSelect[5].setOnClickListener(view -> Char.setWeapon(6));
    }


    //set to move character and change side
    public void characterMovement(int direction) {
        if (direction == JoyStickClass.STICK_UP)
            Char.charMovement(0, -4);
        else if (direction == JoyStickClass.STICK_UPRIGHT) {
            Char.charMovement(4, -4);
            charIMG.setScaleX(1);
        } else if (direction == JoyStickClass.STICK_RIGHT) {
            Char.charMovement(4, 0);
            charIMG.setScaleX(1);
        } else if (direction == JoyStickClass.STICK_DOWNRIGHT) {
            Char.charMovement(4, 4);
            charIMG.setScaleX(1);
        } else if (direction == JoyStickClass.STICK_DOWN)
            Char.charMovement(0, 4);
        else if (direction == JoyStickClass.STICK_DOWNLEFT) {
            Char.charMovement(-4, 4);
            charIMG.setScaleX(-1);
        } else if (direction == JoyStickClass.STICK_LEFT) {
            Char.charMovement(-4, 0);
            charIMG.setScaleX(-1);
        } else if (direction == JoyStickClass.STICK_UPLEFT) {
            Char.charMovement(-4, -4);
            charIMG.setScaleX(-1);
        }
    }

    //move character
    public void setCharImageLocation(int X, int Y) {
        charIMG.setX(X);
        charIMG.setY(Y);
    }

    //set character image by class / lord
    public void setCharacterImage(boolean lord, Elements ele) {
        if (!lord)
            charIMG.setImageResource(R.drawable.character);
        else {
            lordBtn.setBackgroundResource(R.drawable.alordbtn);
            if (ele == Elements.Fire)
                charIMG.setImageResource(R.drawable.firelord);
            else if (ele == Elements.Darkness)
                charIMG.setImageResource(R.drawable.darknesslord);
        }
    }

    //set weapon, weapons atk text and bonus
    public void setWeaponView(Weapons weapon, int weaponSkillLv) {

        //set weapon image
        weaponBtn.setBackgroundResource(weapon.weaponIcon);

        //get weapon attacks array from weapon
        WeaponAttacks[] attacks = weapon.Attacks;

        //check if mastery level high enough to show attacks buttons
        for (int b = 1; b < 3; b++)
            if (weaponSkillLv >= b * 10)
                atksBtn[b + 1].setVisibility(View.VISIBLE);
            else
                atksBtn[b + 1].setVisibility(View.INVISIBLE);

        //set weapon attacks text
        for (int wt = 0; wt < 4; wt++)
            atksBtn[wt].setText(attacks[wt].attackName);

        //hide weapon select bar
        selectWeapon.setVisibility(View.GONE);

        //enable/disable atk buttons base on mp
        mpChecker(attacks, 1, 4);
    }

    //show select weapon menu
    public void setWeaponSelect() {
        if (selectWeapon.getVisibility() == View.GONE) {
            selectWeapon.setVisibility(View.VISIBLE);
            weaponBtn.setBackgroundResource(R.drawable.redbutton2);
        } else
            Char.setWeapon(0);
    }

    //set character element,element atk text and bonus
    public void setElementView(Elements element, Skills eleSkill) {

        //set element image
        elementBtn.setBackgroundResource(element.elementIcon);

        //get weapon attacks array from weapon
        ElementAttacks[] attacks = element.Attacks;

        //check if knowledge level high enough to show attacks buttons
        for (int b = 1; b < 3; b++)
            if (eleSkill.skillLevel >= b * 10)
                atksBtn[b + 4].setVisibility(View.VISIBLE);
            else
                atksBtn[b + 4].setVisibility(View.INVISIBLE);

        //set element attacks text
        for (int et = 4; et < 7; et++)
            atksBtn[et].setText(attacks[et - 4].attackName);

        mpChecker(attacks, 4, 7);
    }

    //disable attack button if in cooldown
    public void disableAttackButton(int btn) {
        atksBtn[btn].setEnabled(false);
        atksBtn[btn].setBackgroundResource(R.drawable.dbluebutton);
    }

    //enable or disable attack button if not enough mp
    public void mpChecker(Attack[] attack, int from, int to) {
        for (int i = from; i < to; i++)
            if (i < 4) {
                //weapon attack
                if (Char.getCharMP() >= attack[i].attackManaCost())
                    setButtonEnabledAndBackground(atksBtn[i], R.drawable.redbutton, true);
                else
                    setButtonEnabledAndBackground(atksBtn[i], R.drawable.dredbutton, false);
            } else {
                //elements attack
                if (Char.getCharMP() >= attack[i - 4].attackManaCost() && !checkIfInCooldown(i))
                    setButtonEnabledAndBackground(atksBtn[i], R.drawable.bluebutton, true);
                else
                    setButtonEnabledAndBackground(atksBtn[i], R.drawable.dbluebutton, false);
            }
    }

    private void setButtonEnabledAndBackground(Button Button, int Background, boolean Enabled) {
        Button.setBackgroundResource(Background);
        Button.setEnabled(Enabled);
    }

    private boolean checkIfInCooldown(int atk) {
        int charEle = Char.getCharCurrentElementTypeNum();
        for (int[] acd : attacksCooldownList)
            if (acd[0] == charEle && acd[1] == atk)
                return true;
        return false;
    }

    //set icon for lord mode by process
    public void setLordBtn() {
        if (Char.getLordStats(1) == 0) {
            if (Char.getLordStats(0) < 250)
                lordBtn.setBackgroundResource(R.drawable.lord0of4btn);
            else if (Char.getLordStats(0) < 500)
                lordBtn.setBackgroundResource(R.drawable.lord1of4btn);
            else if (Char.getLordStats(0) < 750)
                lordBtn.setBackgroundResource(R.drawable.lord2of4btn);
            else if (Char.getLordStats(0) < 1000)
                lordBtn.setBackgroundResource(R.drawable.lord3of4btn);
            else if (Char.getLordStats(0) == 1000)
                lordBtn.setBackgroundResource(R.drawable.lord4of4btn);
        } else if (Char.getLordStats(1) < 0)
            lordBtn.setBackgroundResource(R.drawable.dlordbtn);
    }


    public void setSkillsView(List<Skills> skills, List<Mobs> mobs) {
        //set skillsV gridview
        skillsAdapter.notifyDataSetChanged();
        skillsGrid.setOnItemClickListener((parent, view, pos, id) -> SkillsActions.activeSkillsActions(skills.get(pos), Char, this, Char.getGameUI(), mobs));
    }

    //open or close skills view
    public void showSkillView() {
        if (skillTabBtn.getVisibility() == View.VISIBLE) {
            skillTabBtn.setVisibility(View.GONE);
            skillsGrid.setVisibility(View.VISIBLE);
        } else {
            skillTabBtn.setVisibility(View.VISIBLE);
            skillsGrid.setVisibility(View.GONE);
        }
    }

    //update skills gridview
    public void updateSkillsView() {
        skillsAdapter.notifyDataSetChanged();
    }

    //create image for activated buff skill
    private void createActivatedSkillIcon(Game game, int ind) {
        activeSkillsIMG[ind] = new ImageView(game);
        skillsTab.addView(activeSkillsIMG[ind]);
        activeSkillsIMG[ind].setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        activeSkillsIMG[ind].setImageResource(SkillsEnum.valueOf(Char.getCharSkillsList().get(2).get(ind).skillID).getSkillImage());
        activeSkillsIMG[ind].setPadding(1, 1, 1, 1);
    }

    //show activated buff skill
    public void showHideActivatedSkillIcon(boolean show, int ind, Game game) {
        if (show) {
            if (activeSkillsIMG[ind] == null)
                createActivatedSkillIcon(game, ind);
            activeSkillsIMG[ind].setVisibility(View.VISIBLE);
        } else
            activeSkillsIMG[ind].setVisibility(View.GONE);
    }

    public void updateHPMPText() {
        charHPTxt.setText(Char.statsMixer(3) + "/" + Char.getCharHP());
        charMPTxt.setText(Char.statsMixer(4) + "/" + Char.getCharMP());
    }

    public void setCharNameAndLevel() {
        charNameNLvtxt.setText(Char.getCharName() + "/" + Char.getCharLevel());
    }

    public void updateHPText() {
        charHPTxt.setText(Char.statsMixer(3) + "/" + Char.getCharHP());
    }

    public void updateMPText() {
        charMPTxt.setText(Char.statsMixer(4) + "/" + Char.getCharMP());
    }

    public void updateXPText(int requiredXP, int currentXP) {
        charXPTxt.setText(requiredXP + "/" + currentXP);
    }

    public void setCharImageSide() {
        charIMG.setScaleX(-1 * charIMG.getScaleX());
    }

    public ImageView getCharIMG() {
        return charIMG;
    }

    public int getCharImgHeight() {
        return charIMG.getHeight();
    }

    public int getCharImgWidth() {
        return charIMG.getWidth();
    }

    public void loadBtnText(String txt) {
        lordBtn.setText(txt);
    }

    public float getCharImageSide() {
        return charIMG.getScaleX();
    }
}