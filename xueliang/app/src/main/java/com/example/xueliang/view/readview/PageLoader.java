package com.example.xueliang.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.example.xueliang.R;
import com.example.xueliang.utils.IOUtils;
import com.example.xueliang.utils.ScreenUtils;
import com.example.xueliang.utils.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbf
 */

public abstract class PageLoader {
    private static final String TAG = "PageLoader";

    // 当前页面的状态
    public static final int STATUS_LOADING = 1;         // 正在加载
    public static final int STATUS_FINISH = 2;          // 加载完成
    public static final int STATUS_ERROR = 3;           // 加载错误 (一般是网络加载情况)
    public static final int STATUS_EMPTY = 4;           // 空数据
    public static final int STATUS_PARING = 5;          // 正在解析 (装载本地数据)
    public static final int STATUS_PARSE_ERROR = 6;     // 本地文件解析错误(暂未被使用)
    public static final int STATUS_CATEGORY_EMPTY = 7;  // 获取到的目录为空
    // 默认的显示参数配置
    private static final int DEFAULT_MARGIN_HEIGHT = 0;
    private static final int DEFAULT_MARGIN_WIDTH = 3;
    private static final int DEFAULT_TIP_SIZE = 12;
    private static final int EXTRA_TITLE_SIZE = 4;
    // 监听器
    protected OnPageChangeListener mPageChangeListener;

    private Context mContext;
    // 页面显示类
    private PageView mPageView;
    // 当前显示的页
    public TxtPage mCurPage;
    // 上一章的页面列表缓存
    private List<TxtPage> mPrePageList;
    // 当前章节的页面列表
    private List<TxtPage> mCurPageList;

    // 绘制电池的画笔
    private Paint mBatteryPaint;
    // 绘制提示的画笔
    private Paint mTipPaint;
    // 绘制标题的画笔
    private Paint mTitlePaint;
    // 绘制背景颜色的画笔(用来擦除需要重绘的部分)
    private Paint mBgPaint;
    // 绘制小说内容的画笔
    private TextPaint mTextPaint;
    // 被遮盖的页，或者认为被取消显示的页
    private TxtPage mCancelPage;

    /*****************params**************************/
    // 当前的状态
    protected int mStatus = STATUS_LOADING;
    private boolean isClose;
    // 页面的翻页效果模式
    private PageMode mPageMode = PageMode.SLIDE;
    //书籍绘制区域的宽高
    private int mVisibleWidth;
    private int mVisibleHeight;
    //应用的宽高
    private int mDisplayWidth;
    private int mDisplayHeight;
    //间距
    private int mMarginWidth;
    private int mMarginHeight;
    //字体的颜色
    private int mTextColor;
    //标题的大小
    private int mTitleSize;
    //字体的大小
    private int mTextSize;
    //行间距
    private int mTextInterval;
    //标题的行间距
    private int mTitleInterval;
    //段落距离(基于行间距的额外距离)
    private int mTextPara;
    private int mTitlePara;
    //电池的百分比
    private int mBatteryLevel;
    //当前页面的背景
    private int mBgColor;

    // 当前章
    protected int mCurChapterPos = 0;
    //上一章的记录
    private int mLastChapterPos = 0;

    private int mHangSize; //行间距
    private int mParaSize; //段落间距
    public Bitmap mBgBitmap; //背景图片
    private TxtChapter mChapter; //播放公告栏的 文字对象

    public TxtChapter getChapter() {
        return mChapter;
    }

    public void setChapter(TxtChapter chapter) {
        mChapter = chapter;
    }

    /*****************************init params*******************************/
    public PageLoader(PageView pageView) {
        mPageView = pageView;
        mContext = pageView.getContext();

        // 初始化数据
        initData();
        // 初始化画笔
        initPaint();
        // 初始化PageView
        initPageView();
    }

    private void initData() {
        // 获取配置管理器
        // 获取配置参数
        // 初始化参数
        mMarginWidth = ScreenUtils.dpToPx(DEFAULT_MARGIN_WIDTH);
        mMarginHeight = ScreenUtils.dpToPx(DEFAULT_MARGIN_HEIGHT);
        // 配置文字有关的参数
        setUpTextParams( ScreenUtils.spToPx(12));
    }

