package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.RecommendAPI;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.RecommendListResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by dell on 2017/8/15.
 */

@Route(path = ProgramRouterPath.USECASE_RECOMMEND_LIST)
public class GetRecommendList extends UseCase<RecommendListResponse, GetRecommendList.Param> implements IProvider {

    public GetRecommendList() {

    }

    public GetRecommendList(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RecommendListResponse> buildUseCaseObservable(Param param) {
        return getRepositoryManager().obtainRemoteService(RecommendAPI.class)
                .requestRecommend(param.getPageCode(), param.getBitCode(),
                        param.getPagenum(), param.getSize(), CommonConstants.VALUE_ANDROID_VERSION_NAME)
                .map(new ResponseFunction<RecommendListResponse, RecommendListResponse>());
    }

    @Override
    public void init(Context context) {

    }

    public static class Param {

        private String pageCode;
        private String bitCode;
        private int pagenum;
        private int size;

        public Param(String pageCode, String bitCode, int pagenum, int size) {
            this.pageCode = pageCode;
            this.bitCode = bitCode;
            this.pagenum = pagenum;
            this.size = size;
        }

        public String getPageCode() {
            return pageCode;
        }

        public void setPageCode(String pageCode) {
            this.pageCode = pageCode;
        }

        public String getBitCode() {
            return bitCode;
        }

        public void setBitCode(String bitCode) {
            this.bitCode = bitCode;
        }

        public int getPagenum() {
            return pagenum;
        }

        public void setPagenum(int pagenum) {
            this.pagenum = pagenum;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

}
