package com.example.xueliang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.activity.MonitorActivity;
import com.example.xueliang.bean.PointBean;
import com.example.xueliang.manager.VideoManager;
import com.example.xueliang.utils.AppUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.widget.IjkVideoView;

/**
 * Created by wbf
 */

public class NavGridMonitorAdapter extends RecyclerView.Adapter<NavGridMonitorAdapter.NavMovieHolder> {
    protected final Context context;
    private final List<PointBean> stringList;
    private boolean isOne;
    private FrameLayout mVvPlayerContainer;

    public NavGridMonitorAdapter(Context context, List<PointBean> objectList, boolean isOne) {
        this.stringList = objectList;
        this.context = context;
        this.isOne = isOne;
    }

    @Override
    public NavMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NavMovieHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_grid_monitor, parent, false));
    }

    @Override
    public void onBindViewHolder(NavMovieHolder holder, int position) {
        PointBean pointBean = stringList.get(position);
        mVvPlayerContainer = holder.point_surfaceView;
        View pflContainer = holder.pflContainer;
        ViewGroup.LayoutParams layoutParams = pflContainer.getLayoutParams();
        if (isOne) {
            //1分频使高度
            layoutParams.height = (AppUtils.getScreenHeight() - AppUtils.dip2px(32));
        } else {
            //4分频使高度
            layoutParams.height = (AppUtils.getScreenHeight() - AppUtils.dip2px(32)) / 2;
        }


        if (pointBean == null){
            //站位用的
            mVvPlayerContainer.setVisibility(View.GONE);
            return;
        }
        mVvPlayerContainer.setVisibility(View.VISIBLE);

        VideoManager instance = VideoManager.getInstance();
        IjkVideoView vvPlayer = instance.getVideoViewArrayList().get(position);
        ViewParent parent = vvPlayer.getParent();
        if (null != parent){
            ((ViewGroup)parent).removeAllViews();
        }
        mVvPlayerContainer.addView(vvPlayer);

        String location = pointBean.getLocation();
        String town = pointBean.getTown();
        String village = pointBean.getVillage();
        holder.point_name.setText(town + " " + village + " " + location);
        holder.point_time.setText(stringList.get(position).getEquipment_num());
        vvPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pointBean == null){
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(context, MonitorActivity.class);
                intent.putExtra(MonitorActivity.POINT_BEAN, pointBean);
                context.startActivity(intent);
            }
        });

        vvPlayer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    holder.pflContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_boder));
                } else {
                    holder.pflContainer.setBackground(context.getResources().getDrawable(R.drawable.bg_white_boder));
                }
            }

        });

        String url = pointBean.getRtmpSrc();
//        url = "rtmp://58.200.131.2:1935/livetv/hunantv"; //测试代码
        if (url.equals(vvPlayer.getVideoPath())){
            vvPlayer.resume();
            vvPlayer.start();
        }else {
            vvPlayer.setVideoPath(url);
            vvPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener()  {

                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    vvPlayer.start();
                }
            });


            vvPlayer.setOnErrorListener((mp, what, extra) -> {
                // 缓存有问题 先删除 缓存
                vvPlayer.stopPlayback();
                return true;
            });
        }

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }


    public class NavMovieHolder extends RecyclerView.ViewHolder {
        public View pflContainer;
        public TextView point_time;
        public TextView point_name;
        public View point_top;
        public FrameLayout point_surfaceView;

        public NavMovieHolder(View itemView) {
            super(itemView);
            if (itemView.findViewById(R.id.cl_grid) != null) {
                pflContainer = itemView.findViewById(R.id.cl_grid);
                point_time = itemView.findViewById(R.id.point_time);
                point_name = itemView.findViewById(R.id.point_name);
                point_top = itemView.findViewById(R.id.point_top);
                point_surfaceView = itemView.findViewById(R.id.point_surfaceView);
            }
        }

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NavMovieHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (null != mVvPlayerContainer){
            mVvPlayerContainer.removeAllViews();
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
