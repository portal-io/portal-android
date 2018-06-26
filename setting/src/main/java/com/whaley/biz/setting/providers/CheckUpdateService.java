package com.whaley.biz.setting.providers;


import android.content.Context;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.setting.update.UpdateHelper;
import com.whaley.core.router.Executor;
/**
 * Created by dell on 2017/9/5.
 */

@Route(path = "/setting/service/checkUpdate")
public class CheckUpdateService implements Executor<UpdateUIObserver> {

    @Override
    public void init(Context context) {

    }

    @Override
    public void excute(UpdateUIObserver uiObserver, Callback callback) {
        UpdateHelper.getInstance().checkIfOrNotUpdating(false,uiObserver);
    }
}
