package com.example.thelatticeproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class dbdb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TheLatticeProject.db";
    public static final String TABLE_NAME = "AllToDoList";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "TITLE";
    public static final String COL_3 = "READUNREAD";
    private Context context;

  dbdb(Context context) {
        super(context, DATABASE_NAME,null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,READUNREAD TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

    }
    public boolean insertData(String Title, String readUnread) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Title);
        contentValues.put(COL_3, readUnread);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
//    public Cursor getAllData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = null;
//        long NoOfRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
//        if (NoOfRows > 0) {
//            res = db.rawQuery("Select * from " + TABLE_NAME, null);
//
//        }
//        return res;
//    }
    public void deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "ID=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateData(String id,String readUnread) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_3, readUnread);
        long result = db.update(TABLE_NAME, cv, "ID=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "failed to update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "update successful", Toast.LENGTH_SHORT).show();
        }
    }

}
