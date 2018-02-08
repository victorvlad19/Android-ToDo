package com.example.vves.workshop12;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vves on 13.09.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Inbox Items
    private static final String TABLE_NAME = "Inbox";
    private static final String COL0 = "ID";
    private static final String COL1 = "name";
    private static final String COL2 = "description";
    private static final String COL3 = "category";
    private static final String COL4 = "checkstate";
    private static final String COL5 = "date";
    private static final String COL6 = "path";
    private static final String COL7 = "bitmap";

    // Side Bar Items
    private static final String TABLE_NAME_ITEM = "item_table";
    private static final String COL0_ITEM = "ID_ITEM";
    private static final String COL1_ITEM = "name_item";
    private static final String COL2_ITEM = "imagepath";
    private static final String COL3_ITEM = "bage";
    private static final String COL4_ITEM = "category";
    private static final String COL5_ITEM = "folder_name";



    public DatabaseHelper(Context context) {
        super(context,TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (ID INTEGER PRIMARY KEY , " + COL1 + " TEXT , " + COL2 + " TEXT , " + COL3 + " INTEGER, " + COL4 + " INTEGER, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " BLOB) " ;
        String createTableItem = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ITEM + "(ID INTEGER PRIMARY KEY , " + COL1_ITEM + " TEXT,  " + COL2_ITEM + " INTEGER, " + COL3_ITEM + " INTEGER, " + COL4_ITEM + " INTEGER, " + COL5_ITEM + " TEXT )" ;
        db.execSQL(createTable);
        db.execSQL(createTableItem);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS" + TABLE_NAME);
        onCreate(db);
    }


    public void createTable (String group){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String createTable = "CREATE TABLE IF NOT EXISTS " + group2 + " (ID INTEGER PRIMARY KEY , " + COL1 + " TEXT , " + COL2 + " TEXT , " + COL3 + " INTEGER, " + COL4 + " INTEGER, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " BLOB) " ;
        db.execSQL(createTable);
    }

    public void removeTable (String group){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE  " + group2 );
        onCreate(db);
    }

    public void renameTable (String group, String newname){
        String group2 = group.replaceAll("\\s","");
        String newname2 = newname.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE " + group2 + " RENAME TO " + newname2 );
        onCreate(db);
    }

    public void renameRow (String newname, int position){
        String newname2 = newname.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1_ITEM, newname2);
        db.update(TABLE_NAME_ITEM, contentValues, "id="+position, null);

    }

    public void deleteDataID (String group) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("DB", group);
        String query = " DELETE FROM " + TABLE_NAME_ITEM + " WHERE " + COL1_ITEM + " = '" + group + "' ";
        db.execSQL(query);
    }

    public boolean addData (String group, String item, String item1, Integer categ, Integer check, String date, String path, byte[] image){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,item);
        contentValues.put(COL2,item1);
        contentValues.put(COL3,categ);
        contentValues.put(COL4,check);
        contentValues.put(COL5,date);
        contentValues.put(COL6,path);
        contentValues.put(COL7, image);

        long resault = db.insert(group2,null,contentValues);

        // If resault is correct
        if (resault == -1 ){
            return false;
        }
        else {

            return true;
        }
    }

    public boolean addDataItem (String name_item, Integer imagepath, Integer bage, Integer category, String folder_name){
        String group2 = folder_name.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1_ITEM, name_item);
        contentValues.put(COL2_ITEM, imagepath);
        contentValues.put(COL3_ITEM, bage);
        contentValues.put(COL4_ITEM, category);
        contentValues.put(COL5_ITEM, group2);

        long resault = db.insert(TABLE_NAME_ITEM,null,contentValues);

        // If resault is correct
        if (resault == -1 ){
            return false;
        }
        else {

            return true;
        }
    }

    public Cursor getData(String group){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + group2;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public void updateData (String group, int id, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String group2 = group.replaceAll("\\s","");
        String query = "UPDATE " + group2 + " SET "+ COL5 +" = '"+ date +"' WHERE ID = "+ id + " ";
        db.execSQL(query);
    }

    public Cursor getDataItem(String folder_name){
        String group2 = folder_name.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME_ITEM + " WHERE " + COL5_ITEM + " LIKE '%"+group2+"%'  ";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public void updateDataItem (String item_name, String item_folder) {
        String group2 = item_name.replaceAll("\\s","");
        String group3 = item_folder.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME_ITEM + " SET " + COL5_ITEM + " =  '"+ group3  + "' WHERE " + COL1_ITEM + "='"+group2+"' ";
        db.execSQL(query);
    }

    public void deleteData (String group, int id) {
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = " DELETE FROM " + group2 + " WHERE " + COL0 + " = ' " + id + " ' ";
        String query1 = "UPDATE " + group2 + " SET ID = ID - 1 WHERE ID > ' " + id + " ' ";
        db.execSQL(query);
        db.execSQL(query1);
    }

    public void swapData (String group, int startposition, int endposition){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String createtepmTable = "CREATE TEMPORARY TABLE my_temp AS SELECT * FROM " + group2 + " WHERE id = ' " + startposition + " ' " ;
        String delterow = "DELETE FROM " + group2 + " WHERE id = ' " + startposition + " ' ";
        String swap1 = "UPDATE " + group2 + " SET id = ' " + startposition + " ' WHERE id = ' " + endposition + " ' ";
        String swap2 = "UPDATE my_temp SET id = ' " +endposition + " ' WHERE id = ' " + startposition + " ' ";
        String addnew = "INSERT INTO " + group2 + " SELECT * FROM my_temp";
        String drop = "DROP TABLE my_temp";
        db.execSQL(createtepmTable);
        db.execSQL(delterow);
        db.execSQL(swap1);
        db.execSQL(swap2);
        db.execSQL(addnew);
        db.execSQL(drop);
    }

    public void makeTrue (String group, int position){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + group2 + " SET checkstate = 1 WHERE ID = ' " + position + " ' ";
        db.execSQL(query);
    }

    public void makeFalse (String group, int position){
        String group2 = group.replaceAll("\\s","");
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + group2 + " SET checkstate = 0 WHERE ID = ' " + position + " ' ";
        db.execSQL(query);
    }

}
