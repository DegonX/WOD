package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.Adapters.SkillsAdapter;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.SkillsData;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Skills;
import com.degonx.warriorsofdeagon.R;

import java.util.List;

@SuppressLint("SetTextI18n")
public class CharacterSkillsUI {

    private final TextView skillPointTxt;
    private final ConstraintLayout skillLay;
    private Skills skillHolder;

    private final Character Char;

    public CharacterSkillsUI(Game game, Character Char) {
        this.Char = Char;

        //load skills page views
        skillLay = game.findViewById(R.id.skillspageL);
        skillPointTxt = game.findViewById(R.id.skillpoints);
        ImageView skillIMG = game.findViewById(R.id.skillimg);
        TextView skillInfo = game.findViewById(R.id.skillinfo);
        TextView skillNameNlv = game.findViewById(R.id.skillnameNlv);
        Button skillLvUpBtn = game.findViewById(R.id.skillup);
        skillLvUpBtn.setVisibility(View.INVISIBLE);
        CheckBox skillCB = game.findViewById(R.id.showskill);

        //finds all skills gridview
        GridView[] skillsGV = new GridView[5];
        skillsGV[0] = game.findViewById(R.id.skillsG1);
        skillsGV[1] = game.findViewById(R.id.skillsG2);
        skillsGV[2] = game.findViewById(R.id.skillsG3);
        skillsGV[3] = game.findViewById(R.id.skillsG4);
        skillsGV[4] = game.findViewById(R.id.skillsG5);

        //get all skills from character
        List<List<Skills>> skills = Char.getCharSkillsList();

        //add character skills to their gridview
        for (int st = 0; st < 5; st++) {
            skillsGV[st].setAdapter(new SkillsAdapter(game, skills.get(st)));

            //set skills gridview onlclick
            int finalSt = st;
            skillsGV[st].setOnItemClickListener((parent, view, i, id) -> {
                skillHolder = skills.get(finalSt).get(i);
                showSkillData(skillIMG, skillNameNlv, skillInfo, skillLvUpBtn, skillCB);
            });
        }

        //skills levelup button
        skillLvUpBtn.setOnClickListener(v -> {
            //check if skill selected
            if (skillHolder != null)
                //check if character have skill points
                if (Char.getCharSkillPoints() > 0)
                    //check if skill is not maxed
                    if (skillHolder.skillLevel < SkillsData.getSkillMaxLevel(skillHolder.skillID)) {

                        //level up skill and update views
                        Char.levelUpSkill(skillHolder);
                        skillPointTxt.setText(String.valueOf(Char.getCharSkillPoints()));
                        skillNameNlv.setText(SkillsData.getSkillName(skillHolder.skillID) + "\n" + (skillHolder.skillLevel) + "/" + SkillsData.getSkillMaxLevel(skillHolder.skillID));

                        //show\hide checkbox if active
                        if (skillHolder.skillLevel == 1)
                            setSkillCBView(skillCB);

                    } else
                        Toasts.makeToast(game, "Skill is already maxed");
                else
                    Toasts.makeToast(game, "No skills points to use");
            else
                Toasts.makeToast(game, "You need to select skill first");
        });
    }

    private void showSkillData(ImageView skillIMG, TextView skillNameNlv, TextView skillInfo, Button skillLvUpBtn, CheckBox skillCB) {
        //set skill image,name,level and info
        skillIMG.setImageResource(SkillsData.getSkillImage(skillHolder.skillID));
        skillNameNlv.setText(SkillsData.getSkillName(skillHolder.skillID) + "\n"
                + (skillHolder.skillLevel) + "/" + SkillsData.getSkillMaxLevel(skillHolder.skillID));
        skillInfo.setText(SkillsData.getSkillData(skillHolder.skillID));

        //check if can show checkbox
        setSkillCBView(skillCB);

        //set to add/remove skill from skills tab
        skillCB.setOnClickListener(sk -> {
            skillCB.setChecked(!skillHolder.skillInTab);
            Char.showHideSkillView(skillHolder, skillCB.isChecked());
        });

        //show level up button if skill can be leveled
        if (skillHolder.skillLevel < SkillsData.getSkillMaxLevel(skillHolder.skillID))
            skillLvUpBtn.setVisibility(View.VISIBLE);
        else
            skillLvUpBtn.setVisibility(View.INVISIBLE);
    }

    //show\hide skill check box
    private void setSkillCBView(CheckBox skillCB) {
        if (skillHolder.skillID / 100000 == 3 && skillHolder.skillLevel > 0 || skillHolder.skillID / 100000 == 5 && skillHolder.skillLevel > 0) {
            skillCB.setVisibility(View.VISIBLE);
            skillCB.setChecked(skillHolder.skillInTab);
        } else
            skillCB.setVisibility(View.GONE);
    }

    //show skills page and update skill points
    public void showSkillsPage() {
        skillLay.setVisibility(View.VISIBLE);
        skillPointTxt.setText(String.valueOf(Char.getCharSkillPoints()));
    }
}
