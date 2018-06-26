package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class ButtonTagUIViewModel extends BaseUIViewModel {


    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BUTTON_TAG;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setName(recommendModel.getName());
            setPageModel(FormatPageModel.getPageModel(recommendModel));
        }
    }
}
