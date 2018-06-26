package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.LoadMoreObserver;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.common.response.ListTabResponse;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.common.ui.viewmodel.LoadListData;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.response.RedemptionCodeRespose;
import com.whaley.biz.setting.router.FormatPageModel;
import com.whaley.biz.setting.router.GoPageUtil;
import com.whaley.biz.setting.util.RedemptionErrorUtil;
import com.whaley.biz.setting.event.RedemptionCodeEvent;
import com.whaley.biz.setting.interactor.ConvertRedeem;
import com.whaley.biz.setting.interactor.RequestCouponList;
import com.whaley.biz.setting.interactor.RequestRedeem;
import com.whaley.biz.setting.model.RedeemCodeDateModel;
import com.whaley.biz.setting.model.RedemptionCodeModel;
import com.whaley.biz.setting.ui.repository.RedemptionCodeRepository;
import com.whaley.biz.setting.ui.view.ConvertFragment;
import com.whaley.biz.setting.ui.view.PayDetailFragment;
import com.whaley.biz.setting.ui.view.RedemptionCodeView;
import com.whaley.biz.setting.ui.viewmodel.RedemptionCodeViewModel;
import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.viewholder.IViewHolder;
import com.whaleyvr.core.network.http.exception.NetworkErrorException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/3.
 */

public class RedemptionCodePresenter extends LoadPresenter<RedemptionCodeView> implements ProgramConstants {

    static final String STR_UNITY = "str_fromUnity";

    static final String REDEMPTION_CODE_MODEL = "redemptionCodeModel";

    @Repository
    RedemptionCodeRepository repository;

    @UseCase
    RequestRedeem requestRedeem;

    @UseCase
    RequestCouponList requestCouponList;

    @UseCase
    ConvertRedeem convertRedeem;

    private Disposable disposable1, disposable2;

    public RedemptionCodePresenter(RedemptionCodeView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        EventController.regist(this);
        if (arguments != null) {
            getRedemptionCodeRepository().setUnity(arguments.getBoolean(STR_UNITY, false));
            getRedemptionCodeRepository().setRedemptionCodeModel((RedemptionCodeModel) arguments.getSerializable(REDEMPTION_CODE_MODEL));
        }
    }

    @Override
    public void onDestroy() {
        if (getRedemptionCodeRepository().isUnity()) {
            Router.getInstance().buildExecutor("/unity/service/exchange").excute();
        }
        super.onDestroy();
        EventController.unRegist(this);
        if (disposable1 != null) {
            disposable1.dispose();
        }
        if (disposable2 != null) {
            disposable2.dispose();
        }
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
        if (getRedemptionCodeRepository().getRedemptionCodeModel() != null) {
            if (getUIView() != null) {
                getUIView().onSuccessView(getRedemptionCodeRepository().getRedemptionCodeModel());
            }
        }
    }

    public RedemptionCodeRepository getRedemptionCodeRepository() {
        return repository;
    }

    @Override
    public void onLoadMore() {
        getData(false);
    }

