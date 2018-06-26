package com.whaley.biz.program.uiview;


import android.view.View;

import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public class ClickableSimpleViewHolder extends com.whaley.core.widget.uiview.SimpleViewHolder implements ClickableUIViewHolder {

    public ClickableSimpleViewHolder(View rootView) {
        super(rootView);
    }


    @Override
    public ClickableUIViewModel getData() {
        return (ClickableUIViewModel) super.getData();
    }
}
