package com.whaley.biz.program.ui.live.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.presenter.TabIndicatorPresenter;
import com.whaley.biz.common.ui.viewmodel.TabItemViewModel;
import com.whaley.biz.common.utils.SharedPreferencesUtil;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.interactor.GetLiveTab;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.ui.recommend.CompilationFragment;
import com.whaley.biz.program.ui.live.FootballRecommendFragment;
import com.whaley.biz.program.ui.live.LiveMainView;
import com.whaley.biz.program.ui.live.LiveRecommendFragment;
import com.whaley.biz.program.ui.live.LiveReserveFragment;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.UseCase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/10.
 */

public class LiveMainPresenter extends TabIndicatorPresenter<LiveMainView> implements ProgramConstants {

    @UseCase
    GetLiveTab getLiveTab;

    private Disposable disposable;


    public LiveMainPresenter(LiveMainView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        regist();
        getUIView().updateTitleText();
    }

    @Override
    public void onRetry() {
        getTitles();
    }

    @Override
    public Fragment getItem(int position) {
        TabItemViewModel tabItemViewModel = getTabIndicatorRepository().getTitles().get(position);
        String linkArrangeType = tabItemViewModel.getLinkType();
        String recommendPageType = tabItemViewModel.getRecommendPageType();
        Fragment fragment = null;
        if (fragment == null) {
            if (LINKARRANGETYPE_LIVEORDER_LIST.equals(linkArrangeType)) {
                fragment = LiveReserveFragment.newInstance();
            } else if (LINKARRANGETYPE_RECOMMENDPAGE.equals(linkArrangeType)) {
                if (tabItemViewModel.getData() instanceof RecommendModel) {
                    RecommendModel recommendModel = (RecommendModel) tabItemViewModel.getData();
                    if (recommendModel != null) {
                        if (RECOMMEND_PAGE_TYPE_MIX.equals(recommendPageType)) {
                            fragment = LiveRecommendFragment.newInstance(recommendModel.getLinkArrangeValue());
                        } else if (RECOMMEND_PAGE_TYPE_PAGE.equals(recommendPageType)) {
                            fragment = CompilationFragment.newInstance(recommendModel.getLinkArrangeValue(), recommendModel.getRecommendAreaCodes().get(0));
                        }
                    }
                }
            } else if (LINKARRANGETYPE_FOOTBALL_LIST.contains(linkArrangeType)) {
                if (tabItemViewModel.getData() instanceof RecommendModel) {
                    RecommendModel recommendModel = (RecommendModel) tabItemViewModel.getData();
                    if (recommendModel != null) {
                        fragment = FootballRecommendFragment.newInstance(recommendModel.getLinkArrangeValue());
                    }
                }
            }
        }
        return fragment;
    }

    private boolean checkTabValidate(TabItemViewModel tabItemViewModel) {
        boolean isValidate = false;
        String linkArrangeType = tabItemViewModel.getLinkType();
        String recommendPageType = tabItemViewModel.getRecommendPageType();
        if (LINKARRANGETYPE_LIVEORDER_LIST.equals(linkArrangeType)) {
            isValidate = true;
        } else if (LINKARRANGETYPE_RECOMMENDPAGE.equals(linkArrangeType)) {
            if (tabItemViewModel.getData() instanceof RecommendModel) {
                RecommendModel recommendModel = (RecommendModel) tabItemViewModel.getData();
                if (recommendModel != null) {
                    if (RECOMMEND_PAGE_TYPE_MIX.equals(recommendPageType)) {
                        isValidate = true;
                    } else if (RECOMMEND_PAGE_TYPE_PAGE.equals(recommendPageType)) {
                        isValidate = true;
                    }
                }
            }
        } else if (LINKARRANGETYPE_FOOTBALL_LIST.contains(linkArrangeType)) {
            if (tabItemViewModel.getData() instanceof RecommendModel) {
                RecommendModel recommendModel = (RecommendModel) tabItemViewModel.getData();
                if (recommendModel != null) {
                    isValidate = true;
                }
            }
        }
        return isValidate;
    }

    @Override
    protected void getTitles() {
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = getLiveTab.buildUseCaseObservable("live_tab")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<List<RecommendAreasBean>>(getUIView(), true) {
                    @Override
                    public void onNext(@NonNull List<RecommendAreasBean> data) {
                        super.onNext(data);
                        if (data != null && data.size() > 0) {
                            List<TabItemViewModel> itemViewDatas = new ArrayList<>();
                            List<RecommendModel> mList = data.get(0).getRecommendElements();
                            if (mList != null && mList.size() > 0) {
                                for (RecommendModel bean : mList) {
                                    TabItemViewModel itemViewData = new TabItemViewModel(bean.getName(), bean, bean.getLinkArrangeType(), bean.getRecommendPageType());
                                    if (checkTabValidate(itemViewData)) {
                                        itemViewDatas.add(itemViewData);
                                    }
                                }
                                getTabIndicatorRepository().setTitles(itemViewDatas);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (getUIView() != null) {
                            if (getTabIndicatorRepository().getTitles() != null) {
                                getTabIndicatorRepository().setCount(getTabIndicatorRepository().getTitles().size());
                            }
                            getUIView().updateTabs();
                            if(!SharedPreferencesUtil.getShowTips()){
                                if (getUIView() != null) {
                                    getUIView().showTips();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }

    public void onReserveClick() {
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(ProgramRouterPath.MY_RESERVE);
        Bundle bundle = new Bundle();
        bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.dialog_my_reserve));
        pageModel.setBundle(bundle);
        GoPageUtil.goPage(getStater(), pageModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case "login_cancel":
                if(getUIView()!=null){
                    getUIView().hideStatusBar();
                }
                break;
        }
    }

}
