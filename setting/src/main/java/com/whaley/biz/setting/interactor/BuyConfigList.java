package com.whaley.biz.setting.interactor;
;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.setting.api.CurrencyAPI;
import com.whaley.biz.setting.response.CurrencyResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/10/13.
 */

public class BuyConfigList extends UseCase<CurrencyResponse, String> {

    public BuyConfigList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CurrencyResponse> buildUseCaseObservable(final String param) {
        return getRepositoryManager().obtainRemoteService(CurrencyAPI.class)
                .buyConfigList().map(new ResponseFunction<CurrencyResponse, CurrencyResponse>());
    }

}
