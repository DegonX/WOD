package com.degonx.warriorsofdeagon.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.degonx.warriorsofdeagon.Objects.Equipments;

import java.util.ArrayList;
import java.util.List;

public class EquipmentsDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name 
    private static final String DATABASE_NAME = "Equipments.db";

    //Tables Names
    private static final String TABLE_EQUIP = "EquipmentsDB";

    //Table Columns names
    private static final String KEY_EQUIPMENTSID = "EquipmentsID";
    private static final String KEY_CHARID = "charID";
    private static final String KEY_EQUIPID = "EquipID";
    private static final String KEY_EQUIPSPOT = "EquipSpot";
    private static final String KEY_EQUPT = "EquipUPT";
    private static final String KEY_EQHP = "hp";
    private static final String KEY_EQMP = "mp";
    private static final String KEY_EQATK = "Damage";
    private static final String KEY_EQDEF = "Defence";


    public EquipmentsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            String CREATE_EQUIP_TABLE = "CREATE TABLE " + TABLE_EQUIP + "("
                    + KEY_EQUIPMENTSID + " INTEGER PRIMARY KEY,"
                    + KEY_CHARID + " INTEGER,"
                    + KEY_EQUIPID + " INTEGER,"
                    + KEY_EQUIPSPOT + " INTEGER,"
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
        Cursor cursor = db.rawQuery("SELECT EquipmentsID FROM " + TABLE_EQUIP + " WHERE EquipmentsID>'0' ORDER BY EquipmentsID DESC LIMIT 1", null);

        if (cursor != null && !cursor.isClosed())
            if (cursor.moveToFirst())
                do {
                    lastEquip = cursor.getInt(cursor.getColumnIndex("EquipmentsID"));
                    cursor.close();
                } while (cursor.moveToNext());

        db.close();
        return lastEquip;
    }

    public void createEquipment(int charID, Equipments equip) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EQUIPMENTSID, equip.equipmentPK);
        values.put(KEY_CHARID, charID);
        values.put(KEY_EQUIPID, equip.equipID);
        values.put(KEY_EQUIPSPOT, equip.equipSpot);
        values.put(KEY_EQUPT, equip.equipUPT);
        values.put(KEY_EQHP, equip.equipHP);
        values.put(KEY_EQMP, equip.equipMP);
        values.put(KEY_EQATK, equip.equipDMG);
        values.put(KEY_EQDEF, equip.equipDEF);

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

                    Equipments Data = new Equipments(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(2)),
                            Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)),
                            Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)),
                            Integer.parseInt(cursor.getString(7)), Integer.parseInt(cursor.getString(8)));

                    DataList.add(Data);
                } while (cursor.moveToNext());
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        db.close();
        return DataList;
    }

    public void updateEquipped(int equipmentsID, int equipS) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EQUIP + " SET " + KEY_EQUIPSPOT + "='" + equipS + "'" + " WHERE " + KEY_EQUIPMENTSID + "='" + equipmentsID + "'");
        db.close();
    }

    public void updateEquipmentsStorage(int equipmentsID, int CharID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_EQUIP + " SET " + KEY_CHARID + "='" + CharID + "'" + " WHERE " + KEY_EQUIPMENTSID + "='" + equipmentsID + "'");
        db.close();
    }

    public void updateEquipment(int equipmentsID, int equipUPT, int HP, int MP, int DMG, int DEF) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EQUPT, equipUPT);
        values.put(KEY_EQHP, HP);
        values.put(KEY_EQMP, MP);
        values.put(KEY_EQATK, DMG);
        values.put(KEY_EQDEF, DEF);

        db.update(TABLE_EQUIP, values, KEY_EQUIPMENTSID + " = ?", new String[]{String.valueOf(equipmentsID)});

        db.close();
    }

    public void deleteAllEquipment(int charID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EQUIP + "  WHERE " + KEY_CHARID + "='" + charID + "'");
        db.execSQL("VACUUM");
        db.close();
    }

    public void deleteEquip(int PK) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EQUIP + "  WHERE " + KEY_EQUIPMENTSID + "='" + PK + "'");
        db.execSQL("VACUUM");
        db.close();
    }
}