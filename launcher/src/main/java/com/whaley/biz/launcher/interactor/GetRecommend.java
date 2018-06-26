package com.whaley.biz.launcher.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.CommonConstants;

import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.response.BaseResponse;
import com.whaley.biz.launcher.api.RecommendAPI;
import com.whaley.biz.launcher.model.RecommendAreasBean;
import com.whaley.biz.launcher.model.responce.RecommendResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RemoteRepository;
import com.whaley.core.repository.RepositoryManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/7/27 17:59.
 */

public class GetRecommend extends UseCase<RecommendResponse, String> implements IProvider {

    public GetRecommend() {
        super(RepositoryManager.create(RemoteRepository.getInstance()), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public GetRecommend(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RecommendResponse> buildUseCaseObservable(final String code) {
        return getRepositoryManager().obtainRemoteService(RecommendAPI.class)
                .requestAllRecommend(code, CommonConstants.VALUE_ANDROID_VERSION_NAME)
                .map(new ResponseFunction<RecommendResponse, RecommendResponse>());
    }

    @Override
    public void init(Context context) {

    }

}
