package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.common.ui.viewmodel.IViewModel;
import com.whaley.biz.program.uiview.model.ClickModel;
import com.whaley.biz.program.uiview.model.PageModel;

/**
 * Author: qxw
 * Date: 2017/3/16
 */

public abstract class BaseUIViewModel implements ListUIViewModel, SeverBindViewModel, OutRectUIViewModel, IViewModel {
    public static final int RECT_LEFT = 1;
    public static final int RECT_RIGHT = 2;
    public static final int RECT_LEFT_AND_RIGHT = 3;

    int outTop;
    int outLeft;
    int outRight;
    int outBottom;

    int spanSize;

    String eventId;
    String key;


    Object serverModel;

    PageModel pageModel;

    ClickModel clickModel;

    @Override
    public PageModel getPageModel() {
        return pageModel;
    }

    public void setPageModel(PageModel pageModel) {
        this.pageModel = pageModel;
    }

    @Override
    public ClickModel getClickModel() {
        return clickModel;
    }

    public void setClickModel(ClickModel clickModel) {
        this.clickModel = clickModel;
    }

    @Override
    public int getSpanSize() {
        return spanSize;
    }

    @Override
    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    @Override
    public void convert(Object severModel) {
        this.serverModel = severModel;
    }

    @Override
    public <M> M getSeverModel() {
        return (M) serverModel;
    }

    @Override
    public int getOutTop() {
        return outTop;
    }

    @Override
    public void setOutTop(int outTop) {
        this.outTop = outTop;
    }

    @Override
    public int getOutLeft() {
        return outLeft;
    }

    @Override
    public void setOutLeft(int outLeft) {
        this.outLeft = outLeft;
    }

    @Override
    public int getOutRight() {
        return outRight;
    }

    @Override
    public void setOutRight(int outRight) {
        this.outRight = outRight;
    }

    @Override
    public int getOutBottom() {
        return outBottom;
    }

    @Override
    public void setOutBottom(int outBottom) {
        this.outBottom = outBottom;
    }

    @Override
    public boolean isCanClick() {
        return false;
    }

}
