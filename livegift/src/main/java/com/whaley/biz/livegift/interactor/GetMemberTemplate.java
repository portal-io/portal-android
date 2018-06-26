package com.whaley.biz.livegift.interactor;

import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.livegift.api.LiveGiftApi;
import com.whaley.biz.livegift.model.MemberTemplateModel;
import com.whaley.biz.livegift.model.response.MemberTemplateResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/10/13
 * Introduction:
 */

public class GetMemberTemplate extends UseCase<MemberTemplateModel, String> {

    public GetMemberTemplate() {
        super(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<MemberTemplateModel> buildUseCaseObservable(String code) {
        return getRepositoryManager().obtainRemoteService(LiveGiftApi.class)
                .findByLiveCode(code)
                .map(new CommonFunction<MemberTemplateResponse, MemberTemplateModel>());
    }
}
