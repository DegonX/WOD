package com.degonx.warriorsofdeagon.Objects;


import static android.content.ContentValues.TAG;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.degonx.warriorsofdeagon.Enums.EffectsEnums.ElementalEffects;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Enums.MobsType;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.CharactersData;
import com.degonx.warriorsofdeagon.R;
import com.degonx.warriorsofdeagon.UI.CharacterUI;
import com.degonx.warriorsofdeagon.UI.GameUI;
import com.degonx.warriorsofdeagon.Database.CompanionsDB;

import java.util.List;
import java.util.Random;

public class Companion {

    private final int compID;
    private int compLevel;
    private int compXP;
    private int compHP;
    private int compMP;
    private int compDamage;
    private int compDefence;
    private Mobs compTarget;
    private double targetDamageDivision;
    private int compX;
    private int compY;
    private int compHeight;
    private int compWidth;
    private Weapons compCurrentWeapon = Weapons.Scythe;
    private Elements compCurrentElement = Elements.Lightning;
    private int compWeaponLevel;
    private int compElementLevel;
    private int compSkillLv;
    private int compXPtoLevelUp;
    private int familiarsTime = -180;

    private boolean compCanResurrect = true;
    private boolean compCanBeSummoned = true;
    private boolean compToMob = true;
    private boolean compFromMob = true;
    private boolean compMode = true;

    private final ImageView compIMG;
    private final ImageView[] FamiliarsIMG = new ImageView[2];

    private final Handler compHandler = new Handler();
    private final Handler dmgToCompHandler = new Handler();
    private final Handler compDmgToMobHandler = new Handler();
    private final Handler movementHandler = new Handler();
    private final Handler familiarsHandler = new Handler();
    private final Random compRan = new Random();

    private final Character Char;
    private final CharacterUI charUI;
    private final List<Mobs> mobList;
    private final Game game;
    private final GameUI gameUI;
    private final CompanionsDB compDB;


    public Companion(Character Char, CharacterUI charUI, Game game, GameUI gameUI, List<Mobs> mobList) {
        this.Char = Char;
        this.charUI = charUI;
        this.mobList = mobList;
        this.game = game;
        this.gameUI = gameUI;

        //get companion data from database
        compDB = new CompanionsDB(game);
        int[] CompData = compDB.getCompanionData(Char.getCharID());
        compID = CompData[0];
        compLevel = CompData[1];
        compXP = CompData[2];
        compWeaponLevel = CompData[3];
        compElementLevel = CompData[4];

        //create companion view
        compIMG = new ImageView(game);
        gameUI.addRemoveFromGameLayout(compIMG, true);
        compIMG.setImageResource(R.drawable.companion);
        compIMG.getLayoutParams().height = 315;
        compIMG.getLayoutParams().width = 225;

        //getting companion view height/width
        new Handler().postDelayed(() -> {
            compHeight = compIMG.getHeight();
            compWidth = compIMG.getWidth();
        }, 30);
    }

    public void summonComp(int compSkillLv) {
        //set skill level
        this.compSkillLv = compSkillLv;

        //set companion stats
        setCompStats();

        //set companion at character place
        setCompXY(Char);

        //show companions
        compIMG.setVisibility(View.VISIBLE);

        //reset companion target
        compTarget = null;

        //start companion buff
        Char.companionBuff(compLevel, 1);

        //show companion objects
        charUI.showHideCompObjects(compHP);

        //start companion movement
        compMovementRun.run();

        //summon healing familiars
        summonFamiliars();

        gameUI.setChat("Companion Level:" + compLevel);
    }

    //set companion location when summon or change area
    public void setCompXY(Character Char) {
        compX = Char.getCharX();
        compY = Char.getCharY();
        compIMG.setX(compX);
        compIMG.setY(compY);
    }

