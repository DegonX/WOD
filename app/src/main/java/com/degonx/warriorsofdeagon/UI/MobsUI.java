package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.Enums.MobsType;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Objects.Bosses;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.R;

public class MobsUI {

    private final LinearLayout mobView;
    private final TextView mobHPTxt;
    private final ImageView mobIMG;
    private final ImageView[] mobEffectIcons = new ImageView[2];
    private final TableRow effectsTR;

    @SuppressLint("InflateParams")
    public MobsUI(int mobMaxHP, Game game, GameUI gameui, Mobs mob, int mobImage) {

        //create mob view and add it to the game
        LayoutInflater layoutInflater = (LayoutInflater) game.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mobView = (LinearLayout) layoutInflater.inflate(R.layout.mobview, null);
        gameui.addRemoveFromGameView(mobView, true);

        //find and set mob views
        mobIMG = mobView.findViewById(R.id.mobImage);
        mobIMG.setImageResource(mobImage);
        mobHPTxt = mobView.findViewById(R.id.mobHPTxt);
        mobHPTxt.setText(String.valueOf(mobMaxHP));

        //finds tablerow of effects icons
        effectsTR = mobView.findViewById(R.id.effectsTR);
        mobEffectIcons[0] = mobView.findViewById(R.id.mobBurnIcon);
        mobEffectIcons[1] = mobView.findViewById(R.id.mobFreezeIcon);

        //set mob and mob hp size
        if (mob.getClass() != Bosses.class) {
            mobIMG.getLayoutParams().height = 500;
            mobIMG.getLayoutParams().width = 300;
            mobHPTxt.setTextSize(20);
        } else {
            mobIMG.getLayoutParams().height = 900;
            mobIMG.getLayoutParams().width = 1000;
            mobHPTxt.setTextSize(30);
        }
    }

    //move mob image
    public void setMobMovement(int side, int mobX, int mobY) {
        mobView.setX(mobX);
        mobView.setY(mobY);
        mobIMG.setScaleX(side);
    }

    //show\hide mob view
    public void showMobView(boolean show) {
        if (show)
            mobView.setVisibility(View.VISIBLE);
        else
            mobView.setVisibility(View.GONE);
    }

    //show or hide mob images and icons
    public void showHideImages(int index, boolean vis) {
        if (vis)
            mobEffectIcons[index].setVisibility(View.VISIBLE);
        else
            mobEffectIcons[index].setVisibility(View.INVISIBLE);
    }

    //set mob HP text
    public void setMobHPText(int HP) {
        if (HP > 0)
            mobHPTxt.setText(String.valueOf(HP));
        else
            mobHPTxt.setText("");
    }

    //set mob image color by mob type
    public void setMobElementColor(MobsType type) {
        if (type == MobsType.Fire)
            mobIMG.setColorFilter(Color.argb(70, 255, 0, 0));
        else if (type == MobsType.Water)
            mobIMG.setColorFilter(Color.argb(70, 0, 0, 255));
        else if (type == MobsType.Earth)
            mobIMG.setColorFilter(Color.argb(70, 200, 100, 0));
        else if (type == MobsType.Wind)
            mobIMG.setColorFilter(Color.argb(70, 190, 190, 190));
        else if (type == MobsType.Lightning)
            mobIMG.setColorFilter(Color.argb(70, 255, 255, 0));
        else if (type == MobsType.Light)
            mobIMG.setColorFilter(Color.argb(70, 255, 255, 150));
        else if (type == MobsType.Darkness)
            mobIMG.setColorFilter(Color.argb(70, 60, 60, 60));
        else if (type == MobsType.Metal)
            mobIMG.setColorFilter(Color.argb(70, 100, 100, 100));
    }

    public View getMobView() {
        return mobView;
    }

    public View getMobImage() {
        return mobIMG;
    }

    public int getMobHeight() {
        return (int) (mobView.getHeight() - mobIMG.getX());
    }

    public int getMobWidth() {
        return mobView.getWidth();
    }

    public View getMobHPTxt() {
        return mobHPTxt;
    }

    public View getEffectsTR() {
        return effectsTR;
    }
}
