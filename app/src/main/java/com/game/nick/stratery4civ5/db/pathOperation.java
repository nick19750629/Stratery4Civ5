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
public class pathOperation {

    private static  String TAG = "pathOperation";
    private  static String table_name = "path";

    public static void createTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists ").append(table_name);
        sql.append("(_id integer primary key autoincrement,");
        sql.append("origin text,terminal text,status integer)");
        db.execSQL(sql.toString());
    }

    public static void insert(SQLiteDatabase db,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();

        values.put("origin",rec.get("o"));
        values.put("terminal",rec.get("t"));
        values.put("status", 0);

        Log.i(TAG,"insert path");
        db.insert(table_name,null,values);
    }

    public static void insertBatch(SQLiteDatabase db,HashMap<String,String> rec) {
        StringBuffer sqlStatement = new StringBuffer();

        sqlStatement.append("INSERT INTO ").append(table_name);
        sqlStatement.append(" (origin,terminal,status) VALUES");
        sqlStatement.append("(\"").append(rec.get("o"));
        sqlStatement.append("\",\"").append(rec.get("t"));
        sqlStatement.append("\",").append("0");
        sqlStatement.append(");");

        db.execSQL(sqlStatement.toString());
    }

    public static void updStatus(SQLiteDatabase db,int status,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();

        values = query(db,rec);

        ContentValues updValues = new ContentValues();
        updValues.put("status" ,status);

        String whereClause = "_id=?";
        String[] whereArgs = {rec.get("i")};
        db.update(table_name,updValues,whereClause,whereArgs);
    }

    public static List<HashMap<String,String>> queryAll(SQLiteDatabase db) {
        Cursor cursor = db.query(table_name,new String[]{"_id","origin","terminal",
                "status"},null,null,null,null,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("i",Integer.valueOf(cursor.getInt(0)).toString());
            word.put("o",cursor.getString(1));
            word.put("t",cursor.getString(2));
            word.put("s",Integer.valueOf(cursor.getInt(3)).toString());
            lstRet.add(word);
        }
        Log.i(TAG,"select *:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,String>> queryByOrigin(SQLiteDatabase db,String pOrigin) {
        String sqlStatement = "select _id,origin,terminal,status from " + table_name + " where origin = '"
                + pOrigin + "'";
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("i",Integer.valueOf(cursor.getInt(0)).toString());
            word.put("o",cursor.getString(1));
            word.put("t",cursor.getString(2));
            word.put("s",Integer.valueOf(cursor.getInt(3)).toString());
            lstRet.add(word);
        }
        Log.i(TAG,"select * by origin:"+lstRet.size());
        return lstRet;
    }

    public static List<HashMap<String,String>> queryByTerminal(SQLiteDatabase db,String pTerminal) {
        String sqlStatement = "select _id,origin,terminal,status from " + table_name + " where terminal = '"
                + pTerminal + "'";
        Cursor cursor = db.rawQuery(sqlStatement,null);
        List<HashMap<String,String>> lstRet = new ArrayList<HashMap<String,String>>();
        while (cursor.moveToNext()){
            HashMap<String,String> word = new HashMap<String,String>();
            word.put("i",Integer.valueOf(cursor.getInt(0)).toString());
            word.put("o",cursor.getString(1));
            word.put("t",cursor.getString(2));
            word.put("s",Integer.valueOf(cursor.getInt(3)).toString());
            lstRet.add(word);
        }
        Log.i(TAG,"select * by terminal:"+lstRet.size());
        return lstRet;
    }

    public static ContentValues query(SQLiteDatabase db,HashMap<String,String> rec) {
        ContentValues values = new ContentValues();
        Cursor cursor = db.query(table_name,new String[]{"_id","origin","terminal",
                "status"},"_id=?",new String[]{rec.get("i")},null,null,null);
        while (cursor.moveToNext()){
            values.put("_id",cursor.getInt(cursor.getColumnIndex("_id")));
            values.put("origin",cursor.getString(cursor.getColumnIndex("origin")));
            values.put("terminal",cursor.getString(cursor.getColumnIndex("terminal")));
            values.put("status",cursor.getInt(cursor.getColumnIndex("status")));
        }
        return values;
    }

    public static int deleteAll(SQLiteDatabase db) {
        int intRet = 0;

        intRet = db.delete(table_name,null,null);
        return intRet;
    }


}
