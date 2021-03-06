package com.whaley.biz.user.interactor;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.api.PortalApi;
import com.whaley.biz.user.model.LoginModel;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.portal.AccountModel;
import com.whaley.biz.user.model.portal.SyncResponse;
import com.whaley.biz.user.model.response.WhaleyResponse;
import com.whaley.biz.user.api.UserVrApi;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.StrUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date: 2017/7/17
 * Introduction:账户登录
 */

public class AccountLogin extends BaseUseCase<UserModel, AccountLogin.AccountLoginParam> {


    public AccountLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    public void executeLogin(DisposableObserver<UserModel> observer, AccountLoginParam param) {
        UseCaseParam<AccountLoginParam> loginParam = new UseCaseParam<>(param);
        execute(observer, loginParam);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(UseCaseParam<AccountLoginParam> param) {
        Observable observable = getRepositoryManager()
                .obtainRemoteService(UserVrApi.class)
                .login(param.getParam().username,
                        param.getParam().password,
                        param.getParam().from,
                        UserManager.getInstance().getDeviceId())
                .map(new CommonFunction<WhaleyResponse<LoginModel>, LoginModel>())
                .map(new Function<LoginModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull LoginModel loginBean) throws Exception {
                        UserModel userModel = loginBean.convertToUserBean();
                        if (StrUtil.isEmpty(userModel.getNickname()) || userModel.getNickname().startsWith("vr_")) {
                            userModel.setAddInformation(true);
                        } else {
                            userModel.setAddInformation(false);
                        }
                        return userModel;
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(final UserModel userModel) throws Exception {
                        SyncPortal syncPortal = new SyncPortal(getRepositoryManager());
                        syncPortal.buildUseCaseObservable(userModel.getAccount_id())
                                .subscribe(new Observer<SyncResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(SyncResponse syncResponse) {
                                        userModel.setPortalAccessToken(syncResponse.getAccessTokenModel().getAccess_token());
                                        userModel.setPortalAddress(syncResponse.getUserInfoModel().getPortalAddress());
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userBean) throws Exception {
                        if (!userBean.isAddInformation()) {
                            UserManager.getInstance().saveLoginUser(userBean);
                        }
                    }
                });
        return observable;
    }

    public static class AccountLoginParam {
        private String username;
        private String password;
        private String from = "whaleyVR";

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

    }

}
