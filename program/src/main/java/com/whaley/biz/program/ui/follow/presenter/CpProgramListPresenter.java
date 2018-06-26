package com.whaley.biz.program.ui.follow.presenter;

import android.os.Bundle;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetCpProgramList;
import com.whaley.biz.program.interactor.mapper.CpProgramMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.ui.follow.repository.CpProgramRepository;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;

import io.reactivex.Observable;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */

public class CpProgramListPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {
    public static final String SORT_ORDER_TIME = "publishTime";
    public static final String SORT_ORDER_COUNT = "playCount";


    @Repository
    CpProgramRepository repository;

    @UseCase
    GetCpProgramList getCpProgramList;

    @UseCase
    CpProgramMapper cpProgramMapper;

    public CpProgramListPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        if (arguments != null) {
            repository.setmSortOrder(arguments.getString(ProgramConstants.KEY_PARAM_TYPE));
        }
        super.onViewCreated(arguments, saveInstanceState);
        cpProgramMapper.setRecyclerViewModel(getRecyclerRepository().getRecyclerViewModel());
    }

    @Override
    public Observable<? extends BaseListResponse> onRefreshObservable() {
        return getCpProgramList.buildUseCaseObservable(
                new GetCpProgramList.Param(getRecyclerRepository().getId(), repository.getmSortOrder(), "0"));
    }


    @Override
    public Observable<? extends BaseListResponse> onLoadMoreObservable() {
        int offset = getLoaderRepository().getLoadListData().getPage() + 1;
        return getCpProgramList.buildUseCaseObservable(
                new GetCpProgramList.Param(getRecyclerRepository().getId(), repository.getmSortOrder(), offset + ""));
    }

    @Override
    public Object getMapperData(LoaderUseCase.LoaderData loaderData) {
        return loaderData.getResponse().getData();
    }

    @Override
    public UIViewModelMapper getMapper() {
        return cpProgramMapper;
    }

    @Override
    public GetCpProgramList getUserCase() {
        return getCpProgramList;
    }

    @Override
    public void onViewClick(ClickableUIViewModel data) {
        GoPageUtil.goPage(getStater(), data.getPageModel());
    }
}
