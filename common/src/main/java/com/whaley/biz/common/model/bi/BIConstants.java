package com.whaley.biz.common.model.bi;

/**
 * Created by dell on 2017/9/25.
 */

public interface BIConstants {

    //#############################2.7.0新埋点内容#################################//
    String BI_API_TEST = "http://test-wlslog.aginomoto.com/";

    String BI_API = "http://vrlog.aginomoto.com/";

    String DB_NAME = "bi_whaley.db";

    String LOG_VERSION = "02";
    String CURRENT_PROP_CURRENT_DRAMA_ID = "currentDramaId"; //节目名称，取值不固定
    String CURRENT_PROP_CURRENT_DRAMA_TITLE = "currentDramaTitle";//节目名称，取值不固定
    String CURRENT_PROP_DRAMA_ID = "dramaId"; //节目名称，取值不固定
    String CURRENT_PROP_DRAMA_TITLE = "dramaTitle";//节目名称，取值不固定
    String CURRENT_PROP_VIDEO_SID = "videoSid"; //节目名称，取值不固定
    String CURRENT_PROP_VIDEO_NAME = "videoName";//节目名称，取值不固定
    String CURRENT_PROP_VIDEO_TYPE = "videoType";//视频类型，取值包括{moretv_tv、moretv_movie、local_video、3D、VR、live}
    String CURRENT_PROP_VIDEO_TAGS = "videoTags";// 表明视频标签信息,不固定
    String CURRENT_PROP_SCREEM_TYPE = "screenType";//1:表示全屏播放，2:表示半屏播放（只有在APP上播放才会有这个字段）
    String CURRENT_PROP_CUR_BIT_TYPE = "curBitType";//清晰度，取值{HD、SD、4K、local}：HD-高清、SD：超清、local：本地视频
    String CURRENT_PROP_VIDEO_FORMAT = "videoFormat";//取值包括{{2d，3d，vr}
    String CURRENT_PROP_CONTENT_TYPE = "contentType";

    String EVENT_PROP_KEY_ACTION_TYPE = "actionType";// {startplay,endplay,pause,continue,startbuffer，endbuffer}；
    String EVENT_PROP_KEY_START_PLAY_DURATION = "startPlayDuration";//起播时长，actionType为startplay时，记录该值，其他动作为空；
    String EVENT_PROP_KEY_POSITION = "position";//开始视频位置，只有actionType为startplay、pause、startbuffer时值有意义，其他动作为空；
    //    String EVENT_PROP_KEY_OPEN_WAY = "openWay";//默认local,如果是从app播放跳转到Launcher播放，则为fromapp；只有actionType为startplay时才有值，其他动作类型时为空；
    String EVENT_PROP_KEY_DURATION = "duration";//播放时长，actionType为startplay、pause、startbuffer时为null；
    String EVENT_PROP_KEY_PLAY_MODE = "playMode";//{360_2D_OCTAHEDRAL、 PLANE_2D、 PLANE_3D_LR、PLANE_3D_UD、360_3D_LF、360_3D_UD、180_PLANE、180_3D_UD、180_3D_LF}；
    String EVENT_PROP_KEY_EXIT_TYPE = "exitType";// 表明退出类型，{selfend,crashexit，...};
    String EVENT_PROP_KEY_BUFFER_DATA = "bufferData";// 缓冲数据量，单位为KB，目前只有actionType为endbuffer时值有意义，其他动作不传；
    String EVENT_PROP_KEY_BITRATE = "bitrate";//码率大小，目前只有actionType为lowbitrate时值有意义，其他动作不传

    String EVENT_PROP_KEY_EVENT_ID = "eventID";
    String EVENT_PROP_KEY_EVENT_NAME = "eventName";
    String EVENT_PROP_KEY_EVENT_TYPE = "eventType";


