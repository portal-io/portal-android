package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.CpFollowInfoModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;

/**
 * Author: qxw
 * Date: 2017/3/22
 */

public class GraphicLeftAndRightUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String name;
    private String playNum;

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

    public String getPlayNum() {
        return playNum;
    }

    public void setPlayNum(String playNum) {
        this.playNum = playNum;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_GRAPHIC_LEFT_AND_RIGHT;
    }

    public void convert(CpFollowInfoModel cpProgramModel) {
        if (cpProgramModel != null) {
            setName(cpProgramModel.getDisplayName());
            setImgUrl(cpProgramModel.getSmallPic());
            if (cpProgramModel.getStat() != null)
                setPlayNum(StringUtil.getCuttingCount(cpProgramModel.getStat().getPlayCount()) + "播放");
            setPageModel(FormatPageModel.getProgramPageModel(cpProgramModel.getPlayData(), cpProgramModel.isDrama()));
        }
    }
}
