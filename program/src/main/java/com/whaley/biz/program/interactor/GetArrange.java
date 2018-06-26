package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.ArrangeApi;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.ArrangeResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/17
 * Introduction:
 */
@Route(path = ProgramRouterPath.USECASE_ARRANGE)
public class GetArrange extends UseCase<ArrangeResponse, GetArrange.Param> implements IProvider {


    public GetArrange() {
    }

    public GetArrange(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }


    @Override
    public Observable<ArrangeResponse> buildUseCaseObservable(Param param) {
        return getRepositoryManager().obtainRemoteService(ArrangeApi.class)
                .requestVideoList(param.code, param.page)
                .map(new ResponseFunction<ArrangeResponse, ArrangeResponse>());
    }

    @Override
    public void init(Context context) {

    }

    public static class Param {
        private String code;
        private int page;

        public Param(String code, int page) {
            this.code = code;
            this.page = page;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }
}