    String ROOT_PLAY = "play";//播放界面
    String CHILD_PLAY = "play";//播放事件
    String ACTION_TYPE_START_PLAY = "startplay";  //启播
    String ACTION_TYPE_END_PLAY = "endplay";  //播放结束
    String ACTION_TYPE_PAUSE = "pause";//暂停播放
    String ACTION_TYPE_CONTINUE = "continue";//恢复播放
    String ACTION_TYPE_START_BUFFER = "startbuffer";//开始缓冲
    String ACTION_TYPE_END_BUFFER = "endbuffer";//缓冲结束
    String ACTION_TYPE_LOWBITRATE = "lowbitrate";//低码率
    String ACTION_TYPE_END_BROWSE = "endbrowse";  //结束浏览

    String OPEN_WAY_LOCAL = "local";


    String EXIT_TYPE_SELFEND = "selfend";
    String EXIT_TYPE_CASHEXIT = "crashexit";
    String EXIT_TYPE_PLAYERROR = "playerror";
    String EXIT_TYPE_START_PLAYERROR = "startplayerror";
    String NEXT_PAGEID_DETAIL = "detail";
    String NEXT_PAGEID_PLAY = "play";


    String SCREEM_TYPE_1 = "1";//全屏播放
    String SCREEM_TYPE_2 = "2";//半屏播放
    String SCREEM_TYPE_3 = "3";//banner播放

    String CUR_BIT_TYPE_HD = "HD";
    String CUR_BIT_TYPE_SD = "SD";
    String CUR_BIT_TYPE_4K = "4K";
    String CUR_BIT_TYPE_LOCAL = "local";
    String CUR_BIT_TYPE_SDA = "SDA";
    String CUR_BIT_TYPE_SDB = "SDB";
    String CUR_BIT_TYPE_TDA = "TDA";
    String CUR_BIT_TYPE_TDB = "TDB";

    //#############################3.1.0新埋点内容#############################//
    String ROOT_HOME_AD = "ad";
    String ROOT_HOME_PAGE = "homePage";
    String ROOT_PROGRAM_SET = "programSet";//集合界面
    String ROOT_PROGRAM_PACKAGE = "programPackage";//节目包界面
    String ROOT_LIVE_PREVUE = "livePrevue";//直播预告界面
    String ROOT_LIVE_DETAILS = "liveDetails";//直播界面
    String ROOT_VIDEO_DETAILS = "videoDetails";//视频详情界面
    String ROOT_PAY_DETAILS = "payDetails";//视频详情界面
    String ROOT_LIVE_END = "liveEnd";//直播结束界面
    String ROOT_CONVERT_DETAIL = "convertDetail";
    String ROOT_DRAMA = "dramaDetails";//互动剧
    String ROOT_H5_INNER = "h5_inner";//H5页面
    String ROOT_VOUVHER_MODEL = "voucherModel";//界面id
    String ROOT_VOUVHER_ID = "voucherId";//视频详情界面
    String ROOT_VOUVHER_NAME = "voucherName";//视频详情界面
    String ROOT_VOUVHER_TYPE = "voucherType";//视频详情界面
    String ROOT_RELATED_CODE = "relatedCode";

    String ROOT_FROM_TYPE = "fromType";//视频详情界面
    String ROOT_IS_UNITY = "isUnity";//视频详情界面

    String AD_CLICK = "onClick_ad";//广告点击事件
    String AD_CLOSE = "close_ad";//广告关闭事件
    String VIEW_CLICK = "onClick_view";//点击事件
    String BROWSE_VIEW = "browse_view";//浏览事件
    String PAY_SUCCESS = "pay_success";//支付成功
    String ORDER_FORM = "order_form";
    String DANMU = "danmu";
    String GIFT = "gift";
    String CONVERT = "convert";
    String SHARE = "share";
    String BROWSE_DURATION = "browse_duration";

    String CURRENT_PROP_VIEW_PAGE_ID = "pageId";//界面id
    String CURRENT_PROP_VIEW_PAGE_NAME = "pageName";
    String CURRENT_PROP_VIEW_PAGE_CHARGEABLE = "isChargeable";
    String CURRENT_PROP_VIEW_PAGE_TYPE = "pageType";//类型
    String CURRENT_PROP_VIEW_SHOW_TYPE = "showType";//类型
    String CURRENT_PROP_TOTAL_TIME = "totalTime";


    String PREVUE_CLICK_DOWNLOAD = "download_click";//下载事件
    String PREVUE_CLICK_COLLECTION = "collection_click";//收藏事件
    String PREVUE_DYNAMIC_SELECTED = "dynamicSelected";//选择事件




