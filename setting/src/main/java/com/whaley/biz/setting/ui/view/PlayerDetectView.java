package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/9/4.
 */

public interface PlayerDetectView extends BasePageView{
    void showCautionFlowDialog();
    void showDetectCompleteDialog(int level);
    void detect(String path);
    void showProgress(int progress);
    void showProgress(boolean show);
}
