package com.whaley.biz.program.uiview.viewmodel;


import com.whaley.biz.program.uiview.model.ClickModel;
import com.whaley.biz.program.uiview.model.PageModel;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/8/21
 * Introduction:
 */

public interface ClickableUIViewModel extends com.whaley.core.widget.uiview.UIViewModel {
    ClickModel getClickModel();

    PageModel getPageModel();
}
