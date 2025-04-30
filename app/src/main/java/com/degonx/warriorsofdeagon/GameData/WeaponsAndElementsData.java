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
            case Blood_Weapons:
                return new String[]{
                        "Create blood fist with your blood and punch an enemy",
                        "Create blood claw with your blood and slash an enemy",
                        "Drains an enemy's blood and heal yourself",
                        "Create a giant hand that grabs an enemy and crushing it"};
            case Fists:
                return new String[]{
                        "Punch an enemy",
                        "Throw a bullet fast punch",
                        "Throw multiple punches",
                        "Focus your rage to preform a powerful punch"};
            default:
                return null;
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
            default:
                return null;
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