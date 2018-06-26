package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.CpProgramModel;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.PackageItemModel;
import com.whaley.biz.program.model.TopicModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.utils.DateUtils;
import com.whaley.core.utils.DisplayUtil;

/**
 * Created by dell on 2017/8/15.
 */

public class CardVideoUIViewModel extends BaseUIViewModel {

    private String imgUrl;
    private String name;
    private String subtitle;
    private boolean isReserve;
    private boolean isHaveButton;
    private boolean isPay;

    private boolean isActivity;

    public boolean isActivity() {
        return isActivity;
    }

    public void setActivity(boolean activity) {
        isActivity = activity;
    }

    public int getCornerResureId() {
        return cornerResureId;
    }

    public void setCornerResureId(int cornerResureId) {
        this.cornerResureId = cornerResureId;
    }

    private int cornerResureId = 0;

    public boolean isPay() {
        return isPay;
    }

    public void setPay(boolean pay) {
        isPay = pay;
    }

    public boolean isHaveButton() {
        return isHaveButton;
    }

    public void setHaveButton(boolean haveButton) {
        isHaveButton = haveButton;
    }

    @Override
    public int getType() {
        return isActivity ? ViewTypeConstants.TYPE_ACTIVITY_VIDEO : ViewTypeConstants.TYPE_RESERVE_CARD;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

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

    public boolean isReserve() {
        return isReserve;
    }

    public void setReserve(boolean reserve) {
        isReserve = reserve;
    }


    public void convert(LiveDetailsModel model) {
        setImgUrl(model.getPoster());
        setName(model.getDisplayName());
        String time = DateUtils.foramteToYYYYMMDDHHMM(model.getBeginTime());
        String count = StringUtil.getCuttingCount(model.getLiveOrderCount()) + "人已预约";
        setSubtitle(time + "  |  " + count);
        setReserve(model.getHasOrder() == 1);
        setHaveButton(true);
        setPageModel(FormatPageModel.getPageModel(model));
        setVideoOutSize();
        super.convert(model);
    }

    public void convert(PackageItemModel packageItemModel) {
        super.convert(packageItemModel);
        String videoCount = StringUtil.getFormatTime(packageItemModel.getDuration());
        subtitle = videoCount + "  |  " + StringUtil.getCuttingCount(packageItemModel.getPlayCount()) + "人播放";
        name = packageItemModel.getDisplayName();
        imgUrl = packageItemModel.getPic();
        setVideoOutSize();
    }

    public void convert(TopicModel topicModel) {
        convert(topicModel, false);
    }

    public void convert(TopicModel topicModel, boolean isActivity) {
        super.convert(topicModel);
        String videoCount = StringUtil.getFormatTime(topicModel.getDuration());
        name = topicModel.getItemName();
        imgUrl = topicModel.getPicUrl();
        if (topicModel.getStatQueryDto() != null)
            subtitle = videoCount + "  |  " + StringUtil.getCuttingCount(topicModel.getStatQueryDto().getPlayCount()) + "人播放";
        if (!isActivity)
            setVideoOutSize();
    }

    public void convert(CpProgramModel model) {
        convert(model, false);
    }

    public void convert(CpProgramModel model, boolean isActivity) {
        setImgUrl(model.getBigPic());
        setName(model.getDisplayName());
        setSubtitle(StringUtil.getFormatTime(model.getDuration())
                + " | "
                + StringUtil.getCuttingCount(
                model.getStat().getPlayCount())
                + "播放");
        setPageModel(FormatPageModel.getProgramPageModel(model.getPlayData(), model.isDrama()));
        if (!isActivity)
            setVideoOutSize();
    }

    public void setActivity() {
        setActivity(true);
    }

    private void setVideoOutSize() {
        setOutTop(DisplayUtil.convertDIP2PX(8));
        setOutBottom(DisplayUtil.convertDIP2PX(18));
        setOutLeft(DisplayUtil.convertDIP2PX(10));
        setOutRight(DisplayUtil.convertDIP2PX(10));
    }

}
