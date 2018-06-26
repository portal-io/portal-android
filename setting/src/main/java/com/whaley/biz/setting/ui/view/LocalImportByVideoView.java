package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/8/7.
 */

public interface LocalImportByVideoView extends BasePageView {

    void updateData();

    void noData();

    void updateImage(int position, String picPath);

    void updateCheck();

}
