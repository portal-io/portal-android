package com.whaley.biz.share;

import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * Author: qxw
 * Date:2017/9/8
 * Introduction:
 */
@Route(path = "/share/ui/horizontalshare")
public class HorizontalShareActivity extends ShareActivity {

    @Override
    protected int getLayoutID() {
        return R.layout.activity_horizontal_share;
    }
}
