package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.VRGiftApi;
import com.whaley.biz.program.model.response.GiftTotalCountResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/10/12 17:04.
 */

public class GetTotalContribute extends UseCase<GiftTotalCountResponse,String> {

    public GetTotalContribute() {
        super();
    }

    public GetTotalContribute(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<GiftTotalCountResponse> buildUseCaseObservable(String s) {
        return getRepositoryManager().obtainRemoteService(VRGiftApi.class)
                .totalCount(s)
                .map(new ResponseFunction<GiftTotalCountResponse, GiftTotalCountResponse>());
    }
}
