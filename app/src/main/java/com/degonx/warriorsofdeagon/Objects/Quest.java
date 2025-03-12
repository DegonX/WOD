package com.degonx.warriorsofdeagon.Objects;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Mob;
import com.degonx.warriorsofdeagon.UI.GameUI;

public class Quest {

    Mob mobType;
    int Total;
    int Amount;
    boolean finished = false;
    GameUI gameUI;

    public Quest(Mob mobType, int Total, GameUI gameUI) {
        this.mobType = mobType;
        this.Total = Total;
        this.gameUI = gameUI;
    }

    public void updateQuest() {
        if (!finished) {
            Amount++;
            gameUI.setChat("enemies eliminated " + Amount + "/" + Total);
            if (Amount >= Total) {
                finished = true;
                gameUI.setChat("Quest Complete");
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
