package com.whaley.biz.program.constants;

/**
 * Author: qxw
 * Date: 2016/9/13
 */
public interface ProgramConstants {

    //===================================api=======================================//

    String AGINOMOTO_APPSERVICE = "newVR-service/appservice/";

    //===================================服务器数据类型=====================================//
    /**
     * 推荐位{
     * 1：正常
     * 2：轮播
     * 3：广告
     * 4：品牌专区
     * 5：热门频道
     * 6：标题
     * 7:秀场
     * 8：电视剧
     * 9:发布者
     * 10:电影
     * 11:单个播放
     * }
     */


    int TYPE_DISCOVERY_NORMAL = 1;
    int TYPE_DISCOVERY_BANNER = 2;
    int TYPE_DISCOVERY_AD = 3;
    int TYPE_DISCOVERY_BRAND = 4;
    int TYPE_DISCOVERY_HOT = 5;
    int TYPE_DISCOVERY_TITLE = 6;
    int TYPE_DISCOVERY_SOCIAL = 7;
    int TYPE_DISCOVERY_TVPLAY = 8;
    int TYPE_DISCOVERY_PUBLISHER = 9;
    int TYPE_DISCOVERY_MOVIE = 10;
    int TYPE_DISCOVERY_SINGLE_PLAY = 11;
    /**
     * 推荐页类型
     */
    String RECOMMEND_PAGE_TYPE_TITLE = "recommendPageType_title";//标题推荐页（全景视频、电影、电视剧）
    String RECOMMEND_PAGE_TYPE_PAGE = "recommendPageType_page";//分页推荐页（集合列表、品牌专区列表‘之前的首页推荐’）
    String RECOMMEND_PAGE_TYPE_MIX = "recommendPageType_mix";//混合布局推荐页（）
    /**
     * 推荐元素链接类型
     */
    String LINKARRANGETYPE_ARRANGE = "linkarrangetype_arrange";//自动编排
    String LINKARRANGETYPE_PROGRAM = "linkarrangetype_program";//节目
    String LINKARRANGETYPE_LIVE = "linkarrangetype_live";//直播
    String LINKARRANGETYPE_H5_OUTER = "linkarrangetype_h5_outer";
    String LINKARRANGETYPE_H5_INNER = "linkarrangetype_h5_inner";//h5内页
    String LINKARRANGETYPE_H5_LOCAL = "linkarrangetype_h5_local";
    String LINKARRANGETYPE_INFORMATION = "linkarrangetype_information";//资讯
    String LINKARRANGETYPE_MANUAL_ARRANGE = "linkarrangetype_manual_arrange";//手动编排（专题）
    String LINKARRANGETYPE_RECOMMENDPAGE = "linkarrangetype_recommendPage";//推荐类型
    String LINKARRANGETYPE_MORETVPROGRAM = "linkarrangetype_moretvprogram";//电视猫电视剧
    String LINKARRANGETYPE_MOREMOVIEPROGRAM = "linkarrangetype_moremovieprogram";//电视猫电影

    String LINKARRANGETYPE_DYNAMICPROGRAM = "linkarrangetype_dynamicProgram";//互动剧

    String LINKARRANGETYPE_LIVEORDER_LIST = "linkarrangetype_liveorderList";  //直播预告列表页
    String LINKARRANGETYPE_LIVE_ON_LIST = "linkarrangetype_liveOnList";//直播列表页
    String LINKARRANGETYPE_SHOW = "linkarrangetype_show";//秀场页
    String LINKARRANGETYPE_SHOW_LIST = "linkarrangetype_showList";//秀场列表页
    String LINKARRANGETYPE_FOOTBALL_LIST = "linkarrangetype_footballList";//足球列表页
    String LINKARRANGETYPE_CONTENT_PACKAGE = "linkarrangetype_content_package";//节目包
    String LINKARRANGETYPE_FOOTBALL_HOMEPAGE = "linkarrangetype_footballHomePage";//足球首页
    String LINKARRANGETYPE_FOOTBALL_RECOMMENDPAGE = "linkarrangetype_footballRecommendPage";//足球推荐页
    String LINKARRANGETYPE_CONTENT_PROVIDER = "linkarrangetype_contentProvider";//发布者


    /**
     * 推荐元素类型
     * 1:纯文本
     * 2:图片
     */
    String TYPE_TEXT = "1";
    String TYPE_PIC = "2";

    /**
     * 视频类型
     */
    String VIDEO_TYPE_3D = "3d";//3d電影
    String VIDEO_TYPE_VR = "vr";//vr视频
    String VIDEO_TYPE_MORETV_2D = "moretv_2d";
    String VIDEO_TYPE_MORETV_TV = "moretv_tv";

