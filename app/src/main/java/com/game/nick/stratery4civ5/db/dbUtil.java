package com.game.nick.stratery4civ5.db;

import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.util.Date;
import java.util.HashMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by nick on 2017/09/29.
 */

public class dbUtil {
    public static long transDbDateType(Object dt) {
        Date dat = (Date)dt;
        return dat.getTime();
    }

    public static Date transDateType(long time){
        Date ret = new Date();
        ret.setTime(time);
        return ret;
    }

    public static void ExportToCSV(Cursor c, String path, String fileName) {
        int rowCount = 0;
        int colCount = 0;
        FileWriter fw;
        BufferedWriter bfw;


        /*
        //获取sd卡根目录
        File sdCardDir = Environment.getExternalStorageDirectory();
        //保存文件目录
        File saveFile = new File(sdCardDir, fileName);
        */

        try {

            rowCount = c.getCount();
            colCount = c.getColumnCount();
            //fw = new FileWriter(saveFile);
            //Log.i("FILE PATH",path);

            fw = new FileWriter(path + "/" + fileName);
            bfw = new BufferedWriter(fw);
            if (rowCount > 0) {
                c.moveToFirst();
                // 写入表头
                for (int i = 0; i < colCount; i++) {
                    if (i != colCount - 1)
                        bfw.write(c.getColumnName(i) + ',');
                    else
                        bfw.write(c.getColumnName(i));
                }
                // 写好表头后换行
                bfw.newLine();
                // 写入数据
                for (int i = 0; i < rowCount; i++) {
                    c.moveToPosition(i);
                    Log.v("导出数据", "正在导出第" + (i + 1) + "条");
                    for (int j = 0; j < colCount; j++) {
                        if (j != colCount - 1)
                            bfw.write(c.getString(j) + ',');
                        else
                            bfw.write(c.getString(j));
                    }
                    // 写好每条记录后换行
                    bfw.newLine();
                }
            }
            // 将缓存数据写入文件
            bfw.flush();
            // 释放缓存
            bfw.close();
            Log.v("导出数据", "导出完毕！" + path + "/" + fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("导出数据","Exception" + e.getMessage());
            e.printStackTrace();
        } finally {
            c.close();
        }
    }
}
