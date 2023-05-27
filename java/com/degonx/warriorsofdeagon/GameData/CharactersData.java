package com.degonx.warriorsofdeagon.GameData;


public class CharactersData {

    public static int XPToLevelUP(int CharLevel) {
        int lvXpMul = 0;
        if (CharLevel > 0 && CharLevel < 20)
            lvXpMul = 20;
        else if (CharLevel >= 20 && CharLevel < 50)
            lvXpMul = 51;
        else if (CharLevel >= 50 && CharLevel < 70)
            lvXpMul = 72;
        else if (CharLevel >= 70 && CharLevel < 100)
            lvXpMul = 103;
        else if (CharLevel >= 100 && CharLevel < 150)
            lvXpMul = 154;
        else if (CharLevel < 200)
            lvXpMul = 205;
        return (24 * CharLevel) + lvXpMul * CharLevel;
    }
}