    /**
     * 作用：设置与文字相关的参数
     *
     * @param textSize
     */
    private void setUpTextParams(int textSize) {
        // 文字大小
        mTextSize = textSize;
        // 行间距(大小为字体的一半)  默认 字体变大 行也要变大
        mTextInterval = mTextSize / 2;
        mTitleInterval = mTitleSize / 2;
        // 段落间距(大小为字体的高度)  默认  字体变大 段落也要变大
        mTextPara = mTextSize;
        mTitlePara = mTitleSize;
    }

    /**
     * 设置文字行间距
     */
    public void setHangSize(int hangSize) {
        mHangSize = hangSize;
    }

    /**
     * 设置文字段落间距
     */
    public void setParaSize(int paraSize) {
        mParaSize = paraSize;
    }

    /**
     * 方法说明：设置字体
     */
    public void setFont(String ttf) {
        Typeface typeface;
        if (!ttf.equals("null")) {
            typeface = Typeface.createFromAsset(mContext.getAssets(), ttf);
        } else {
            typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
        }
        mTitlePaint.setTypeface(typeface);
        mTextPaint.setTypeface(typeface);
    }


    private void initPaint() {
        // 绘制提示的画笔
        mTipPaint = new Paint();
        mTipPaint.setColor(mTextColor);
        mTipPaint.setTextAlign(Paint.Align.LEFT); // 绘制的起始点
        mTipPaint.setTextSize(ScreenUtils.spToPx(DEFAULT_TIP_SIZE)); // Tip默认的字体大小
        mTipPaint.setAntiAlias(true);
        mTipPaint.setSubpixelText(true);

        // 绘制页面内容的画笔
        mTextPaint = new TextPaint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        // 绘制标题的画笔
        mTitlePaint = new TextPaint();
        mTitlePaint.setColor(mTextColor);
        mTitlePaint.setTextSize(mTitleSize);
        mTitlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTitlePaint.setAntiAlias(true);

        // 绘制背景的画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);

        // 绘制电池的画笔
        mBatteryPaint = new Paint();
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setDither(true);


        // 初始化字体样式
        setFont("null");
    }

    private void initPageView() {
        //配置参数
        mPageView.setPageMode(mPageMode);
        mPageView.setBgColor(mBgColor);
    }

    /****************************** public method***************************/
    /**
     * 跳转到指定的页
     *
     * @param pos
     */
    public boolean skipToPage(int pos) {
        mCurPage = getCurPage(pos);
        mPageView.drawCurPage(false);
        return true;
    }

    /**
     * 翻到上一页
     *
     * @return
     */
    public boolean skipToPrePage() {
        return mPageView.autoPrevPage();
    }

    /**
     * 翻到下一页
     *
     * @return
     */
    public boolean skipToNextPage() {
        return mPageView.autoNextPage();
    }

    /**
     * 设置页面样式
     */
    public void setPageStyle() {
        // 设置当前颜色样式
//        mTextColor = ContextCompat.getColor(mContext, pageStyle.getFontColor());
//        mBgColor = ContextCompat.getColor(mContext, pageStyle.getBgColor());
        mTextColor = mContext.getResources().getColor(R.color.white);
        mBgColor = mContext.getResources().getColor(R.color.blue);
        //设置背景图片
        mBgBitmap = null;

        mTipPaint.setColor(mTextColor);
        mTitlePaint.setColor(mTextColor);
        mTextPaint.setColor(mTextColor);

        mBgPaint.setColor(mBgColor);

        mPageView.drawCurPage(false);
    }

    /**
     * 设置内容与屏幕的间距
     *
     * @param marginWidth  :单位为 px
     * @param marginHeight :单位为 px
     */
    public void setMargin(int marginWidth, int marginHeight) {
        mMarginWidth = marginWidth;
        mMarginHeight = marginHeight;

        mPageView.drawCurPage(false);
    }

    /**
     * 设置页面切换监听
     *
     * @param listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mPageChangeListener = listener;
    }

    /**
     * 获取当前页的状态
     *
     * @return
     */
    public int getPageStatus() {
        return mStatus;
    }

    /**
     * 获取当前页的页码
     *
     * @return
     */
    public int getPagePos() {
        return mCurPage.position;
    }

    /**
     * 获取当前章节的章节位置
     *
     * @return
     */
    public int getChapterPos() {
        return mCurChapterPos;
    }

    /**
     * 获取距离屏幕的高度
     *
     * @return
     */
    public int getMarginHeight() {
        return mMarginHeight;
    }

