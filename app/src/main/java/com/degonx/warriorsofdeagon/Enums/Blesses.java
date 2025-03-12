package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.R;

public enum Blesses {

    /*
    Blesses adds list

    HP - MP @Inno
    HP - Attack @Calm
    HP - Defense @Gent
    HP - Critical Rate @Chee
    HP - Critical Damage @Devo
    HP - XP Bonus @Timi
    *
    MP - Attack @Happ
    MP - Defense @Kind
    MP - Critical Rate
    MP - Critical Damage @Chil
    MP - XP Bonus @Ador
    *
    Attack - Defense @Frie
    Attack - Critical Rate @Boyi
    Attack - Critical Damage @Ener
    Attack - XP Bonus
    *
    Defense - Critical Rate @Matu
    Defense - Critical Damage @Tsun
    Defense - XP Bonus @Imma
    */


    Chee(R.color.chee, "HP", "Critical Rate"),
    Inno(R.color.inno, "MP", "HP"),
    Matu(R.color.matu, "Defense", "Critical Rate"),
    Ener(R.color.ener, "Attack", "Critical Damage"),
    Timi(R.color.timi, "HP", "XP Bonus"),
    Tsun(R.color.tsun, "Defense", "Critical Damage"),
    Ador(R.color.ador, "MP", "XP Bonus"),
    Devo(R.color.devo, "HP", "Critical Damage"),
    Frie(R.color.frie, "Attack", "Defense"),
    Chil(R.color.chil, "MP", "Critical Damage"),
    Gent(R.color.gent, "HP", "Defense"),
    Happ(R.color.happ, "MP", "Attack"),
    Kind(R.color.kind, "Defense", "MP"),
    Imma(R.color.imma, "Defense", "XP Bonus"),
    Boyi(R.color.boyi, "Attack", "Critical Rate"),
    Calm(R.color.calm, "HP", "Attack");

    public final int Color;
    public final String Add1;
    public final String Add2;

    Blesses(int color, String add1, String add2) {
        Color = color;
        Add1 = add1;
        Add2 = add2;
    }
}
