package com.whaley.biz.program.interactor;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.interactor.ListTabFunction;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.api.RecommendAPI;
import com.whaley.biz.program.model.RecommendAreasBean;
import com.whaley.biz.program.model.response.RecommendResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/10.
 */

public class GetLiveTab extends UseCase<List<RecommendAreasBean>,String> implements ProgramConstants{

    public GetLiveTab(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<List<RecommendAreasBean>> buildUseCaseObservable(String param) {
        return getRepositoryManager().obtainRemoteService(RecommendAPI.class)
                .requestAllRecommend(param, CommonConstants.VALUE_ANDROID_VERSION_NAME)
                .map(new ListTabFunction<RecommendResponse,RecommendAreasBean>());
    }

}

