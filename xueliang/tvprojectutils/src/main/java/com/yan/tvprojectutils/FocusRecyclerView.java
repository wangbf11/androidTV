package com.yan.tvprojectutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by wbf
 */
public class FocusRecyclerView extends RecyclerView {

    public FocusRecyclerView(Context context) {
        this(context, null);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FocusRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
