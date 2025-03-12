package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.Enums.Blesses;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.BlessesData;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.R;

import java.util.Locale;

@SuppressLint("SetTextI18n")
public class CharacterBlessesUI {


    private final TextView blessPointTxt, blessCreditsTxt, activeBlessInfo;
    private final ConstraintLayout blessesLay;
    private final LinearLayout blessesScroll;
    private final Button[][] blessesBtn;

    private final Character Char;
    private final Game game;

    public CharacterBlessesUI(Game game, Character Char, int[][] blesses) {
        this.game = game;
        this.Char = Char;

        //create buttons to use and level up
        blessesBtn = new Button[blesses.length][2];

        //load all blesses page view
        blessesLay = game.findViewById(R.id.blessesL);
        blessPointTxt = game.findViewById(R.id.BPointsT);
        blessCreditsTxt = game.findViewById(R.id.USEPointsT);
        blessesScroll = game.findViewById(R.id.blessesScroll);
        activeBlessInfo = game.findViewById(R.id.activeBlessInfo);

        //create blesses views
        for (int b = 0; b < blesses.length; b++)
            setBlessInBlessesPage(b, blesses[b][0]);
    }

    //set blesses views
    private void setBlessInBlessesPage(int blessInd, int blessLV) {
        //create layout and textview for each bless
        LinearLayout newBlessLay = new LinearLayout(game);
        newBlessLay.setOrientation(LinearLayout.VERTICAL);
        blessesScroll.addView(newBlessLay);
        TextView blessInfo = new TextView(game);
        newBlessLay.addView(blessInfo);
        blessInfo.setTextSize(25);
        blessInfo.setTextColor(Color.BLACK);

        //get bless by index
        Blesses bless = Blesses.values()[blessInd];

        //set bless text and background color
        setBlessText(blessInfo, bless, blessLV);
        blessInfo.setBackgroundResource(bless.Color);

        //create and set bless use button
        final int finalInd = blessInd;

        //create and set bless level up button
        blessesBtn[blessInd][0] = new Button(game);
        newBlessLay.addView(blessesBtn[blessInd][0]);
        blessesBtn[blessInd][0].setText("Use");
        blessesBtn[blessInd][0].setOnClickListener(view -> Char.attemptToUseBless(finalInd));

        //create and set bless level up button
        if (blessLV < 30) {
            blessesBtn[blessInd][1] = new Button(game);
            newBlessLay.addView(blessesBtn[blessInd][1]);
            blessesBtn[blessInd][1].setText("Level up Bless\n(" + BlessesData.blessLevelCost(blessLV) + ")");
            blessesBtn[blessInd][1].setOnClickListener(view -> Char.levelUpBless(finalInd, blessInfo));
        }
    }

    private void setBlessText(TextView blessInfo, Blesses bless, int blessLV) {

        //make the text of the bless
        String blessInfoStr = "    " + bless;
        blessInfoStr += "\nLv.:" + blessLV + "\n";

        //bless 1st add
        String add = bless.Add1;
        if (add.equals("Attack") || add.equals("Defense"))
            blessInfoStr += add + ":" + (int) (BlessesData.getBlessBaseValue(add, blessLV) * 100) + " \n";
        else
            blessInfoStr += add + ":" + (int) BlessesData.getBlessBaseValue(add, blessLV) + " \n";


        //bless 2nd add
        add = bless.Add2;
        if (!add.equals("Critical Rate") && !add.equals("XP Bonus")) {

            if (add.equals("Defense"))
                blessInfoStr += add + ":" + (int) (BlessesData.getBlessBaseValue(add, blessLV) * 100) + " ";
            else
                blessInfoStr += add + ":" + (int) BlessesData.getBlessBaseValue(add, blessLV) + " ";

        } else
            blessInfoStr += add + ":" + String.format(Locale.ENGLISH, "%.2f", (BlessesData.getBlessBaseValue(add, blessLV))) + " ";


        //set the text into bless text view
        blessInfo.setText(blessInfoStr);
    }

    //show blesses page and update points
    public void showBlessesPage() {
        blessesLay.setVisibility(View.VISIBLE);
        blessCreditsTxt.setText("Use Points:" + Char.getCharBlessUse());
        blessPointTxt.setText("Bless Points:" + Char.getCharBlessPoints());
    }

    //update bless after been leveled
    public void setBlessAfterLevelUp(int blessInd, int blessLV, TextView blessInfo) {
        //update bless points
        blessPointTxt.setText("Bless Points:" + Char.getCharBlessPoints());

        //update bless text
        setBlessText(blessInfo, Blesses.values()[blessInd], blessLV);

        //update \ hide level up button
        if (blessesBtn[blessInd][1] != null)
            if (blessLV < 30)
                blessesBtn[blessInd][1].setText("Level up Bless\n(" + BlessesData.blessLevelCost(blessLV) + ")");
            else
                blessesBtn[blessInd][1].setVisibility(View.GONE);
    }

    //set text of activated blesses
    public void setActiveBlessesText(int active, double[] blessesStats) {
        activeBlessInfo.setText("Active blesses:" + active +
                "\nattack:" + (int) (blessesStats[0] * 100) + ", defense:" + (int) (blessesStats[1] * 100) + ", hp:" + (int) blessesStats[2] + ", mp:" + (int) blessesStats[3] +
                "\ncritical rate:" + blessesStats[4] + ", critical damage:" + (int) blessesStats[5] + ", exp bonus:" + blessesStats[6]);
    }
}
