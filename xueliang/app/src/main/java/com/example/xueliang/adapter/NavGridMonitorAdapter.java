package com.example.xueliang.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xueliang.R;
import com.example.xueliang.activity.MonitorActivity;
import com.example.xueliang.utils.AppUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wbf
 */

public class NavGridMonitorAdapter extends RecyclerView.Adapter<NavGridMonitorAdapter.NavMovieHolder> {
    protected final Context context;
    private final List<String> stringList;

    public NavGridMonitorAdapter(Context context, List<String> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_grid_monitor, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        holder.pflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, MonitorActivity.class);
                context.startActivity(intent);
            }
        });

        View pflContainer = holder.pflContainer;
        ViewGroup.LayoutParams layoutParams = pflContainer.getLayoutParams();
        if (stringList.size() == 1){
            layoutParams.height = (AppUtils.getScreenHeight() - AppUtils.dip2px(32));
        }else {
            layoutParams.height = (AppUtils.getScreenHeight() - AppUtils.dip2px(32))/2;
        }
        holder.pflContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackground(context.getResources().getDrawable(R.drawable.bg_boder));
                } else {
                    v.setBackground(null);
                }
            }

        });
    }

    private float dipToPx(Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }



    public class NavMovieHolder extends RecyclerView.ViewHolder {
        public View pflContainer;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.cl_grid) != null) {
                pflContainer = itemView.findViewById(R.id.cl_grid);
            }
        }

    }

}