    @Override
    public void onRefresh() {
        if (isShowEmpty()) {
            getUIView().getEmptyDisplayView().showLoading(null);
        }
        if (disposable1 != null) {
            disposable1.dispose();
        }
        disposable1 = requestRedeem.buildUseCaseObservable(null).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<RedeemCodeDateModel>() {
                    @Override
                    public void onNext(@NonNull RedeemCodeDateModel redeemCodeDateModel) {
                        if (getUIView() != null) {
                            getUIView().getEmptyDisplayView().showContent();
                        }
                        getData(true);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getUIView() != null) {
                            getUIView().getEmptyDisplayView().showContent();
                        }
                        getData(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<ListTabResponse<RedemptionCodeModel>>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<ListTabResponse<RedemptionCodeModel>> o) throws Exception {
                if (getRedemptionCodeRepository().getRedemptionList() == null) {
                    changeRedemptionCodeItemList();
                }
                List<RedemptionCodeViewModel> list = new ArrayList<>();
                list.addAll(getRedemptionCodeRepository().getRedemptionList());
                if (o.getLoadListData().getListData() == null || o.getLoadListData().getListData().size() <= 0) {
                    return;
                }
                RedemptionCodeViewModel redemptionCodeItemName = new RedemptionCodeViewModel(RedemptionCodeViewModel.TYPE_NAME);
                list.add(redemptionCodeItemName);
                List<RedemptionCodeModel> listDatas = o.getLoadListData().getListData();
                for (RedemptionCodeModel redemptionCodeModel : listDatas) {
                    RedemptionCodeViewModel redemptionCodeViewModel = new RedemptionCodeViewModel(redemptionCodeModel, RedemptionCodeViewModel.TYPE_VOUCHER);
                    list.add(redemptionCodeViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }


    private void changeRedemptionCodeItemList() {
        List<RedemptionCodeViewModel> redemptionList = new ArrayList<>();
        RedemptionCodeViewModel redemptionCodeItemBox = new RedemptionCodeViewModel(RedemptionCodeViewModel.TYPE_BOX);
        getRedemptionCodeRepository().setRedemptionCodeItemBox(redemptionCodeItemBox);
        redemptionList.add(redemptionCodeItemBox);
        getRedemptionCodeRepository().setRedemptionList(redemptionList);
    }

    private void getData(final boolean isRefresh) {
        int pageIndex;
        if (isRefresh) {
            pageIndex = 0;
        } else {
            pageIndex = getLoaderRepository().getLoadListData().getPage() + 1;
        }
        RequestCouponList.RequestCouponListParam param = new RequestCouponList.RequestCouponListParam(pageIndex, 20);
        Observable<RedemptionCodeRespose> observable = requestCouponList.buildUseCaseObservable(param);
        if (isRefresh) {
            refresh(observable, new RefreshObserver(getUIView(), isShowEmpty()) {
                @Override
                public void onNext(@NonNull LoaderUseCase.LoaderData loaderData) {
                    super.onNext(loaderData);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    getUIView().getIRefreshView().stopRefresh(false);
                    if (!NetworkUtils.isNetworkAvailable()) {
                        if (isShowEmpty())
                            getEmptyDisplayView().showError(new NetworkErrorException("没有网络信号，请检查网络配置后重试", e));
                        else if (getPageView() != null && e != null && !StrUtil.isEmpty(e.getMessage()))
                            getPageView().showToast("没有网络信号，请检查网络配置后重试");
                    } else {
                        getEmptyDisplayView().showContent();
                        if (getRedemptionCodeRepository().getRedemptionList() == null) {
                            changeRedemptionCodeItemList();
                        }
                        if (getLoaderRepository().getLoadListData() == null) {
                            getLoaderRepository().saveLoadListData(new LoadListData());
                        }
                        getLoaderRepository().getLoadListData().setViewDatas(getRedemptionCodeRepository().getRedemptionList());
                        getUIView().updateOnRefresh(getLoaderRepository().getLoadListData().getViewDatas());
                    }
                }
            });
        } else {
            loadMore(observable);
        }
    }

    /**
     * 购买明细
     */
    public void onPayDetailsClick() {
        TitleBarActivity.goPage((Starter) getUIView().getAttachActivity(), 0, PayDetailFragment.class);
    }

    public void onItemDetailClick(IViewHolder viewHolder, int position) {
        if (getLoaderRepository().getLoadListData().getViewDatas() == null
                || position >= getLoaderRepository().getLoadListData().getViewDatas().size()) {
            return;
        }
        RedemptionCodeViewModel redemptionCodeViewModel = (RedemptionCodeViewModel) getLoaderRepository().getLoadListData().getViewDatas().get(position);
        switch (redemptionCodeViewModel.type) {
            case RedemptionCodeViewModel.TYPE_BOX:
                ConvertFragment.goPage(getStater(), viewHolder);
                break;
            case RedemptionCodeViewModel.TYPE_CODE:
                break;
            case RedemptionCodeViewModel.TYPE_NAME:
                break;
            case RedemptionCodeViewModel.TYPE_VOUCHER:
                RedemptionCodeModel redemptionCodeModel = redemptionCodeViewModel.redemptionCodeModel;
                if (TYPE_CONTENT_PACKGE.equals(redemptionCodeModel.getRelatedType())) {
                    GoPageUtil.goPage(getStater(), FormatPageModel.getPackagePageModel(redemptionCodeModel.getRelatedCode()));
                } else if (TYPE_RECORDED.equals(redemptionCodeModel.getRelatedType())) {
                    GoPageUtil.goPage(getStater(), FormatPageModel.getPlayerPageModel(redemptionCodeModel.getPlayData(), false));
                } else if (TYPE_LIVE.equals(redemptionCodeModel.getRelatedType())) {
                    int status = redemptionCodeModel.getLiveStatus();
                    if (LIVE_STATE_BEING == status) {
                        GoPageUtil.goPage(getStater(), FormatPageModel.getPlayerPageModel(redemptionCodeModel.getPlayData(), true));
                    } else if (LIVE_STATE_BEFORE == status) {
                        GoPageUtil.goPage(getStater(), FormatPageModel.getLiveBeforePageModel(redemptionCodeModel.getRelatedCode()));
                    } else {
                        GoPageUtil.goPage(getStater(), FormatPageModel.getLiveAfterPageModel(redemptionCodeModel.getRelatedCode()));
                    }
                }
                break;
        }
    }

    /**
     * 点击兑换
     *
     * @param code
     */
    public void onConvertClick(String code) {
        if (getUIView() != null)
            getUIView().showLoading(null);
        ConvertRedeem.ConvertRedeemParam param = new ConvertRedeem.ConvertRedeemParam(code);
        if (disposable2 != null) {
            disposable2.dispose();
        }
        disposable2 = convertRedeem.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RedemptionCodeModel>() {
                    @Override
                    public void onNext(@NonNull RedemptionCodeModel redemptionCodeModel) {
                        onConvertSuccess(redemptionCodeModel);
                        if (getUIView() != null) {
                            getUIView().removeLoading();
                            getUIView().onSuccessView(redemptionCodeModel);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (getUIView() != null) {
                            getUIView().removeLoading();
                        }
                        if (e instanceof StatusErrorThrowable) {
                            StatusErrorThrowable e1 = (StatusErrorThrowable) e;
                            showDialog(RedemptionErrorUtil.getErrorSting(e1.getSubCode(), e1.getMessage()));
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void showDialog(String msg) {
        DialogUtil.showDialog(getUIView().getAttachActivity(), msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RedemptionCodeEvent event) {
        RedemptionCodeModel redemptionCodeModel = event.getRedemptionCodeModel();
        if (redemptionCodeModel != null) {
            if (getUIView() != null) {
                getUIView().onSuccessView(redemptionCodeModel);
            }
        }
    }

    private void onConvertSuccess(RedemptionCodeModel redemptionCodeModel) {
        if (redemptionCodeModel != null) {
            LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                    .setEventId(CONVERT)
                    .setCurrentPageId(ROOT_CONVERT_DETAIL)
                    .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_TYPE, redemptionCodeModel.getRelatedType())
                    .putEventPropKeyValue(EVENT_PROP_KEY_EVENT_ID, redemptionCodeModel.getRelatedCode())
                    .setNextPageId(ROOT_CONVERT_DETAIL);
            Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
        }
    }


}
