package com.whaley.biz.share;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;

import com.umeng.socialize.Config;
import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.common.utils.StringUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.debug.Debug;
import com.whaley.core.router.Router;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.share.SharePanelCallback;
import com.whaley.core.share.ShareUtil;
import com.whaley.core.share.model.ShareParam;
import com.whaley.core.utils.StrUtil;
import com.whaleyvr.core.network.http.HttpManager;

/**
 * Author: qxw
 * Date:2017/9/6
 * Introduction:
 */

public class ShareSettingUtil implements ShareConstants, ShareTypeConstants {

    private final static String WX_APP_ID = "wx451cc87ab867c81c"; //"wx8d194f350eac172b"
    private final static String WX_APP_KEY = "a4fd98ac68407ef273695f6b5ef74969"; //"b141a2bba3905f9eecaa94adf78f526c"
    private final static String WB_APP_ID = "3689115682";
    private final static String WB_APP_KEY = "f7a77155f368b015cd7e75ebd6201ab4";
    private final static String QQ_APP_ID = "1105403128";
    private final static String QQ_APP_KEY = "6BVvOaFFhcKYGT8J";

    static String SHARE_TEST_URL = "http://vrh5.test.moguv.com/app-share-h5/";
    static String SHARE_URL = HttpManager.getInstance().isTest() ? SHARE_TEST_URL : "http://vrh5.moguv.com/app-share-h5/";
    static String SHARE_APP_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.whaley&ckey=CK1340670182054";
    static String SHARE_TOPIC_URL = SHARE_URL + "index.html?code=%1$s";
    static String SHARE_PACKAGE_URL = SHARE_URL + "itempack.html?code=%1$s";
    static String SHARE_LIVE_DETAIL = SHARE_URL + "liveProgram.html?code=%1$s";
    static String SHARE_DETAIL = SHARE_URL + "viewthread.html?code=%1$s";
    static String SHARE_3D_DETAIL = SHARE_URL + "viewthread3D.html?code=%1$s";
    static String SHARE_TV = SHARE_URL + "tv.html?code=%1$s";
    static String SHARE_DRAMA = SHARE_URL + "dynaprogram.html?code=%1$s";
    static String SHARE_ACTIVITY_TOPIC_URL = SHARE_URL + "springFestival2018.html?code=%1$s";

    static int REQUEST_CODE = 1024;

