package com.example.xueliang.view.readview;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

/**
 * Created by wbf
 */

public class LocalPageLoader extends PageLoader {

    public LocalPageLoader(PageView pageView) {
        super(pageView);
        mStatus = STATUS_LOADING;
    }

    @Override
    protected BufferedReader getChapterReader(TxtChapter chapter) throws Exception {
        //从文件中获取数据
        byte[] content = chapter.getBody().getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(content);
        BufferedReader br = new BufferedReader(new InputStreamReader(bais, "utf-8"));
        return br;
    }
}
