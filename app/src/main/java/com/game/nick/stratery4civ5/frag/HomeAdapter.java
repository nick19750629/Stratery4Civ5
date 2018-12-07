package com.game.nick.stratery4civ5.frag;
/**
 * Created by nick on 2018/12/04.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


public abstract class HomeAdapter<T> extends RecyclerView.Adapter<HomeAdapter.VH>{

    private List<T> mDatas;
    private ButtonInterface buttonInterface;

    public HomeAdapter(List<T> datas){
        this.mDatas = datas;
    }

    public abstract int getLayoutId(int viewType);

    public void setDatas(List<T> pDatas) {
        mDatas = pDatas;
    }

    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface = buttonInterface;
    }

    public interface ButtonInterface{
        public void onclick( View view,int position);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.get(parent,getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        //Log.d("HomeAdapter","onBindViewHolder");
        convert(holder, mDatas.get(position), position);
        holder.mBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (buttonInterface!=null){
                    buttonInterface.onclick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(VH holder, T data, int position);

    static class VH extends RecyclerView.ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;
        Button mBtn;

        private VH(View v){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
        }


        public static VH get(ViewGroup parent, int layoutId){
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new VH(convertView);
        }

        public <T extends View> T getView(int id){
            View v = mViews.get(id);
            if(v == null){
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T)v;
        }

        public void setText(int id, String value){
            //Log.d("Adapter","setText");
            if (mBtn == null) {
                mBtn = getView(id);
            }
            //Log.d("Adapter","getid:"+value);
            mBtn.setText(value);
            //Log.d("Adapter","set text over");
        }

    }
}

