package com.game.nick.stratery4civ5.frag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import com.game.nick.stratery4civ5.R;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class HolyFragment extends Fragment {

    public HolyFragment() {
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
                holder.setText(R.id.id, (String)data.get("奇观"));
                StringBuffer content = new StringBuffer();
                content.append("产出:").append((String)data.get("产出")).append((String)data.get("产出评价"));
                content.append("\n位置:").append((String)data.get("位置")).append( (String)data.get("位置评价"));
                if (!"".equals((String)data.get("其他因素"))){
                    content.append("\n其他:").append((String)data.get("其他因素"));
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
                JSONArray array = root.getJSONArray("holy");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject lan = array.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("奇观", lan.getString("奇观"));
                    map.put("产出",lan.getString("产出"));
                    map.put("产出评价",lan.getString("产出评价"));
                    map.put("位置",lan.getString("位置"));
                    map.put("位置评价",lan.getString("位置评价"));
                    map.put("其他因素",lan.getString("其他因素"));
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
