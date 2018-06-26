package com.whaley.biz.livegift;

import com.whaley.biz.livegift.model.GiftNoticeModle;
import com.whaley.biz.longconnection.BaseSocketBizManager;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.core.network.socketio.EventListener;

import org.json.JSONObject;

/**
 * Author: qxw
 * Date:2017/10/19
 * Introduction:
 */

public class GiftSocketBizManager extends BaseSocketBizManager {

    static final String EVENT_GIFT = "vr_gift_user_give";
    GiftSocketListener giftSocketListener;


    public void setGiftSocketListener(GiftSocketListener giftSocketListener) {
        this.giftSocketListener = giftSocketListener;
    }

    public GiftSocketBizManager(String code, String title) {
        super(code, title);
    }


    public void onAllQuit() {
        getConnectionController().quit(EVENT_GIFT);
    }

    @Override
    public void onJoin() {
        getConnectionController().quit(EVENT_GIFT);
        getConnectionController().join(EVENT_GIFT, new EventListener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                if (jsonObject != null && giftSocketListener != null) {
                    GiftNoticeModle giftNoticeModle = GsonUtil.getGson().fromJson(jsonObject.toString(), GiftNoticeModle.class);
                    giftSocketListener.onGift(giftNoticeModle);
                }
            }
        });
    }

    public void ceshi() {
        String JSON = "{\"liveCode\":\"519f52b1c87a40b19096e7ecc95bec9f\",\"uid\":\"48269644\",\"nickName\":\"meiling\",\"userHeadUrl\":\"\",\"memberCode\":\"c1a270e514284afcb42dca4ce323c904\",\"memberName\":\"杨俊2\",\"memberHeadUrl\":\"http://test-image.tvmore.com.cn/image/get-image/10000004/15014816411102449836.jpg/zoom/1024/768\",\"giftCode\":\"19674dcbf1dbf0c0f5aefe4557279f02\",\"giftName\":\"蛋糕\",\"giftPic\":\"http://store.whaley-vr.com/props/gift/2017-04-10/-4866991711491803997633\",\"giftIcon\":\"http://store.whaley-vr.com/props/gift/2017-04-10/-4866991711491804001090\"}";
        GiftNoticeModle giftNoticeModle = GsonUtil.getGson().fromJson(JSON, GiftNoticeModle.class);
        giftSocketListener.onGift(giftNoticeModle);
    }

    @Override
    public void onLogined(String notice) {
        if (giftSocketListener != null) {
            giftSocketListener.onLogined();
        }
    }

    public interface GiftSocketListener {
        void onGift(GiftNoticeModle giftNoticeModle);

        void onLogined();
    }

}
