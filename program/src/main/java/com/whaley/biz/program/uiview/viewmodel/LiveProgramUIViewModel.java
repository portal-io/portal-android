package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveProgramUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String title;
    private String subtitle;
    private boolean isSoccer;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_RECOMMEND_LIVE_PROGRAM;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public boolean isSoccer() {
        return isSoccer;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public void setSoccer(boolean soccer) {
        isSoccer = soccer;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        setTitle(recommendModel.getName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.getFormatTime(recommendModel.getProgramPlayTime()));
        if (recommendModel.getStatQueryDto() != null) {
            stringBuilder.append(" | ");
            stringBuilder.append(StringUtil.getCuttingCount(recommendModel.getStatQueryDto().getPlayCount()));
            stringBuilder.append("人播放");
        }
        setSubtitle(stringBuilder.toString());
        setImgUrl(recommendModel.getNewPicUrl());
        setPageModel(FormatPageModel.getPageModel(recommendModel));
    }
}
