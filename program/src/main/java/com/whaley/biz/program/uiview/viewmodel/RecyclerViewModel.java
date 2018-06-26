package com.whaley.biz.program.uiview.viewmodel;

import android.view.ViewGroup;

import com.whaley.biz.common.ui.viewmodel.IViewModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.model.ClickModel;
import com.whaley.biz.program.uiview.model.PageModel;

import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/10
 */

public class RecyclerViewModel implements ClickableUIViewModel, IViewModel {
    private boolean isHorizontal;

    private boolean isGrid;

    private int columnCount;

    private int width = ViewGroup.LayoutParams.MATCH_PARENT;

    private int hight = ViewGroup.LayoutParams.MATCH_PARENT;

    List<ClickableUIViewModel> clickableUiViewModels;

    boolean isChanged = true;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_RECYCLE_VIEW;
    }

    @Override
    public boolean isCanClick() {
        return false;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public boolean isGrid() {
        return isGrid;
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public List<ClickableUIViewModel> getClickableUiViewModels() {
        return clickableUiViewModels;
    }

    public void setClickableUiViewModels(List<ClickableUIViewModel> clickableUiViewModels) {
        this.clickableUiViewModels = clickableUiViewModels;
    }

    public int getSpanSize(int position) {
        if (clickableUiViewModels != null && clickableUiViewModels.size() > position) {
            Object object = clickableUiViewModels.get(position);
            if (object instanceof ListUIViewModel) {
                ListUIViewModel listUIViewModel = (ListUIViewModel) object;
                return listUIViewModel.getSpanSize();
            }
        }
        return 1;
    }

    public OutRectUIViewModel getOutRectModel(int position) {
        if (clickableUiViewModels != null && clickableUiViewModels.size() > position) {
            Object object = clickableUiViewModels.get(position);
            if (object instanceof OutRectUIViewModel) {
                OutRectUIViewModel outRectUIViewModel = (OutRectUIViewModel) object;
                return outRectUIViewModel;
            }
        }
        return null;
    }


    @Override
    public ClickModel getClickModel() {
        return null;
    }


    @Override
    public PageModel getPageModel() {
        return null;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

    public boolean isChanged() {
        return isChanged;
    }
}
