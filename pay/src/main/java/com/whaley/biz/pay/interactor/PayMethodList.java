package com.whaley.biz.pay.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.pay.PayApi;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.model.program.PayMethodModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.GsonUtil;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Author: qxw
 * Date:2017/7/20
 * Introduction:检查支付渠道
 */

@Route(path = "/pay/usecase/paymethodlist")
public class PayMethodList extends UseCase<PayMethodModel, String> implements IProvider {

    public PayMethodList() {

    }

    public PayMethodList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<PayMethodModel> buildUseCaseObservable(String param) {
        return getRepositoryManager()
                .obtainRemoteService(PayApi.class)
                .payMethodList("android", CommonConstants.VALUE_APP_VERSION_NAME)
                .map(new CommonFunction<CMSResponse<PayMethodModel>, PayMethodModel>())
                .doOnNext(new Consumer<PayMethodModel>() {
                    @Override
                    public void accept(@NonNull PayMethodModel payMethodModel) throws Exception {
                        String payMethodString = GsonUtil.getGson().toJson(payMethodModel);
                        PayUtil.setPayMethod(payMethodString);
                    }
                });
    }

    @Override
    public void init(Context context) {

    }
}
