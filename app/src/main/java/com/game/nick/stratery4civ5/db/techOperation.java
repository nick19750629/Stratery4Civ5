package com.game.nick.stratery4civ5.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nick on 2017/09/29.
 */
public class techOperation {

    private static  String TAG = "techOperation";
    private  static String table_name = "tech";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(tech text primary key,status integer)");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,String tech) {
        ContentValues values = new ContentValues();

        values.put("tech",tech);
        values.put("status", 0);

        Log.i(TAG,"insert path");
        db.insert(table_name,null,values);
    }

    public static void insertBatch(SQLiteDatabase db,String tech) {
        StringBuffer sqlStatement = new StringBuffer();

        sqlStatement.append("INSERT INTO ").append(table_name);
        sqlStatement.append(" (tech,status) VALUES");
        sqlStatement.append("(\"").append(tech);
        sqlStatement.append("\",").append("0");
        sqlStatement.append(");");

        db.execSQL(sqlStatement.toString());
    }

    public static void updStatus(SQLiteDatabase db,int status,String tech) {
        ContentValues values = new ContentValues();

        values = query(db,tech);

        ContentValues updValues = new ContentValues();
        updValues.put("status" ,(Integer) status);

        String whereClause = "tech=?";
        String[] whereArgs = {tech};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static List<HashMap<String,String>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"tech","status"},null,null,null,null,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("t",cursor.getString(0));
            word.put("s",Integer.valueOf(cursor.getInt(1)).toString());
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,String>> queryByStatus(SQLiteDatabase db,int pStatus) {
        String sqlStatement = "select tech,status from " + table_name + " where status = "
                + pStatus;
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("t",cursor.getString(0));
            word.put("s",Integer.valueOf(cursor.getInt(1)).toString());
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static ContentValues query(SQLiteDatabase db,String tech) {
        ContentValues values = new ContentValues();
        Cursor cursor = db.query(table_name,new String[]{"tech",
                "status"},"tech=?",new String[]{tech},null,null,null);
        while (cursor.moveToNext()){
            values.put("tech",cursor.getString(cursor.getColumnIndex("tech")));
            values.put("status",cursor.getInt(cursor.getColumnIndex("status")));
        }
        Log.i(TAG,"select *:" + tech + "," + values.get("status"));
        return values;
    }

    public static int deleteAll(SQLiteDatabase db) {
        int intRet = 0;

        intRet = db.delete(table_name,null,null);
        return intRet;
    }


}
