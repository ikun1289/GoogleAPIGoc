package com.example.googleapi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.os.Build.ID;
import static android.provider.Contacts.SettingsColumns.KEY;
import static java.text.Collator.PRIMARY;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATA_NAME = "QuanAnDB.db";
    public static final String TABLE_NAME ="MonAn";
    public static final String COL_1 ="IDMonAn";
    public static final String COL_2 ="NAME";
    public static final String COL_3 ="PRICE";

    public DatabaseHelper(Context context){
        super(context, DATA_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + TABLE_NAME + "(IDMonAn INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PRICE TEXT)");
        //db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }

    public boolean insertDataMonAn(String name, String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues  = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,price);
        if(db.insert(TABLE_NAME,null,contentValues) == -1)
            return false;
        return true;
    }

    public Cursor getAllDataFromQuanAn(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + "QuanAn",null);
        return res;
    }
    public Cursor getAllDataFromMonAn(int IDQuanAn){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + "MonAn" + " WHERE IDQuanAn = " +IDQuanAn ,null);
        return res;
    }
}
