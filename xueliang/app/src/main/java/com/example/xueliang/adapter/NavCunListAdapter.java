package com.example.xueliang.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.bean.VillageBean;
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
                .inflate(R.layout.item_cun_list, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        holder.tv_cun.setText(stringList.get(position).getvName());
        holder.pflContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClick( holder.iv_arrow,holder.rv_point);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                holder.rv_point.setLayoutManager(mLayoutManager);
                holder.rv_point.setHasFixedSize(true);
                List<PointBean> points = stringList.get(position).getPoints();
                NavPointListAdapter navCunListAdapter = new NavPointListAdapter(context, points);
                holder.rv_point.setAdapter(navCunListAdapter);
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
        public ImageView iv_arrow;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.ll_cun_item) != null) {
                pflContainer = itemView.findViewById(R.id.ll_cun_item);
                tv_cun = itemView.findViewById(R.id.tv_cun);
                rv_point = itemView.findViewById(R.id.rv_point);
                iv_arrow = itemView.findViewById(R.id.iv_arrow);
            }
        }

    }


    /**
     * 点击了展开详情
     */
    private void openClick(ImageView down, FocusRecyclerView tv_detail) {
        if (tv_detail.getVisibility() == View.GONE){
            startMoreAnima(true,down,tv_detail);
        }else {
            startMoreAnima(false,down,tv_detail);
        }
    }


    /**
     * 箭头旋转动画
     */
    private void startMoreAnima(final boolean isOpen, ImageView down, final FocusRecyclerView tv_detail) {
        RotateAnimation ta;
        if (!isOpen) {
            ta = new RotateAnimation(90, 0, down.getWidth() / 2, down.getHeight() / 2);
        } else {
            ta = new RotateAnimation(0, 90, down.getWidth() / 2, down.getHeight() / 2);
        }
        // 设置动画播放的时间
        ta.setDuration(300);
        ta.setFillAfter(!ta.getFillAfter());//每次都取相反值，
        // 开始播放动画
        down.startAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isOpen){
                    tv_detail.setVisibility(View.VISIBLE);
                }else {
                    tv_detail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
