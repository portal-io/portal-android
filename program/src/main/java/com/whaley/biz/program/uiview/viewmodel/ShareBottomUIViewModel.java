package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.uiview.ViewTypeConstants;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class ShareBottomUIViewModel extends BaseUIViewModel {

    public final static int TOPIC_TYPE = 0;
    public final static int PROGRAM_SET_TYPE = 1;
    public final static int PROGRAM_PACKAGE_TYPE = 2;
    private boolean isPay;
    private int ContentType;
    private boolean isActivity;

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public int getContentType() {
        return ContentType;
    }

    public void setContentType(int contentType) {
        ContentType = contentType;
    }

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    @Override
    public int getType() {
        return isActivity ? ViewTypeConstants.TYPE_ACTIVITY_SHARE_BOTTOM : ViewTypeConstants.TYPE_SHARE_BOTTOM;
    }
}