    //set companion stata
    private void setCompStats() {
        compDamage = 100 * compLevel + (compSkillLv * compLevel);
        compDefence = 70 * compLevel + (compSkillLv * compLevel);
        compHP = 400 * compLevel + (1000 * compSkillLv);
        compMP = 300 * compLevel + (1000 * compSkillLv);
        compXPtoLevelUp = CharactersData.XPToLevelUP(compLevel);
        charUI.setCompHealthHBar(compHP);
        Log.d(TAG, "Companion stats:" + compDamage + "/" + compDefence + "/" + compHP + "/" + compMP);
    }

    //change to random weapon and get its attack data
    private void changeCompWeapon() {
        compCurrentWeapon = Weapons.values()[compRan.nextInt(4) + 8];
        Log.d(TAG, "Companion weapon:" + compCurrentWeapon);
    }

    //change element by target type and get attack data
    private void changeCompElement() {
        MobsType targetType = compTarget.getMobType();
        if (targetType == MobsType.Water || targetType == MobsType.Darkness)
            compCurrentElement = Elements.Lightning;
        else if (targetType == MobsType.Fire || targetType == MobsType.Lightning)
            compCurrentElement = Elements.Earth;
        else
            compCurrentElement = Elements.Wind;
        Log.d(TAG, "Companion element:" + compCurrentElement);
    }

    //companion movement runnable
    private final Runnable compMovementRun = new Runnable() {
        @Override
        public void run() {
            if (!game.Pause && compHP > 0) {
                if (compMode) {

                    //change if companion has target and target alive and chase it
                    if (compTarget != null && !compTarget.isMobIsDead()) {
                        if (compX > compTarget.getMobX()) {
                            compX -= 6;
                            compIMG.setScaleX(-1);
                        } else {
                            compX += 6;
                            compIMG.setScaleX(1);
                        }
                        if (compY > (compTarget.getMobY() + 100))
                            compY -= 6;
                        else
                            compY += 6;
                        compIMG.setX(compX);
                        compIMG.setY(compY);

                        //companions hit mobs
                        if (compToMob && checkCompRange())
                            attackMob();
                    } else

                        //generate companions new target if current target is dead or null
                        generateTarget();

                } else {
                    if (compY > 150 || compX > 10) {
                        if (compX >= 10)
                            compX -= 6;

                        if (compY >= 150)
                            compY -= 6;
                        else
                            compY += 6;

                        compIMG.setX(compX);
                        compIMG.setY(compY);
                    }
                }
                //0.05 sec delay for movement
                movementHandler.postDelayed(compMovementRun, 50);
            }
        }
    };

    //check if companion is close to target
    private boolean checkCompRange() {
        return compY < compTarget.getMobY() + (compTarget.getMobHeight() * 0.9)
                && compY + (compHeight * 0.5) > compTarget.getMobY()
                && compX + compWidth + 175 > compTarget.getMobX()
                && compX - 175 < compTarget.getMobX() + compTarget.getMobWidth();
    }