    /**
     * 关闭书本
     */
    public void closeBook() {
        isClose = true;

        clearList(mCurPageList);

        mCurPageList = null;
        mPageView = null;
        mCurPage = null;
    }

    private void clearList(List list) {
        if (list != null) {
            list.clear();
        }
    }

    public boolean isClose() {
        return isClose;
    }

    /**
     * 加载页面列表
     * @return
     */
    private List<TxtPage> loadPageList() throws Exception {
        BufferedReader reader = getChapterReader(mChapter);
        List<TxtPage> chapters = loadPages(mChapter, reader);
        return chapters;
    }

    /*******************************abstract method***************************************/

    /**
     * 获取文本流
     * @param chapter
     * @return
     */
    protected abstract BufferedReader getChapterReader(TxtChapter chapter) throws Exception;

    /***********************************default method***********************************************/

    void drawPage(Bitmap bitmap, boolean isUpdate) {
        drawBackground(mPageView.getBgBitmap(), isUpdate);
        if (!isUpdate) {
            drawContent(bitmap);
        }
        //更新绘制
        mPageView.invalidate();
    }

    private void drawBackground(Bitmap bitmap, boolean isUpdate) {
        Canvas canvas = new Canvas(bitmap);
        if (!isUpdate) {
            /****绘制背景****/
            canvas.drawColor(mBgColor);// 颜色背景
        }
    }

