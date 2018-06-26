package com.whaley.biz.setting.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.interactor.LoaderUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.ui.presenter.LoadPresenter;
import com.whaley.biz.setting.util.SettingUtil;
import com.whaley.biz.setting.interactor.RequestOrderList;
import com.whaley.biz.setting.model.PayContentModel;
import com.whaley.biz.setting.response.PayDetailsResponse;
import com.whaley.biz.setting.ui.repository.PayDetailRepository;
import com.whaley.biz.setting.ui.view.PayDetailView;
import com.whaley.biz.setting.ui.viewmodel.PayDetailViewModel;
import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by dell on 2017/8/4.
 */

public class PayDetailPresenter extends LoadPresenter<PayDetailView>{

    @Repository
    PayDetailRepository repository;

    @UseCase
    RequestOrderList requestOrderList;

    public PayDetailPresenter(PayDetailView view) {
        super(view);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        onRefresh();
    }

    public PayDetailRepository getPayDetailRepository(){
        return repository;
    }

    @Override
    public void onLoadMore() {
        int pageIndex = getLoaderRepository().getLoadListData().getPage() + 1;
        RequestOrderList.RequestOrderListParam param = new RequestOrderList.RequestOrderListParam(pageIndex, 20);
        loadMore(requestOrderList.buildUseCaseObservable(new UseCaseParam(param)));

    }

    @Override
    public void onRefresh() {;
        RequestOrderList.RequestOrderListParam param = new RequestOrderList.RequestOrderListParam(0, 20);
        refresh(requestOrderList.buildUseCaseObservable(new UseCaseParam(param)));
    }

    @Override
    protected Consumer getConvertConsumer() {
        return new Consumer<LoaderUseCase.LoaderData<PayDetailsResponse>>() {
            @Override
            public void accept(@NonNull LoaderUseCase.LoaderData<PayDetailsResponse> o) throws Exception {
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
