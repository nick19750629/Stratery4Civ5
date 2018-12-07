package com.game.nick.stratery4civ5.frag;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.game.nick.stratery4civ5.R;
import com.game.nick.stratery4civ5.db.DatabaseHelper;
import com.game.nick.stratery4civ5.db.pathOperation;
import com.game.nick.stratery4civ5.db.techOperation;

import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TechFragment extends Fragment {

    private HomeAdapter mAdapter;
    private List<HashMap<String, String>> data;
    private RecyclerView rv;

    public TechFragment() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            //Log.d("onHiddenChanged","refresh");
            data = initData();
            if (mAdapter != null){
                mAdapter.setDatas(data);
                //Log.d("onHiddenChanged","refresh");
                mAdapter.notifyDataSetChanged();
            }else{
                createAdapter();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tech_list, container, false);
        data = initData();
        rv = (RecyclerView) v.findViewById(R.id.tlist);

        if (mAdapter != null){
            //Log.d("on create","refresh");
            mAdapter.notifyDataSetChanged();
        }else{
            createAdapter();
        }

        rv.setAdapter(mAdapter);
        mAdapter.buttonSetOnclick(new HomeAdapter.ButtonInterface() {
            @Override
            public void onclick(View view, int position) {
                String tech = data.get(position).get("t");
                DatabaseHelper dbHelper= new DatabaseHelper(view.getContext(),"civ");
                process(dbHelper.getWritableDatabase(),tech);
                data = initData();
                if (mAdapter != null){
                    mAdapter.setDatas(data);
                    //Log.d("buttonSetOnclick","refresh");

                    mAdapter.notifyDataSetChanged();
                }else{
                    createAdapter();
                }

                Toast.makeText(view.getContext(),"点击：" + tech,Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }

    private List<HashMap<String, String>> initData() {
        DatabaseHelper dbHelper= new DatabaseHelper(this.getContext(),"civ");
        List<HashMap<String, String>> mData = techOperation.queryByStatus(dbHelper.getReadableDatabase(),1);
        return mData;
    }

    private void createAdapter(){
        mAdapter = new HomeAdapter<HashMap<String, String>>(data){
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.fragment_tech;
            }

            @Override
            public void convert(VH holder,HashMap<String, String>  data, int position) {
                holder.setText(R.id.btnTech,data.get("t"));
            }

        };
    }

    private void process(SQLiteDatabase db,String tech){
        //当前技术设为已完成（9）
        techOperation.updStatus(db,9,tech);
        //关联技术查询（以当前技术为起点）
        List<HashMap<String,String>> result = pathOperation.queryByOrigin(db,tech);
        // 遍历关联技术
        for (int i = 0; i < result.size(); i++){
            HashMap<String,String> map = result.get(i);
            Log.d("preProcess","term:"+map.get("t"));
            //所有节点状态更新为1
            pathOperation.updStatus(db,1,map);
            if (parseCondition(db,map.get("t"))) {
                //如果前提技术都完成，则关联技术设为可用（1）
                techOperation.updStatus(db,1,map.get("t"));
            }
        }
        //以当前技术为终点的路径关闭
        closePath(db,tech);
    }

    private boolean parseCondition(SQLiteDatabase db,String tech) {
        boolean ret = true;
        Log.d("parseCondition",tech);
        //关联技术可用前提查询（以关联技术为终点）
        List<HashMap<String,String>> result  = pathOperation.queryByTerminal(db,tech);
        for (int i = 0;i < result.size(); i++){
            //遍历前提技术
            HashMap<String,String> map = result.get(i);
            ContentValues values = techOperation.query(db, map.get("o"));
            if (((Integer)values.get("status")).intValue() <= 1) {
                Log.d("parseCondition","return false");
                return false;
            }
        }
        Log.d("parseCondition","return " + ret);
        return ret;
    }

    private void closePath(SQLiteDatabase db,String tech){
        //以当前技术为终点的路径关闭
        List<HashMap<String,String>> result  = pathOperation.queryByTerminal(db,tech);
        for (int i = 0;i < result.size(); i++) {
            pathOperation.updStatus(db, 9, result.get(i));
        }
    }

}
