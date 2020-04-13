package com.example.xueliang.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.VillageBean;
import com.yan.tvprojectutils.AnimationHelper;
import com.yan.tvprojectutils.FocusRecyclerView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wbf
 */

public class NavCunListAdapter extends RecyclerView.Adapter<NavCunListAdapter.NavMovieHolder> {
    protected final Context context;
    private final List<VillageBean> stringList;

    public NavCunListAdapter(Context context, List<VillageBean> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_point_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        holder.tv_cun.setText(stringList.get(position).getvName());
        holder.pflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                holder.rv_point.setLayoutManager(mLayoutManager);
                holder.rv_point.setHasFixedSize(true);
                List<PointBean> points = stringList.get(position).getPoints();
                NavPointAdapter navCunListAdapter = new NavPointAdapter(context, points);
                holder.rv_point.setAdapter(navCunListAdapter);
            }
        });

        holder.pflContainer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AnimationHelper animationHelper = new AnimationHelper();
                animationHelper.setRatioX((v.getWidth() - dipToPx(context, 10)) / ((ViewGroup) v).getChildAt(0).getWidth());
                animationHelper.setRatioY((v.getHeight() - dipToPx(context, 10)) / ((ViewGroup) v).getChildAt(0).getHeight());
                if (hasFocus) {
                    animationHelper.starLargeAnimation(((ViewGroup) v).getChildAt(0));
                } else {
                    animationHelper.starSmallAnimation(((ViewGroup) v).getChildAt(0));
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
        public FocusRecyclerView rv_point;
        public TextView tv_cun;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.ll_cun_item) != null) {
                pflContainer = itemView.findViewById(R.id.ll_cun_item);
                tv_cun = itemView.findViewById(R.id.tv_cun);
                rv_point = itemView.findViewById(R.id.rv_point);
            }
        }

    }

}
