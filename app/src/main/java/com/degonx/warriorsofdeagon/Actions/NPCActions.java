package com.degonx.warriorsofdeagon.Actions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.degonx.warriorsofdeagon.Adapters.InventoryAdapter;
import com.degonx.warriorsofdeagon.Database.AccountsDB;
import com.degonx.warriorsofdeagon.Database.EquipmentsDB;
import com.degonx.warriorsofdeagon.Enums.EquipmentsEnums.EquipmentsEnum;
import com.degonx.warriorsofdeagon.Enums.ItemsEnums.GachaItems;
import com.degonx.warriorsofdeagon.Enums.ItemsEnums.QuestsItemsRewardPool;
import com.degonx.warriorsofdeagon.Enums.ItemsEnums.ShopItems;
import com.degonx.warriorsofdeagon.Enums.QuestsEnum;
import com.degonx.warriorsofdeagon.Game;
import com.degonx.warriorsofdeagon.Interfaces.Items;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Equipments;
import com.degonx.warriorsofdeagon.Objects.Quest;
import com.degonx.warriorsofdeagon.R;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@SuppressLint("SetTextI18n")

public class NPCActions {

    private final Game game;

    private final AccountsDB accountsDB;
    private final Character Char;
    private final EquipmentsDB equipmentsDB;
    private final Equipments[] equipments;
    private final Equipments[] storageEquipments = new Equipments[50];
    private Equipments equipmentHolder, combineEquipmentHolder1, combineEquipmentHolder2;
    private final ConstraintLayout shopLayout, storageLayout, jewelerLayout;
    private final TextView inventoryCountTxt, oresTV, NPCTitle, equipmentName, equipmentStats;
    private TextView storageCountTxt;
    private AlertDialog NPCDialog, gachaDialog;
    private ImageView combineItem1, combineItem2;
    private final Random Ran = new Random();
    private int inventoryCount = 0, storageCount = 0;
    private boolean storageItem, dialogSizeSet = false;
    private final InventoryAdapter inventoryAdapter;
    private InventoryAdapter storageAdapter;
    private final Button NPCActionBtn;

    public NPCActions(Game game, AccountsDB accountsDB, Character Char) {
        this.game = game;
        this.accountsDB = accountsDB;
        this.Char = Char;
        equipmentsDB = Char.getEquipmentDB();
        equipments = Char.getCharEquipments();

        //get equipments count
        for (Equipments e : equipments)
            if (e != null)
                inventoryCount++;

        //create dialog view
        View npcView = createNPCDialogView();

        //set item count text
        inventoryCountTxt = npcView.findViewById(R.id.inveCountTXT);
        inventoryCountTxt.setText("Inventory 80/" + inventoryCount);

        //set equipments grid
        inventoryAdapter = new InventoryAdapter(game, equipments);
        GridView equipmentsGrid = npcView.findViewById(R.id.InventoryGrid);
        equipmentsGrid.setAdapter(inventoryAdapter);

        //onClick set itemholder and show equipment data
        equipmentsGrid.setOnItemClickListener((parent, view, position, id) -> {
            storageItem = false;
            equipmentHolder = equipments[position];
            setEquipmentData();
        });

        //action button for all npcs actions
        NPCActionBtn = npcView.findViewById(R.id.actionBtn);
        NPCActionBtn.setOnClickListener(na -> {
            if (equipmentHolder != null) {
                String action = NPCActionBtn.getText().toString();
                switch (action) {

                    //sell item
                    case "Sell":
                        sellEquipmentDialog(EquipmentsEnum.valueOf(equipmentHolder.equipmentID).getEquipmentImage(), (equipmentHolder.equipmentUpgradeTimes + 1) * 10);
                        break;

                    //upgrade item
                    case "Upgrade":
                        checkIfUpgradePossible();
                        break;

                    //select item from combine
                    case "Select":
                        addItemToCombine();
                        break;

                    //deposit item to storage
                    case "Deposit":
                        if (!storageItem)
                            //check if there is enough space in storage
                            if (storageCount < 50)
                                depositItem();
                            else
                                Toasts.makeToast(game, "Storage is full");
                        else
                            Toasts.makeToast(game, "Item is already stored");
                        break;
                }
            } else
                Toasts.makeToast(game, "no item is selected");
        });

        //close npc dialog
        Button closeBtn = npcView.findViewById(R.id.close);
        closeBtn.setOnClickListener(cd -> NPCDialog.dismiss());

        //set ores text
        oresTV = npcView.findViewById(R.id.oresTxt);
        oresTV.setText(String.valueOf(Char.getOres()));

        //find and set npcs layouts
        shopLayout = npcView.findViewById(R.id.shopLayout);
        jewelerLayout = npcView.findViewById(R.id.jewelerLayout);
        storageLayout = npcView.findViewById(R.id.storageLayout);
        setShop(npcView);
        setJeweler(npcView);
        setStorage(npcView);

        //find other layout view that used in all
        NPCTitle = npcView.findViewById(R.id.NPCTitle);

        equipmentName = npcView.findViewById(R.id.equipmentName);
        equipmentStats = npcView.findViewById(R.id.equipmentStats);
    }

