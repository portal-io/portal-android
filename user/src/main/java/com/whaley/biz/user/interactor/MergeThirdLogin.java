package com.whaley.biz.user.interactor;

import android.app.Activity;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.model.portal.SyncResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */

public class MergeThirdLogin extends UseCase<UserModel, MergeThirdLogin.MergeThirdLoginParam> {


    public MergeThirdLogin(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UserModel> buildUseCaseObservable(final MergeThirdLoginParam param) {
        ThirdPlatformInfo thirdPlatformInfo = new ThirdPlatformInfo();
        return thirdPlatformInfo.buildUseCaseObservable(param.activity, param.type)
                .flatMap(new Function<UserModel, ObservableSource<UserModel>>() {
                    @Override
                    public ObservableSource<UserModel> apply(@NonNull UserModel thirdUserModel) throws Exception {
                        param.setUserModel(thirdUserModel);
                        ThirdLogin thirdLogin = new ThirdLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
                        return thirdLogin.buildUseCaseObservable(thirdUserModel);
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(final UserModel userModel) throws Exception {
                        SyncPortal syncPortal = new SyncPortal(getRepositoryManager());
                        syncPortal.buildUseCaseObservable(userModel.getAccount_id())
                                .subscribe(new Consumer<SyncResponse>() {
                                    @Override
                                    public void accept(SyncResponse syncResponse) throws Exception {

                                        userModel.setPortalAccessToken(syncResponse.getAccessTokenModel().getAccess_token());
                                        userModel.setPortalAddress(syncResponse.getUserInfoModel().getPortalAddress());
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userModel) throws Exception {
                        if (!userModel.isAddInformation()) {
                            UserManager.getInstance().saveLoginUser(userModel);
                        }
                    }
                });
    }


    public void execute(DisposableObserver<UserModel> observer, Activity activity, String type) {
        MergeThirdLogin.MergeThirdLoginParam param = new MergeThirdLogin.MergeThirdLoginParam(activity, type);
        execute(observer, param);
    }

    public static class MergeThirdLoginParam {
        private Activity activity;
        private String type;
        private UserModel userModel;

        public MergeThirdLoginParam(Activity activity, String type) {
            this.activity = activity;
            this.type = type;
        }

        public UserModel getUserModel() {
            return userModel;
        }

        public void setUserModel(UserModel userModel) {
            this.userModel = userModel;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
