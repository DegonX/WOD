package com.degonx.warriorsofdeagon.Objects;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.degonx.warriorsofdeagon.Enums.EffectsEnums.AreaEffects;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Enums.MobsType;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.UI.GameUI;
import com.degonx.warriorsofdeagon.UI.MobsUI;

import java.util.Random;

import static android.content.ContentValues.TAG;
import static com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl.ScreenHeight;
import static com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl.ScreenWidth;

public class Mobs {

    //protected int mobID;
    protected int mobMaxHP;
    protected int mobBaseHP;
    protected int mobHP;
    protected int mobXP;
    protected int mobBaseAttack;
    protected int mobAttack;
    protected int mobBaseDefence;
    protected int mobDefence;
    protected int mobDefaultX;
    protected int mobDefaultY;
    protected int mobX;
    protected int mobY;
    protected int mobXDes;
    protected int mobYDes;
    protected int mobHeight;
    protected int mobWidth;
    protected double mobBaseSpeed;
    protected double mobSpeed = 0;
    protected int MobActiveElementalEffectCounter = 0;
    protected int mobAreaID;
    protected int mobSide = 1;
    protected boolean mobAggro = false;
    protected boolean mobIsDead = false;
    protected boolean mobIsActive = true;
    protected static int charLevel;
    protected int[] mobElementalEffects = new int[3];//mobElementalEffects - burn time + freeze time + paralyze time
    protected MobsType mobType;

    private final Handler mobHandler = new Handler();
    protected static Handler dmgToCharHandler = new Handler();
    protected static Handler mobAttackedDelay = new Handler();
    protected static Random mobRan = new Random();

    protected MobsUI mobUI;
    protected static Game game;
    protected GameUI gameUI;
    protected static Character Char;
    protected static Companion Comp;

    public Mobs(int mobID, int mobBaseHP, int mobXP, int mobX, int mobY, int mobAttack, int mobDefence, int mobSpeed, int mobAreaID, GameUI gameUI) {
//        this.mobID = mobID;
        this.mobBaseHP = mobBaseHP;
        this.mobBaseAttack = mobAttack;
        this.mobBaseDefence = mobDefence;
        this.mobXP = mobXP;
        mobXDes = mobDefaultX = mobX;
        mobYDes = mobDefaultY = mobY;
        this.mobAreaID = mobAreaID;
        this.gameUI = gameUI;

        //set mob stats
        setMobStats();

        //create mob views
        mobUI = new MobsUI(mobMaxHP, game, gameUI, this, this.mobAreaID);

        //set mob view location
        mobUI.setMobMovement(1, mobX, mobY);

        //change mob color by type
        mobUI.setMobElementColor(mobType);

        //delay inorder to get accurate sizes(height and width)
        new Handler().postDelayed(() -> {
            setMobSize();
            this.mobSpeed = mobBaseSpeed = mobSpeed;
            mobMovementRun.run();
        }, 40);

        Log.d(TAG, "Mob:" + mobID + " Created ");
    }

    //set mob stats when created or respawn
    private void setMobStats() {
        mobY = mobDefaultY;
        mobX = mobDefaultX;
        mobHP = mobMaxHP = 400 * mobBaseHP * charLevel;
        mobAttack = mobBaseAttack * charLevel;
        mobDefence = mobBaseDefence * charLevel;
        mobType = MobsType.values()[mobRan.nextInt(8)];
    }

    //mobs movement runnable
    private final Runnable mobMovementRun = new Runnable() {
        @Override
        public void run() {
            if (!game.Pause) {

                //check if mob can move
                if (mobSpeed > 0 && Char.getGMSkill(5) == 0) {

                    //if mob is not aggressive
                    if (!mobAggro) {
                        //generate mob destination
                        if (mobX - mobXDes < 8 && mobX - mobXDes > -8 && mobY - mobYDes < 7 && mobY - mobYDes > -7) {
                            mobXDes = mobRan.nextInt(ScreenWidth - mobWidth);
                            mobYDes = mobRan.nextInt(ScreenHeight - mobHeight + 100) - 100;
                        } else {

                            //move to destination
                            if (mobXDes - mobX > 5 || mobXDes - mobX < -5)
                                if (mobX > mobXDes) {
                                    mobX -= 3 * mobSpeed;
                                    mobSide = 1;
                                } else {
                                    mobX += 3 * mobSpeed;
                                    mobSide = -1;
                                }
                            if (mobYDes - mobY > 4 || mobYDes - mobY < -4)
                                if (mobY > mobYDes)
                                    mobY -= 2 * mobSpeed;
                                else
                                    mobY += 2 * mobSpeed;
                        }
                    } else {
                        //chase character
                        if (mobX > Char.getCharX()) {
                            mobX -= 3 * mobSpeed;
                            mobSide = 1;
                        } else {
                            mobX += 3 * mobSpeed;
                            mobSide = -1;
                        }

                        if (mobY > Char.getCharY() - 100)
                            mobY -= 2 * mobSpeed;
                        else
                            mobY += 2 * mobSpeed;
                    }
                    mobUI.setMobMovement(mobSide, mobX, mobY);
                }

                //check if mob can hit the character
                if (Char.getCharHP() > 0 && Char.isCharHitDelay() && checkIfInRange(true))
                    //damage character
                    damageCharAndComp(true, 1);

                //check if mob can hit the character companions
                if (Comp != null && Comp.getCompHP() > 0 && Comp.isCompFromMob() && checkIfInRange(false))
                    //damage companion
                    damageCharAndComp(false, 1);

                //0.05 sec delay(movement speed)
                mobHandler.postDelayed(mobMovementRun, 50);
            }
        }
    };

