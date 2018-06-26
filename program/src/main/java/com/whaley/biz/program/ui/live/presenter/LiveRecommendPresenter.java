package com.whaley.biz.program.ui.live.presenter;

import com.whaley.biz.program.interactor.GetRecommendUid;
import com.whaley.biz.program.interactor.mapper.LiveRecommendMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;

/**
 * Created by dell on 2017/8/17.
 */

public class LiveRecommendPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {
    @UseCase
    GetRecommendUid getRecommendUid;

    @UseCase
    LiveRecommendMapper mapper;


    public LiveRecommendPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    public UIViewModelMapper getMapper() {
        return mapper;
    }

    @Override
    public GetRecommendUid getUserCase() {
        return getRecommendUid;
    }

    public void onViewClick(ClickableUIViewModel data) {
        GoPageUtil.goPage(getStater(), data.getPageModel());
    }
}
