package com.whaley.biz.portal.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.portal.api.PortalApi;
import com.whaley.biz.portal.model.user.UserModel;
import com.whaley.biz.portal.response.EarningInfoResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2018/8/10.
 */

public class GetEarning extends UseCase<EarningInfoResponse, GetEarning.Param> {

    public GetEarning(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<EarningInfoResponse> buildUseCaseObservable(final GetEarning.Param param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .map(new Function<UserModel, UserModel>() {
                    @Override
                    public UserModel apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return userModel;
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).flatMap(new Function<UserModel, ObservableSource<EarningInfoResponse>>() {
                    @Override
                    public ObservableSource<EarningInfoResponse> apply(@NonNull UserModel userModel) throws Exception {
                        return getRepositoryManager().obtainRemoteService(PortalApi.class)
                                .earnings(userModel.getPortalAccessToken(),param.getPageNum(),param.getPageSize());
                    }
                }) .map(new ResponseFunction<EarningInfoResponse,EarningInfoResponse>());
    }

    public static class Param{

        private int pageNum;
        private int pageSize;

        public Param(int pageNum, int pageSize) {
            this.pageNum = pageNum;
            this.pageSize = pageSize;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

}
