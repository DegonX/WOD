package com.degonx.warriorsofdeagon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Switch;

import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Weapons;
import com.degonx.warriorsofdeagon.GameData.AreasData;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.GameData.MobsData;
import com.degonx.warriorsofdeagon.VisionAndSound.Sounds;
import com.degonx.warriorsofdeagon.Objects.Bosses;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Mobs;
import com.degonx.warriorsofdeagon.UI.CharacterUI;
import com.degonx.warriorsofdeagon.UI.GameUI;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.SettingDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;
import static android.os.Build.VERSION.SDK_INT;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Game extends Activity implements PopupMenu.OnMenuItemClickListener {

    private final CharactersDB charDB = new CharactersDB(this);
    private final SettingDB setDB = new SettingDB(this);
    private final AccountsDB accDB = new AccountsDB(this);

    private GameUI gameUI;
    private Character Char;
    private CharacterUI charUI;
    private final List<Mobs> mobsList = new ArrayList<>();
    private final List<Mobs> activeMobsList = new ArrayList<>();
    private final int[] soundsArr = new int[3];
    private final SoundPool soundPool = new SoundPool.Builder().setMaxStreams(4).build();
    private float volume = 0;
    private PopupMenu Menu;
    final int COMBAT_BACKGROUND_MUSIC = 2;

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
        Log.d(TAG, "CharID:" + charID);

        Char.setMobsList(activeMobsList);
        Mobs.setGameNChar(this, Char);

        //load and set game at start
        gameUI = new GameUI(this, Char, setDB, accDB);
        Char.setCharacterAtStart(this, gameUI, charDB);

        charUI = Char.getCharUI();
        setArea(Char.getCharAreaID());

        //set soundPool
        soundsArr[1] = soundPool.load(this, R.raw.eleatk, 3);
        soundsArr[2] = soundPool.load(this, R.raw.lordstart, 3);

        //apply setting states
        if (setDB.getSoundState() == 1)
            volume = 4;

        //play BGM
        try {
            Sounds.playBGM(this, COMBAT_BACKGROUND_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "loading and setting processes completed");
    }

    public void setArea(int area) {
        //set area background
        gameUI.setAreaBG(area);
        Log.d(TAG, "AreaID:" + area);

        //stop area effect
        if (Char.getAreaEffectTime() > 0)
            Char.stopAreaEffect();

        //remove all the mobs of the previous area
        for (Mobs rm : activeMobsList) {
            //remove mobs views
            gameUI.addRemoveFromGameLayout(rm.getMobViewFromUI(), false);
            //deactivate mobs
            rm.setMobActivation(false);
        }
        activeMobsList.clear();

        //get the amount of mobs in the current area
        int mobsCount = AreasData.getAreaMobsCount(area);

        //check if mobs already exist, if not create them
        for (Mobs m : mobsList)
            if (m.getMobAreaID() == area) {
                activeMobsList.add(m);
                gameUI.addRemoveFromGameLayout(m.getMobViewFromUI(), true);
                m.setMobActivation(true);

                mobsCount--;
            }
        if (mobsCount > 0)
            createMobs(mobsCount, area);

        //set area portals
        gameUI.setPortals();
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
                gameUI.settingPage();
                break;

            case (R.id.shop):
                moveToAnotherActivity(1);
                break;

            case (R.id.storage):
                moveToAnotherActivity(2);
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

    //create mobs / bosses by area
    private void createMobs(int mobsCount, int area) {
        int[] mobsData = MobsData.getDataMobs(area);
        for (int cm = 0; cm < mobsCount; cm++) {
            Mobs mob;
            if (area != 10)
                mob = new Mobs(mobsList.size(), mobsData[1], mobsData[2], 1500, 100 + (150 * cm), mobsData[3], mobsData[4], mobsData[5], area, gameUI);
            else
                mob = new Bosses(mobsList.size(), mobsData[1], mobsData[2], 1100, (150 * cm), mobsData[3], mobsData[4], mobsData[5], area, gameUI);

            //add mobs to lists
            mobsList.add(mob);
            activeMobsList.add(mob);
        }
    }

    //admin commands
    public void executeAdminCommand(String cmd) {
        try {
            if (cmd.contains("drop")) {
                String[] cmdSplit = cmd.split(" ");
                gameUI.createDrop(Char.getCharX(), Char.getCharY(), Integer.parseInt(cmdSplit[1]));
            } else
                Log.d(TAG, "command not found");
        } catch (Exception e) {
            Log.d(TAG, "ERROR:" + e.getMessage());
        }
    }

    //mute/unmute BGM
    public void changeBGM(Switch BGM) {
        setDB.updateSetting(1, BGM.isChecked());
        if (BGM.isChecked()) {
            Sounds.muteBGM(1.0f);
            Sounds.mediaPlayer.start();
        } else
            Sounds.muteBGM(0);
    }

    //mute/unmute sound effect
    public void changeSound(Switch GSound) {
        setDB.updateSetting(2, GSound.isChecked());
        if (GSound.isChecked())
            volume = 4;
        else
            volume = 0;
    }

    public void createFolder() {
        //check if permit to create folder
        if (checkPermission())
            askPermission();
        else {
            //check if folder exist, if not create it
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/WOD Media");
                if (!dir.exists()) {
                    if (dir.mkdirs())
                        Toasts.makeToast(this, "folder created");
                } else
                    Toasts.makeToast(this, "folder already exist");
            } catch (Exception E) {
                Log.d(TAG, E.getMessage());
            }
        }
    }

    public void playMusic() {
        //check if permit to create folder
        if (checkPermission())
            askPermission();
        else {
            try {
                if (Sounds.mediaPlayer2 == null)
                    //set mediaPlayer2 and songs list
                    Sounds.playMusic(this);
                else
                    //play and pause music
                    Sounds.playPauseMusic();
            } catch (Exception E) {
                Log.d(TAG, E.getMessage());
            }
        }
    }

    //play next song in the music folder
    public void nextSong() {
        try {
            if (Sounds.mediaPlayer2 != null)
                Sounds.nextSong(this);
            else
                Toasts.makeToast(this, "you need to start the music first");
        } catch (Exception E) {
            Log.d(TAG, E.getMessage());
        }
    }

    private void moveToAnotherActivity(int mov) {
        Pause = true;
        Intent move;
        if (mov == 1)
            move = new Intent(this, Shop.class);
        else
            move = new Intent(this, Storage.class);
        startActivity(move.putExtra("charID", Char.getCharID()));
        finish();
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
        else if (weapon == Weapons.Bow)
            soundInt = R.raw.arrow;
        else if (weapon == Weapons.Staff)
            soundInt = R.raw.magic;
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

    /*--------------------Permissions--------------------*/

    //permission dialog
    private void askPermission() {
        AlertDialog.Builder perm = new AlertDialog.Builder(this);
        perm.setTitle("Request Permission.");
        perm.setMessage("The game need your permission to read from and write to your device.");
        perm.setPositiveButton("Yes", (dialog, which) -> requestPermission());
        perm.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        perm.show();
    }

    //check permission to read and write granted
    private boolean checkPermission() {
        //android 11+
        if (SDK_INT >= Build.VERSION_CODES.R)
            return !Environment.isExternalStorageManager();
        else {
            //android under 11
            int perm1 = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);
            int perm2 = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
            return perm1 != PackageManager.PERMISSION_GRANTED && perm2 != PackageManager.PERMISSION_GRANTED;
        }
    }

    //ask for permission
    private void requestPermission() {
        //android 11+
        if (SDK_INT >= Build.VERSION_CODES.R)
            try {
                Intent request = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                request.addCategory("android.intent.category.DEFAULT");
                request.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(request, 6);
            } catch (Exception e) {
                Intent request = new Intent();
                request.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(request, 6);
            }
        else
            //android under 11
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, 69);
    }
}

//TODO
//body with attached objects(use inflate view for char body and weapon , add to weapons enum hold type)
//add effects to elements attacks