package com.example.xueliang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.bean.PointBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wbf
 */

public class NavMonitorPagePointListAdapter extends BaseQuickAdapter<NavMonitorPagePointListAdapter.NavMovieHolder> {
    protected final Context context;
    private final List<PointBean> stringList;

    public NavMonitorPagePointListAdapter(Context context, List<PointBean> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_monitor, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        PointBean pointBean = stringList.get(position);
        holder.tv_point_name.setText(pointBean.getLocation());
        holder.pflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换视频 播放
                if (null != mOnItemChildClickListener) {
                    mOnItemChildClickListener.onItemChildClick(NavMonitorPagePointListAdapter.this, v, position);
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
        public TextView tv_point_name;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.ll_monitor_item) != null) {
                pflContainer = itemView.findViewById(R.id.ll_monitor_item);
                tv_point_name = itemView.findViewById(R.id.tv_point_name);
            }
        }

    }

}
