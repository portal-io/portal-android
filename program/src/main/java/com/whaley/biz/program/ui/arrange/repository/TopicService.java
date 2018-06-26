package com.whaley.biz.program.ui.arrange.repository;

import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.core.share.model.ShareParam;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public interface TopicService {

    void setTopicHead(TopicHeadViewModel topicHeadData);

    void setPlayerButton(boolean isPlayerButton);

    void setBuilder(ShareParam.Builder builder);

    void setPageModel(PageModel pageModel);

    void setActivity(boolean isActivity);

}
