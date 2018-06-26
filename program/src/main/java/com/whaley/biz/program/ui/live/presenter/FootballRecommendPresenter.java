package com.whaley.biz.program.ui.live.presenter;

import com.whaley.biz.program.interactor.GetRecommendUid;
import com.whaley.biz.program.interactor.mapper.FootballRecommendMapper;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;


/**
 * Created by dell on 2017/8/14.
 */

public class FootballRecommendPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {


    @UseCase
    GetRecommendUid getRecommendUid;

    @UseCase
    FootballRecommendMapper mapper;

    public FootballRecommendPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    public FootballRecommendMapper getMapper() {
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