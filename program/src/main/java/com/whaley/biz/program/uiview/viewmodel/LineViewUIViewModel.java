package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.core.utils.DisplayUtil;

/**
 * Author: qxw
 * Date: 2017/3/16
 */

public class LineViewUIViewModel extends BaseUIViewModel {

    public final static int TYPE_LINE_1 = 1;
    public final static int TYPE_LINE_2 = 2;
    public final static int TYPE_LINE_3 = 3;
    public final static int TYPE_LINE_4 = 4;


    public final static int TYPE_LINE_COLOR_1 = 1;

    private final static int LINE_1_HEIGHT = DisplayUtil.convertDIP2PX(6);
    private final static int LINE_2_HEIGHT = DisplayUtil.convertDIP2PX(10);


    private int lineColorType;

    private int lineHeight;
    private int lineHeightType;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_VIEW_LINE;
    }


    public int getLineHeight() {
        switch (lineHeightType) {
            case TYPE_LINE_1:
                return LINE_1_HEIGHT;
            case TYPE_LINE_2:
                return LINE_2_HEIGHT;
        }
        return lineHeight;
    }

    public void setLineHeight(int lineHeight) {
        this.lineHeight = lineHeight;
    }

    public int getLineHeightType() {
        return lineHeightType;
    }

    public void setLineHeightType(int lineHeightType) {
        this.lineHeightType = lineHeightType;
    }

    public int getLineColorType() {
        return lineColorType;
    }

    public void setLineColorType(int lineColorType) {
        this.lineColorType = lineColorType;
    }
}
