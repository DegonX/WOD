package com.degonx.warriorsofdeagon.Objects;

import static android.content.ContentValues.TAG;
import static com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl.ScreenHeight;
import static com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl.ScreenWidth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.AttackAndSkills.AttacksActions;
import com.degonx.warriorsofdeagon.AttackAndSkills.SkillsAdder;
import com.degonx.warriorsofdeagon.Enums.Blesses;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.AreaEffects;
import com.degonx.warriorsofdeagon.Enums.AttacksEnums.AttacksType;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.WeaponAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.BlessesData;
import com.degonx.warriorsofdeagon.GameData.CharactersData;
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
import com.degonx.warriorsofdeagon.Database.CompanionsDB;
import com.degonx.warriorsofdeagon.Database.EquipmentsDB;
import com.degonx.warriorsofdeagon.Database.SkillsDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class Character {

    private AccountsDB accDB;
    private CharactersDB charDB;
    private EquipmentsDB equipDB;
    private SkillsDB skillDB;
    private BlessesDB blessDB;

    private int accountID;
    private int charID;
    private int charLevel;
    private int charXP;
    private int XPToLevelUp;
    private String charName;
    private int charMaxHP;
    private int charMaxMP;
    private int charHP = 1;
    private int charMP = 1;
    private int charDefence;
    private int charDamage;
    private double charCriticalRate;
    private int charCriticalDamage;
    private int charX = 260;
    private int charY = 500;
    private int charHeight;
    private int charWidth;
    private int charSkillPoints;
    private int charBlessPoints;
    private int charHPpots;
    private int charMPpots;
    private int charAreaID;

    private int charCurrentWeaponIndex = 0;
    private Weapons charCurrentWeaponType;

    private int charCurrentElementIndex = 0;
    private Elements charCurrentElementType;

    private int activeBlessCounter = 0;
    private int activeBuffSkillsCounter = 0;

    private boolean charMoving = false;
    private boolean charAtkDelay = false;
    private boolean charHitDelay = true;
    private boolean compIsSummoned = false;
    private boolean charProjectileIsActive = false;

    private final int[] charEquippedWeaponsIndexes = new int[]{-1, -1, -1, -1, -1};
    private final int[] charEquippedArmorIndexes = new int[]{-1, -1, -1, -1, -1};
    private final int[] equipAdd = new int[4];//equipAdd - DMG + DEF + HP + MP

    private int[][] charBlesses; //charBlesses - level + times
    private final int[] charBlessUse = new int[2];//blessUse - blessusepoints + blessuses;
    private final double[] blessesAdd = new double[7];//blessesAdd - DMG + DEF + HP + MP + CRITRATE + CRITDMG + XP

    private final int[] charLordState = new int[2];//charLordState - process + time
    private final int[] lordAdd = new int[6];//lordAdd - DMG + DEF + HP + MP + CRITRATE + CRITDMG

    private final int[] compBuffAdd = new int[2];//compBuffAdd - DEF + CRITDMG

    private final int[] charPassiveSkillsAdd = new int[12];
    private final int[][] charActiveSkillsAdd = new int[4][2];//activeSkillsAdd - power up + dmg up + def up + crit up / times
    private final int[] GMSkills = new int[6];

    private AreaEffects areaEffect;
    private int areaEffectTime = 0;

    private List<Equipments> charEquipmentsList;
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
    private Companion Comp;

    final int WEAPON_ATTACK_SOUND = 0;
    final int ELEMENT_ATTACK_SOUND = 1;
    final int LORD_START_SOUND = 2;

    public Character() {
    }

    //make connection with databases, create character UI, load character and set it to start playing
    public void setCharacterAtStart(Game game, GameUI gameUI, CharactersDB charDB) {
        this.game = game;
        this.gameUI = gameUI;
        Mobs.setCharLevel(charLevel);

        //connection with databases
        this.charDB = charDB;
        equipDB = new EquipmentsDB(game);
        skillDB = new SkillsDB(game);
        blessDB = new BlessesDB(game);
        CompanionsDB companionDB = new CompanionsDB(game);

        //create Blesses if not exist
        if (!blessDB.checkIfBlessesExist(charID))
            blessDB.createCharBless(charID);

        if (!companionDB.checkIfCompanionExist(charID))
            companionDB.createCompanion(charID);

        //load character date from database
        charEquipmentsList = equipDB.getCharEquipments(charID);
        charElements = charDB.getCharacterElements(charID);
        charSkillsList = skillDB.getCharSkills(charID);
        charBlesses = blessDB.getCharBlesses(charID);

        //create character UI and set his view
        charUI = new CharacterUI(this, game, gameUI, charElements, charAttackCooldownList);

        //create character menu pages
        characterStatsUI = new CharacterStatsUI(game, this, charSkillsList.get(0), charElements);
        characterInventoryUI = new CharacterInventoryUI(game, this, charEquippedArmorIndexes, charEquippedWeaponsIndexes);
        characterSkillsUI = new CharacterSkillsUI(game, this);
        characterBlessesUI = new CharacterBlessesUI(game, this, charBlesses);

        //load and set equipments and skills
        checkEquipped();
        enablePassiveSkills();
        setSkillsTab();

        //set character texts
        charUI.setCharNameAndLevel();
        charUI.updateXPText();

        //set weapon selection by equipped weapons
        charUI.setupEquippedWeaponSelection(game, charEquipmentsList, charEquippedWeaponsIndexes, getWeaponMasteryLevel(7));

        //set weapon and element
        setWeapon(0);
        setElement(charElements.get(0));

        //set joystick
        gameUI.joyStick();

        //send user to choose third element if not selected yet
        if (charLevel >= 50 && charElements.size() <= 2 || charLevel >= 70 && charElements.size() <= 3 || charLevel >= 100 && charElements.size() <= 4)
            gameUI.selectAdditionalElement(charElements);

        //set HP and MP and their texts
        addCharHPMP(statsMixer(3), statsMixer(4));
        standHealing.start();

        //delay to get accurate sizes(height and width)
        new Handler().postDelayed(this::setCharSize, 150);
    }

    public void setWeapon(int weaponInd) {
        charCurrentWeaponIndex = weaponInd;

        //set weapon and his damage from mastery and base
        int equippedWeaponDamage;
        int weaponMasteryBonus;
        if (weaponInd > 0 && weaponInd < 6) {
            //if weapon is an equipped weapon

            //get weapon type
            charCurrentWeaponType = Weapons.values()[charEquipmentsList.get(charEquippedWeaponsIndexes[charCurrentWeaponIndex - 1]).equipType];

            //get weapon skill mastery
            weaponMasteryBonus = getWeaponMasteryLevel(charCurrentWeaponType.weaponTypeNum) * 5;

            //get weapon damage
            equippedWeaponDamage = charEquipmentsList.get(charEquippedWeaponsIndexes[charCurrentWeaponIndex - 1]).equipDMG;
        } else {

            //check if weapon is fists or blood weapons and set their damage
            if (charCurrentWeaponIndex == 0) {
                charCurrentWeaponType = Weapons.values()[0];
                equippedWeaponDamage = 50 * (charLevel / 3);
            } else {
                charCurrentWeaponType = Weapons.values()[7];
                equippedWeaponDamage = 150 * (charLevel / 3);
            }

            //get weapon skill mastery
            weaponMasteryBonus = getWeaponMasteryLevel(charCurrentWeaponType.weaponTypeNum) * 10;

        }

        //set weapon attack bonus damage
        WeaponAttacks.bonusDamage = equippedWeaponDamage + weaponMasteryBonus;

        Log.d(TAG, "weapon:" + charCurrentWeaponType + ", weapon mastery:" + getWeaponMasteryLevel(charCurrentWeaponType.weaponTypeNum) + ", weapon damage:" + equippedWeaponDamage);

        //set weapon sound
        game.setWeaponSound(charCurrentWeaponType);

        //show weapon attack button by mastery level
        charUI.setWeaponView(charCurrentWeaponType, getWeaponMasteryLevel(charCurrentWeaponType.weaponTypeNum));
    }

    public void setElement(Elements element) {
        charCurrentElementType = element;

        //set element attack bonus damage
        ElementAttacks.bonusDamage = charSkillsList.get(1).get(charCurrentElementIndex).skillLevel * 5;

        Log.d(TAG, "element:" + charCurrentElementType + ", element knowledge:" + charSkillsList.get(1).get(charCurrentElementIndex).skillLevel);

        //show element attack button by knowledge level
        charUI.setElementView(element, charSkillsList.get(1).get(charCurrentElementIndex));
    }

    //change character element if not in lord mode
    public void changeElement() {
        if (charLordState[1] <= 0) {
            if (++charCurrentElementIndex >= charElements.size())
                charCurrentElementIndex = 0;
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
                charAttackCooldownList.add(new int[]{charCurrentElementType.elementTypeNum, atk, charCurrentElementType.Attacks[atk - 4].attackCooldown});
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
            areaEffectTime = effect.Time;
            gameUI.setChat(effect.startMessage);
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

                    //lightning storm - chance to damage mob and paralyze it every 1 second
                else if (areaEffect == AreaEffects.LightningStorm && charRan.nextInt(100) + 1 <= 50) {
                    m.removeMobHP(charRan.nextInt(charLevel * 25 - charLevel * 15) + charLevel * 15);
                    m.attemptToAddElementalEffect(ElementalEffects.Paralyze);

                    //whirlwind - damage mobs if in range
                } else if (areaEffect == AreaEffects.Whirlwind) {
                    if (checkIfMobsInWhirlwindRange(m))
                        m.removeMobHP(charRan.nextInt(charLevel * 13 - charLevel * 5) + charLevel * 5);

                    //tornado - suck mobs to the middle of the area and damage them every 1 second
                } else if (areaEffect == AreaEffects.Tornado) {
                    m.removeMobHP(charRan.nextInt(charLevel * 8 - charLevel * 3) + charLevel * 3);
                    m.setMobXY((ScreenWidth - m.getMobWidth()) / 2, (ScreenHeight - m.getMobHeight()) / 2);
                }
                m.mobAttackedResult(true);
            }
    }

    private boolean checkIfMobsInWhirlwindRange(Mobs m) {
        return charX + (charWidth * 1.2) > m.getMobX()
                && charX < m.getMobX() + (m.getMobWidth() * 1.2)
                && charY + (charHeight * 1.1) > m.getMobY() * 1.3
                && charY < m.getMobY() + (m.getMobHeight() * 1.1);
    }

    public void endAreaEffect() {
        //ends area effect
        gameUI.setChat(areaEffect.stopMessage);
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
        gameUI.setChat("Area effects stop as you left the area");
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
                    if (--charLordState[1] == 0) {
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

    //check if wear equipments and add stats
    private void checkEquipped() {
        //check which equip is equipped by char
        for (int e = 0; e < charEquipmentsList.size(); e++)
            if (charEquipmentsList.get(e).equipSpot > 0) {

                //check if item is weapon or armor and add their index to equipped array
                if (charEquipmentsList.get(e).equipID < 100)
                    charEquippedArmorIndexes[charEquipmentsList.get(e).equipSpot - 1] = e;
                else
                    charEquippedWeaponsIndexes[charEquipmentsList.get(e).equipSpot - 1] = e;
                characterInventoryUI.setEquippedImage(charEquipmentsList.get(e).equipID > 100, charEquipmentsList.get(e).equipSpot - 1, charEquipmentsList.get(e).equipID, true);
            }
        //add equipped stats
        for (int value : charEquippedArmorIndexes) {
            if (value > -1) {
                equipAdd[0] += charEquipmentsList.get(value).equipDMG;
                equipAdd[1] += charEquipmentsList.get(value).equipDEF;
                equipAdd[2] += charEquipmentsList.get(value).equipHP;
                equipAdd[3] += charEquipmentsList.get(value).equipMP;
            }
        }
    }

    //equip item, remove out equip of the same type if equipped
    public void Equip(int holder) {
        if (holder > -1) {
            if (charEquipmentsList.get(holder).equipID > 100)
                equipWeapon(holder);
            else
                equipArmor(holder);
        }
    }

    private void equipArmor(int equipInd) {
        //remove equipped armor of the same type
        unEquip(charEquippedArmorIndexes[charEquipmentsList.get(equipInd).equipType - 1]);

        //equip the item
        charEquipmentsList.get(equipInd).equipSpot = charEquipmentsList.get(equipInd).equipType;
        equipDB.updateEquipped(charEquipmentsList.get(equipInd).equipmentPK, charEquipmentsList.get(equipInd).equipSpot);
        charEquippedArmorIndexes[charEquipmentsList.get(equipInd).equipType - 1] = equipInd;
        characterInventoryUI.hideItem();

        //add the equip stats
        equipAdd[0] += charEquipmentsList.get(equipInd).equipDMG;
        equipAdd[1] += charEquipmentsList.get(equipInd).equipDEF;
        equipAdd[2] += charEquipmentsList.get(equipInd).equipHP;
        equipAdd[3] += charEquipmentsList.get(equipInd).equipMP;

        //update hp\mp text by equipment stats
        if (charEquipmentsList.get(equipInd).equipHP > 0)
            charUI.updateHPText();
        if (charEquipmentsList.get(equipInd).equipMP > 0)
            charUI.updateMPText();

        //reset inventory view
        characterInventoryUI.setInventoryCharStats();
        characterInventoryUI.setEquippedImage(false, charEquipmentsList.get(equipInd).equipType - 1, charEquipmentsList.get(equipInd).equipID, true);
    }

    private void equipWeapon(int equipInd) {
        //remove equipped item first
        Equipments Weapon = charEquipmentsList.get(equipInd);
        if (getWeaponMasteryLevel(Weapon.equipType) > 0) {
            if (checkSameTypeEquipped(Weapon.equipType, equipInd)) {
                int emptySlot = checkEmptyWeaponSlot();
                if (emptySlot > -1) {

                    //equip the weapon
                    charEquipmentsList.get(equipInd).equipSpot = emptySlot + 1;
                    equipDB.updateEquipped(charEquipmentsList.get(equipInd).equipmentPK, charEquipmentsList.get(equipInd).equipSpot);
                    charEquippedWeaponsIndexes[emptySlot] = equipInd;
                    characterInventoryUI.hideItem();

                    charUI.showHideWeaponSelectButtons(true, emptySlot, Weapons.values()[charEquipmentsList.get(equipInd).equipType]);

                    //reset inventory view
                    characterInventoryUI.setInventoryCharStats();
                    characterInventoryUI.setEquippedImage(true, emptySlot, charEquipmentsList.get(equipInd).equipID, true);
                } else
                    Toasts.makeToast(game, "you can't equip more then 5 weapons");
            } else
                Toasts.makeToast(game, "you can't equip more then 1 weapon of the same type");
        } else
            Toasts.makeToast(game, "you need to rise weapon mastery skill for this weapon to equip it");
    }


    //check if theres the same type of weapon equipped
    private boolean checkSameTypeEquipped(int type, int equipInd) {
        for (int ew : charEquippedWeaponsIndexes) {
            if (ew != -1 && ew != equipInd && charEquipmentsList.get(ew).equipType == type)
                return false;
        }
        return true;
    }

    //check if theres an empty weapon slot available
    private int checkEmptyWeaponSlot() {
        for (int ew = 0; ew < 5; ew++)
            if (charEquippedWeaponsIndexes[ew] == -1) {
                return ew;
            }
        return -1;
    }

    private int getWeaponMasteryLevel(int type) {
        for (Skills s : charSkillsList.get(0))
            if (s.skillID - 100000 == type) {
                return s.skillLevel;
            }
        return 0;
    }

    //unequip an equip
    public void unEquip(int unequipInd) {
        if (unequipInd > -1) {
            //remove equip adds
            equipAdd[0] -= charEquipmentsList.get(unequipInd).equipDMG;
            equipAdd[1] -= charEquipmentsList.get(unequipInd).equipDEF;
            equipAdd[2] -= charEquipmentsList.get(unequipInd).equipHP;
            equipAdd[3] -= charEquipmentsList.get(unequipInd).equipMP;

            //reset hp\mp if unequipped equip had them in is stats
            if (charEquipmentsList.get(unequipInd).equipHP > 0)
                addCharHP(0);
            if (charEquipmentsList.get(unequipInd).equipMP > 0)
                addCharMP(0);

            //change to fists if unequipped weapon was held
            if (charCurrentWeaponType.weaponTypeNum == charEquipmentsList.get(unequipInd).equipType)
                setWeapon(0);

            //hide weapon select button if weapon
            if (charEquipmentsList.get(unequipInd).equipID > 100)
                charUI.showHideWeaponSelectButtons(false, charEquipmentsList.get(unequipInd).equipSpot - 1, null);

            //unequip the equipment and show it in inventory
            characterInventoryUI.showUnequippedItem(unequipInd);

            //set empty spot in the equipment array by type
            if (charEquipmentsList.get(unequipInd).equipID > 100)
                charEquippedWeaponsIndexes[charEquipmentsList.get(unequipInd).equipSpot - 1] = -1;
            else
                charEquippedArmorIndexes[charEquipmentsList.get(unequipInd).equipSpot - 1] = -1;

            //set unequipped image
            characterInventoryUI.setEquippedImage(charEquipmentsList.get(unequipInd).equipID > 100, charEquipmentsList.get(unequipInd).equipSpot - 1, charEquipmentsList.get(unequipInd).equipID, false);

            //update unequipped in database
            equipDB.updateEquipped(charEquipmentsList.get(unequipInd).equipmentPK, 0);

            //set equipment spot to 0(not equipped)
            charEquipmentsList.get(unequipInd).equipSpot = 0;
        }
    }

    //add passive skills effects
    private void enablePassiveSkills() {
        for (Skills ps : charSkillsList.get(3)) {
            if (ps.skillLevel > 0) {
                if (ps.skillID == 400001)
                    charPassiveSkillsAdd[0] = 10 * ps.skillLevel;
                else if (ps.skillID == 400002)
                    charPassiveSkillsAdd[1] = 10 * ps.skillLevel;
                else if (ps.skillID == 400003)
                    charPassiveSkillsAdd[2] = 100 * ps.skillLevel;
                else if (ps.skillID == 400004)
                    charPassiveSkillsAdd[3] = 100 * ps.skillLevel;
                else if (ps.skillID == 400005)
                    charPassiveSkillsAdd[4] = ps.skillLevel;
                else if (ps.skillID == 440001)
                    charPassiveSkillsAdd[5] = 10 * ps.skillLevel;
                else if (ps.skillID == 440002)
                    charPassiveSkillsAdd[6] = 10 * ps.skillLevel;
                else if (ps.skillID == 430001)
                    charPassiveSkillsAdd[7] = 10 * ps.skillLevel;
                else if (ps.skillID == 410001)
                    charPassiveSkillsAdd[8] = ps.skillLevel;
                else if (ps.skillID == 420001)
                    charPassiveSkillsAdd[9] = ps.skillLevel;
                else if (ps.skillID == 460001)
                    charPassiveSkillsAdd[10] = ps.skillLevel;
                else if (ps.skillID == 415801)
                    charPassiveSkillsAdd[11] = ps.skillLevel;
                else if (ps.skillID == 499900)
                    GMSkills[0] = 60;
                else if (ps.skillID == 499901)
                    GMSkills[1] = 1;
                else if (ps.skillID == 499902)
                    GMSkills[2] = 10;
                else if (ps.skillID == 499903)
                    GMSkills[3] = 1;
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
            Skills pot = new Skills(-1, 1, 1);
            charTabSkills.add(pot);
        }
        if (charMPpots > 0) {
            Skills pot = new Skills(-2, 1, 1);
            charTabSkills.add(pot);
        }

        //add 'close' icon
        Skills closeSV = new Skills(-10, 1, 1);
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
            if (charTabSkills.get(s).skillID < 0) {
                charTabSkills.add(s, skill);
                break;
            }
    }

    //level up skill and remove skill points
    public void levelUpSkill(Skills SHolder) {
        charDB.updateSkillPoints(charID, --charSkillPoints);
        skillDB.updateSkillLevel(++SHolder.skillLevel, SHolder.skillID, charID);

        if (SHolder.skillID / 100000 == 1) {

            //check if its the skill first level
            if (SHolder.skillLevel == 1) {

                //create stats weapon information button
                characterStatsUI.createInfoButton(true, SHolder.skillID - 100000);

                //add blood weapons
                if (SHolder.skillID == 100007)
                    charUI.addBloodWeapons(game);

                //update current weapon
            } else if (charCurrentWeaponType.weaponTypeNum == SHolder.skillID - 100000)
                setWeapon(charCurrentWeaponIndex);

            //update current element
        } else if (SHolder.skillID / 100000 == 2 && charCurrentElementType.elementTypeNum == SHolder.skillID - 200000)
            setElement(charCurrentElementType);

            //update passive skills
        else if (SHolder.skillID / 100000 == 4)
            enablePassiveSkills();
    }

    //spawn companions
    public void summonComp(int Lv) {
        //check if companion can be summoned
        if (!compIsSummoned) {

            //check if companion create
            if (Comp != null) {

                //check if companion can be summoned
                if (Comp.isCompCanBeSummoned()) {

                    //summon companion
                    Comp.summonComp(Lv);
                    charUI.setCompCmdBtnIMG(true);
                    compIsSummoned = true;
                    addCharMP(-80);
                }
            } else {
                //create companion
                Comp = new Companion(this, charUI, game, gameUI, mobsList);
                Mobs.setComp(Comp);
                summonComp(Lv);
            }
        }
    }

    //add/remove companion buff and
    public void companionBuff(int compLv, int num) {
        compBuffAdd[0] = (compLv * 10) * num;
        compBuffAdd[1] += (compLv / 10) * num;
    }

    //set companion command
    public void compCmd(boolean on) {
        charUI.setCompCmdBtnIMG(on);
        Comp.compCmd(on);
    }

    public void usePotion(int pot) {
        //HP potion
        if (pot == 1 & charHPpots > 0) {
            addCharHP(2000);
            charDB.updatePotions(charID, 1, --charHPpots);
            gameUI.setChat(charHPpots + " HP Potions left");

            //MP potion
        } else if (pot == 2 && charMPpots > 0) {
            addCharMP(2000);
            charDB.updatePotions(charID, 2, --charMPpots);
            gameUI.setChat(charMPpots + " MP Potions left");

        } else
            gameUI.setChat("you are out of this Potions");
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
            else if (activeBlessCounter >= 5)
                Toasts.makeToast(game, "cannot use more then 5 blesses at once");
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
        } else {
            if (charBlesses[bless][0] >= 30)
                Toasts.makeToast(game, "bless is maxed");
            else
                Toasts.makeToast(game, "you don't have enough point you level up");
        }
    }

    private void addBlessPoints() {
        //add blessuse point and convert to bless Use point when reach 30
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

    //call add ores and xp when mob defeated and add bless point if character defeated the mob
    public void mobDefeated(int XPMulti, boolean character) {
        addOres();
        addCharXP(XPMulti, character);
        if (character)
            addBlessPoints();
    }

    private void addOres() {
        //generate random amount of ores by character level
        int Ores = charLevel * (charRan.nextInt((10 - 1)) + 1);
        accDB.updateOres(accountID, accDB.getOres(accountID) + Ores);
    }

    private void addCharXP(int XPMulti, boolean character) {
        //set xp
        int XP = (int) ((10 + charLevel * 3 * XPMulti) * (1 + blessesAdd[6]));

        //check if companion summoned
        if (compIsSummoned)

            //add xp to character
            if (character) {
                //give 10% of the xp to companion if character defeat the mob
                Comp.compXP(XP / 10);
                XP *= 0.9;
            } else {
                //give 50% of the xp to companion if companion defeat the mob
                Comp.compXP(XP / 2);
                XP *= 0.5;
            }

        //limit character to level 200
        if (charLevel < 200) {

            //add xp to character
            charXP += XP;

            //level up character if got enough xp
            if (charXP >= XPToLevelUp)
                levelUpChar();

            //set xp to 0 if level maxed
            if (charLevel == 200)
                charXP = 0;
        }

        //set xp text
        charUI.updateXPText();

        //add xp to character
        charDB.updateEXP(charID, charXP);
    }

    private void levelUpChar() {
        //reduce xp and level up character
        charXP -= XPToLevelUp;
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

        //move character to element selection page when reach level 50,70 or 100
        if (charLevel == 50 || charLevel == 70 || charLevel == 100)
            gameUI.selectAdditionalElement(charElements);

        gameUI.setChat("Level UP!");
    }

    //generate random stats for picked item
    public void pickUpItem(int droppedItem) {
        //check if inventory is full
        if (charEquipmentsList.size() < 80) {
            int EqHp = 0, EqMp = 0, EqDmg = 0, EqDef = 0;

            //check if dropped item is armor or weapon
            if (droppedItem < 100) {

                //generate random stats when pickup
                EqDef = charRan.nextInt(30 - 25) + 25;
                if (droppedItem == 1 || droppedItem == 4)
                    EqMp = charRan.nextInt(100 - 90) + 90;
                else if (droppedItem == 2 || droppedItem == 3)
                    EqHp = charRan.nextInt(100 - 90) + 90;
                if (droppedItem == 4)
                    EqDmg = charRan.nextInt(20 - 10) + 10;
            } else
                EqDmg = charRan.nextInt(21) + 50;

            //add the item to character inventory
            Equipments newEquip = new Equipments(equipDB.totalEquipments() + 1, droppedItem, 0, 0, EqHp, EqMp, EqDmg, EqDef);
            charEquipmentsList.add(newEquip);
            equipDB.createEquipment(charID, newEquip);
            characterInventoryUI.updateInventory();
        } else
            gameUI.setChat("Inventory is full");
    }

    //load character stats by class and level
    private void loadCharStats() {
        charDamage = 30 * charLevel + (charPassiveSkillsAdd[0] * (charLevel / 10 + 1));
        charDefence = 30 * charLevel + (charPassiveSkillsAdd[1] * (charLevel / 10 + 1));
        charMaxHP = 300 * charLevel + (charPassiveSkillsAdd[2] * (charLevel / 10 + 1));
        charMaxMP = 200 * charLevel + (charPassiveSkillsAdd[3] * (charLevel / 10 + 1));
        charCriticalRate = 0.3 * charLevel;
        charCriticalDamage = charLevel;
        XPToLevelUp = CharactersData.XPToLevelUP(charLevel);
        if (charCriticalRate > 100)
            charCriticalRate = 100;
        charUI.updateHPMPText();
    }

    //return total stat value
    public int statsMixer(int stat) {
        if (stat == 1)
            return charDamage + equipAdd[0] + lordAdd[0] + (int) blessesAdd[0] + charActiveSkillsAdd[0][0] + charActiveSkillsAdd[1][0];
        else if (stat == 2)
            return charDefence + equipAdd[1] + lordAdd[1] + (int) blessesAdd[1] + charActiveSkillsAdd[0][0] + charActiveSkillsAdd[2][0] + compBuffAdd[0];
        else if (stat == 3)
            return charMaxHP + equipAdd[2] + lordAdd[2] + (int) blessesAdd[2];
        else if (stat == 4)
            return charMaxMP + equipAdd[3] + lordAdd[3] + (int) blessesAdd[3];
        else if (stat == 5)
            return charCriticalDamage + lordAdd[5] + (int) blessesAdd[5] + compBuffAdd[1];
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
        charX += x;
        charY += y;
        fixCharXY();
    }

    //set character x and y move it's image
    public void setCharXY(int x, int y) {
        charX = x;
        if (y != -1)
            charY = y;
        fixCharXY();
    }

    //fix character XY if out of screen range
    private void fixCharXY() {
        //set character image view
        if (charX < 0)
            charX = 0;
        else if (charX > ScreenWidth - charWidth)
            charX = ScreenWidth - charWidth;
        if (charY < 0)
            charY = 0;
        else if (charY > ScreenHeight - charHeight)
            charY = ScreenHeight - charHeight;
        charUI.setCharImageLocation(charX, charY);
    }

    //update character area and move it to enter portal location
    public void updateArea(int area, int x, int y) {
        charAreaID = area;
        setCharXY(x, y);
        charDB.updateCurrentArea(charID, charAreaID);

        //move companion to the next area if summoned
        if (compIsSummoned) {
            Comp.setCompXY(this);
            Comp.setCompTargets();
        }

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
                addCharHPMP(15 + charPassiveSkillsAdd[6], 15 + charPassiveSkillsAdd[7]);
                start();
            }
        }
    };

    //pause the game and show another page
    public void showPage(int page) {
        gameUI.pauseGame();
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

        //resume companions movement if summoned and alive
        if (compIsSummoned)
            Comp.resumeComp();

        //resume projectile if active
        if (charProjectileIsActive)
            projectilesRun.run();
    }

    //set character height and width
    public void setCharSize() {
        charHeight = charUI.getCharImgHeight();
        charWidth = charUI.getCharImgWidth();
        Log.d(TAG, "Height:" + charHeight + ", Width:" + charWidth);
    }

    //pop if character is dead
    private void charISDead() {
        if (compIsSummoned && Comp.resurrectChar())
            gameUI.setChat("Companion resurrected you");
        else {
            charHP = 0;
            game.Pause = true;
            final AlertDialog.Builder endgame = new AlertDialog.Builder(game);
            endgame.setTitle("YOU DIED!");
            endgame.setMessage("you will be send back to the lobby");
            endgame.setPositiveButton("Back to Lobby", (dialog, which) -> game.backToLobby());
            endgame.setOnDismissListener(dialog -> game.backToLobby());
            endgame.show();
        }
    }

    //add new element and skills to character
    public void addNewElement(int charElement, Elements element) {
        charElements.add(element);
        charDB.updateElements(charID, charElement, element.elementTypeNum);
        SkillsAdder.addElementSkills(charID, skillDB, element, charElements);
    }

    public void characterMovement(int way) {
        charUI.characterMovement(way);
    }

    public void startStandHealing() {
        standHealing.start();
    }

    public int getCharHeight() {
        return charHeight;
    }

    public int getXPToLevelUp() {
        return XPToLevelUp;
    }

    public int getCharWidth() {
        return charWidth;
    }

    public void changeCharSide() {
        charUI.setCharImageSide();
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

    public int getCharID() {
        return charID;
    }

    public void setCharID(int charID) {
        this.charID = charID;
    }

    public int getCharLevel() {
        return charLevel;
    }

    public void setCompIsSummoned(boolean compIsSummoned) {
        this.compIsSummoned = compIsSummoned;
    }

    public void setCharLevel(int charLevel) {
        this.charLevel = charLevel;
    }

    public int getCharXP() {
        return charXP;
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

    public int getCharDefence() {
        return charDefence;
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
        return charCurrentElementType.elementTypeNum;
    }

    public int getCharAreaID() {
        return charAreaID;
    }

    public void setCharAreaID(int charAreaID) {
        this.charAreaID = charAreaID;
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

    public List<Equipments> getCharEquipmentsList() {
        return charEquipmentsList;
    }

    public Equipments getEquip(int ind) {
        return charEquipmentsList.get(ind);
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

    public void setAccDB(AccountsDB accountID) {
        accDB = accountID;
    }
}