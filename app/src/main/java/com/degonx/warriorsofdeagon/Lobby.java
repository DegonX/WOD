package com.degonx.warriorsofdeagon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.Actions.SkillsAdder;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.VisionAndSound.Sounds;
import com.degonx.warriorsofdeagon.Database.BlessesDB;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.EquipmentsDB;
import com.degonx.warriorsofdeagon.Database.SkillsDB;
import com.degonx.warriorsofdeagon.Objects.Characters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import androidx.constraintlayout.widget.ConstraintLayout;

@SuppressLint({"SetTextI18n", "DiscouragedApi"})

public class Lobby extends Activity {

    CharactersDB charDB = new CharactersDB(this);
    List<Characters> accountCharacters;
    int accountID, backStoryTextCount = 0;
    List<Elements> newCharElements = new ArrayList<>();
    boolean deleteMode = false;
    String newCharName;
    LinearLayout backStoryLay;
    TextView backStoryTV;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenControl.screenControl(this);

        //receive accountID from previous activity
        Intent mIntent = getIntent();
        accountID = mIntent.getIntExtra("account", 0);

        //load characters from database
        accountCharacters = charDB.getCharsForLobby(accountID);
        Log.i("LOG_ACCOUNT_ID", "accountID:" + accountID);

        lobbyPage();

