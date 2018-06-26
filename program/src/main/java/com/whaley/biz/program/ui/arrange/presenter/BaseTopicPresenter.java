package com.whaley.biz.program.ui.arrange.presenter;


import android.content.Intent;
import android.text.TextUtils;

import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.ui.arrange.repository.BaseTopicRepository;
import com.whaley.biz.program.ui.arrange.TopicView;
import com.whaley.biz.program.uiview.viewmodel.BaseUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.utils.BIUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.ShareListener;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:
 */

public abstract class BaseTopicPresenter<V extends TopicView> extends BasePagePresenter<V> implements BIConstants {


    @Repository
    BaseTopicRepository baseTopicRepository;

    protected Disposable disposable;

    public BaseTopicPresenter(V view) {
        super(view);
    }


    public abstract TopicHeadViewModel getTopicHead();

    public void getData() {
        dispose();
        disposable = onCreatedObservable()
//                .map(new Function<BaseResponse, Object>() {
//                    @Override
//                    public Object apply(@NonNull BaseResponse baseResponse) throws Exception {
//                        return baseResponse.getData();
//                    }
//                })
//                .flatMap(build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new UpdateUIObserver<RecyclerViewModel>(getUIView(), true, getBaseTopicRepository().isShowEmpty()) {
                    @Override
                    public void onNext(@NonNull RecyclerViewModel o) {
                        getBaseTopicRepository().setRecyclerViewModel(o);
                        getUIView().updata(o);
                        browse(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                        getUIView().onError();
                    }

                    @Override
                    protected boolean isShowToast() {
                        return false;
                    }
                });
    }

    public abstract Observable<RecyclerViewModel> onCreatedObservable();

//    public abstract Function<? super Object, Observable<RecyclerViewModel>> build();

    protected void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public void onViewClick(int position, ClickableUIViewModel data) {
        setBIOnClick(data);
//        setBIPlay(position);
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        dispose();
        shareClear();
    }

    public void shareClear() {
    }

    public BaseTopicRepository getBaseTopicRepository() {
        return baseTopicRepository;
    }

    public void onShareClick(int type) {
        ShareParam.Builder builder = getShareParamBuilder();
        if (builder == null) {
            return;
        }
        if (type == ShareConstants.TYPE_ALL) {
            builder.setContext(getUIView().getAttachActivity());
            ShareUtil.share(builder.build());
        } else {
            ShareParam shareParam = builder.build().getShareParams().get(type);
            shareParam.setContext(getUIView().getAttachActivity());
            onClickShare(shareParam);
            shareParam.setListener(new ShareListener() {
                @Override
                public void onResult(int type) {
                    if (getUIView() != null)
                        getUIView().showToast("分享成功");
                }

                @Override
                public void onError(int type, Throwable throwable) {
                    if (getUIView() != null)
                        getUIView().showToast("分享失败");
                }

                @Override
                public void onCancel(int type) {
                    if (getUIView() != null)
                        getUIView().showToast("分享取消");
                }
            });
            ShareUtil.share(shareParam);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ShareUtil.onActivityResult(requestCode, resultCode, data);
    }

    public ShareParam.Builder getShareParamBuilder() {
        return null;
    }

    public boolean isShowPlayer() {
        return false;
    }

    public void onPlayerClick() {
//        setBIPlay(0);
    }

    //==============================BI埋点====================================//

//    /***
//     * 点击播放事件
//     * @param pos
//     */
//    private void setBIPlay(int pos) {
//        LogInfoParam.Builder builder = getPlayBuilder(pos);
//        if (builder != null) {
//            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
//        }
//    }

//    protected LogInfoParam.Builder getPlayBuilder(int position) {
//        return null;
//    }

    @Override
    protected boolean isNeedBrowseBuried() {
        return true;
    }

    /***
     * 点击事件
     */
    public void setBIOnClick(ClickableUIViewModel clickableUIViewModel) {
        LogInfoParam.Builder builder = getOnClickBuilder(clickableUIViewModel);
        if (builder != null) {
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    protected LogInfoParam.Builder getOnClickBuilder(ClickableUIViewModel clickableUIViewModel) {
        return null;
    }

    public void onClickShare(ShareParam shareParam) {
        if(shareParam!=null) {
            String currentPageId = getPageId();
            if(TextUtils.isEmpty(currentPageId))
                return;
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(SHARE)
                    .setCurrentPageId(currentPageId)
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_ID, shareParam.getShareCode())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_NAME, shareParam.getShareName())
                    .setNextPageId(currentPageId);
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }

    protected abstract String getPageId();


}
