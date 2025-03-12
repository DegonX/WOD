package com.degonx.warriorsofdeagon.Enums.AreaEnums;

import com.degonx.warriorsofdeagon.R;

public enum NPC {

    Merchant_StarterCamp(Areas.StarterCamp, R.drawable.merchant, 1380, 980),
    Storage_StarterCamp(Areas.StarterCamp, R.drawable.storage, 2180, 1650),
    Blacksmith_StarterCamp(Areas.StarterCamp, R.drawable.blacksmith, 1950, 1080),
    Jeweler_StarterCamp(Areas.StarterCamp, R.drawable.jeweler, 2600, 1080),
    QuestGiver_StarterCamp(Areas.StarterCamp, R.drawable.questgiver, 3050, 1450);

    final Areas npcArea;
    final int npcImage;
    final int npcX;
    final int npcY;

    NPC(Areas npcArea, int npcImage, int npcX, int npcY) {
        this.npcArea = npcArea;
        this.npcImage = npcImage;
        this.npcX = npcX;
        this.npcY = npcY;
    }

    public Areas getNpcArea() {
        return npcArea;
    }

    public int getNpcImage() {
        return npcImage;
    }

    public int getNpcX() {
        return npcX;
    }

    public int getNpcY() {
        return npcY;
    }
}
