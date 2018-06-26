package com.whaley.biz.setting.interactor;


import com.umeng.analytics.AnalyticsConfig;
import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.setting.api.UpdateAPI;
import com.whaley.biz.setting.model.UpdateModel;
import com.whaley.biz.setting.response.UpdateResponse;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/8/28
 * Introduction:
 */

public class Update extends UseCase<UpdateModel, Boolean> implements CommonConstants {


    public Update() {
    }

    public Update(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<UpdateModel> buildUseCaseObservable(final Boolean b) {
        UpdateDownloading updateDownloading = new UpdateDownloading(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        return updateDownloading.buildUseCaseObservable(b)
                .flatMap(new Function<Boolean, ObservableSource<UpdateModel>>() {
                    @Override
                    public ObservableSource<UpdateModel> apply(Boolean aBoolean) throws Exception {
                        return getRepositoryManager().obtainRemoteService(UpdateAPI.class).update(VALUE_APP_VERSION_CODE, VALUE_MAC_ADDRESS,
                                VALUE_DEVICE_MODEL, VALUE_DEVICE_SERIAL, VALUE_SYSTEM_VERSION,
                                VALUE_MAC_ADDRESS, AnalyticsConfig.getChannel(AppContextProvider.getInstance().getContext()))
                                .map(new CommonFunction<UpdateResponse, UpdateModel>());
                    }
                });
    }
}
