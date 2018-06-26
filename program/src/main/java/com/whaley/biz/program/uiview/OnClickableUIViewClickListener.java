package com.whaley.biz.program.uiview;


import com.whaley.core.widget.uiview.OnUIViewClickListener;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public interface OnClickableUIViewClickListener extends OnUIViewClickListener<ClickableUIViewHolder> {
    @Override
    void onViewClick(ClickableUIViewHolder uiViewHolder);
}
