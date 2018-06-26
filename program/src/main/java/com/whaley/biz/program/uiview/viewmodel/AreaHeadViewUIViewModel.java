package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.utils.StrUtil;


/**
 * Author: qxw
 * Date: 2017/3/10
 * Introduction:
 */

public class AreaHeadViewUIViewModel extends BaseUIViewModel {

    private String titleLeft;

    private String titleRight;

    private boolean isShowRight;

    private boolean isCanClick;

    public String getTitleLeft() {
        return titleLeft;
    }

    public void setTitleLeft(String titleLeft) {
        this.titleLeft = titleLeft;
    }

    public String getTitleRight() {
        return titleRight;
    }

    public void setTitleRight(String titleRight) {
        this.titleRight = titleRight;
    }

    public boolean isShowRight() {
        return isShowRight;
    }

    public void setShowRight(boolean showRight) {
        isShowRight = showRight;
    }

    @Override
    public boolean isCanClick() {
        return isCanClick;
    }

    public void setCanClick(boolean canClick) {
        isCanClick = canClick;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setTitleLeft(recommendModel.getName());
            if (!StrUtil.isEmpty(recommendModel.getSubtitle())) {
                setTitleRight(recommendModel.getSubtitle());
                setShowRight(true);
                setPageModel(FormatPageModel.getPageModel(recommendModel));
                setCanClick(true);
            } else {
                setShowRight(false);
                setCanClick(false);
            }
        }
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_AREA_HEAD;
    }

}
