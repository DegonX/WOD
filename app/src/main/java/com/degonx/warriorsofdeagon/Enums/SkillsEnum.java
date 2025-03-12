package com.degonx.warriorsofdeagon.Enums;

import com.degonx.warriorsofdeagon.R;

public enum SkillsEnum {
    FISTERS_RAGE("Fister's Rage", "weapon_mastery-Fists", "Fister damage increase by 10 per skill level", R.drawable.fisterrage, 30),
    SPEAR_MASTERY("Spear Mastery", "weapon_mastery-Spear", "Spear damage increase by 5 per skill level", R.drawable.smspear, 30),
    POLEAXE_MASTERY("Poleaxe Mastery", "weapon_mastery-Poleaxe", "Poleaxe damage increase by 5 per skill level", R.drawable.smpoleaxe, 30),
    HEAVY_SWORD_MASTERY("Heavy Sword Mastery", "weapon_mastery-Heavy_Sword", "Heavy Sword damage increase by 5 per skill level", R.drawable.smheavysword, 30),
    CHOKUTO_MASTERY("Chokuto Mastery", "weapon_mastery-Chokuto", "Chokuto damage increase by 5 per skill level", R.drawable.smchokuto, 30),
    DAGGERS_MASTERY("Daggers Mastery", "weapon_mastery-Daggers", "Daggers damage increase by 5 per skill level", R.drawable.smdaggers, 30),
    WARHAMMER_MASTERY("Warhammer Mastery", "weapon_mastery-Warhammer", "Warhammer damage increase by 5 per skill level", R.drawable.smwarhammer, 30),
    BLOOD_WEAPONS_MASTERY("Blood Weapons Mastery", "weapon_mastery-Blood_Weapons", "Blood Weapons damage increase by 10 per skill level", R.drawable.smbloodweapons, 30),
    FIRE_KNOWLEDGE("Fire Knowledge", "element_knowledge-Fire", "Fire damage increase by 5 per skill level", R.drawable.smfire, 30),
    ICE_KNOWLEDGE("Ice Knowledge", "element_knowledge-Ice", "Ice damage increase by 5 per skill level", R.drawable.smice, 30),
    ENERGY_KNOWLEDGE("Energy Knowledge", "element_knowledge-Energy", "Energy damage increase by 5 per skill level", R.drawable.smenergy, 30),
    BLOOD_KNOWLEDGE("Blood Knowledge", "element_knowledge-Blood", "Blood damage increase by 5 per skill level", R.drawable.smblood, 30),
    DARKNESS_KNOWLEDGE("Darkness Knowledge", "element_knowledge-Darkness", "Darkness damage increase by 5 per skill level", R.drawable.smdarkness, 30),
    POWER_BOOST("Power Boost", "active_skill", "increases Damage and Defense", R.drawable.dmgndefup, 10),
    DAMAGE_UP("Damage Up", "active_skill", "increases Damage", R.drawable.dmgup, 10),
    DEFENSE_UP("Defense Up", "active_skill", "increases Defense", R.drawable.defup, 10),
    CRITICAL_UP("Critical Up", "active_skill", "increases Critical Chance", R.drawable.critup, 10),
    HEAL("Heal", "active_skill", "Restore some lost Health", R.drawable.heal, 10),
    MANA_REGENERATE("Mana Regenerate", "active_skill", "Regenerate some Used Mana", R.drawable.manaheal, 10),
    GOD_MODE("god mode", "active_skill", "mobs won't damage character, ON/OFF", R.drawable.magebarrier, 10),
    CHAR_SPEED("char speed", "active_skill", "move 2 times faster", R.drawable.avoid, 1),
    DAMAGE1("1 damage", "active_skill", "1 damage from attacks", R.drawable.manahealing, 1),
    STOP_MOBS("stop mobs", "active_skill", "stop mobs movements, ON/OFF", R.drawable.freeze, 10),
    MORE_DAMAGE("More Damage", "passive_skill", "Rises your Damage by 20 per skill level", R.drawable.dmgup, 40),
    MORE_DEFENSE("More Defense", "passive_skill", "Rises your Defense by 20 per skill level", R.drawable.defup, 40),
    MORE_HP("More HP", "passive_skill", "Rises your HP by 200 per skill level", R.drawable.hpup, 40),
    MORE_MP("More MP", "passive_skill", "Rises your MP by 200 per skill level", R.drawable.mpup, 40),
    EXTRA_ATTACK("Extra Attack", "passive_skill", "Grants a chance to attack once more", R.drawable.extraatk, 40),
    BLOOD_BOOST("Blood Boost", "passive_skill", "Improves your Blood Weapons and make them stronger", R.drawable.bloodweaponsup, 40),
    BLOOD_HEALING("Blood Healing", "passive_skill", "Your Blood helps you to heal", R.drawable.bloodheal, 40),
    ENERGY_HEALING("Energy Healing", "passive_skill", "Your Energy helps restoring your mana", R.drawable.manahealing, 40),
    BURN("Burn", "passive_skill", "Chance to burn enemy when attacking", R.drawable.burn, 40),
    FREEZE("Freeze", "passive_skill", "Chance to Freeze enemy when attacking", R.drawable.freeze, 40),
    AVOID("Avoid", "passive_skill", "A chance to avoid enemy attacking", R.drawable.avoid, 40),
    DROP_TEST("drop test", "passive_skill", "60%+ drop rate", R.drawable.manahealing, 1),
    SKILLS_TEST("skills test", "passive_skill", "skills while not cost MP", R.drawable.manahealing, 1),
    LORD_TEST("lord test", "passive_skill", "getting lords points 10 times faster", R.drawable.manahealing, 1),
    BLESS_TEST("bless test", "passive_skill", "get bless use from mobs", R.drawable.manahealing, 1),
    GROUND_PUNCH("Ground Punch", "attack_skill", "Unleash your rage on the field and cause damage to surrounding enemies", R.drawable.gpunch, 40),
    DARKNESS_EXPLOSION("Darkness Explosion", "attack_skill", "Feed Energy to your Darkness and cause a massive explosion", R.drawable.armageddon, 10);

    final String skillName;
    final String skillType;
    final String skillDescription;
    final int skillImage;
    final int skillMaxLevel;

    SkillsEnum(String skillName, String skillType, String skillDescription, int skillImage, int skillMaxLevel) {
        this.skillName = skillName;
        this.skillType = skillType;
        this.skillDescription = skillDescription;
        this.skillImage = skillImage;
        this.skillMaxLevel = skillMaxLevel;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillType() {
        return skillType;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public int getSkillImage() {
        return skillImage;
    }

    public int getSkillMaxLevel() {
        return skillMaxLevel;
    }
}