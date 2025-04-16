package com.degonx.warriorsofdeagon.UI;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.Adapters.InventoryAdapter;
import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEnum;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Equipments;
import com.degonx.warriorsofdeagon.R;

@SuppressLint("SetTextI18n")
public class CharacterInventoryUI {

    private boolean switchMode = false;
    private int positionHolder;
    private final TextView equipmentNameTxt, equipmentStatsTxt, inveSpaceCount, inveCharStats;
    private final Button equipBtn;
    private final ImageView[] equippedArmorIMG = new ImageView[9];
    private final ImageView[] equippedWeaponsIMG = new ImageView[3];
    private final InventoryAdapter inventoryAdapter;
    private final ConstraintLayout inventoryLay;

    private final Character Char;

    public CharacterInventoryUI(Game game, Character Char, Equipments[] charEquipments, Equipments[] equippedEquipments, Equipments[] equippedWeapon) {
        this.Char = Char;

        inventoryLay = game.findViewById(R.id.inveL);
        equipBtn = game.findViewById(R.id.Uequip);

        //get equipments from database
        inventoryAdapter = new InventoryAdapter(game, charEquipments);

        //set equipment into a gridview
        GridView equipmentsGrid = game.findViewById(R.id.InventoryGrid);
        equipmentsGrid.setAdapter(inventoryAdapter);
        equipmentsGrid.setOnItemClickListener((parent, view, position, id) -> {

            if (!switchMode) {
                if (charEquipments[position] != null) {
                    //show equipment info
                    equipBtn.setText("Equip");
                    setEquipmentData(charEquipments[position]);
                    equipBtn.setOnClickListener(view1 -> Char.Equip(position));
                }
            } else {
                Char.swipeEquipmentsSlots(positionHolder, position);
                switchMode = false;
                inventoryAdapter.notifyDataSetChanged();
            }
        });

        equipmentsGrid.setOnItemLongClickListener((parent, view, position, id) -> {
            positionHolder = position;
            if (charEquipments[positionHolder] != null) {
                view.setBackgroundResource(R.color.tred);
                switchMode = true;
            }
            return true;
        });

        equipmentNameTxt = game.findViewById(R.id.equipName);
        equipmentStatsTxt = game.findViewById(R.id.invEStats);
        inveSpaceCount = game.findViewById(R.id.inveCountTXT);
        inveCharStats = game.findViewById(R.id.invCstats);

        //load equipped armors images
        equippedArmorIMG[0] = game.findViewById(R.id.imageHelmet);
        equippedArmorIMG[1] = game.findViewById(R.id.imageShirt);
        equippedArmorIMG[2] = game.findViewById(R.id.imagePants);
        equippedArmorIMG[3] = game.findViewById(R.id.imageGloves);
        equippedArmorIMG[4] = game.findViewById(R.id.imageShoes);
        equippedArmorIMG[5] = game.findViewById(R.id.imageRing);
        equippedArmorIMG[6] = game.findViewById(R.id.imagePendent);
        equippedArmorIMG[7] = game.findViewById(R.id.imageEarring);
        equippedArmorIMG[8] = game.findViewById(R.id.imageBracelet);

        //set equipped armors views onclick to show their data
        for (int eq = 0; eq < 9; eq++) {
            int finalEq = eq;

            if (equippedEquipments[eq] != null)
                //set equipped slot image
                equippedArmorIMG[eq].setImageResource(EquipmentsEnum.valueOf(equippedEquipments[eq].equipmentID).getEquipmentImage());

            //show equipment stats on click
            equippedArmorIMG[eq].setOnClickListener(view -> equippedData(equippedEquipments[finalEq]));
        }

        //load equipped weapons images
        equippedWeaponsIMG[0] = game.findViewById(R.id.imageWeapon1);
        equippedWeaponsIMG[1] = game.findViewById(R.id.imageWeapon2);
        equippedWeaponsIMG[2] = game.findViewById(R.id.imageWeapon3);

        //set equipped weapons views onclick to show their data
        for (int eq = 0; eq < 3; eq++) {
            int finalEq = eq;

            if (equippedWeapon[eq] != null)
                //set equipped slot image
                equippedWeaponsIMG[eq].setImageResource(EquipmentsEnum.valueOf(equippedWeapon[eq].equipmentID).getEquipmentImage());

            //show weapon stats on click
            equippedWeaponsIMG[eq].setOnClickListener(view -> equippedData(equippedWeapon[finalEq]));
        }
    }

