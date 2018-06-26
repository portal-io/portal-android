package com.whaley.biz.user.ui.view;

import android.graphics.Bitmap;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public interface ClipAvatarView extends BasePageView {
    void showBitmap(Bitmap bitmap);
    Bitmap getClipedBitmap();
}
