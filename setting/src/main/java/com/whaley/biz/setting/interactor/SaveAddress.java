package com.whaley.biz.setting.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.biz.setting.api.GiftAPI;
import com.whaley.biz.setting.model.AddressModel;
import com.whaley.biz.setting.response.AddressResponse;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/7/25.
 */

public class SaveAddress extends UseCase<AddressResponse, AddressModel> {

    public SaveAddress(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<AddressResponse> buildUseCaseObservable(AddressModel param) {
        return getRepositoryManager().obtainRemoteService(GiftAPI.class)
                .saveAddress(param)
                .map(new ResponseFunction<AddressResponse,AddressResponse>());
    }

}