    String PREVUE_CLICK = "prevue_click";//预约事件


    //#############################2.8.0新埋点内容#############################//
    String ROOT_TOPIC = "topic";//专题页面
    String TOPIC_CLICK = "topic_click";//点击事件
    String TOPIC_VIEW = "topic_view";//浏览事件
    String ROOT_CONTENT_PACKAGE = "content_packge";

    String CURRENT_PROP_TOPIV_PAGE_ID = "topicPageId";//专题code，取值不固定
    String CURRENT_PROP_TOPIV_NAME = "topicName";//专题名字，取值不固定
    String EVENT_PROP_KEY_LOCATION_INDEX = "locationIndex";// 表明所在排序中的位置索引信息,最顶上播放按钮点击排序为"0"，其他视频图片点击从"1"开始


    //#############################2.7.0以前埋点内容#################################//
    String EVENT_PROP_KEY_NAME = "name";//标题，名字
    String EVENT_PROP_KEY_CODE = "code";
    String EVENT_PROP_KEY_TYPE = "type";//类型
    String EVENT_PROP_KEY_SHARE_URL = "shareUrl";//分享地址
    String EVENT_PROP_KEY_BUFFERING_TIME = "bufferingTime";//缓存时间
    String EVENT_PROP_KEY_PLAY_TIME = "playTime";//缓存时间
    String EVENT_PROP_KEY_TIME_OUT = "pauseTime";//暂停游戏
    String EVENT_PROP_KEY_ERROR_CODE = "errorCode";//错误码
    String EVENT_PROP_KEY_BUFFERING_COUNT = "bufferingCount";//缓存次数
    String EVENT_PROP_KEY_POS = "position";//事件发生位置

    String CURRENT_PROP_KEY_FULL = "isFullScreen";//是否全屏
    String CURRENT_PROP_KEY_MONOCULAR = "isMonocular";//是否手机模式
    String CURRENT_PROP_KEY_TITLE = "title";//页面标题
    String CURRENT_PROP_KEY_CODE = "currentCode";//页面标题


    String ROOT_WELCOME = "welcome";//欢迎页
    String ROOT_HOME = "home";//主页
    String ROOT_RECOMMENDATION = "recommendation";//推荐页
    String ROOT_LIVE = "live";//直播
    String ROOT_ON_LIVE = "onLive";//直播
    String ROOT_ORDER = "order";//预约
    String ROOT_REVIEW = "review";//直播回顾
    String ROOT_ME = "me";//我
    String ROOT_VIDEO_PLAY = "videoPlay";//播放页面

    String ROOT_LIVE_INFO = "liveInfo";//直播详情页
    String ROOT_VIDEO_DETAIL = "videoDetail";//视频详情页
    String ROOT_MOVIE_DETAIL = "movieDetail";//電影详情页
    String ROOT_ORDER_DETAIL = "orderInfo";//直播预约详情页
    String ROOT_TV_SERIES_DETAIL = "tvSeriesDetail";//电视剧详情页
    String ROOT_TV_MOVIE_DETAIL = "tvMovieDetail";//电视猫电影详情页
    String ROOT_NEWS_DETAIL = "newsDetail";//资讯详情页
    String ROOT_H5_DETAIL = "webDetail";//內部h5页
    String ROOT_WEB_OUTSIDE = "webOutside";//外部h5页面
    String ROOT_SUBJECT = "topic";//专题列表
    String ROOT_VIDEO_LIST = "videoList";//视频列表
    String ROOT_BRAND_LIST = "brand";//品牌列表页
    String ROOT_VIDEO_RECOMMENDATION_LIST = "videoRecommendList";//视频推薦列表
    String ROOT_VIDEO_COLLECTION = "collection";//专题合辑
    String ROOT_PURCHASE = "purchase";//直播详情页
    String ROOT_SOCCER = "soccer";//足球tab页

