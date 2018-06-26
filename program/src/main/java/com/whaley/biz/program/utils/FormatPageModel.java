package com.whaley.biz.program.utils;

import android.os.Bundle;

import com.whaley.biz.common.ShareTypeConstants;
import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.model.share.ShareModel;
import com.whaley.biz.common.model.hybrid.TitleBarModel;
import com.whaley.biz.common.model.hybrid.WebviewGoPageModel;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.model.ArrangeModel;
import com.whaley.biz.program.model.CouponModel;
import com.whaley.biz.program.model.LiveDetailsModel;
import com.whaley.biz.program.model.PackageItemModel;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.model.ReserveModel;
import com.whaley.biz.program.model.SearchModel;
import com.whaley.biz.program.model.TopicModel;
import com.whaley.biz.program.model.pay.ThirdPayModel;
import com.whaley.biz.program.playersupport.model.MatchInfo;
import com.whaley.biz.program.ui.recommend.presenter.CompilationPresenter;
import com.whaley.biz.program.ui.follow.presenter.PublisherDetailPresenter;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.core.share.ShareConstants;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangzhi on 2017/8/15.
 */

public class FormatPageModel implements ProgramConstants {


    public static PageModel getPageModelPay(String code, List<CouponModel> packsCoupons, String content, boolean isUnity, String type, String loginTips) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        ThirdPayModel thirdPayModel = new ThirdPayModel();
        thirdPayModel.setCode(code);
        thirdPayModel.setPacksCoupons(packsCoupons);
        thirdPayModel.setContent(content);
        thirdPayModel.setUnity(isUnity);
        thirdPayModel.setType(type);
        bundle.putString(PublisherDetailPresenter.KEY_PAY_DATA, GsonUtil.getGson().toJson(thirdPayModel));
        bundle.putString(PublisherDetailPresenter.KEY_LOGIN_TIPS, loginTips);
        pageModel.setBundle(bundle);
        pageModel.setRouterPath(ProgramRouterPath.PAY);
        return pageModel;
    }

    /**
     * 发布者页面
     *
     * @param code
     * @return
     */
    public static PageModel goPageModelPublisher(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        bundle.putString(PublisherDetailPresenter.EXTRA_CP_CODE, code);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        pageModel.setBundle(bundle);
        pageModel.setRouterPath(ProgramRouterPath.PUBLISHER_DETAIL);
        return pageModel;
    }

    public static PageModel goPagePageModelChannelRecommend() {
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(ProgramRouterPath.CHANNEL_RECOMMEND);
        Bundle bundle = new Bundle();
        bundle.putString(ProgramConstants.KEY_PARAM_ID, "channel_home");
        pageModel.setBundle(bundle);
        return pageModel;
    }

    /**
     * 关注推荐页
     *
     * @return
     */
    public static PageModel goPagePageModelFollowRecommend() {
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(ProgramRouterPath.FOLLOW_RECOMMEND);
        return pageModel;
    }


    public static PageModel getPageModel(PackageItemModel packageItemModel) {
        if (packageItemModel.getLiveStatus() == ProgramConstants.LIVE_STATE_BEFORE) {
            return getLiveBeforePageModel(packageItemModel.getContentCode());
        }
        if (packageItemModel.getLiveStatus() == ProgramConstants.LIVE_STATE_AFTER) {
            return getLiveAfterPageModel(packageItemModel.getContentCode(), packageItemModel.getPic());
        }
        return getPayerPageModel(packageItemModel.getPlayData(), true);
    }

    public static PageModel getPayerListPagModel(List<PlayData> list, int pos, boolean isDrama) {
        return getPayerListPagModel(list, pos, false, isDrama);
    }


    public static PageModel getPayerListPagModel(List<PlayData> list, int pos) {
        return getPayerListPagModel(list, pos, false, false);
    }

    public static PageModel getPayerListPagModel(List<PlayData> list, int pos, boolean isFull, boolean isDrama) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        bundle.putString(ProgramConstants.KEY_PARAM_DATAS, GsonUtil.getGson().toJson(list));
        bundle.putInt(ProgramConstants.KEY_PAYER_POS, pos);
        pageModel.setBundle(bundle);
        if (isDrama) {
            pageModel.setRouterPath(ProgramRouterPath.PROGRAM_DRAMA);
        } else if (isFull) {
            pageModel.setRouterPath(ProgramRouterPath.PLAYER);
        } else {
            pageModel.setRouterPath(ProgramRouterPath.PROGRAM);
        }
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        return pageModel;
    }

    public static PageModel getPageModel(TopicModel topicModel) {
        if (ProgramConstants.TYPE_ARRANGE.equals(topicModel.getProgramType())) {
            return getTopicPageModel(topicModel.getLinkId());
        } else {
            return getPayerPageModel(topicModel.getPlayData(), false);
        }
    }

    public static PageModel getPageModel(LiveDetailsModel model) {
        if (model.getLiveStatus() == LIVE_STATE_BEFORE) {
            return getLiveBeforePageModel(model.getCode());
        } else if (model.getLiveStatus() == LIVE_STATE_BEING) {
            return getLivePageModel(model.getPlayData());
        } else {
            return getLiveAfterPageModel(model.getCode(), model.getPoster());
        }
    }

    public static PageModel getPageModel(ReserveModel model) {
        if (model.getLiveStatus() == LIVE_STATE_BEFORE) {
            return getLiveBeforePageModel(model.getCode());
        } else if (model.getLiveStatus() == LIVE_STATE_BEING) {
            return getLivePageModel(model.getPlayData());
        } else {
            return getLiveAfterPageModel(model.getCode(), model.getPoster());
        }
    }

    public static PageModel getSearchPageModel(SearchModel.ProgramBean programBean, boolean isDrama) {
        return getProgramPageModel(programBean.getPlayData(), isDrama);
    }


    public static PageModel getPageModel(ArrangeModel arrangeModel) {
        return getProgramPageModel(arrangeModel.getPlayData());
    }

    public static PageModel getPageModel(ArrangeModel arrangeModel, boolean isDrama) {
        return getProgramPageModel(arrangeModel.getPlayData(), isDrama);
    }

    /**
     * 直播前页面跳转
     *
     * @return
     */
    public static PageModel getLiveBeforePageModel(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        pageModel.setRouterPath(ProgramRouterPath.LIVE_STATE_BEFORE);
        bundle.putString(KEY_PARAM_ID, code);
        pageModel.setBundle(bundle);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        return pageModel;
    }

    /**
     * 直播页面跳转
     *
     * @param playData
     * @return
     */
    public static PageModel getLivePageModel(PlayData playData) {
        return getPayerPageModel(playData, true);
    }

    /**
     * 直播后
     *
     * @return
     */
    public static PageModel getLiveAfterPageModel(String code, String backimage) {
        PageModel pageModel = new PageModel();
        pageModel.setCanJump(true);
        pageModel.setRouterPath(ProgramRouterPath.LIVE_COMPLETED);
        Bundle bundle = new Bundle();
        bundle.putString(ProgramConstants.KEY_PARAM_LIVE_CODE, code);
        bundle.putString(ProgramConstants.KEY_PARAM_LIVE_BACKGROUND_IMAGE, backimage);
        pageModel.setBundle(bundle);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        return pageModel;
    }

    /**
     * 详情页跳转
     *
     * @return
     */

    public static PageModel getProgramPageModel(PlayData playData) {
        return getPayerPageModel(playData, false);
    }

    public static PageModel getProgramPageModel(PlayData playData, boolean isDrama) {
        return getPayerPageModel(playData, false, isDrama);
    }

    public static PageModel getProgramDramaPageModel(PlayData playData) {
        return getPayerPageModel(playData, false, true);
    }

    public static PageModel getPayerPageModel(PlayData playData, boolean isLive) {
        return getPayerPageModel(playData, isLive, false);
    }

    public static PageModel getPayerPageModel(PlayData playData, boolean isLive, boolean isDrama) {
        PageModel pageModel = new PageModel();
        if (playData != null) {
            Bundle bundle = new Bundle();
            List<PlayData> list = new ArrayList<>();
            list.add(playData);
            bundle.putString(ProgramConstants.KEY_PARAM_DATAS, GsonUtil.getGson().toJson(list));
            pageModel.setBundle(bundle);
            if (isLive) {
                pageModel.setRouterPath(ProgramRouterPath.PLAYER_LIVE);
            } else if (isDrama) {
                pageModel.setRouterPath(ProgramRouterPath.PROGRAM_DRAMA);
            } else {
                pageModel.setRouterPath(ProgramRouterPath.PROGRAM);
            }
            pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        } else {
            pageModel.setCanJump(false);
            pageModel.setMsg("播放地址出错");
        }
        return pageModel;
    }


    public static PageModel getNormalPayerPageModel(PlayData playData, boolean isContinue) {
        PageModel pageModel = new PageModel();
        if (playData != null) {
            Bundle bundle = new Bundle();
            List<PlayData> list = new ArrayList<>();
            playData.putCustomData(PlayerDataConstants.PLAYER_IS_CONTINUE, isContinue);
            list.add(playData);
            bundle.putString(ProgramConstants.KEY_PARAM_DATAS, GsonUtil.getGson().toJson(list));
            pageModel.setBundle(bundle);
            pageModel.setRouterPath(ProgramRouterPath.PLAYER);
            pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        } else {
            pageModel.setCanJump(false);
            pageModel.setMsg("播放地址出错");
        }
        return pageModel;
    }

    public static PageModel getLivePayerPageModel(PlayData playData) {
        PageModel pageModel = new PageModel();
        if (playData != null) {
            Bundle bundle = new Bundle();
            List<PlayData> list = new ArrayList<>();
            list.add(playData);
            bundle.putString(ProgramConstants.KEY_PARAM_DATAS, GsonUtil.getGson().toJson(list));
            pageModel.setBundle(bundle);
            pageModel.setRouterPath(ProgramRouterPath.PLAYER_LIVE);
            pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        } else {
            pageModel.setCanJump(false);
            pageModel.setMsg("播放地址出错");
        }
        return pageModel;
    }


    public static PageModel getTopicPageModel(String code) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        bundle.putString(ProgramConstants.KEY_PARAM_ID, code);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        pageModel.setBundle(bundle);
        pageModel.setRouterPath(ProgramRouterPath.TOPIC);
        return pageModel;
    }

    public static PageModel getPageModel(RecommendModel recommendModel) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        String routerPath = getPath(recommendModel);
        pageModel.setRouterPath(routerPath);
        switch (routerPath) {
            case ProgramRouterPath.PLAYER:
                break;
            case ProgramRouterPath.PROGRAM:
                return getProgramPageModel(recommendModel.getPlayData());
            case ProgramRouterPath.PROGRAM_DRAMA:
                return getProgramDramaPageModel(recommendModel.getPlayData());
            case ProgramRouterPath.ARRANGE:
                bundle.putString(ProgramConstants.KEY_PARAM_ID, recommendModel.getLinkArrangeValue());
                bundle.putString(ProgramConstants.KEY_PARAM_TITLE, recommendModel.getName());
                break;
            case ProgramRouterPath.PLAYER_LIVE:
                return getLivePageModel(recommendModel.getPlayData());
            case ProgramRouterPath.LIVE_STATE_BEFORE:
                return getLiveBeforePageModel(recommendModel.getLinkArrangeValue());
            case ProgramRouterPath.LIVE_COMPLETED:
                return getLiveAfterPageModel(recommendModel.getLinkArrangeValue(), recommendModel.getNewPicUrl());
            case ProgramRouterPath.WEB_OUTSIDE:
                bundle.putString(ProgramConstants.WEBVIEW_URL, recommendModel.getLinkArrangeValue());
                break;
            case ProgramRouterPath.WEB_INNER:
                return getWebModel(recommendModel.getLinkArrangeValue(), recommendModel.getName());
            case ProgramRouterPath.WEB_NEWS:
                pageModel.setRouterPath(ProgramRouterPath.WEB_INNER);
                bundle.putParcelable(WEBVIEW_DATA, WebviewGoPageModel.createWebviewGoPageModel(recommendModel.getInfUrl(),
                        TitleBarModel.createTitleBarModel(recommendModel.getInfTitle()),
                        ShareModel.createBuilder().setUrl(recommendModel.getInfUrl())
                                .setImgUrl(recommendModel.getInfImageUrl())
                                .setDes(recommendModel.getInfIntroduction())
                                .setType(ShareConstants.TYPE_ALL)
                                .setShareType(ShareTypeConstants.TYPE_SHARE_NEWS)
                                .setTitle(recommendModel.getInfTitle()).build()));
                break;
            case ProgramRouterPath.TOPIC:
                return getTopicPageModel(recommendModel.getLinkArrangeValue());
            case ProgramRouterPath.PACKAGE:
                bundle.putString(ProgramConstants.KEY_PARAM_ID, recommendModel.getLinkArrangeValue());
                pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
                break;
            case ProgramRouterPath.COMPILATION:
                bundle.putString(CompilationPresenter.STR_CODE, recommendModel.getLinkArrangeValue());
                bundle.putString(CompilationPresenter.STR_BIT_CODE, recommendModel.getRecommendAreaCodes().get(0));
                bundle.putString(ProgramConstants.KEY_PARAM_GET_DATA_USECASE_PATH, ProgramRouterPath.USECASE_RECOMMEND);
                bundle.putString(ProgramConstants.KEY_PARAM_TITLE, recommendModel.getName());
                break;
            case ProgramRouterPath.PAGE_MIX:
                bundle.putString(ProgramConstants.KEY_PARAM_ID, recommendModel.getLinkArrangeValue());
                break;
            case ProgramRouterPath.PAGE_TITLE:
                bundle.putString(ProgramConstants.KEY_PARAM_ID, recommendModel.getLinkArrangeValue());
                bundle.putString(ProgramConstants.KEY_PARAM_TITLE, recommendModel.getName());
                break;
            case ProgramRouterPath.UNITY_SHOW:
                break;
            case ProgramRouterPath.SHOW_LIST:
                break;
            case ProgramRouterPath.FOOTBALL_HOME:
                pageModel.setStartUnity(true);
                pageModel.setRouterPath(ProgramRouterPath.UNITY_SOCCER);
                bundle = null;
                break;
            case ProgramRouterPath.FOOTBALL_RECOMMEND:
                pageModel.setStartUnity(true);
                pageModel.setRouterPath(ProgramRouterPath.UNITY_SOCCER);
                MatchInfo matchInfo = new MatchInfo(0, recommendModel.getLinkArrangeValue());
                pageModel.setUnityObject(matchInfo);
                bundle = null;
                break;
            case ProgramRouterPath.PUBLISHER_DETAIL:
                if (recommendModel.getCpInfo() != null) {
                    bundle.putString(PublisherDetailPresenter.EXTRA_CP_CODE, recommendModel.getCpInfo().getCode());
                    pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
                } else {
                    pageModel.setCanJump(false);
                }
                break;
            case ProgramRouterPath.STATION:
                pageModel.setCanJump(false);
                pageModel.setMsg("没更多数据喽！");
                bundle = null;
                break;
            default:
                pageModel.setCanJump(false);
                bundle = null;
                break;
        }
        pageModel.setBundle(bundle);
        return pageModel;
    }

    public static PageModel getWebModel(String url, String name) {
        PageModel pageModel = new PageModel();
        Bundle bundle = new Bundle();
        pageModel.setRouterPath(ProgramRouterPath.WEB_INNER);
        TitleBarModel titleBarModel = TitleBarModel.createTitleBarModel(name);
        ShareModel shareModel = null;
        if (!StrUtil.isEmpty(url) && url.indexOf("topBarTransparent=1") != -1) {
            titleBarModel.setType(4);
            shareModel = ShareModel.createBuilder().setUrl(url)
                    .setResId(R.mipmap.ic_activity_home_pageshare)
                    .setDes("活动期间，登陆“微鲸VR”APP看视频集祝福卡，丰厚奖品等你抢")
                    .setType(ShareConstants.TYPE_ALL)
                    .setShareType(ShareTypeConstants.TYPE_SHARE_WEB)
                    .setTitle("抢红包啦 | 你敢看，我就敢给").build();
        }
        bundle.putParcelable(WEBVIEW_DATA, WebviewGoPageModel.createWebviewGoPageModel(url, titleBarModel, shareModel));
        pageModel.setBundle(bundle);
        return pageModel;
    }

    public static String getPath(RecommendModel recommendModel) {
        String routerPath = null;
        switch (recommendModel.getLinkArrangeType()) {
            case LINKARRANGETYPE_ARRANGE:
                routerPath = ProgramRouterPath.ARRANGE;
                break;
            case LINKARRANGETYPE_MOREMOVIEPROGRAM:
            case LINKARRANGETYPE_MORETVPROGRAM:
            case LINKARRANGETYPE_PROGRAM:
                routerPath = ProgramRouterPath.PROGRAM;
                break;
            case LINKARRANGETYPE_DYNAMICPROGRAM:
                routerPath = ProgramRouterPath.PROGRAM_DRAMA;
                break;
            case LINKARRANGETYPE_LIVE:
                int status = recommendModel.getLiveStatus();
                if (LIVE_STATE_BEING == status) {
                    routerPath = ProgramRouterPath.PLAYER_LIVE;
                } else if (LIVE_STATE_BEFORE == status) {
                    routerPath = ProgramRouterPath.LIVE_STATE_BEFORE;
                } else {
                    routerPath = ProgramRouterPath.LIVE_COMPLETED;
                }
                break;
            case LINKARRANGETYPE_H5_OUTER:
                routerPath = ProgramRouterPath.WEB_OUTSIDE;
                break;
            case LINKARRANGETYPE_H5_INNER:
            case LINKARRANGETYPE_H5_LOCAL:
                routerPath = ProgramRouterPath.WEB_INNER;
                break;
            case LINKARRANGETYPE_INFORMATION:
                routerPath = ProgramRouterPath.WEB_NEWS;
                break;
            case LINKARRANGETYPE_MANUAL_ARRANGE:
                routerPath = ProgramRouterPath.TOPIC;
                break;
            case LINKARRANGETYPE_CONTENT_PACKAGE:
                routerPath = ProgramRouterPath.PACKAGE;
                break;
            case LINKARRANGETYPE_RECOMMENDPAGE:
                switch (recommendModel.getRecommendPageType()) {
                    case RECOMMEND_PAGE_TYPE_TITLE:
                        routerPath = ProgramRouterPath.PAGE_TITLE;
                        break;
                    case RECOMMEND_PAGE_TYPE_PAGE:
                        routerPath = ProgramRouterPath.COMPILATION;
                        break;
                    case RECOMMEND_PAGE_TYPE_MIX:
                        routerPath = ProgramRouterPath.PAGE_MIX;
                        break;
                    default:
                        routerPath = ProgramRouterPath.STATION;
                }
                break;
            case LINKARRANGETYPE_SHOW:
                routerPath = ProgramRouterPath.UNITY_SHOW;
                break;
            case LINKARRANGETYPE_SHOW_LIST:
                routerPath = ProgramRouterPath.SHOW_LIST;
                break;
            case LINKARRANGETYPE_FOOTBALL_HOMEPAGE:
                routerPath = ProgramRouterPath.FOOTBALL_HOME;
                break;
            case LINKARRANGETYPE_FOOTBALL_RECOMMENDPAGE:
                routerPath = ProgramRouterPath.FOOTBALL_RECOMMEND;
                break;
            case LINKARRANGETYPE_CONTENT_PROVIDER:
                routerPath = ProgramRouterPath.PUBLISHER_DETAIL;
                break;
            default:
                routerPath = ProgramRouterPath.STATION;
                break;
        }
        return routerPath;
    }

    public static PageModel goPagePageModelDownload() {
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(ProgramRouterPath.LOCAL);
        return pageModel;
    }

    public static PageModel goPagePageModelHistory() {
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(ProgramRouterPath.HISTORY);
        return pageModel;
    }

    public static PageModel goPagePageModelSearch() {
        PageModel pageModel = new PageModel();
        pageModel.setRouterPath(ProgramRouterPath.SEARCH);
        pageModel.setActivityType(RouterConstants.CONTAINER_ACTIVITY);
        return pageModel;
    }

}
