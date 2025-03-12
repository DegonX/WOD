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
    private static final String KEY_CHARBLESS = "charBless";
    private static final String KEY_CHARBLESS1 = "charBless1";
    private static final String KEY_CHARBLESS2 = "charBless2";
    private static final String KEY_CHARBLESS3 = "charBless3";
    private static final String KEY_CHARBLESS4 = "charBless4";
    private static final String KEY_CHARBLESS5 = "charBless5";
    private static final String KEY_CHARBLESS6 = "charBless6";
    private static final String KEY_CHARBLESS7 = "charBless7";
    private static final String KEY_CHARBLESS8 = "charBless8";
    private static final String KEY_CHARBLESS9 = "charBless9";
    private static final String KEY_CHARBLESS10 = "charBless10";
    private static final String KEY_CHARBLESS11 = "charBless11";
    private static final String KEY_CHARBLESS12 = "charBless12";
    private static final String KEY_CHARBLESS13 = "charBless13";
    private static final String KEY_CHARBLESS14 = "charBless14";
    private static final String KEY_CHARBLESS15 = "charBless15";
    private static final String KEY_CHARBLESS16 = "charBless16";

    private static final int totalBlesses = 16;

    public BlessesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            String CREATE_TABLE_BLESSES = "CREATE TABLE " + TABLE_BLESSES + "("
                    + KEY_CHARID + " INTEGER PRIMARY KEY,"
                    + KEY_CHARBLESS1 + " INTEGER,"
                    + KEY_CHARBLESS2 + " INTEGER,"
                    + KEY_CHARBLESS3 + " INTEGER,"
                    + KEY_CHARBLESS4 + " INTEGER,"
                    + KEY_CHARBLESS5 + " INTEGER,"
                    + KEY_CHARBLESS6 + " INTEGER,"
                    + KEY_CHARBLESS7 + " INTEGER,"
                    + KEY_CHARBLESS8 + " INTEGER,"
                    + KEY_CHARBLESS9 + " INTEGER,"
                    + KEY_CHARBLESS10 + " INTEGER,"
                    + KEY_CHARBLESS11 + " INTEGER,"
                    + KEY_CHARBLESS12 + " INTEGER,"
                    + KEY_CHARBLESS13 + " INTEGER,"
                    + KEY_CHARBLESS14 + " INTEGER,"
                    + KEY_CHARBLESS15 + " INTEGER,"
                    + KEY_CHARBLESS16 + " INTEGER" + ")";
            db.execSQL(CREATE_TABLE_BLESSES);
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
            values.put((KEY_CHARBLESS + b), 0);

        db.insert(TABLE_BLESSES, null, values);
        db.close();
    }

    public int[][] getCharBlesses(int CharID) {
        int[][] Data = new int[totalBlesses][2];

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BLESSES + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        if (cursor.moveToFirst())
            for (int b = 0; b < Data.length; b++)
                Data[b][0] = Integer.parseInt(cursor.getString(b + 1));
        db.close();
        cursor.close();
        return Data;
    }

    public void levelupBless(int charID, int how, int level) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_BLESSES + " SET " + (KEY_CHARBLESS + how) + "='" + level + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }

    public boolean checkIfBlessesExist(int CID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_CHARID + " FROM " + TABLE_BLESSES + " WHERE " + KEY_CHARID + "='" + CID + "'", null);
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