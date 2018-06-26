package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.setting.interactor.BuyConfigList;
import com.whaley.biz.setting.interactor.GetUserAmount;
import com.whaley.biz.setting.model.CurrencyModel;
import com.whaley.biz.setting.response.CurrencyResponse;
import com.whaley.biz.setting.ui.repository.CurrencyRepository;
import com.whaley.biz.setting.ui.view.CurrencyDetailFragment;
import com.whaley.biz.setting.ui.view.CurrencyView;
import com.whaley.biz.setting.ui.view.PayDetailFragment;
import com.whaley.biz.setting.ui.viewmodel.CurrencyViewModel;
import com.whaley.biz.setting.util.PayUtil;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/10/12.
 */

public class CurrencyPresenter extends LoadPresenter<CurrencyView> {

    @Repository
    CurrencyRepository currencyRepository;

    @UseCase
    GetUserAmount getUserAmount;

    @UseCase
    BuyConfigList buyConfigList;

    private Disposable disposable;

    public CurrencyPresenter(CurrencyView view) {
        super(view);
    }

    public CurrencyRepository getRepository() {
        return currencyRepository;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        regist();
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        refresh(true);
    }

    private void refresh(final boolean flag) {
        if (isShowEmpty()) {
            getUIView().getEmptyDisplayView().showLoading(null);
        }
        if (disposable != null) {
            disposable.dispose();
        }
        disposable = getUserAmount.buildUseCaseObservable(null)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<Integer>() {
                    @Override
                    public void onFinalError(Throwable e) {
                        if (getUIView() != null) {
                            getUIView().getIRefreshView().stopRefresh(false);
                            getUIView().getEmptyDisplayView().showError(e);
                        }
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        if (getUIView() != null) {
                            getUIView().getIRefreshView().stopRefresh(false);
                            getUIView().showToast(message);
                            getUIView().getEmptyDisplayView().showError(new StatusErrorThrowable(status, message));
                        }
                    }

                    @Override
                    public void onNoDataError() {
                        if (getUIView() != null) {
                            getUIView().getIRefreshView().stopRefresh(false);
                            getUIView().getEmptyDisplayView().showEmpty();
                        }
                    }

                    @Override
                    public void onNext(@NonNull Integer o) {
                        getRepository().setAmount(o.intValue());
                        EventController.postEvent(new BaseEvent("/setting/event/user_amount_update", o.intValue()));
                    }

                    @Override
                    public void onComplete() {
                        if (getUIView() != null) {
                            getUIView().updateAmount(getRepository().getAmount());
                            getUIView().showCurencyContent();
                            getUIView().getEmptyDisplayView().showContent();
                        }
                        if (flag)
                            requestBuyConfigList();
                    }
                });
    }

    private void requestBuyConfigList() {
        refresh(buyConfigList.buildUseCaseObservable(null), new RefreshObserver(getUIView(), isShowEmpty()) {
            @Override
            public void onError(@NonNull Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNoDataError() {
                super.onNoDataError();
            }
        });
    }

    @Override
    public boolean isShowEmpty() {
        return true;
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<CurrencyResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<CurrencyResponse> o) throws Exception {
                List<CurrencyViewModel> list = new ArrayList<>();
                List<CurrencyModel> listDatas = o.getLoadListData().getListData();
                for (CurrencyModel currencyModel : listDatas) {
                    CurrencyViewModel currencyViewModel = new CurrencyViewModel();
                    currencyViewModel.convert(currencyModel);
                    currencyViewModel.setName(currencyModel.getWhaleyCurrencyNumber() + "鲸币");
                    if (currencyModel.getWhaleyCurrencyGiveNumber() > 0)
                        currencyViewModel.setPreferName("（赠送" + currencyModel.getWhaleyCurrencyGiveNumber() + "鲸币）");
                    currencyViewModel.setContent(SettingUtil.fromFenToYuan("" + currencyModel.getPrice()) + "元");
                    if (currencyModel.getPreferPrice() > 0) {
                        currencyViewModel.setPrefer(true);
                        currencyViewModel.setPreferContent(SettingUtil.fromFenToYuan("" + currencyModel.getPreferPrice()) + "元");
                    }
                    list.add(currencyViewModel);
                }
                CurrencyViewModel currencyViewModel = new CurrencyViewModel();
                currencyViewModel.setType(1);
                list.add(currencyViewModel);
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case "currency_pay":
                refresh(false);
                break;
        }
    }

    public void onBtnClick(int position) {
        CurrencyViewModel currencyViewModel = (CurrencyViewModel) getLoaderRepository().getLoadListData().getViewDatas().get(position);
        if (currencyViewModel.getType() == 1) {
            WebviewGoPageModel webviewGoPageModel = WebviewGoPageModel.createWebviewGoPageModel("file:///android_asset/currency.html",
                    TitleBarModel.createTitleBarModel("鲸币说明"));
            Router.getInstance().buildExecutor("/hybrid/service/goPage").putObjParam(webviewGoPageModel).excute();
        } else {
            CurrencyModel currencyModel = currencyViewModel.getSeverModel();
            if (currencyModel != null && getUIView() != null) {
                PayUtil.showCurrencyPayDialog(getUIView().getFragmentManager(), GsonUtil.getGson().toJson(currencyModel));
            }
        }
    }

    /**
     * 充值明细
     */
    public void onCurrencyDetailsClick() {
        TitleBarActivity.goPage((Starter) getUIView().getAttachActivity(), 0, CurrencyDetailFragment.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegist();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }

}
