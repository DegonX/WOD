package com.degonx.warriorsofdeagon.GameData;

import com.degonx.warriorsofdeagon.R;

/* how to give skill an ID

    the 1st digit is the type of the skill
    1 - weapon mastery, 2 - element Knowledge, 3 - active skill , 4 - passive skill, 5 - attack skill

    2nd to 4rd digit for the elements that adds the skill
    1 - fire, 2 - ice, 3 - energy, 4 - blood, 5 - darkness, 6 - lightning, 7 - earth, 8 - wind

    last 2 digits for ordering
    */

public class SkillsData {

    public static int getSkillImage(int SkillID) {
        switch (SkillID / 100000) {
            case 1:
                switch (SkillID) {
                    case 100000:
                        return R.drawable.fisterrage;
                    case 100001:
                        return R.drawable.smspear;
                    case 100002:
                        return R.drawable.smpoleaxe;
                    case 100003:
                        return R.drawable.smheavysword;
                    case 100004:
                        return R.drawable.smchokuto;
                    case 100005:
                        return R.drawable.smdaggers;
                    case 100006:
                        return R.drawable.smwarhammer;
                    case 100007:
                        return R.drawable.smbloodweapons;
                    case 100008:
                        return R.drawable.smscythe;
                    case 100009:
                        return R.drawable.smtrident;
                    case 100010:
                        return R.drawable.smdoublebladednaginata;
                    case 100011:
                        return R.drawable.smwhipsword;
                    case 100012:
                        return R.drawable.smbow;
                    case 100013:
                        return R.drawable.smstaff;
                }
                break;
            case 2:
                switch (SkillID) {
                    case 200001:
                        return R.drawable.smfire;
                    case 200002:
                        return R.drawable.smice;
                    case 200003:
                        return R.drawable.smenergy;
                    case 200004:
                        return R.drawable.smblood;
                    case 200005:
                        return R.drawable.smdarkness;
                    case 200006:
                        return R.drawable.smlightning;
                    case 200007:
                        return R.drawable.smearth;
                    case 200008:
                        return R.drawable.smwind;
                }
                break;
            case 3:
                switch (SkillID) {
                    case 300000:
                        return R.drawable.scompanion;
                    case 300001:
                        return R.drawable.dmgndefup;
                    case 300002:
                        return R.drawable.dmgup;
                    case 300003:
                        return R.drawable.defup;
                    case 300004:
                        return R.drawable.critup;
                    case 330001:
                        return R.drawable.heal;
                    case 330002:
                        return R.drawable.manaheal;
                    case 399900:
                        return R.drawable.magebarrier;
                    case 399901:
                        return R.drawable.freeze;
                }
                break;
            case 4:
                switch (SkillID) {
                    case 400001:
                        return R.drawable.dmgup;
                    case 400002:
                        return R.drawable.defup;
                    case 400003:
                        return R.drawable.hpup;
                    case 400004:
                        return R.drawable.mpup;
                    case 400005:
                        return R.drawable.extraatk;
                    case 440001:
                        return R.drawable.bloodweaponsup;
                    case 440002:
                        return R.drawable.bloodheal;
                    case 430001:
                    case 499900:
                    case 499901:
                    case 499902:
                    case 499903:
                        return R.drawable.manahealing;
                    case 410001:
                        return R.drawable.burn;
                    case 420001:
                        return R.drawable.freeze;
                    case 460001:
                        return R.drawable.paralysis;
                    case 415801:
                        return R.drawable.avoid;
                }
                break;
            case 5:
                switch (SkillID) {
                    case 500000:
                        return R.drawable.gpunch;
                    case 535001:
                        return R.drawable.armageddon;
                    case 512601:
                        return R.drawable.triatk;
                }
                break;
        }
        return R.drawable.skillc;
    }

    public static String getSkillName(int SkillID) {
        switch (SkillID / 100000) {
            case 1:
                switch (SkillID) {
                    case 100000:
                        return "Fister's Rage";
                    case 100001:
                        return "Spear Mastery";
                    case 100002:
                        return "Poleaxe Mastery";
                    case 100003:
                        return "Heavy Sword Mastery";
                    case 100004:
                        return "Chokuto Mastery";
                    case 100005:
                        return "Daggers Mastery";
                    case 100006:
                        return "Warhammer Mastery";
                    case 100007:
                        return "Blood Weapons Mastery";
                    case 100008:
                        return "Scythe Mastery";
                    case 100009:
                        return "Trident Mastery";
                    case 100010:
                        return "Double Bladed Naginata Mastery";
                    case 100011:
                        return "Whip Sword Mastery";
                    case 100012:
                        return "Bow Mastery";
                    case 100013:
                        return "Staff Mastery";
                    default:
                        return null;
                }
            case 2:
                switch (SkillID) {
                    case 200001:
                        return "Fire Knowledge";
                    case 200002:
                        return "Ice Knowledge";
                    case 200003:
                        return "Energy Knowledge";
                    case 200004:
                        return "Blood Knowledge";
                    case 200005:
                        return "Darkness Knowledge";
                    case 200006:
                        return "Lightning Knowledge";
                    case 200007:
                        return "Earth Knowledge";
                    case 200008:
                        return "Wind Knowledge";
                    default:
                        return null;
                }
            case 3:
                switch (SkillID) {
                    case 300000:
                        return "Summon Companion";
                    case 300001:
                        return "Power Boost";
                    case 300002:
                        return "Damage Up";
                    case 300003:
                        return "Defence Up";
                    case 300004:
                        return "Critical Up";
                    case 330001:
                        return "Heal";
                    case 330002:
                        return "Mana Regenerate";
                    case 399900:
                        return "god mode";
                    case 399901:
                        return "stop mobs";
                    default:
                        return null;
                }
            case 4:
                switch (SkillID) {
                    case 400001:
                        return "More Damage";
                    case 400002:
                        return "More Defence";
                    case 400003:
                        return "More HP";
                    case 400004:
                        return "More MP";
                    case 400005:
                        return "Extra Attack";
                    case 440001:
                        return "Blood Boost";
                    case 440002:
                        return "Blood Healing";
                    case 430001:
                        return "Energy Healing";
                    case 410001:
                        return "Burn";
                    case 420001:
                        return "Freeze";
                    case 460001:
                        return "Paralysis";
                    case 415801:
                        return "Avoid";
                    case 499900:
                        return "drop test";
                    case 499901:
                        return "skills test";
                    case 499902:
                        return "lord test";
                    case 499903:
                        return "bless test";
                    default:
                        return null;
                }
            case 5:
                switch (SkillID) {
                    case 500000:
                        return "Ground Punch";
                    case 535001:
                        return "Darkness Explosion";
                    case 512601:
                        return "Elemental Rage";
                }
        }
        return null;
    }

