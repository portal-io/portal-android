package com.whaley.biz.program.constants;

/**
 * Created by YangZhi on 2017/8/4 21:26.
 */

public class PlayerConstants {
    public static final String TYPE_FOOTBALL = "football";
    public static final String TYPE_LIVE_FOOTBALL = "live_football";
    public static final String TYPE_LIVE_CAR = "live_car";
    public static final String CAMERA_PUBLIC = "Public";


    /**
     * 获取节目/直播详情
     */
    public static final int PREPARE_START_PLAY_PRIORITY_GET_INFO = 100;
    /**
     * 电视猫电视剧类型处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_TV = 99;
    /**
     * 电视猫电影和电视剧资源处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_TVMORE = 98;
    /**
     * 节目详情页面处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_PROGRAM = 97;


    /**
     * 支付相关处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_PAY = 90;


    /**
     * 非直播节目解密播放地址处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_DECRYPT_URL = 81;
    /**
     * 非直播节目解析真是播放地址处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_PARSER = 80;
    /**
     * 处理组装清晰度集合
     */
    public static final int PREPARE_START_PLAY_PRIORITY_RESOLVE_DEFINITION = 70;
    /**
     * 切换清晰度控制
     */
    public static final int PREPARE_START_PLAY_PRIORITY_SWITCH_DEFINITION = 60;
    /**
     * 直播解密播放地址处理
     */
    public static final int PREPARE_START_PLAY_PRIORITY_LIVE_DECRYPT_URL = 50;

    /**
     * 切换清晰度错误的拦截
     */
    public static final int ERROR_EVENT_PRIORITY_SWITCH_DEFINITION = 100;

    /**
     * 电视猫电视剧的错误拦截
     */
    public static final int ERROR_EVENT_PRIORITY_TV_MORE = 99;
}
