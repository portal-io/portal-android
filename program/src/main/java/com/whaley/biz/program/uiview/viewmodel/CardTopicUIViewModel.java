package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.TopicModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class CardTopicUIViewModel extends BaseUIViewModel {

    public final static int TYPE_LINE_COLOR_0 = 0;
    public final static int TYPE_LINE_COLOR_1 = 1;

    private String imgUrl;
    private String name;
    private String playCount;
    private String introduction;
    private int bgColorType;

    public int getBgColorType() {
        return bgColorType;
    }

    public void setBgColorType(int bgColorType) {
        this.bgColorType = bgColorType;
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

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public boolean isCanClick() {
        return true;
    }

    public void convert(TopicModel topicModel) {
        super.convert(topicModel);
        imgUrl = topicModel.getPicUrl();
        name = topicModel.getItemName();
        playCount = "共" + topicModel.getDetailCount() + "个内容";
        introduction = topicModel.getIntroduction();
        setPageModel(FormatPageModel.getPageModel(topicModel));
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_TOPIC_CARD;
    }
}
