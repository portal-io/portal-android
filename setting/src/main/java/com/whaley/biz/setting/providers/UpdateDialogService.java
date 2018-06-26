package com.whaley.biz.setting.providers;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.setting.model.UpdateModel;
import com.whaley.biz.setting.update.UpdateDialog;
import com.whaley.core.router.Executor;
import com.whaley.core.utils.GsonUtil;

import java.util.Map;

/**
 * Author: qxw
 * Date:2018/1/15
 * Introduction:
 */
@Route(path = "/setting/service/updatedialog")
public class UpdateDialogService implements Executor<Map<String, Object>> {
    @Override
    public void excute(Map<String, Object> stringObjectMap, Callback callback) {
        Activity activity = (Activity) stringObjectMap.get("activity");
        String updateModelString = (String) stringObjectMap.get("content");
        View.OnClickListener onClickListener = (View.OnClickListener) stringObjectMap.get("clickListener");
        UpdateModel updateModel = GsonUtil.getGson().fromJson(updateModelString, UpdateModel.class);
        UpdateDialog.showDialog(activity, updateModel.getFilePath(), updateModel.getVersionName(),
                updateModel.getDescription(), updateModel.getUpdateType(), onClickListener);
    }

    @Override
    public void init(Context context) {

    }
}
