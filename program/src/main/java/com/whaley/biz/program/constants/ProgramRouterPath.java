package com.whaley.biz.program.constants;

/**
 * Created by yangzhi on 2017/8/15.
 */

public class ProgramRouterPath {

    //=============================其他模块UI============================//
    public static final String PAY = "/pay/ui/paydialog";

    public static final String WEB_INNER = "/hybrid/ui/web";//内部网页
    public static final String WEB_NEWS = "/hybrid/ui/news";//新闻页面

    public static final String UNITY_SHOW = "/unity/service/startShow";//unity秀场

    public static final String UNITY_SOCCER = "/unity/service/startSoccer";//unity足球

    public static final String UNITY_PLAYER = "/unity/service/startPlayer";//unity播放器

    public static final String LOCAL = "/setting/ui/local";//本地管理

    public static final String HISTORY = "/setting/ui/history";//观看历史

    //=============================本模块UI=================================//


    public static final String PLAYER = "/program/ui/player";//

    public static final String PLAYER_LIVE = "/program/ui/liveplayer";//

    public static final String PLAYER_LOCAL = "/program/ui/localplayer";//

    public static final String PROGRAM = "/program/ui/program";//内页

    public static final String PROGRAM_DRAMA = "/program/ui/programdrama";//互动剧内页

    public static final String ARRANGE = "/program/ui/arrange";//小列表自动编排

    public static final String LIVE_STATE_BEFORE = "/program/ui/livebefore";//直播前

    public static final String WEB_OUTSIDE = "/program/ui/weboutside";//外部网页


    public static final String TOPIC = "/program/ui/topic";//专题页面手动编排

    public static final String PACKAGE = "/program/ui/package";//节目包

    public static final String COMPILATION = "/program/ui/compilation";//合集页面 分页推荐页

    public static final String PAGE_MIX = "/program/ui/pagemix";//混合推荐页

    public static final String PAGE_TITLE = "/program/ui/pagetitle";//标题推荐页（二级页面）

    public static final String PAGE_HOME = "/program/ui/home";//标题推荐页（二级页面）

    public static final String SEARCH = "/program/ui/search";//搜索页

    public static final String SHOW_LIST = "/program/ui/showlist";//秀场列表

    public static final String FOOTBALL_HOME = "/program/ui/footballhome";//足球主页

    public static final String FOOTBALL_RECOMMEND = "/program/ui/footballrecommend";//足球推荐页

    public static final String CHANNEL_RECOMMEND = "/program/ui/channelrecommend";


    public static final String FOLLOW_RECOMMEND = "/program/ui/followrecommend";//关注推荐页


    public static final String PUBLISHER_DETAIL = "/program/ui/publisherdetail";//发布者页面

    public static final String FOLLOW_MY = "/program/ui/followmy";//我的关注

    public static final String UNITY_PROGRAM = "/program/ui/unityprogram";//unity详情页

    public static final String UNITY_PACKAGE = "/program/ui/unitypackage";//unity节目包页

    public static final String COLLECT = "/program/ui/collect";//我的播单

    public static final String MY_RESERVE = "/program/ui/myreserve";//我的预约

    public static final String CONTRIBUTION_TAB = "/program/ui/contributiontab"; // 贡献榜 Tab 主页

    public static final String CONTRIBUTION_RANK = "/program/ui/contributionrank"; // 贡献榜页面

    public static final String LIVE_COMPLETED = "/program/ui/livecompleted"; // 直播结束页面

    //================================usecase======================================//
    public static final String USECASE_RECOMMEND = "/program/usecase/recommend";//推荐页usecase

    public static final String USECASE_CP_PROGRAM_LIST = "/program/usecase/cpprogram";//发布者内容usecase

    public static final String USECASE_ARRANGE = "/program/usecase/arrange";//自动编排usecase

    public static final String USECASE_RECOMMEND_UID = "/program/usecase/recommenduid";//推荐页需要uid

    public static final String USECASE_RECOMMEND_LIST = "/program/usecase/recommendlist";//分页推荐页
    public static final String USECASE_RESERVE = "/program/usecase/reserve";//直播预告

    public static final String USECASE_TOPIC = "/program/usecase/topic";//专题
    public static final String USECASE_PACKAGE = "/program/usecase/package";//专题

    //==================================service=======================================//
    public static final String SERVICE_LIVE_NOTICE = "/program/service/noticelive";//上报设备

    public static final String SERVICE_GO_RECOMMEND_PAGE = "/program/service/gorecommendpage";//跳转推荐页

    //==================================mapper=========================================//
    public static final String MAPPER_RECOMMENT = "/program/mapper/recommend";//推荐

    public static final String MAPPER_COMPILATION = "/program/mapper/compilation";//合集

    public static final String MAPPER_CP_PROGRAM_LIST = "/program/mapper/cpprogram";//发布者内容

    public static final String MAPPER_FOLLOW_RECOMMENT = "/program/mapper/recommendfollow";//关注推荐

    public static final String MAPPER_ARRANGE = "/program/mapper/arrange";//自动编排

    public static final String MAPPER_FOLLOW_MY = "/program/mapper/followmy";//我的关注

    public static final String MAPPER_FOOTBALL_RECOMMEND = "/program/mapper/footballrecommend";//足球推荐页

    public static final String MAPPER_LIVE_RECOMMEND = "/program/mapper/liverecommend";//足球推荐页

    public static final String MAPPER_RESERVE = "/program/mapper/reserve";//直播预告


    public static final String MAPPER_TOPIC = "/program/mapper/topic";//专题
    //=====================================占位===========================================//
    public static final String STATION = "station";//防止服务器出现新类型老版本不兼容

//    public static final String LIVE_AFTER = "after_the_live";//直播结束

    public static final String SERVICE_PROVIDE_BANNER_PLAYER = "/program/service/providebannerplayer";

    public static final String SERVICE_ROUTER_RECOMMEND = "/program/service/routerecommend";
}
