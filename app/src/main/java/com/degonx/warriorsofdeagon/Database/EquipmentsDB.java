package com.degonx.warriorsofdeagon.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.degonx.warriorsofdeagon.Objects.Equipments;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class EquipmentsDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name 
    private static final String DATABASE_NAME = "Equipments.db";

    //Tables Names
    private static final String TABLE_EQUIP = "EquipmentsDB";

    //Table Columns names
    private static final String KEY_EQUIPMENTPK = "EquipmentPK";
    private static final String KEY_CHARID = "charID";
    private static final String KEY_EQID = "EquipmentID";
    private static final String KEY_EQESLOT = "EquipESlot";
    private static final String KEY_EQISLOT = "EquipmentISlot";
    private static final String KEY_EQUPT = "EquipmentUpgrades";
    private static final String KEY_EQHP = "HP";
    private static final String KEY_EQMP = "MP";
    private static final String KEY_EQATK = "Damage";
    private static final String KEY_EQDEF = "Defense";


    public EquipmentsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            String CREATE_EQUIP_TABLE = "CREATE TABLE " + TABLE_EQUIP + "("
                    + KEY_EQUIPMENTPK + " INTEGER PRIMARY KEY,"
                    + KEY_CHARID + " TEXT,"
                    + KEY_EQID + " INTEGER,"
                    + KEY_EQESLOT + " INTEGER,"
                    + KEY_EQISLOT + " INTEGER,"
                    + KEY_EQUPT + " INTEGER,"
                    + KEY_EQHP + " INTEGER,"
                    + KEY_EQMP + " INTEGER,"
                    + KEY_EQATK + " INTEGER,"
                    + KEY_EQDEF + " INTEGER" + ")";
            db.execSQL(CREATE_EQUIP_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EQUIP);
        onCreate(db);
    }

    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_EQUIP});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public Integer totalEquipments() {
        int lastEquip = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT EquipmentPK FROM " + TABLE_EQUIP + " WHERE EquipmentPK>'0' ORDER BY EquipmentPK DESC LIMIT 1", null);

        if (cursor != null && !cursor.isClosed())
            if (cursor.moveToFirst())
                do {
                    lastEquip = cursor.getInt(cursor.getColumnIndex("EquipmentPK"));
                    cursor.close();
                } while (cursor.moveToNext());

        db.close();
        return lastEquip;
    }

    public void createEquipment(int charID, Equipments equipment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EQUIPMENTPK, equipment.equipmentPK);
        values.put(KEY_CHARID, charID);
        values.put(KEY_EQID, equipment.equipmentID);
        values.put(KEY_EQESLOT, equipment.equipmentEquippedSlot);
        values.put(KEY_EQISLOT, equipment.equipmentInventorySlot);
        values.put(KEY_EQUPT, equipment.equipmentUpgradeTimes);
        values.put(KEY_EQHP, equipment.equipmentHP);
        values.put(KEY_EQMP, equipment.equipmentMP);
        values.put(KEY_EQATK, equipment.equipmentDMG);
        values.put(KEY_EQDEF, equipment.equipmentDEF);

        db.insert(TABLE_EQUIP, null, values);

        db.close();
    }

    public List<Equipments> getCharEquipments(int CharID) {
        List<Equipments> DataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EQUIP + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        try {
            if (cursor.moveToFirst())
                do {

                    Equipments Data = new Equipments(
                            Integer.parseInt(cursor.getString(0)),
                            cursor.getString(2),
                            Integer.parseInt(cursor.getString(3)),
                            Integer.parseInt(cursor.getString(4)),
                            Integer.parseInt(cursor.getString(5)),
                            Integer.parseInt(cursor.getString(6)),
                            Integer.parseInt(cursor.getString(7)),
                            Integer.parseInt(cursor.getString(8)),
                            Integer.parseInt(cursor.getString(9)));

                    DataList.add(Data);
                } while (cursor.moveToNext());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        db.close();
        return DataList;
    }

    public void updateEquipmentSlot(int equipmentsPK, int equipISlot, int equipESlot) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EQUIP + " SET " + KEY_EQESLOT + "='" + equipESlot + "'" + ", " + KEY_EQISLOT + "='" + equipISlot + "'" + " WHERE " + KEY_EQUIPMENTPK + "='" + equipmentsPK + "'");
        db.close();
    }

    public void updateEquipmentsStorage(int equipmentsPK, int CharID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EQUIP + " SET " + KEY_CHARID + "='" + CharID + "'" + " WHERE " + KEY_EQUIPMENTPK + "='" + equipmentsPK + "'");
        db.close();
    }

    public void updateEquipment(int equipmentsPK, int equipUPT, int HP, int MP, int DMG, int DEF) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EQUPT, equipUPT);
        values.put(KEY_EQHP, HP);
        values.put(KEY_EQMP, MP);
        values.put(KEY_EQATK, DMG);
        values.put(KEY_EQDEF, DEF);

        db.update(TABLE_EQUIP, values, KEY_EQUIPMENTPK + " = ?", new String[]{String.valueOf(equipmentsPK)});

        db.close();
    }

    public void updateAccessories(int equipmentsPK, int equipUPT) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EQUPT, equipUPT);

        db.update(TABLE_EQUIP, values, KEY_EQUIPMENTPK + " = ?", new String[]{String.valueOf(equipmentsPK)});

        db.close();
    }

    public void deleteAllEquipment(int charID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EQUIP + "  WHERE " + KEY_CHARID + "='" + charID + "'");
        db.execSQL("VACUUM");
        db.close();
    }

    public void deleteEquip(int equipmentsPK) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EQUIP + "  WHERE " + KEY_EQUIPMENTPK + "='" + equipmentsPK + "'");
        db.execSQL("VACUUM");
        db.close();
    }
}