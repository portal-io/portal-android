package com.whaley.biz.jpush;

import android.os.Bundle;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.jpush.model.NoticPushModel;
import com.whaley.biz.jpush.model.NoticeModel;
import com.whaley.biz.jpush.model.PlayData;
import com.whaley.biz.jpush.model.UserModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.Debug;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

/**
 * Author: qxw
 * Date: 2016/10/28
 */

public class PushStartController implements PushConstants {


    public static void setDebugMode(boolean isDebug) {
        JPushInterface.setDebugMode(isDebug);
    }

    public static void initPush() {
        JPushInterface.init(AppContextProvider.getInstance().getContext());
    }

    /**
     * 推送设置tag
     */
    public static void setPushTags() {
        final Set<String> tags = new HashSet<>();
        if (Debug.isDebug()) {
            tags.add("测试");
        } else {
            tags.add(CommonConstants.VALUE_APP_VERSION_CODE);
        }
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                String uid = userModel.getAccount_id();
                if (!StrUtil.isEmpty(uid)) {
                    tags.add(uid);
                }
                setPushTags(tags);
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                setPushTags(tags);
            }
        }).excute();

    }

    private static void setPushTags(Set<String> tags) {
        JPushInterface.setTags(AppContextProvider.getInstance().getContext(), tags, null);
    }

    public static void processNotice(Starter starter, String notice) {
        try {
            NoticPushModel noticPushModel = GsonUtil.getGson().fromJson(notice, NoticPushModel.class);
            NoticeModel noticeModel = noticPushModel.getExtra();
            String temp[] = setResolveString(noticeModel.getMsg_param());
            switch (noticeModel.getMsg_type()) {
                case MSG_TYPE_HOME:
                    break;
                case MSG_TYPE_TOPIC:
                    Bundle bundle = new Bundle();
                    bundle.putString("key_param_id", noticeModel.getMsg_param());
                    startRouter(starter, bundle, "/program/ui/topic");
                    break;
                case MSG_TYPE_WEB:
                    if (temp != null && temp.length == 2) {
                        Router.getInstance().buildExecutor("/hybrid/service/goPage")
                                .putObjParam(WebviewGoPageModel.createWebviewGoPageModel(temp[0],
                                        TitleBarModel.createTitleBarModel(temp[1]))).excute();
                    } else {
                        Router.getInstance().buildExecutor("/hybrid/service/goPage")
                                .putObjParam(WebviewGoPageModel.createWebviewGoPageModel(temp[0])).excute();
                    }
                    break;
                case MSG_TYPE_LIVE:
                    processLive(noticeModel);
                    break;
                case MSG_TYPE_DETAIL:
                    if (temp != null && temp.length == 2) {
                        int videoType = 1;
                        String routerPath = "/program/ui/program";
                        if (temp[0].equals("5"))
                            videoType = 5;
                        else if (temp[0].equals("6"))
                            routerPath = "/program/ui/programdrama";
                        Bundle detail = new Bundle();
                        PlayData playData = new PlayData();
                        playData.setId(temp[1]);
                        playData.setType(videoType);
                        playData.setMonocular(true);
                        List<PlayData> list = new ArrayList<>();
                        list.add(playData);
                        detail.putString("key_param_datas", GsonUtil.getGson().toJson(list));
                        startRouter(starter, detail, routerPath);
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(e, "");
        }
    }

    private static void startRouter(Starter starter, Bundle bundle, String routerPath) {
        Router.getInstance().buildNavigation(routerPath)
                //設置 starter
                .setStarter(starter)
                //設置 requestCode
                .with(bundle)
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, 1)
                .navigation();
    }


    private static void processLive(final NoticeModel noticeModel) {
        Router.getInstance().buildExecutor("/program/service/noticelive")
                .putObjParam(noticeModel.getMsg_param()).excute();

//        LiveAPI liveAPI = HttpManager.getInstance().getRetrofit(LiveAPI.class).create(LiveAPI.class);
//        Call call = liveAPI.requestLiveDeta(noticeModel.getMsg_param());
//        call.enqueue(new NormalCallback<LiveDetailsResponse>(null, false) {
//            @Override
//            public void onSuccess(Call<LiveDetailsResponse> call, LiveDetailsResponse data) {
//                super.onSuccess(call, data);
//                LiveDetailsModel liveDetailsModel = data.getData();
//                if (liveDetailsModel != null) {
//                    int liveStatus = liveDetailsModel.getLiveStatus();
//                    if (ConstantsLive.LIVE_STATE_BEING == liveStatus) {
//                        LivePlayerInitModel livePlayerInitModel = new LivePlayerInitModel();
//                        livePlayerInitModel.setSid(noticeModel.getMsg_param())
//                                .setPoster(liveDetailsModel.getPoster())
//                                .setDes(liveDetailsModel.getDescription())
//                                .setIsChargeable(liveDetailsModel.getIsChargeable())
//                                .setPrice(liveDetailsModel.getPrice())
//                                .setBeginTime(liveDetailsModel.getBeginTime())
//                                .setTitle(liveDetailsModel.getDisplayName())
//                                .setLiveType(liveDetailsModel.getType())
//                                .setDisplayMode(liveDetailsModel.getDisplayMode())
//                                .setVrPanPlayerBean(liveDetailsModel.getPlayModel().convertToVRPanPlayerBean());
//                        if (liveDetailsModel.getStat() != null)
//                            livePlayerInitModel.setPlayCount(liveDetailsModel.getStat().getPlayCount());
//                        LiveBeingFragment.goPage(starter, livePlayerInitModel);
//                    } else if (ConstantsLive.LIVE_STATE_BEFORE == liveStatus) {
//                        LiveReserveDetailFragment.goPage(starter, noticeModel.getMsg_param());
//                    } else {
//                        CustomToast.makeToast(starter.getAttatchContext(), "直播已结束", Toast.LENGTH_SHORT);
//                    }
//                } else {
//                    LiveReserveDetailFragment.goPage(starter, noticeModel.getMsg_param());
//                }
//            }
//
//            @Override
//            protected void onFailure(Call<LiveDetailsResponse> call, Throwable t, boolean isForceShowEmpty) {
//                super.onFailure(call, t, isForceShowEmpty);
//                LiveReserveDetailFragment.goPage(starter, noticeModel.getMsg_param());
//            }
//
//            @Override
//            public void onStatusError(Call<LiveDetailsResponse> call, LiveDetailsResponse data) {
//                super.onStatusError(call, data);
//                LiveReserveDetailFragment.goPage(starter, noticeModel.getMsg_param());
//            }
//        });
    }

    public static String[] setResolveString(String resolve) {
        String temp[] = null;
        try {
            temp = resolve.split("\\{lewo\\}");
        } catch (Exception e) {

        }
        return temp;
    }

}
