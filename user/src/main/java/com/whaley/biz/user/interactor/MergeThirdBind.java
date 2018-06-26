package com.whaley.biz.user.interactor;

import android.app.Activity;

import com.whaley.biz.common.interactor.BaseUseCase;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.user.UserConstants;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.interactor.function.RetryWhenRefreshTokenFunction;
import com.whaley.biz.user.model.UserModel;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.StrUtil;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */

public class MergeThirdBind extends com.whaley.core.interactor.UseCase<String, MergeThirdBind.MergeThirdBindParam> {

    @UseCase
    ThirdPlatformInfo thirdPlatformInfo;

    @UseCase
    ThirdBind thirdBind;


    public MergeThirdBind(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(final MergeThirdBindParam param) {
        return thirdPlatformInfo.buildUseCaseObservable(param.activity, param.type)
                .flatMap(new Function<UserModel, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull UserModel thirdUserModel) throws Exception {
                        param.setUserModel(thirdUserModel);
                        return thirdBind.buildUseCaseObservable(new UseCaseParam<>(thirdUserModel));
                    }
                }).doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        UserModel userModel = UserManager.getInstance().getUser();
                        UserModel user = param.getUserModel();
                        if (StrUtil.isEmpty(user.getNickname())) {
                            user.setNickname(userModel.getNickname());
                        }
                        switch (param.type) {
                            case UserConstants.TYPE_AUTH_QQ:
                                userModel.setQq(user.getNickname());
                                break;
                            case UserConstants.TYPE_AUTH_WX:
                                userModel.setWx(user.getNickname());
                                break;
                            default:
                                userModel.setWb(user.getNickname());
                        }
                        UserManager.getInstance().saveUser(userModel);
                    }
                });
    }


    public void execute(DisposableObserver<String> observer, Activity activity, String type) {
        MergeThirdBind.MergeThirdBindParam param = new MergeThirdBind.MergeThirdBindParam(activity, type);
        execute(observer, param);
    }

    public static class MergeThirdBindParam {
        private Activity activity;
        private String type;
        private UserModel userModel;

        public MergeThirdBindParam(Activity activity, String type) {
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
