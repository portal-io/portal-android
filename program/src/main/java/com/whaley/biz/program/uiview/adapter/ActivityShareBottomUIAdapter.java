package com.whaley.biz.program.uiview.adapter;

import android.view.ViewGroup;

import com.whaley.biz.program.R;

/**
 * Author: qxw
 * Date:2018/1/25
 * Introduction:
 */

public class ActivityShareBottomUIAdapter extends ShareBottomUIAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(getLayoutInfalterFormParent(viewGroup).inflate(R.layout.item_activity_share_bottom, viewGroup, false));
    }

}
