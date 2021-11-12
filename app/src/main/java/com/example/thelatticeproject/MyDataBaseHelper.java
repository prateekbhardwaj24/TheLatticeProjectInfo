package com.example.thelatticeproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "abcdefgh.db";
    private static final String TABLE_NAME = "newtab";
    private static final String COl_1 = "ID";
    private static final String COL_2 = "TITLE";
    private static final String COL_3 = "READUNREAD";

    public MyDataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,READUNREAD TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean adddata(String title, String readUnread) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, title);
        cv.put(COL_3, readUnread);
        long res = db.insert(TABLE_NAME, null, cv);
        if (res == -1) {
            Toast.makeText(context, "failed to inert", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(context, "success inserted ", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = null;
        long NoOfRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        if (NoOfRows > 0) {
            res = db.rawQuery("Select * from " + TABLE_NAME, null);

        }
        return res;
    }
    public int getLastInertedId(){
        Integer id=null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_NAME, null);

        if(cursor.moveToLast()){
            //name = cursor.getString(column_index);//to get other values
            id = cursor.getInt(0);//to get id, 0 is the column index
        }
        return id;
    }

    public void updateData(String id, String readUnread) {
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

    public void deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "ID=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "failed to delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