    private boolean checkIfInRange(boolean isChar) {
        //check if mob touches the character
        if (isChar)
            return Char.getCharX() + (Char.getCharWidth() * 0.65) > mobX
                    && Char.getCharX() < mobX + (mobWidth * 0.65)
                    && Char.getCharY() + (Char.getCharHeight() * 0.5) > mobY * 1.3
                    && Char.getCharY() < mobY + (mobHeight * 0.8);

            //check if mob touches companions
        else
            return Comp.getCompX() + ((Comp.getCompWidth()) * 0.5) > mobX
                    && Comp.getCompX() < mobX + (mobWidth * 0.5)
                    && Comp.getCompY() + (Comp.getCompHeight() * 0.5) > mobY * 1.3
                    && Comp.getCompY() < mobY + (mobHeight * 0.5);
    }

    //return multiplier that increases\decreases mob damage by character\companion defence
    private double changeMobDamageByDefence(int Defence) {
        double damageMultiplier = (double) mobAttack / Defence;

        //limit mob damage range
        if (damageMultiplier > 1.5)
            damageMultiplier = 1.5;
        else if (damageMultiplier < 0.2)
            damageMultiplier = 0.2;

        return damageMultiplier;
    }

    //damage character
    protected void damageCharAndComp(boolean isChar, int mul) {
        if (isChar) {
            //damage character

            //attempt to avoid attack
            if (mobRan.nextInt(100) + 1 > Char.getPassiveSkillsAdd(11)) {
                Char.addCharHP(-(int) (mobAttack * mul * (mobRan.nextDouble() * (1.25 - 0.75) + 0.75) * changeMobDamageByDefence(Char.statsMixer(2))));
            } else
                gameUI.setChat("avoided attack");

            //character damage delay
            Char.setCharHitDelay(false);
            dmgToCharHandler.postDelayed(() -> Char.setCharHitDelay(true), 1000);

        } else
            //damage companion
            Comp.damageComp((int) (mobAttack * mul * (((charLevel - Comp.getCompLevel()) / 10) + 1) * (mobRan.nextDouble() * (1.25 - 0.75) + 0.75) * changeMobDamageByDefence(Comp.getCompDefence())));
    }

    public void mobAttacked(ElementalEffects eleEffect) {
        //make mob chase the character
        mobAggro = true;

        //elemental effects
        if (eleEffect != ElementalEffects.None)
            attemptToAddElementalEffect(eleEffect);

        //push mob back
        if (this.getClass() != Bosses.class)
            if (Char.getCharX() < mobX)
                moveMob(25, 0);
            else
                moveMob(-25, 0);
        else
            ((Bosses) this).addMana();

        //stop mob movement for 0.1 seconds
        mobHandler.removeCallbacks(mobMovementRun);
        if (mobElementalEffects[2] <= 0)
            mobAttackedDelay.postDelayed(mobMovementRun, 100);
    }

    //check if mob is dead add XP,drop and call respawn timer
    public void mobAttackedResult(boolean character) {
        if (!mobIsDead && mobHP <= 0) {

            //hide mob and start respawn time
            mobIsDead = true;

            //hide elemental effects icons
            for (int mi = 1; mi <= 3; mi++)
                mobUI.showHideImages(mi, false);

            //hide mob view
            mobUI.showMobView(false);

            //set to respawn mob
            mobRespawnRun.run();

            //gives character ores and xp
            Char.mobDefeated(mobXP, character);

            //create drop
            itemDrop();

            //stop movement and elemental effects runnables
            mobHandler.removeCallbacks(mobMovementRun);
            mobHandler.removeCallbacks(mobElementalEffectRun);

        } else
            //set mob hp text
            mobUI.setMobHPText(mobHP);
    }

