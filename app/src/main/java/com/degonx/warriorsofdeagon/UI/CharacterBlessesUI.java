package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.degonx.warriorsofdeagon.Enums.Blesses;
import com.degonx.warriorsofdeagon.Enums.Stats;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.BlessesData;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.R;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@SuppressLint("SetTextI18n")
public class CharacterBlessesUI {


    private final TextView blessPointTxt, activeBlessInfo;
    private final ConstraintLayout blessesLay;
    private final LinearLayout blessesScroll;
    private final Button[][] blessesBtn;

    private final int[] activeBlessesIndexes;

    private final Character Char;
    private final Game game;

    public CharacterBlessesUI(Game game, Character Char, int[] blessesLevels, int[] activeBlesses) {
        this.game = game;
        this.Char = Char;
        this.activeBlessesIndexes = activeBlesses;

        //create buttons to use and level up
        blessesBtn = new Button[blessesLevels.length][2];

        //load all blesses page view
        blessesLay = game.findViewById(R.id.blessesL);
        blessPointTxt = game.findViewById(R.id.BPointsT);
        blessesScroll = game.findViewById(R.id.blessesScroll);
        activeBlessInfo = game.findViewById(R.id.activeBlessInfo);

        //create blesses views
        for (int b = 0; b < blessesLevels.length; b++)
            setBlessInBlessesPage(b, blessesLevels, Blesses.values()[b]);
    }

    //set blesses views
    private void setBlessInBlessesPage(int blessIndex, int[] blessLevel, Blesses bless) {
        //load blessView inflater layout
        LayoutInflater inflater = LayoutInflater.from(game);
        View blessView = inflater.inflate(R.layout.blessview, blessesScroll, false); // inflate into blessesScroll later

        //add the blessView to the blessesscroll
        blessesScroll.addView(blessView);

        //get the views from blessview
        TextView blessNameTxt = blessView.findViewById(R.id.blessNameTxt);
        TextView blessStatsTxt = blessView.findViewById(R.id.blessStatsTxt);
        blessesBtn[blessIndex][0] = blessView.findViewById(R.id.blessActivationBtn);
        blessesBtn[blessIndex][1] = blessView.findViewById(R.id.blessLevelUpBtn);
        LinearLayout blessInfoView = blessView.findViewById(R.id.blessInfoView);

        //set bless name and background color
        blessNameTxt.setText(bless.name());
        blessInfoView.setBackgroundResource(bless.Color);

        //set bless stats text
        setBlessText(blessStatsTxt, bless, blessLevel[blessIndex]);

        //set activation button
        if (getBlessIndex(blessIndex) > -1) {
            blessesBtn[blessIndex][0].setText("Deactivate");
            blessInfoView.setBackground(getBorder(bless.Color, true));
        } else
            blessesBtn[blessIndex][0].setText("Activate");

        //activate \ deactivate bless
        blessesBtn[blessIndex][0].setOnClickListener(view -> {
            if (blessLevel[blessIndex] > 0)
                activateButtonAction(blessIndex, blessInfoView, bless.Color);
            else
                Toasts.makeToast(game, "You need to level up this bless first");
        });

        //set level up button
        if (blessLevel[blessIndex] < 50) {
            int cost = (100 + (50 * blessLevel[blessIndex])) * (1 + (blessLevel[blessIndex] / 10));
            blessesBtn[blessIndex][1].setText("Level up Bless\n(" + cost + ")");
            blessesBtn[blessIndex][1].setOnClickListener(view -> Char.levelUpBless(blessIndex, blessStatsTxt));
        } else
            //hide the levelup button if maxed
            blessesBtn[blessIndex][1].setVisibility(View.GONE);
    }

    public void activateButtonAction(int blessIndex, LinearLayout blessInfo, int color) {
        //get the bless index if active(-1 is not activate)
        int activeSlot = getBlessIndex(blessIndex);

        //if bless is activate
        if (activeSlot != -1) {
            //deactivate the bless
            Char.blessActions(blessIndex, -1, activeSlot, true);

            //set active button text to activate
            blessesBtn[blessIndex][0].setText("Activate");

            //remove red border
            blessInfo.setBackground(getBorder(color, false));
        } else {

            //get empty spot for the bless(-1 no empty spot)
            int spot = getEmptyBlessSpot();

            if (spot != -1) {
                //activate bless
                Char.blessActions(blessIndex, 1, getEmptyBlessSpot(), true);

                //set active button text to deactivate
                blessesBtn[blessIndex][0].setText("Deactivate");

                //add red border
                blessInfo.setBackground(getBorder(color, true));
            } else
                Toasts.makeToast(game, "cannot use more than 4 blesses at once");
        }
    }

