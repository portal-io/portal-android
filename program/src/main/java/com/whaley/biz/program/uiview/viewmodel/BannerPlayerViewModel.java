package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.core.utils.StrUtil;

/**
 * Created by dell on 2017/8/23.
 */

public class BannerPlayerViewModel extends BaseUIViewModel implements ProgramConstants{

    private boolean isCanPlay;

    private boolean isLive;

    private RecommendModel recommendModel;

    private long videoCurrentPosition;

    private boolean isSetToPlayer;

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_BANNER_GALLERY;
    }

    public void setSetToPlayer(boolean setToPlayer) {
        isSetToPlayer = setToPlayer;
    }

    public boolean isSetToPlayer() {
        return isSetToPlayer;
    }

    public void setVideoCurrentPosition(long videoCurrentPosition) {
        this.videoCurrentPosition = videoCurrentPosition;
    }

    public long getVideoCurrentPosition() {
        return videoCurrentPosition;
    }

    public boolean isCanPlay() {
        return isCanPlay;
    }

    public void setCanPlay(boolean canPlay) {
        isCanPlay = canPlay;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public void setRecommendModel(RecommendModel recommendModel) {
        this.recommendModel = recommendModel;
    }

    public RecommendModel getRecommendModel() {
        return recommendModel;
    }

    public void convert(RecommendModel recommendModel){
        super.convert(recommendModel);
        setRecommendModel(recommendModel);
        setLive(recommendModel.getLinkArrangeType().equals(ProgramConstants.LINKARRANGETYPE_LIVE));
        setCanPlay(!StrUtil.isEmpty(recommendModel.getVideoUrl())&&recommendModel.getIsChargeable() != 1&&(!isLive()||isLive()&&recommendModel.getLiveStatus()== LIVE_STATE_BEING));
    }

}
