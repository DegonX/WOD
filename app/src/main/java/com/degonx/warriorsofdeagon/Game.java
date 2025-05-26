package com.degonx.warriorsofdeagon;

import android.app.Activity;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.degonx.warriorsofdeagon.Actions.NPCActions;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Mob;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.NPC;
import com.degonx.warriorsofdeagon.Enums.AreaEnums.Portal;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.Objects.NPCs;
import com.degonx.warriorsofdeagon.Objects.Portals;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Sounds;
import com.degonx.warriorsofdeagon.Objects.Bosses;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.UI.CharacterUI;
import com.degonx.warriorsofdeagon.UI.GameUI;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.SettingDB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends Activity implements PopupMenu.OnMenuItemClickListener {

    private final CharactersDB charDB = new CharactersDB(this);
    private final SettingDB setDB = new SettingDB(this);
    private final AccountsDB accDB = new AccountsDB(this);

    private GameUI gameUI;
    private Character Char;
    private CharacterUI charUI;
    private NPCActions npcActions;
    private final List<Areas> createdArea = new ArrayList<>();
    private final List<Mobs> mobsList = new ArrayList<>();
    private final List<Mobs> activeMobsList = new ArrayList<>();
    private final List<Portals> activePortalList = new ArrayList<>();
    private final List<Portals> portalList = new ArrayList<>();
    private final List<NPCs> activeNPCList = new ArrayList<>();
    public final List<NPCs> NPCList = new ArrayList<>();
    private final int[] soundsArr = new int[3];
    private final SoundPool soundPool = new SoundPool.Builder().setMaxStreams(4).build();
    private float volume;
    private PopupMenu Menu;

    public boolean Pause = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenControl.screenControl(this);
        setContentView(R.layout.game);

        //receive charID from previous activity and load his data
        Intent mIntent = getIntent();
        int charID = mIntent.getIntExtra("charID", 0);
        Char = charDB.getCharacterData(charID);
        Char.setAccDB(accDB);
        Log.i("LOG_CHARACTER_ID", "CharID:" + charID);

        //load and set game at start
        Char.setMobsList(activeMobsList);
        Mobs.setGameNChar(this, Char);
        gameUI = new GameUI(this, Char, setDB);
        Char.setCharacterAtStart(this, gameUI, charDB);
        charUI = Char.getCharUI();

        //set character area at start
        setArea(Char.getCharArea());

        //set soundPool
        soundsArr[1] = soundPool.load(this, R.raw.eleatk, 3);
        soundsArr[2] = soundPool.load(this, R.raw.lordstart, 3);

        //apply setting states
        volume = setDB.getSoundState() == 1 ? 4 : 0;
    }

    public void setArea(Areas area) {
        //set area background
        gameUI.setAreaBG(area);

        //play area BGM
        try {
            Sounds.playBGM(this, Char.getCharArea());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //stop area effect
        if (Char.getAreaEffectTime() > 0)
            Char.stopAreaEffect();

        //set areas mobs, npcs and portals
        setAreaMobs(area);
        setAreaNPCs(area);
        setAreaPortals(area);

        //add area to create list
        if (!isAreaCreated(area)) {
            addCreatedArea(area);

            //reset mobs,npc and portals to fix their locations
            new Handler().postDelayed(() -> {
                setAreaMobs(area);
                setAreaNPCs(area);
                setAreaPortals(area);
            }, 60);
        }
    }

    private void setAreaMobs(Areas area) {
        if (!activeMobsList.isEmpty())
            removeMobs();

        //check if there is mobs in the area
        int mobsCount = area.getMobsCount();
        if (mobsCount > 0) {

            //check if the area already created
            if (isAreaCreated(area)) {

                //activate current area mobs
                for (Mobs m : mobsList)
                    if (m.getMobArea() == area) {
                        gameUI.addRemoveFromGameView(m.getMobViewFromUI(), true);
                        m.setMobActivation(true);

                        //add mob to active mob list
                        activeMobsList.add(m);
                    }
            } else
                //create mobs
                createMobs(mobsCount, area);
        }
    }

    //create mobs / bosses by area
    private void createMobs(int mobsCount, Areas area) {
        //find which mobs are in the area
        Mob areaMob = null;
        for (Mob m : Mob.values())
            if (m.toString().contains(area.toString())) {
                areaMob = m;
                break;
            }

        //create the mobs and add them to mobs list
        for (int cm = 0; cm < mobsCount; cm++) {
            Mobs newMob;

            //create the mob by type(normal , boss)
            if (areaMob != null && areaMob.getMobType().equals("normal"))
                newMob = new Mobs(areaMob, 2000, 100 + (350 * cm), area, gameUI);
            else
                newMob = new Bosses(areaMob, 1700, 500, area, gameUI);

            //add mobs to lists
            mobsList.add(newMob);
            activeMobsList.add(newMob);
        }
    }

    private void removeMobs() {
        //remove all the mobs of the previous area
        for (Mobs rm : activeMobsList) {
            //remove mobs views
            gameUI.addRemoveFromGameView(rm.getMobViewFromUI(), false);
            //deactivate mobs
            rm.setMobActivation(false);
        }
        activeMobsList.clear();
    }

    private void setAreaNPCs(Areas area) {
        if (!activeNPCList.isEmpty())
            removeNPCs();

        //check if there is npc in the area
        int npcCount = area.getNpcCount();
        if (npcCount > 0) {

            //check if the area already created
            if (isAreaCreated(area)) {
                for (NPCs npc : NPCList)

                    //show npc and set its location
                    if (area == npc.getNpcArea()) {
                        gameUI.addRemoveFromGameView(npc.getNpcImage(), true);
                        gameUI.setViewLocation(npc.getNpcImage(), npc.getNpcX(), npc.getNpcY());

                        //add npc to active npc list
                        activeNPCList.add(npc);
                    }
            } else
                //create npc
                createNPCs(area);
        }
    }

    private void createNPCs(Areas area) {
        //create npcActions class
        if (npcActions == null)
            npcActions = new NPCActions(this, accDB, Char);

        //create and set the npcs
        for (NPC npc : NPC.values()) {
            //check if npcs belong to the area
            if (npc.getNpcArea().equals(area)) {

                //create npc image and set its function
                ImageView npcImage = gameUI.createNPCView(npc, npcActions);

                //create npc object and add to lists
                NPCs NPC = new NPCs(npc, npcImage);
                NPCList.add(NPC);
                activeNPCList.add(NPC);
            }
        }
    }

    private void removeNPCs() {
        //remove all the npc of the previous area
        for (NPCs npc : activeNPCList)
            gameUI.addRemoveFromGameView(npc.getNpcImage(), false);

        //clear npc list
        activeNPCList.clear();
    }

    private void setAreaPortals(Areas area) {
        if (!activePortalList.isEmpty())
            removePortals();

        //check if there is portals in the area
        int portalsCount = area.getPortalCount();
        if (portalsCount > 0) {

            //check if the area already created
            if (isAreaCreated(area)) {
                //show current area portals
                for (Portals p : portalList)

                    //show portal and set its location
                    if (area == p.getPortalArea()) {
                        gameUI.addRemoveFromGameView(p.getPortalImage(), true);
                        gameUI.setViewLocation(p.getPortalImage(), p.getPortalX(), p.getPortalY());

                        //add mob to active portal list
                        activePortalList.add(p);
                    }
            } else
                //create portals
                createPortals(area);
        }
    }

    private void createPortals(Areas area) {
        //create and set the portals
        for (Portal p : Portal.values()) {
            //check if portal belong to the area
            if (p.getPortalArea().equals(area)) {

                //create portal image and set its function
                ImageView portalImage = gameUI.createPortalsView(p);

                //create portal object and add to lists
                Portals portal = new Portals(p, portalImage);
                portalList.add(portal);
                activePortalList.add(portal);
            }
        }
    }

    private void removePortals() {
        //remove all the portals of the previous area
        for (Portals rp : activePortalList)
            gameUI.addRemoveFromGameView(rp.getPortalImage(), false);

        //clear portals list
        activePortalList.clear();
    }

    public void addCreatedArea(Areas area) {
        createdArea.add(area);
    }

    public boolean isAreaCreated(Areas area) {
        return createdArea.contains(area);
    }

    //onClick from buttons
    public void gameMove(View v) {
        switch (v.getId()) {

            case (R.id.skillOpen):
                charUI.showSkillView();
                break;

            case (R.id.GBack):
                Pause = false;
                gameUI.backToGame(v.getParent());
                resumeWhenBackToGame();
                break;
        }
    }

    public void showMenu(View v) {
        //create menu if null and load menu xml into it
        if (Menu == null) {
            Menu = new PopupMenu(this, v);
            Menu.setOnMenuItemClickListener(this);
            Menu.inflate(R.menu.menu);
        }

        //show menu
        Menu.show();
    }

    //selected item in menu
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {

            case (R.id.stats):
                Char.showPage(1);
                break;

            case (R.id.inventory):
                Char.showPage(2);
                break;

            case (R.id.skills):
                Char.showPage(3);
                break;

            case (R.id.bless):
                Char.showPage(4);
                break;

            case (R.id.setting):
                gameUI.settingPage(accDB);
                break;

            case (R.id.BackToLobby):
                backToLobby();
                break;
        }
        return false;
    }

    //resume paused actions
    private void resumeWhenBackToGame() {

        //resume character and related runnables
        Char.resumeWhenUnpause();

        //resume mobs movement and elemental effects if has
        for (Mobs m : activeMobsList)
            m.setMobActivation(true);
    }

    //mute/unmute BGM
    public void changeBGM(Boolean BGM) {
        setDB.updateSetting(1, BGM);
        if (BGM) {
            Sounds.muteBGM(1.0f);
            Sounds.mediaPlayer.start();
        } else
            Sounds.muteBGM(0);
    }

    //mute/unmute sound effect
    public void changeSound(boolean GSound) {
        setDB.updateSetting(2, GSound);
        volume = GSound ? 4 : 0;
    }

    public void backToLobby() {
        Pause = true;
        Intent move = new Intent(this, Lobby.class);
        startActivity(move.putExtra("account", Char.getAccountID()));
        finish();
    }

    public void playSound(int sind) {
        soundPool.play(soundsArr[sind], volume, volume, 1, 0, 1);
    }

    //set weapon sound by weapon type
    public void setWeaponSound(Weapons weapon) {
        int soundInt;

        if (weapon == Weapons.Fists)
            soundInt = R.raw.fist;
        else
            soundInt = R.raw.atk;

        soundsArr[0] = soundPool.load(this, soundInt, 3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Sounds.mediaPlayer.pause();
        if (Sounds.mediaPlayer2 != null)
            Sounds.mediaPlayer2.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sounds.mediaPlayer.start();
        if (Sounds.mediaPlayer2 != null)
            Sounds.mediaPlayer2.start();
    }

    @Override
    public void onBackPressed() {
    }
}