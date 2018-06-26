package com.whaley.biz.launcher.interactor;

import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.launcher.api.FestivalApi;
import com.whaley.biz.launcher.model.FestivalModel;
import com.whaley.biz.launcher.model.responce.FestivalResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2018/1/25.
 */

public class GetFestival extends UseCase<FestivalModel, String>  {

    public GetFestival(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<FestivalModel> buildUseCaseObservable(final String code) {
        return getRepositoryManager().obtainRemoteService(FestivalApi.class)
                .findEventDetail()
                .map(new CommonCMSFunction<FestivalResponse, FestivalModel>());
    }

}
