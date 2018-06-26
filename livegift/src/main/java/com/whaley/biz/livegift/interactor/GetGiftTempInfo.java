package com.whaley.biz.livegift.interactor;

import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.livegift.api.LiveGiftApi;
import com.whaley.biz.livegift.model.GiftTempInfoModel;
import com.whaley.biz.livegift.model.response.GiftTempInfoResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */

public class GetGiftTempInfo extends UseCase<GiftTempInfoModel, String> {


    public GetGiftTempInfo() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<GiftTempInfoModel> buildUseCaseObservable(String code) {
        return getRepositoryManager().obtainRemoteService(LiveGiftApi.class)
                .getGiftTempInfo(code)
                .map(new CommonFunction<GiftTempInfoResponse, GiftTempInfoModel>());
    }
}
