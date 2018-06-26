package com.whaley.biz.livegift;

/**
 * Author: qxw
 * Date:2017/10/20
 * Introduction:
 */

public interface GiftConstants {


    //==================================EVENT====================================//

    String EVENT_LOGIN_SUCCESS = "login_success";
    //直播有成员
    String MEMBER_LIVE_GIFT_EVENT = "/program/event/memberlivegift";

    //直播有送礼礼物
    String LIVE_GIFT_EVENT = "/program/event/livegift";

    //礼物面板显示
    String SHOW_LIVE_GIFT_EVENT = "/program/event/showlivegift";

    //礼物面板消失
    String HIDE_LIVE_GIFT_EVENT = "/livegift/event/gifthide";

    //礼物面板弹起高度
    String UPDATE_GIFT_HEIGHT_EVENT = "/livegift/event/updategiftheight";

    //自己发的礼物
    String HIDE_LIVE_SELF_GIFT_EVENT = "/livegift/event/selfgift";

    //===================================SERVICE=======================================//
    //鲸币消费
    String PAY_USERCOST_SERVICE = "/pay/service/usercost";

    //================================UI=======================================//
    String CURRENCY = "/setting/ui/currency";


    //=================================key=======================================//
    String ORIENTATION_IS_LANDSCAPE = "key_orientation_islandscape";
}
