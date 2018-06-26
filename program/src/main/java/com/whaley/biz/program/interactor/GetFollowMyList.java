package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.FollowApi;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.model.response.FollowMyResponse;
import com.whaley.biz.program.utils.SharedPreferencesUtil;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Author: qxw
 * Date:2017/8/9
 * Introduction:
 */
@Route(path = "/program/usecase/getfollowmylist")
public class GetFollowMyList extends UseCase<FollowMyResponse, Integer> implements IProvider {

    public GetFollowMyList() {

    }

    public GetFollowMyList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<FollowMyResponse> buildUseCaseObservable(final Integer page) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, String>() {
                    @Override
                    public String apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel.getAccount_id();
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                })
                .flatMap(new Function<String, ObservableSource<FollowMyResponse>>() {
                    @Override
                    public ObservableSource<FollowMyResponse> apply(@NonNull final String id) throws Exception {
                        return getRepositoryManager().obtainRemoteService(FollowApi.class).requestMyFollow(id, page)
                                .map(new ResponseFunction<FollowMyResponse, FollowMyResponse>())
                                .doOnNext(new Consumer<FollowMyResponse>() {
                                    @Override
                                    public void accept(@NonNull FollowMyResponse followMyResponse) throws Exception {
                                        if (SharedPreferencesUtil.getUnreadData(id) > 0 && page == 0) {
                                            SharedPreferencesUtil.resetUnreadData(id);
                                            EventController.postEvent(new BaseEvent("follow_notice_update"));
                                        }

                                    }
                                });
                    }
                });
    }

    @Override
    public void init(Context context) {

    }
}
