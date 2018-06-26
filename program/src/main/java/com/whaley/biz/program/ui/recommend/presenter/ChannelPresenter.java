package com.whaley.biz.program.ui.recommend.presenter;

import com.whaley.biz.program.interactor.GetRecommendList;
import com.whaley.biz.program.interactor.GetRecommendPage;
import com.whaley.biz.program.interactor.mapper.ChannelMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;

/**
 * Created by dell on 2017/9/14.
 */

public class ChannelPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {

    @UseCase
    GetRecommendPage getRecommendPage;

    @UseCase
    ChannelMapper mapper;

    public ChannelPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    protected UIViewModelMapper getMapper() {
        return mapper;
    }

    @Override
    protected GetRecommendPage getUserCase() {
        return getRecommendPage;
    }


    public void onViewClick(ClickableUIViewModel data) {
        if (data.getPageModel() != null) {
            GoPageUtil.goPage(getStater(), data.getPageModel());
        }
    }

}
