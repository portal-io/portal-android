package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.user.api.PortalApi;
import com.whaley.biz.user.model.portal.AccountModel;
import com.whaley.biz.user.model.portal.SyncResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2018/8/8.
 */

public class SyncPortal extends UseCase<SyncResponse, String> {

    public SyncPortal(IRepositoryManager repositoryManager){
        this(repositoryManager, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public SyncPortal(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<SyncResponse> buildUseCaseObservable(String param) {
        return getRepositoryManager()
                .obtainRemoteService(PortalApi.class)
                .sync(new AccountModel(param))
                .map(new ResponseFunction<SyncResponse, SyncResponse>());
    }

}

