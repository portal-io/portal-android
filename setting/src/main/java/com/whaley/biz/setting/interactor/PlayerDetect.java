package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.response.Response;
import com.whaley.biz.setting.api.PlayerDetectApi;
import com.whaley.biz.setting.model.DectectInfo;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/9/4.
 */

public class PlayerDetect extends UseCase<Response, DectectInfo> {

    public PlayerDetect(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<Response> buildUseCaseObservable(final DectectInfo param) {
        return getRepositoryManager().obtainRemoteService(PlayerDetectApi.class).comitDetectResult(param);
    }

}

