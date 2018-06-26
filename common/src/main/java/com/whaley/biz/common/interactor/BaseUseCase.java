package com.whaley.biz.common.interactor;

import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date: 2017/7/14
 */

public abstract class BaseUseCase<T, PARAM> extends UseCase<T, UseCaseParam<PARAM>> {

    public BaseUseCase() {
    }


    public BaseUseCase(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

}
