package com.example.xueliang.view.listener;

import android.view.View;

public class MyFocusChange implements View.OnFocusChangeListener {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            v.animate().scaleX(1.15f).scaleY(1.15f).start();
        } else {
            v.animate().scaleX(1f).scaleY(1f).start();
        }
    }
}
