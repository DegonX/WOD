package com.degonx.warriorsofdeagon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Sounds;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.SettingDB;

import java.io.IOException;
import java.util.regex.Pattern;

public class MainActivity extends Activity {

    AccountsDB db = new AccountsDB(this);
    SettingDB setDB = new SettingDB(this);
    ConstraintLayout registerL;
    CheckBox Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenControl.screenControl(this);
        mainPage();
        registerL = findViewById(R.id.regL);

        //check if setting exist
        if (!setDB.checkIfSettingExist())
            setDB.createSettings();

        //start BGM
        try {
            Sounds.vol = setDB.getBGMState();
            Sounds.playBGM(this, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //load login page
    private void mainPage() {
        setContentView(R.layout.main);
        EditText ID = findViewById(R.id.textID);
        EditText PW = findViewById(R.id.textPW);

        //set username and password if saved
        Save = findViewById(R.id.savedCheck);
        if (setDB.getSaveState() == 1) {
            Save.setChecked(true);
            String savedAccount = setDB.getAccount();
            ID.setText(savedAccount, TextView.BufferType.EDITABLE);
            PW.setText(db.getAccountPWID(savedAccount), TextView.BufferType.EDITABLE);
        }

        //delete saved user from database
        Save.setOnCheckedChangeListener((c, h) -> setDB.updateSavedAccount("", 0));

        //load and set login button
        Button login = findViewById(R.id.bEnter);
        login.setGravity(Gravity.CENTER);
        login.setOnClickListener(v -> Login(ID.getText().toString(), PW.getText().toString()));
    }

    //login to the account
    private void Login(String accountUN, String accountPW) {
        //search for password by username, if return null account not exist else check if passwords matches
        String AccountPWID = db.getAccountPWID(accountUN);
        if (AccountPWID != null) {
            if (AccountPWID.equals(accountPW)) {

                //save username in setting
                if (Save.isChecked())
                    setDB.updateSavedAccount(accountUN, 1);

                //move to lobby with accountID
                Intent moveAID = new Intent(this, Lobby.class);
                startActivity(moveAID.putExtra("account", db.getAccountID(accountUN)));
                finish();
            } else
                Toasts.makeToast(this, "Password is Incorrect");
        } else
            Toasts.makeToast(this, "Username is Incorrect");
    }

    private void loadRegisterPage() {
        registerL.setVisibility(View.VISIBLE);
        EditText IDr = findViewById(R.id.textIDr);
        EditText PWr = findViewById(R.id.textPWr);
        EditText PWr2 = findViewById(R.id.textPWr2);
        Button regir = findViewById(R.id.regir);
        regir.setOnClickListener(v -> {
            if (registerAccount(IDr.getText().toString(), PWr.getText().toString(), PWr2.getText().toString())) {
                IDr.getText().clear();
                PWr.getText().clear();
                PWr2.getText().clear();
                registerL.setVisibility(View.GONE);
            }
        });
    }

    //attempt to register account
    private boolean registerAccount(String accountUN, String PW1, String PW2) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");

        //checks username(length,legit chars chars,exist)
        if ((accountUN.length() >= 4) && (accountUN.length() <= 13) && !pattern.matcher(accountUN).find()) {
            if (!db.checkIfAccountExist(accountUN)) {

                //checks password(length,legit chars,match the other 2nd password)
                if ((PW1.length() >= 5) && (PW1.length() <= 13) && !pattern.matcher(PW1).find()) {
                    if (PW1.equals(PW2)) {

                        //register the account
                        db.registerAccount(db.totalAccounts() + 1, accountUN, PW1);
                        Toasts.makeToast(this, "Account Created.");
                        return true;

                    } else
                        Toasts.makeToast(this, "Passwords not matches.");
                } else {
                    if (PW1.length() < 5)
                        Toasts.makeToast(this, "Password too short.");
                    else if (PW1.length() > 13)
                        Toasts.makeToast(this, "Password too long.");
                    else
                        Toasts.makeToast(this, "Username or Password contains special character.");
                }
            } else
                Toasts.makeToast(this, "Account already exist.");
        } else {
            if (accountUN.length() < 4)
                Toasts.makeToast(this, "Username too short.");
            else if (accountUN.length() > 13)
                Toasts.makeToast(this, "Username too long.");
            else
                Toasts.makeToast(this, "Username or Password contains special character.");
        }
        return false;
    }

    public void mainMove(View v) {
        switch (v.getId()) {

            case (R.id.bRegi):
                loadRegisterPage();
                break;

            case (R.id.Back):
                registerL.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && registerL.getVisibility() == View.VISIBLE) {
            registerL.setVisibility(View.GONE);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
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
}