package com.game.nick.stratery4civ5.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.game.nick.stratery4civ5.R;
import com.game.nick.stratery4civ5.db.DatabaseHelper;
import com.game.nick.stratery4civ5.db.pathOperation;
import com.game.nick.stratery4civ5.db.techOperation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

/**
 * A placeholder fragment containing a simple view.
 */
public class ToolFragment extends Fragment {

    public ToolFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tool, container, false);

        Button btn = (Button)v.findViewById(R.id.btnReset);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper= new DatabaseHelper(v.getContext(),"civ");

                int cnt1 = procTech(dbHelper.getWritableDatabase());
                int cnt = procPath(dbHelper.getWritableDatabase());
                preProcess(dbHelper.getWritableDatabase());

                Toast.makeText(v.getContext(),cnt1 + "/" + cnt + "件数据重置完成",Toast.LENGTH_LONG).show();
                //Log.d("ToolFragment","On Click");
            }

            private int procTech(SQLiteDatabase db) {

                techOperation.createTable(db);
                techOperation.deleteAll(db);
                String[] techs = loadTechs().split(",");
                for (int i = 0;i < techs.length; i++){
                    techOperation.insertBatch(db,techs[i]);
                }
                return techs.length;
            }

            private int procPath(SQLiteDatabase db) {
                pathOperation.createTable(db);
                pathOperation.deleteAll(db);
                List<HashMap> data = loadPaths();
                for (int i = 0;i < data.size(); i++){
                    HashMap<String, String> rec = data.get(i);
                    pathOperation.insertBatch(db,rec);
                }
                return data.size();
            }

            private void preProcess(SQLiteDatabase db){
                String tech = "农业";
                techOperation.updStatus(db,9,tech); //自动取得
                List<HashMap<String,String>> result = pathOperation.queryByOrigin(db,tech);
                for (int i = 0; i < result.size(); i++){
                    HashMap<String,String> map = result.get(i);
                    //Log.d("preProcess","term:"+map.get("t"));
                    //所有节点状态更新为1
                    pathOperation.updStatus(db,1,map);
                    techOperation.updStatus(db,1,map.get("t"));
                }
            }

            private List<HashMap> loadPaths() {
                List<HashMap> mData = new ArrayList<HashMap>();

                try{
                    InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "tech.json");
                    BufferedReader buf = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = buf.readLine()) != null) {
                        builder.append(line);
                    }
                    is.close();
                    buf.close();

                    try {
                        JSONObject root = new JSONObject(builder.toString());
                        JSONArray array = root.getJSONArray("path");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject lan = array.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("o", lan.getString("起点"));
                            map.put("t",lan.getString("终点"));
                            mData.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return mData;
            }

            private String loadTechs() {
                String techs = "";

                try{
                    InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "tech.json");
                    BufferedReader buf = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = buf.readLine()) != null) {
                        builder.append(line);
                    }
                    is.close();
                    buf.close();

                    try {
                        JSONObject root = new JSONObject(builder.toString());
                        techs = root.getString("tech");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    return techs;
                }
            }
        });
        return v;
    }


}
