package com.example.xueliang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.bean.VillageBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wbf
 */

public class NavMonitorPageCunListAdapter extends BaseQuickAdapter<NavMonitorPageCunListAdapter.NavMovieHolder> {
    protected final Context context;
    private final List<VillageBean> stringList;

    public NavMonitorPageCunListAdapter(Context context,List<VillageBean> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_cun, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        VillageBean villageBean = stringList.get(position);
        holder.tv_cun_name.setText(villageBean.getName());
        holder.pflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemChildClickListener){
                    mOnItemChildClickListener.onItemChildClick(NavMonitorPageCunListAdapter.this,v,position);
                }
            }
        });

        //切换该村监视地点list
        holder.pflContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (null != mOnItemChildFocusChangeListener){
                    mOnItemChildFocusChangeListener.onFocusChange(NavMonitorPageCunListAdapter.this,v,hasFocus,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class NavMovieHolder extends RecyclerView.ViewHolder {
        public View pflContainer;
        public TextView tv_cun_name;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.ll_cun_item) != null) {
                pflContainer = itemView.findViewById(R.id.ll_cun_item);
                tv_cun_name = itemView.findViewById(R.id.tv_cun_name);
            }
        }
    }

}
