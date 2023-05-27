package com.degonx.warriorsofdeagon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.Adapters.InventoryAdapter;
import com.degonx.warriorsofdeagon.GameData.EquipmentsData;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.VisionAndSound.Sounds;
import com.degonx.warriorsofdeagon.Objects.Equipments;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.EquipmentsDB;
import com.degonx.warriorsofdeagon.Database.SkillsDB;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class Shop extends Activity {

    private final AccountsDB db = new AccountsDB(this);
    private final CharactersDB charDB = new CharactersDB(this);
    private final EquipmentsDB equipDB = new EquipmentsDB(this);
    private final SkillsDB skillDB = new SkillsDB(this);
    private final Random Ran = new Random();
    private int accountID, CharID, Ores = 0, sellCount = 0;
    private TextView oresText, equipmentName, equipmentStats, inventoryCount;
    private List<Equipments> charEquipments;
    private Equipments equipmentHolder;
    private View equipmentView;
    private EditText editText;
    final int HP_POTION = 1;
    final int MP_POTION = 2;
    final int BREAK_BACKGROUND_MUSIC = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenControl.screenControl(this);
        setContentView(R.layout.shop);

        //receive CharID from previous activity and get it's AccountID
        Intent mIntent = getIntent();
        CharID = mIntent.getIntExtra("charID", 0);
        accountID = charDB.getAccountID(CharID);

        //get account ores and set ores text
        Ores = db.getOres(accountID);
        oresText = findViewById(R.id.shopOres);
        oresText.setText(String.valueOf(Ores));

        loadEquipmentsGrid();

        //show testing code button if admin
        if (db.getAccountType(accountID) > 0) {
            Button Code = findViewById(R.id.Code);
            Code.setVisibility(View.VISIBLE);
        }
        try {
            Sounds.playBGM(this, BREAK_BACKGROUND_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //update ores
    private void updateOres(int ore) {
        Ores += ore;
        db.updateOres(accountID, Ores);
        oresText.setText(String.valueOf(Ores));
    }

    //set equipment gridview
    private void loadEquipmentsGrid() {
        //get character equipments from db
        charEquipments = equipDB.getCharEquipments(CharID);
        if (charEquipments.size() > 0) {
            equipmentName = findViewById(R.id.equipName);
            equipmentStats = findViewById(R.id.invSstats);
            inventoryCount = findViewById(R.id.inveCount);

            //set equips in gridview
            inventoryCount.setText("Inventory 80/" + charEquipments.size());
            GridView itemGrid = findViewById(R.id.equipGrid);
            itemGrid.setAdapter(new InventoryAdapter(this, charEquipments, 1));
            itemGrid.setOnItemClickListener((parent, view, position, id) -> {
                equipmentHolder = charEquipments.get(position);
                setEquipmentData();
            });

            //sell item popup on long press
            itemGrid.setOnItemLongClickListener((parent, view, position, id) -> {

                //check if item is not equipped
                if (charEquipments.get(position).equipSpot == 0) {
                    equipmentHolder = charEquipments.get(position);
                    equipmentView = view;
                    setDialog("Sell the item?", "you will receive " + (equipmentHolder.equipUPT + 1) * 10 + " Ores for it",
                            EquipmentsData.getEquipmentImage(equipmentHolder.equipID), 2, position, 0);
                } else
                    Toasts.makeToast(this, "item is equipped,you most remove it to sell it");
                return true;
            });

            //upgrade item
            Button equipUP = findViewById(R.id.upgradeEquip);
            equipUP.setOnClickListener(v -> {
                if (equipmentHolder != null) {

                    //check if item has less then 50 upgrades
                    if (equipmentHolder.equipUPT < 50)

                        //check if item is weapon or armor
                        if (equipmentHolder.equipID < 100)
                            //armor
                            setDialog("Upgrade This Armor",
                                    "Upgrade it to " + (equipmentHolder.equipUPT + 1) + " will cost " + (equipmentHolder.equipUPT + 1) * 70 + " " + getString(R.string.equipup),
                                    EquipmentsData.getEquipmentImage(equipmentHolder.equipID), 1, 0, 0);
                        else
                            //weapon
                            setDialog("Upgrade This Weapon",
                                    "Upgrade it to " + (equipmentHolder.equipUPT + 1) + " will cost " + ((int) Math.pow((equipmentHolder.equipUPT + 1), 2)) * 100 + " " + getString(R.string.equipup),
                                    EquipmentsData.getEquipmentImage(equipmentHolder.equipID), 1, 0, 0);
                    else
                        Toasts.makeToast(this, "item level maxed");
                } else
                    Toasts.makeToast(this, "select item to upgrade");
            });
        }
    }

    //show stats of the selected item
    private void setEquipmentData() {
        //check if item was upgraded and set it's name
        if (equipmentHolder.equipUPT > 0)
            equipmentName.setText(EquipmentsData.getEquipmentName(equipmentHolder.equipID) + "(" + equipmentHolder.equipUPT + ")");
        else
            equipmentName.setText(EquipmentsData.getEquipmentName(equipmentHolder.equipID));
        String stats = "";

        //add item stats names and values to text
        if (equipmentHolder.equipDMG > 0)
            stats += "Attack:" + equipmentHolder.equipDMG + "\n";
        if (equipmentHolder.equipDEF > 0)
            stats += "Defence:" + equipmentHolder.equipDEF + "\n";
        if (equipmentHolder.equipHP > 0)
            stats += "HP:" + equipmentHolder.equipHP + "\n";
        if (equipmentHolder.equipMP > 0)
            stats += "MP:" + equipmentHolder.equipMP;
        equipmentStats.setText(stats);
    }

    //set and show dialog
    private void setDialog(String Title, String Message, int icon, int way, int value, int price) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Shop.this);
        dialog.setTitle(Title);
        dialog.setIcon(icon);
        dialog.setMessage(Message);
        if (way > 2) {
            editText = new EditText(this);
            dialog.setView(editText);
        }
        dialog.setPositiveButton("YES", (dia, which) -> {
            if (way == 1)
                upgradeEquipment();
            else if (way == 2)
                sellEquipment(value);
            else if (way == 3)
                try {
                    buyPotions(value, Integer.parseInt(editText.getText().toString()), price);
                } catch (Exception e) {
                    Toasts.makeToast(this, "numbers only");
                }
            else if (way == 4)
                useCode(editText.getText().toString());
        });
        dialog.setNegativeButton("NO", (dia, which) -> dia.dismiss());
        dialog.show();
    }

    //upgrade equipment
    private void upgradeEquipment() {
        //check if item is weapon or armor and if theres enough ores to upgrade
        if (equipmentHolder.equipID < 100 && Ores >= (equipmentHolder.equipUPT + 1) * 70 || equipmentHolder.equipID > 100 && Ores >= (Math.pow(equipmentHolder.equipUPT + 1, 2)) * 100) {

            //remove upgrade cost
            int cost;
            if (equipmentHolder.equipID > 100)
                cost = (int) ((Math.pow((equipmentHolder.equipUPT + 1), 2)) * 100);
            else
                cost = (equipmentHolder.equipUPT + 1) * 70;
            updateOres(-cost);

            //generates random stats for the item by type
            int type = equipmentHolder.equipType;

            if (equipmentHolder.equipID < 100) {
                //armor
                equipmentHolder.equipDEF += Ran.nextInt(30 - 25) + 25;
                if (type == 1 || type == 4)
                    equipmentHolder.equipMP += Ran.nextInt(100 - 90) + 90;
                else if (type == 2 || type == 3)
                    equipmentHolder.equipHP += Ran.nextInt(100 - 90) + 90;

                if (type == 4)
                    equipmentHolder.equipDMG += Ran.nextInt(20 - 10) + 10;
            } else
                //weapon
                equipmentHolder.equipDMG += equipmentHolder.equipDMG * 0.1 + Ran.nextInt(11);

            //update changes in database
            equipDB.updateEquipment(equipmentHolder.equipmentPK, ++equipmentHolder.equipUPT, equipmentHolder.equipHP, equipmentHolder.equipMP, equipmentHolder.equipDMG, equipmentHolder.equipDEF);
            setEquipmentData();
        } else
            Toasts.makeToast(this, "you don't have enough Ores to Upgrade");
    }

    //sell equip
    private void sellEquipment(int position) {
        equipmentView.setVisibility(View.GONE);
        updateOres((charEquipments.get(position).equipUPT + 1) * 10);
        equipDB.deleteEquip(charEquipments.get(position).equipmentPK);
        inventoryCount.setText("Inventory 80/" + (charEquipments.size() - ++sellCount));
        equipmentName.setText("");
        equipmentStats.setText("");
        equipmentHolder = null;
    }

    //buy the potions and reduce ores
    private void buyPotions(int pot, int amount, int price) {
        if (amount > 0) {
            if (Ores >= amount * price) {
                updateOres(-price * amount);
                charDB.updatePotions(CharID, pot, getPotionType(pot) + amount);
            } else
                Toasts.makeToast(this, "you don't have enough Ores to buy");
        } else
            Toasts.makeToast(this, "enter number bigger the 0");
    }

    //return the amount of potions the character have by type
    private int getPotionType(int pot) {
        if (pot == HP_POTION)
            return charDB.getCharHPpot(CharID);
        else if (pot == MP_POTION)
            return charDB.getCharMPpot(CharID);
        return 0;
    }

    //testing codes
    private void useCode(String code) {
        switch (code) {
            case "leveluptest":
                charDB.updateCharacterLevelUp(CharID, 50, 250);
                break;

            case "leveluptest2":
                charDB.updateCharacterLevelUp(CharID, 70, 350);
                break;

            case "leveluptest3":
                charDB.updateCharacterLevelUp(CharID, 100, 500);
                break;

            case "addores":
                updateOres(500000);
                break;

            case "addxp":
                charDB.updateEXP(CharID, 50000);
                break;

            case "droptest":
                if (!skillDB.CheckIfSkillExist(499900, CharID))
                    skillDB.registerSkill(CharID, 499900, 99);
                break;

            case "nompcost":
                if (!skillDB.CheckIfSkillExist(499901, CharID))
                    skillDB.registerSkill(CharID, 499901, 99);
                break;

            case "lordtest":
                if (!skillDB.CheckIfSkillExist(499902, CharID))
                    skillDB.registerSkill(CharID, 499902, 99);
                break;

            case "blesstest":
                if (!skillDB.CheckIfSkillExist(499903, CharID))
                    skillDB.registerSkill(CharID, 499903, 99);
                charDB.updateBlessPoints(CharID, 500000);
                break;

            case "godmode":
                if (!skillDB.CheckIfSkillExist(399900, CharID))
                    skillDB.registerSkill(CharID, 399900, 99);
                break;

            case "stopmobs":
                if (!skillDB.CheckIfSkillExist(399901, CharID))
                    skillDB.registerSkill(CharID, 399901, 99);
                break;

            case "alladminskills":
                for (int s = 0; s < 4; s++)
                    if (!skillDB.CheckIfSkillExist(499900 + s, CharID))
                        skillDB.registerSkill(CharID, 499900 + s, 99);

                for (int s = 0; s < 2; s++)
                    if (!skillDB.CheckIfSkillExist(399900 + s, CharID))
                        skillDB.registerSkill(CharID, 399900 + s, 99);
                break;

            case "addpots":
                charDB.updatePotions(CharID, HP_POTION, 50);
                charDB.updatePotions(CharID, MP_POTION, 50);
                break;

            default:
                Toasts.makeToast(this, "the code is incorrect");
                break;
        }
    }

    public void shopClick(View v) {
        switch (v.getId()) {

            case (R.id.hppot):
                setDialog("Buy HP Potion?", "HP " + getString(R.string.pothpmp), R.drawable.hppot, 3, HP_POTION, 50);
                break;

            case (R.id.mppot):
                setDialog("Buy MP Potion?", "MP " + getString(R.string.pothpmp), R.drawable.mppot, 3, MP_POTION, 50);
                break;

            case (R.id.Code):
                setDialog("Testing Codes", getString(R.string.testcode), R.drawable.trans, 4, 0, 0);
                break;

            case (R.id.back):
                Intent moveb = new Intent(Shop.this, Game.class);
                startActivity(moveb.putExtra("charID", CharID));
                break;
        }
    }

    @Override
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