    private void setBlessText(TextView blessInfo, Blesses bless, int blessLevel) {
        //create string builder and add bless level
        StringBuilder blessInfoStr = new StringBuilder("Lv." + blessLevel);

        Map<Stats, Float> statTotals = new LinkedHashMap<>();

        //sum if there is duplicate stat
        for (Stats s : bless.blessStats) {
            //get the stat multiplied by the level
            float value = BlessesData.getBlessStats(s, blessLevel);
            statTotals.put(s, statTotals.getOrDefault(s, 0f) + value);
        }

        //add the stat to the text
        for (Map.Entry<Stats, Float> stat : statTotals.entrySet()) {
            Stats s = stat.getKey();
            float statSum = stat.getValue();

            if (s.equals(Stats.ATTACK) || s.equals(Stats.DEFENCE) || s.equals(Stats.HP) || s.equals(Stats.MP))
                blessInfoStr.append("\n").append(s.statName).append(":").append((int) statSum);
            else if (s.equals(Stats.CRITICAL_RATE) || s.equals(Stats.CRITICAL_DAMAGE))
                blessInfoStr.append("\n").append(s.statName).append(":").append(String.format(Locale.ENGLISH, "%.2f", statSum)).append("%");
            else if (s.equals(Stats.BONUS_XP))
                blessInfoStr.append("\n").append(s.statName).append(":").append((int) statSum).append("%");
        }

        //set the text into bless text view
        blessInfo.setText(blessInfoStr);
    }

    //create \ remove border around activated blesses
    private Drawable getBorder(int originalBackgroundColor, boolean addBorder) {
        //create the original background
        Drawable backgroundDrawable = ContextCompat.getDrawable(game, originalBackgroundColor);

        if (addBorder) {
            //create the border
            GradientDrawable border = new GradientDrawable();
            border.setColor(Color.TRANSPARENT);
            border.setStroke(12, Color.GREEN);

            //combine border and background
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{border, backgroundDrawable});
            layerDrawable.setLayerInset(1, 12, 12, 12, 12); // Padding to show border

            return layerDrawable;
        } else
            //remove border
            return backgroundDrawable;
    }

    //set text of activated blesses
    public void setActiveBlessesText(int active, int[] blessesStats, float[] blessesCriticalStats) {
        //format float values to prevent inaccurate text values
        String formattedRate = String.format(Locale.US, "%.1f", blessesCriticalStats[0]);
        String formattedDamage = String.format(Locale.US, "%.1f", blessesCriticalStats[1]);

        //set text
        activeBlessInfo.setText("Active blesses:" + active + "\nattack:" + blessesStats[0] + ", defense:" + blessesStats[1] +
                ", hp:" + blessesStats[2] + ", mp:" + blessesStats[3] + "\ncritical rate:" + formattedRate +
                ", critical damage:" + formattedDamage + ", exp bonus:" + blessesStats[4]);
    }

    //update bless after level up
    public void setBlessAfterLevelUp(int blessIndex, int blessLevel, TextView blessInfo) {
        //update bless points
        blessPointTxt.setText("Bless Points:" + Char.getCharBlessPoints());

        //update bless text
        setBlessText(blessInfo, Blesses.values()[blessIndex], blessLevel);

        //update \ hide level up button
        if (blessesBtn[blessIndex][1] != null)
            if (blessLevel < 50)
                blessesBtn[blessIndex][1].setText("Level up Bless\n(" + (100 + (50 * blessLevel)) * (1 + (blessLevel / 10)) + ")");
            else
                blessesBtn[blessIndex][1].setVisibility(View.GONE);
    }

    //show blesses page and update points
    public void showBlessesPage() {
        blessesLay.setVisibility(View.VISIBLE);
        blessPointTxt.setText("Bless Points:" + Char.getCharBlessPoints());
    }

    private int getBlessIndex(int blessIndex) {
        for (int b = 0; b < 4; b++)
            if (activeBlessesIndexes[b] == blessIndex)
                return b;
        return -1;
    }

    private int getEmptyBlessSpot() {
        int spot = -1;
        for (int b = 0; b < 4; b++)
            if (activeBlessesIndexes[b] == -1) {
                spot = b;
                break;
            }
        return spot;
    }
}