        try {
            Sounds.playBGM(this, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //load lobby page
    private void lobbyPage() {
        setContentView(R.layout.lobby);
        //check if there is characters in the account and load them
        if (!accountCharacters.isEmpty())
            loadCharacters();
    }

    //load characters in lobby
    private void loadCharacters() {
        LinearLayout charactersView = findViewById(R.id.charactersView);

        //create arrays for characters views
        ImageView[] chars = new ImageView[accountCharacters.size()];
        TextView[] names = new TextView[accountCharacters.size()];

        //set height, width and margins params for character linearlayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(ScreenControl.ScreenWidth / 50, 70, 0, 1);

        for (int i = 0; i < accountCharacters.size(); i++) {
            //create new character linearlayout and add it to view
            LinearLayout characterInfo = new LinearLayout(this);
            charactersView.addView(characterInfo);

            //cast params to character linearlayout and set gravity
            characterInfo.setLayoutParams(params);
            characterInfo.setOrientation(LinearLayout.VERTICAL);
            characterInfo.setGravity(Gravity.CENTER | Gravity.TOP);

            //create image and text for the character
            names[i] = new TextView(this);
            chars[i] = new ImageView(this);
            characterInfo.addView(chars[i]);
            characterInfo.addView(names[i]);

            //set image for the character
            chars[i].setImageResource(R.drawable.character);
            chars[i].getLayoutParams().width = ScreenControl.ScreenWidth / 6;
            chars[i].getLayoutParams().height = (int) (ScreenControl.ScreenHeight * 0.5);

            //set text for the character
            names[i].setText(accountCharacters.get(i).charName + " / lv." + accountCharacters.get(i).charLevel);
            names[i].setTextSize(20);
            names[i].setTextColor(Color.LTGRAY);
            names[i].setBackgroundColor(this.getColor(R.color.tblack));
            names[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            //start play or delete character on click
            int finalI = i;
            chars[i].setOnClickListener(view -> {
                if (!deleteMode)
                    startGame(accountCharacters.get(finalI).charID);
                else
                    deleteCharPopup(accountCharacters.get(finalI).charID, finalI);
            });
        }
    }

    private void startGame(int charID) {
        Intent moveGame = new Intent(Lobby.this, Game.class);
        startActivity(moveGame.putExtra("charID", charID));
        finish();
    }

    //create new character
    private void startNewCharacter() {
        ConstraintLayout charCreLay = findViewById(R.id.charCreateLayout);
        ConstraintLayout eleSelLay = findViewById(R.id.eleSelectLayout);
        backStoryLay = findViewById(R.id.backStoryLayout);
        backStoryLay.setVisibility(View.VISIBLE);
        backStoryTV = findViewById(R.id.backStoryTextView);

        backStoryTextCount = 1;

        //change back story text
        backStoryLay.setOnClickListener(bs -> {
            backStoryTextCount++;
            if (backStoryTextCount < 8)
                //back story 1st part
                backStoryTV.setText(getResources().getIdentifier("backstory" + backStoryTextCount, "string", getPackageName()));

            else if (backStoryTextCount == 8) {
                //show character name screen
                charCreLay.setVisibility(View.VISIBLE);
                EditText Ccharname = findViewById(R.id.ccname);
                Button charCreate = findViewById(R.id.ccreate);
                charCreate.setOnClickListener(view -> {

                    //check name
                    if (checkIfNameCanBeUsed(Ccharname.getText().toString())) {

                        //hide keyboard
                        View KB = this.getCurrentFocus();
                        if (KB != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(KB.getWindowToken(), 0);
                        }

                        //hide character name screen
                        charCreLay.setVisibility(View.GONE);
                        backStoryTV.setText(getResources().getIdentifier("backstory" + 8, "string", getPackageName()));
                    }
                });

            } else if (backStoryTextCount < 11)
                //back story 2nd part
                backStoryTV.setText(getResources().getIdentifier("backstory" + backStoryTextCount, "string", getPackageName()));

            else if (backStoryTextCount == 11) {
                //show select elements screen
                eleSelLay.setVisibility(View.VISIBLE);
                loadSelectElements();
                Button start = findViewById(R.id.SelStart);
                start.setOnClickListener(view -> {

                    //check if 2 elements selected
                    if (newCharElements.size() == 2) {

                        //hide  select elements screen
                        eleSelLay.setVisibility(View.GONE);
                        backStoryTV.setText(getResources().getIdentifier("backstory" + 11, "string", getPackageName()));
                    } else
                        Toasts.makeToast(this, "select 2 elements first.");
                });

            } else if (backStoryTextCount < 14)
                //back story 3rd part
                backStoryTV.setText(getResources().getIdentifier("backstory" + backStoryTextCount, "string", getPackageName()));

            else {
                //disable backstory to prevent multi character creation
                backStoryLay.setEnabled(false);

                //register character
                int charID = charDB.totalCharacters() + 1;
                charDB.createCharacter(charID, accountID, newCharName, newCharElements.get(0).elementNum, newCharElements.get(1).elementNum);

                //add skills by elements
                SkillsAdder.newCharSkills(charID, new SkillsDB(this), newCharElements);

                //start game
                startGame(charID);
            }
        });
    }


    //check if name is usable
    private boolean checkIfNameCanBeUsed(String charName) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        if (charName.length() >= 4 && charName.length() <= 14 && !pattern.matcher(charName).find())
            if (!charDB.checkIfNameExist(charName)) {
                newCharName = charName;
                return true;
            } else
                Toasts.makeToast(this, "name is already taken.");
        else if (charName.length() <= 3)
            Toasts.makeToast(this, "Name is too short.");
        else if (charName.length() >= 15)
            Toasts.makeToast(this, "Name is too long.");
        else
            Toasts.makeToast(this, "name contains special character.");
        return false;
    }

    //load select element
    private void loadSelectElements() {
        Button bSele1 = findViewById(R.id.ele1);
        Button bSele2 = findViewById(R.id.ele2);
        TextView elet1 = findViewById(R.id.elet1);
        TextView elet2 = findViewById(R.id.elet2);
        Button[] ElementsBtns = new Button[5];


        //set elements for character to choose
        for (int i = 0; i < 5; i++) {
            String BtnID = "elementBtn" + (i + 1);
            int resID = getResources().getIdentifier(BtnID, "id", getPackageName());
            ElementsBtns[i] = findViewById(resID);
//            int finalI = i + 1;
            Elements newElement = Elements.values()[i + 1];
            ElementsBtns[i].setOnClickListener(view -> {
                if (newCharElements.size() < 2) {
                    if (newCharElements.isEmpty()) {
                        newCharElements.add(0, newElement);
                        bSele1.setBackgroundResource(newElement.elementIcon);
                        elet1.setText(newElement.toString());
                    } else {
                        newCharElements.add(newElement);
                        bSele2.setBackgroundResource(newElement.elementIcon);
                        elet2.setText(newElement.toString());
                    }
                    ElementsBtns[newElement.elementNum - 1].setVisibility(View.INVISIBLE);
                } else
                    Toasts.makeToast(this, "cannot select more than 2 elements");
            });
        }

        //deselect selected elements
        bSele1.setOnClickListener(view -> {
            if (!elet1.getText().toString().equals("")) {
                ElementsBtns[Elements.valueOf(elet1.getText().toString()).elementNum - 1].setVisibility(View.VISIBLE);
                newCharElements.remove(Elements.valueOf(elet1.getText().toString()));
                bSele1.setBackgroundResource(R.color.trans);
                elet1.setText("");
            }
        });

        bSele2.setOnClickListener(view -> {
            if (!elet2.getText().toString().equals("")) {
                ElementsBtns[Elements.valueOf(elet2.getText().toString()).elementNum - 1].setVisibility(View.VISIBLE);
                newCharElements.remove(Elements.valueOf(elet2.getText().toString()));
                bSele2.setBackgroundResource(R.color.trans);
                elet2.setText("");
            }
        });
    }

    public void lobbyClick(View v) {
        switch (v.getId()) {
            case (R.id.startNewCharacter):
                if (accountCharacters.size() < 5)
                    startNewCharacter();
                else
                    Toasts.makeToast(this, "no available slots to create character");
                break;

            case (R.id.delete):
                deleteCharMode();
                break;

            case (R.id.backMain):
                Intent move = new Intent(Lobby.this, MainActivity.class);
                startActivity(move);
                finish();
                break;
        }
    }

    private void deleteCharMode() {
        if (accountCharacters.size() > 0) {
            TextView chooseTo = findViewById(R.id.chooseTo);
            if (!deleteMode) {
                chooseTo.setText("Who You Want To Delete?");
                chooseTo.setTextColor(Color.RED);
            } else {
                chooseTo.setText("Select a Character");
                chooseTo.setTextColor(Color.BLACK);
            }
            deleteMode = !deleteMode;
        } else
            Toasts.makeToast(this, "you don't have any character");
    }

    //delete character popup
    private void deleteCharPopup(final int CharID, final int spot) {
        AlertDialog.Builder deleteChar = new AlertDialog.Builder(Lobby.this);
        deleteChar.setTitle("Delete Character?");
        deleteChar.setMessage("characters cannot be restored after delete");
        deleteChar.setPositiveButton("Yes", (dialog, which) -> deleteChar(CharID, spot));
        deleteChar.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        deleteChar.show();
    }

    //delete character
    private void deleteChar(int CharID, int spot) {
        //delete character's data
        new EquipmentsDB(this).deleteAllEquipment(CharID);
        new SkillsDB(this).deleteAllSkill(CharID);
        new BlessesDB(this).deleteCharBlesses(CharID);

        //delete character
        charDB.deleteCharacter(CharID);
        accountCharacters.remove(spot);
        Toasts.makeToast(this, "Character Deleted!");

        //rest lobby
        lobbyPage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Sounds.mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sounds.mediaPlayer.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //close delete move
            if (deleteMode)
                deleteCharMode();

                //back to lobby from character creation and reset backstory
            else if (backStoryTextCount > 0) {
                backStoryLay.setVisibility(View.GONE);
                backStoryTextCount = 0;
                backStoryTV.setText(getResources().getIdentifier("backstory" + 1, "string", getPackageName()));
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
    }
}