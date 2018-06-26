package com.whaley.biz.program.ui.arrange.presenter;

import android.os.Bundle;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PayConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.IsPayedList;
import com.whaley.biz.program.interactor.MergePackage;
import com.whaley.biz.program.interactor.mapper.PackageViewModelMapper;
import com.whaley.biz.program.model.PackageItemModel;
import com.whaley.biz.program.model.PayEventModel;
import com.whaley.biz.program.model.TopicModel;
import com.whaley.biz.program.model.pay.PayResultModel;
import com.whaley.biz.program.model.response.PackageResponse;
import com.whaley.biz.program.ui.arrange.PackageView;
import com.whaley.biz.program.ui.arrange.repository.PackageRepository;
import com.whaley.biz.program.uiview.viewmodel.BaseUIViewModel;
import com.whaley.biz.program.uiview.viewmodel.RecyclerViewModel;
import com.whaley.biz.program.uiview.viewmodel.TopicHeadViewModel;
import com.whaley.biz.program.uiview.viewmodel.ClickableUIViewModel;
import com.whaley.biz.program.utils.BIUtil;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.share.model.ShareParam;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Author: qxw
 * Date:2017/8/22
 * Introduction:
 */

public class PackagePresenter extends BaseTopicPresenter<PackageView> {

    @Repository
    PackageRepository packageRepository;

    @UseCase
    MergePackage mergePackage;

    @UseCase
    PackageViewModelMapper mapper;

    @UseCase
    IsPayedList isPayedList;

