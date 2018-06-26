package com.whaley.biz.program.interactor;

import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.CollectionAPI;
import com.whaley.biz.program.model.response.CollectListResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by dell on 2017/7/25.
 */

public class RequestCollect extends UseCase<CollectListResponse, RequestCollect.RequestCollectionParam> {

    public RequestCollect(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CollectListResponse> buildUseCaseObservable(final RequestCollectionParam param) {
        CheckLogin checkLogin = new CheckLogin();
        return checkLogin.buildUseCaseObservable(null)
                .flatMap(new Function<UserModel, ObservableSource<CollectListResponse>>() {

                    @Override
                    public ObservableSource<CollectListResponse> apply(@NonNull UserModel userModel) throws Exception {
                        if (userModel.isLoginUser()) {
                            return getRepositoryManager().obtainRemoteService(CollectionAPI.class)
                                    .requestCollectionList(param.getPage(), param.getSize(), userModel.getAccount_id());
                        }
                        throw new NotLoggedInErrorException("未登录");
                    }
                }).map(new ResponseFunction<CollectListResponse, CollectListResponse>());
    }

    public static class RequestCollectionParam {

        private int page;
        private int size;

        public RequestCollectionParam(int page, int size) {
            this.page = page;
            this.size = size;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

    }

}

