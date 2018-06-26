package com.whaley.biz.program.uiview.viewmodel;

import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.ViewTypeConstants;
import com.whaley.biz.program.utils.FormatPageModel;

/**
 * Created by dell on 2017/8/16.
 */

public class LiveTopicMoreUIViewModel extends BaseUIViewModel {

    @Override
    public int getType() {
        return ViewTypeConstants.TYPE_LIVE_TOPIC_MORE;
    }


    @Override
    public boolean isCanClick() {
        return true;
    }

    public void convert(RecommendModel recommendModel) {
        super.convert(recommendModel);
        setPageModel(FormatPageModel.getPageModel(recommendModel));
    }

}
