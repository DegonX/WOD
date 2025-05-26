package com.degonx.warriorsofdeagon.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BlessesDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "Blesses.db";

    //Tables Names
    private static final String TABLE_BLESSES = "BlessesDB";

    //Table Columns names
    private static final String KEY_CHARID = "charID";
    private static final int totalBlesses = 15;

    public BlessesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            StringBuilder createTable = new StringBuilder("CREATE TABLE " + TABLE_BLESSES + "(");
            createTable.append(KEY_CHARID).append(" INTEGER PRIMARY KEY");

            for (int i = 1; i <= totalBlesses; i++) {
                createTable.append(", charBless").append(i).append(" INTEGER");
            }

            createTable.append(")");
            db.execSQL(createTable.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLESSES);
        onCreate(db);
    }

    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_BLESSES});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void createCharBless(int CharID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CHARID, CharID);

        for (int b = 1; b <= totalBlesses; b++)
            values.put(("charBless" + b), 0);

        db.insert(TABLE_BLESSES, null, values);
        db.close();
    }

    public int[] getCharBlesses(int CharID) {
        int[] Blesses = new int[totalBlesses];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLESSES + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        if (cursor.moveToFirst())
            for (int b = 0; b < Blesses.length; b++)
                Blesses[b] = Integer.parseInt(cursor.getString(b + 1));
        db.close();
        cursor.close();
        return Blesses;
    }

    public void levelUpBless(int charID, int ind, int level) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BLESSES + " SET " + ("charBless" + ind) + "='" + level + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }

    public boolean checkIfBlessesExist(int CharID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_CHARID + " FROM " + TABLE_BLESSES + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);
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

    public void deleteCharBlesses(int CharID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BLESSES + "  WHERE " + KEY_CHARID + "='" + CharID + "'");
        db.execSQL("VACUUM");
        db.close();
    }
}