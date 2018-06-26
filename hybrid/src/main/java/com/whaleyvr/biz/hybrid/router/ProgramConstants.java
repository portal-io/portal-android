package com.whaleyvr.biz.hybrid.router;

/**
 * Created by dell on 2017/9/7.
 */

public interface ProgramConstants {

    /**
     * 视频类型
     */
    String VIDEO_TYPE_3D = "3d";//3d電影
    String VIDEO_TYPE_VR = "vr";//vr视频
    String VIDEO_TYPE_MORETV_2D = "moretv_2d";
    String VIDEO_TYPE_MORETV_TV = "moretv_tv";

    int TYPE_PLAY_PANO = 1;
    int TYPE_PLAY_LIVE = 2;
    int TYPE_PLAY_MORETV_TV = 3;
    int TYPE_PLAY_MORETV_2D = 4;
    int TYPE_PLAY_3D = 5;
    int TYPE_PLAY_LOCALVIDEO = 13;

    /**
     * 类型
     */
    String TYPE_ARRANGE = "arrange";//子专题
    String TYPE_RECORDED = "recorded";//节目视频（录播）类型
    String TYPE_LIVE = "live";//直播类型
    String TYPE_CONTENT_PACKGE = "content_packge";//节目包类型
    String TYPE_COUPON = "coupon";//券类型
    String TYPE_REDEEM_CODE = "redeemCode";//兑换码类型

    String KEY_PARAM_ID = "key_param_id";

    public static final int LIVE_STATE_BEFORE = 0;
    public static final int LIVE_STATE_BEING = 1;
    public static final int LIVE_STATE_AFTER = 2;

}
