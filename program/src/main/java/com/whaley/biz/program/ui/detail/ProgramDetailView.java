package com.whaley.biz.program.ui.detail;

import com.whaley.biz.common.ui.view.BasePageView;

public interface ProgramDetailView extends BasePageView {

    void updatePlayerHeight(int height);

    void updateRealPlayerWidth(int width);

    void changeToMoive();

    void updateInfo(Object object);

    void changeToTV();

    void changeToVR();

    void updatePayBtn(boolean isChargeable, boolean isShowPayBtn, String pice);

    void updateCollection(boolean isCollection);

    void updateFollow(boolean isUpdate, boolean isFollow);

    void updateDownloaded(boolean isDownloaded);
}