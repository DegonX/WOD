package com.degonx.warriorsofdeagon.Objects;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.Actions.AttacksActions;
import com.degonx.warriorsofdeagon.Actions.SkillsAdder;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.Blesses;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.AreaEffects;
import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEffects;
import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEnum;
import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.WeaponAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.BlessesData;
import com.degonx.warriorsofdeagon.UI.CharacterBlessesUI;
import com.degonx.warriorsofdeagon.UI.CharacterInventoryUI;
import com.degonx.warriorsofdeagon.UI.CharacterSkillsUI;
import com.degonx.warriorsofdeagon.UI.CharacterStatsUI;
import com.degonx.warriorsofdeagon.UI.CharacterUI;
import com.degonx.warriorsofdeagon.UI.GameUI;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.BlessesDB;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.EquipmentsDB;
import com.degonx.warriorsofdeagon.Database.SkillsDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class Character {

    private AccountsDB accDB;
    private CharactersDB charDB;
    private EquipmentsDB equipmentDB;
    private SkillsDB skillDB;
    private BlessesDB blessDB;

    private int accountID;
    private int Ores;
    private int charID;
    private int charLevel;
    private int charXP;
    private int charXPToLevelUp;
    private String charName;
    private int charMaxHP;
    private int charMaxMP;
    private int charHP = 1;
    private int charMP = 1;
    private int charDefense;
    private int charDamage;
    private double charCriticalRate;
    private int charCriticalDamage;
    private int charSpeed = 1;
    private int charX = 2530;
    private int charY = 1500;
    private int charHeight;
    private int charWidth;
    private int charSkillPoints;
    private int charBlessPoints;
    private int charHPpots;
    private int charMPpots;
    private Areas charArea;

    private int charCurrentWeaponIndex = 0;
    private Weapons charCurrentWeaponType;

    private int charCurrentElementIndex = 0;
    private Elements charCurrentElementType;

    private int activeBlessCounter = 0;
    private int activeBuffSkillsCounter = 0;

    private boolean charMoving = false;
    private boolean charAtkDelay = false;
    private boolean charHitDelay = true;
    private boolean charProjectileIsActive = false;

    private final Equipments[] charEquippedWeapons = new Equipments[3];
    private final Equipments[] charEquippedEquipments = new Equipments[9];
    private final int[] equipAdd = new int[4];//equipAdd - DMG + DEF + HP + MP

    private int[][] charBlesses; //charBlesses - level + times
    private final int[] charBlessUse = new int[2];//blessUse - blessusepoints + blessuses;
    private final double[] blessesAdd = new double[7];//blessesAdd - DMG + DEF + HP + MP + CRITRATE + CRITDMG + XP

    private final int[] charLordState = new int[2];//charLordState - process + time
    private final int[] lordAdd = new int[6];//lordAdd - DMG + DEF + HP + MP + CRITRATE + CRITDMG

    private final int[] charPassiveSkillsAdd = new int[11];
    private final int[][] charActiveSkillsAdd = new int[4][2];//activeSkillsAdd - power up + dmg up + def up + crit up / times
    private final int[] GMSkills = new int[7];

    private AreaEffects areaEffect;
    private int areaEffectTime = 0;

    private final Equipments[] charEquipments = new Equipments[80];
    private int inventoryCount = 0;
    private List<List<Skills>> charSkillsList;
    private final List<Skills> charTabSkills = new ArrayList<>();

    private Runnable projectilesRun;
    private Mobs projectileTarget;

    private final Random charRan = new Random();
    private final Handler charActionsHandler = new Handler();
    private final Handler charAttackDelayHandler = new Handler();
    private final Handler charAttackCooldownHandler = new Handler();
    private final List<int[]> charAttackCooldownList = new ArrayList<>();

    private Game game;
    private GameUI gameUI;
    private CharacterUI charUI;
    private List<Mobs> mobsList = new ArrayList<>();
    private CharacterStatsUI characterStatsUI;
    private CharacterInventoryUI characterInventoryUI;
    private CharacterSkillsUI characterSkillsUI;
    private CharacterBlessesUI characterBlessesUI;
    private List<Elements> charElements;
    private Quest quest;

    final int WEAPON_ATTACK_SOUND = 0;
    final int ELEMENT_ATTACK_SOUND = 1;
    final int LORD_START_SOUND = 2;

    private int HP_REGENERATION = 0;
    private int MP_REGENERATION = 0;

    public Character() {
    }

    //make connection with databases, create character UI, load character and set it to start playing
    public void setCharacterAtStart(Game game, GameUI gameUI, CharactersDB charDB) {
        this.game = game;
        this.gameUI = gameUI;
        Mobs.setCharLevel(charLevel);

        //connection with databases
        this.charDB = charDB;
        equipmentDB = new EquipmentsDB(game);
        skillDB = new SkillsDB(game);
        blessDB = new BlessesDB(game);

        //create Blesses if not exist
        if (!blessDB.checkIfBlessesExist(charID))
            blessDB.createCharBless(charID);

        //load character date from database
        charElements = charDB.getCharacterElements(charID);
        charSkillsList = skillDB.getCharSkills(charID);
        charBlesses = blessDB.getCharBlesses(charID);

        //get character equipments from database and sort them
        sortEquipments(equipmentDB.getCharEquipments(charID));

        //set x and y to area default spawn
        charX = charArea.getAreaSpawnX();
        charY = charArea.getAreaSpawnY();

        //create character UI and set his view
        charUI = new CharacterUI(this, game, gameUI, charElements, charAttackCooldownList);

        //create character menu pages
        characterStatsUI = new CharacterStatsUI(game, this, charSkillsList.get(0), charElements);
        characterInventoryUI = new CharacterInventoryUI(game, this, charEquipments, charEquippedEquipments, charEquippedWeapons);
        characterSkillsUI = new CharacterSkillsUI(game, this);
        characterBlessesUI = new CharacterBlessesUI(game, this, charBlesses);

        //load and set equipments and skills
        addEquipmentsStats();
        enablePassiveSkills();
        setSkillsTab();

        //set character texts
        charUI.setCharNameAndLevel();
        charUI.updateXPText(charXPToLevelUp, charXP);

        //set weapon selection by equipped weapons
        charUI.setupEquippedWeaponSelection(game, charEquippedWeapons, getWeaponMasteryLevel(SkillsEnum.BLOOD_WEAPONS_MASTERY));

        //set weapon and element
        setWeapon(0);
        setElement(charElements.get(0));

        //set ores
        Ores = accDB.getOres(accountID);

        //set joystick
        gameUI.joyStick();

        //send the user to choose additional element if not selected yet
        if (charLevel >= 50 && charElements.size() == 2)
            gameUI.selectAdditionalElement(charElements);

        //set HP and MP and their texts
        addCharHPMP(statsMixer(3), statsMixer(4));
        standHealing.start();

        //delay to get accurate sizes(height and width)
        new Handler().postDelayed(() -> {
            setCharSize();
            gameUI.scrollToChar(charX, charY);
        }, 150);
    }

    public void setWeapon(int weaponInd) {
        charCurrentWeaponIndex = weaponInd;

        //set weapon and his damage from mastery and base
        int equippedWeaponDamage;
        int weaponMasteryLevel;

        //if weapon is an equipped weapon
        if (weaponInd > 0 && weaponInd < 4) {

            //get weapon type
            charCurrentWeaponType = Weapons.valueOf(charEquippedWeapons[charCurrentWeaponIndex - 1].equipmentType.split("-")[1]);

            //get weapon skill mastery
            weaponMasteryLevel = getWeaponMasteryLevel(charCurrentWeaponType.weaponSkill) * 5;

            //get weapon damage
            equippedWeaponDamage = charEquippedWeapons[charCurrentWeaponIndex - 1].equipmentDMG;
        } else {

            //check if weapon is fists or blood weapons and set their damage
            if (charCurrentWeaponIndex == 0) {
                charCurrentWeaponType = Weapons.Fists;
                equippedWeaponDamage = 25 * (1 + charLevel / 10);
            } else {
                charCurrentWeaponType = Weapons.Blood_Weapons;
                equippedWeaponDamage = 40 * (1 + charLevel / 10);
            }

            //get weapon skill mastery
            weaponMasteryLevel = getWeaponMasteryLevel(charCurrentWeaponType.weaponSkill) * 10;
        }

        //set weapon attack bonus damage
        WeaponAttacks.weaponDamage = equippedWeaponDamage;

        //set weapon mastery bonus damage
        WeaponAttacks.bonusDamage = (float) ((1 + weaponMasteryLevel / 100) * (double) (1 + charLevel / 10));

        Log.i(TAG, "weapon:" + charCurrentWeaponType + ", weapon mastery:" + getWeaponMasteryLevel(charCurrentWeaponType.weaponSkill) + ", weapon damage:" + equippedWeaponDamage);

        //set weapon sound
        game.setWeaponSound(charCurrentWeaponType);

        //show weapon attack buttons by mastery level
        charUI.setWeaponView(charCurrentWeaponType, getWeaponMasteryLevel(charCurrentWeaponType.weaponSkill));
    }

    public void setElement(Elements element) {
        charCurrentElementType = element;

        //set element attack bonus damage
        ElementAttacks.bonusDamage = (float) ((1 + charSkillsList.get(1).get(charCurrentElementIndex).skillLevel / 100) * (1 + charLevel / 8));

        Log.i(TAG, "element:" + charCurrentElementType + ", element knowledge:" + charSkillsList.get(1).get(charCurrentElementIndex).skillLevel);

        //show element attack button by knowledge level
        charUI.setElementView(element, charSkillsList.get(1).get(charCurrentElementIndex));
    }

    //change character element if not in lord mode
    public void changeElement() {
        if (charLordState[1] <= 0) {
            charCurrentElementIndex = ++charCurrentElementIndex % charElements.size();
            setElement(charElements.get(charCurrentElementIndex));
        } else
            gameUI.setChat("cannot change element while in lord mode");
    }

    //attempt to attack mob
    public void startAttack(int atk) {
        //check if character alive and theres no attacking delay
        if (charHP > 0 && !charAtkDelay) {
            charAtkDelay = true;

            if (atk < 4) {
                //attack by weapon
                game.playSound(WEAPON_ATTACK_SOUND);

                //start weapon attack
                AttacksActions.rangeChecker(charCurrentWeaponType.Attacks[atk], charCurrentElementType, this, mobsList);

            } else {
                //attack by element
                game.playSound(ELEMENT_ATTACK_SOUND);

                //check if attack is projectile
                if (charCurrentElementType.Attacks[atk - 4].attackType != AttacksType.Homing)
                    AttacksActions.rangeChecker(charCurrentElementType.Attacks[atk - 4], charCurrentElementType, this, mobsList);
                else
                    projectileAttack(charCurrentElementType.Attacks[atk - 4]);

                //add attack to cooldown list
                charAttackCooldownList.add(new int[]{charCurrentElementType.elementNum, atk, charCurrentElementType.Attacks[atk - 4].attackCooldown});
                charUI.disableAttackButton(atk);

                //start \ reset cooldown runnable(timer)
                charAttackCooldownHandler.removeCallbacks(attackCooldownRun);
                attackCooldownRun.run();

                //start area effect if possible
                if (charCurrentElementType.Attacks[atk - 4].areaEffect != AreaEffects.None)
                    startAreaEffect(charCurrentElementType.Attacks[atk - 4].areaEffect);
            }

            //attack delay timer
            charAttackDelayHandler.postDelayed(() -> charAtkDelay = false, 150);
        }
    }

    private void projectileAttack(ElementAttacks attack) {
        //reduce MP by attack cost
        addCharMP(-attack.attackManaCost);

        for (Mobs mob : mobsList)
            if (!mob.isMobIsDead()) {

                projectileTarget = mob;

                //setting projectile
                gameUI.setProjectiles(attack);
                charProjectileIsActive = true;

                //set projectile runnable
                if (projectilesRun == null) {
                    projectilesRun = () -> {
                        if (!game.Pause && charProjectileIsActive) {

                            //move projectile and check if it hits his target
                            if (gameUI.moveProjectiles(projectileTarget)) {

                                //damage target and end projectile
                                AttacksActions.damageEnhance(attack, this, projectileTarget, charCurrentElementType);
                                endProjectile();

                                //end projectile if target is dead
                            } else if (projectileTarget.isMobIsDead())
                                endProjectile();

                            // 0.03 second delay(movement speed)
                            charActionsHandler.postDelayed(projectilesRun, 30);
                        }
                    };
                }

                //start projectile runnable
                projectilesRun.run();
                break;
            }
    }

    private void endProjectile() {
        gameUI.hideProjectiles();
        charActionsHandler.removeCallbacks(projectilesRun);
        charProjectileIsActive = false;
    }

    //start area effect
    private void startAreaEffect(AreaEffects effect) {
        //check if there is no other area effect on
        if (areaEffectTime == 0) {

            //start area effect
            areaEffect = effect;
            areaEffectTime = effect.getTime();
            gameUI.setChat(effect.getStartMessage());
            gameUI.showHideAreaEffectView(effect);

            //add darkness fog effect (slow down mobs)
            if (effect == AreaEffects.DarknessFog)
                for (Mobs m : mobsList)
                    m.setMobSpeed(false);

            //start area effect timer(runnable)
            areaEffectRun.run();
        }
    }

    //runnable for area effect
    private final Runnable areaEffectRun = new Runnable() {
        @Override
        public void run() {
            if (areaEffectTime > 0 && !game.Pause) {

                //execute area effect action
                areaEffectActions();

                //disable area effect when timed out
                if (--areaEffectTime == 0)
                    endAreaEffect();

                charActionsHandler.postDelayed(areaEffectRun, 1000);
            }
        }
    };

    //execute activate area effect every 1 sec
    private void areaEffectActions() {
        for (Mobs m : mobsList)
            if (!m.isMobIsDead()) {

                //overheat - damage mob every 1 second
                if (areaEffect == AreaEffects.Overheat)
                    m.removeMobHP(charRan.nextInt(charLevel * 20 - charLevel * 5) + charLevel * 5);

                    //darkness fog - damage mob every 1 second
                else if (areaEffect == AreaEffects.DarknessFog)
                    m.removeMobHP(charRan.nextInt(charLevel * 20 - charLevel * 4) + charLevel * 4);

                m.mobAttackedResult();
            }
    }

    public void endAreaEffect() {
        //ends area effect
        gameUI.setChat(areaEffect.getStopMessage());
        gameUI.showHideAreaEffectView(AreaEffects.None);

        //disable darkness fog effect in mobs are not frozen
        if (areaEffect == AreaEffects.DarknessFog)
            for (Mobs m : mobsList)
                if (m.getMobElementalEffect(2) == 0)
                    m.setMobSpeed(true);
    }

    public void stopAreaEffect() {
        charActionsHandler.removeCallbacks(areaEffectRun);
        areaEffectTime = 0;
        gameUI.showHideAreaEffectView(AreaEffects.None);
        gameUI.setChat("Area effect stopped as you left the area");
    }

    //runnable for attacks cooldown
    private final Runnable attackCooldownRun = new Runnable() {
        @Override
        public void run() {
            if (!game.Pause && !charAttackCooldownList.isEmpty()) {

                //reduce 1 second from every attack thats in cooldown and remove cooldown when reach 0
                for (int acd = 0; acd < charAttackCooldownList.size(); acd++) {
                    if (--charAttackCooldownList.get(acd)[2] <= 0) {
                        charAttackCooldownList.remove(acd--);
                        charUI.mpChecker(charCurrentElementType.Attacks, 4, 7);
                    }
                }

                charAttackCooldownHandler.postDelayed(attackCooldownRun, 1000);
            }
        }
    };

    //add lord point if possible and set lord button
    public void addLordPoints() {
        if (charLevel >= 70 && charLordState[1] == 0 && charLordState[0] < 1000) {
            charLordState[0] += charRan.nextInt(41) * (GMSkills[2] + 1);
            if (charLordState[0] > 1000)
                charLordState[0] = 1000;
            if (charLordState[0] >= 250)
                charUI.setLordBtn();
        }
    }

    //activate lord mode and add it's state
    public void activeLordMode() {
        //check if have enough lord points and if available
        if (charLordState[0] == 1000 && charLordState[1] == 0)
            if (charCurrentElementType == Elements.Fire || charCurrentElementType == Elements.Darkness) {

                charLordState[0] = 0;
                charLordState[1] = 60;
                game.playSound(LORD_START_SOUND);

                //set load image
                charUI.setCharacterImage(true, charCurrentElementType);

                //add lord stats
                lordAdd[0] = charLevel * 10;
                lordAdd[1] = charLevel * 10;
                lordAdd[2] = charLevel * 25;
                lordAdd[3] = charLevel * 25;
                lordAdd[4] = charLevel / 10;
                lordAdd[5] = charLevel;
                addCharHPMP(charLevel * 50, charLevel * 50);

                //start lord mode timer(runnable)
                lordModeRun.run();
            } else
                gameUI.setChat("change element to fire, darkness to use lord mode");
        else
            gameUI.setChat("not enough lord points or in cooldown");
    }

    //lord mode runnable
    private final Runnable lordModeRun = new Runnable() {
        @Override
        public void run() {
            //lord timer, when done remove adds and set cooldown
            if (charLordState[1] != 0 && !game.Pause) {

                if (charLordState[1] > 0) {
                    //show time before lord mode ends on lord button
                    charUI.loadBtnText(String.valueOf(charLordState[1]));

                    //check if lord mode is timed out
                    if (--charLordState[1] <= 0) {
                        //remove lord's adds
                        lordAdd[0] = lordAdd[1] = lordAdd[2] = lordAdd[3] = lordAdd[4] = lordAdd[5] = 0;
                        addCharHPMP(0, 0);

                        //set cooldown
                        charLordState[1] = -90;

                        //update views
                        charUI.setCharacterImage(false, Elements.NONE);
                        charUI.setLordBtn();
                    }
                } else {
                    //show cooldown time on lord button
                    charUI.loadBtnText(String.valueOf(-1 * charLordState[1]));

                    //enable lord when cool down ends
                    if (++charLordState[1] == 0) {
                        charUI.setLordBtn();
                        charUI.loadBtnText("");
                    }
                }

                charActionsHandler.postDelayed(lordModeRun, 1000);
            }
        }
    };

    //get the equipments from database and sort them to their arrays
    private void sortEquipments(List<Equipments> equipmentsList) {
        //check which equipment is equipped
        for (Equipments equipment : equipmentsList) {

            //if not equipped add to character inventory
            if (equipment.equipmentInventorySlot > -1) {
                charEquipments[equipment.equipmentInventorySlot] = equipment;
                inventoryCount++;
            } else {

                //if equipped check that equipment type and add it to its array
                if (!equipment.equipmentType.contains("weapon"))
                    charEquippedEquipments[equipment.equipmentEquippedSlot] = equipment;
                else
                    charEquippedWeapons[equipment.equipmentEquippedSlot] = equipment;
            }
        }
    }

    private void addEquipmentsStats() {
        //add equipped equipments stats
        for (Equipments equipment : charEquippedEquipments) {
            if (equipment != null) {
                equipAdd[0] += equipment.equipmentDMG;
                equipAdd[1] += equipment.equipmentDEF;
                equipAdd[2] += equipment.equipmentHP;
                equipAdd[3] += equipment.equipmentMP;

                //add equipments effects
                if (equipment.equipmentEffect != EquipmentsEffects.NONE)
                    setEquipmentsEffects(equipment.equipmentEffect, 1 + equipment.equipmentUpgradeTimes);
            }
        }
    }

    //equip item, remove out equip of the same type if equipped
    public void Equip(int equipmentIndex) {
        if (equipmentIndex > -1 && charEquipments[equipmentIndex] != null) {

            //check the type of the equipment the equip it by its type
            if (charEquipments[equipmentIndex].equipmentType.contains("weapon"))
                equipWeapon(equipmentIndex);
            else
                equipEquipment(equipmentIndex);
        }
    }

    private void equipEquipment(int equipmentIndex) {
        //get the equipment from inventory
        Equipments equipment = charEquipments[equipmentIndex];

        //get equipment slot index
        int equipmentSlotIndex = equipment.getEquipmentSlotIndex();

        //check if theres an equipment with the same type equipped and remove it
        if (charEquippedEquipments[equipmentSlotIndex] != null)
            Unequip(charEquippedEquipments[equipmentSlotIndex], equipmentIndex);
        else
            charEquipments[equipmentIndex] = null;

        //equip the equipment and remove from inventory
        charEquippedEquipments[equipmentSlotIndex] = equipment;

        //change the equipment slot indexes
        equipment.equipmentInventorySlot = -1;
        equipment.equipmentEquippedSlot = equipmentSlotIndex;

        //update equipped changes in database
        equipmentDB.updateEquipmentSlot(equipment.equipmentPK, -1, equipmentSlotIndex);

        //add the equip stats
        equipAdd[0] += equipment.equipmentDMG;
        equipAdd[1] += equipment.equipmentDEF;
        equipAdd[2] += equipment.equipmentHP;
        equipAdd[3] += equipment.equipmentMP;

        //add equipments effects
        if (equipment.equipmentEffect != EquipmentsEffects.NONE)
            setEquipmentsEffects(equipment.equipmentEffect, -1 - equipment.equipmentUpgradeTimes);

        //update hp\mp text by equipment stats
        if (equipment.equipmentHP > 0)
            charUI.updateHPText();
        if (equipment.equipmentMP > 0)
            charUI.updateMPText();

        //reset inventory view
        inventoryCount--;
        characterInventoryUI.setEquippedImage(false, equipmentSlotIndex, equipment.equipmentID, true);
        characterInventoryUI.updateInventory();
        characterInventoryUI.setInventoryCharStats();
    }

    private void equipWeapon(int weaponIndex) {
        //get the weapon from equipments list
        Equipments weapon = charEquipments[weaponIndex];

        //check if the weapon mastery skill level > 0
        if (getWeaponMasteryLevel(weapon.getWeaponByEquipmentType().weaponSkill) > 0) {

            //check if not equipped with a weapon with the same type
            if (checkSameWeaponTypeEquipped(weapon)) {

                //check if theres an empty slot to equip the weapon
                int emptySlot = checkEmptyWeaponSlot();
                if (emptySlot > -1) {

                    //equip the weapon
                    weapon.equipmentEquippedSlot = emptySlot;
                    weapon.equipmentInventorySlot = -1;
                    equipmentDB.updateEquipmentSlot(weapon.equipmentPK, -1, emptySlot);

                    //show weapon select button for the weapon
                    String[] weaponType = weapon.equipmentType.split("-");
                    charUI.showHideWeaponSelectButtons(true, emptySlot, Weapons.valueOf(weaponType[1]));

                    //remove the weapon from the inventory and add it to equipped weapons
                    charEquippedWeapons[emptySlot] = weapon;
                    charEquipments[weaponIndex] = null;

                    //reset inventory view
                    inventoryCount--;
                    characterInventoryUI.setEquippedImage(true, emptySlot, weapon.equipmentID, true);
                    characterInventoryUI.updateInventory();
                    characterInventoryUI.setInventoryCharStats();

                } else
                    Toasts.makeToast(game, "you can't equip more than 3 weapons");
            } else
                Toasts.makeToast(game, "you can't equip more than 1 weapon of the same type");
        } else
            Toasts.makeToast(game, "you need to rise the weapon mastery skill for this weapon to equip it");
    }

    //unequip equipments and weapons
    public void Unequip(Equipments unequippedEquipment, int index) {
        if (inventoryCount < 80 || index > -1) {

            //remove equipped armor adds
            if (!unequippedEquipment.equipmentType.contains("weapon")) {
                equipAdd[0] -= unequippedEquipment.equipmentDMG;
                equipAdd[1] -= unequippedEquipment.equipmentDEF;
                equipAdd[2] -= unequippedEquipment.equipmentHP;
                equipAdd[3] -= unequippedEquipment.equipmentMP;

                //remove equipments effects
                if (unequippedEquipment.equipmentEffect != EquipmentsEffects.NONE)
                    setEquipmentsEffects(unequippedEquipment.equipmentEffect, -1 - unequippedEquipment.equipmentUpgradeTimes);

                //reset hp\mp if unequipped equipment had them in its stats
                if (unequippedEquipment.equipmentHP > 0)
                    addCharHP(0);
                if (unequippedEquipment.equipmentMP > 0)
                    addCharMP(0);

                //set empty slot in the equipment array by type
                charEquippedEquipments[unequippedEquipment.equipmentEquippedSlot] = null;
            } else {
                //remove equipped weapon

                //change to fists if unequipped weapon was held
                if (unequippedEquipment.equipmentType.contains(charCurrentWeaponType.toString()))
                    setWeapon(0);

                //hide weapon select button
                charUI.showHideWeaponSelectButtons(false, unequippedEquipment.equipmentEquippedSlot, null);

                //set empty slot in the equipment array by type
                charEquippedWeapons[unequippedEquipment.equipmentEquippedSlot] = null;
            }

            //get the first empty slot in the inventory
            if (index == -1)
                index = getInventoryFirstEmptySlot();

            //set the equipment in the empty slot and add it to the inventory
            unequippedEquipment.equipmentInventorySlot = index;
            charEquipments[index] = unequippedEquipment;

            //update unequipped in database
            equipmentDB.updateEquipmentSlot(unequippedEquipment.equipmentPK, index, -1);

            //reset inventory view
            inventoryCount++;
            characterInventoryUI.setEquippedImage(unequippedEquipment.equipmentType.contains("weapon"), unequippedEquipment.equipmentEquippedSlot, unequippedEquipment.equipmentID, false);
            characterInventoryUI.setInventoryCharStats();
            characterInventoryUI.updateInventory();

            //reset equipment equipped slot
            unequippedEquipment.equipmentEquippedSlot = -1;
        } else
            Toasts.makeToast(game, "inventory is full");
    }

    public void swipeEquipmentsSlots(int toSwipeIndex, int swipeWithIndex) {
        //get the equipment to swipe
        Equipments toSwipe = charEquipments[toSwipeIndex];

        if (charEquipments[swipeWithIndex] != null) {
            //get the equipment to swipe with from the inventory
            Equipments swipeWith = charEquipments[swipeWithIndex];

            //swipe the equipments indexes
            toSwipe.equipmentInventorySlot = swipeWithIndex;
            swipeWith.equipmentInventorySlot = toSwipeIndex;

            //swipe the equipments in the inventory
            charEquipments[toSwipeIndex] = swipeWith;
            charEquipments[swipeWithIndex] = toSwipe;

            //update swipe in database
            equipmentDB.updateEquipmentSlot(toSwipe.equipmentPK, swipeWithIndex, -1);
            equipmentDB.updateEquipmentSlot(swipeWith.equipmentPK, toSwipeIndex, -1);
        } else {
            //change the equipment index
            toSwipe.equipmentInventorySlot = swipeWithIndex;

            //move the equipment in the inventory
            charEquipments[swipeWithIndex] = toSwipe;
            charEquipments[toSwipeIndex] = null;

            //update swipe in database
            equipmentDB.updateEquipmentSlot(toSwipe.equipmentPK, swipeWithIndex, -1);
        }

        //reset inventory view
        characterInventoryUI.updateInventory();
        characterInventoryUI.setInventoryCharStats();
    }

    //check if theres the same type of weapon equipped
    private boolean checkSameWeaponTypeEquipped(Equipments weapon) {
        for (Equipments ew : charEquippedWeapons) {
            if (ew != null && ew != weapon & ew.equipmentType.equals(weapon.equipmentType))
                return false;
        }
        return true;
    }

    //check if theres an empty weapon slot available
    private int checkEmptyWeaponSlot() {
        for (int ew = 0; ew < 3; ew++)
            if (charEquippedWeapons[ew] == null) {
                return ew;
            }
        return -1;
    }

    private int getWeaponMasteryLevel(SkillsEnum weaponSkill) {
        for (Skills s : charSkillsList.get(0))
            if (SkillsEnum.valueOf(s.skillID).equals(weaponSkill))
                return s.skillLevel;
        return 0;
    }

    private void setEquipmentsEffects(EquipmentsEffects effect, int effectLvl) {
        if (effect == EquipmentsEffects.HP_REGEN)
            HP_REGENERATION += effectLvl;
        else if (effect == EquipmentsEffects.MP_REGEN)
            MP_REGENERATION += effectLvl;
    }

    private int getInventoryFirstEmptySlot() {
        for (int i = 0; i < charEquipments.length; i++)
            if (charEquipments[i] == null)
                return i;
        return -1;
    }

    //add passive skills effects
    private void enablePassiveSkills() {
        for (Skills ps : charSkillsList.get(3)) {
            if (ps.skillLevel > 0) {
                switch (ps.skillID) {
                    case "MORE_DAMAGE":
                        charPassiveSkillsAdd[0] = 20 * ps.skillLevel;
                        break;
                    case "MORE_DEFENSE":
                        charPassiveSkillsAdd[1] = 20 * ps.skillLevel;
                        break;
                    case "MORE_HP":
                        charPassiveSkillsAdd[2] = 200 * ps.skillLevel;
                        break;
                    case "MORE_MP":
                        charPassiveSkillsAdd[3] = 200 * ps.skillLevel;
                        break;
                    case "EXTRA_ATTACK":
                        charPassiveSkillsAdd[4] = ps.skillLevel;
                        break;
                    case "BLOOD_BOOST":
                        charPassiveSkillsAdd[5] = 10 * ps.skillLevel;
                        break;
                    case "BLOOD_HEALING":
                        charPassiveSkillsAdd[6] = 10 * ps.skillLevel;
                        break;
                    case "ENERGY_HEALING":
                        charPassiveSkillsAdd[7] = 10 * ps.skillLevel;
                        break;
                    case "BURN":
                        charPassiveSkillsAdd[8] = ps.skillLevel;
                        break;
                    case "FREEZE":
                        charPassiveSkillsAdd[9] = ps.skillLevel;
                        break;
                    case "AVOID":
                        charPassiveSkillsAdd[10] = ps.skillLevel;
                        break;
                    case "DROP_TEST":
                        GMSkills[0] = 60;
                        break;
                    case "SKILLS_TEST":
                        GMSkills[1] = 1;
                        break;
                    case "LORD_TEST":
                        GMSkills[2] = 10;
                        break;
                    case "BLESS_TEST":
                        GMSkills[3] = 1;
                        break;
                }
            }
        }
        loadCharStats();
    }

    //add usable skills and potions to skills tab
    private void setSkillsTab() {
        //check how many skills are above level 0 and checked and add to skills tab
        for (Skills s : charSkillsList.get(2))
            if (s.skillInTab)
                charTabSkills.add(s);
        for (Skills s : charSkillsList.get(4))
            if (s.skillInTab)
                charTabSkills.add(s);

        //add pots to skills tab
        if (charHPpots > 0) {
            Skills pot = new Skills("HP_POTION_SKILL", -1, 1);
            charTabSkills.add(pot);
        }
        if (charMPpots > 0) {
            Skills pot = new Skills("MP_POTION_SKILL", -1, 1);
            charTabSkills.add(pot);
        }

        //add 'close' icon
        Skills closeSV = new Skills("SKILL_MENU_BUTTON", -1, 1);
        charTabSkills.add(closeSV);
        charUI.setSkillsView(charTabSkills, mobsList);
    }

    //add/remove skill from skills tab
    public void showHideSkillView(Skills skill, boolean show) {
        skill.skillInTab = show;
        skillDB.updateSkillView(show, skill.skillID, charID);
        if (show)
            addToSkillsTab(skill);
        else
            charTabSkills.remove(skill);
        charUI.updateSkillsView();
    }

    //add active skill to skill Tab
    private void addToSkillsTab(Skills skill) {
        for (int s = 0; s < charTabSkills.size(); s++)
            if (charTabSkills.get(s).skillLevel == -1) {
                charTabSkills.add(s, skill);
                break;
            }
    }

    //level up skill and remove skill points
    public void levelUpSkill(Skills SHolder) {
        charDB.updateSkillPoints(charID, --charSkillPoints);
        skillDB.updateSkillLevel(++SHolder.skillLevel, SHolder.skillID, charID);

        if (SHolder.skillType.contains("weapon_mastery")) {

            //check if its the skill first level
            if (SHolder.skillLevel == 1) {

                //create stats weapon information button
                characterStatsUI.createInfoButton(true, SkillsEnum.valueOf(SHolder.skillID).getSkillType().split("-")[1]);

                //add blood weapons
                if (SHolder.skillID.equals("BLOOD_WEAPONS_MASTERY"))
                    charUI.addBloodWeapons(game);

                //update current weapon
            } else if (charCurrentWeaponType.weaponSkill.toString().equals(SHolder.skillID))
                setWeapon(charCurrentWeaponIndex);

            //update current element
        } else if (SHolder.skillType.contains("element_knowledge") && charCurrentElementType.elementSkill.toString().equals(SHolder.skillID))
            setElement(charCurrentElementType);

            //update passive skills
        else if (SHolder.skillType.equals("passive_skill"))
            enablePassiveSkills();
    }

    public void usePotion(String pot) {
        //HP potion
        if (pot.equals("HP") & charHPpots > 0) {
            addCharHP(2000);
            charDB.updatePotions(charID, "HP", --charHPpots);
            gameUI.setChat(charHPpots + " HP Potions left");

            //MP potion
        } else if (pot.equals("MP") && charMPpots > 0) {
            addCharMP(2000);
            charDB.updatePotions(charID, "MP", --charMPpots);
            gameUI.setChat(charMPpots + " MP Potions left");

        } else
            gameUI.setChat("you are out of this Potions");
    }

    //add potion to character
    public void addPotions(String type, int amount) {
        if (type.equals("HP")) {
            charHPpots += amount;
            charDB.updatePotions(charID, type, charHPpots);
        } else if (type.equals("MP")) {
            charMPpots += amount;
            charDB.updatePotions(charID, type, charHPpots);
        }
    }

    //start active skill
    public void activatedSkillsAdd(int ind, int num, int time, int cost) {
        addCharMP(-cost);
        charUI.showHideActivatedSkillIcon(true, ind, game);
        charActiveSkillsAdd[ind][0] = num;
        charActiveSkillsAdd[ind][1] = time;
        activeBuffSkillsCounter++;
        charActionsHandler.removeCallbacks(activeSkillsRun);
        activeSkillsRun.run();
    }

    //runnable for active buff skills
    private final Runnable activeSkillsRun = new Runnable() {
        @Override
        public void run() {
            if (activeBuffSkillsCounter > 0 && !game.Pause) {

                for (int as = 0; as < charActiveSkillsAdd.length; as++)

                    //check if skill timeout
                    if (--charActiveSkillsAdd[as][1] == 0) {

                        //disable activated skill
                        charActiveSkillsAdd[as][0] = 0;
                        charUI.showHideActivatedSkillIcon(false, as, game);
                        activeBuffSkillsCounter--;
                    }

                charActionsHandler.postDelayed(activeSkillsRun, 1000);
            }
        }
    };

    //check if bless can be used
    public void attemptToUseBless(int bless) {
        if (charBlessUse[1] > 0 && charBlesses[bless][0] > 0 && charBlesses[bless][1] <= 0 && activeBlessCounter < 5) {
            charBlessUse[1]--;
            blessActions(bless, 1);
        } else {
            if (charBlessUse[1] <= 0)
                Toasts.makeToast(game, "no bless credits to use");
            else if (charBlesses[bless][0] <= 0)
                Toasts.makeToast(game, "you need to level up this bless first");
            else if (charBlesses[bless][1] > 0)
                Toasts.makeToast(game, "bless is already activated");
            else if (activeBlessCounter >= 7)
                Toasts.makeToast(game, "cannot use more than 5 blesses at once");
        }
    }

    //activate or deactivate blesses and add or remove their effects(state =  1 activate, -1 deactivate)
    private void blessActions(int blessInd, int state) {
        double bhp = 0, bmp = 0;

        //get the bless
        Blesses bless = Blesses.values()[blessInd];

        //first bless add
        switch (bless.Add1) {
            case "Attack":
                blessesAdd[0] += state * BlessesData.getBlessBaseValue("Attack", charBlesses[blessInd][0]);
                break;
            case "Defense":
                blessesAdd[1] += state * BlessesData.getBlessBaseValue("Defense", charBlesses[blessInd][0]);
                break;
            case "HP":
                bhp = state * BlessesData.getBlessBaseValue("HP", charBlesses[blessInd][0]);
                blessesAdd[2] += bhp;
                break;
            case "MP":
                bmp = state * BlessesData.getBlessBaseValue("MP", charBlesses[blessInd][0]);
                blessesAdd[3] += bmp;
                break;
        }

        //second bless add
        switch (bless.Add2) {
            case "Defense":
                blessesAdd[1] += state * BlessesData.getBlessBaseValue("Defense", charBlesses[blessInd][0]);
                break;
            case "HP":
                bhp = state * BlessesData.getBlessBaseValue("HP", charBlesses[blessInd][0]);
                blessesAdd[2] += bhp;
                break;
            case "MP":
                bmp = state * BlessesData.getBlessBaseValue("MP", charBlesses[blessInd][0]);
                blessesAdd[3] += bmp;
                break;
            case "Critical Rate":
                blessesAdd[4] += state * BlessesData.getBlessBaseValue("Critical Rate", charBlesses[blessInd][0]);
                break;
            case "Critical Damage":
                blessesAdd[5] += state * BlessesData.getBlessBaseValue("Critical Damage", charBlesses[blessInd][0]);
                break;
            case "XP Bonus":
                blessesAdd[6] += state * BlessesData.getBlessBaseValue("XP Bonus", charBlesses[blessInd][0]);
                break;
        }

        //bless start
        if (state == 1) {
            characterBlessesUI.showBlessesPage();
            charBlesses[blessInd][1] = 120;
            activeBlessCounter++;
            Toasts.makeToast(game, "bless activated");

            //set hp\mp and restore if bless add to them
            if (bhp > 0 || bmp > 0)
                addCharHPMP((int) bhp, (int) bmp);

            //bless end
        } else if (state == -1) {
            activeBlessCounter--;
            gameUI.setChat(bless + "'s bless ended");

            //remove hp\mp if bless add to them
            if (bhp < 0 || bmp < 0)
                addCharHPMP(0, 0);
        }

        characterBlessesUI.setActiveBlessesText(activeBlessCounter, blessesAdd);
    }

    //runnable for blesses
    private final Runnable blessesRun = new Runnable() {
        @Override
        public void run() {
            if (activeBlessCounter > 0 && !game.Pause) {

                //check if a blesses time is out and disable it
                for (int bt = 0; bt < charBlesses.length; bt++)
                    if (--charBlesses[bt][1] == 0)
                        blessActions(bt, -1);

                charActionsHandler.postDelayed(blessesRun, 1000);
            }
        }
    };

    //level up chosen bless
    public void levelUpBless(int bless, TextView infoText) {
        //check if leveling possible
        if (charBlesses[bless][0] < 30 && charBlessPoints >= BlessesData.blessLevelCost(charBlesses[bless][0])) {
            charBlessPoints -= BlessesData.blessLevelCost(charBlesses[bless][0]);
            charDB.updateBlessPoints(charID, charBlessPoints);
            blessDB.levelupBless(charID, bless + 1, ++charBlesses[bless][0]);

            //update bless view
            characterBlessesUI.setBlessAfterLevelUp(bless, charBlesses[bless][0], infoText);
        } else if (charBlesses[bless][0] >= 30)
            Toasts.makeToast(game, "bless is maxed");
        else
            Toasts.makeToast(game, "you don't have enough point you level up");

    }

    private void addBlessPoints() {
        //add blessuse points and convert when reach 30
        if (++charBlessUse[0] >= 30 || GMSkills[3] > 0) {
            charBlessUse[1]++;
            charBlessUse[0] -= 30;
        }

        //chance to give character bless points(used to level up bless)
        if (charRan.nextInt(100) + 1 + GMSkills[3] >= 50) {
            charBlessPoints += (charRan.nextInt(51) + 1) * (1 + GMSkills[3]);
            charDB.updateBlessPoints(charID, charBlessPoints);
        }
    }

    //add ores, xp and bless point when mob defeated
    public void mobDefeated(int XPMulti) {
        addOres();
        addCharXP(XPMulti);
        addBlessPoints();
    }

    private void addOres() {
        //generate random amount of ores base on character level
        int genOres = charLevel * (charRan.nextInt((10 - 1)) + 1);
        Ores += genOres;
        accDB.updateOres(accountID, Ores);
    }

    public void addCharXP(int XPMulti) {
        //make xp base on character level xp multiplier and blass if activated
        int XP = (int) ((10 + charLevel * 3 * XPMulti) * (1 + blessesAdd[6]));

        //limit character to level 200
        if (charLevel < 200) {

            //add xp to character
            charXP += XP;

            //level up character if got enough xp
            if (charXP >= charXPToLevelUp)
                levelUpChar();

            //set xp to 0 if level maxed
            if (charLevel == 200)
                charXP = 0;
        }

        //set xp text
        charUI.updateXPText(charXPToLevelUp, charXP);

        //add xp to character
        charDB.updateEXP(charID, charXP);
    }

    private void levelUpChar() {
        //reduce xp and level up character
        charXP -= charXPToLevelUp;
        if (++charLevel == 200)
            charXP = 0;

        //add skill points
        charSkillPoints += 5;

        //update character level in database
        charDB.updateCharacterLevelUp(charID, charLevel, charSkillPoints);

        //reset character stats
        loadCharStats();
        addCharHPMP(statsMixer(3), statsMixer(4));

        //set level in mobs class
        Mobs.setCharLevel(charLevel);

        //reset character info view
        charUI.setCharNameAndLevel();

        //move character to element selection page when reach level 50
        if (charLevel == 50)
            gameUI.selectAdditionalElement(charElements);

        //reset weapon and element to update mastery and knowledge bonus damage
        if (charLevel % 10 == 0) {
            setWeapon(charCurrentWeaponIndex);
            setElement(charCurrentElementType);
        }

        gameUI.setChat("Level UP!");
    }

    //generate random stats for picked item
    public void pickUpItem(String droppedItem) {
        //check if inventory is full
        if (inventoryCount < 80) {
            int EqHp = 0, EqMp = 0, EqDmg = 0, EqDef = 0;

            //get equipment data from equipments enum
            EquipmentsEnum equipment = EquipmentsEnum.valueOf(droppedItem);

            //get equipment type
            String equipmentType = equipment.getEquipmentType();

            //check if dropped item is armor or weapon and generate random stats when pickup
            if (equipmentType.contains("weapon"))
                EqDmg = charRan.nextInt(21) + equipment.getEquipmentBaseDamage();

            else {
                //add defense,hp,mp to armors
                EqDef = charRan.nextInt(6) + equipment.getEquipmentBaseDefense();
                if (equipmentType.contains("Helmet") || equipmentType.contains("Gloves"))
                    EqMp = charRan.nextInt(51) + equipment.getEquipmentBaseMP();
                else if (equipmentType.contains("Shirt") || equipmentType.contains("Pants"))
                    EqHp = charRan.nextInt(51) + equipment.getEquipmentBaseHP();

                //add damage to gloves
                if (equipmentType.contains("Gloves"))
                    EqDmg = charRan.nextInt(11) + equipment.getEquipmentBaseDamage();
            }

            //add the item to character inventory
            int inventorySlot = getInventoryFirstEmptySlot();
            Equipments newEquip = new Equipments(equipmentDB.totalEquipments() + 1, droppedItem, -1, inventorySlot, 0, EqHp, EqMp, EqDmg, EqDef);
            charEquipments[inventorySlot] = newEquip;
            equipmentDB.createEquipment(charID, newEquip);
            inventoryCount++;
            characterInventoryUI.updateInventory();
        } else
            gameUI.setChat("Inventory is full");
    }

    private void loadCharStats() {
        //set character stats by level
        charDamage = 30 * charLevel + (charPassiveSkillsAdd[0] * (1 + charLevel / 10));
        charDefense = 30 * charLevel + (charPassiveSkillsAdd[1] * (1 + charLevel / 10));
        charMaxHP = 300 * charLevel + (charPassiveSkillsAdd[2] * (1 + charLevel / 10));
        charMaxMP = 200 * charLevel + (charPassiveSkillsAdd[3] * (1 + charLevel / 10));
        charCriticalRate = 0.3 * charLevel;
        charCriticalDamage = charLevel;
        if (charCriticalRate > 100)
            charCriticalRate = 100;

        //set xp that needed to level up
        setCharXPToLevelUP();

        //set hp and mp txt
        charUI.updateHPMPText();
    }

    //set the amount of xp point character need to level up
    private void setCharXPToLevelUP() {
        int lvXpMul = 0;
        if (charLevel > 0 && charLevel < 20)
            lvXpMul = 20;
        else if (charLevel >= 20 && charLevel < 50)
            lvXpMul = 51;
        else if (charLevel >= 50 && charLevel < 70)
            lvXpMul = 72;
        else if (charLevel >= 70 && charLevel < 100)
            lvXpMul = 103;
        else if (charLevel >= 100 && charLevel < 150)
            lvXpMul = 154;
        else if (charLevel < 200)
            lvXpMul = 205;
        charXPToLevelUp = (20 * charLevel) + lvXpMul * charLevel;
    }

    //return total stat value
    public int statsMixer(int stat) {
        if (stat == 1)
            return charDamage + equipAdd[0] + lordAdd[0] + charActiveSkillsAdd[0][0] + charActiveSkillsAdd[1][0];
        else if (stat == 2)
            return (int) ((charDefense + equipAdd[1] + lordAdd[1] + charActiveSkillsAdd[0][0] + charActiveSkillsAdd[2][0]) * (1 + blessesAdd[1]));
        else if (stat == 3)
            return charMaxHP + equipAdd[2] + lordAdd[2] + (int) blessesAdd[2];
        else if (stat == 4)
            return charMaxMP + equipAdd[3] + lordAdd[3] + (int) blessesAdd[3];
        else if (stat == 5)
            return charCriticalDamage + lordAdd[5] + (int) blessesAdd[5];
        return -1;
    }

    //return critical rate value
    public double criticalRateMixer() {
        return charCriticalRate + lordAdd[4] + blessesAdd[4] + charActiveSkillsAdd[3][0];
    }

    public void addCharHPMP(int HP, int MP) {
        addCharHP(HP);
        addCharMP(MP);
    }

    //change character hp and update hp text
    public void addCharHP(int addHP) {
        if (GMSkills[4] == 1)
            addHP = 0;
        else if (GMSkills[6] == 1)
            addHP = -1;

        charHP += addHP;

        if (charHP <= 0)
            charISDead();
        else if (charHP > statsMixer(3))
            charHP = statsMixer(3);

        charUI.updateHPText();
    }

    //change character mp and update mp text
    public void addCharMP(int addMP) {
        charMP += addMP;

        if (charMP < 0)
            charMP = 0;
        else if (charMP > statsMixer(4))
            charMP = statsMixer(4);

        charUI.mpChecker(charCurrentWeaponType.Attacks, 1, 4);
        charUI.mpChecker(charCurrentElementType.Attacks, 4, 7);

        charUI.updateMPText();
    }

    //move character and it's image from joystick use
    public void charMovement(int x, int y) {
        charX += x * charSpeed;
        charY += y * charSpeed;
        setCharacterLocation();
    }

    //set character x and y move it's image
    public void setCharXY(int x, int y) {
        charX = x;
        if (y != -1)
            charY = y;
        setCharacterLocation();
    }


    private void setCharacterLocation() {
        //fix character XY if out of area range
        if (charX < 0)
            charX = 0;
        else if (charX > gameUI.getAreaMaxX() - charWidth)
            charX = gameUI.getAreaMaxX() - charWidth;
        if (charY < 0)
            charY = 0;
        else if (charY > gameUI.getAreaMaxY() - charHeight)
            charY = gameUI.getAreaMaxY() - charHeight;

        //focus on character
        gameUI.focusOnChar(charX, charY);

        //set character image location
        charUI.setCharImageLocation(charX, charY);
    }

    //update character area and move it to enter portal location
    public void updateArea(Areas area, int x, int y) {
        charArea = area;
        setCharXY(x, y);
        charDB.updateCurrentArea(charID, charArea.toString());

        //end and hide projectiles
        if (charProjectileIsActive)
            endProjectile();
    }

    //healing while standing
    private final CountDownTimer standHealing = new CountDownTimer(10000, 1000) {
        public void onTick(long CDTtime) {

            //if character dead or moved cancel healing
            if (charMoving || charHP <= 0)
                cancel();
        }

        public void onFinish() {
            //heal character
            if (!game.Pause && !charMoving && charHP > 0) {
                addCharHPMP((int) ((15 + charPassiveSkillsAdd[6]) * (1 + 0.5 * HP_REGENERATION)), ((int) ((15 + charPassiveSkillsAdd[7]) * (1 + 0.5 * MP_REGENERATION))));
                start();
            }
        }
    };

    //pause the game and show another page
    public void showPage(int page) {
        game.Pause = true;
        if (page == 1)
            characterStatsUI.showStatsPage();
        else if (page == 2)
            characterInventoryUI.showInventoryPage();
        else if (page == 3)
            characterSkillsUI.showSkillsPage();
        else if (page == 4)
            characterBlessesUI.showBlessesPage();
    }

    //resume runnable when game is unpause
    public void resumeWhenUnpause() {
        if (charLordState[1] != 0)
            lordModeRun.run();
        if (activeBlessCounter > 0)
            blessesRun.run();
        if (areaEffectTime > 0)
            areaEffectRun.run();
        if (activeBuffSkillsCounter > 0)
            activeSkillsRun.run();
        if (!charAttackCooldownList.isEmpty())
            attackCooldownRun.run();

        //resume projectile if active
        if (charProjectileIsActive)
            projectilesRun.run();
    }

    public void updateCharInventory(int count) {
        inventoryCount = count;
        characterInventoryUI.updateInventory();
    }

    //set character height and width
    public void setCharSize() {
        charHeight = charUI.getCharImgHeight();
        charWidth = charUI.getCharImgWidth();
    }

    //dialog if character is dead
    private void charISDead() {
        charHP = 0;
        final AlertDialog.Builder deadDialog = new AlertDialog.Builder(game);
        deadDialog.setTitle("YOU DIED!");
        deadDialog.setMessage("you will be send back to camp");
        deadDialog.setPositiveButton("OK", (dialog, which) -> sendCharToCamp());
        deadDialog.setOnDismissListener(dialog -> sendCharToCamp());
        deadDialog.show();
    }

    private void sendCharToCamp() {
        //end lord mode()
        charLordState[1] = 0;

        //recover char health
        addCharHP(charMaxHP);

        //set char area
        updateArea(Areas.StarterCamp, 2530, 1500);

        //clear and change area to starter camp
        gameUI.clearAreaBeforeSet(Areas.StarterCamp);
    }

    //add new element and skills to character
    public void addNewElement(int charElement, Elements element) {
        charElements.add(element);
        charDB.updateElements(charID, charElement, element.elementNum);
        SkillsAdder.addElementSkills(charID, skillDB, element, charElements);
    }

    public void characterMovement(int way) {
        charUI.characterMovement(way);
    }

    public double getBlessDamage() {
        return 1 + blessesAdd[1];
    }

    public void startStandHealing() {
        standHealing.start();
    }

    public int getCharHeight() {
        return charHeight;
    }

    public int getCharWidth() {
        return charWidth;
    }

    public void changeCharSide() {
        charUI.setCharImageSide();
    }

    public ImageView getCharImage() {
        return charUI.getCharIMG();
    }

    public float getCharSide() {
        return charUI.getCharImageSide();
    }

    public void setMobsList(List<Mobs> mobsList) {
        this.mobsList = mobsList;
    }

    public void setCharMoving(boolean move) {
        charMoving = move;
    }

    public void setGMSkills(int ind, int num) {
        GMSkills[ind] = num;
    }

    public int getLordStats(int ind) {
        return charLordState[ind];
    }

    public List<List<Skills>> getCharSkillsList() {
        return charSkillsList;
    }

    public int getGMSkill(int ind) {
        return GMSkills[ind];
    }

    public int getPassiveSkillsAdd(int ind) {
        return charPassiveSkillsAdd[ind];
    }

    public Equipments[] getCharEquipments() {
        return charEquipments;
    }

    public int getCharID() {
        return charID;
    }

    public void setCharID(int charID) {
        this.charID = charID;
    }

    public int getCharLevel() {
        return charLevel;
    }

    public void setCharLevel(int charLevel) {
        this.charLevel = charLevel;
    }

    public int getOres() {
        return Ores;
    }

    public void setOres(int ores) {
        Ores = ores;
    }

    public void setCharXP(int charXP) {
        this.charXP = charXP;
    }

    public String getCharName() {
        return charName;
    }

    public void setCharName(String charName) {
        this.charName = charName;
    }

    public int getCharSkillPoints() {
        return charSkillPoints;
    }

    public void setCharSkillPoints(int charSkillPoints) {
        this.charSkillPoints = charSkillPoints;
    }

    public void setCharHPpots(int charHPpots) {
        this.charHPpots = charHPpots;
    }

    public void setCharMPpots(int charMPpots) {
        this.charMPpots = charMPpots;
    }

    public int getCharBlessPoints() {
        return charBlessPoints;
    }

    public void setCharBlessPoints(int charBlessPoints) {
        this.charBlessPoints = charBlessPoints;
    }

    public int getCharX() {
        return charX;
    }

    public int getCharY() {
        return charY;
    }

    public int getCharMaxHP() {
        return charMaxHP;
    }

    public int getCharMaxMP() {
        return charMaxMP;
    }

    public int getCharHP() {
        return charHP;
    }

    public int getCharMP() {
        return charMP;
    }

    public int getCharDefense() {
        return charDefense;
    }

    public int getCharDamage() {
        return charDamage;
    }

    public double getCharCriticalRate() {
        return charCriticalRate;
    }

    public int getCharCriticalDamage() {
        return charCriticalDamage;
    }

    public Weapons getCharCurrentWeaponType() {
        return charCurrentWeaponType;
    }

    public int getCharCurrentElementTypeNum() {
        return charCurrentElementType.elementNum;
    }

    public Areas getCharArea() {
        return charArea;
    }

    public void setCharArea(Areas charArea) {
        this.charArea = charArea;
    }

    public boolean isCharHitDelay() {
        return charHitDelay;
    }

    public void setCharHitDelay(boolean charHitDelay) {
        this.charHitDelay = charHitDelay;
    }

    public List<Skills> getCharTabSkills() {
        return charTabSkills;
    }

    public int getCharBlessUse() {
        return charBlessUse[1];
    }

    public int getEquipAdd(int ind) {
        return equipAdd[ind];
    }

    public AreaEffects getAreaEffect() {
        return areaEffect;
    }

    public int getAreaEffectTime() {
        return areaEffectTime;
    }

    public CharacterUI getCharUI() {
        return charUI;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public int getCharSpeed() {
        return charSpeed;
    }

    public void setCharSpeed(int speed) {
        charSpeed = speed;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public GameUI getGameUI() {
        return gameUI;
    }

    public void setAccDB(AccountsDB accDB) {
        this.accDB = accDB;
    }

    public EquipmentsDB getEquipmentDB() {
        return equipmentDB;
    }

    public CharactersDB getCharDB() {
        return charDB;
    }

    public SkillsDB getSkillDB() {
        return skillDB;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public boolean isQuestFinished() {
        return quest.isFinished();
    }
}