    public PackagePresenter(PackageView view) {
        super(view);
    }


    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        String id = arguments.getString(ProgramConstants.KEY_PARAM_ID);
        packageRepository.setId(id);
        getData();
        regist();
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        unRegist();
    }


    @Override
    public TopicHeadViewModel getTopicHead() {
        return packageRepository.getTopicHead();
    }

    @Override
    public Observable<RecyclerViewModel> onCreatedObservable() {
        return mergePackage.buildUseCaseObservable(packageRepository.getId())
                .map(new Function<PackageResponse, RecyclerViewModel>() {
                    @Override
                    public RecyclerViewModel apply(@NonNull PackageResponse packageResponse) throws Exception {
                        return mapper.buildFunction().apply(packageResponse.getData());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<RecyclerViewModel, RecyclerViewModel>() {
                    @Override
                    public RecyclerViewModel apply(@NonNull RecyclerViewModel recyclerViewModel) throws Exception {
                        if (recyclerViewModel.getClickableUiViewModels() != null && recyclerViewModel.getClickableUiViewModels().size() > 0) {
                            return recyclerViewModel;
                        } else {
                            throw new ResponseErrorException();
                        }
                    }
                })
                .doOnNext(new Consumer<RecyclerViewModel>() {
                    @Override
                    public void accept(@NonNull RecyclerViewModel recyclerViewModel) throws Exception {
                        getBaseTopicRepository().setRecyclerViewModel(recyclerViewModel);
                        getUIView().updata(recyclerViewModel);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<RecyclerViewModel, ObservableSource<RecyclerViewModel>>() {
                    @Override
                    public ObservableSource<RecyclerViewModel> apply(@NonNull RecyclerViewModel recyclerViewModel) throws Exception {
                        if (packageRepository.isPayList()) {
                            IsPayedList.Param param = new IsPayedList.Param(packageRepository.getGoodsNos(), packageRepository.getGoodsTypes());
                            return isPayedList.buildUseCaseObservable(param).map(new Function<List<PayResultModel>, RecyclerViewModel>() {
                                @Override
                                public RecyclerViewModel apply(@NonNull List<PayResultModel> resultModels) throws Exception {
                                    return mapper.buildFunction().apply(resultModels);
                                }
                            });
                        } else {
                            return Observable.just(recyclerViewModel);
                        }
                    }
                });
    }


    public boolean isShowPayBotton() {
        if (packageRepository.isChargeable() && !packageRepository.isPay()) {
            return true;
        } else {
            return false;
        }
    }

    public String getPayString() {
        return packageRepository.getPayString();
    }

    public void onPayClick() {
        GoPageUtil.goPage(getStater(), FormatPageModel.getPageModelPay(packageRepository.getId(), packageRepository.getCouponModels(),
                "", false,
                ProgramConstants.TYPE_CONTENT_PACKGE, AppContextProvider.getInstance().getContext().getString(R.string.dialog_buy_pay)));
    }

    @Override
    public void onViewClick(int position, ClickableUIViewModel data) {
        GoPageUtil.goPage(getStater(), data.getPageModel());
        super.onViewClick(position, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (PayConstants.EVENT_PAY.equals(event.getEventType())) {
            PayEventModel payEventModel = event.getObject(PayEventModel.class);
            if (!payEventModel.isPayed()) {
                return;
            }
            if (payEventModel == null || getUIView() == null) {
                return;
            }
            if ((packageRepository.getId().equals(payEventModel.getProgramCode()))
                    || (packageRepository.getCouponModels() != null
                    && packageRepository.getCouponModels().get(0) != null
                    && payEventModel.getGoodsNo().equals(packageRepository.getCouponModels().get(0).getCode()))
                    ) {
                packageRepository.setPay(payEventModel.isPayed());
                getUIView().updatePayView();
                return;
            }
            getData();
            return;
        }
        if (ProgramConstants.EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            getData();
        }
    }

    @Override
    public ShareParam.Builder getShareParamBuilder() {
        return packageRepository.getBuilder();
    }

    @Override
    public boolean isShowPlayer() {
        return packageRepository.isPlayerButton();
    }

    @Override
    public void onPlayerClick() {
        super.onPlayerClick();
        GoPageUtil.goPage(getStater(), packageRepository.getPageModel());
    }

    @Override
    public void shareClear() {
        if (packageRepository != null) {
            packageRepository.setBuilder(null);
        }
    }

    //==============================BI埋点====================================//
    @Override
    protected String getPageId() {
        if (packageRepository.isPackage()) {
            return ROOT_PROGRAM_PACKAGE;
        }
        return ROOT_PROGRAM_SET;
    }

    @Override
    protected LogInfoParam.Builder getGeneralBuilder(String eventId) {
        TopicHeadViewModel topicHeadData = packageRepository.getTopicHead();
        if (topicHeadData != null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(eventId)
                    .setCurrentPageId(getPageId())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_ID, topicHeadData.getCode())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_NAME, topicHeadData.getName())
                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIEW_PAGE_CHARGEABLE, String.valueOf(topicHeadData.getIsChargeable()));
            return builder;
        }
        return null;
    }

//    protected LogInfoParam.Builder getPlayBuilder(int pos) {
//        int position = pos;
//        if (position == 0) {
//            position = 1;
//        }
//        PackageItemModel bean = ((BaseUIViewModel) getBaseTopicRepository().getRecyclerViewModel().getClickableUiViewModels().get(position)).getSeverModel();
//        if (bean == null) {
//            return null;
//        }
//        LogInfoParam.Builder builder = getGeneralBuilder(VIEW_CLICK);
//        if (builder != null) {
//            builder.setNextPageId(ROOT_PLAY);
//            String code = bean.getContentCode();
//            String name = bean.getDisplayName();
//            builder.putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, code)
//                    .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, name)
//                    .putEventPropKeyValue(EVENT_PROP_KEY_LOCATION_INDEX, pos + "");
//        }
//        return builder;
//    }

    @Override
    protected LogInfoParam.Builder getOnClickBuilder(ClickableUIViewModel clickableUIViewModel) {
        String nextPageId = BIUtil.getNextPageId(clickableUIViewModel);
        if (nextPageId == null)
            return null;
        LogInfoParam.Builder builder = getGeneralBuilder(VIEW_CLICK);
        if (builder != null) {
            BaseUIViewModel baseUIViewModel = (BaseUIViewModel) clickableUIViewModel;
            PackageItemModel packageItemModel = baseUIViewModel.getSeverModel();
            if (packageItemModel != null) {
                builder.putEventPropKeyValue(EVENT_PROP_KEY_EVENT_ID, packageItemModel.getContentCode())
                        .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_NAME, packageItemModel.getDisplayName())
                        .setNextPageId(nextPageId);
            }
        }
        return builder;
    }


}
