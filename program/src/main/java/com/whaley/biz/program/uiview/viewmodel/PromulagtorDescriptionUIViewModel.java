package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.CpInfoModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.StringUtil;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class PromulagtorDescriptionUIViewModel extends BaseUIViewModel {

    private String name;
    private String Introduction;
    private String followNum;
    private int fansCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }

    public String getFollowNum() {
        return followNum;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public void setFollowNum(String followNum) {
        this.followNum = followNum;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_PROMULGATOR_DESCRIPTION;
    }

    @Override
    public void convert(Object severModel) {
        super.convert(severModel);
        CpInfoModel cpInfoModel = getSeverModel();
        if (cpInfoModel != null) {
            setName(cpInfoModel.getName());
            setIntroduction(cpInfoModel.getInfo());
            setFansCount(cpInfoModel.getFansCount());
            udataFollowNum();
        }
    }

    public void udataFollowNum() {
        setFollowNum(StringUtil.getCuttingCount(fansCount) + "粉丝");
    }
}
