package com.whaley.biz.program.ui.arrange.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetTopicList;
import com.whaley.biz.program.interactor.ShareCall;
import com.whaley.biz.program.interactor.mapper.TopicViewModelMapper;
import com.whaley.biz.program.model.FestivalModel;
import com.whaley.biz.program.model.TopicModel;
import com.whaley.biz.program.model.response.TopicListResponse;
import com.whaley.biz.program.ui.arrange.TopicView;
import com.whaley.biz.program.ui.arrange.repository.TopicRepository;
import com.whaley.biz.program.uiview.viewmodel.BaseUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.BIUtil;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class TopicPresenter extends BaseTopicPresenter<TopicView> {

    @Repository
    TopicRepository repository;

    @UseCase
    GetTopicList getTopicList;

    @UseCase
    TopicViewModelMapper mapper;

    public TopicPresenter(TopicView view) {
        super(view);
    }

    @Override
    public TopicHeadViewModel getTopicHead() {
        return repository.getTopicHead();
    }

    public TopicRepository getRepository() {
        return repository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        String id = arguments.getString(ProgramConstants.KEY_PARAM_ID);
        repository.setId(id);
        getData();
    }

    @Override
    public Observable<RecyclerViewModel> onCreatedObservable() {
        return getTopicList.buildUseCaseObservable(repository.getId())
                .map(new Function<TopicListResponse, RecyclerViewModel>() {
                    @Override
                    public RecyclerViewModel apply(@NonNull TopicListResponse topicListResponse) throws Exception {
                        return mapper.buildFunction().apply(topicListResponse.getData());
                    }
                });
    }

    @Override
    public void onShareClick(final int type) {
        if (getRepository().isMovable() && type != ShareConstants.TYPE_ALL) {
            ShareParam.Builder builder = getShareParamBuilder();
            if (builder == null) {
                return;
            }
            ShareParam shareParam = builder.build().getShareParams().get(type);
            shareParam.setContext(getUIView().getAttachActivity());
            onClickShare(shareParam);
            shareParam.setListener(new ShareListener() {
                @Override
                public void onResult(int resultType) {
                    if (getUIView() != null)
                        getUIView().showToast("分享成功");
                    ShareCall call = new ShareCall(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
                    call.buildUseCaseObservable(new ShareCall.Param(resultType));
                }

                @Override
                public void onError(int resultType, Throwable throwable) {
                    if (getUIView() != null)
                        getUIView().showToast("分享失败");
                }

                @Override
                public void onCancel(int resultType) {
                    if (getUIView() != null)
                        getUIView().showToast("分享取消");
                }
            });
            ShareUtil.share(shareParam);
        } else {
            super.onShareClick(type);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CommonConstants.SHARE_REQUEST_CODE && data.getBooleanExtra(CommonConstants.SHARE_PARAM_OUTSIDE, false)) {
            Router.getInstance().buildExecutor("/program/service/activitysharecall").putObjParam(data.getIntExtra("type", 0)).excute();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onViewClick(int position, ClickableUIViewModel data) {
        GoPageUtil.goPage(getStater(), data.getPageModel());
        super.onViewClick(position, data);
    }

    @Override
    public ShareParam.Builder getShareParamBuilder() {
        return repository.getBuilder();
    }


    @Override
    public boolean isShowPlayer() {
        return repository.isPlayerButton();
    }

    @Override
    public void onPlayerClick() {
        super.onPlayerClick();
        GoPageUtil.goPage(getStater(), repository.getPageModel());
    }

    @Override
    public void shareClear() {
        if (repository != null) {
            repository.setBuilder(null);
        }
    }
    //==============================BI埋点====================================//

    @Override
    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        TopicHeadViewModel topicHeadData = repository.getTopicHead();
        if (topicHeadData != null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(eventId)
                    .setCurrentPageId(ROOT_TOPIC)
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_ID, topicHeadData.getCode())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_NAME, topicHeadData.getName());
            return builder;
        }
        return null;
    }

    @Override
    protected LogInfoParam.Builder getOnClickBuilder(ClickableUIViewModel clickableUIViewModel) {
        String nextPageId = BIUtil.getNextPageId(clickableUIViewModel);
        if (nextPageId == null)
            return null;
        LogInfoParam.Builder builder = getGeneralBuilder(VIEW_CLICK);
        if (builder != null) {
            BaseUIViewModel baseUIViewModel = (BaseUIViewModel) clickableUIViewModel;
            TopicModel topicModel = baseUIViewModel.getSeverModel();
            if (topicModel != null) {
                builder.putEventPropKeyValue(EVENT_PROP_KEY_EVENT_ID, topicModel.getLinkId())
                        .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_NAME, topicModel.getItemName())
                        .setNextPageId(nextPageId);
            }
        }
        return builder;
    }

    @Override
    protected String getPageId() {
        return ROOT_TOPIC;
    }

    public void onMyCardClick() {
        Router.getInstance().buildExecutor("/launch/service/getfestivalmodel")
                .notTransParam()
                .callback(new Executor.Callback<FestivalModel>() {
                    @Override
                    public void onCall(FestivalModel festivalModel) {
                        if (festivalModel != null) {
                            goWeb(festivalModel);
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        //
                    }
                })
                .excute();
    }

    private void goWeb(FestivalModel festivalModel) {
        GoPageUtil.goPage(getStater(), FormatPageModel.getWebModel(festivalModel.getUrl(), festivalModel.getDisplayName()));
    }
}
