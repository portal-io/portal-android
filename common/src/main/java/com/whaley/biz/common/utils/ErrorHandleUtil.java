package com.whaley.biz.common.utils;

import android.accounts.NetworkErrorException;

import com.whaley.biz.common.BuildConfig;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

/**
 * Created by YangZhi on 2017/8/10 19:28.
 */

public class ErrorHandleUtil {

    public static Throwable getFormatThrowable(Throwable t) {
        if (!"Canceled".equals(t.getMessage()) && !(t instanceof SocketException)) {
            if (t instanceof SocketTimeoutException) {
                t = new NetworkErrorException("连接超时了，请稍候重试", t);
            } else if (t instanceof ConnectException || t instanceof UnknownHostException || t instanceof SSLException) {
                t = new NetworkErrorException("网络出错了，请检查后重试", t);
            } else if (t instanceof NetworkErrorException) {
                t = new NetworkErrorException("没有网络信号，请检查网络配置后重试", t);
            } else {
                String msg = "出错了，请稍后重试" + (BuildConfig.DEBUG ? t.getMessage() : "");
                t = new Throwable(msg, t);

            }
            return t;
        }
        return null;
    }

}
