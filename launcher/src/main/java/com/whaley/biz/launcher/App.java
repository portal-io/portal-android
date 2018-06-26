package com.whaley.biz.launcher;

import android.content.Context;
import android.content.Intent;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.whaley.core.appcontext.Starter;


/**
 * Created by YangZhi on 2017/7/25 20:49.
 */

public class App extends TinkerApplication implements Starter {

    public App() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.whaley.biz.launcher.AppLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        startActivity(intent);
    }


    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
    }

    @Override
    public Context getAttatchContext() {
        return this;
    }

    @Override
    public void transitionAnim(int i, int i1) {

    }

    @Override
    public void finish() {

    }


}
