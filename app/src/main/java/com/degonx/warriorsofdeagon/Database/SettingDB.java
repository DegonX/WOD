package com.degonx.warriorsofdeagon.Database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("Range")
public class SettingDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name (When changing anything new in tables or DB you must rename the database or make sure you delete all tables)
    private static final String DATABASE_NAME = "Setting.db";

    //Tables Names
    private static final String TABLE_SETTING = "SettingDB";

    private static final String KEY_USERNAME = "userName";
    private static final String KEY_SAVEDUSER = "savedUser";
    private static final String KEY_MUTEBGM = "muteBGM";
    private static final String KEY_MUTESOUND = "muteSound";

    public SettingDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @SuppressLint("SQLiteString")
    @Override
    public void onCreate(SQLiteDatabase db) {

        if (!tableExists(db)) {
            String CREATE_TABLE_SETTING = "CREATE TABLE " + TABLE_SETTING + "("
                    + KEY_USERNAME + " STRING,"
                    + KEY_SAVEDUSER + " INTEGER,"
                    + KEY_MUTEBGM + " INTEGER,"
                    + KEY_MUTESOUND + " INTAGER" + ")";
            db.execSQL(CREATE_TABLE_SETTING);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTING);
        onCreate(db);
    }

    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_SETTING});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void createSettings() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, "");
        values.put(KEY_SAVEDUSER, 0);
        values.put(KEY_MUTEBGM, 1);
        values.put(KEY_MUTESOUND, 1);

        db.insert(TABLE_SETTING, null, values);
        db.close();
    }

    public void updateSetting(int what, boolean turnB) {
        SQLiteDatabase db = this.getWritableDatabase();

        int turn = turnB ? 1 : 0;

        if (what == 1)
            db.execSQL("UPDATE " + TABLE_SETTING + " SET " + KEY_MUTEBGM + "='" + turn + "'");
        else if (what == 2)
            db.execSQL("UPDATE " + TABLE_SETTING + " SET " + KEY_MUTESOUND + "='" + turn + "'");

        db.close();
    }

    public void updateSavedAccount(String userName, int save) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SETTING + " SET " + KEY_USERNAME + "='" + userName + "', " + KEY_SAVEDUSER + "='" + save + "'");
        db.close();
    }

    public boolean checkIfSettingExist() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT *  FROM " + TABLE_SETTING, null);
            if (cursor.moveToFirst()) {
                cursor.close();
                db.close();
                return true;
            }
            cursor.close();
            db.close();
        } catch (Exception ignored) {
        }
        return false;
    }

    public int getBGMState() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_MUTEBGM + " FROM " + TABLE_SETTING, null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_MUTEBGM));
        cursor.close();
        db.close();
        return 0;
    }

    public int getSoundState() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_MUTESOUND + " FROM " + TABLE_SETTING, null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_MUTESOUND));
        cursor.close();
        db.close();
        return 0;
    }

    public int getSaveState() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_SAVEDUSER + " FROM " + TABLE_SETTING, null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_SAVEDUSER));
        cursor.close();
        db.close();
        return 0;
    }

    public String getAccount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_USERNAME + " FROM " + TABLE_SETTING, null);
        if (cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
        cursor.close();
        db.close();
        return null;
    }
}