package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.R;

public enum Blesses {

    /*
    Blesses stats list

    HP,MP,Attack @Happ
    HP,MP,Defence @Calm
    HP,Attack,Defence @Kind
    HP,MP,Critical Rate @Inno
    HP,MP,Critical Damage
    HP,Attack,Critical Rate @Chee
    HP,Attack,Critical Damage
    HP,Defence,Critical Rate @Matu
    HP,Defence,Critical Damage @Tsun
    HP,Critical Rate,Critical Damage @Devo
    HP,MP,Bonus XP @Timi
    HP,Attack,Bonus XP
    HP,Defence,Bonus XP @Gent
    HP,Critical Rate,Bonus XP
    HP,Critical Damage,Bonus XP @Tend

    MP,Attack,Defence
    MP,Attack,Critical Rate
    MP,Attack,Critical Damage @Play
    MP,Defence,Critical Rate
    MP,Defence,Critical Damage @Imma
    MP,Critical Rate,Critical Damage @Ador
    MP,Attack,Bonus XP
    MP,Defence,Bonus XP
    MP,Critical Rate,Bonus XP
    MP,Critical Damage,Bonus XP

    Attack,Defence,Critical Rate @Frie
    Attack,Defence,Critical Damage
    Attack,Critical Rate,Critical Damage @Ener
    Attack,Defence,Bonus XP
    Attack,Critical Rate,Bonus XP
    Attack,Critical Damage,Bonus XP @Boyi

    Defence,Critical Rate,Critical Damage
    Defence,Critical Rate,Bonus XP
    Defence,Critical Damage,Bonus XP

    */

    Chee(R.color.chee, new Stats[]{Stats.HP, Stats.ATTACK, Stats.CRITICAL_RATE}),
    Inno(R.color.inno, new Stats[]{Stats.HP, Stats.MP, Stats.CRITICAL_RATE}),
    Matu(R.color.matu, new Stats[]{Stats.HP, Stats.DEFENCE, Stats.CRITICAL_RATE}),
    Ener(R.color.ener, new Stats[]{Stats.ATTACK, Stats.CRITICAL_RATE, Stats.CRITICAL_DAMAGE}),
    Timi(R.color.timi, new Stats[]{Stats.HP, Stats.MP, Stats.BONUS_XP}),
    Tsun(R.color.tsun, new Stats[]{Stats.HP, Stats.DEFENCE, Stats.CRITICAL_DAMAGE}),
    Ador(R.color.ador, new Stats[]{Stats.MP, Stats.CRITICAL_RATE, Stats.CRITICAL_DAMAGE}),
    Devo(R.color.devo, new Stats[]{Stats.HP, Stats.CRITICAL_RATE, Stats.CRITICAL_DAMAGE}),
    Frie(R.color.frie, new Stats[]{Stats.ATTACK, Stats.DEFENCE, Stats.CRITICAL_RATE}),
    Play(R.color.play, new Stats[]{Stats.MP, Stats.ATTACK, Stats.CRITICAL_DAMAGE}),
    Gent(R.color.gent, new Stats[]{Stats.HP, Stats.DEFENCE, Stats.BONUS_XP}),
    Happ(R.color.happ, new Stats[]{Stats.HP, Stats.MP, Stats.ATTACK}),
    Kind(R.color.kind, new Stats[]{Stats.HP, Stats.MP, Stats.DEFENCE}),
    Imma(R.color.imma, new Stats[]{Stats.MP, Stats.DEFENCE, Stats.CRITICAL_DAMAGE}),
    Boyi(R.color.boyi, new Stats[]{Stats.ATTACK, Stats.CRITICAL_DAMAGE, Stats.BONUS_XP}),
    Calm(R.color.calm, new Stats[]{Stats.HP, Stats.MP, Stats.DEFENCE}),
    Tend(R.color.tend, new Stats[]{Stats.HP, Stats.CRITICAL_DAMAGE, Stats.BONUS_XP});

    public final int Color;
    public final Stats[] blessStats;

    Blesses(int color, Stats[] stats) {
        Color = color;
        blessStats = stats;
    }
}