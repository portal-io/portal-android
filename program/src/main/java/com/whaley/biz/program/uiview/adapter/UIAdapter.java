package com.whaley.biz.program.uiview.adapter;


import android.view.ViewGroup;

import com.whaley.biz.program.uiview.ClickableUIViewHolder;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public interface UIAdapter<T extends ClickableUIViewHolder, H extends ClickableUIViewModel> extends com.whaley.core.widget.uiview.UIAdapter<T, H> {
    @Override
    T onCreateView(ViewGroup viewGroup, int i);
}
