package com.whaley.biz.user.interactor;


import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.biz.user.model.response.WhaleyStringResponse;
import com.whaley.biz.user.ui.repository.CompleteUserInfoRepository;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/7/19
 * Introduction:
 */

public class CompleteUserinfo extends BaseUseCase<String, CompleteUserinfo.PerfectUserinfoParam> {


    public CompleteUserinfo(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final UseCaseParam<PerfectUserinfoParam> param) {
        return getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .updateUserinfo(UserManager.getInstance().getDeviceId(),
                        param.getParam().getUserModel().getAccessTokenModel().getAccesstoken(),
                        param.getParam().nickname,
                        param.getParam().password
                ).map(new CommonFunction<WhaleyStringResponse, String>())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        param.getParam().userModel.setNickename(param.getParam().nickname);
                        UserManager.getInstance().saveUser(param.getParam().userModel);
                    }
                });
    }

    public void executeComplete(DisposableObserver<String> observer) {
        CompleteUserInfoRepository repository = getRepositoryManager().obtainMemoryService(CompleteUserInfoRepository.class);
        PerfectUserinfoParam param = new PerfectUserinfoParam(repository.getNickname(), repository.getPassword(), repository.getUserModel());
        execute(observer, new UseCaseParam<>(param));
    }


    static class PerfectUserinfoParam {
        private UserModel userModel;
        private String nickname;
        private String password;

        PerfectUserinfoParam(String nickname, String password, UserModel userModel) {
            this.nickname = nickname;
            this.password = password;
            this.userModel = userModel;
        }

        public UserModel getUserModel() {
            return userModel;
        }

        public void setUserModel(UserModel userModel) {
            this.userModel = userModel;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
