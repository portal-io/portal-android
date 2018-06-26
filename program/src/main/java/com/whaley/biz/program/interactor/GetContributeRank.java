package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.program.api.VRGiftApi;
import com.whaley.biz.program.model.response.GiftListCountResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/10/12 17:04.
 */

public class GetContributeRank extends UseCase<GiftListCountResponse,String> {

    public GetContributeRank() {
        super();
    }

    public GetContributeRank(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<GiftListCountResponse> buildUseCaseObservable(String s) {
        return getRepositoryManager().obtainRemoteService(VRGiftApi.class)
                .listCount(s)
                .map(new ResponseFunction<GiftListCountResponse, GiftListCountResponse>());
    }
}
