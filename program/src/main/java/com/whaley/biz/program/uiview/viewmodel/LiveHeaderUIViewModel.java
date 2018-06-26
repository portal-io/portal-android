package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.uiview.ViewTypeConstants;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveHeaderUIViewModel extends BaseUIViewModel{

    private String title;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_LIVE_HEADER;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
