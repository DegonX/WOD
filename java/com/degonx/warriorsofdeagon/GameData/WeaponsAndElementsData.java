package com.degonx.warriorsofdeagon.GameData;

import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.R;

public class WeaponsAndElementsData {

    public static String[] getWeaponAtkDescription(Weapons weapon) {
        switch (weapon) {
            case Spear:
                return new String[]{
                        "Swing that slash an enemy",
                        "Force stab enemies in front",
                        "Quickly stab an enemy 3 time",
                        "Wildly whirl your spear damaging nearby enemies"};
            case Poleaxe:
                return new String[]{
                        "Swing that slash enemies",
                        "Stab an enemy 3 time",
                        "Uppercut slash an enemy then slam it to the ground",
                        "Slam your poleaxe to the ground and damage the nearby enemies"};
            case Heavy_Sword:
                return new String[]{
                        "Unleash an heavily slash upon enemies",
                        "Uppercut slash an enemy then slam it to the ground",
                        "Spin your heavy sword causing damage to nearby enemies",
                        "Slam your heavy sword to the ground and damage the nearby enemies"};
            case Chokuto:
                return new String[]{
                        "Slash an enemy",
                        "Stab an enemy",
                        "Triple slash an enemy",
                        "Dash through enemies with a powerful slash"};
            case Daggers:
                return new String[]{
                        "Double slash an enemy",
                        "Slash through enemies cutting them X pattern",
                        "Unleash 8 slashes upon an enemy",
                        "Spin while holding out your daggers releasing wrath on enemies"};
            case Warhammer:
                return new String[]{
                        "Swing to hit an enemy",
                        "Smash into enemies and push them back",
                        "Slam a enemy to the ground",
                        "smack your warhammer to the ground and damage the nearby enemies"};
            case Blood_Weapons:
                return new String[]{
                        "Create blood fist with your blood and punch an enemy",
                        "Create blood claw with your blood and slash an enemy",
                        "Drains an enemy's blood and heal yourself",
                        "Create a giant hand that grabs an enemy and crushing it"};
            case Scythe:
                return new String[]{
                        "Swing that slash an enemy",
                        "Cut down Enemies",
                        "Spin your scythe causing damage to the nearby enemies",
                        "Wildly whirl your scythe damaging nearby enemies"};
            case Trident:
                return new String[]{
                        "Swing that slash an enemy",
                        "Force stab an enemy",
                        "Uppercut slash an enemy then slam it to the ground",
                        "Wildly whirl your trident damaging nearby enemies"};
            case Double_Bladed_Naginata:
                return new String[]{
                        "Swing that slash an enemy",
                        "Force stab enemies in front",
                        "Spin your double bladed naginata causing damage to the nearby enemies",
                        "Wildly whirl your double bladed naginata damaging nearby enemies"};
            case Whip_Sword:
                return new String[]{
                        "Whip and slash an enemies in front",
                        "Whip that stab enemies in front",
                        "Catch an enemy and whip it away",
                        "Wildly whip and slash your nearby enemies"};
            case Bow:
                return new String[]{
                        "Shoot an arrow",
                        "Shoot a powerful arrow",
                        "Shoot four arrows at once",
                        "Rain down arrows all over the area"};
            case Staff:
                return new String[]{
                        "Drains energy out of an enemy",
                        "Smack an enemy with your staff",
                        "Send magic spheres towards enemies in front",
                        "Shoot magic arrows all over the area"};
            default:
                return new String[]{
                        "Punch an enemy",
                        "Throw a bullet fast punch",
                        "Throw multiple punches",
                        "Focus your rage to preform a powerful punch"};
        }
    }

    public static String[] getElementAtkDescription(Elements element) {
        switch (element) {
            case Fire:
                return new String[]{
                        "Send flaming phoenix towards an enemy, may burn the enemy",
                        "Rush towards enemies pushing them back, may burn the enemy",
                        "Release a fire blast impacting the surrounding area damage and pushing back enemies, leaves overheat that damaging enemies every 1 second for 20 sec, may burn the enemy"};
            case Ice:
                return new String[]{
                        "Send icicles towards an enemy, may cause freeze",
                        "Throw ice stars towards enemies, may cause freeze",
                        "Create a giant iceberg to damage the entire area, may cause freeze"};
            case Energy:
                return new String[]{
                        "Release energy sphere that chases an enemy and explode on impact",
                        "Release energy beam towards enemies",
                        "Release energy blast impacting the surrounding area damage and pushing back enemies"};
            case Blood:
                return new String[]{
                        "Create blood spears and send them to stab an enemy",
                        "Sending blood chains to stab enemies in front",
                        "Showering blood needles all over the area"};
            case Darkness:
                return new String[]{
                        "Create a giant darkness hands to riping a part enemies in the surrounding area",
                        "Release darkness all over the area that feed on the enemies",
                        "Call down a beam of darkness upon the entire area damage enemies and leaves a fog that slows down the enemies movement and damaging them for 20 seconds"};
            case Lightning:
                return new String[]{
                        "Throwing lightning spear towards an enemy, may cause paralyze",
                        "Release lightning jolts causing damage to the nearby enemies, may cause paralyze",
                        "Calls a lightning storm that strikes the enemies in the area, there is a chance to strike enemies again every 1 seconds for 20 seconds, may cause paralyze"};
            case Earth:
                return new String[]{
                        "Shooting rock bullets towards the enemies in front",
                        "Release pillars from the ground damage enemies in the surrounding area",
                        "Call down a Meteor to damage the entire area"};
//            case Wind:
            default:
                return new String[]{
                        "Release wind that slashes enemies",
                        "Whirlwind that damages the surrounding area and pushing back the enemies, whirlwind will keep damaging the nearby enemies for 20 seconds",
                        "form a tornado that clustering enemies to the middle of the area and damages them for 20 seconds"};
        }
    }

    public static int getProjectilesImage(ElementAttacks attack) {
        switch (attack) {
            case FIRE_PHOENIX:
                return R.drawable.phoenix;
            case ICE_ICICLES:
                return R.drawable.icicles;
            case ENERGY_SPHERE:
                return R.drawable.energysphere;
            case BLOOD_BLOOD_SPEARS:
                return R.drawable.bloodspears;
            default:
                return 0;
        }
    }

    public static int getUnlockLevel(int atk) {
        if (atk == 3 || atk == 6)
            return 10;
        else if (atk == 4 || atk == 7)
            return 20;
        return 0;
    }
}