package com.whaleyvr.biz.unity.util;

/**
 * Created by dell on 2016/11/24.
 */

public interface HermesConstants {

    String EVENT_PLAY = "startPlayer";
    String EVENT_SCENE = "startScene";
    String EVENT_SHOW = "startLiveVR";
    String EVENT_SOCCER = "startSoccerVR";

    String EVENT_SYN = "PluginSynData";
    String EVENT_SEND_USERINFO = "sendUserInfo";
    String EVENT_SEND_LOGIN = "sendLogin";
    String EVENT_READY = "SceneReady";
    String EVENT_LOGIN = "requireToken";
    String EVENT_REGISTER = "requireRegister";
    String EVENT_GIFT = "viewGift";
    String EVENT_RESUME = "Manager2UnityResume";
    String EVENT_SEND_ISTEST = "sendIsTest";
    String EVENT_REPORT_VIDEO_PROGRESS = "reportVideoProgress";
    String EVENT_SWITCH_FULLSCREEN = "switchFullscreen";
    String EVENT_EXIT_PLAY = "exitPlay";

    String EVENT_OPERATION_LOCALVIDEO = "operation_localvideo";
    String EVENT_OPERATION_LOCALVIDEO_CALLBACK = "operation_localvideo_callback";

    String EVENT_SEND_REDEEM = "sendRedeemCode";
    String EVENT_ADD_REDEEM = "addRedeemCode";

    String EVENT_VIDEO_PAYMENT = "videoPayment";
    String EVENT_PROGRAM_PAYMENT = "programSetPayment";
    String EVENT_VIDEO_PAYMENT_CALLBAK = "videoPaymentCallback";

    String EVENT_GET_METADATA = "GetMetaData";
    String EVENT_SEND_METADATA = "MetaDataValue";

    String MOVIE_MODE_VR = "VR";
    String MOVIE_MODE_3D = "3D";

    String MOVIE_FORMAT_2D = "2D";
    String MOVIE_FORMAT_3DUD = "3DUD";
    String MOVIE_FORMAT_3DLR = "3DLR";

    String MOVIE_SOURCE_LIVE = "LIVE";
    String MOVIE_SOURCE_ONLINE = "ONLINE";
    String MOVIE_SOURCE_OFFLINE = "OFFLINE";

    String EVENT_REPORT_DEVICE = "reportDevice";
    String EVENT_REPORT_DEVICE_CALLBACK = "reportDeviceCallback";
    String EVENT_CHECK_DEVICE = "checkDevice";
    String EVENT_CHECK_DEVICE_CALLBACK = "checkDeviceCallback";
    String EVENT_REDEEM_CODE = "input_redeem_Code";

    String EVENT_EXCHANGE_CODE_RESULT = "exchange_code_result";
}
