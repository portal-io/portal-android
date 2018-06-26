package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.utils.StrUtil;

/**
 * Created by YangZhi on 2017/3/14 17:55.
 */

public class ChannelIconViewUIViewModel extends BaseUIViewModel {

    private String imageUrl;

    private String name;

    private boolean isActivity;

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_CHANNEL_ICON;
    }


    @Override
    public int getSpanSize() {
        return super.getSpanSize();
    }


    @Override
    public boolean isCanClick() {
        return true;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setName(recommendModel.getName());
            if (StrUtil.isEmpty(recommendModel.getLogoImageUrl())) {
                setImageUrl(recommendModel.getNewPicUrl());
            } else {
                setImageUrl(recommendModel.getLogoImageUrl());
            }
            setPageModel(FormatPageModel.getPageModel(recommendModel));
        }
    }
}
