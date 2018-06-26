package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.CpProgramModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;

import java.util.List;

/**
 * Author: qxw
 * Date: 2017/3/21
 */

public class PromulagtorContentUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String name;
    private String palyNum;

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

    public String getPalyNum() {
        return palyNum;
    }

    public void setPalyNum(String palyNum) {
        this.palyNum = palyNum;
    }


    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_PROMULGATOR_CONTENT;
    }

    public void convert(CpProgramModel cpProgramModel) {
        if (cpProgramModel != null) {
            setName(cpProgramModel.getDisplayName());
            setImgUrl(cpProgramModel.getBigPic());
            setPageModel(FormatPageModel.getProgramPageModel(cpProgramModel.getPlayData(), cpProgramModel.isDrama()));
            if (cpProgramModel.getStat() != null)
                setPalyNum(StringUtil.getCuttingCount(cpProgramModel.getStat().getPlayCount()) + "播放");
        }
    }

    @Override
    public boolean isCanClick() {
        return true;
    }
}
