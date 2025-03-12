package com.degonx.warriorsofdeagon.Objects;

import android.widget.ImageView;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.NPC;

public class NPCs {

    private final ImageView npcImage;
    private final Areas npcArea;
    private final int npcX;
    private final int npcY;

    public NPCs(NPC npc, ImageView npcImage) {
        this.npcImage = npcImage;
        npcArea = npc.getNpcArea();
        npcX = npc.getNpcX();
        npcY = npc.getNpcY();
    }

    public Areas getNpcArea() {
        return npcArea;
    }

    public ImageView getNpcImage() {
        return npcImage;
    }

    public int getNpcX() {
        return npcX;
    }

    public int getNpcY() {
        return npcY;
    }
}
