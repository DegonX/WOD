package com.degonx.warriorsofdeagon.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("Range")
public class AccountsDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name (When changing anything new in tables or DB you must rename the database or make sure you delete all tables)
    private static final String DATABASE_NAME = "accounts.db";

    //Tables Names
    private static final String TABLE_ACCOUNTS = "AccountsDB";

    //Table Columns names
    private static final String KEY_ACCOUNTID = "accountID";
    private static final String KEY_ACCOUNTUN = "accountUN";
    private static final String KEY_ACCOUNTPW = "accountPW";
    private static final String KEY_ACCOUNTTYPE = "accountType";
    private static final String KEY_ACCOUNTORES = "accountOres";

    public AccountsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                    + KEY_ACCOUNTID + " INTEGER PRIMARY KEY,"
                    + KEY_ACCOUNTUN + " TEXT,"
                    + KEY_ACCOUNTPW + " TEXT,"
                    + KEY_ACCOUNTTYPE + " INTEGER,"
                    + KEY_ACCOUNTORES + " INTEGER" + ")";
            db.execSQL(CREATE_ACCOUNTS_TABLE);
        }
    }

    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);

        // Create tables again
        onCreate(db);
    }

    //Check if table exists
    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_ACCOUNTS});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    //Register new Account
    public void registerAccount(int userID, String username, String password) {

        //Connect to db
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ACCOUNTID, userID);
        values.put(KEY_ACCOUNTUN, username);
        values.put(KEY_ACCOUNTPW, password);
        values.put(KEY_ACCOUNTTYPE, 2);
        values.put(KEY_ACCOUNTORES, 0);

        //Insert data to table
        db.insert(TABLE_ACCOUNTS, null, values);
        db.close();
    }

    //Get the last AccountID
    public Integer totalAccounts() {
        int lastAccountID = 0;

        //Get last accountID
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AccountID FROM " + TABLE_ACCOUNTS + " WHERE AccountID>'0' ORDER BY AccountID DESC LIMIT 1", null);

        //If results not empty
        if (cursor != null && !cursor.isClosed())

            //Get first line & do
            if (cursor.moveToFirst())
                do {
                    lastAccountID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("accountID")));
                    cursor.close();
                } while (cursor.moveToNext());

        db.close();
        return lastAccountID;
    }

    //set Ores Value
    public void updateOres(int accountID, int accountOres) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_ACCOUNTS + " SET " + KEY_ACCOUNTORES + "='" + accountOres + "'"  + " WHERE " + KEY_ACCOUNTID + "='" + accountID + "'");
        db.close();
    }

    public boolean checkIfAccountExist(String accountUN) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + KEY_ACCOUNTUN + " FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNTUN + "='" + accountUN + "'", null);
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

    public String getAccountPWID(String accountUN) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ACCOUNTPW + " FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNTUN + "='" + accountUN + "'", null);
        if (cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex(KEY_ACCOUNTPW));
        cursor.close();
        return null;
    }

    public Integer getAccountID(String accountUN) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ACCOUNTID + " FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNTUN + "='" + accountUN + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNTID));
        cursor.close();
        return -1;
    }

    public Integer getAccountType(int accountID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ACCOUNTTYPE + " FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNTID + "='" + accountID + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNTTYPE));
        cursor.close();
        return -1;
    }

    public Integer getOres(int accountID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_ACCOUNTORES + " FROM " + TABLE_ACCOUNTS + " WHERE " + KEY_ACCOUNTID + "='" + accountID + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_ACCOUNTORES));
        cursor.close();
        return -1;
    }
}