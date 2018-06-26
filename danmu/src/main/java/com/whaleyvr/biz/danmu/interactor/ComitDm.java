package com.whaleyvr.biz.danmu.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.core.repository.IRepositoryManager;
import com.whaleyvr.biz.danmu.api.DanmuAPI;
import com.whaleyvr.biz.danmu.model.DmModel;
import com.whaleyvr.biz.danmu.response.DmComitResponse;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2017/7/24.
 */

public class ComitDm extends BaseUseCase<DmComitResponse, DmModel> {

    public ComitDm(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<DmComitResponse> buildUseCaseObservable(UseCaseParam<DmModel> param) {
        return getRepositoryManager().obtainRemoteService(DanmuAPI.class)
                .comitDm(param.getParam())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new ResponseFunction<DmComitResponse, DmComitResponse>());
    }
}
