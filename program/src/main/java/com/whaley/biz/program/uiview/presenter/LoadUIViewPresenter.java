package com.whaley.biz.program.uiview.presenter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.LoadMoreObserver;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.repository.LoaderRepository;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.ui.view.LoaderView;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.uiview.repository.RecyclerUIViewRepository;
import com.whaley.biz.program.uiview.viewmodel.BaseUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewpool.CommonViewPoolAysncHelper;
import com.whaley.biz.program.utils.BIUtil;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.router.Router;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:
 */

public abstract class LoadUIViewPresenter<UIVIEW extends LoadUIViewPresenter.RecyclerUIVIEW> extends LoadPresenter<UIVIEW> {


    protected RecyclerUIViewRepository repository;


    protected RecyclerUIViewRepository getRepository() {
        return repository;
    }

    Disposable disposable;

    boolean isViewCreated;

    public LoadUIViewPresenter(UIVIEW view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        String id = arguments.getString(ProgramConstants.KEY_PARAM_ID);
        repository = getRepositoryFromMemoryCache(id, saveInstanceState);
        if (repository == null) {
            repository = new RecyclerUIViewRepository();
        }
        getRepository().setId(id);

        String loadId = id+"_loader";
        LoaderRepository loaderRepository = getRepositoryFromMemoryCache(loadId, saveInstanceState);
        if (loaderRepository !=null) {
            setLoaderRepository(loaderRepository);
        }
        this.isViewCreated = true;
        if (checkHasData()) {
            getUIView().bindViewModel(getRecyclerViewModel());
            if(getUIView().getILoadMoreView()!=null&&getLoaderRepository().getLoadListData()!=null) {
                getUIView().getILoadMoreView().setHasMore(getLoaderRepository().getLoadListData().isHasMore(), true);
            }
        } else if (isVisible()) {
            onRefresh();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getRepository() == null)
            return;
        saveToMemoryCache(getRepository().getId()+"_loader",getLoaderRepository(),outState);
        saveToMemoryCache(getRepository().getId(), getRepository(), outState);
    }

    protected void initData() {
        if (checkHasData()) {
            getUIView().bindViewModel(getRecyclerViewModel());
        } else {
            onRefresh();
        }
    }

    private boolean checkHasData() {
        return getRecyclerViewModel() != null && getRecyclerViewModel().getClickableUiViewModels() != null && getRecyclerViewModel().getClickableUiViewModels().size() > 0;
    }

    protected boolean isVisible() {
        return isViewCreated;
    }


    @Override
    public void onRefresh() {
        dispose();
        disposable = buildRefreshObservable(onRefreshObservable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(onRefreshObserver());
    }

    public RefreshObserver onRefreshObserver() {
        return new RefreshObserver(getUIView(), !checkHasData()) {
            @Override
            public void onNext(@NonNull LoaderUseCase.LoaderData data) {
                getRecyclerViewModel().setChanged(true);
                onSuccess(data.getLoadListData().isHasMore(), true);
            }

            @Override
            public void onNoDataError() {
                onNoData();

            }


        };
    }

    protected void onNoData() {
        if (getRecyclerViewModel() != null) {
            getRecyclerViewModel().setClickableUiViewModels(null);
            getRecyclerViewModel().setChanged(true);
            getUIView().bindViewModel(getRecyclerViewModel());
        }
        getUIView().getEmptyDisplayView().showEmpty();
        getUIView().getILoadMoreView().setHasMore(false, true);
    }


    @Override
    public void onLoadMore() {
        dispose();
        disposable = buildLoadMoreObservable(onLoadMoreObservable())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new LoadMoreObserver<LoaderUseCase.LoaderData>(getUIView()) {
                    @Override
                    public void onNext(@NonNull LoaderUseCase.LoaderData loaderData) {
                        getRecyclerViewModel().setChanged(true);
                        onSuccess(loaderData.getLoadListData().isHasMore(), false);
                    }
                });
    }

    protected void onSuccess(boolean isHasMore, boolean isRefresh) {
        getUIView().bindViewModel(getRecyclerViewModel());
        getUIView().getILoadMoreView().setHasMore(isHasMore, isRefresh);
        if (!isRefresh) {
            getUIView().getILoadMoreView().stopLoadMore(true);
        }
    }

    protected Observable<? extends BaseListResponse> onRefreshObservable() {
        return getUserCase().buildUseCaseObservable(getRepository().getId());

    }

    protected Observable<? extends BaseListResponse> onLoadMoreObservable() {
        return getUserCase().buildUseCaseObservable(null);
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData loaderData) throws Exception {
                Function<Object, RecyclerViewModel> function = getMapper().buildFunction();
                RecyclerViewModel recyclerViewModel = function.apply(getMapperData(loaderData));
                CommonViewPoolAysncHelper.putViewHoldersByRecyclerViewModel(getUIView().getRecyclerViewRoot(),recyclerViewModel);
                getRepository().setRecyclerViewModel(recyclerViewModel);
            }
        };
    }

    protected Object getMapperData(LoaderUseCase.LoaderData loaderData) {
        return loaderData.getResponse().getList();
    }

    protected RecyclerViewModel getRecyclerViewModel() {
        if (getRepository() == null)
            return null;
        return getRepository().getRecyclerViewModel();
    }

    protected RecyclerUIViewRepository getRecyclerRepository() {
        return getRepository();
    }

    protected abstract UIViewModelMapper getMapper();

    protected abstract UseCase getUserCase();

    public void onViewClick(ClickableUIViewModel data) {
    }

    public interface RecyclerUIVIEW extends LoaderView<List<Object>> {
        void bindViewModel(RecyclerViewModel recyclerViewModel);

        void updateItem(int pos);

        RecyclerView getRecyclerViewRoot();
    }

    protected void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        if (getRecyclerViewModel() != null) {
            getRecyclerViewModel().setChanged(true);
        }
        dispose();
    }

    //==============================BI埋点====================================//

    /***
     * 点击事件
     */
    public void setBIOnClick(ClickableUIViewModel clickableUIViewModel) {
        if(!BIUtil.isHomePage(getStater()))
            return;
        LogInfoParam.Builder builder = getOnClickBuilder(clickableUIViewModel);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    protected LogInfoParam.Builder getOnClickBuilder(ClickableUIViewModel clickableUIViewModel) {
        String nextPageId = BIUtil.getNextPageId(clickableUIViewModel);
        if(nextPageId == null) {
            return null;
        }
        BaseUIViewModel baseUIViewModel = (BaseUIViewModel)clickableUIViewModel;
        RecommendModel recommendModel = baseUIViewModel.getSeverModel();
        if (recommendModel != null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(VIEW_CLICK)
                    .setCurrentPageId(ROOT_HOME_PAGE)
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_ID, getRepository().getId())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_NAME, getRepository().getTitle())
                    .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_ID, recommendModel.getLinkArrangeValue())
                    .setNextPageId(nextPageId);
            return builder;
        }
        return null;
    }

}