    String ROOT_TEST = "test";//播放检测页
    String ROOT_REGISTER = "register";//注册
    String ROOT_LOGIN = "login";//登录
    String ROOT_INFORMATION = "information";// 个人信息
    String ROOT_NAME = "modifyName"; //    修改昵称界面
    String ROOT_PASSWORD = "modifyPassword";//   修改密码界面
    String ROOT_MOBILE = "modifyMobile";//修改手机
    String ROOT_SOCIAL = "social";//三方社交平台登录注册
    String ROOT_SMS_LOGIN = "smsLogin";//短信快捷登录注册
    String ROOT_CHANNEL = "channel";//频道页
    String ROOT_CINEMA = "cinema";//电影页


    String ROOT_COLLECTION = "collect";//收藏界面
    String ROOT_HISTORY = "history";//历史浏览
    String ROOT_FEEDBACK = "feedback";//反馈页面
    String ROOT_SETTING = "setting";//设置页面
    String ROOT_ADDRESS = "address";//地址页面
    String ROOT_LOCAL = "local";//本地页面
    String ROOT_LOCALVIDEO = "localVideo";//缓存页
    String ROOT_GAME_DOWNLOAD = "gameDownload";//我的下载
    String ROOT_DOWNLOAD = "download";//我的下载
    String ROOT_RESEARCH = "research";//搜索页面
    String ROOT_ABOUT = "about";

    String ROOT_PRIZE = "prize";//奖品页面
    String ROOT_HELP = "help";
    String ROOT_MY_PURCHASE = "myPurchase";
    String ROOT_ALBUMIMPORT = "albumImport";
    String ROOT_LINKIMPORT = "linkImport";
    String ROOT_CODEIMPORT = "codeImport";


    String CHILD_LIVE_NO_PURCHASE = "liveNoPurchase";//待支付节目包
    String CHILD_LIVE_PURCHASE = "livePurchased";//支付节目包
    String CHILD_ORDER_NO_PURCHASE = "orderNoPurchase";//待支付节目包
    String CHILD_ORDER_PURCHASE = "orderPurchased";//支付节目包
    String CHILD_VIDEO_NO_PURCHASE = "videoNoPurchase";//待支付节目包
    String CHILD_VIDEO_PURCHASE = "videoPurchased";//支付节目包
    String CHILD_POCKET_NO_PURCHASE = "pocketNoPurchase";//待支付节目包
    String CHILD_POCKET_PURCHASE = "pocketPurchased";//支付节目包


    String CHILD_ALIPAY = "alipay";//待支付节目包


