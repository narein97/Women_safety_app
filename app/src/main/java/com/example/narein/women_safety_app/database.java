package com.example.narein.women_safety_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by narein on 2/4/18.
 */

public class database extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private static final String DATABASE_NAME="contacts.db",TABLE="contacts";

    public database(Context context) {
        super(context, DATABASE_NAME, null, 4);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS CONTACTS (name text, phoneno text not null unique);");
    }

    public boolean isempty(){
        Cursor mcursor = db.rawQuery("select count(*) from contacts", null);
        mcursor.moveToFirst();int icount = mcursor.getInt(0);
        if(icount==0){return true;}else{return false;}
    }

    public boolean insertData(String name,String phoneno) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("phoneno",phoneno);
        long result = db.insert(TABLE,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE,null);
        return res;
    }

    public Integer deleteData (String phoneno) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, "phoneno = ?",new String[] {phoneno});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
