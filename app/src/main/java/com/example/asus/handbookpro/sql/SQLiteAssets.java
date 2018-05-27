package com.example.asus.handbookpro.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by ASUS on 16.04.2018.
 */

public class SQLiteAssets extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "baseHandBook.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteAssets(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
    }
    public Cursor getInformation (SQLiteDatabase db, String tableName){
        String[] projection={"name", "email" , "phoneWork" , "phoneCell" , "bDay" , "job" , "department" , "company","image","rating"};
        Cursor cursor=db.query(tableName,projection,null,null,null,null,null);
        // Cursor cursor=db.query(tableName,projection,null,null,null,null,null,null,null,null);
        return cursor;
    }
    public Cursor  getListByKeyword(SQLiteDatabase db,String search,String tableName) {

        String selectQuery = "select * from "+tableName+" where LOWER(rating) LIKE '%"+search+"%'";


        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
      /*  if (cursor != null) {
           //v Log.d("XXXXXXXX", "cursor- " + cursor.getString(0));
        }
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }*/
        return cursor;


    }
}
