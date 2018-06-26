package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.utils.DateUtils;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String title;
    private String subtitle;
    private int status;
    private boolean isSoccer;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_RECOMMEND_LIVE;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSoccer() {
        return isSoccer;
    }

    public void setSoccer(boolean soccer) {
        isSoccer = soccer;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        setTitle(recommendModel.getName());
        setStatus(recommendModel.getLiveStatus());
        setImgUrl(recommendModel.getNewPicUrl());
        String subTitle;
        if (recommendModel.getLiveStatus() == ProgramConstants.LIVE_STATE_BEING)
            subTitle = convertPlayCoutToSubTitle(recommendModel.getStatQueryDto() == null ? 0 : recommendModel.getStatQueryDto().getPlayCount());
        else if (recommendModel.getLiveStatus() == ProgramConstants.LIVE_STATE_BEFORE)
            subTitle = convertReserveCountToSubTitle(recommendModel);
        else if (recommendModel.getLiveStatus() == ProgramConstants.LIVE_STATE_AFTER)
            subTitle = "直播已结束";
        else
            subTitle = "";
        setSubtitle(subTitle);
        setPageModel(FormatPageModel.getPageModel(recommendModel));
    }

    private String convertReserveCountToSubTitle(RecommendModel recommendModel) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DateUtils.foramteToYYYYMMDDHHMM(recommendModel.getLiveBeginTime()));
        stringBuilder.append(" | ");
        stringBuilder.append(StringUtil.getCuttingCount(recommendModel.getLiveOrderCount()));
        stringBuilder.append("人已预约");
        return stringBuilder.toString();
    }

    private String convertPlayCoutToSubTitle(int playCount) {
        StringBuilder sb = new StringBuilder();
        if (playCount > 10000) {
            double playCountD = 1.0 * playCount / 10000;
            String playCountStr = String.format("%.1f万", playCountD);
            sb.append(playCountStr);
        } else {
            sb.append(playCount);
        }
        sb.append("人正在观看");
        return sb.toString();
    }

}
