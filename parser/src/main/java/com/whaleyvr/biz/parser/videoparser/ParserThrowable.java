package com.whaleyvr.biz.parser.videoparser;

/**
 * Created by dell on 2017/7/24.
 */

public class ParserThrowable extends Throwable {

    public final static String MSG_PARSER_RESULT_EMPTY = "播放地址解析失败";
    public final static String MSG_PARSER_INIT_FAIL = "解析库初始化失败";
    public final static String MSG_ORIGIN_URL_EMPTY = "播放地址为空";

    public ParserThrowable(String message) {
        super(message);
    }

}
