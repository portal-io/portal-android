package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.program.model.CpInfoModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class IconRoundUIViewModel extends BaseUIViewModel {
    private String imgUrl;
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean isCanClick() {
        return true;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_CHANNEL_ICON_ROUND;
    }

    public void convert(CpInfoModel cpInfo) {
        if (cpInfo != null) {
            setName(cpInfo.getName());
            setImgUrl(cpInfo.getHeadPic());
        }
        setPageModel(FormatPageModel.goPageModelPublisher(cpInfo.getCode()));
    }
}
