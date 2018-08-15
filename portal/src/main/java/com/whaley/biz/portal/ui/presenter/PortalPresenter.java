package com.whaley.biz.portal.ui.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.portal.interactor.CollectPortal;
import com.whaley.biz.portal.interactor.GetEarning;
import com.whaley.biz.portal.interactor.GetPortalRecord;
import com.whaley.biz.portal.model.EarningInfo;
import com.whaley.biz.portal.model.PortalCollectModel;
import com.whaley.biz.portal.response.EarningInfoResponse;
import com.whaley.biz.portal.response.PortalRecordResponse;
import com.whaley.biz.portal.ui.repository.PortalRepository;
import com.whaley.biz.portal.ui.view.PortalView;
import com.whaley.biz.portal.ui.viewmodel.PortalViewModel;
import com.whaley.biz.portal.utils.RelativeDateFormat;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2018/8/2.
 */

public class PortalPresenter extends LoadPresenter<PortalView> {

    @Repository
    PortalRepository repository;

    @UseCase
    GetPortalRecord getPortalRecord;

    @UseCase
    GetEarning getEarning;

    @UseCase
    CollectPortal collectPortal;

    private Disposable disposable;

    public PortalPresenter(PortalView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    public PortalRepository getGiftRepository() {
        return repository;
    }

    public void onRefresh() {
        if(isShowEmpty()) {
            getUIView().getEmptyDisplayView().showLoading(null);
        }
        if(disposable !=null){
            disposable.dispose();
        }
        disposable = getPortalRecord.buildUseCaseObservable(null)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<PortalRecordResponse>() {
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
                    public void onNext(@NonNull PortalRecordResponse portalRecordResponse) {
                        if(getUIView()!=null){
                            getUIView().getEmptyDisplayView().showContent();
                            getUIView().updateBalance(portalRecordResponse.getPortalInfo().getBalance());
                            getUIView().showBind(!TextUtils.isEmpty(portalRecordResponse.getUserInfo().getPortalAddress()));
                            getUIView().updateRecode(portalRecordResponse.getList());
                        }
                        getEarnings();
                    }
                    @Override
                    public void onComplete() {
                       //
                    }
                });
    }

    private void getEarnings(){
        GetEarning.Param param = new GetEarning.Param(0,30);
        refresh(getEarning.buildUseCaseObservable(param));
    }

    @Override
    public void onLoadMore() {
        int pageIndex = getLoaderRepository().getLoadListData().getPage() + 1;
        GetEarning.Param param = new GetEarning.Param(pageIndex,30);
        loadMore(getEarning.buildUseCaseObservable(param));
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<EarningInfoResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<EarningInfoResponse> o) throws Exception {
                List<PortalViewModel> list = new ArrayList<>();
                List<EarningInfo> listDatas = o.getLoadListData().getListData();
                for (EarningInfo earningInfo : listDatas) {
                    PortalViewModel portalViewModel = new PortalViewModel();
                    portalViewModel.setCode(earningInfo.getCode());
                    portalViewModel.setValue("+"+earningInfo.getValue()+" portal");
                    portalViewModel.setTime(RelativeDateFormat.format(earningInfo.getCreateTime()));
                    int type = 0;
                    String name = "日常领取";
                    if("PLAY_VIDEO".equals(earningInfo.getType())){
                        type= 1;
                        name = "观看《"+earningInfo.getName()+"》";
                    }
                    portalViewModel.setType(type);
                    portalViewModel.setName(name);
                    list.add(portalViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

    public void onBind(){

    }

    public void onCollect(String code){
        collectPortal.buildUseCaseObservable(new PortalCollectModel(code))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable !=null){
            disposable.dispose();
            disposable =null;
        }
    }

}