    String CHILD_TOPIC = "topic";
    String CHILD_SKIP = "skip";//跳过
    String CHILD_LOCAL = "local";//跳到本地
    String CHILD_PRIZE = "prize";
    String CHILD_HELP = "help";
    String CHILD_SHARE = "share";
    String CHILD_CHANNEL = "channel";//频道页
    String CHILD_RECOMMENDATION = "recommendation";//recommendation	推荐tab	点击后进入推荐tab
    String CHILD_FOLLOW = "follow";//recommendation	推荐tab	点击后进入推荐tab
    String CHILD_LIVE = "live";//    live	直播tab	点击后进入直播tab
    String CHILD_ME = "me";//    me	我tab	点击后进入我tab
    String CHILD_VR = "VR";
    String CHILD_LOCK = "lock";
    String CHILD_FEEDBACK = "feedback";//点击跳转反馈界面
    String CHILD_INFORMATION = "information";// 点击跳转个人信息//预约详情页
    String CHILD_HISTORY = "history";//点击跳转历史浏览
    String CHILD_SETTING = "setting";// 点击跳转设置页面
    String CHILD_VIDEO = "video";//    video[id]	播放视频	点击每个cell上的播放按钮进行播放（按ID区分）	V1.1
    String CHILD_TITLE = "title";//    title[id]	标题	点击每个cell的标题进入详情页或者专题列表（按ID区分）	V1.2	原本进入内容详情页，V1.2的推荐内包含专题，因此需要加入专题计数
    String CHILD_WECHAT = "wechat";//    wechat	微信分享或者登陆	点击分享到微信	V1.2
    String CHILD_MOMENTS = "moments";//    朋友圈分享	点击分享到朋友圈	V1.2
    String CHILD_EXTEND_WECHAT = "extendWechat";//    extendWechat	微信分享	点击分享弹出中的微信	V1.2
    String CHILD_EXTEND_QQ = "extendQQ";//    extendQQ	QQ分享	点击分享弹出中的QQ	V1.2
    String CHILD_EXTEND_MOMENTS = "extendMoments";//    extendMoments	朋友圈分享	点击分享弹出中的朋友圈	V1.2
    String CHILD_EXTEND_WEIBO = "extendWeibo";//    extendWeibo	微博分享	点击分享弹出中的微博	V1.2
    String CHILD_EXTEND_CHAIN = "extendChain";//    extendChain	链接分享	点击分享弹出中的链接	V1.2
    String CHILD_CANCEL = "cancel";//    cancel	点击取消	在第一次弹出硬件检测时点击取消	V1.2
    String CHILD_CONFIRM = "confirm";//    confirm	确认	在第一次弹出硬件检测时点击确定	V1.2
    String CHILD_MOBILE = "mobile"; //    mobile	点击手机号输入框	点击后输入手机号码	V1.2
    String CHILD_CODE = "code"; //    点击获取安全码
    String CHILD_NAME = "name"; //    点击昵称
    String CHILD_SMSCODE = "smsCode"; //    点击输入短信验证码
    String CHILD_PICCODE = "picCode"; //    点击输入图形验证码
    String CHILD_PASSWORD = "password";//    password	点击密码输入框	点击后输入密码	V1.2
    String CHILD_SMS_LOGIN = "smsLogin";//    smsLogin	短信快捷登陆	点击后进入快捷登录页面	V1.2
    String CHILD_FORGET = "forget";//    forget	点击忘记密码	点击后进入找回密码流程	V1.2
    String CHILD_QQ = "qq";//    qq	点击qq登陆	点击qq登陆后跳转到第三方授权	V1.2
    String CHILD_WEIBO = "weibo";//    weibo	点击微博登陆	点击微博登陆跳转到第三方授权	V1.2
    String CHILD_LOGIN = "login";//    login	点击登陆按钮	点击后进行登陆
    String CHILD_REGISTER = "register";//    login	点击登陆按钮	点击后进行登陆
    String CHILD_AVATAR = "avatar";//    avatar	点击头像编辑
    String CHILD_QQ_CANCEL = "qqCancel";//    qqCancel	qq解绑	点击解绑qq	V1.2
    String CHILD_WECHAT_CANCEL = "wechatCancel";//    wechatCancel	微信解绑	点击解绑微信	V1.2
    String CHILD_WEIBO_CANCEL = "weiboCancel";//    weiboCancel	微博解绑	点击解绑微博	V1.2
    String CHILD_TAKE_PICS = "takePics";//    takePics	拍摄头像照片	点击后进入拍摄照片	V1.2
    String CHILD_GALLERY = "gallery";//    gallery	从相册选择照片做头像	点击后从相册选择照片	V1.2

    String CHILD_VIDEO_ORDER = "videoOrder";//    点击预约中的视频
    String CHILD_VIDEO_LIVE = "videoLive";//    点击直播中的视频


    String CHILD_HOTSUBJECT = "hotSubject";//    hotSubject[name]	热门板块	点击进入对应专题、频道详情页（以名称区分）	V1.1
    String CHILD_MODULE = "module";//    module[id]	推荐位模块	点击进入对应推荐详情（以ID区分）	V1.1
    //    String CHILD_ = "";//            [name]	点击对应专题进入专题列表	点击进入对应推荐专题、分类	V1.2	由V1.1更多按钮修改成对应专题列表按钮
    String CHILD_MORE = "more";//    more	点击展开更多	展开	V1.2
    String CHILD_SEARCH = "search";//    search	搜索	点击搜索框进行搜索	V1.2


