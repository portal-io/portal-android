package com.whaley.biz.program.playersupport.exception;

/**
 * Created by YangZhi on 2017/8/24 16:05.
 */

public interface ProgramErrorConstants {

    /**
     * 网络错误
     */
    int STR_ERROR_NETWORK = -999;

    /**
     * 播放器弹出的异常
     */
    int STR_ERROR_PLAYER = -1999;


    /**
     * 解析库解析错误
     */
    int PARSER_ERROR = - 2999;

    /**
     * 解析时获取原始地址错误
     */
    int PARSER_ERROR_ORIGIN_URL = -2998;

    /**
     * 原始地址获取失败
     */
    int STR_ERROR_TV_SERIES = -3998;

    /**
     * 原始地址获取失败
     */
    int STR_ERROR_ORIGIN_URL = -3999;


    /**
     * 处理清晰度时报错
     */
    int STR_ERROR_RESOLVE_DEFINITION_ERROR = -4997;

    /**
     * 没有可用清晰度地址
     */
    int STR_ERROR_NONE_ENABLE_DEFINITION_ERROR = -4996;

    /**
     * 切换默认清晰度错误
     */
    int ERROR_SWITCH_DEFAULT_DEFINITION_ERROR = -4998;

    /**
     * 所有清晰度播放错误
     */
    int ERROR_ALL_DEFINITION_ERROR = -4999;

    /**
     * 需要支付
     */
    int ERROR_PAY_NEED_PAY = -5000;

    /**
     * 直播未开始
     */
    int ERROR_LIVE_ON_BEFORE = -5999;


}
