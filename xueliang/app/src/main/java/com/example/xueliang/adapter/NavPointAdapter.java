package com.example.xueliang.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueliang.R;
import com.example.xueliang.bean.PointBean;
import com.yan.tvprojectutils.AnimationHelper;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wbf
 */

public class NavPointAdapter extends RecyclerView.Adapter<NavPointAdapter.NavMovieHolder> {
    protected final Context context;
    private final List<PointBean> stringList;

    public NavPointAdapter(Context context, List<PointBean> objectList) {
        this.stringList = objectList;
        this.context = context;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_point, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        holder.tv_point.setText(stringList.get(position).getpName());
        holder.pflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you just touched me", Toast.LENGTH_LONG).show();
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
        public TextView tv_point;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.ll_point_item) != null) {
                pflContainer = itemView.findViewById(R.id.ll_point_item);
                tv_point = itemView.findViewById(R.id.tv_point);
            }
        }

    }

}