    String CHILD_BANNER = "banner";// 点击banner位置轮播图	点击进入轮播图的详情页
    String CHILD_BANNER_AD = "bannerAd";
    String CHILD_ON_LIVE = "onLive";//点击正在直播按钮进入正在直播列表页
    String CHILD_REVIEW = "review";//点击进入回顾页
    String CHILD_ORDER = "order";//点击进入预告//点击预约
    String CHILD_ORDER_CANCEL = "orderCancel";//取消预约
    String CHILD_PURCHASE = "purchase";//取消预约
    String CHILD_BRAND_DETAIL = "brandDetail"; //点击进入品牌专题；
    String CHILD_ENTER = "enter";//"进入下一页"
    String CHILD_REFRESH = "refresh";//    换一批
    String CHILD_TEXT = "text";//    点击文字推荐
    String CHILD_CHANGE_VIDEO = "changeVideo";//    changeVideo	切换播放
    String CHILD_RESUME = "resume";//恢复播放
    String CHILD_COMPLETE = "complete";//播放完成
    String CHILD_REPLAY = "replay";//重播
    String CHILD_FREE_REPLAY = "freeReplay";//重播
    String CHILD_PURCHASE1 = "purchase_1";//待支付节目包
    String CHILD_PURCHASE2 = "purchase_";//待支付节目包
    String CHILD_FULL_SCREEN = "fullScreen";//切换全屏半屏
    String CHILD_ERROR = "error";//播放出错
    String CHILD_BACK = "back";//    back	返回
    String CHILD_COMMENT = "comment";//    点击评论按钮输入弹幕
    String CHILD_SEND = "send";//点击发送按钮发送弹幕
    String CHILD_MONOCULAR = "monocular";//    monocular	切换模式
    String CHILD_RENDER_TYPE = "renderType";//renderType 清晰度切换
    String CHILD_COLLECT = "collect";//   收藏
    String CHILD_CANCLE_COLLECT = "collectCancel";//   取消收藏

    String CHILD_DOWNLOAD = "download"; //    download	离线缓存tab	点击进入离线缓存tab	V1.2
    String CHILD_LOCALVIDEO = "localVideo";//   本地视频
    String CHILD_GAMEDOWNLOAD = "gameDownload";//   游戏下载
    String CHILD_SELECT_ALL = "selectAll";//    selectAll	全选	在编辑模式点击全选按钮	V1.2
    String CHILD_PAUSE = "pause";//    pause	暂停	点击暂停下载	V1.2
    String CHILD_OPEN = "open";//    open	打开	点击打开	V1.2
    String CHILD_SETUP = "setup";//    setup	安装	点击安装	V1.2
    String CHILD_CONTINUE = "continue";//            continue	继续	点击继续下载	V1.2
    String CHILD_LINKIMPORT = "linkImport";//
    String CHILD_CODEIMPORT = "codeImport";//
    String CHILD_ALBUMIMPORT = "albumImport";//
    String CHILD_EDIT = "edit";//
    String CHILD_SELECTALL = "selectAll";//
    String CHILD_CANCEL_SELECTALL = "cancelSelectAll";//

    String CHILD_SELECT = "select";//
    String CHILD_DELETE = "delete";//
    String CHILD_IMPORT = "import";//
    String CHILD_INPUT = "input";//
    String CHILD_ALBUM = "album";//
    String CHILD_NEXT = "next";//点击播放下一个/点击下一步SMSCODE
    String CHILD_CENTER = "next";//点击播放下一个/点击下一步SMSCODE

    String CHILD_RESEARCH = "research";//刷新
    String CHILD_RESULT = "result";//刷新
    String CHILD_FORUM = "官方论坛";//刷新

    String CHILD_WIFI_OPEN = "wifiOpen";////    wifiOpen	仅在wifi下下载	默认开启，若从关闭到开启则计数一次
    String CHILD_WIFI_CLOSE = "wifiClose";//    wifiClose	关闭仅在wifi下下载	从开启到关闭则计数一次
    String CHILD_CLEAR = "clear";//    clear	清除缓存	点击后清除缓存
    String CHILD_UPDATE = "update";//    update	版本更新	点击后更新版本
    String CHILD_ABOUT = "about";//    about	关于	点击后进入关于页面
    String CHILD_TEST = "test";//    test	点击硬件播放检测	点击后进入检测流程
    String CHILD_HD = "HD";//    HD	点击默认画质高清	高清按钮
    String CHILD_SD = "SD";//    SD	点击默认画质超清	2K八面体按钮
    String CHILD_LOGOUT = "logout";//退出登录

}