    //attack target
    private void attackMob() {
        int atkIndex;

        if (compMP > 150 && compRan.nextInt(2) == 1) {
            //element attack

            //select element attack that cab be used if theres enough mp
            do
                atkIndex = compRan.nextInt(3);
            while (compCurrentElement.Attacks[atkIndex].attackManaCost > compMP);

            //reduce element attack mp cost
            compMP -= compCurrentElement.Attacks[atkIndex].attackManaCost;

            //attack target
            for (int ca = 1; ca <= compCurrentElement.Attacks[atkIndex].attackTimes; ca++) {
                compTarget.removeMobHP((int) ((compCurrentElement.Attacks[atkIndex].attackDamage * (compElementLevel * 30) * (compRan.nextDouble() * (1.25 - 0.75)) + 0.75) * targetDamageDivision));
                if (compTarget.isMobIsDead())
                    break;
            }

            //if element is lightning try to paralyze target
            if (compCurrentElement == Elements.Lightning && !compTarget.isMobIsDead())
                compTarget.attemptToAddElementalEffect(ElementalEffects.Paralyze);

        } else {
            //weapon attack

            //select weapon attack that cab be used if theres enough mp
            do
                atkIndex = compRan.nextInt(4);
            while (compCurrentWeapon.Attacks[atkIndex].attackManaCost > compMP);

            //reduce weapon attack mp cost
            compMP -= compCurrentWeapon.Attacks[atkIndex].attackManaCost;

            //attack target
            for (int ca = 1; ca <= compCurrentWeapon.Attacks[atkIndex].attackTimes; ca++) {
                compTarget.removeMobHP((int) ((compCurrentWeapon.Attacks[atkIndex].attackDamage * (compWeaponLevel * 30) * (compRan.nextDouble() * (1.25 - 0.75)) + 0.75) * targetDamageDivision));
                if (compTarget.isMobIsDead())
                    break;
            }
        }

        compTarget.mobAttackedResult(false);

        //attack delay
        compToMob = false;
        compDmgToMobHandler.postDelayed(() -> compToMob = true, 800);
    }

    public void compXP(int XP) {
        if (compLevel < 200) {

            //add companion xp and check if can level up
            compXP += XP;
            if (compXP >= compXPtoLevelUp) {
                levelUpComp();
                compXP -= compXPtoLevelUp;
            }

            //update xp in database
            compDB.updateCompXP(compID, XP);
        }
    }

    private void levelUpComp() {
        //level up companion
        compDB.updateCompLevel(compID, ++compLevel);

        //level up weapon or element level
        if (compLevel % 2 == 0)
            compDB.updateCompWeaLevel(compID, ++compWeaponLevel);
        else
            compDB.updateCompEleLevel(compID, ++compElementLevel);

        //reset companion stats and buff
        setCompStats();
        Char.companionBuff(compLevel, 1);
        gameUI.setChat("Companion Leveled up to:" + compLevel);
    }

    public void damageComp(int dmg) {
        //damage companion and start damage delay
        if (dmg < 1)
            dmg = 1;

        compHP -= dmg;

        //update companion health bar
        charUI.updateCompHealthHBar(compHP);

        //delay before can be hit again
        compFromMob = false;
        dmgToCompHandler.postDelayed(() -> compFromMob = true, 800);

        //hide companion if dead
        if (compHP <= 0) {
            gameUI.setChat("Companion dead");

            //hide companion
            compIMG.setVisibility(View.GONE);

            //reset companion mode
            compMode = true;

            //stop his movement
            movementHandler.removeCallbacks(compMovementRun);

            //remove companion buff
            Char.companionBuff(compLevel, 0);

            //hide companion objects
            charUI.showHideCompObjects(0);

            //start companion cooldown
            Char.setCompIsSummoned(false);
            compCanBeSummoned = false;
            compHandler.postDelayed(() -> compCanBeSummoned = true, 180000);

        }
    }

    //set companion mode to attack or stay back
    public void compCmd(boolean act) {
        if (act) {
            compMode = true;
            gameUI.setChat("Companion attacking");
        } else {
            compMode = false;
            gameUI.setChat("Companion staying back");
        }
    }

    //resurrect character if dead
    public boolean resurrectChar() {
        //check if can resurrect
        if (compCanResurrect) {

            //set character hp to full health
            Char.addCharHP(Char.getCharMaxHP());

            //set 5minutes resurrection cooldown
            compCanResurrect = false;
            compHandler.postDelayed(() -> compCanResurrect = true, 300000);
            return true;
        }
        return false;
    }