    private void setShop(View npcView) {
        //get all the items the merchant has
        ShopItems[] shopItems = ShopItems.values();

        //create tablerow and add it to tablelayout
        TableLayout shopItemsTL = npcView.findViewById(R.id.shopItemTL);
        TableRow itemTR = new TableRow(game);
        shopItemsTL.addView(itemTR);

        //add items to tablerow
        for (ShopItems i : shopItems) {
            ImageView item = new ImageView(game);
            itemTR.addView(item);
            item.setImageResource(i.getItemImage());

            //set buy potion dialog
            item.setOnClickListener(bi -> buyPotionsDialog(i.getItemName(), i.getItemImage(), i.getItemDescription(), i.getItemPrice()));

            //create new tablerow if the current is full
            if (itemTR.getChildCount() % 5 == 0) {
                itemTR = new TableRow(game);
                shopItemsTL.addView(itemTR);

            }
        }

        //create gacha dialog
        setGachaDialog();

        //set gacha button
        Button gachaBtn = npcView.findViewById(R.id.gachaBtn);
        gachaBtn.setOnClickListener(gac -> gachaDialog.show());
    }

    private void sellEquipmentDialog(int icon, int ores) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(game);
        dialog.setTitle("Sell Equipment?");
        dialog.setIcon(icon);
        dialog.setMessage("you will receive " + ores + " Ores from selling this item");
        dialog.setPositiveButton("YES", (dia, which) -> sellEquipment(ores));
        dialog.setNegativeButton("NO", (dia, which) -> dia.dismiss());
        dialog.show();
    }

    //sell equipment
    private void sellEquipment(int ores) {
        //add ores from sell
        updateOres(ores);

        //remove the equipment from database
        equipmentsDB.deleteEquip(equipmentHolder.equipmentPK);

        //remove the equipment from inventory
        equipments[equipmentHolder.equipmentInventorySlot] = null;
        equipmentHolder = null;
        updateInventory(-1);
    }

    private void buyPotionsDialog(String itemName, int icon, String message, int price) {
        AlertDialog.Builder potDialog = new AlertDialog.Builder(game);
        potDialog.setTitle(itemName);
        potDialog.setIcon(icon);
        potDialog.setMessage(message + ",its item cost " + price + " Ores");
        EditText editText = new EditText(game);
        potDialog.setView(editText);
        editText.setText("1");
        potDialog.setPositiveButton("Buy", (dia, which) -> {
            try {
                buyPotions(itemName, Integer.parseInt(editText.getText().toString()), price);
            } catch (Exception e) {
                Toasts.makeToast(game, "invalid input");
            }
        });
        potDialog.setNegativeButton("Close", (dia, which) -> dia.dismiss());
        potDialog.show();
    }

    private void buyPotions(String potType, int amount, int price) {
        if (amount > 0) {
            if (Char.getOres() >= amount * price) {
                updateOres(-amount * price);
                Char.addPotions(potType, amount);
            } else
                Toasts.makeToast(game, "you don't have enough Ores to buy");
        } else
            Toasts.makeToast(game, "enter number bigger the 0");
    }

    private void setGachaDialog() {
        AlertDialog.Builder gachaDialogBuilder = new AlertDialog.Builder(game);
        LayoutInflater layoutInflater = (LayoutInflater) game.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View gachaView = layoutInflater.inflate(R.layout.gachaview, null);
        gachaDialogBuilder.setView(gachaView);

        //find all the objects in the gacha layout
        ImageView gachaImage = gachaView.findViewById(R.id.GachaImage);
        TextView gachaName = gachaView.findViewById(R.id.GachaName);
        Button gachaPullBtn = gachaView.findViewById(R.id.GachaPull);
        Button gachaCancelBtn = gachaView.findViewById(R.id.GachaCancel);

        //pull gacha item
        gachaPullBtn.setOnClickListener(gp -> {

            //check if the inventory is not full and if theres enough ores
            if (inventoryCount < 80) {
                if (Char.getOres() >= 10000) {

                    //remove -10000 ores
                    updateOres(-10000);

                    //pull gacha item and get it
                    EquipmentsEnum pulledItem = pullItem(GachaItems.values());

                    //show the gacha item name and image
                    gachaImage.setImageResource(pulledItem.getEquipmentImage());
                    gachaName.setText(pulledItem.getEquipmentName());

                } else
                    Toasts.makeToast(game, "not enough Ores");
            } else
                Toasts.makeToast(game, "Inventory is full");
        });

        gachaDialog = gachaDialogBuilder.create();
        gachaCancelBtn.setOnClickListener(gc -> gachaDialog.dismiss());
    }

    private void setJeweler(View npcView) {
        //find and setOnClick to remove item from combine on combineitem imageviews
        combineItem1 = npcView.findViewById(R.id.combineItem1);
        combineItem1.setOnClickListener(ri1 -> {
            if (combineEquipmentHolder1 != null) {
                combineEquipmentHolder1 = null;
                combineItem1.setImageResource(R.drawable.empty);
            }
        });
        combineItem2 = npcView.findViewById(R.id.combineItem2);
        combineItem2.setOnClickListener(ri2 -> {
            if (combineEquipmentHolder2 != null) {
                combineEquipmentHolder2 = null;
                combineItem2.setImageResource(R.drawable.empty);
            }
        });

        //attempt to combine the items
        Button combineBtn = npcView.findViewById(R.id.combineBtn);
        combineBtn.setOnClickListener(ci -> combineItems());
    }

    private void addItemToCombine() {
        //check if the item is an accessory
        if (equipmentHolder.equipmentType.contains("accessory"))

            //check if the item is not already selected
            if (combineEquipmentHolder1 != equipmentHolder && combineEquipmentHolder2 != equipmentHolder) {

                //add item for combine
                if (combineEquipmentHolder1 == null) {
                    combineEquipmentHolder1 = equipmentHolder;
                    combineItem1.setImageResource(EquipmentsEnum.valueOf(combineEquipmentHolder1.equipmentID).getEquipmentImage());
                } else if (combineEquipmentHolder2 == null) {
                    combineEquipmentHolder2 = equipmentHolder;
                    combineItem2.setImageResource(EquipmentsEnum.valueOf(combineEquipmentHolder2.equipmentID).getEquipmentImage());
                }
            } else
                Toasts.makeToast(game, "this item already selected");
        else
            Toasts.makeToast(game, "can only combine accessories");
    }

    private void combineItems() {
        //check if there is no missing items
        if (combineEquipmentHolder1 != null && combineEquipmentHolder2 != null)

            //check if the 1st item is the same as the 2nd
            if (combineEquipmentHolder1.equipmentID.equals(combineEquipmentHolder2.equipmentID))

                //check if the items has the same upgrade count
                if (combineEquipmentHolder1.equipmentUpgradeTimes == combineEquipmentHolder2.equipmentUpgradeTimes)

                    //check if the item upgrade count is not over 10
                    if (combineEquipmentHolder1.equipmentUpgradeTimes < 10) {
                        //delete and remove one of the items
                        equipmentsDB.deleteEquip(combineEquipmentHolder2.equipmentPK);
                        equipments[combineEquipmentHolder2.equipmentInventorySlot] = null;
                        combineEquipmentHolder2 = null;
                        updateInventory(-1);
                        combineItem2.setImageResource(R.drawable.empty);

                        //upgrade the other item
                        combineEquipmentHolder1.equipmentUpgradeTimes++;
                        equipmentsDB.updateAccessories(combineEquipmentHolder1.equipmentPK, combineEquipmentHolder1.equipmentUpgradeTimes);
                        combineEquipmentHolder1 = null;
                        combineItem1.setImageResource(R.drawable.empty);

                    } else
                        Toasts.makeToast(game, "item upgrade is maxed");
                else
                    Toasts.makeToast(game, "items must have the same upgrade count");
            else
                Toasts.makeToast(game, "can only combine the same items");
        else
            Toasts.makeToast(game, "missing items");
    }

    private void setStorage(View npcView) {
        //set all stored items that belong to the account
        List<Equipments> storedEquipments = equipmentsDB.getCharEquipments(-Char.getAccountID());
        storageCount = storedEquipments.size();

        //set account's item in array
        for (Equipments equipment : storedEquipments)
            storageEquipments[equipment.equipmentInventorySlot] = equipment;

        //set storage grid and add account items in it
        GridView storageGrid = npcView.findViewById(R.id.storageGrid);
        storageAdapter = new InventoryAdapter(game, storageEquipments);
        storageGrid.setAdapter(storageAdapter);

        //show stored item data
        storageGrid.setOnItemClickListener((parent, view, position, id) -> {
            storageItem = true;
            equipmentHolder = storageEquipments[position];
            setEquipmentData();
        });

        //set stored item count text
        storageCountTxt = npcView.findViewById(R.id.storeCountTXT);
        storageCountTxt.setText("Storage 50/" + storageCount);

        //attempt withdraw item
        Button WithdrawBtn = npcView.findViewById(R.id.Withdraw);
        WithdrawBtn.setOnClickListener(wd -> {
            if (equipmentHolder != null) {
                if (storageItem) {
                    if (inventoryCount < 80)
                        withdrawItem();
                    else
                        Toasts.makeToast(game, "Inventory is full");
                } else
                    Toasts.makeToast(game, "Item is already in your inventory");
            }
        });
    }

    //put equipment in storage
    private void depositItem() {
        //update storage in database - deposit the item
        equipmentsDB.updateEquipmentsStorage(equipmentHolder.equipmentPK, Char.getAccountID() * -1);

        //get equipment inventory index
        int equipmentIndex = equipmentHolder.equipmentInventorySlot;

        //find an empty slot in the storage and place the item in it
        int emptySlot = getFirstEmptySlot(storageEquipments);

        //update item slot in database
        equipmentsDB.updateEquipmentSlot(equipmentHolder.equipmentPK, emptySlot, -1);

        //remove the item from the inventory and add it to storage
        storageEquipments[emptySlot] = equipmentHolder;
        equipments[equipmentIndex] = null;
        inventoryCount--;
        storageCount++;
        updateStorageView();
    }

    //take equipment from storage
    private void withdrawItem() {
        //update storage in database - withdraw the item
        equipmentsDB.updateEquipmentsStorage(equipmentHolder.equipmentPK, Char.getCharID());

        //get equipment storage index
        int equipmentIndex = equipmentHolder.equipmentInventorySlot;

        //find an empty slot in the inventory and place the item in it
        int emptySlot = getFirstEmptySlot(equipments);

        //update item slot in database
        equipmentsDB.updateEquipmentSlot(equipmentHolder.equipmentPK, emptySlot, -1);

        //remove the item from the storage and add it to inventory
        equipments[emptySlot] = equipmentHolder;
        storageEquipments[equipmentIndex] = null;
        inventoryCount++;
        storageCount--;
        updateStorageView();
    }

    //update storage view
    private void updateStorageView() {
        equipmentHolder = null;
        storageAdapter.notifyDataSetChanged();
        storageCountTxt.setText("Storage 50/" + storageCount);
        updateInventory(0);
    }

    private void checkIfUpgradePossible() {

        //check if the item is not an accessory
        if (!equipmentHolder.equipmentType.contains("accessory")) {

            //check if item has less than 50 upgrades
            if (equipmentHolder.equipmentUpgradeTimes < 50)

                //check if the item is weapon or armor
                if (equipmentHolder.equipmentType.contains("weapon"))

                    //armor
                    equipmentUpgradeDialog("Upgrade This Armor", (equipmentHolder.equipmentUpgradeTimes + 1) * 70, EquipmentsEnum.valueOf(equipmentHolder.equipmentID).getEquipmentImage());
                else
                    //weapon
                    equipmentUpgradeDialog("Upgrade This Weapon", ((int) Math.pow((equipmentHolder.equipmentUpgradeTimes + 1), 2)) * 100, EquipmentsEnum.valueOf(equipmentHolder.equipmentID).getEquipmentImage());
            else
                Toasts.makeToast(game, "item level maxed");
        } else
            Toasts.makeToast(game, "accessories cannot be upgraded");
    }

    private void equipmentUpgradeDialog(String Title, int icon, int cost) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(game);
        dialog.setTitle(Title);
        dialog.setIcon(icon);
        dialog.setMessage("Upgrade to " + (equipmentHolder.equipmentUpgradeTimes + 1) + " will cost " + cost + game.getString(R.string.equipup));
        dialog.setPositiveButton("YES", (dia, which) -> upgradeEquipment(cost));
        dialog.setNegativeButton("NO", (dia, which) -> dia.dismiss());
        dialog.show();
    }

    //upgrade equipment
    private void upgradeEquipment(int cost) {
        //get equipment type
        String type = equipmentHolder.equipmentType;

        //check if item is weapon or armor and if theres enough ores to upgrade
        if (Char.getOres() >= cost) {

            //remove upgrade cost
            updateOres(-cost);

            //generates random stats for the item by type
            if (!type.contains("weapon")) {
                //armor
                equipmentHolder.equipmentDEF += Ran.nextInt(6) + 25;
                if (type.contains("Helmet") || type.contains("Gloves"))
                    equipmentHolder.equipmentMP += Ran.nextInt(51) + 100;
                else if (type.contains("Shirt") || type.contains("Pants"))
                    equipmentHolder.equipmentHP += Ran.nextInt(51) + 100;

                if (type.contains("Gloves"))
                    equipmentHolder.equipmentATK += Ran.nextInt(11) + 10;
            } else
                //weapon
                equipmentHolder.equipmentATK += Ran.nextInt(20) + 1;

            //update changes in database
            equipmentsDB.updateEquipment(equipmentHolder.equipmentPK, ++equipmentHolder.equipmentUpgradeTimes, equipmentHolder.equipmentHP, equipmentHolder.equipmentMP, equipmentHolder.equipmentATK, equipmentHolder.equipmentDEF);
            setEquipmentData();
        } else
            Toasts.makeToast(game, "you don't have enough Ores to Upgrade");
    }

    //create quest dialog and give a random quest
    private void createQuestDialog(QuestsEnum newQuest) {
        AlertDialog.Builder questDialogBuilder = new AlertDialog.Builder(game);
        questDialogBuilder.setTitle("Quest Offer");
        questDialogBuilder.setMessage(newQuest.getQuestText());
        questDialogBuilder.setPositiveButton("OK", (dia, which) -> {
            Quest quest = new Quest(newQuest.getQuestMob(), newQuest.getQuestAmount(), Char.getGameUI());
            Char.setQuest(quest);
        });
        questDialogBuilder.setNegativeButton("CLOSE", (dia, which) -> dia.dismiss());
        questDialogBuilder.show();
    }

    //dialog for finish quest and reward the character
    private void finishQuestDialog() {
        AlertDialog.Builder questDialogBuilder = new AlertDialog.Builder(game);
        questDialogBuilder.setTitle("Quest Complete");
        questDialogBuilder.setMessage("you have done your quest, good job!");
        questDialogBuilder.setPositiveButton("OK", (dia, which) -> {
            questReward();
            Char.setQuest(null);
        });
        questDialogBuilder.setNegativeButton("CLOSE", (dia, which) -> dia.dismiss());
        questDialogBuilder.show();
    }

    //reward from finishing quest
    private void questReward() {
        updateOres((Ran.nextInt(30) + 1) * Char.getCharLevel());
        Char.addCharXP(30);

        //item reward
        if (inventoryCount < 80)
            pullItem(QuestsItemsRewardPool.values());
        else
            Toasts.makeToast(game, "inventory is full");
    }

    private EquipmentsEnum pullItem(Items[] items) {
        //get a random item name
        String Item = items[Ran.nextInt(items.length)].toString();

        //get the item by its name
        EquipmentsEnum newEquipment = EquipmentsEnum.valueOf(Item);

        //find empty slot in the inventory
        int inventorySlot = getFirstEmptySlot(equipments);

        //create the item
        Equipments equipment = new Equipments(equipmentsDB.totalEquipments() + 1, newEquipment.toString(), -1, inventorySlot, 0, newEquipment.getEquipmentBaseHP(), newEquipment.getEquipmentBaseMP(), newEquipment.getEquipmentBaseAttack(), newEquipment.getEquipmentBaseDefense());

        //add the item to inventory and update in database
        equipments[inventorySlot] = equipment;
        equipmentsDB.createEquipment(Char.getCharID(), equipment);

        //update inventory
        updateInventory(1);

        return newEquipment;
    }

    private void setEquipmentData() {
        if (equipmentHolder != null) {

            //set item upgraded count in it's name
            if (equipmentHolder.equipmentUpgradeTimes > 0)
                equipmentName.setText(EquipmentsEnum.valueOf(equipmentHolder.equipmentID).getEquipmentName() + "(" + equipmentHolder.equipmentUpgradeTimes + ")");
            else
                equipmentName.setText(EquipmentsEnum.valueOf(equipmentHolder.equipmentID).getEquipmentName());
            String stats = "";

            //add item stats names and values to text
            if (equipmentHolder.equipmentATK > 0)
                stats += "Attack:" + equipmentHolder.equipmentATK + "\n";
            if (equipmentHolder.equipmentDEF > 0)
                stats += "Defense:" + equipmentHolder.equipmentDEF + "\n";
            if (equipmentHolder.equipmentHP > 0)
                stats += "HP:" + equipmentHolder.equipmentHP + "\n";
            if (equipmentHolder.equipmentMP > 0)
                stats += "MP:" + equipmentHolder.equipmentMP;
            equipmentStats.setText(stats);
        }
    }

    public void showNPCDialog(String npc) {
        if (npc.contains("QuestGiver")) {
            if (Char.getQuest() == null) {
                QuestsEnum newQuest = QuestsEnum.values()[Ran.nextInt(QuestsEnum.values().length)];
                createQuestDialog(newQuest);
            } else if (Char.isQuestFinished())
                finishQuestDialog();
            else
                Toasts.makeToast(game, "you have not finished your quest yet");
        } else {

            //show npc dialog
            NPCDialog.show();

            //set dialog window size
            if (!dialogSizeSet) {
                Objects.requireNonNull(NPCDialog.getWindow()).setLayout(ScreenControl.ScreenWidth, ScreenControl.ScreenHeight);
                dialogSizeSet = false;
            }

            //hide all npc layout in case they were visible from previous use
            hideAllNpcViews();

            //update ore and inventory item count
            oresTV.setText(String.valueOf(Char.getOres()));

            inventoryCount = Char.getInventoryCount();
            inventoryCountTxt.setText("Inventory 80/" + inventoryCount);

            //set npc view by npc type
            if (npc.contains("Merchant")) {
                NPCTitle.setText("Shop");
                NPCActionBtn.setText("Sell");
                oresTV.setVisibility(View.VISIBLE);
                shopLayout.setVisibility(View.VISIBLE);
            } else if (npc.contains("Blacksmith")) {
                NPCTitle.setText("Blacksmith");
                NPCActionBtn.setText("Upgrade");
                oresTV.setVisibility(View.VISIBLE);
            } else if (npc.contains("Jeweler")) {
                NPCTitle.setText("Jewel Combiner");
                NPCActionBtn.setText("Select");
                jewelerLayout.setVisibility(View.VISIBLE);
            } else if (npc.contains("Storage")) {
                NPCTitle.setText("Storage");
                NPCActionBtn.setText("Deposit");
                storageLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    //hide dialog layout(if visible)
    public void hideAllNpcViews() {
        oresTV.setVisibility(View.GONE);
        shopLayout.setVisibility(View.GONE);
        jewelerLayout.setVisibility(View.GONE);
        storageLayout.setVisibility(View.GONE);
    }

    private void updateOres(int ore) {
        int newOres = Char.getOres() + ore;
        Char.setOres(newOres);
        accountsDB.updateOres(Char.getAccountID(), newOres);
        oresTV.setText(String.valueOf(newOres));
    }

    private void updateInventory(int num) {
        inventoryCount += num;
        inventoryCountTxt.setText("Inventory 80/" + inventoryCount);
        inventoryAdapter.notifyDataSetChanged();
        equipmentName.setText("");
        equipmentStats.setText("");
        Char.updateCharInventory(inventoryCount);
    }

    private int getFirstEmptySlot(Equipments[] equipments) {
        for (int e = 0; e < equipments.length; e++)
            if (equipments[e] == null)
                return e;
        return -1;
    }

    private View createNPCDialogView() {
        AlertDialog.Builder NPCDialogBuilder = new AlertDialog.Builder(game);
        LayoutInflater layoutInflater = (LayoutInflater) game.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.npcviewwithequipments, null);
        NPCDialogBuilder.setView(dialogView);
        NPCDialog = NPCDialogBuilder.create();

        return dialogView;
    }
}