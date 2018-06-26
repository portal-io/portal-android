package com.whaley.biz.program.ui.recommend.presenter;


import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.response.BaseListResponse;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.interactor.GetRecommendPage;
import com.whaley.biz.program.interactor.mapper.RecommendMapper;
import com.whaley.biz.program.interactor.mapper.UIViewModelMapper;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.model.response.RecommendResponse;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.uiview.presenter.LoadUIViewPresenter;
import com.whaley.biz.program.uiview.repository.RecyclerUIViewRepository;
import com.whaley.biz.program.uiview.viewmodel.BaseUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ButtonViewUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.utils.BIUtil;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.StrUtil;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * Created by YangZhi on 2017/8/14 17:54.
 */

public class RecommendPagePresenter extends LoadUIViewPresenter<LoadUIViewPresenter.RecyclerUIVIEW> implements BIConstants{


    @UseCase
    GetRecommendPage getRecommendPage;

    @UseCase
    RecommendMapper recommendMapper;

    boolean isVisibleToUser;

    public RecommendPagePresenter(RecyclerUIVIEW view) {
        super(view);
    }


    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (getUIView().getTitleBar() != null) {
            getUIView().getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
            if (repository != null && !StrUtil.isEmpty(repository.getTitle()))
                getUIView().getTitleBar().setTitleText(repository.getTitle());
        }
    }

    public void onViewClick(ClickableUIViewModel data, int position) {
        if (data.getPageModel() != null) {
            GoPageUtil.goPage(getStater(), data.getPageModel());
            setBIOnClick(data);
            return;
        }
        if (data.getClickModel() != null) {
            if (data.getClickModel().getMode() instanceof ButtonViewUIViewModel.BatchViewModel) {
                ButtonViewUIViewModel.BatchViewModel batchViewModel = data.getClickModel().getMode();
                forBatch(batchViewModel, position);
            }
        }
    }

    /**
     * @param batchViewModel
     * @param pos
     */
    private void forBatch(ButtonViewUIViewModel.BatchViewModel batchViewModel, int pos) {
        batchViewModel.pos += batchViewModel.displayNum;
        if (!(batchViewModel.pos + batchViewModel.displayNum <= batchViewModel.clickableUiViewModels.size())) {
            batchViewModel.pos = 0;
        }
        int j = batchViewModel.pos + batchViewModel.displayNum;
        for (int i = batchViewModel.pos; i < j; i++) {
            int p = pos - (j - i + batchViewModel.differential);
            getRepository().getRecyclerViewModel().getClickableUiViewModels().set(p, batchViewModel.clickableUiViewModels.get(i));
            getUIView().updateItem(p);
        }
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

    @Override
    protected Observable<? extends BaseListResponse> onRefreshObservable() {
        return super.onRefreshObservable().doOnNext(new Consumer<BaseListResponse>() {
            @Override
            public void accept(@NonNull BaseListResponse baseListResponse) throws Exception {
                RecommendResponse recommendResponse = (RecommendResponse) baseListResponse;
                getRepository().setTitle(recommendResponse.getData().getName());
            }
        });
    }

    @Override
    protected void onSuccess(boolean isHasMore, boolean isRefresh) {
        super.onSuccess(isHasMore, isRefresh);
        if (getUIView().getTitleBar() != null) {
            getUIView().getTitleBar().setTitleText(getRepository().getTitle());
        }
    }

    @Override
    public UIViewModelMapper getMapper() {
        return recommendMapper;
    }

    @Override
    public GetRecommendPage getUserCase() {
        return getRecommendPage;
    }

}
