package com.example.catchregisterapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseController extends SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "CatchRegister.db";
    private static final int DATABASE_VERSION = 2; // Növeljük a verziót

    private static final String TABLE_NAME = "Catches";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LAKE = "catch_lake";
    private static final String COLUMN_WEIGHT = "catch_weight";
    private static final String COLUMN_BAIT = "catch_bait";
    private static final String COLUMN_RIG = "catch_rig";   // Új oszlop a rig számára
    private static final String COLUMN_ROD = "catch_rod";   // Új oszlop a rod számára

    public DataBaseController(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LAKE + " TEXT, " +
                COLUMN_WEIGHT + " INTEGER, " +
                COLUMN_BAIT + " TEXT, " +
                COLUMN_RIG + " TEXT, " +     // Új oszlop rig számára
                COLUMN_ROD + " TEXT);";      // Új oszlop rod számára
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Adatok hozzáadása
    public void addCatch(String lake, int weight, String bait, String rig, String rod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LAKE, lake);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_BAIT, bait);
        cv.put(COLUMN_RIG, rig);    // Új mező rig
        cv.put(COLUMN_ROD, rod);    // Új mező rod
        long result = db.insert(TABLE_NAME, null, cv);
        Log.d("DB_INSERT", "Insert result: " + result); // Hiba nyomkövetés
        if (result == -1) {
            Toast.makeText(context, "Failed to add catch", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Adatok olvasása
    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(query, null);
    }

    // Adatok frissítése
    public void updateData(String row_id, String lake, String weight, String bait, String rig, String rod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LAKE, lake);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_BAIT, bait);
        cv.put(COLUMN_RIG, rig);    // Rig frissítése
        cv.put(COLUMN_ROD, rod);    // Rod frissítése

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to update catch", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Egy sor törlése
    public void deleteOneRow(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete catch", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    // Minden adat törlése
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_NAME + "'");
        db.close();
    }
}
