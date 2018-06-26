package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.ArrangeModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.StringUtil;
import com.whaley.core.utils.DisplayUtil;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveTopicItemUIViewModel extends BaseUIViewModel {

    private boolean isMore;
    private String title;
    private String subTitle;
    private String image;

    @Override
    public boolean isCanClick() {
        return true;
    }

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_LIVE_TOPIC_ITEM;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void convert(ArrangeModel model) {
        setTitle(model.getItemName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtil.getFormatTime(model.getDuration()));
        if (model.getStatQueryDto() != null) {
            stringBuilder.append(" | ");
            stringBuilder.append(StringUtil.getCuttingCount(model.getStatQueryDto().getPlayCount()));
            stringBuilder.append("人播放");
        }
        setSubTitle(stringBuilder.toString());
        setImage(model.getPicUrl());
        boolean isDrama = ProgramConstants.TYPE_DYNAMIC.equals(model.getProgramType());
        setPageModel(FormatPageModel.getPageModel(model, isDrama));
    }
}
