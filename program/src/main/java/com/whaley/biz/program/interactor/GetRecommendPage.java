package com.whaley.biz.program.interactor;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.program.api.RecommendAPI;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.response.RecommendResponse;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by YangZhi on 2017/7/27 17:59.
 */

@Route(path = ProgramRouterPath.USECASE_RECOMMEND)
public class GetRecommendPage extends UseCase<RecommendResponse, String> implements IProvider {
    public static final String HOME_WELCOME_AD = "WelcomePage";
    public static final String HOME_RECOMMEND = "shouyetuijian";
    public static final String HOME_MOVICE = "moviefaxianye";
    public static final String HOME_VR = "vrfaxianye";
    public static final String HOME_LOOK_LIVE = "livereview";
    public static final String RECOMMEND_HOME_PAGE = "RecommendPage";
    public static final String RECOMMEND_HOME_LIVE = "live_home";
    public static final String TV_FLAG = "?v=1";
    public static final int SIZE_RECOMMEND = 9;
    public static final String TAB_LIVE = "live_tab";
    public static final String HOME_PAGE = "Page_Recommend";

    public GetRecommendPage() {

    }

    public GetRecommendPage(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<RecommendResponse> buildUseCaseObservable(String code) {
        return getRepositoryManager().obtainRemoteService(RecommendAPI.class)
                .requestAllRecommend(code, CommonConstants.VALUE_ANDROID_VERSION_NAME)
                .map(new ResponseFunction<RecommendResponse, RecommendResponse>());
    }

    @Override
    public void init(Context context) {

    }

    public static class Param {
        private String code;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
