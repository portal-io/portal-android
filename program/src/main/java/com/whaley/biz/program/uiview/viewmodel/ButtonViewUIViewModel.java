package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.uiview.model.ClickModel;
import com.whaley.biz.program.utils.FormatPageModel;

import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/17
 */

public class ButtonViewUIViewModel extends BaseUIViewModel {

    public final static int TYPE_ENTER = 1;
    public final static int TYPE_BATCH = 2;
    public final static int TYPE_EXPAND = 3;
    public final static int TYPE_PUT_AWAY = 4;

    private int buttonType;

    public int getButtonType() {
        return buttonType;
    }

    public void setButtonType(int buttonType) {
        this.buttonType = buttonType;
    }


    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BUTTON;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public void convert(RecommendModel bean) {
        super.convert(bean);
        setPageModel(FormatPageModel.getPageModel(bean));
    }

    public void convert(ButtonViewUIViewModel.BatchViewModel batchViewModel) {
        ClickModel clickModel = new ClickModel();
        clickModel.setMode(batchViewModel);
        setClickModel(clickModel);
    }

    /**
     * Author: qxw
     * Date: 2017/3/17
     */

    public static class BatchViewModel {
        public List<ClickableUIViewModel> clickableUiViewModels;
        public int displayNum = 4;
        public int pos;
        public int differential;
    }

}
