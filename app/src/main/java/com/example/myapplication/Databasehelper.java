package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Databasehelper extends SQLiteOpenHelper {

    static final String DBNAME = "notes";
    static final int DBVERSION = 1;

    public Databasehelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE notes(_id INTEGER PRIMARY KEY AUTOINCREMENT, description VARCHAR(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE notes");
        onCreate(db);
    }

    public List<NoteItem> getListNote() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notes", null, null);
        List<NoteItem> noteItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                NoteItem noteItem = new NoteItem();
                noteItem.setId(id);
                noteItem.setDescription(description);
                noteItems.add(noteItem);
                cursor.moveToNext();
            }
        }
        db.close();
        return noteItems;
    }

    public boolean updateNote(NoteItem noteItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", noteItem.getDescription());
        boolean isSuccess = db.update("notes", values, "_id=" + noteItem.getId(), null) > 0;
        db.close();
        return isSuccess;
    }

    public long addNote(NoteItem noteItem) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", noteItem.getDescription());
        //Add
        long rowId = db.insert("notes", null, values);
        db.close();
        return rowId;
    }

    public boolean deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        boolean success = db.delete("notes", "_id=" + id, null) > 0;
        db.close();
        return success;
    }
//
//    public String getID(String id) {
//        String selectQuery = "SELECT TYPE FROM " + TABLENAME + " where ID = " + id;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            db.close();
//            return cursor.getString(0);
//        }
//        return "";
//
//    }

}
