package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.Adapters.InventoryAdapter;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.GameData.EquipmentsData;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Equipments;
import com.degonx.warriorsofdeagon.R;

@SuppressLint("SetTextI18n")
public class CharacterInventoryUI {

    private final TextView equipNameTxt, equipStatsTxt, inveSpaceCount, inveCharStats;
    private final Button equipBtn;
    private final GridView equipsGrid;
    private final ImageView[] equippedArmorIMG = new ImageView[5];
    private final ImageView[] equippedWeaponsIMG = new ImageView[5];
    private final InventoryAdapter inventoryAdapter;
    private final ConstraintLayout inventoryLay;
    private View itemHolder;
    private int itemIndexHolder;

    private final Character Char;

    public CharacterInventoryUI(Game game, Character Char, int[] equippedArmor, int[] equippedWeapon) {
        this.Char = Char;

        inventoryLay = game.findViewById(R.id.inveL);
        equipBtn = game.findViewById(R.id.Uequip);

        //get equipments from database
        inventoryAdapter = new InventoryAdapter(game, Char.getCharEquipmentsList(), 0);

        //set equipment into a gridview
        equipsGrid = game.findViewById(R.id.equipGrid);
        equipsGrid.setAdapter(inventoryAdapter);
        equipsGrid.setOnItemClickListener((parent, view, position, id) -> {

            //show equipment info
            itemHolder = view;
            equipBtn.setText("Equip");
            equipBtn.setOnClickListener(view1 -> Char.Equip(itemIndexHolder));
            setEquipmentData(position);
        });
        equipNameTxt = game.findViewById(R.id.equipName);
        equipStatsTxt = game.findViewById(R.id.invEStats);
        inveSpaceCount = game.findViewById(R.id.inveCount);
        inveCharStats = game.findViewById(R.id.invCstats);

        //load equipped images and set their actions
        equippedArmorIMG[0] = game.findViewById(R.id.imageHelmet);
        equippedArmorIMG[1] = game.findViewById(R.id.imageShirt);
        equippedArmorIMG[2] = game.findViewById(R.id.imagePants);
        equippedArmorIMG[3] = game.findViewById(R.id.imageGloves);
        equippedArmorIMG[4] = game.findViewById(R.id.imageShoes);

        //load equipped weapons images
        equippedWeaponsIMG[0] = game.findViewById(R.id.imageWeapon1);
        equippedWeaponsIMG[1] = game.findViewById(R.id.imageWeapon2);
        equippedWeaponsIMG[2] = game.findViewById(R.id.imageWeapon3);
        equippedWeaponsIMG[3] = game.findViewById(R.id.imageWeapon4);
        equippedWeaponsIMG[4] = game.findViewById(R.id.imageWeapon5);

        //set all equipped views onclick to show their data
        for (int eq = 0; eq < 5; eq++) {
            int finalEq = eq;
            equippedArmorIMG[eq].setOnClickListener(view -> equippedData(equippedArmor[finalEq]));
            equippedWeaponsIMG[eq].setOnClickListener(view -> equippedData(equippedWeapon[finalEq]));
        }
    }

    //open inventory page
    public void showInventoryPage() {
        inventoryLay.setVisibility(View.VISIBLE);
        setInventoryCharStats();
        itemIndexHolder = -1;
    }

    //set character stats in
    public void setInventoryCharStats() {
        equipNameTxt.setText("");
        equipStatsTxt.setText("");
        inveSpaceCount.setText("Inventory 80/" + Char.getCharEquipmentsList().size());
        String InveCharStats;

        //check if equip have the stat and add it to text
        if (Char.getEquipAdd(0) > 0)
            InveCharStats = "Attack:" + (Char.getCharDamage() + Char.getEquipAdd(0)) + "(" + Char.getCharDamage() + "+" + Char.getEquipAdd(0) + ")";
        else
            InveCharStats = "Attack:" + Char.getCharDamage();
        if (Char.getEquipAdd(1) > 0)
            InveCharStats += "\nDefence:" + (Char.getCharDefence() + Char.getEquipAdd(1)) + "(" + Char.getCharDefence() + "+" + Char.getEquipAdd(1) + ")";
        else
            InveCharStats += "\nDefence:" + Char.getCharDefence();
        if (Char.getEquipAdd(2) > 0)
            InveCharStats += "\nHP:" + (Char.getCharMaxHP() + Char.getEquipAdd(2)) + "(" + Char.getCharMaxHP() + "+" + Char.getEquipAdd(2) + ")";
        else
            InveCharStats += "\nHP:" + Char.getCharMaxHP();
        if (Char.getEquipAdd(3) > 0)
            InveCharStats += "\nMP:" + (Char.getCharMaxMP() + Char.getEquipAdd(3)) + "(" + Char.getCharMaxMP() + "+" + Char.getEquipAdd(3) + ")";
        else
            InveCharStats += "\nMP:" + Char.getCharMaxMP();

        inveCharStats.setText(InveCharStats);
    }

    //show selected equipped data
    private void equippedData(int equip) {
        if (equip > -1) {
            equipBtn.setText("UnEquip");
            setEquipmentData(equip);
            equipBtn.setOnClickListener(view2 -> {
                Char.unEquip(itemIndexHolder);
                itemIndexHolder = -1;
                setInventoryCharStats();
            });
        }
    }

    //show items stats in inventory
    private void setEquipmentData(int equip) {
        //hold equip position
        itemIndexHolder = equip;
        Equipments equipData = Char.getEquip(equip);

        //set and show equip stats
        String equipStats = "";
        if (equipData.equipUPT > 0)
            equipNameTxt.setText(EquipmentsData.getEquipmentName(equipData.equipID) + "(" + equipData.equipUPT + ")");
        else
            equipNameTxt.setText(EquipmentsData.getEquipmentName(equipData.equipID));

        if (equipData.equipDMG > 0)
            equipStats += "Attack:" + equipData.equipDMG + "\n";
        if (equipData.equipDEF > 0)
            equipStats += "Defence:" + equipData.equipDEF + "\n";
        if (equipData.equipHP > 0)
            equipStats += "HP:" + equipData.equipHP + "\n";
        if (equipData.equipMP > 0)
            equipStats += "MP:" + equipData.equipMP;

        equipStatsTxt.setText(equipStats);
    }

    //update inventory view when picked up an item
    public void updateInventory() {
        inventoryAdapter.notifyDataSetChanged();
    }

    //show unequipped item in inventory
    public void showUnequippedItem(int ind) {
        View newItemHolder = equipsGrid.getChildAt(ind);
        newItemHolder.setVisibility(View.VISIBLE);
    }

    //hide remove(hide) item in inventory
    public void hideItem() {
        itemHolder.setVisibility(View.GONE);
        itemIndexHolder = -1;
    }

    //set equipped image
    public void setEquippedImage(boolean weapon, int ind, int id, boolean putOn) {
        if (weapon) {
            if (putOn)
                equippedWeaponsIMG[ind].setImageResource(EquipmentsData.getEquipmentImage(id));
            else
                equippedWeaponsIMG[ind].setImageResource(R.drawable.eweapon);
        } else {
            if (putOn)
                equippedArmorIMG[ind].setImageResource(EquipmentsData.getEquipmentImage(id));
            else
                equippedArmorIMG[ind].setImageResource(EquipmentsData.getUnEquippedImage(id));
        }
    }
}