    private void drawContent(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);
        /******绘制内容****/
        if (mStatus != STATUS_FINISH) {
            //绘制字体
            String tip = "";
            switch (mStatus) {
                case STATUS_LOADING:
                    tip = "正在拼命加载中...";
                    break;
                case STATUS_ERROR:
                    tip = "解析错误(请重试)";
                    break;
                case STATUS_EMPTY:
                    tip = "内容为空";
                    break;
                case STATUS_PARING:
                    tip = "正在加载请等待...";
                    break;
                case STATUS_PARSE_ERROR:
                    tip = "内容解析错误";
                    break;
            }

            //将提示语句放到正中间
            Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
            float textHeight = fontMetrics.top - fontMetrics.bottom;
            float textWidth = mTextPaint.measureText(tip);
            float pivotX = (mDisplayWidth - textWidth) / 2;
            float pivotY = (mDisplayHeight - textHeight) / 2;
            canvas.drawText(tip, pivotX, pivotY, mTextPaint);
        } else {
            float top;
            top = mMarginHeight - mTextPaint.getFontMetrics().top;

            //设置总距离
            int interval = mTextInterval + (int) mTextPaint.getTextSize();
            int para = mTextPara + (int) mTextPaint.getTextSize();
            int titleInterval = mTitleInterval + (int) mTitlePaint.getTextSize();
            int titlePara = mTitlePara + (int) mTextPaint.getTextSize();
            String str = null;

            //对标题进行绘制
            for (int i = 0; i < mCurPage.titleLines; ++i) {
                str = mCurPage.lines.get(i);

                //设置顶部间距
                if (i == 0) {
                    top += mTitlePara + mHangSize;
                }

                //计算文字显示的起始点
//                int start = (int) (mDisplayWidth - mTitlePaint.measureText(str)) / 2;
                int start = 30;
                //进行绘制
                canvas.drawText(str, start, top, mTitlePaint);

                //设置尾部间距
                if (i == mCurPage.titleLines - 1) {
                    top = top + titlePara + mParaSize;
                } else {
                    //行间距
                    top = top + titleInterval + mHangSize;
                }
            }

            //对内容进行绘制
            for (int i = mCurPage.titleLines; i < mCurPage.lines.size(); ++i) {
                str = mCurPage.lines.get(i);

                canvas.drawText(str, mMarginWidth, top, mTextPaint);
                if (str.endsWith("\n")) {
                    top = top + para + mParaSize;
                } else {
                    top = top + interval + mHangSize;
                }
            }
        }
    }

    void prepareDisplay(int w, int h) {
        // 获取PageView的宽高
        mDisplayWidth = w;
        mDisplayHeight = h;

        // 获取内容显示位置的大小
        mVisibleWidth = mDisplayWidth - mMarginWidth * 2;
        mVisibleHeight = mDisplayHeight - mMarginHeight * 2;

        // 重置 PageMode
        mPageView.setPageMode(mPageMode);
        // 如果章节已显示，那么就重新计算页面
        if (mStatus == STATUS_FINISH) {
            dealLoadPageList();
            // 重新设置文章指针的位置
            mCurPage = getCurPage(mCurPage.position);
        }
        mPageView.drawCurPage(false);
    }

    /**
     * 翻阅上一页
     *
     * @return
     */
    boolean prev() {
        // 以下情况禁止翻页
        if (!canTurnPage()) {
            return false;
        }

        if (mStatus == STATUS_FINISH) {
            // 先查看是否存在上一页
            TxtPage prevPage = getPrevPage();
            if (prevPage != null) {
                mCancelPage = mCurPage;
                mCurPage = prevPage;
                mPageView.drawNextPage();
                return true;
            }
        }

        if (!hasPrevChapter()) {
            return false;
        }

        mCancelPage = mCurPage;
        mPageView.drawNextPage();
        return true;
    }

    private boolean hasPrevChapter() {
        //判断是否上一章节为空
        return false;
    }

    /**
     * 翻到下一页
     *
     * @return:是否允许翻页
     */
    boolean next() {
        // 以下情况禁止翻页
        if (!canTurnPage()) {
            return false;
        }

        if (mStatus == STATUS_FINISH) {
            // 先查看是否存在下一页
            TxtPage nextPage = getNextPage();
            if (nextPage != null) {
                mCancelPage = mCurPage;
                mCurPage = nextPage;
                mPageView.drawNextPage();
                return true;
            }
        }
        return false;
    }

    public boolean parseCurChapter() {
        // 解析数据
        dealLoadPageList();
        return mCurPageList != null ? true : false;
    }

    private void dealLoadPageList() {
        try {
            mCurPageList = loadPageList();
            mCurPage = mCurPageList.get(0);
            if (mCurPageList != null) {
                if (mCurPageList.isEmpty()) {
                    mStatus = STATUS_EMPTY;

                    // 添加一个空数据
                    TxtPage page = new TxtPage();
                    page.lines = new ArrayList<>(1);
                    mCurPageList.add(page);
                } else {
                    mStatus = STATUS_FINISH;
                }
            } else {
                mStatus = STATUS_LOADING;
            }
        } catch (Exception e) {
            e.printStackTrace();

            mCurPageList = null;
            mStatus = STATUS_ERROR;
        }

        // 回调
        chapterChangeCallback();
    }

    private void chapterChangeCallback() {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageCountChange(mCurPageList != null ? mCurPageList.size() : 0);
        }
    }

    // 取消翻页
    void pageCancel() {
        if (mCurPage.position == 0 && mCurChapterPos > mLastChapterPos) { // 加载到下一章取消了
            if (mPrePageList != null) {
                cancelNextChapter();
            } else {
                mCurPage = new TxtPage();
            }
        } else if (mCurPageList == null
                || (mCurPage.position == mCurPageList.size() - 1
                && mCurChapterPos < mLastChapterPos)) {  // 加载上一章取消了

            mCurPage = new TxtPage();
        } else {
            // 假设加载到下一页，又取消了。那么需要重新装载。
            mCurPage = mCancelPage;
        }
    }

    private void cancelNextChapter() {
        int temp = mLastChapterPos;
        mLastChapterPos = mCurChapterPos;
        mCurChapterPos = temp;

        mCurPageList = mPrePageList;
        mPrePageList = null;


        mCurPage = getPrevLastPage();
        mCancelPage = null;
    }

    private void cancelPreChapter() {
        // 重置位置点
        int temp = mLastChapterPos;
        mLastChapterPos = mCurChapterPos;
        mCurChapterPos = temp;
        // 重置页面列表
        mPrePageList = mCurPageList;

        mCurPage = getCurPage(0);
        mCancelPage = null;
    }

    /**************************************private method********************************************/
    /**
     * 解析成页面列表
     * @param br：文本流
     * @return
     */
    private List<TxtPage> loadPages(TxtChapter chapter, BufferedReader br) {
        //生成的页面
        List<TxtPage> pages = new ArrayList<>();
        //使用流的方式加载
        List<String> lines = new ArrayList<>();
        int rHeight = mVisibleHeight;
        int titleLinesCount = 0;
        boolean showTitle = false; // 是否展示标题
        String paragraph = chapter.getTitle();//默认展示标题
        try {
            while (showTitle || (paragraph = br.readLine()) != null) {
                // 重置段落
                if (!showTitle) {
                    paragraph = paragraph.replaceAll("\\s", "");
                    // 如果只有换行符，那么就不执行
                    if (paragraph.equals("")) continue;
                    paragraph = StringUtils.halfToFull("  " + paragraph + "\n");
                } else {
                    //设置 title 的顶部间距  加上自定义行距
                    rHeight = rHeight - mTitlePara - mHangSize;
                }
                int wordCount = 0;
                String subStr = null;
                while (paragraph.length() > 0) {
                    //当前空间，是否容得下一行文字
                    if (showTitle) {
                        rHeight = rHeight - (int) mTitlePaint.getTextSize();
                    } else {
                        rHeight = rHeight - (int) mTextPaint.getTextSize();
                    }
                    // 一页已经填充满了，创建 TextPage
                    if (rHeight <= 0) {
                        // 创建Page
                        TxtPage page = new TxtPage();
                        page.position = pages.size();
                        page.title = chapter.getTitle();
                        page.lines = new ArrayList<>(lines);
                        page.titleLines = titleLinesCount;
                        pages.add(page);
                        // 重置Lines
                        lines.clear();
                        rHeight = mVisibleHeight;
                        titleLinesCount = 0;

                        continue;
                    }

                    //测量一行占用的字节数
                    if (showTitle) {
                        wordCount = mTitlePaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    } else {
                        wordCount = mTextPaint.breakText(paragraph,
                                true, mVisibleWidth, null);
                    }

                    subStr = paragraph.substring(0, wordCount);
                    if (!subStr.equals("\n")) {
                        //将一行字节，存储到lines中
                        lines.add(subStr);

                        //设置段落间距
                        if (showTitle) {
                            titleLinesCount += 1;
                            rHeight -= mTitleInterval + mHangSize;
                        } else {
                            rHeight -= mTextInterval + mHangSize;
                        }
                    }
                    //裁剪
                    paragraph = paragraph.substring(wordCount);
                }

                //增加段落的间距
                if (!showTitle && lines.size() != 0) {
                    rHeight = rHeight - mTextPara + mTextInterval + mHangSize;
                }

                if (showTitle) {
                    rHeight = rHeight - mTitlePara + mTitleInterval + mHangSize;
                    showTitle = false;
                }
            }

            if (lines.size() != 0) {
                //创建Page
                TxtPage page = new TxtPage();
                page.position = pages.size();
                page.title =chapter.getTitle();
                page.lines = new ArrayList<>(lines);
                page.titleLines = titleLinesCount;
                pages.add(page);
                //重置Lines
                lines.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(br);
        }
        return pages;
    }


    /**
     * @return:获取初始显示的页面
     */
    private TxtPage getCurPage(int pos) {
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        if (mCurPageList != null){
            if (pos>=mCurPageList.size()){
                pos = mCurPageList.size()-1;
            }
            return mCurPageList.get(pos); //防空指针
        }else {
            return new TxtPage();
        }
    }

    /**
     * @return:获取上一个页面
     */
    private TxtPage getPrevPage() {

        int pos = mCurPage.position - 1;
        if (pos < 0) {
            return null;
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取下一的页面
     */
    private TxtPage getNextPage() {
        int pos = mCurPage.position + 1;
        if (pos >= mCurPageList.size()) {
            return null;
        }
        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }
        return mCurPageList.get(pos);
    }

    /**
     * @return:获取上一个章节的最后一页
     */
    private TxtPage getPrevLastPage() {
        int pos = mCurPageList.size() - 1;

        if (mPageChangeListener != null) {
            mPageChangeListener.onPageChange(pos);
        }

        return mCurPageList.get(pos);
    }

    /**
     * 根据当前状态，决定是否能够翻页
     *
     * @return
     */
    private boolean canTurnPage() {

        if (mStatus == STATUS_PARSE_ERROR
                || mStatus == STATUS_PARING) {
            return false;
        } else if (mStatus == STATUS_ERROR) {
            mStatus = STATUS_LOADING;
        }
        return true;
    }

    /*****************************************interface*****************************************/

    public interface OnPageChangeListener {
        /**
         * 作用：页码数量改变之后的回调。==> 字体大小的调整，或者是否关闭虚拟按钮功能都会改变页面的数量。
         *
         * @param count:页面的数量
         */
        void onPageCountChange(int count);

        /**
         * 作用：当页面改变的时候回调
         *
         * @param pos:当前的页面的序号
         */
        void onPageChange(int pos);
    }
}