    public static String getSkillData(int SkillID) {
        switch (SkillID / 100000) {
            case 1:
                switch (SkillID) {
                    case 100000:
                        return "Fister base damage increase by 10 per skill level";
                    case 100001:
                        return "Spear base damage increase by 5 per skill level";
                    case 100002:
                        return "Poleaxe base damage increase by 5 per skill level";
                    case 100003:
                        return "Heavy Sword base damage increase by 5 per skill level";
                    case 100004:
                        return "Chokuto base damage increase by 5 per skill level";
                    case 100005:
                        return "Daggers base damage increase by 5 per skill level";
                    case 100006:
                        return "Warhammer base damage increase by 5 per skill level";
                    case 100007:
                        return "Blood Weapons base damage increase by 10 per skill level";
                    case 100008:
                        return "Scythe base damage increase by 5 per skill level";
                    case 100009:
                        return "Trident base damage increase by 5 per skill level";
                    case 100010:
                        return "Double Bladed Naginata base damage increase by 5 per skill level";
                    case 100011:
                        return "Whip Sword base damage increase by 5 per skill level";
                    case 100012:
                        return "Bow base damage increase by 5 per skill level";
                    case 100013:
                        return "Staff base damage increase by 5 per skill level";
                }
                break;
            case 2:
                switch (SkillID) {
                    case 200001:
                        return "Fire base damage increase by 5 per skill level";
                    case 200002:
                        return "Ice base damage increase by 5 per skill level";
                    case 200003:
                        return "Energy base damage increase by 5 per skill level";
                    case 200004:
                        return "Blood base damage increase by 5 per skill level";
                    case 200005:
                        return "Darkness base damage increase by 5 per skill level";
                    case 200006:
                        return "Lightning base damage increase by 5 per skill level";
                    case 200007:
                        return "Earth base damage increase by 5 per skill level";
                    case 200008:
                        return "Wind base damage increase by 5 per skill level";
                }
                break;
            case 3:
                switch (SkillID) {
                    case 300000:
                        return "Summon Companion to help battling, make you stronger and summon healing familiars";
                    case 300001:
                        return "increases Damage and Defence";
                    case 300002:
                        return "increases Damage";
                    case 300003:
                        return "increases Defence";
                    case 300004:
                        return "increases Critical Chance";
                    case 330001:
                        return "Restore some lost Health";
                    case 330002:
                        return "Regenerate some Used Mana";
                    case 399900:
                        return "mobs won't damage character, ON/OFF";
                    case 399901:
                        return "stop mobs movements, ON/OFF";
                }
                break;
            case 4:
                switch (SkillID) {
                    case 400001:
                        return "Rises your Damage by 10 per skill level";
                    case 400002:
                        return "Rises your Defence by 10 per skill level";
                    case 400003:
                        return "Rises your HP by 100 per skill level";
                    case 400004:
                        return "Rises your MP by 100 per skill level";
                    case 400005:
                        return "Grants a chance to attack once more";
                    case 440001:
                        return "Improves your Blood Weapons and make them stronger";
                    case 440002:
                        return "Your Blood helps you to heal";
                    case 430001:
                        return "Your Energy helps restoring your mana";
                    case 410001:
                        return "Chance to burn enemy when attacking";
                    case 420001:
                        return "Chance to Freeze enemy when attacking";
                    case 460001:
                        return "Chance to paralyse enemy when attacking";
                    case 415801:
                        return "A chance to avoid enemy attacking";
                    case 499900:
                        return "60%+ drop rate";
                    case 499901:
                        return "skills while not cost MP";
                    case 499902:
                        return "getting lords points 10 times faster";
                    case 499903:
                        return "get bless use from mobs";
                }
                break;
            case 5:
                switch (SkillID) {
                    case 500000:
                        return "Unleash your rage on the field and cause damage to surrounding enemies";
                    case 535001:
                        return "Feed Energy to your Darkness and cause a massive explosion";
                    case 512601:
                        return "The power of the Elements combines and rages, causing damage surrounding enemies and may cause them to burn,freeze or paralyse";
                }
                break;
        }
        return null;
    }

    public static int getSkillMaxLevel(int SkillID) {
        if (SkillID / 100000 <= 2)
            return 30;
        else if (SkillID / 100000 == 3 || SkillID / 100000 == 5)
            return 10;
        else if (SkillID / 100000 == 4)
            return 40;
        return 1;
    }

    public static int getActivetedSkillImage(int num) {
        if (num == 1)
            return R.drawable.dmgndefup;
        else if (num == 2)
            return R.drawable.dmgup;
        else if (num == 3)
            return R.drawable.defup;
        else if (num == 4)
            return R.drawable.critup;
        return 0;
    }
}
