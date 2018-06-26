package com.whaley.biz.setting.constant;

/**
 * Created by dell on 2017/7/25.
 */

public interface SettingConstants {

    public static String DB_NAME = "localvideo_whaley.db";



    /**
     * 类型
     */
    public static final String TYPE_ARRANGE = "arrange";//子专题
    public static final String TYPE_RECORDED = "recorded";//节目视频（录播）类型
    public static final String TYPE_LIVE = "live";//直播类型
    public static final String TYPE_CONTENT_PACKGE = "content_packge";//节目包类型
    public static final String TYPE_COUPON = "coupon";//券类型
    public static final String TYPE_REDEEM_CODE = "redeemCode";//兑换码类型

    public static final int LIVE_STATE_BEFORE = 0;
    public static final int LIVE_STATE_BEING = 1;
    public static final int LIVE_STATE_AFTER = 2;

    public static final int CODE_RESULT_FOR_URL = 0;

    public final static int CAMERA_RESULT = 100;
    public final static int RESULT_LOAD_IMAGE = 200;
    public final static int RESULT_CLIP_IMAGE = 300;
    public final static int REQUEST_CAMERA = 400;

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    public static final int TYPE_LOCALVIDEO = 13;


    //====================event============//
    String EVENT_LOGIN_SUCCESS = "login_success";
    String EVENT_SIGN_OUT = "sign_out";
    String EVENT_UPATE_NAME = "update_name";
    String EVENT_UPATE_AVATAR = "update_avatar";
    String EVENT_REDEMTION_SUCCCESS = "redemtion_success";
    String EVENT_LOGIN_CANCEL = "login_cancel";

}
