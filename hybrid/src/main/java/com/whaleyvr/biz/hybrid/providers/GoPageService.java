package com.whaleyvr.biz.hybrid.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Executor;
import com.whaley.core.share.model.ShareParam;
import com.whaleyvr.biz.hybrid.fragments.WebViewFragment;


/**
 * Created by dell on 2017/8/9.
 */

@Route(path = "/hybrid/service/goPage")
public class GoPageService implements Executor<GoPageModel> {

    private Context context;

    @Override
    public void excute(GoPageModel goPageModel, final Callback callback) {
        ShareModel shareModel = goPageModel.getShareModel();
        if (shareModel != null) {
            ShareParam shareParam = ShareParam.createBuilder().setFrom(shareModel.getFrom())
                    .setShareType(shareModel.getShareType())
                    .setType(shareModel.getType())
                    .setTitle(shareModel.getTitle())
                    .setDes(shareModel.getDes())
                    .setUrl(shareModel.getUrl())
                    .setHorizontal(shareModel.isHorizontal())
                    .setFullscreen(shareModel.isFullscreen())
                    .setVideo(shareModel.isVideo()).build();
            WebViewFragment.goPage((Starter) context, goPageModel.getUrl(), goPageModel.getTitleBarModel(), shareModel);
        } else {
            WebViewFragment.goPage((Starter) context, goPageModel.getUrl(), goPageModel.getTitleBarModel());
        }
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

}
