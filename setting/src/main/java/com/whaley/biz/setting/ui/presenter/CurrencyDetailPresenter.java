package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.setting.interactor.RequestCurrencyList;
import com.whaley.biz.setting.model.PayContentModel;
import com.whaley.biz.setting.response.CurrencyDetailsResponse;
import com.whaley.biz.setting.ui.repository.CurrencyDetailRepository;
import com.whaley.biz.setting.ui.view.CurrencyDetailView;
import com.whaley.biz.setting.ui.viewmodel.PayDetailViewModel;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/10/13.
 */

public class CurrencyDetailPresenter extends LoadPresenter<CurrencyDetailView> {

    @Repository
    CurrencyDetailRepository repository;

    @UseCase
    RequestCurrencyList requestCurrencyList;

    public CurrencyDetailPresenter(CurrencyDetailView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    public CurrencyDetailRepository getCurrencyDetailRepository() {
        return repository;
    }

    @Override
    public void onLoadMore() {
        int pageIndex = getLoaderRepository().getLoadListData().getPage() + 1;
        RequestCurrencyList.RequestCurrencyListParam param = new RequestCurrencyList.RequestCurrencyListParam(pageIndex, 20);
        loadMore(requestCurrencyList.buildUseCaseObservable(param));
    }

    @Override
    public void onRefresh() {
        RequestCurrencyList.RequestCurrencyListParam param = new RequestCurrencyList.RequestCurrencyListParam(0, 20);
        refresh(requestCurrencyList.buildUseCaseObservable(param));
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<CurrencyDetailsResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<CurrencyDetailsResponse> o) throws Exception {
                List<PayDetailViewModel> list = new ArrayList<>();
                List<PayContentModel> listDatas = o.getLoadListData().getListData();
                for (PayContentModel payContentModel : listDatas) {
                    PayDetailViewModel payDetailViewModel = new PayDetailViewModel();
                    payDetailViewModel.setName(payContentModel.getMerchandiseName());
                    payDetailViewModel.setTime(DateUtils.foramteToYYYYMMDDHHMM(payContentModel.getUpdateTime()));
                    String payChannel;
                    if(payContentModel.getCurrency().equals("JINGBI")){
                        payChannel="鲸币支付";
                        payDetailViewModel.setPrice(String.format(Locale.CHINESE, "%s鲸币", payContentModel.getAmount()));
                    }else {
                        payDetailViewModel.setPrice(String.format(Locale.CHINESE, "¥%s", SettingUtil.fromFenToYuan(payContentModel.getAmount())));
                        if ("weixin".equals(payContentModel.getPlatform())) {
                            payChannel = "微信支付";
                        } else if ("alipay".equals(payContentModel.getPlatform())) {
                            payChannel = "支付宝支付";
                        } else {
                            payChannel = "苹果支付";
                        }
                    }
                    payDetailViewModel.setChannel(payChannel);
                    list.add(payDetailViewModel);
                }
                o.getLoadListData().setViewDatas(list);
            }
        };
    }

}

