package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.program.api.StatisticApi;
import com.whaley.biz.program.model.CountModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangzhi on 2017/9/8.
 */

public class GetPlayStatisic extends UseCase<CMSResponse<CountModel>, String> {

    public GetPlayStatisic() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<CMSResponse<CountModel>> buildUseCaseObservable(String s) {
        return getRepositoryManager().obtainRemoteService(StatisticApi.class)
                .requestStatistic(s)
                .map(new ResponseFunction<CMSResponse<CountModel>, CMSResponse<CountModel>>());
    }
}
