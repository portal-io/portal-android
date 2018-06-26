package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.ArrangeApi;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.TopicListResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/10
 * Introduction:专题手动编排
 */
@Route(path = ProgramRouterPath.USECASE_TOPIC)
public class GetTopicList extends UseCase<TopicListResponse, String> implements IProvider {

    public GetTopicList() {
    }

    public GetTopicList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<TopicListResponse> buildUseCaseObservable(String code) {
        return getRepositoryManager().obtainRemoteService(ArrangeApi.class).requestTopicList(code)
                .map(new ResponseFunction<TopicListResponse, TopicListResponse>());
    }

    @Override
    public void init(Context context) {

    }
}