    public static void initShare() {
        Config.DEBUG = Debug.isDebug();
        ShareUtil.setQQ(QQ_APP_ID, QQ_APP_KEY);
        ShareUtil.setWeixin(WX_APP_ID, WX_APP_KEY);
        ShareUtil.setSinaWeibo(WB_APP_ID, WB_APP_KEY, "https://itunes.apple.com/us/app/vr-guan-jia/id963637613?l=zh&ls=1&mt=8");
        ShareUtil.setShareDefaultImg(R.mipmap.ic_share_default);
        ShareParam.defaultPanelCallback = new SharePanelCallback() {
            @Override
            public void panelCallback(Activity context, ShareParam shareParam) {
                EventController.postEvent(new BaseEvent("/share/service/sharetemp", null));
                Intent intent = new Intent();
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    intent.setClass(AppContextProvider.getInstance().getContext(), HorizontalShareActivity.class);
                } else {
                    intent.setClass(AppContextProvider.getInstance().getContext(), ShareActivity.class);
                }
                intent.putExtra("data", shareParam);
                context.startActivityForResult(intent, REQUEST_CODE);
            }
        };
    }

    public static String getShareUrl(ShareModel shareModel) {
        String code = shareModel.getCode();
        switch (shareModel.getShareType()) {
            case TYPE_SHARE_APP:
                return SHARE_APP_URL;
            case TYPE_SHARE_LIVE:
            case TYPE_SHARE_LIVE_PLAYER:
                return String.format(SHARE_LIVE_DETAIL, code);
            case TYPE_SHARE_MOVIE:
            case TYPE_SHARE_MM:
                return String.format(SHARE_3D_DETAIL, code);
            case TYPE_SHARE_VIDEO:
                return String.format(SHARE_DETAIL, code);
            case TYPE_SHARE_TOPIC:
                return String.format(SHARE_TOPIC_URL, code);
            case TYPE_ACTIVITY_SHARE_TOPIC:
                return String.format(SHARE_ACTIVITY_TOPIC_URL, code);
            case TYPE_SHARE_MT:
                return String.format(SHARE_TV, code);
            case TYPE_SHARE_PACK:
            case TYPE_SHARE_SET:
                return String.format(SHARE_PACKAGE_URL, code);
            case TYPE_SHARE_DRAMA:
                return String.format(SHARE_DRAMA, code);
            case TYPE_SHARE_WEB:
            case TYPE_SHARE_NEWS:
            default:
                return shareModel.getUrl();
        }
    }

    public static String getTitle(ShareModel shareModel) {
        String title = shareModel.getTitle();
        switch (shareModel.getShareType()) {
            case TYPE_SHARE_APP:
                return AppContextProvider.getInstance().getContext().getString(R.string.share_mine_title);
            case TYPE_SHARE_LIVE:
            case TYPE_SHARE_MOVIE:
            case TYPE_SHARE_VIDEO:
            case TYPE_SHARE_DRAMA:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_detail_title), title);
            case TYPE_SHARE_NEWS:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_news_title), title);
            case TYPE_SHARE_TOPIC:
            case TYPE_SHARE_PACK:
            case TYPE_SHARE_SET:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_topic_title), title);
            case TYPE_SHARE_MT:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_tv_series_title), StringUtil.getReplaceEpisode(title));
            case TYPE_SHARE_MM:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_tv_movie_title), title);
            case TYPE_SHARE_LIVE_PLAYER:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_live), title);
            case TYPE_SHARE_WEB:
            default:
                return title;
        }
    }

    public static String getText(ShareModel shareModel) {
        switch (shareModel.getShareType()) {
            case TYPE_SHARE_APP:
                return AppContextProvider.getInstance().getContext().getString(R.string.share_mine_content);
            case TYPE_SHARE_LIVE_PLAYER:
                String des = "";
                if (!StrUtil.isEmpty(shareModel.getDes())) {
                    des = "," + shareModel.getDes();
                }
                return "直播时间：" + shareModel.getExtra() + des;
            case TYPE_SHARE_LIVE:
            case TYPE_SHARE_MOVIE:
            case TYPE_SHARE_VIDEO:
            case TYPE_SHARE_NEWS:
            case TYPE_SHARE_TOPIC:
            case TYPE_SHARE_WEB:
            default:
                return shareModel.getDes();
        }
    }

    public static String getWeixinCircleTitle(ShareModel shareModel) {
        switch (shareModel.getShareType()) {
            case TYPE_SHARE_APP:
                return AppContextProvider.getInstance().getContext().getString(R.string.share_mine_weixin_circle_title);
            case TYPE_SHARE_LIVE_PLAYER:
                return shareModel.getTitle() + "，直播时间：" + shareModel.getExtra();
            case TYPE_SHARE_LIVE:
            case TYPE_SHARE_MOVIE:
            case TYPE_SHARE_VIDEO:
            case TYPE_SHARE_NEWS:
            case TYPE_SHARE_TOPIC:
            case TYPE_SHARE_WEB:
            default:
                return getTitle(shareModel);
        }
    }

    public static String getSineContent(ShareModel shareModel, String url) {
        switch (shareModel.getShareType()) {
            case TYPE_SHARE_APP:
                return String.format(AppContextProvider.getInstance().getContext().getString(R.string.share_mine_sine_content), SHARE_APP_URL + AppContextProvider.getInstance().getContext().getString(R.string.share_sine));
            case TYPE_SHARE_ABOUT:
                return AppContextProvider.getInstance().getContext().getString(R.string.trade_share_intro_weibo);
            case TYPE_SHARE_LIVE_PLAYER:
                return getTitle(shareModel) + "，直播时间：" + shareModel.getExtra() + url + AppContextProvider.getInstance().getContext().getString(R.string.share_sine);
            case TYPE_SHARE_LIVE:
            case TYPE_SHARE_MOVIE:
            case TYPE_SHARE_VIDEO:
            case TYPE_SHARE_NEWS:
            case TYPE_SHARE_TOPIC:
            case TYPE_SHARE_WEB:
            default:
                return getTitle(shareModel) + url + AppContextProvider.getInstance().getContext().getString(R.string.share_sine);
        }
    }
}
