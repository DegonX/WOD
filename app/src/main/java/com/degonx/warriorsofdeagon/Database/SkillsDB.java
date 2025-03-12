package com.degonx.warriorsofdeagon.Database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.degonx.warriorsofdeagon.Enums.SkillsEnum;
import com.degonx.warriorsofdeagon.Objects.Skills;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("Range")
public class SkillsDB extends SQLiteOpenHelper {

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Database Name
    private static final String DATABASE_NAME = "Skills.db";

    //Tables Names
    private static final String TABLE_SKILLS = "SkillsDB";

    //Table Columns names
    private static final String KEY_SKILLPK = "skillPK";
    private static final String KEY_CHARID = "charID";
    private static final String KEY_SKILLID = "skillID";
    private static final String KEY_SKILLLEVEL = "skillLevel";
    private static final String KEY_SKILLVIEW = "skillView";


    public SkillsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!tableExists(db)) {
            String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_SKILLS + "("
                    + KEY_SKILLPK + " INTEGER PRIMARY KEY,"
                    + KEY_CHARID + " INTEGER,"
                    + KEY_SKILLID + " TEXT,"
                    + KEY_SKILLLEVEL + " INTEGER,"
                    + KEY_SKILLVIEW + " INTEGER" + ")";
            db.execSQL(CREATE_ACCOUNTS_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SKILLS);
        onCreate(db);
    }

    private boolean tableExists(SQLiteDatabase db) {
        if (db == null || !db.isOpen())
            return false;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", TABLE_SKILLS});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public List<List<Skills>> getCharSkills(int CharID) {
        List<List<Skills>> SkillsLists = new ArrayList<>();
        String SID;
        for (int l = 0; l < 5; l++)
            SkillsLists.add(new ArrayList<>());


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SKILLS + " WHERE " + KEY_CHARID + "='" + CharID + "'", null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    SID = cursor.getString(2);
                    Skills skill = new Skills(SID, Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)));
                    SkillsLists.get(getSkillTypeIndex(SkillsEnum.valueOf(SID).getSkillType())).add(skill);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        db.close();
        return SkillsLists;
    }

    private int getSkillTypeIndex(String type) {
        if (type.contains("weapon_mastery"))
            return 0;
        else if (type.contains("element_knowledge"))
            return 1;
        else if (type.contains("active_skill"))
            return 2;
        else if (type.contains("passive_skill"))
            return 3;
        else if (type.contains("attack_skill"))
            return 4;
        return -1;
    }

    public void registerSkill(int CID, SkillsEnum skill, int SLV) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SKILLPK, totalSkills() + 1);
        values.put(KEY_CHARID, CID);
        values.put(KEY_SKILLID, skill.toString());
        values.put(KEY_SKILLLEVEL, SLV);
        values.put(KEY_SKILLVIEW, 0);

        db.insert(TABLE_SKILLS, null, values);
        db.close();
    }

    public Integer totalSkills() {
        int lastSkill = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT skillPK FROM " + TABLE_SKILLS + " WHERE skillPK>'0' ORDER BY skillPK DESC LIMIT 1", null);
        if (cursor != null && !cursor.isClosed())
            if (cursor.moveToFirst())
                do {
                    lastSkill = Integer.parseInt(cursor.getString(cursor.getColumnIndex("skillPK")));
                    cursor.close();
                } while (cursor.moveToNext());
        return lastSkill;
    }

    public void updateSkillLevel(int skillLevel, String skillID, int charID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SKILLS + " SET " + KEY_SKILLLEVEL + "='" + skillLevel + "'" + " WHERE " + KEY_SKILLPK + "='" + getSkillPK(skillID, charID) + "'");
        db.close();
    }

    public void updateSkillView(boolean show, String skillID, int charID) {
        SQLiteDatabase db = this.getWritableDatabase();

        int shows = 0;
        if (show)
            shows = 1;

        db.execSQL("UPDATE " + TABLE_SKILLS + " SET " + KEY_SKILLVIEW + "='" + shows + "'" + " WHERE " + KEY_SKILLPK + "='" + getSkillPK(skillID, charID) + "'");
        db.close();
    }

    public void deleteAllSkill(int charID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_SKILLS + "  WHERE " + KEY_CHARID + "='" + charID + "'");
        db.execSQL("VACUUM");
        db.close();
    }

    public Integer getSkillPK(String skillID, int charID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_SKILLPK + " FROM " + TABLE_SKILLS + " WHERE " + KEY_CHARID + "='" + charID + "'" + " AND " + KEY_SKILLID + "='" + skillID + "'", null);
        if (cursor.moveToFirst())
            return cursor.getInt(cursor.getColumnIndex(KEY_SKILLPK));
        cursor.close();
        return -1;
    }

    public Boolean CheckIfSkillExist(String SkillID, int CharID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + KEY_SKILLPK + " FROM " + TABLE_SKILLS + " WHERE " + KEY_CHARID + "='" + CharID + "'" + " AND " + KEY_SKILLID + "='" + SkillID + "'", null);
        if (cursor.moveToFirst()) {
            return true;
        }
        cursor.close();
        return false;
    }
}