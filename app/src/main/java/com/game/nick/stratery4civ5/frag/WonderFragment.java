package com.game.nick.stratery4civ5.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.game.nick.stratery4civ5.R;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class WonderFragment extends Fragment {

    public WonderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_holy_list, container, false);
        List<HashMap> data = initData();
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.list);

        rv.setAdapter(new QuickAdapter<HashMap>(data) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.fragment_holy;
            }

            @Override
            public void convert(VH holder,HashMap  data, int position) {
                holder.setText(R.id.id, (String)data.get("奇迹"));
                StringBuffer content = new StringBuffer();
                content.append("科技：").append( (String)data.get("科技"));
                if (!"".equals((String)data.get("锤"))) {
                    content.append("\t").append( (String)data.get("锤")).append("锤,");
                }else{
                    content.append("\t").append("????锤,");
                }
                if (!"".equals((String)data.get("难度"))) {
                    content.append("\t难度指数:").append((String)data.get("难度")).append(",");
                }else{
                    content.append("\t难度指数:").append("????,");
                }
                if (!"".equals((String)data.get("效果"))) {
                    content.append("\t效果指数:").append((String)data.get("效果")).append(",");
                }else{
                    content.append("\t效果指数:").append("????,");
                }
                if (!"".equals((String)data.get("推荐"))) {
                    content.append("\t推荐指数:").append((String)data.get("推荐"));
                }else{
                    content.append("\t推荐指数:").append("????");
                }
                if (!"".equals((String)data.get("备注"))) {
                    content.append("\n备注：").append((String)data.get("备注"));
                }
                holder.setText(R.id.content,content.toString());
            }

        });

        return v;
    }

    private List<HashMap> initData() {
        List<HashMap> mData = new ArrayList<HashMap>();

        try{
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/" + "holy.json");
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
                JSONArray array = root.getJSONArray("wonder");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject lan = array.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("奇迹", lan.getString("奇迹"));
                    map.put("科技",lan.getString("科技"));
                    map.put("锤",lan.getString("锤"));
                    map.put("难度",lan.getString("难度"));
                    map.put("效果",lan.getString("效果"));
                    map.put("推荐",lan.getString("推荐"));
                    map.put("备注",lan.getString("备注"));
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
}