    //generate new target for the companion to chase
    private void generateTarget() {
        //select new random target check if new target alive(will select new target if not)
        compTarget = mobList.get(compRan.nextInt(mobList.size()));

        if (!compTarget.isMobIsDead()) {

            //damage division and limiter for companion attacks
            targetDamageDivision = (double) compDamage / compTarget.getMobDefence();
            if (targetDamageDivision > 1.3)
                targetDamageDivision = 1.3;
            else if (targetDamageDivision < 0.7)
                targetDamageDivision = 0.7;

            //change companion weapon and element
            changeCompWeapon();
            changeCompElement();
        }
    }

    //summon familiars for 2 minutes
    private void summonFamiliars() {
        if (FamiliarsIMG[0] == null)
            createFamiliar();
        setFamiliars(true);
        familiarsTime = 120;
        familiarsRun.run();
    }

    private void createFamiliar() {
        //create familiars views
        for (int f = 0; f < 2; f++) {
            FamiliarsIMG[f] = new ImageView(game);
            gameUI.addRemoveFromGameLayout(FamiliarsIMG[f], true);
            FamiliarsIMG[f].getLayoutParams().height = 125;
            FamiliarsIMG[f].getLayoutParams().width = 170;
        }

        //set familiars images
        FamiliarsIMG[0].setImageResource(R.drawable.familiarred);
        FamiliarsIMG[1].setImageResource(R.drawable.familiarblue);
    }

    //show\hide familiars and set their location
    private void setFamiliars(boolean show) {
        for (int f = 0; f < 2; f++) {
            if (show) {
                FamiliarsIMG[f].setX(compX - 100);
                FamiliarsIMG[f].setY(compY + 50 - (200 * -f));
                FamiliarsIMG[f].setVisibility(View.VISIBLE);
            } else
                FamiliarsIMG[f].setVisibility(View.GONE);
        }
    }

    //timer for familiars actions with 1 second delay
    private final Runnable familiarsRun = new Runnable() {
        @Override
        public void run() {
            if (familiarsTime > -180 && !game.Pause) {
                familiarsTime--;
                if (familiarsTime >= 0) {
                    //change familiars side to character side
                    if (Char.getCharX() > FamiliarsIMG[0].getX()) {
                        FamiliarsIMG[0].setScaleX(1);
                        FamiliarsIMG[1].setScaleX(1);
                    } else {
                        FamiliarsIMG[0].setScaleX(-1);
                        FamiliarsIMG[1].setScaleX(-1);
                    }

                    //heal character every 5 seconds
                    if (familiarsTime % 10 == 0 && Char.getCharHP() > 0) {
                        Char.addCharHPMP(10 * Char.getCharLevel(), 10 * Char.getCharLevel());
                        if (compHP > 0) {
                            compHP += 10 * compLevel;
                            compMP += 10 * compLevel;
                            charUI.updateCompHealthHBar(compHP);
                        }
                    }

                    //check if familiars timeout and end their actions
                    if (familiarsTime == 0) {
                        endFamiliars();
                        gameUI.setChat("healing familiars left");
                    }
                }
                //1 second delay
                familiarsHandler.postDelayed(familiarsRun, 1000);
            } else if (familiarsTime == -180 && compHP > 0)
                summonFamiliars();
        }
    };

    //set\reset companion target
    public void setCompTargets() {
        compTarget = null;
    }

    //start\resume companion runnable
    public void resumeComp() {
        compMovementRun.run();

        //resume familiars if summoned
        if (familiarsTime > -180)
            familiarsRun.run();
    }

    //remove familiars from area
    public void endFamiliars() {
        setFamiliars(false);
    }

    public boolean isCompCanBeSummoned() {
        return compCanBeSummoned;
    }

    public int getCompDefence() {
        return compDefence;
    }

    public int getCompLevel() {
        return compLevel;
    }

    public int getCompHP() {
        return compHP;
    }

    public int getCompX() {
        return compX;
    }

    public int getCompY() {
        return compY;
    }

    public int getCompHeight() {
        return compHeight;
    }

    public int getCompWidth() {
        return compWidth;
    }

    public boolean isCompFromMob() {
        return compFromMob;
    }
}