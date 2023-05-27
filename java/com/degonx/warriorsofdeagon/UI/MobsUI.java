package com.degonx.warriorsofdeagon.UI;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.Enums.MobsType;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.MobsData;
import com.degonx.warriorsofdeagon.Objects.Bosses;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.R;

public class MobsUI {

    private final TableLayout mobsView;
    private final TextView mobsHPTxt;
    private final ImageView[] mobsIMGS = new ImageView[4];//mobsIMGS - mob's image + 3*effect icons

    public MobsUI(int mobMaxHP, Game game, GameUI gameui, Mobs mob, int areaID) {
        mobsView = new TableLayout(game);
        gameui.addRemoveFromGameLayout(mobsView, true);

        //create container from hp and elemental effects icons
        TableLayout mobup = new TableLayout(game);
        mobsView.addView(mobup);

        //create tablerow for elemental effects icons
        TableRow Teffect = new TableRow(game);
        mobup.addView(Teffect);

        //create and set mob hp text
        mobsHPTxt = new TextView(game);
        mobup.addView(mobsHPTxt);
        mobsHPTxt.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        Teffect.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        mobsHPTxt.setTextColor(Color.RED);
        mobsHPTxt.setText(String.valueOf(mobMaxHP));
        mobsHPTxt.setBackgroundResource(R.color.tsilver);

        //create and set mob image
        mobsIMGS[0] = new ImageView(game);
        mobsView.addView(mobsIMGS[0]);
        mobsIMGS[0].setImageResource(MobsData.getMobsImg(areaID));

        //create icons for elemental effects
        for (int i = 1; i < 4; i++) {
            mobsIMGS[i] = new ImageView(game);
            Teffect.addView(mobsIMGS[i]);
            mobsIMGS[i].setVisibility(View.INVISIBLE);
            mobsIMGS[i].getLayoutParams().height = 50;
            mobsIMGS[i].getLayoutParams().width = 50;
        }
        mobsIMGS[1].setImageResource(R.drawable.burn);
        mobsIMGS[2].setImageResource(R.drawable.freeze);
        mobsIMGS[3].setImageResource(R.drawable.paralysis);

        //set mobs and mobs hp size
        if (mob.getClass() != Bosses.class) {
            mobsIMGS[0].getLayoutParams().height = 300;
            mobsIMGS[0].getLayoutParams().width = 250;
            mobsHPTxt.setTextSize(20);
        } else {
            mobsIMGS[0].getLayoutParams().height = 800;
            mobsIMGS[0].getLayoutParams().width = 900;
            mobsHPTxt.setTextSize(30);
        }
    }

    //move mobs image
    public void setMobMovement(int side, int mobX, int mobY) {
        mobsView.setX(mobX);
        mobsView.setY(mobY);
        mobsIMGS[0].setScaleX(side);
    }

    //show\hide mobs view
    public void showMobView(boolean show) {
        if (show)
            mobsView.setVisibility(View.VISIBLE);
        else
            mobsView.setVisibility(View.GONE);
    }

    //show or hide mobs images and icons
    public void showHideImages(int index, boolean vis) {
        if (vis)
            mobsIMGS[index].setVisibility(View.VISIBLE);
        else
            mobsIMGS[index].setVisibility(View.INVISIBLE);
    }

    //set mobs HP text
    public void setMobHPText(int HP) {
        if (HP > 0)
            mobsHPTxt.setText(String.valueOf(HP));
        else
            mobsHPTxt.setText("");
    }

    //set mob image color by mob type
    public void setMobElementColor(MobsType type) {
        if (type == MobsType.Fire)
            mobsIMGS[0].setColorFilter(Color.argb(70, 255, 0, 0));
        else if (type == MobsType.Water)
            mobsIMGS[0].setColorFilter(Color.argb(70, 0, 0, 255));
        else if (type == MobsType.Earth)
            mobsIMGS[0].setColorFilter(Color.argb(70, 200, 100, 0));
        else if (type == MobsType.Wind)
            mobsIMGS[0].setColorFilter(Color.argb(70, 190, 190, 190));
        else if (type == MobsType.Lightning)
            mobsIMGS[0].setColorFilter(Color.argb(70, 255, 255, 0));
        else if (type == MobsType.Light)
            mobsIMGS[0].setColorFilter(Color.argb(70, 255, 255, 150));
        else if (type == MobsType.Darkness)
            mobsIMGS[0].setColorFilter(Color.argb(70, 60, 60, 60));
        else if (type == MobsType.Metal)
            mobsIMGS[0].setColorFilter(Color.argb(70, 100, 100, 100));
    }

    public View getMobView() {
        return mobsView;
    }

    public int getMobHeight() {
        return mobsView.getHeight();
    }

    public int getMobWidth() {
        return mobsView.getWidth();
    }
}
