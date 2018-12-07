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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PolicyFragment extends Fragment {

    public PolicyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_policy_list, container, false);
        List<String> data = initData();
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.plist);

        rv.setAdapter(new QuickAdapter<String>(data) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.fragment_policy;
            }

            @Override
            public void convert(VH holder,String  data, int pos) {
                //Log.d("data",data);
                holder.setText(R.id.policytxt, data);
                //Log.d("policyfragment","setText over");
            }

        });

        return v;
    }

    private List<String> initData() {
        List<String> mData = new ArrayList<String>();

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
                JSONArray array = root.getJSONArray("policy");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject lan = array.getJSONObject(i);
                    mData.add(lan.getString("item"));
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
