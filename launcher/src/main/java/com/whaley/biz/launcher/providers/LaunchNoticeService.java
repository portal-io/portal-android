package com.whaley.biz.launcher.providers;

import android.content.Context;
import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.launcher.activitys.MainActivity;
import com.whaley.biz.launcher.model.NoticeModel;
import com.whaley.core.router.Executor;

/**
 * Author: qxw
 * Date:2017/9/4
 * Introduction:
 */
@Route(path = "/launch/service/notice")
public class LaunchNoticeService implements Executor<NoticeModel> {

    private Context context;

    @Override
    public void excute(NoticeModel noticeModel, Callback callback) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.TYPE_NOTICE, noticeModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
