package com.whaley.biz.program.ui.detail.presenter;

import com.whaley.biz.program.ui.detail.DramaDetailView;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaDetailPresenter extends BaseProgramDetailPresenter<DramaDetailView> {

    public DramaDetailPresenter(DramaDetailView view) {
        super(view);
    }

    @Override
    public void onDownloadClick() {
        getUIView().showToast("版权原因，暂不提供缓存");
    }

}
