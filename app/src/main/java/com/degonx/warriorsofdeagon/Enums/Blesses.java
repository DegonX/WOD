package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.R;

public enum Blesses {

    /*
    HP, HP, Critical Rate @Devo
    HP, HP, Critical Damage @Tend
    HP, MP, Defence @Kind
    HP, MP, Bonus XP @Timi
    HP, MP, Critical Rate @Inno
    HP, Attack, Critical Rate @Chee
    HP, Defence, Bonus XP @Gent
    MP, MP, Critical Damage @Ador
    MP, Defence, Critical Rate @Imma
    Attack, Attack, Critical Damage @Ener
    Attack, Defence, Bonus XP @Frie
    Attack, MP, Bonus XP @Play
    Attack, Critical Rate, Critical Damage @Boyi
    Defence, Defence, Critical Rate @Matu
    Defence, Critical Rate, Critical Damage @Tsun
    */

    Chee(R.color.chee, new Stats[]{Stats.HP, Stats.ATTACK, Stats.CRITICAL_RATE}),
    Inno(R.color.inno, new Stats[]{Stats.MP, Stats.HP, Stats.CRITICAL_RATE}),
    Matu(R.color.matu, new Stats[]{Stats.DEFENCE, Stats.DEFENCE, Stats.CRITICAL_RATE}),
    Ener(R.color.ener, new Stats[]{Stats.ATTACK, Stats.ATTACK, Stats.CRITICAL_DAMAGE}),
    Timi(R.color.timi, new Stats[]{Stats.HP, Stats.MP, Stats.BONUS_XP}),
    Tsun(R.color.tsun, new Stats[]{Stats.DEFENCE, Stats.CRITICAL_RATE, Stats.CRITICAL_DAMAGE}),
    Ador(R.color.ador, new Stats[]{Stats.MP, Stats.MP, Stats.CRITICAL_DAMAGE}),
    Devo(R.color.devo, new Stats[]{Stats.HP, Stats.HP, Stats.CRITICAL_RATE}),
    Frie(R.color.frie, new Stats[]{Stats.ATTACK, Stats.DEFENCE, Stats.BONUS_XP}),
    Play(R.color.play, new Stats[]{Stats.ATTACK, Stats.MP, Stats.BONUS_XP}),
    Gent(R.color.gent, new Stats[]{Stats.HP, Stats.DEFENCE, Stats.BONUS_XP}),
    Kind(R.color.kind, new Stats[]{Stats.HP, Stats.MP, Stats.DEFENCE}),
    Imma(R.color.imma, new Stats[]{Stats.MP, Stats.DEFENCE, Stats.CRITICAL_RATE}),
    Boyi(R.color.boyi, new Stats[]{Stats.ATTACK, Stats.CRITICAL_RATE, Stats.CRITICAL_DAMAGE}),
    Tend(R.color.tend, new Stats[]{Stats.HP, Stats.HP, Stats.CRITICAL_DAMAGE});

    public final int Color;
    public final Stats[] blessStats;

    Blesses(int color, Stats[] stats) {
        Color = color;
        blessStats = stats;
    }
}