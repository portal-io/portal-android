package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.ArrangeModel;
import com.whaley.biz.program.model.CpProgramModel;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.utils.DisplayUtil;

/**
 * Author: qxw
 * Date: 2017/3/16
 */

public class ProgramUIViewModel extends BaseUIViewModel {


    private int displayNum = 4;
    private int pos;
    private String imgUrl;
    private String name;
    private String subtitle;
    private int typeProgramHeight;
    private int imgWidth;
    private int imgHeight;
    private boolean isCustomizeWidth;
    private boolean isCustomizeHeight;
    private int cornerResourceId;

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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getTypeProgramHeight() {
        return typeProgramHeight;
    }

    public void setTypeProgramHeight(int typeProgramHeight) {
        this.typeProgramHeight = typeProgramHeight;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(int displayNum) {
        this.displayNum = displayNum;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public boolean isCustomizeWidth() {
        return isCustomizeWidth;
    }

    public void setCustomizeWidth(boolean customizeWidth) {
        isCustomizeWidth = customizeWidth;
    }

    public boolean isCustomizeHeight() {
        return isCustomizeHeight;
    }

    public void setCustomizeHeight(boolean customizeHeight) {
        isCustomizeHeight = customizeHeight;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }


    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        if (recommendModel != null) {
            setName(recommendModel.getName());
            setImgUrl(recommendModel.getNewPicUrl());
            setSubtitle(recommendModel.getSubtitle());
            setPageModel(FormatPageModel.getPageModel(recommendModel));
            if (ProgramConstants.LINKARRANGETYPE_DYNAMICPROGRAM.equals(recommendModel.getLinkArrangeType())) {
                setCornerResourceId(R.mipmap.corner_drama);
            }
        }
    }

    public void convert(ArrangeModel arrangeModel) {
        if (arrangeModel != null) {
            setName(arrangeModel.getItemName());
            setImgUrl(arrangeModel.getPicUrl());
            setSubtitle(arrangeModel.getSubtitle());
            boolean isDrama = ProgramConstants.TYPE_DYNAMIC.equals(arrangeModel.getProgramType());
            setPageModel(FormatPageModel.getPageModel(arrangeModel, isDrama));
        }
    }

    @Override
    public void convert(Object severModel) {
        super.convert(severModel);
        Object o = getSeverModel();
        if (o instanceof RecommendModel) {
            RecommendModel recommendModel = getSeverModel();
            if (recommendModel != null) {
                setName(recommendModel.getName());
                setImgUrl(recommendModel.getNewPicUrl());
                setSubtitle(recommendModel.getSubtitle());
                if (recommendModel.getLinkArrangeType() == ProgramConstants.LINKARRANGETYPE_DYNAMICPROGRAM) {
                    setCornerResourceId(R.mipmap.corner_drama);
                }
            }
            return;
        }
        if (o instanceof CpProgramModel) {
            CpProgramModel cpProgramModel = getSeverModel();
            if (cpProgramModel != null) {
                setName(cpProgramModel.getDisplayName());
                setImgUrl(cpProgramModel.getBigPic());
                if (cpProgramModel.getStat() != null)
                    setSubtitle(StringUtil.getCuttingCount(cpProgramModel.getStat().getPlayCount()) + "播放");
                setOutLeft(DisplayUtil.convertDIP2PX(8));
                setCustomizeHeight(true);
                setImgHeight(DisplayUtil.convertDIP2PX(68.7f));
                setCustomizeWidth(true);
                setImgWidth(DisplayUtil.convertDIP2PX(122));
            }
        }
    }


    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_PROGRAM;
    }

    public int getCornerResourceId() {
        return cornerResourceId;
    }

    public void setCornerResourceId(int cornerResourceId) {
        this.cornerResourceId = cornerResourceId;
    }
}