    //open inventory page
    public void showInventoryPage() {
        inventoryLay.setVisibility(View.VISIBLE);
        setInventoryCharStats();
    }

    //set character stats in
    public void setInventoryCharStats() {
        equipmentNameTxt.setText("");
        equipmentStatsTxt.setText("");
        inveSpaceCount.setText("Inventory 80/" + Char.getInventoryCount());
        String InveCharStats;

        //check if equip have the stat and add it to text
        if (Char.getEquipAdd(0) > 0)
            InveCharStats = "Attack:" + (Char.getCharAttack() + Char.getEquipAdd(0)) + "(" + Char.getCharAttack() + "+" + Char.getEquipAdd(0) + ")";
        else
            InveCharStats = "Attack:" + Char.getCharAttack();
        if (Char.getEquipAdd(1) > 0)
            InveCharStats += "\nDefense:" + (Char.getCharDefense() + Char.getEquipAdd(1)) + "(" + Char.getCharDefense() + "+" + Char.getEquipAdd(1) + ")";
        else
            InveCharStats += "\nDefense:" + Char.getCharDefense();
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
    private void equippedData(Equipments equipment) {
        if (equipment != null) {
            equipBtn.setText("UnEquip");
            setEquipmentData(equipment);
            equipBtn.setOnClickListener(ue -> {
                if (equipment.equipmentEquippedSlot != -1) {
                    Char.Unequip(equipment, -1);
                    setInventoryCharStats();
                }
            });
        }
    }

    //show items stats in inventory
    private void setEquipmentData(Equipments equipment) {
        //set and show equip stats
        String equipStats = "";
        if (equipment.equipmentUpgradeTimes > 0)
            equipmentNameTxt.setText(EquipmentsEnum.valueOf(equipment.equipmentID).getEquipmentName() + "(" + equipment.equipmentUpgradeTimes + ")");
        else
            equipmentNameTxt.setText(EquipmentsEnum.valueOf(equipment.equipmentID).getEquipmentName());

        if (equipment.equipmentATK > 0)
            equipStats += "Attack:" + equipment.equipmentATK + "\n";
        if (equipment.equipmentDEF > 0)
            equipStats += "Defense:" + equipment.equipmentDEF + "\n";
        if (equipment.equipmentHP > 0)
            equipStats += "HP:" + equipment.equipmentHP + "\n";
        if (equipment.equipmentMP > 0)
            equipStats += "MP:" + equipment.equipmentMP;

        equipmentStatsTxt.setText(equipStats);
    }

    //update inventory view
    public void updateInventory() {
        inventoryAdapter.notifyDataSetChanged();
    }

    //set equipped image
    public void setEquippedImage(boolean weapon, int ind, String ID, boolean putOn) {
        if (weapon) {
            if (putOn)
                equippedWeaponsIMG[ind].setImageResource(EquipmentsEnum.valueOf(ID).getEquipmentImage());
            else
                equippedWeaponsIMG[ind].setImageResource(R.drawable.eweapon);
        } else {
            if (putOn)
                equippedArmorIMG[ind].setImageResource(EquipmentsEnum.valueOf(ID).getEquipmentImage());
            else
                equippedArmorIMG[ind].setImageResource(getUnEquippedImage(EquipmentsEnum.valueOf(ID).getEquipmentType()));
        }
    }

    private int getUnEquippedImage(String equipType) {
        switch (equipType) {
            case "armor-Helmet":
                return R.drawable.ehelmet;
            case "armor-Shirt":
                return R.drawable.eshirt;
            case "armor-Pants":
                return R.drawable.epants;
            case "armor-Gloves":
                return R.drawable.egloves;
            case "armor-Shoes":
                return R.drawable.eshoes;
            case "accessory-Ring":
                return R.drawable.ering;
            case "accessory-Pendent":
                return R.drawable.ependent;
            case "accessory-Earring":
                return R.drawable.eearrings;
            case "accessory-Bracelet":
                return R.drawable.ebracelet;
            default:
                return R.drawable.eweapon;
        }
    }
}