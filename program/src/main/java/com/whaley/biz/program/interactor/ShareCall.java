package com.whaley.biz.program.interactor;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.program.api.SpringFestivalApi;
import com.whaley.biz.program.model.BoxLeftCountModel;
import com.whaley.biz.program.model.response.BoxLeftCountResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.share.ShareConstants;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2018/1/29
 * Introduction:
 */

public class ShareCall extends UseCase<Integer, ShareCall.Param> {

    public ShareCall() {
        super();
    }

    public ShareCall(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<Integer> buildUseCaseObservable(final Param param) {
        CheckLogin checkLogin = new CheckLogin(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return checkLogin.buildUseCaseObservable(null).map(new Function<UserModel, UserModel>() {
            @Override
            public UserModel apply(@NonNull UserModel userModel) throws Exception {
                if (userModel.isLoginUser()) {
                    return userModel;
                } else {
                    throw new NotLoggedInErrorException("");
                }
            }
        }).flatMap(new Function<UserModel, ObservableSource<BoxLeftCountResponse>>() {
            @Override
            public ObservableSource<BoxLeftCountResponse> apply(UserModel userModel) throws Exception {
                return getRepositoryManager().obtainRemoteService(SpringFestivalApi.class)
                        .shareCall(userModel.getAccount_id(), param.channel, userModel.getAccessTokenModel().getAccesstoken());
            }
        }).map(new CommonFunction<BoxLeftCountResponse, BoxLeftCountModel>())
                .map(new Function<BoxLeftCountModel, Integer>() {
                    @Override
                    public Integer apply(@NonNull BoxLeftCountModel boxLeftCountModel) throws Exception {
                        return boxLeftCountModel.getLeftCount();
                    }
                })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        BaseEvent baseEvent = new BaseEvent("activityShareCall", integer);
                        EventBus.getDefault().post(baseEvent);
                    }
                });
    }

    public static class Param {
        String channel;

        public Param(int type) {
            switch (type) {
                case ShareConstants.TYPE_QQ:
                case ShareConstants.TYPE_QZONE:
                    channel = "qq";
                    break;
                case ShareConstants.TYPE_SINA:
                    channel = "weibo";
                    break;
                default:
                    channel = "weixin";
                    break;
            }

        }
    }
}
