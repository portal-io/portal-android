package com.whaley.biz.setting.interactor;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.interactor.UseCase;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.utils.CacheCleanManager;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Author: qxw
 * Date:2017/8/28
 * Introduction:
 */

public class CleanCache extends UseCase<String, String> {

    public CleanCache() {
    }

    public CleanCache(IRepositoryManager repositoryManager, Scheduler executeThread, Scheduler postExecutionThread) {
        super(repositoryManager, executeThread, postExecutionThread);
    }

    @Override
    public Observable<String> buildUseCaseObservable(String setting) {
        return Observable.just(cleanCache());
    }

    private String getCache() {
        String result = "";
        try {
            result = CacheCleanManager.getTotalCacheSize(AppContextProvider.getInstance().getContext());
        } catch (Exception e) {
            Log.e(e, "Cache");
        }
        return result;
    }

    private String cleanCache() {
        CacheCleanManager.cleanApplicationData(AppContextProvider.getInstance().getContext());
        return getCache();
    }
}
