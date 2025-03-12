package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Mob;

public enum QuestsEnum {

    DEVIL_ELIMINATE("Devils are about to attack the camp, eliminate 20 of them will send them back", Mob.Mob1_Dungeon1, 20),
    WEREWOLF_ELIMINATE("Werewolves spotted nearby, eliminate 30 of them", Mob.Mob2_Dungeon2, 30),
    BALOR_ELIMINATE("The Balor is awaken, go eliminate it", Mob.Boss1_Dungeon10, 1);

    final String questText;
    final Mob questMob;
    final int questAmount;

    QuestsEnum(String questText, Mob questMob, int questAmount) {
        this.questText = questText;
        this.questMob = questMob;
        this.questAmount = questAmount;
    }

    public String getQuestText() {
        return questText;
    }

    public Mob getQuestMob() {
        return questMob;
    }

    public int getQuestAmount() {
        return questAmount;
    }
}
