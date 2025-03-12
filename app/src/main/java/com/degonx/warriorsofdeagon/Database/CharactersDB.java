package com.degonx.warriorsofdeagon.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.degonx.warriorsofdeagon.Enums.AreaEnums.Areas;
import com.degonx.warriorsofdeagon.Enums.WeaponsAndElementsEnums.Elements;
import com.degonx.warriorsofdeagon.Objects.Character;
import com.degonx.warriorsofdeagon.Objects.Characters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("Range")
public class CharactersDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "Characters.db";

    //Tables Names
    private static final String TABLE_CHARACTERS = "CharactersDB";

    //Table Columns names
    private static final String KEY_CHARID = "charID";
    private static final String KEY_CHARACCOUNT = "charAccount";
    private static final String KEY_CHARLEVEL = "charLevel";
    private static final String KEY_CHARXP = "charXp";
    private static final String KEY_CHARNAME = "charName";
    private static final String KEY_CHARSKILLP = "charSkillP";
    private static final String KEY_CHARELEMENT1 = "charElement1";
    private static final String KEY_CHARELEMENT2 = "charElement2";
    private static final String KEY_CHARELEMENT3 = "charElement3";
    private static final String KEY_CHARHPPOT = "charHPpot";
    private static final String KEY_CHARMPPOT = "charMPpot";
    private static final String KEY_BLESSPOINTS = "blessPoints";
    private static final String KEY_CURRENTAREA = "CurrentArea";


    public CharactersDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        if (!tableExists(db)) {
            String CREATE_CHARACTERS_TABLE = "CREATE TABLE " + TABLE_CHARACTERS + "("
                    + KEY_CHARID + " INTEGER PRIMARY KEY,"
                    + KEY_CHARACCOUNT + " INTEGER,"
                    + KEY_CHARLEVEL + " INTEGER,"
                    + KEY_CHARXP + " INTEGER,"
                    + KEY_CHARNAME + " TEXT,"
                    + KEY_CHARSKILLP + " INTEGER,"
                    + KEY_CHARELEMENT1 + " INTEGER,"
                    + KEY_CHARELEMENT2 + " INTEGER,"
                    + KEY_CHARELEMENT3 + " INTEGER,"
                    + KEY_CHARHPPOT + " INTEGER,"
                    + KEY_CHARMPPOT + " INTEGER,"
                    + KEY_BLESSPOINTS + " INTEGER,"
                    + KEY_CURRENTAREA + " TEXT" + ")";
            db.execSQL(CREATE_CHARACTERS_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);
        onCreate(db);
    }

    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_CHARACTERS});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void createCharacter(int charID, int charAccount, String charName, int ele1, int ele2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CHARID, charID);
        values.put(KEY_CHARACCOUNT, charAccount);
        values.put(KEY_CHARLEVEL, 1);
        values.put(KEY_CHARXP, 0);
        values.put(KEY_CHARNAME, charName);
        values.put(KEY_CHARSKILLP, 0);
        values.put(KEY_CHARELEMENT1, ele1);
        values.put(KEY_CHARELEMENT2, ele2);
        values.put(KEY_CHARELEMENT3, 0);
        values.put(KEY_CHARHPPOT, 0);
        values.put(KEY_CHARMPPOT, 0);
        values.put(KEY_BLESSPOINTS, 0);
        values.put(KEY_CURRENTAREA, "StarterCamp");

        db.insert(TABLE_CHARACTERS, null, values);
        db.close();
    }

    public Integer totalCharacters() {
        int LastCharID = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT charID FROM " + TABLE_CHARACTERS + " WHERE charID>'0' ORDER BY charID DESC LIMIT 1", null);

        if (cursor != null && !cursor.isClosed()) {
            if (cursor.moveToFirst()) {
                do {
                    LastCharID = Integer.parseInt(cursor.getString(cursor.getColumnIndex("charID")));
                    cursor.close();
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return LastCharID;
    }

    public Character getCharacterData(int CharID) {
        Character Data = new Character();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        if (cursor.moveToFirst()) {
            Data.setCharID(Integer.parseInt(cursor.getString(0)));
            Data.setAccountID(Integer.parseInt(cursor.getString(1)));
            Data.setCharLevel(Integer.parseInt(cursor.getString(2)));
            Data.setCharXP(Integer.parseInt(cursor.getString(3)));
            Data.setCharName(cursor.getString(4));
            Data.setCharSkillPoints(Integer.parseInt(cursor.getString(5)));
            Data.setCharHPpots(Integer.parseInt(cursor.getString(9)));
            Data.setCharMPpots(Integer.parseInt(cursor.getString(10)));
            Data.setCharBlessPoints(Integer.parseInt(cursor.getString(11)));
            Data.setCharArea(Areas.valueOf(cursor.getString(12)));
        }

        db.close();
        cursor.close();
        return Data;
    }

    public List<Elements> getCharacterElements(int CharID) {
        List<Elements> charElements = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        if (cursor.moveToFirst()) {
            charElements.add(Elements.values()[Integer.parseInt(cursor.getString(6))]);
            charElements.add(Elements.values()[Integer.parseInt(cursor.getString(7))]);
            charElements.add(Elements.values()[Integer.parseInt(cursor.getString(8))]);
        }

        charElements.removeAll(Collections.singleton(Elements.NONE));

        db.close();
        cursor.close();
        return charElements;
    }

    public List<Characters> getCharsForLobby(int accID) {
        List<Characters> DataList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARACCOUNT + "='" + accID + "'", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Characters Data = new Characters(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(2)), cursor.getString(4));
                    DataList.add(Data);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        db.close();
        return DataList;
    }

    public void updateCharacterLevelUp(int charID, int charLevel, int charSkillP) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CHARLEVEL, charLevel);
        values.put(KEY_CHARSKILLP, charSkillP);

        db.update(TABLE_CHARACTERS, values, KEY_CHARID + " = ?", new String[]{String.valueOf(charID)});
        db.close();
    }

    public void updateSkillPoints(int charID, int skill) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET " + KEY_CHARSKILLP + "='" + skill + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }

    public void updateEXP(int charID, int xp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET " + KEY_CHARXP + "='" + xp + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }


    public void updateElements(int charID, int charEle, int type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET charElement" + charEle + "='" + type + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }

    public void updatePotions(int charID, String pot, int Count) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (pot.equals("HP"))
            db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET " + KEY_CHARHPPOT + "='" + Count + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        else if (pot.equals("MP"))
            db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET " + KEY_CHARMPPOT + "='" + Count + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");

        db.close();
    }

    public void updateBlessPoints(int charID, int points) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET " + KEY_BLESSPOINTS + "='" + points + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }


    public void updateCurrentArea(int charID, String area) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_CHARACTERS + " SET " + KEY_CURRENTAREA + "='" + area + "'" + " WHERE " + KEY_CHARID + "='" + charID + "'");
        db.close();
    }

    public boolean checkIfNameExist(String charName) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + KEY_CHARNAME + " FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARNAME + "='" + charName + "'", null);
            if (cursor.moveToFirst()) {
                db.close();
                return true;
            }
            cursor.close();
            db.close();
        } catch (Exception ignored) {
        }
        return false;
    }

    public void deleteCharacter(int CharID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CHARACTERS + "  WHERE " + KEY_CHARID + "='" + CharID + "'");
        db.execSQL("VACUUM");
        db.close();
    }

    public Integer getAccountID(int charID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_CHARACCOUNT + " FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARID + "='" + charID + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_CHARACCOUNT));
        cursor.close();
        return -1;
    }

    public Integer getCharHPpot(int charID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_CHARHPPOT + " FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARID + "='" + charID + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_CHARHPPOT));
        cursor.close();
        return -1;
    }

    public Integer getCharMPpot(int charID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_CHARMPPOT + " FROM " + TABLE_CHARACTERS + " WHERE " + KEY_CHARID + "='" + charID + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_CHARMPPOT));
        cursor.close();
        return -1;
    }
}