    //attempt to drop item
    private void itemDrop() {
        //chance to drop item
        if (mobRan.nextInt(100) + 1 <= 20 + Char.getGMSkill(0))
            gameUI.createDrop(mobX + mobWidth / 2, mobY + mobHeight / 2, 0);
    }

    //runnable timer before mob respawn
    private final Runnable mobRespawnRun = new Runnable() {
        @Override
        public void run() {
            mobHandler.postDelayed(() -> {
                //check is respawn possible
                if (!game.Pause && mobIsActive && mobIsDead)
                    //respawn mob
                    respawnMob();
                else
                    //restart respawn runnable
                    mobRespawnRun.run();
            }, 7000);
        }
    };

    //respawn mob
    private void respawnMob() {
        //reset mob stats
        setMobStats();

        //change mob color by type
        mobUI.setMobElementColor(mobType);

        //reset variables to default
        mobSpeed = mobBaseSpeed;
        if (Char.getAreaEffect() == AreaEffects.DarknessFog)
            mobSpeed = mobBaseSpeed / 2;
        mobElementalEffects[0] = mobElementalEffects[1] = mobElementalEffects[2] = MobActiveElementalEffectCounter = 0;
        mobAggro = mobIsDead = false;

        //reset mob views
        mobUI.setMobHPText(mobMaxHP);
        mobUI.setMobMovement(1, mobX, mobY);
        mobUI.showMobView(true);

        if (this.getClass() == Bosses.class)
            ((Bosses) this).respawnBoss();

        //start mob movement
        mobMovementRun.run();
    }

    //attempt to add elemental effects
    public void attemptToAddElementalEffect(ElementalEffects eleEffect) {
        //check if the mob is alive and not a boss
        if (!mobIsDead && this.getClass() != Bosses.class) {
            if (eleEffect == ElementalEffects.Chance) {
                for (int ee = 1; ee < 4; ee++)
                    if (mobElementalEffects[ee - 1] == 0 && mobRan.nextInt(100) + 1 <= Char.getPassiveSkillsAdd(7 + ee))
                        addElementalEffect(getElementalEffect(ee));
            } else if (mobElementalEffects[eleEffect.getEffectNum() - 1] == 0 && mobRan.nextInt(100) + 1 <= 30)
                addElementalEffect(eleEffect);
        }
    }

    private ElementalEffects getElementalEffect(int ee) {
        if (ee == 1)
            return ElementalEffects.Burn;
        else if (ee == 2)
            return ElementalEffects.Freeze;
        else
            return ElementalEffects.Paralyze;
    }

    //add elemental effects on mob if not active and not the same\resist type
    private void addElementalEffect(ElementalEffects eleEffect) {
        boolean elementalEffectAdded = false;

        if (eleEffect == ElementalEffects.Burn) {
            if (mobType != MobsType.Fire && mobType != MobsType.Water) {
                mobElementalEffects[0] = 20;
                elementalEffectAdded = true;
            }
        } else if (eleEffect == ElementalEffects.Freeze) {
            if (mobType != MobsType.Fire && mobType != MobsType.Darkness) {
                mobElementalEffects[1] = 20;
                elementalEffectAdded = true;

                //slow mob speed by half
                mobSpeed = mobBaseSpeed / 2;
            }
        } else if (eleEffect == ElementalEffects.Paralyze) {
            if (mobType != MobsType.Light && mobType != MobsType.Lightning) {
                mobElementalEffects[2] = 20;
                elementalEffectAdded = true;

                //stop mob movement
                mobHandler.removeCallbacks(mobMovementRun);
            }
        }

        //check if elemental effects was added to the mob
        if (elementalEffectAdded) {

            //rise elemental effects counter
            MobActiveElementalEffectCounter++;

            //start / reset elemental effects time
            mobHandler.removeCallbacks(mobElementalEffectRun);
            mobElementalEffectRun.run();

            //show elemental effects icon
            mobUI.showHideImages(eleEffect.getEffectNum(), true);
        }
    }

