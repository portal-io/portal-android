package com.whaley.biz.setting.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.setting.db.LocalVideoBean;
import com.whaley.biz.setting.db.LocalVideoDatabseManager;
import com.whaley.biz.setting.model.LocalVideoInfo;
import com.whaley.core.router.Executor;

import java.util.List;

/**
 * Created by dell on 2017/9/21.
 */

@Route(path = "/setting/service/operationLocalVideo")
public class OperationLocalVideoService implements Executor<LocalVideoInfo> {

    //0 query 1 insert 2 delete

    @Override
    public void excute(LocalVideoInfo o, Callback callback) {
        if(o==null)
            return;
        LocalVideoInfo localVideoInfo = new LocalVideoInfo();
        localVideoInfo.setOperation(o.getOperation());
        if(o.getOperation()==0){
            localVideoInfo.setLocalVideoModelList(LocalVideoDatabseManager.getInstance().queryLocalList());
            localVideoInfo.setResult(true);
        }else if(o.getOperation()==1){
            localVideoInfo.setResult(LocalVideoDatabseManager.getInstance().insertOrReplaceList(o.getLocalVideoModelList()));
        }else if(o.getOperation()==2){
            localVideoInfo.setResult(LocalVideoDatabseManager.getInstance().deleteList(o.getLocalVideoModelList()));
        }else{
            return;
        }
        callback.onCall(localVideoInfo);
    }

    @Override
    public void init(Context context) {

    }

}
