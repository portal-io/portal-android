package com.whaley.biz.common;

import com.whaley.biz.common.utils.StringUtil;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.VersionUtil;

/**
 * Created by dell on 2016/7/20.
 */
public interface CommonConstants {

    String TAG = "VR-TAG";
    String LOCAL_ID_PREFIX = "local:";
    int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;


    String TODAY = "今天";
    String YESTERDAY = "昨天";
    String WITHIN_WEEK = "一周内";
    String BEYOND_WEEK = "更早";

    String PRE_FIRST_INSTALL = "pre_first_install";
    String PRE_SHOW_TIPS = "pre_show_tips";
    String PRE_WIFI_SWITCH = "pre_wifi_switch";
    String PRE_SPLASH_UPDATE = "pre_splash_last_update";
    String KEY_SPLASH_URL = "key_splash_url";
    String KEY_SPLASH_CATEGORY = "key_splash_category";
    String KEY_SPLASH_FROM = "key_splash_from";
    /*######################开机广告活动内容##########################*/
    String KEY_BOOT = "key_boot_";
    String KEY_POSTER = "key_poster_";
    String KEY_CONTENT = "key_content_";
    String KEY_SPLASH_TYPE = "type";
    String KEY_SPLASH_TIME = "time";
    String KEY_SPLASH_PATH = "path";
    String KEY_SPLASH_PARAM = "param";

//    String PRE_FIRST_LOAD = "pre_first_load";
//    String KEY_FIRST_LOAD = "pre_first_load";
    String PRE_PAGE_VISIT = "pre_visit";
    String KEY_FIRST_DETECT = "pre_detect";
    String KEY_FIRST_PLAY = "pre_first_play";
    String KEY_FIRST_VR = "pre_first_vr";
    String KEY_FIRST_CAMERA = "pre_first_camera";

    String PRE_LAST_REDEEM = "pre_last_redeem";
    String PRE_LOCKOUT_TIME = "pre_lockout_time";
    String KEY_LAST_REDEEM = "key_last_redeem";
    String KEY_LOCKOUT_TIME = "key_lockout_time";


    String KEY_UNREAD_MSG_UPDATE = "key_unread_msg_update";
    String KEY_UNREAD_MSG_PARAM = "key_unread_msg_param";

    String PRE_PAY_METHOD = "pre_pay_method";
    String KEY_PAY_METHOD = "key_pay_method";

    String PRE_TIME_DIFF = "pre_time_diff";
    String KEY_TIME_DIFF = "key_time_diff";

    String LEVEL = "clear_level";

    int TYPE_CONTENT_PANO = 1;
    int TYPE_CONTENT_LIVE = TYPE_CONTENT_PANO + 1;
    int TYPE_CONTENT_MOVIE = TYPE_CONTENT_LIVE + 1;
    int TYPE_CONTENT_SHORT = TYPE_CONTENT_MOVIE + 1;
    int TYPE_CONTENT_3D = TYPE_CONTENT_SHORT + 1;
    int TYPE_CONTENT_GAME = TYPE_CONTENT_3D + 1;
    int TYPE_PANOLIST = TYPE_CONTENT_GAME + 1;
    int TYPE_LIVELIST = TYPE_PANOLIST + 1;
    int TYPE_MOVIELIST = TYPE_LIVELIST + 1;
    int TYPE_SHORTLIST = TYPE_MOVIELIST + 1;
    int TYPE_3DLIST = TYPE_SHORTLIST + 1;
    int TYPE_GAMELIST = TYPE_3DLIST + 1;

    String KEY_SYSTEM_VERSION = "systemver";
    String VALUE_SYSTEM_VERSION = AppUtil.getVersionRelease();
    String KEY_APP_NAME = "appname";
    String VALUE_APP_NAME = "WhaleyVR";
    String KEY_APP_VERSION = "appver";
    String VALUE_APP_VERSION_NAME = VersionUtil.getVersionName();
    String VALUE_ANDROID_VERSION_NAME = StringUtil.setResolveString(VALUE_APP_VERSION_NAME);
    String KEY_APP_VERSION_CODE = "appvercode";
    String VALUE_APP_VERSION_CODE = "" + VersionUtil.getVersionCode();
    String KEY_SYSTEM_NAME = "systemname";
    String VALUE_SYSTEM_NAME = "android";
    String VALUE_DEVICE_MODEL = AppUtil.getDeviceModel();
    String VALUE_DEVICE_SERIAL = android.os.Build.MODEL;
    String VALUE_MAC_ADDRESS = AppUtil.getMacAddress();
    String VALUE_VERSION_RELEASE = VersionUtil.getVersionRelease();

    String APPLICATION_ID = "com.whaley.biz";

    int SHARE_REQUEST_CODE = 1024;
    String SHARE_PARAM_OUTSIDE = "ShareOutside";

}
