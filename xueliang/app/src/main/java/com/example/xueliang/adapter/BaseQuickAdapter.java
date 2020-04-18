package com.example.xueliang.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseQuickAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected OnItemChildFocusChangeListener mOnItemChildFocusChangeListener;
    protected OnItemChildClickListener mOnItemChildClickListener;

    public void setOnItemChildFocusChangeListener(OnItemChildFocusChangeListener l) {
        this.mOnItemChildFocusChangeListener = l;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener l) {
        this.mOnItemChildClickListener = l;
    }

    public interface OnItemChildFocusChangeListener {
        void onFocusChange(BaseQuickAdapter adapter,View v, boolean hasFocus, int position);
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BaseQuickAdapter adapter, View view, int position);
    }
}
