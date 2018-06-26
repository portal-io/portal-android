package com.whaley.biz.setting.ui.view;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/8/4.
 */

public interface DownloadView extends BasePageView {

    void updateList();

    void showEdit();

    void noList();

    void updateMainList();

    void cancelEdit();

    void updateDownloadText(int position);

    void remove(int position);

    void updateCheck();

    void updateCheck(int position);

}
