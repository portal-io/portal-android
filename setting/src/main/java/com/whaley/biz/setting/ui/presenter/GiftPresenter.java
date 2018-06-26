package com.whaley.biz.setting.ui.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.setting.interactor.Setting;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.event.ModifyAddressEvent;
import com.whaley.biz.setting.interactor.RequestAddress;
import com.whaley.biz.setting.interactor.RequestGift;
import com.whaley.biz.setting.model.PrizeDataModel;
import com.whaley.biz.setting.response.GiftListResponse;
import com.whaley.biz.setting.ui.repository.GiftRepository;
import com.whaley.biz.setting.ui.view.AddressFragment;
import com.whaley.biz.setting.ui.view.GiftView;
import com.whaley.biz.setting.ui.viewmodel.AddressViewModel;
import com.whaley.biz.setting.ui.viewmodel.GiftViewModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/1.
 */

public class GiftPresenter extends LoadPresenter<GiftView> {

    static final String STR_FROM_UNITY = "str_fromUnity";

    @Repository
    GiftRepository repository;

    @UseCase
    RequestGift requestGift;

    @UseCase
    RequestAddress requestAddress;

    private Disposable disposable;

    public GiftPresenter(GiftView view) {
        super(view);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        if(arguments!=null){
            getGiftRepository().setFromUnity(arguments.getBoolean(STR_FROM_UNITY, false));
        }
        regist();
    }

    @Override
    public void onDestroy() {
        if(getGiftRepository().isFromUnity()){
            Router.getInstance().buildExecutor("/unity/service/resume").excute();
        }
        super.onDestroy();
        unRegist();
        if(disposable!=null){
            disposable.dispose();
        }
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    public GiftRepository getGiftRepository() {
        return repository;
    }

    public void onRefresh() {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        RequestAddress.RequestAddressParam param = new RequestAddress.RequestAddressParam(timestamp);
        if(isShowEmpty()) {
            getUIView().getEmptyDisplayView().showLoading(null);
        }
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = requestAddress.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<AddressViewModel>() {
                    @Override
                    public void onFinalError(Throwable e) {
                        if(getUIView()!=null){
                            getUIView().getIRefreshView().stopRefresh(false);
                            getUIView().getEmptyDisplayView().showError(e);
                        }
                    }
                    @Override
                    public void onStatusError(int status, String message) {
                        if(getUIView()!=null){
                            getUIView().getIRefreshView().stopRefresh(false);
                            getUIView().showToast(message);
                            getUIView().getEmptyDisplayView().showError(new StatusErrorThrowable(status, message));
                        }
                    }
                    @Override
                    public void onNoDataError() {
                        if(getUIView()!=null){
                            getUIView().getIRefreshView().stopRefresh(false);
                            getUIView().getEmptyDisplayView().showEmpty();
                        }
                    }
                    @Override
                    public void onNext(@NonNull AddressViewModel addressViewModel) {
                        //
                    }
                    @Override
                    public void onComplete() {
                        if(getUIView()!=null){
                            getUIView().updateAddress();
                            getUIView().getEmptyDisplayView().showContent();
                        }
                        requestGift();
                    }
                });
    }

    private void requestGift(){
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        UseCaseParam<RequestGift.RequestGiftParam> useCaseParam = new UseCaseParam<>(new RequestGift.RequestGiftParam(timestamp));
        refresh(requestGift.buildUseCaseObservable(useCaseParam), new RefreshObserver(getUIView(), isShowEmpty()){

        });
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<GiftListResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<GiftListResponse> o) throws Exception {
                List<GiftViewModel> list = new ArrayList<>();
                List<PrizeDataModel> listDatas = o.getLoadListData().getListData();
                for (PrizeDataModel prizeDataModel : listDatas) {
                    GiftViewModel giftViewModel = new GiftViewModel();
                    giftViewModel.setDate(SettingUtil.getDateFromSeconds(prizeDataModel.getDateline()));
                    giftViewModel.setYear(SettingUtil.getYearFromSeconds(prizeDataModel.getDateline()));
                    if(giftViewModel.getYear().equals(SettingUtil.getYearFromSeconds(""+System.currentTimeMillis()/1000))){
                        giftViewModel.setMonth("本月");
                    }else {
                        giftViewModel.setMonth(SettingUtil.getMonthFromSeconds(prizeDataModel.getDateline()));
                    }
                    giftViewModel.setPic(prizeDataModel.getPicture());
                    giftViewModel.setTitle(giftViewModel.getDate()+"我抽中了");
                    giftViewModel.setContent(prizeDataModel.getName());
                    giftViewModel.setInfo(prizeDataModel.getInfo());
                    if("1".equals(prizeDataModel.getGoodstype())){
                        giftViewModel.setType(1);
                    }else if("2".equals(prizeDataModel.getGoodstype())){
                        giftViewModel.setType(2);
                    }else{
                        giftViewModel.setType(0);
                    }
                    list.add(giftViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

    public void onModifyAddress(){
        AddressViewModel addressViewModel = getGiftRepository().getAddressViewModel();
        AddressFragment.goPage(getStater(), addressViewModel);
    }

    public void onItemClick(int position){
        GiftViewModel giftViewModel = (GiftViewModel)getLoaderRepository().getLoadListData().getViewDatas().get(position);
        if(giftViewModel != null && !TextUtils.isEmpty(giftViewModel.getInfo())){
            try {
                ClipboardManager myClipboard = (ClipboardManager) AppContextProvider.getInstance().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", giftViewModel.getInfo());
                myClipboard.setPrimaryClip(myClip);
                if(getUIView() != null) {
                    getUIView().showToast("链接已成功复制到剪贴板");
                }
            } catch (Exception e) {
                //
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ModifyAddressEvent event) {
        AddressViewModel addressViewModel = event.getAddressViewModel();
        getGiftRepository().setAddressViewModel(addressViewModel);
        String text = "";
        if(addressViewModel!=null){
            text = addressViewModel.getAddress();
        }
        if(TextUtils.isEmpty(text)){
            getGiftRepository().setAddress("");
            getGiftRepository().setBtnText("添加");
        }else{
            getGiftRepository().setAddress("当前地址：" + text);
            getGiftRepository().setBtnText("修改");
        }
        if(getUIView()!=null){
            getUIView().updateAddress();
        }
    }

}
