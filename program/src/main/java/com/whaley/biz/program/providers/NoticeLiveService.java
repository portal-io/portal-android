package com.whaley.biz.program.providers;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.interactor.GetLiveDetail;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.response.LiveDetailsResponse;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/9/4
 * Introduction:
 */
@Route(path = ProgramRouterPath.SERVICE_LIVE_NOTICE)
public class NoticeLiveService implements Executor<String> {

    Context context;

    @Override
    public void excute(final String code, Callback callback) {
        GetLiveDetail getLiveDetail = new GetLiveDetail(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        GetLiveDetail.Param param = new GetLiveDetail.Param();
        param.setCode(code);
        getLiveDetail.execute(new DisposableObserver<LiveDetailsResponse>() {
            @Override
            public void onNext(@NonNull LiveDetailsResponse liveDetailsResponse) {
                LiveDetailsModel liveDetailsModel = liveDetailsResponse.getData();
                if (liveDetailsModel != null) {
                    GoPageUtil.goPage((Starter) context, FormatPageModel.getPageModel(liveDetailsModel));
                } else {
                    goLiveDetail(code);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                goLiveDetail(code);
            }

            @Override
            public void onComplete() {

            }
        }, param);
    }


    private void goLiveDetail(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        pageModel.setRouterPath(ProgramRouterPath.LIVE_STATE_BEFORE);
        bundle.putString(ProgramConstants.KEY_PARAM_ID, code);
        pageModel.setBundle(bundle);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        GoPageUtil.goPage((Starter) context, pageModel);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
