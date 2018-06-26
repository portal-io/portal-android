package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.StrUtil;

/**
 * Created by dell on 2017/8/23.
 */

public class BannerItemUIViewModel extends BaseUIViewModel implements ProgramConstants {

    private static final String STR_FORMAT="%.1f万";
    private static final String STR_SUBTITLE_APPEND="人正在观看 立即前往 >";
    private String title;
    private String subTitle;
    private boolean isHasPlayUrl;
    private String image;
    private long position;
    private BannerPlayerViewModel bannerPlayerViewModel;

    public static String getStrFormat() {
        return STR_FORMAT;
    }

    public static String getStrSubtitleAppend() {
        return STR_SUBTITLE_APPEND;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isHasPlayUrl() {
        return isHasPlayUrl;
    }

    public void setHasPlayUrl(boolean hasPlayUrl) {
        isHasPlayUrl = hasPlayUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        setTitle(recommendModel.getName());
        if(!StrUtil.isEmpty(recommendModel.getLinkArrangeType())&&recommendModel.getLinkArrangeType().equals(ProgramConstants.LINKARRANGETYPE_LIVE)&&recommendModel.getLiveStatus()== LIVE_STATE_BEING) {
            int playCount = recommendModel.getStatQueryDto() != null ? recommendModel.getStatQueryDto().getPlayCount() : 0;
            convertPlayCoutToSubTitle(playCount);
        }else {
            setSubTitle(recommendModel.getSubtitle());
        }
        boolean isLive=recommendModel.getLinkArrangeType().equals(ProgramConstants.LINKARRANGETYPE_LIVE);
        setHasPlayUrl(!StrUtil.isEmpty(recommendModel.getVideoUrl())&&recommendModel.getIsChargeable() != 1&&(!isLive||isLive&&recommendModel.getLiveStatus()== LIVE_STATE_BEING));
        if(isHasPlayUrl()){
            BannerPlayerViewModel bannerPlayerModel=new BannerPlayerViewModel();
            bannerPlayerModel.convert(recommendModel);
            setBannerPlayerViewModel(bannerPlayerModel);
        }
        setImage(recommendModel.getNewPicUrl());
        setPageModel(FormatPageModel.getPageModel(recommendModel));
    }

    private void convertPlayCoutToSubTitle(int playCount){
        StringBuilder sb=new StringBuilder();
        if(playCount>10000){
            double playCountD= 1.0*(playCount/1000)/10;
            String playCountStr=String.format(STR_FORMAT,playCountD);
            sb.append(playCountStr);
        }else {
            sb.append(playCount);
        }
        sb.append(STR_SUBTITLE_APPEND);
        setSubTitle(sb.toString());
    }

    public BannerPlayerViewModel getBannerPlayerViewModel() {
        return bannerPlayerViewModel;
    }

    public void setBannerPlayerViewModel(BannerPlayerViewModel bannerPlayerViewModel) {
        this.bannerPlayerViewModel = bannerPlayerViewModel;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BANNER_GALLERY;
    }
}
