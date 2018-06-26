package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.FollowApi;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.CpProgramsResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/16
 * Introduction:
 */
@Route(path = ProgramRouterPath.USECASE_CP_PROGRAM_LIST)
public class GetCpProgramList extends UseCase<CpProgramsResponse, GetCpProgramList.Param> implements IProvider{

    public GetCpProgramList() {

    }

    public GetCpProgramList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<CpProgramsResponse> buildUseCaseObservable(Param param) {
        return getRepositoryManager().obtainRemoteService(FollowApi.class).requestPrograms(param.code, param.sortCol, param.page)
                .map(new ResponseFunction<CpProgramsResponse, CpProgramsResponse>());
    }

    @Override
    public void init(Context context) {

    }

    public static class Param {
        private String code;
        private String sortCol;
        private String page;

        public Param(String code, String sortCol, String page) {
            this.code = code;
            this.sortCol = sortCol;
            this.page = page;
        }

        public String getSortCol() {
            return sortCol;
        }

        public void setSortCol(String sortCol) {
            this.sortCol = sortCol;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
