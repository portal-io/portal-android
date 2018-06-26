package com.whaley.biz.share.providers;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.share.ShareSettingUtil;
import com.whaley.core.router.Executor;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.StrUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: qxw
 * Date:2017/9/7
 * Introduction:
 */
@Route(path = "/share/service/sharemodel")
public class GetShareModelService implements Executor<ShareModel> {

    @Override
    public void excute(ShareModel shareModel, Callback callback) {
        String url = ShareSettingUtil.getShareUrl(shareModel);
        if (ShareConstants.TYPE_ALL == shareModel.getType()) {
            Map<Integer, ShareParam> shareParamMap = new HashMap<>();
            shareParamMap.put(ShareConstants.TYPE_QQ, getShareParam(ShareConstants.TYPE_QQ, url, shareModel).build());
            shareParamMap.put(ShareConstants.TYPE_QZONE, getShareParam(ShareConstants.TYPE_QZONE, url, shareModel).build());
            shareParamMap.put(ShareConstants.TYPE_WEIXIN, getShareParam(ShareConstants.TYPE_WEIXIN, url, shareModel).build());
            shareParamMap.put(ShareConstants.TYPE_WEIXIN_CIRCLE, getShareParam(ShareConstants.TYPE_WEIXIN_CIRCLE, url, shareModel).build());
            shareParamMap.put(ShareConstants.TYPE_SINA, getShareParam(ShareConstants.TYPE_SINA, url, shareModel).build());
            ShareParam.Builder builder = ShareParam.createBuilder()
                    .setImgModelBitmap(shareModel.getImgUrl())
                    .setUrl(url)
                    .setHorizontal(shareModel.isHorizontal())
                    .setVideo(shareModel.isVideo())
                    .setType(ShareConstants.TYPE_ALL)
                    .setShareParams(shareParamMap)
                    .setShareType(shareModel.getShareType())
                    .setShareName(shareModel.getTitle())
                    .setShareCode(shareModel.getCode());
            if(shareModel.getShareType()== ShareTypeConstants.TYPE_ACTIVITY_SHARE_TOPIC){
                builder.setShareType(ShareTypeConstants.TYPE_SHARE_TOPIC);
            }
            callback.onCall(builder);
        } else {
            ShareParam.Builder builder = getShareParam(shareModel.getType(), url, shareModel);
            callback.onCall(builder);
        }
    }

    private ShareParam.Builder getShareParam(int type, String url, ShareModel shareModel) {
        String title;
        if (type == ShareConstants.TYPE_WEIXIN_CIRCLE) {
            title = ShareSettingUtil.getWeixinCircleTitle(shareModel);
        } else {
            title = ShareSettingUtil.getTitle(shareModel);
        }
        String des;
        if (type == ShareConstants.TYPE_SINA) {
            des = ShareSettingUtil.getSineContent(shareModel, url);
        } else {
            des = ShareSettingUtil.getText(shareModel);
            if (StrUtil.isEmpty(des)) {
                des = "众多人气综艺、体育内容每周更新VR版，只在微鲸VR ~";
            }
        }
        ShareParam.Builder builder = ShareParam.createBuilder()
                .setUrl(url)
                .setTitle(title)
                .setDes(des)
                .setVideo(shareModel.isVideo())
                .setType(type)
                .setShareType(shareModel.getShareType())
                .setShareName(shareModel.getTitle())
                .setShareCode(shareModel.getCode());
//        if(shareModel.getShareType()== ShareTypeConstants.TYPE_SHARE_WEB){
//            builder.setShareCode(shareModel.getCode());
//        }
        if(shareModel.getShareType()== ShareTypeConstants.TYPE_ACTIVITY_SHARE_TOPIC){
            builder.setShareType(ShareTypeConstants.TYPE_SHARE_TOPIC);
        }
        if (!StrUtil.isEmpty(shareModel.getImgUrl())) {
            builder.setImgModelBitmap(shareModel.getImgUrl());
        } else if (shareModel.getResId() != 0) {
            builder.setImgModelBitmap(shareModel.getResId());
        }
        return builder;
    }

    @Override
    public void init(Context context) {

    }
}
