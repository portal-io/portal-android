package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.function.RefreshTokenFunction;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.api.UserInfoApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.biz.user.ui.repository.EditNickNameRepository;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:修改昵称
 */

public class UpdateNickname extends BaseUseCase<String, String> {

    public UpdateNickname(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final UseCaseParam<String> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserInfoApi.class)
                .updateNickname(UserManager.getInstance().getDeviceId(),
                        UserManager.getInstance().getAccesstoken(),
                        getRepositoryManager().obtainMemoryService(EditNickNameRepository.class).getUserName())
                .map(new CommonFunction<WhaleyStringResponse, String>())
                .retryWhen(new RetryWhenRefreshTokenFunction<Observable<Throwable>, String>() {
                    @Override
                    public ObservableSource<String> retryWhenThrowable() {
                        return buildUseCaseObservable(param);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        UserModel userModel = UserManager.getInstance().getUser();
                        userModel.setNickename(getRepositoryManager().obtainMemoryService(EditNickNameRepository.class).getUserName());
                        UserManager.getInstance().saveUser(userModel);
                    }
                });
    }
}