    String VIDEO_TYPE_MORETV_MOVIE = "moretv_movie";//专题内容特有
    /**
     * 类型
     */
    String TYPE_ARRANGE = "arrange";//子专题
    String TYPE_RECORDED = "recorded";//节目视频（录播）类型
    String TYPE_LIVE = "live";//直播类型
    String TYPE_CONTENT_PACKGE = "content_packge";//节目包类型
    String TYPE_COUPON = "coupon";//券类型
    String TYPE_REDEEM_CODE = "redeemCode";//兑换码类型
    String TYPE_DYNAMIC = "dynamic";//互动剧类型

    /**
     * @author qxw
     * @time 2016/11/9 11:04
     * 跳转类型{
     * 1：详情内页
     * 2：游戏内页(app内已经没有了)
     * 3：直播内页
     * 4：广告网页
     * 5：新闻网页
     * 6：外部网页
     * 7：专题页
     * 8：子站点数（小列表形式）
     * 9：品牌单个列表(与专题重复去掉)
     * 10:品牌列表(与集合页面合并)
     * 11:更多频道
     * 12:集合页面
     * 13:秀场跳转
     * 14:秀场列表
     * 15:混合推荐页面
     * }
     */
    int TYPE_GO_PAGE_DETAIL = 1;
    int TYPE_GO_PAGE_DETAIL_GAME = 2;
    int TYPE_GO_PAGE_DETAIL_LIVE = 3;
    int TYPE_GO_PAGE_WEB = 4;
    int TYPE_GO_PAGE_WEB_NEWS = 5;
    int TYPE_GO_PAGE_WEB_OUTSIDE = 6;
    int TYPE_GO_PAGE_TOPITCS = 7;
    int TYPE_GO_PAGE_VIDEO_LIST = 8;
    int TYPE_GO_PAGE_TOP = 11;
    int TYPE_GO_PAGE_COMPILATION = 12;
    int TYPE_GO_PAGE_SHOW = 13;
    int TYPE_GO_PAGE_SHOW_LIST = 14;
    int TYPE_GO_PAGE_RECOMMEND_MIX = 15;
    int TYPE_GO_PAGE_PACKAGE = 16;
    int TYPE_GO_PAGE_FOOTBALL_HOME = 17;
    int TYPE_GO_PAGE_FOOTBALL_RECOMMEND = 18;

    /**
     * 播放tab{
     * 1：全景视频
     * 2：直播
     * 3：电视猫电视剧
     * 4：电视猫电影
     * 5: 3d电影
     * }
     */
    int TYPE_PLAY_PANO = 1;
    int TYPE_PLAY_LIVE = 2;
    int TYPE_PLAY_MORETV_TV = 3;
    int TYPE_PLAY_MORETV_2D = 4;
    int TYPE_PLAY_3D = 5;
    int TYPE_PLAY_LOCALVIDEO = 13;


    /**
     * 直播状态{
     * 0:直播预告
     * 1:直播中
     * 2:直播已结束
     * }
     */
    int LIVE_STATE_BEFORE = 0;
    int LIVE_STATE_BEING = 1;
    int LIVE_STATE_AFTER = 2;


    //====================================event====================================//
    String EVENT_LOGIN_SUCCESS = "login_success";
    String EVENT_SIGN_OUT = "sign_out";
    String EVENT_NOT_LOGIN_PAY = "not_login_pay";
    String EVENT_FOLLOW_MY_CLICK = "follow_my_click";
    String EVENT_RESERVE_CHANGE = "reserve_change";

    //=============================================================================//
    int TYPE_IS_TITLE = 1;

    //===================================参数key=========================================//
    String KEY_PARAM_GET_DATA_USECASE_PATH = "key_param_get_data_usecase_path";

    String KEY_PARAM_MAPPER_PATH = "key_param_mapper_path";

    String KEY_PARAM_ID = "key_param_id";
    String KEY_PARAM_TYPE = "key_param_type";
    String KEY_PARAM_TITLE = "key_param_title";
    String KEY_LOGIN_TIPS = "key_login_tips";
    String KEY_PAY_DATA = "key_pay_data";

    String KEY_PARAM_LIVE_CODE = "key_param_live_code";
    String KEY_PARAM_LIVE_BACKGROUND_IMAGE = "key_param_live_background_image";
    String KEY_PARAM_HAS_MEMBER_RANK = "key_param_has_member_rank";

    //=====================================Player===========================================//
    String KEY_PARAM_DATAS = "key_param_datas";
    String KEY_PAYER_POS = "key_payer_pos";
    //====================================WEB=========================================//
    String WEBVIEW_URL = "webview_url";
    String WEBVIEW_DATA = "webview_data";
    String WEBVIEW_TITLE = "webview_title";
    String WEBVIEW_IS_SINGLE_PAGE = "webview_is_single_page";
    String WEBVIEW_PRESENTER = "webview_presenter";
    String WEBVIEW_SHARE_MODEL = "webview_share_model";
    String WEBVIEW_CACHE_POLICE = "webview_cache_police";

}
