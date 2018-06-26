package com.whaley.biz.program.ui.activitys;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.whaley.biz.common.ui.CommonActivity;

/**
 * Created by YangZhi on 2017/9/6 20:20.
 */

public class PlayerActivity extends CommonActivity{

    @Override
    protected void initView(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);/**屏幕常亮*/
        super.initView(savedInstanceState);
    }
}
