package com.degonx.warriorsofdeagon.UI;

import static android.content.ContentValues.TAG;

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
import com.degonx.warriorsofdeagon.Enums.EffectsEnums.AreaEffects;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.ElementAttacks;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.GameData.AreasData;
import com.degonx.warriorsofdeagon.GameData.EquipmentsData;
import com.degonx.warriorsofdeagon.GameData.WeaponsAndElementsData;
import com.degonx.warriorsofdeagon.VisionAndSound.JoyStickClass;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.R;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.SettingDB;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;


@SuppressLint("SetTextI18n")
public class GameUI {

    private final LinearLayout GameMainLayout, chatScrollLayout;

    private final Handler gameUIHandler = new Handler();
    private final Random gameUIRan = new Random();
    private final List<ImageView> droppedItem = new ArrayList<>();
    private final ImageView[] PortalIMG = new ImageView[2];
    private final ImageView transBG;
    private ImageView projectileImg;
    private final ConstraintLayout GameLayout;
    private ConstraintLayout settingL;
    private final ScrollView chatScroll;
    private int chatView = 0;

    private final Game game;
    private final Character Char;
    private final SettingDB setDB;
    private JoyStickClass Stick;


    //load View for game layout
    public GameUI(Game game, Character Char, SettingDB setDB, AccountsDB accDB) {
        this.game = game;
        this.Char = Char;
        this.setDB = setDB;

        GameMainLayout = game.findViewById(R.id.GameMainLayout);
        GameLayout = game.findViewById(R.id.game);
        transBG = game.findViewById(R.id.transBG);

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

        //post text or execute admin command
        chatDialogBuilder.setPositiveButton("Enter", (dialog, which) -> {
            if (chatET.getText().toString().length() > 0)

                //check if text is admin command
                if (accDB.getAccountType(Char.getAccountID()) > 0 && chatET.getText().toString().startsWith("?"))
                    //execute admin command
                    game.executeAdminCommand(chatET.getText().toString());
                else
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

        //load portals
        PortalIMG[0] = game.findViewById(R.id.portal1);
        PortalIMG[1] = game.findViewById(R.id.portal2);
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
                    Char.characterMovement(Stick.get8Direction());
                    Char.setCharMoving(true);
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                    Char.setCharMoving(false);
                    Char.startStandHealing();
                }
            }
            return true;
        });
    }

    public void createDrop(int x, int y, int drop) {
        //create item and place it at mob location
        ImageView droppedItemIMG = new ImageView(game);
        droppedItem.add(droppedItemIMG);
        addRemoveFromGameLayout(droppedItemIMG, true);
        droppedItemIMG.setX(x);
        droppedItemIMG.setY(y);
        droppedItemIMG.setLayoutParams(new ConstraintLayout.LayoutParams(80, 80));
        int droppedItem = drop;

        //check if drop made my admin command
        if (drop == 0) {
            //generate between weapon and armor
            if (gameUIRan.nextInt(2) == 0)
                droppedItem = gameUIRan.nextInt(5) + 1;
            else
                do {
                    droppedItem = gameUIRan.nextInt(13) + 101;
                } while (droppedItem == 107);
        }

        //get dropped item image
        int itemImage = EquipmentsData.getEquipmentImage(droppedItem);

        //check if image found
        if (itemImage != 0) {
            //add item to view
            droppedItemIMG.setImageResource(itemImage);
            AtomicBoolean pick = new AtomicBoolean(true);

            //pickup the item
            int finalDroppedItem = droppedItem;
            droppedItemIMG.setOnClickListener(v -> {
                this.droppedItem.remove(droppedItemIMG);
                Char.pickUpItem(finalDroppedItem);
                addRemoveFromGameLayout(droppedItemIMG, false);
                pick.set(false);
            });

            //remove dropped item if not pick
            gameUIHandler.postDelayed(() -> {
                if (pick.get())
                    this.droppedItem.remove(droppedItemIMG);
                addRemoveFromGameLayout(droppedItemIMG, false);
            }, 30000);
        } else
            Log.d(TAG, "item not found");
    }

    //show damage above the mod when attacked
    public void damageMobText(double dmg, boolean crit, Mobs m, int i) {
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
        mobDmgText.setY((m.getMobY() - 80) - (50 * i));
        mobDmgText.setTextSize(20);
        mobDmgText.setText(String.valueOf(Math.round(dmg)));
        GameLayout.addView(mobDmgText);
        gameUIHandler.postDelayed(() -> GameLayout.removeView(mobDmgText), 800);
    }

    //create portal and set move to another area
    public void setPortals() {

        //get portals data
        int[] portalData = AreasData.getAreaPortals(Char.getCharAreaID());

        //set the portals
        for (int p = 0; p < portalData.length; p += 3) {

            //show area's portal
            PortalIMG[portalData[p]].setVisibility(View.VISIBLE);

            //onclick check if character stand in the portal
            final int finalP = p;
            PortalIMG[portalData[finalP]].setOnClickListener(por -> {
                if (PortalIMG[portalData[finalP]].getX() - Char.getCharX() > -90 && PortalIMG[portalData[finalP]].getX() - Char.getCharX() < 90 &&
                        PortalIMG[portalData[finalP]].getY() - Char.getCharY() > -30 && PortalIMG[portalData[finalP]].getY() - Char.getCharY() < 70) {

                    //hide portals before area changes
                    for (ImageView portalImage : PortalIMG)
                        portalImage.setVisibility(View.INVISIBLE);

                    //change character area
                    Char.updateArea(portalData[finalP + 1], (int) PortalIMG[portalData[finalP + 2]].getX(), (int) PortalIMG[portalData[finalP + 2]].getY());

                    //clear drops
                    clearDrops();

                    //set the next area
                    game.setArea(portalData[finalP + 1]);
                }
            });
        }
    }

    //set / change area background
    public void setAreaBG(int area) {
        GameMainLayout.setBackgroundResource(AreasData.getAreaScreen(area));
    }

    //remove all drops from the area
    private void clearDrops() {
        while (droppedItem.size() > 0) {
            addRemoveFromGameLayout(droppedItem.get(0), false);
            droppedItem.remove(0);
        }
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

        //check if chat have more then 50 texts, if so use the oldest one else create new text
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
    public void settingPage() {
        pauseGame();
        if (settingL == null) {
            settingL = game.findViewById(R.id.settingL);
            //BGM Switch
            Switch BGM = game.findViewById(R.id.switchbgm);
            if (setDB.getBGMState() == 0)
                BGM.setChecked(false);

            //mute and unmute BGM
            BGM.setOnCheckedChangeListener((checked, view) -> game.changeBGM(BGM));

            //Sounds Switch
            Switch GSound = game.findViewById(R.id.switchsound);
            if (setDB.getSoundState() == 0)
                GSound.setChecked(false);

            //mute and unmute sounds
            GSound.setOnCheckedChangeListener((checked, view) -> game.changeSound(GSound));

            //create media folder
            Button createFolderBtn = game.findViewById(R.id.createFolder);
            createFolderBtn.setOnClickListener(d -> game.createFolder());

            //play music form folder
            Button MusicPBtn = game.findViewById(R.id.playMusic);
            MusicPBtn.setOnClickListener(m -> game.playMusic());

            //play next song
            Button nextSongBtn = game.findViewById(R.id.nextbtn);
            nextSongBtn.setOnClickListener(n -> game.nextSong());
        }
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
            else if (skill == AreaEffects.Tornado)
                Glide.with(game).load(R.drawable.tornado).into(transBG);
            else
                transBG.setVisibility(View.GONE);

        } else
            transBG.setVisibility(View.GONE);
    }

    public void setProjectiles(ElementAttacks attack) {
        //create and set projectile view if not created
        if (projectileImg == null) {
            projectileImg = new ImageView(game);
            GameLayout.addView(projectileImg);
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
    public void addRemoveFromGameLayout(View view, Boolean add) {
        if (add)
            GameLayout.addView(view);
        else
            GameLayout.removeView(view);
    }

    //pause and hide game layout when opening another layout(page)
    public void pauseGame() {
        game.Pause = true;
        GameLayout.setVisibility(View.GONE);
    }

    //return to game from other layouts
    public void backToGame(ViewParent layout) {

        //hide current layout
        if (((ConstraintLayout) layout).getVisibility() == View.VISIBLE) {
            ((ConstraintLayout) layout).setVisibility(View.GONE);

            //if layout if setting
        } else if (settingL != null && settingL.getVisibility() == View.VISIBLE)
            settingL.setVisibility(View.GONE);

        //show game layout
        GameLayout.setVisibility(View.VISIBLE);
    }

    public void selectAdditionalElement(List<Elements> charElements) {
        pauseGame();
        game.setContentView(R.layout.elements);

        //load and set all element button and remove button of the element the character already have
        Button[] eleBtn = new Button[8];
        for (int i = 0; i < 8; i++) {
            String BtnID = "elementBtn" + (i + 1);
            int resID = game.getResources().getIdentifier(BtnID, "id", game.getPackageName());
            eleBtn[i] = game.findViewById(resID);
            if (charElements.contains(Elements.values()[i + 1]))
                eleBtn[i].setVisibility(View.GONE);

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
}