package com.degonx.warriorsofdeagon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.degonx.warriorsofdeagon.Adapters.InventoryAdapter;
import com.degonx.warriorsofdeagon.GameData.EquipmentsData;
import com.degonx.warriorsofdeagon.VisionAndSound.ScreenControl;
import com.degonx.warriorsofdeagon.VisionAndSound.Toasts;
import com.degonx.warriorsofdeagon.VisionAndSound.Sounds;
import com.degonx.warriorsofdeagon.Objects.Equipments;
import com.degonx.warriorsofdeagon.Database.CharactersDB;
import com.degonx.warriorsofdeagon.Database.EquipmentsDB;

import java.io.IOException;
import java.util.List;

@SuppressLint("SetTextI18n")
public class Storage extends Activity {

    private final EquipmentsDB equipDB = new EquipmentsDB(this);
    private List<Equipments> charEquipments;
    private List<Equipments> storageEquipments;
    private int CharID, accountID, Holder, Move = -1;
    private final TextView[] equipText = new TextView[2];
    private InventoryAdapter charEquipAdapter, storageEquipAdapter;
    private TextView charInv, storageInv;
    final int BREAK_BACKGROUND_MUSIC = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenControl.screenControl(this);

        //receive CharID from previous activity and get it's AccountID
        Intent mIntent = getIntent();
        CharID = mIntent.getIntExtra("charID", 0);
        CharactersDB db2 = new CharactersDB(this);
        accountID = db2.getAccountID(CharID);

        setStoragePage();
        try {
            Sounds.playBGM(this, BREAK_BACKGROUND_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set storage page, Move(-1 - nothing , 1 - inventory , 2 - storage), Holder holds the position in List
    private void setStoragePage() {
        setContentView(R.layout.storage);
        equipText[0] = findViewById(R.id.equipName);
        equipText[1] = findViewById(R.id.invEStatsS);

        //set inventory gridview and onclick
        charEquipments = equipDB.getCharEquipments(CharID);
        charEquipAdapter = new InventoryAdapter(this, charEquipments, 2);
        charInv = findViewById(R.id.inveCount);
        charInv.setText("Inventory 80/" + charEquipments.size());
        GridView equipGridChar = findViewById(R.id.equipGrid);
        equipGridChar.setAdapter(charEquipAdapter);
        equipGridChar.setOnItemClickListener((parent, view, position, id) -> {
            Move = 1;
            Holder = position;
            setEquipData(charEquipments.get(position));
        });

        //set storage gridview and onclick
        storageEquipments = equipDB.getCharEquipments(-accountID);
        storageEquipAdapter = new InventoryAdapter(this, storageEquipments, 1);
        storageInv = findViewById(R.id.inveCountS);
        storageInv.setText("Storage 50/" + storageEquipments.size());
        GridView equipGridStorage = findViewById(R.id.equipGridS);
        equipGridStorage.setAdapter(storageEquipAdapter);
        equipGridStorage.setOnItemClickListener((parent, view, position, id) -> {
            Move = 2;
            Holder = position;
            setEquipData(storageEquipments.get(position));
        });

        //check if there is enough space in storage,if so store the selected item
        Button deposit = findViewById(R.id.Deposit);
        deposit.setOnClickListener(view -> {
            if (Move == 1) {
                if (storageEquipments.size() < 50) {
                    depositItem();
                } else
                    Toasts.makeToast(this, "Storage is full");
            } else if (Move == 2)
                Toasts.makeToast(this, "Item is already stored");
        });

        //check if there is enough space in inventory,if so add the equip to inventory
        Button withdraw = findViewById(R.id.Withdraw);
        withdraw.setOnClickListener(view -> {
            if (Move == 2) {
                if (charEquipments.size() < 80) {
                    withdrawItem();
                } else
                    Toasts.makeToast(this, "Inventory is full");
            } else if (Move == 1)
                Toasts.makeToast(this, "Item is already in your inventory");
        });

        //back to game button.
        Button btg = findViewById(R.id.backToGame);
        btg.setOnClickListener(view -> {
            Intent moveb = new Intent(Storage.this, Game.class);
            startActivity(moveb.putExtra("charID", CharID));
            finish();
        });
    }

    //show Equipments stats
    private void setEquipData(Equipments equip) {
        if (equip.equipUPT > 0)
            equipText[0].setText(EquipmentsData.getEquipmentName(equip.equipID) + "(" + equip.equipUPT + ")");
        else
            equipText[0].setText(EquipmentsData.getEquipmentName(equip.equipID));
        String stats = "";

        if (equip.equipDMG > 0)
            stats += "Attack:" + equip.equipDMG + "\n";
        if (equip.equipDEF > 0)
            stats += "Defence:" + equip.equipDEF + "\n";
        if (equip.equipHP > 0)
            stats += "HP:" + equip.equipHP + "\n";
        if (equip.equipMP > 0)
            stats += "MP:" + equip.equipMP;
        equipText[1].setText(stats);
    }

    //put equipment in storage
    private void depositItem() {
        equipDB.updateEquipmentsStorage(charEquipments.get(Holder).equipmentPK, accountID * -1);
        storageEquipments.add(charEquipments.get(Holder));
        charEquipments.remove(Holder);
        resetView();
    }

    //take equipment from storage
    private void withdrawItem() {
        equipDB.updateEquipmentsStorage(storageEquipments.get(Holder).equipmentPK, CharID);
        charEquipments.add(storageEquipments.get(Holder));
        storageEquipments.remove(Holder);
        resetView();
    }

    //resets storage view
    private void resetView() {
        Move = Holder = -1;
        charEquipAdapter.notifyDataSetChanged();
        storageEquipAdapter.notifyDataSetChanged();
        charInv.setText("Inventory 50/" + charEquipments.size());
        storageInv.setText("Storage 50/" + storageEquipments.size());
        equipText[0].setText("");
        equipText[1].setText("");
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