package com.whaley.biz.user;

/**
 * Author: qxw
 * Date: 2016/9/13
 */
public interface UserConstants {
    String TYPE_AUTH_QQ = "qq";
    String TYPE_AUTH_WX = "wx";
    String TYPE_AUTH_WB = "wb";
    String REG_ILLEGAL = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）—|{}【】‘；：”“'。，、？]";

    String NCODE = "86";

    String FROM = "whaleyVR";


    int THIRD_ERROR = 1097;

    int THIRD_CANCEL = 2889;
    int THIRD_INSTALL = 3054;
    int THIRD_WEIBO = 5650;
    //====================================event====================================//
    String EVENT_LOGIN_SUCCESS = "login_success";
    String EVENT_SIGN_OUT = "sign_out";
    String EVENT_UPATE_NAME = "update_name";
    String EVENT_UPATE_AVATAR = "update_avatar";
    String EVENT_LOGIN_CANCEL = "login_cancel";
}
