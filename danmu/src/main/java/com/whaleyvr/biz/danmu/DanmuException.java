package com.whaleyvr.biz.danmu;

/**
 * Created by dell on 2017/7/13.
 */

public class DanmuException extends Exception{

    public final static String MSG_OPERATION_FREQUENT = "操作频繁";
    public final static String MSG_NOT_LOGIN = "未登录";
    public final static String MSG_CONTENT_EMPTY = "弹幕内容为空";
    public final static String MSG_START_VERIFY_TOKEN = "开始token验证";
    public final static String MSG_WAIT_VERIFY_TOKEN = "等待token验证完成";
    public final static String MSG_NETWOTK_ERROR = "网络异常";
    public final static String MSG_ERROR = "";

    public DanmuException() {
        super();
    }

    public DanmuException(String msg) {
        super(msg);
    }

    public DanmuException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DanmuException(Throwable cause) {
        super(cause);
    }

}
