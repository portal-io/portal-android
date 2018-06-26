package com.whaley.biz.program.ui.live.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.observer.RefreshObserver;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.interactor.GetMyReserve;
import com.whaley.biz.program.model.ReserveModel;
import com.whaley.biz.program.model.pay.IsPayedListModel;
import com.whaley.biz.program.model.pay.PayResultModel;
import com.whaley.biz.program.model.response.ReserveListResponse;
import com.whaley.biz.program.ui.live.MyReserveView;
import com.whaley.biz.program.ui.live.repository.MyReserveRepository;
import com.whaley.biz.program.ui.uimodel.ReserveViewModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DateUtils;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/8/21.
 */

public class MyReservePresenter extends LoadPresenter<MyReserveView> implements ProgramConstants{

    @Repository
    MyReserveRepository myReserveRepository;

    @UseCase
    GetMyReserve getMyReserve;

    private Disposable disposable;

    public MyReservePresenter(MyReserveView view) {
        super(view);
    }

    public MyReserveRepository getRepository(){
        return myReserveRepository;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    @Override
    public void onLoadMore() {

    }

    public void onRefresh() {
        refresh(getMyReserve.buildUseCaseObservable(null), new RefreshObserver(getUIView(),isShowEmpty()){
            @Override
            public void onNotLoggedInError() {
                super.onNotLoggedInError();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                onIsPayList();
            }
        });
    }

    private void onIsPayList(){
        if (!TextUtils.isEmpty((getRepository().getGoodsNos())) && !TextUtils.isEmpty((getRepository().getGoodsType()))) {
            IsPayedListModel isPayedListModel = new IsPayedListModel(getRepository().getGoodsNos(), getRepository().getGoodsType());
            Router.getInstance().buildExecutor("/pay/service/ispayedlist").putObjParam(isPayedListModel).callback(new Executor.Callback<List<PayResultModel>>() {
                @Override
                public void onCall(List<PayResultModel> payResultModels) {
                    changePayResult(payResultModels);
                }

                @Override
                public void onFail(Executor.ExecutionError executionError) {
                    //
                }
            }).excute();
        }
    }

    private void changePayResult(final List<PayResultModel> models){
        if(disposable!=null){
            disposable.dispose();
        }
        disposable = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                List<ReserveViewModel> reserveViewModels = getLoaderRepository().getLoadListData().getViewDatas();
                for(ReserveViewModel reserveViewModel : reserveViewModels){
                    int index = models.indexOf(PayResultModel.obtain(reserveViewModel.getCode()));
                    if(index >= 0){
                        reserveViewModel.setBuy(models.get(index).isResult());
                    }
                }
                if (e.isDisposed())
                    return;
                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                    }
                    @Override
                    public void onComplete() {
                        if(getUIView()!=null){
                            getUIView().updatePayStatus();
                        }
                    }
                });
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<ReserveListResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<ReserveListResponse> o) throws Exception {
                List<ReserveViewModel> list = new ArrayList<>();
                List<ReserveModel> listDatas = o.getLoadListData().getListData();
                getRepository().setGoodsNos(null);
                getRepository().setGoodsType(null);
                for (ReserveModel reserveModel : listDatas) {
                    ReserveViewModel reserveViewModel = new ReserveViewModel();
                    reserveViewModel.convert(reserveModel);
                    reserveViewModel.setCode(reserveModel.getCode());
                    reserveViewModel.setName(reserveModel.getDisplayName());
                    reserveViewModel.setPic(reserveModel.getPoster());
                    reserveViewModel.setLiveStatus(reserveModel.getLiveStatus());
                    reserveViewModel.setChargeable(reserveModel.getIsChargeable()==1);
                    if (LIVE_STATE_BEFORE == reserveModel.getLiveStatus()) {
                        int timeLeftSeconds = reserveModel.getTimeLeftSeconds();
                        if(timeLeftSeconds > 24*60*60){
                            reserveViewModel.setIntro(DateUtils.foramteToYYYYMMDDHHMM(reserveModel.getBeginTime())+"开播");
                        }else if(timeLeftSeconds > 0){
                            reserveViewModel.setIntro(covertToTimeRemain(reserveModel.getTimeLeftSeconds()));
                        }else{
                            reserveViewModel.setIntro(DateUtils.foramteToYYYYMMDDHHMM(reserveModel.getBeginTime())+"开播");
                        }
                    }else if (LIVE_STATE_BEING == reserveModel.getLiveStatus()){
                        reserveViewModel.setIntro("正在直播");
                    }else{
                        reserveViewModel.setIntro("直播已结束");
                    }
                    list.add(reserveViewModel);
                    getRepository().setGoodsNos(add(getRepository().getGoodsNos(), reserveModel.getCode()));
                    getRepository().setGoodsType(add(getRepository().getGoodsType(), reserveModel.getProgramType()));
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

    private static String add(String original, String add) {
        if (StrUtil.isEmpty(original)) {
            original = add;
        } else {
            original = original + "," + add;
        }
        return original;
    }

    private String covertToTimeRemain(int timeLeftSeconds){
        String remain = "距开始 ";
        int days = timeLeftSeconds / (60 * 60 * 24);
        int hours = (timeLeftSeconds % (60 * 60 * 24)) / (60 * 60);
        int minutes = (timeLeftSeconds % (60 * 60)) / (60);
        if (days > 0) {
            remain = remain + days + "天";
        }
        if (hours > 0) {
            remain = remain + hours + "小时";
        }
        if (minutes > 0) {
            remain = remain + minutes + "分钟";
        }
        else {
            remain = "即将开始";
        }
        return remain;
    }

    public void onClick(int position) {
        ReserveViewModel reserveViewModel = (ReserveViewModel)getLoaderRepository().getLoadListData().getViewDatas().get(position);
        ReserveModel reserveModel = reserveViewModel.getSeverModel();
        GoPageUtil.goPage(getStater(), FormatPageModel.getPageModel(reserveModel));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable!=null){
            disposable.dispose();
        }
    }

}
