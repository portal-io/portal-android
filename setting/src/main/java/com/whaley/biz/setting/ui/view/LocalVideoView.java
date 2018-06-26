package com.whaley.biz.setting.ui.view;

import android.support.v4.app.Fragment;

import com.whaley.biz.common.ui.view.BasePageView;

/**
 * Created by dell on 2017/8/4.
 */

public interface LocalVideoView extends BasePageView {

    void updateData();
    void noData();
    void updatePosition(int position);
    void showEdit();
    void cancelEdit();
    void showEditButton();
    void hideEditButton();
    void updateCheck();
    void showMemoryFullDialog();
    Fragment getFragment();

}
