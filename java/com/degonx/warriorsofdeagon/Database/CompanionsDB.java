package com.degonx.warriorsofdeagon.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;;

public class CompanionsDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "Companion.db";

    //Tables Names
    private static final String TABLE_COMPANIONS = "CompanionDB";


    //Table Columns names
    private static final String KEY_COMPANIONID = "CompID";
    private static final String KEY_CHARID = "charID";
    private static final String KEY_COMPLEVEL = "CompLevel";
    private static final String KEY_COMPXP = "CompXP";
    private static final String KEY_COMPWEAPONLEVEL = "CompWeaponLevel";
    private static final String KEY_COMPELEMENTLEVEL = "CompElementLevel";


    public CompanionsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            String CREATE_COMPANION_TABLE = "CREATE TABLE " + TABLE_COMPANIONS + "("
                    + KEY_COMPANIONID + " INTEGER PRIMARY KEY,"
                    + KEY_CHARID + " INTEGER,"
                    + KEY_COMPLEVEL + " INTEGER,"
                    + KEY_COMPXP + " INTEGER,"
                    + KEY_COMPWEAPONLEVEL + " INTEGER,"
                    + KEY_COMPELEMENTLEVEL + " INTEGER" + ")";
            db.execSQL(CREATE_COMPANION_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANIONS);
        onCreate(db);
    }

    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_COMPANIONS});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public Integer totalCompanions() {
        int lastComp = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT CompID FROM " + TABLE_COMPANIONS + " WHERE CompID>'0' ORDER BY CompID DESC LIMIT 1", null);

        if (cursor != null && !cursor.isClosed())
            if (cursor.moveToFirst())
                do {
                    lastComp = cursor.getInt(cursor.getColumnIndex("CompID"));
                    cursor.close();
                } while (cursor.moveToNext());

        return lastComp;
    }

    public void createCompanion(int charID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPANIONID, totalCompanions() + 1);
        values.put(KEY_CHARID, charID);
        values.put(KEY_COMPLEVEL, 1);
        values.put(KEY_COMPXP, 0);
        values.put(KEY_COMPWEAPONLEVEL, 1);
        values.put(KEY_COMPELEMENTLEVEL, 1);

        db.insert(TABLE_COMPANIONS, null, values);

        db.close();
    }

    public boolean checkIfCompanionExist(int CID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_CHARID + " FROM " + TABLE_COMPANIONS + " WHERE " + KEY_CHARID + "='" + CID + "'", null);
        try {
            if (cursor.moveToFirst()) {
                db.close();
                cursor.close();
                return true;
            }
        } catch (Exception ignored) {
        }
        db.close();
        cursor.close();
        return false;
    }

    public int[] getCompanionData(int CharID) {
        int[] Data = new int[5];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COMPANIONS + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        try {
            if (cursor.moveToFirst()) {
                Data[0] = Integer.parseInt(cursor.getString(0));
                Data[1] = Integer.parseInt(cursor.getString(2));
                Data[2] = Integer.parseInt(cursor.getString(3));
                Data[3] = Integer.parseInt(cursor.getString(4));
                Data[4] = Integer.parseInt(cursor.getString(5));
            }

        } catch (Exception ignored) {

        }
        cursor.close();
        db.close();
        return Data;
    }


    public void updateCompLevel(int CompID, int LV) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_COMPANIONS + " SET " + KEY_COMPLEVEL + "='" + LV + "'" + " WHERE " + KEY_COMPANIONID + "='" + CompID + "'");
        db.close();
    }

    public void updateCompXP(int CompID, int XP) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_COMPANIONS + " SET " + KEY_COMPXP + "='" + XP + "'" + " WHERE " + KEY_COMPANIONID + "='" + CompID + "'");
        db.close();
    }

    public void updateCompWeaLevel(int CompID, int LV) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_COMPANIONS + " SET " + KEY_COMPWEAPONLEVEL + "='" + LV + "'" + " WHERE " + KEY_COMPANIONID + "='" + CompID + "'");
        db.close();
    }

    public void updateCompEleLevel(int CompID, int LV) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_COMPANIONS + " SET " + KEY_COMPELEMENTLEVEL + "='" + LV + "'" + " WHERE " + KEY_COMPANIONID + "='" + CompID + "'");
        db.close();
    }

    public void deleteCompanion(int CharID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COMPANIONS + "  WHERE " + KEY_CHARID + "='" + CharID + "'");
        db.execSQL("VACUUM");
        db.close();
    }
}