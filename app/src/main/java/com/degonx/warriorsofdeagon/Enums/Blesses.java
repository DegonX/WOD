package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.R;

public enum Blesses {

    /*
    Blesses adds list

    HP - MP @Inno
    HP - Attack @Chee
    HP - Defense @Gent
    HP - Critical Rate @Kind
    HP - Critical Damage @Devo
    HP - XP Bonus @Timi
    *
    MP - Attack @Play
    MP - Defense @Calm
    MP - Critical Rate @Happ
    MP - Critical Damage
    MP - XP Bonus @Ador
    *
    Attack - Defense @Frie
    Attack - Critical Rate @Ener
    Attack - Critical Damage @Boyi
    Attack - XP Bonus
    *
    Defense - Critical Rate @Matu
    Defense - Critical Damage @Tsun
    Defense - XP Bonus @Imma
    */

    Chee(R.color.chee, "HP", "Attack"),
    Inno(R.color.inno, "MP", "HP"),
    Matu(R.color.matu, "Defense", "Critical Rate"),
    Ener(R.color.ener, "Attack", "Critical Rate"),
    Timi(R.color.timi, "HP", "XP Bonus"),
    Tsun(R.color.tsun, "Defense", "Critical Damage"),
    Ador(R.color.ador, "MP", "XP Bonus"),
    Devo(R.color.devo, "HP", "Critical Damage"),
    Frie(R.color.frie, "Attack", "Defense"),
    Play(R.color.play, "MP", "Attack"),
    Gent(R.color.gent, "HP", "Defense"),
    Happ(R.color.happ, "MP", "Critical Rate"),
    Kind(R.color.kind, "HP", "Critical Rate"),
    Imma(R.color.imma, "Defense", "XP Bonus"),
    Boyi(R.color.boyi, "Attack", "Critical Damage"),
    Calm(R.color.calm, "MP", "Defense");

    public final int Color;
    public final String Add1;
    public final String Add2;

    Blesses(int color, String add1, String add2) {
        Color = color;
        Add1 = add1;
        Add2 = add2;
    }
}
