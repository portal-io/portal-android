package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.SearchAPI;
import com.whaley.biz.program.model.response.SearchResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/25.
 */

public class GetSearch extends UseCase<SearchResponse, String> {

    public GetSearch(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<SearchResponse> buildUseCaseObservable(String param) {
        return getRepositoryManager().obtainRemoteService(SearchAPI.class).search(param)
                .map(new ResponseFunction<SearchResponse, SearchResponse>());
    }


}
