package com.whaley.biz.program.interactor;

import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.VRGiftApi;
import com.whaley.biz.program.model.response.GiftMyCountResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/10/12 16:17.
 */

public class GetMyContribute extends UseCase<GiftMyCountResponse,Void>{

    CheckLogin checkLogin;

    public GetMyContribute() {
        checkLogin = new CheckLogin();
    }

    public GetMyContribute(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
        this.checkLogin = new CheckLogin();
    }

    @Override
    public Observable<GiftMyCountResponse> buildUseCaseObservable(Void aVoid) {
        return checkLogin.buildUseCaseObservable(null)
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<UserModel, String>() {
                    @Override
                    public String apply(@NonNull UserModel userModel) throws Exception {
                        return userModel.getAccount_id();
                    }
                })
                .observeOn(Schedulers.io())
                .concatMap(new Function<String, ObservableSource<GiftMyCountResponse>>() {
                    @Override
                    public ObservableSource<GiftMyCountResponse> apply(@NonNull String uid) throws Exception {
                        return getRepositoryManager().obtainRemoteService(VRGiftApi.class).myGiftCount(uid);
                    }
                })
                .map(new ResponseFunction<GiftMyCountResponse,GiftMyCountResponse>());
    }
}