    //elemental effects runnable
    private final Runnable mobElementalEffectRun = new Runnable() {
        @Override
        public void run() {
            if (!game.Pause && MobActiveElementalEffectCounter > 0) {

                //fire effect and burn
                if (mobElementalEffects[0] > 0) {
                    //damage mob from burn
                    mobHP -= mobMaxHP / 400;
                    mobAttackedResult(true);

                    //check if burn timeout
                    if (--mobElementalEffects[0] == 0) {
                        //remove burn
                        mobUI.showHideImages(1, false);
                        MobActiveElementalEffectCounter--;
                    }
                }

                //ice effect
                //check if freeze timeout
                if (--mobElementalEffects[1] == 0) {

                    //check if mobs are not slowed by other effects
                    if (Char.getAreaEffect() != AreaEffects.DarknessFog) {

                        //return mob speed to normal(2)
                        mobSpeed = mobBaseSpeed;
                    }
                    //remove freeze
                    mobUI.showHideImages(2, false);
                    MobActiveElementalEffectCounter--;
                }

                //lightning effect
                //check if paralyze timeout
                if (--mobElementalEffects[2] == 0) {

                    //remove paralyze
                    mobUI.showHideImages(3, false);
                    MobActiveElementalEffectCounter--;

                    //resume mob movement
                    mobMovementRun.run();
                }

                mobHandler.postDelayed(mobElementalEffectRun, 1000);
            }
        }
    };

    public void setMobXY(int x, int y) {
        if (this.getClass() != Bosses.class) {
            mobX = x;
            if (y > -1)
                mobY = y;
            fixMobXY();
            mobUI.setMobMovement(mobSide, mobX, mobY);
        }
    }

    public void moveMob(int x, int y) {
        if (this.getClass() != Bosses.class) {
            this.mobX += x;
            this.mobY += y;
            fixMobXY();
            mobUI.setMobMovement(mobSide, mobX, mobY);
        }
    }

    //check and fix if mob XY is out of screen range
    private void fixMobXY() {
        if (mobX + mobWidth > ScreenWidth)
            mobX = ScreenWidth - mobWidth;
        else if (mobX < 0)
            mobX = 0;

        if (mobY + mobHeight > ScreenHeight)
            mobY = ScreenHeight - mobHeight;
        else if (mobY + 100 < 0)
            mobY = 100;
    }

    //reactivate/deactivate mob
    public void setMobActivation(boolean mobIsActive) {
        this.mobIsActive = mobIsActive;
        if (mobIsActive) {
            if (!mobIsDead) {

                //check if mob is not paralyze and alive inorder to start moving
                if (mobElementalEffects[2] <= 0)
                    mobMovementRun.run();

                //check if mob have elemental effects
                if (MobActiveElementalEffectCounter > 0)
                    mobElementalEffectRun.run();

                if (this.getClass() == Bosses.class)
                    ((Bosses) this).resumeBossAttack();

            } else {
                //start/restart respawn timer if mob is dead
                mobHandler.removeCallbacks(mobRespawnRun);
                mobRespawnRun.run();
            }
        } else {
            //deactivate mob
            mobHandler.removeCallbacks(mobMovementRun);
            mobHandler.removeCallbacks(mobElementalEffectRun);
        }
    }

    public void removeMobHP(int hp) {
        mobHP -= hp;
    }

    //set mobs height and width
    private void setMobSize() {
        mobHeight = mobUI.getMobHeight();
        mobWidth = mobUI.getMobWidth();
        Log.d(TAG, "Hei:" + mobHeight + ", Wid:" + mobWidth);
    }

    //change mob speed
    public void setMobSpeed(boolean base) {
        if (base)
            mobSpeed = mobBaseSpeed;
        else
            mobSpeed = mobBaseSpeed / 2;
    }

    public static void setGameNChar(Game Mgame, Character Mchar) {
        game = Mgame;
        Char = Mchar;
    }

    public int getMobElementalEffect(int ele) {
        return mobElementalEffects[ele - 1];
    }

    public View getMobViewFromUI() {
        return mobUI.getMobView();
    }

    public static void setComp(Companion comp) {
        Comp = comp;
    }

    public static void setCharLevel(int level) {
        charLevel = level;
    }

    public int getMobHeight() {
        return mobHeight;
    }

    public int getMobWidth() {
        return mobWidth;
    }

    public int getMobHP() {
        return mobHP;
    }

    public int getMobDefence() {
        return mobDefence;
    }

    public MobsType getMobType() {
        return mobType;
    }

    public int getMobX() {
        return mobX;
    }

    public int getMobY() {
        return mobY;
    }

    public boolean isMobIsDead() {
        return mobIsDead;
    }

    public int getMobAreaID() {
        return mobAreaID;
    }
}