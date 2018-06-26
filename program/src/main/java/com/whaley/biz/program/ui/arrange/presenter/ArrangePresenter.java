package com.whaley.biz.program.ui.arrange.presenter;

import android.os.Bundle;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetArrange;
import com.whaley.biz.program.interactor.mapper.ArrangeMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;

import io.reactivex.Observable;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */

public class ArrangePresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {

    @UseCase
    GetArrange getArrange;

    @UseCase
    ArrangeMapper arrangeMapper;

    public ArrangePresenter(RecyclerUIVIEW view) {
        super(view);
    }


    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (getUIView().getTitleBar() != null) {
            getUIView().getTitleBar().setTitleText(arguments.getString(ProgramConstants.KEY_PARAM_TITLE));
            getUIView().getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        arrangeMapper.setRecyclerViewModel(getRecyclerRepository().getRecyclerViewModel());
    }

    @Override
    public Observable<? extends BaseListResponse> onRefreshObservable() {
        return getArrange.buildUseCaseObservable(new GetArrange.Param(getRecyclerRepository().getId(), 0));
    }

    @Override
    public Observable<? extends BaseListResponse> onLoadMoreObservable() {
        int offset = getLoaderRepository().getLoadListData().getPage() + 1;
        return getArrange.buildUseCaseObservable(new GetArrange.Param(getRecyclerRepository().getId(), offset));
    }


    @Override
    public Object getMapperData(LoaderUseCase.LoaderData loaderData) {
        return loaderData.getResponse().getData();
    }

    @Override
    public UIViewModelMapper getMapper() {
        return arrangeMapper;
    }

    @Override
    public GetArrange getUserCase() {
        return getArrange;
    }

    public void onViewClick(ClickableUIViewModel data) {
        GoPageUtil.goPage(getStater(), data.getPageModel());
        setBIOnClick(data);
    }

}
