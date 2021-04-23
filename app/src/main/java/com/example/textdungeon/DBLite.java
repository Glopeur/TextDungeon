package com.example.textdungeon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

public class DBLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "db.sqlite";

    public DBLite(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE 'dungeon' ('SLOT' INTEGER, 'POSITION' INTEGER, 'INV' BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'dungeon'");
        this.onCreate(db);
    }

    public boolean insertData(Save save) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SLOT", save.getSlot());
        values.put("POSITION", save.getPosition());

        Gson gson = new Gson();
        values.put("INV", gson.toJson(save.getInventory()).getBytes());

        long res = 0;
        Cursor data = db.rawQuery("SELECT * FROM dungeon WHERE SLOT = " + save.getSlot(), null);
        if (data.getCount() == 0) {
            db.insert("dungeon", null, values);
        } else {
            db.update("dungeon", values, "SLOT = ?", new String[]{String.valueOf(save.getSlot())});
        }
        return (res != 0) ? false : true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM dungeon", null);
    }

    public Cursor getSlotData(int slot) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT POSITION, INV FROM dungeon WHERE SLOT = " + slot, null);
    }
}
