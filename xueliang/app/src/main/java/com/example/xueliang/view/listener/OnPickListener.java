package com.example.xueliang.view.listener;


import android.app.Dialog;

import com.example.xueliang.bean.PointBean;


public interface OnPickListener {
    void onPick(int position, PointBean data, Dialog dialog);
    void onCancel();
}
