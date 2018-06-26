package com.whaley.biz.program.ui.recommend.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.ui.view.TabIndicatorView;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetRecommendPage;
import com.whaley.biz.program.interactor.mapper.TabItemViewModelMapper;
import com.whaley.biz.program.ui.recommend.CompilationFragment;
import com.whaley.biz.program.ui.arrange.ArrangeFragment;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.model.response.RecommendResponse;
import com.whaley.biz.program.ui.recommend.RecommendPageFragment;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.StrUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/8/14 15:40.
 */

public class PageTitlePresenter extends TabIndicatorPresenter<TabIndicatorView> implements ProgramConstants {


    @UseCase
    GetRecommendPage getRecommendPage;

    @UseCase
    TabItemViewModelMapper tabItemViewModelMapper;


    Disposable disposable;

//    private SparseArray<Fragment> viewPoolCache = new SparseArray<>();

    public PageTitlePresenter(TabIndicatorView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        String id = arguments.getString(ProgramConstants.KEY_PARAM_ID);
        if (StrUtil.isEmpty(id)) {
            id = GetRecommendPage.HOME_PAGE;
        }
        getTabIndicatorRepository().setCode(id);
        super.onViewCreated(arguments, saveInstanceState);
        if (getUIView().getTitleBar() != null) {
            getUIView().getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
//            getUIView().getTitleBar().setTitleText(arguments.getString(ProgramConstants.KEY_PARAM_TITLE));
        }
    }

    @Override
    public Fragment getItem(int position) {
        TabItemViewModel tabItemViewData = getTabIndicatorRepository().getTitles().get(position);
        RecommendModel recommendModel = (RecommendModel) tabItemViewData.getData();
        String linkArrangeType = recommendModel.getLinkArrangeType();
//        umBuriedPoint(tabItemViewData.getTitle());
//        Fragment fragment = viewPoolCache.get(position);
//        if (fragment == null) {
        Fragment fragment = null;
        switch (linkArrangeType) {
            case LINKARRANGETYPE_RECOMMENDPAGE:
                if (RECOMMEND_PAGE_TYPE_PAGE.equals(recommendModel.getRecommendPageType())) {
                    fragment = CompilationFragment.newInstance(recommendModel.getLinkArrangeValue(), recommendModel.getRecommendAreaCodes().get(0));
                } else {
                    fragment = RecommendPageFragment.newInstance(recommendModel.getLinkArrangeValue());
                }
                break;
            case LINKARRANGETYPE_ARRANGE:
                fragment = ArrangeFragment.newInstance(recommendModel.getLinkArrangeValue());
                break;
        }
//            viewPoolCache.put(position, fragment);
//        }
        return fragment;
    }


    private boolean checkTabValidate(RecommendModel recommendModel) {
        boolean isValidate = false;
        switch (recommendModel.getLinkArrangeType()) {
            case LINKARRANGETYPE_RECOMMENDPAGE:
                if (RECOMMEND_PAGE_TYPE_MIX.equals(recommendModel.getRecommendPageType()) || RECOMMEND_PAGE_TYPE_PAGE.equals(recommendModel.getRecommendPageType())) {
                    isValidate = true;
                }
                break;
            case LINKARRANGETYPE_ARRANGE:
                isValidate = true;
                break;
        }
        return isValidate;
    }

    @Override
    protected void getTitles() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
//        GetRecommendPage.Param param = new GetRecommendPage.Param();
//        param.setCode(GetRecommendPage.HOME_PAGE);
        disposable = getRecommendPage
                .buildUseCaseObservable(getTabIndicatorRepository().getCode())
                .doOnNext(new Consumer<RecommendResponse>() {
                    @Override
                    public void accept(@NonNull RecommendResponse recommendResponse) throws Exception {
                        getTabIndicatorRepository().setTitleText(recommendResponse.getList().get(0).getName());
                    }
                })
                .map(new Function<RecommendResponse, List<RecommendModel>>() {
                    @Override
                    public List<RecommendModel> apply(@NonNull RecommendResponse recommendResponse) throws Exception {
                        return recommendResponse.getList().get(0).getRecommendElements();
                    }
                })
                .concatMap(new Function<List<RecommendModel>, ObservableSource<RecommendModel>>() {
                    @Override
                    public ObservableSource<RecommendModel> apply(@NonNull final List<RecommendModel> recommendModels) throws Exception {
                        return Observable.fromIterable(recommendModels);
                    }
                })
                .filter(new Predicate<RecommendModel>() {
                    @Override
                    public boolean test(@NonNull RecommendModel recommendModel) throws Exception {
                        return checkTabValidate(recommendModel);
                    }
                })
                .map(tabItemViewModelMapper.<RecommendModel>buildFunction())
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<List<TabItemViewModel>>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull List<TabItemViewModel> tabItemViewModels) {
                        super.onNext(tabItemViewModels);
                        getTabIndicatorRepository().setTitles(tabItemViewModels);
                        getTabIndicatorRepository().setCount(getTabIndicatorRepository().getTitles().size());
                    }
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(getUIView()!=null){
                            getUIView().updateTabs();
                            updateTitle();
                        }
                    }
                });
    }

    private void updateTitle() {
        if (getUIView().getTitleBar() != null) {
            getUIView().getTitleBar().setTitleText(getTabIndicatorRepository().getTitleText());
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }


    public void onClickHistory() {
        GoPageUtil.goPage(getStater(), FormatPageModel.goPagePageModelHistory());
    }


    public void onClickDownload() {
        GoPageUtil.goPage(getStater(), FormatPageModel.goPagePageModelDownload());
    }


    public void onClickSearch() {
        GoPageUtil.goPage(getStater(), FormatPageModel.goPagePageModelSearch());
    }

    public void onClickMoreChannel(){
        GoPageUtil.goPage(getStater(), FormatPageModel.goPagePageModelChannelRecommend());
    }

}
