package com.example.xueliang.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueliang.R;
import com.example.xueliang.base.LoadCallBack;
import com.example.xueliang.presenter.MainPresenter;
import com.example.xueliang.utils.DialogUtil;
import com.example.xueliang.utils.ScreenUtils;
import com.example.xueliang.view.readview.PageLoader;
import com.example.xueliang.view.readview.PageView;
import com.example.xueliang.view.readview.TxtChapter;

import static androidx.core.view.ViewCompat.LAYER_TYPE_SOFTWARE;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends BaseMvpActivity<MainPresenter> implements LoadCallBack {
    PageView mPvPage;
    public PageLoader mPageLoader;
    public TextView mLogout;
    public LinearLayout dot_ll;
    public FrameLayout fl_monitor;


    @Override
    public MainPresenter setPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    public void loadData() {
        presenter.processLogic();//调用接口
    }

    @Override
    public void initView() {
        mPvPage = findViewById(R.id.read_pv_page);
        mLogout = findViewById(R.id.tv_logout);
        fl_monitor = findViewById(R.id.fl_monitor);
        dot_ll = (LinearLayout) findViewById(R.id.dot_ll);
        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mPageLoader = mPvPage.getPageLoader();
    }

    @Override
    public void initListener() {
        TxtChapter txtChapter = new TxtChapter();
        txtChapter.setBody("    为贯彻落实《中共中央国务院关于坚持农业农村优先发展好“三农”工作的若干意见》《中共中央办公厅 国务院办公厅关于加强和改进乡村治理的指导见》的求，中央农办、农业农村部、中央宣传部、民政部、司法部等部门从2019年起组织开展乡村治理示范村镇创建活动，通过示范创建活动推动健全党组织领导的自治、法治、德治相结合的乡村治理体系，培育和树立一批乡村治理典型，发挥其引领示范和辐射带动作用，进一步促进乡村治理体系和治理能力现代化。现将有关事项通知如下。为贯彻落实《中共中央 国务院关于坚持农业农村优先发展做好“三农”" +
                "工作的若干意见》《中共中央办公厅 国务院办公厅关于加强和改进乡村治理的指导意见》的要求，中央农办、农业农村部、" +
                "中央宣传部、民政部、司法部等部门从2019年起组织开展乡村治理示范村镇创建活动，通过示范创建活动推动健全党组织领导的自治、" +
                "法治、德治相结合的乡村治理体系，培育和树立一批乡村治理典型，发挥其引领示范和辐射带动作用，进一步促进乡村治理体系和治理能力现代化。现将有关事项通知如下。现将有关事项通知如下。现将有关事项通知如下。现将有关事项通知如下。现将有关事项通知如下。" +
                "现将有关事项通知如下。现将有关事项通知如下。现将有关事项通知如下。现将有关事项通知如下。将有关事项通知如下。将有关事项通知如下。将有关事项通知如下。将有关事项通知如下。");
        mPageLoader.setChapter(txtChapter);

        mPageLoader.setPageStyle(); //设置默认颜色
        mPvPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public boolean onTouch() {
                return true;
            }

            @Override
            public void prePage() {
            }

            @Override
            public void nextPage() {
            }

            @Override
            public void cancel() {
            }
        });

        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onPageCountChange(int count) {
                mPvPage.post(new Runnable() {
                    @Override
                    public void run() {
                        initDots(count);
                    }
                });

            }

            @Override
            public void onPageChange(int pos) {
                for (int i = 0; i < dot_ll.getChildCount(); i++) {
                    dot_ll.getChildAt(i).setBackgroundResource(pos == i ? R.drawable.icon_dot_focused : R.drawable.icon_dot_normal);
                }
            }
        });

        mLogout.setOnClickListener(v->{
            //退出登录
            DialogUtil.showAlert(mContext, null, "您确认要退出吗？",
                    "确 定", (dialog, which) -> {
                        dialog.dismiss();
                    }, "取 消", (dialog, which) -> {
                        dialog.dismiss();
                    }, false);
        });

        mPvPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPageLoader.parseCurChapter();
                new Handler().postDelayed(runnable,5000);
            }
        },1000);

        fl_monitor.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setClass(this, MonitorListActivity.class);
            startActivity(intent);
        });
    }

    /**
     * 初始化指示小圆点
     */
    private void initDots(int num) {
        dot_ll.removeAllViews();
        for (int i = 0; i < num; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ScreenUtils.dpToPx(8),ScreenUtils.dpToPx(8));
            params.leftMargin = ScreenUtils.dpToPx(3);
            params.rightMargin = ScreenUtils.dpToPx(3);
            if (i == 0) {
                dot.setBackgroundResource(R.drawable.icon_dot_focused);
            } else {
                dot.setBackgroundResource(R.drawable.icon_dot_normal);
            }
            dot_ll.addView(dot, params);
        }
    }


    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if ( mPvPage.hasNextPage()){
                mPageLoader.skipToNextPage();
            }else {
                mPageLoader.skipToPage(0);
            }

            new Handler().postDelayed(runnable,5000);
        }
    };

    @Override
    public void onLoad(Object data) {

    }

    @Override
    public void onLoadFail(String message) {

    }
}
