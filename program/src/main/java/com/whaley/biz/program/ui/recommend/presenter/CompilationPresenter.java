package com.whaley.biz.program.ui.recommend.presenter;

import android.os.Bundle;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetRecommendList;
import com.whaley.biz.program.interactor.mapper.CompilationMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.ui.recommend.repository.CompilationRepository;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.repository.RecyclerUIViewRepository;
import com.whaley.biz.program.uiview.viewmodel.BaseUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.BIUtil;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;

import io.reactivex.Observable;

/**
 * Created by dell on 2017/8/15.
 */

public class CompilationPresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> {

    private static final int SIZE = 10;

    public static final String STR_CODE = "code";
    public static final String STR_BIT_CODE = "bitCode";

    @Repository
    CompilationRepository compilationRepository;

    @UseCase
    GetRecommendList getRecommendList;

    @UseCase
    CompilationMapper mapper;

    boolean isVisibleToUser;

    public CompilationPresenter(RecyclerUIVIEW view) {
        super(view);
    }

    @Override
    protected RecyclerUIViewRepository getRecyclerRepository() {
        return super.getRecyclerRepository();
    }

    public CompilationRepository getCompilationRepository() {
        return compilationRepository;
    }


    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if (arguments != null) {
            getCompilationRepository().setCode(arguments.getString(STR_CODE));
            getCompilationRepository().setBitCode(arguments.getString(STR_BIT_CODE));
            getCompilationRepository().setTitle(arguments.getString(ProgramConstants.KEY_PARAM_TITLE));
        }
    }

    @Override
    public Observable<? extends BaseListResponse> onRefreshObservable() {
        return getUserCase().buildUseCaseObservable(new GetRecommendList.Param(getCompilationRepository().getCode(),
                getCompilationRepository().getBitCode(), 0, SIZE));
    }

    @Override
    public Observable<? extends BaseListResponse> onLoadMoreObservable() {
        int offset = getLoaderRepository().getLoadListData().getListData().size() / SIZE;
        return getUserCase().buildUseCaseObservable(new GetRecommendList.Param(getCompilationRepository().getCode(),
                getCompilationRepository().getBitCode(), offset, SIZE));
    }

    @Override
    protected Object getMapperData(LoaderUseCase.LoaderData loaderData) {
        return loaderData.getResponse();
    }

    @Override
    protected UIViewModelMapper getMapper() {
        return mapper;
    }

    @Override
    protected GetRecommendList getUserCase() {
        return getRecommendList;
    }

    @Override
    protected RecyclerUIViewRepository getRepository() {
        return compilationRepository;
    }

    public void onViewClick(ClickableUIViewModel data) {
        GoPageUtil.goPage(getStater(), data.getPageModel());
    }


    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser == this.isVisibleToUser)
            return;
        this.isVisibleToUser = isVisibleToUser;
        if (!isVisible()) {
            return;
        }
        initData();
    }


    @Override
    protected boolean isVisible() {
        return super.isVisible() && isVisibleToUser;
    }

}
