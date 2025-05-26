package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.degonx.warriorsofdeagon.Actions.AdminCodes;
import com.degonx.warriorsofdeagon.Actions.NPCActions;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.NPC;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Portal;
import com.degonx.warriorsofdeagon.Enums.ItemsEnums.DropsPool;
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.AreaEffects;
import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEnum;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.GameData.WeaponsAndElementsData;
import com.degonx.warriorsofdeagon.VisionAndSound.GameScrollView;
import com.degonx.warriorsofdeagon.VisionAndSound.JoyStickClass;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.R;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.SettingDB;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressLint({"SetTextI18n", "DiscouragedApi"})
public class GameUI {


    private final Game game;
    private final Character Char;
    private final SettingDB setDB;

    private final List<ImageView> droppedItem = new ArrayList<>();
    private final Handler gameUIHandler = new Handler();
    private final Random gameUIRan = new Random();
    private final ConstraintLayout gameBackground;
    private ConstraintLayout settingL;
    private int areaMaxX, areaMaxY, chatView = 0;
    private final LinearLayout chatScrollLayout;
    private final ImageView transBG;
    private ImageView projectileImg;
    private final ScrollView chatScroll;
    public GameScrollView gameAreaScrolling;
    private JoyStickClass Stick;

    //load View for game layout
    @SuppressLint("ClickableViewAccessibility")
    public GameUI(Game game, Character Char, SettingDB setDB) {
        this.game = game;
        this.Char = Char;
        this.setDB = setDB;

        //find game scrollview and its layout
        gameAreaScrolling = game.findViewById(R.id.gameAreaScrolling);
        gameBackground = game.findViewById(R.id.gameBackground);

        //find trans background (used for area effects)
        transBG = game.findViewById(R.id.transBG);

        //create a imageview and add it to game background layout (used to fix full screen error)
        ImageView newView = new ImageView(game);
        newView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT));
        newView.setImageResource(R.drawable.blinkarea);
        gameBackground.addView(newView);

        //load chat
        chatScroll = game.findViewById(R.id.log);
        chatScrollLayout = game.findViewById(R.id.ScrollLayout);
        Button changeLog = game.findViewById(R.id.changelog);
        changeLog.setOnClickListener(cs -> setChatShow());

        //create dialog builder
        AlertDialog.Builder chatDialogBuilder = new AlertDialog.Builder(game);

        //create input edittext and add it into dialog builder
        EditText chatET = new EditText(game);
        chatDialogBuilder.setView(chatET);

        //post text
        chatDialogBuilder.setPositiveButton("Enter", (dialog, which) -> {
            if (chatET.getText().toString().length() > 0)
                //add text to chat
                setChat(chatET.getText().toString());

            //clear chat's editext
            chatET.getText().clear();
        });

        //close dialog
        chatDialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        final AlertDialog chatDialog = chatDialogBuilder.create();

        //show input edittext
        changeLog.setOnLongClickListener(view -> {
            chatDialog.show();
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    //joystick setting and move character
    public void joyStick() {
        RelativeLayout layout_joystick = game.findViewById(R.id.layout_joystick);
        Stick = new JoyStickClass(game, layout_joystick, R.drawable.redbutton2);
        Stick.setStickSize(110, 110);
        Stick.setLayoutSize(350, 350);
        Stick.setOffset(90);
        Stick.setMinimumDistance(50);

        //move character and change side
        layout_joystick.setOnTouchListener((arg0, arg1) -> {
            Stick.drawStick(arg1);
            if (Char.getCharHP() > 0) {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                    // Get the current direction based on joystick state
                    int direction = Stick.getDirection();

                    // Update character movement only if a valid direction is detected
                    if (direction != JoyStickClass.STICK_NONE) {
                        Char.characterMovement(direction);
                        Char.setCharMoving(true);
                    }
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    // Stop character movement when the touch ends
                    Char.setCharMoving(false);
                    Char.startStandHealing();
                }

            }
            return true;
        });
    }

    //create and set npcs
    public ImageView createNPCView(NPC npc, NPCActions npcActions) {
        //create and set npc image
        ImageView npcImage = new ImageView(game);
        npcImage.setImageResource(npc.getNpcImage());
        npcImage.setX(npc.getNpcX());
        npcImage.setY(npc.getNpcY());
        gameBackground.addView(npcImage);
        npcImage.getLayoutParams().height = 400;
        npcImage.getLayoutParams().width = 150;

        //set npc action
        npcImage.setOnClickListener(npca -> {
            npcActions.hideAllNpcViews();
            npcActions.showNPCDialog(npc.toString());
            gameAreaScrolling.requestLayout();
        });

        return npcImage;
    }

    //create portals and set move to another area
    public ImageView createPortalsView(Portal p) {
        //create and set portal image
        ImageView portalImg = new ImageView(game);
        portalImg.setImageResource(R.drawable.portal);
        portalImg.setX(p.getPortalX());
        portalImg.setY(p.getPortalY());
        gameBackground.addView(portalImg);
        portalImg.getLayoutParams().height = 350;
        portalImg.getLayoutParams().width = 350;

        //onclick - check if character stand in the portal
        portalImg.setOnClickListener(por -> {
            if (portalImg.getX() - Char.getCharX() > -120 && portalImg.getX() - Char.getCharX() < 120 && portalImg.getY() - Char.getCharY() > -50 && portalImg.getY() - Char.getCharY() < 90) {

                //get the area that the portal leads to
                Areas portalArea = p.getPortalTo();

                //get portal destination
                Portal portalDestination = Portal.valueOf(p.getPortalDestination());

                //change character area
                Char.updateArea(portalArea, portalDestination.getPortalX(), portalDestination.getPortalY());

                //remove portals and drops and set the next area
                clearAreaBeforeSet(portalArea);
            }
        });

        return portalImg;
    }

    public void createDrop(int x, int y) {
        //create item and place it at mob location
        ImageView droppedItemIMG = new ImageView(game);
        droppedItem.add(droppedItemIMG);
        addRemoveFromGameView(droppedItemIMG, true);
        droppedItemIMG.setX(x);
        droppedItemIMG.setY(y);
        droppedItemIMG.setLayoutParams(new ConstraintLayout.LayoutParams(80, 80));

        //generate random drop from drops pool
        String droppedItemID = DropsPool.values()[gameUIRan.nextInt(DropsPool.values().length)].toString();

        //get dropped item image
        int itemImage = EquipmentsEnum.valueOf(droppedItemID).getEquipmentImage();

        //check if image found
        if (itemImage != 0) {
            //add item to view
            droppedItemIMG.setImageResource(itemImage);
            AtomicBoolean pick = new AtomicBoolean(true);

            //pickup the item
            droppedItemIMG.setOnClickListener(v -> {
                this.droppedItem.remove(droppedItemIMG);
                Char.pickUpItem(droppedItemID);
                addRemoveFromGameView(droppedItemIMG, false);
                pick.set(false);
            });

            //remove dropped item if not pick
            gameUIHandler.postDelayed(() -> {
                if (pick.get())
                    this.droppedItem.remove(droppedItemIMG);
                addRemoveFromGameView(droppedItemIMG, false);
            }, 30000);
        } else
            Log.e("LOG_ITEM", "item not found");
    }

    //remove all drops from the area
    private void clearDrops() {
        while (droppedItem.size() > 0) {
            addRemoveFromGameView(droppedItem.get(0), false);
            droppedItem.remove(0);
        }
    }

    //clear area from drops and set new area
    public void clearAreaBeforeSet(Areas portalArea) {
        //clear drops
        clearDrops();

        //set the next area
        game.setArea(portalArea);
    }

    //set area background and its size
    public void setAreaBG(Areas area) {
        gameBackground.setBackgroundResource(area.getAreaBG());

        areaMaxY = ScreenControl.ScreenHeight * 2;
        areaMaxX = ScreenControl.ScreenWidth * 2;

        gameBackground.getLayoutParams().height = areaMaxY;
        gameBackground.getLayoutParams().width = areaMaxX;
    }

    //show damage above the mod when attacked
    public void damageMobText(float dmg, boolean crit, Mobs m, int i) {
        TextView mobDmgText = new TextView(game);
        mobDmgText.setTextColor(Color.RED);
        String dmgText = "you did " + Math.round(dmg) + " damage";

        if (crit) {
            mobDmgText.setTextColor(Color.BLACK);
            dmgText = "Critical,you did " + Math.round(dmg) + " damage";
        }

        setChat(dmgText);

        //show the damage done to the mob above it
        mobDmgText.setX(m.getMobX() + 50);
        mobDmgText.setY((m.getMobY() - 70 + (-50 * i)));
        mobDmgText.setTextSize(20);
        mobDmgText.setText(String.valueOf(Math.round(dmg)));
        gameBackground.addView(mobDmgText);
        gameUIHandler.postDelayed(() -> gameBackground.removeView(mobDmgText), 800);
    }

    //show chat 1/3/10
    private void setChatShow() {
        chatView = ++chatView % 4;
        if (chatView == 0) {
            chatScroll.setVisibility(View.VISIBLE);
            chatScroll.getLayoutParams().height = 75;
        } else if (chatView == 1)
            chatScroll.getLayoutParams().height = 375;
        else if (chatView == 2)
            chatScroll.getLayoutParams().height = 750;
        else if (chatView == 3)
            chatScroll.setVisibility(View.INVISIBLE);
        chatScroll.requestLayout();
    }

    //add text to chat
    public void setChat(String str) {
        TextView Text;

        //check if chat have more than 50 texts, if so use the oldest one else create new text
        if (chatScrollLayout.getChildCount() >= 50) {

            //use oldest text
            View firstText = chatScrollLayout.getChildAt(0);
            chatScrollLayout.removeView(firstText);
            Text = (TextView) firstText;
        } else {

            //create new text
            Text = new TextView(game);
            Text.setTextSize(20);
            Text.setTextColor(Color.BLACK);
        }

        //add text to chat
        Text.setText(str);
        chatScrollLayout.addView(Text);
        chatScroll.fullScroll(View.FOCUS_DOWN);
    }

    //open setting page
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    public void settingPage(AccountsDB accDB) {
        //pause the game
        game.Pause = true;

        //create setting page if not created
        if (settingL == null) {
            settingL = game.findViewById(R.id.settingL);
            //BGM Switch
            Switch BGM = game.findViewById(R.id.switchbgm);
            if (setDB.getBGMState() == 0)
                BGM.setChecked(false);

            //mute and unmute BGM
            BGM.setOnCheckedChangeListener((checked, view) -> game.changeBGM(BGM.isChecked()));

            //Sounds Switch
            Switch GSound = game.findViewById(R.id.switchsound);
            if (setDB.getSoundState() == 0)
                GSound.setChecked(false);

            //mute and unmute sounds
            GSound.setOnCheckedChangeListener((checked, view) -> game.changeSound(GSound.isChecked()));

            //set admin codes buttons
            if (accDB.getAccountType(Char.getAccountID()) > 0) {
                Button adminCodeBTN = game.findViewById(R.id.adminCodeBtn);
                adminCodeBTN.setVisibility(View.VISIBLE);
                AdminCodes.setAdminData(game, Char, accDB);
                adminCodeBTN.setOnClickListener(ac -> {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(game);
                    dialog.setTitle("Admin code");
                    dialog.setMessage(R.string.testcode);
                    EditText editText = new EditText(game);
                    dialog.setView(editText);
                    dialog.setPositiveButton("OK", (dia, which) -> AdminCodes.useCode(editText.getText().toString()));
                    dialog.setNegativeButton("NO", (dia, which) -> dia.dismiss());
                    dialog.show();

                });
            }
        }

        //show setting page
        settingL.setVisibility(View.VISIBLE);
    }

    //show \ hide area effect view
    public void showHideAreaEffectView(AreaEffects skill) {
        if (skill != AreaEffects.None) {
            transBG.setVisibility(View.VISIBLE);

            if (skill == AreaEffects.Overheat)
                transBG.setBackgroundResource(R.color.tred);
            else if (skill == AreaEffects.DarknessFog)
                transBG.setBackgroundResource(R.color.tblack);
            else
                transBG.setVisibility(View.GONE);

        } else
            transBG.setVisibility(View.GONE);
    }

    public void setProjectiles(ElementAttacks attack) {
        //create and set projectile view if not created
        if (projectileImg == null) {
            projectileImg = new ImageView(game);
            gameBackground.addView(projectileImg);
            projectileImg.setLayoutParams(new ConstraintLayout.LayoutParams(400, 240));
        }

        //show and set projectile image
        projectileImg.setVisibility(View.VISIBLE);
        Glide.with(game).load(WeaponsAndElementsData.getProjectilesImage(attack)).into(projectileImg);

        //set image at character location
        projectileImg.setX(Char.getCharX());
        projectileImg.setY(Char.getCharY());
    }

    //move projectiles towards a mob and rotate to face it, return true when close enough to hit it
    public boolean moveProjectiles(Mobs mob) {
        if (mob.getMobX() > projectileImg.getX()) {
            projectileImg.setX(projectileImg.getX() + 22);
            projectileImg.setScaleX(1);
        } else {
            projectileImg.setX(projectileImg.getX() - 22);
            projectileImg.setScaleX(-1);
        }

        if (mob.getMobY() + 140 - projectileImg.getY() > 40) {
            projectileImg.setY(projectileImg.getY() + 22);
            if (projectileImg.getScaleX() == 1)
                projectileImg.setRotation(45);
            else
                projectileImg.setRotation(-45);
        } else if (mob.getMobY() + mob.getMobHeight() - 200 - projectileImg.getY() < 0) {
            projectileImg.setY(projectileImg.getY() - 22);
            if (projectileImg.getScaleX() == 1)
                projectileImg.setRotation(-45);
            else
                projectileImg.setRotation(45);
        } else
            projectileImg.setRotation(0);

        return mob.getMobX() - projectileImg.getX() < 50 && mob.getMobX() + mob.getMobHeight() - projectileImg.getX() > -50 &&
                projectileImg.getY() > mob.getMobY() + 100 && projectileImg.getY() < mob.getMobY() + mob.getMobHeight() - 50;
    }

    public void hideProjectiles() {
        projectileImg.setVisibility(View.GONE);
    }

    //add or remove view from game
    public void addRemoveFromGameView(View view, Boolean add) {
        if (add)
            gameBackground.addView(view);
        else
            gameBackground.removeView(view);
    }

    public void setViewLocation(View view, int viewX, int viewY) {
        view.setX(viewX);
        view.setY(viewY);
    }

    //focus game scrollview on character while moving
    public void focusOnChar(int charX, int charY) {
        gameAreaScrolling.scrollTo(charX - ScreenControl.ScreenWidth / 2, charY - ScreenControl.ScreenHeight / 2);
    }

    public void scrollToChar(int charX, int charY) {
        gameAreaScrolling.smoothScrollToPosition(charX - ScreenControl.ScreenWidth / 2, charY - ScreenControl.ScreenHeight / 2);
    }

    //return to game from other layouts
    public void backToGame(ViewParent layout) {

        //hide current layout
        if (((ConstraintLayout) layout).getVisibility() == View.VISIBLE) {
            ((ConstraintLayout) layout).setVisibility(View.GONE);

            //if layout if setting
        } else if (settingL != null && settingL.getVisibility() == View.VISIBLE)
            settingL.setVisibility(View.GONE);

        //move view to char after closing layout
        scrollToChar(Char.getCharX(), Char.getCharY());
    }

    public void selectAdditionalElement(List<Elements> charElements) {
        game.Pause = true;
        game.setContentView(R.layout.elements);

        //load all elements buttons and hide those of elements the character already have
        Button[] eleBtn = new Button[5];
        for (int i = 0; i < 5; i++) {

            //load the elements buttons
            String BtnID = "elementBtn" + (i + 1);
            int resID = game.getResources().getIdentifier(BtnID, "id", game.getPackageName());
            eleBtn[i] = game.findViewById(resID);

            //hide the button
            if (charElements.contains(Elements.values()[i + 1]))
                eleBtn[i].setVisibility(View.GONE);

            //set onclick to select element
            int finalI = i;
            eleBtn[i].setOnClickListener(view -> popNewElement(charElements.size() + 1, Elements.values()[finalI + 1]));
        }
    }

    private void popNewElement(int charElement, Elements element) {
        AlertDialog.Builder AddElePop = new AlertDialog.Builder(game);
        AddElePop.setTitle("Add this Element?");
        AddElePop.setIcon(element.elementIcon);
        AddElePop.setPositiveButton("Yes", (dialog, which) -> {
            //save the selected element
            Char.addNewElement(charElement, element);
            game.finish();
            game.startActivity(game.getIntent());
        });
        AddElePop.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AddElePop.show();
    }

    public int getAreaMaxX() {
        return areaMaxX;
    }

    public int getAreaMaxY() {
        return areaMaxY;
    }
}