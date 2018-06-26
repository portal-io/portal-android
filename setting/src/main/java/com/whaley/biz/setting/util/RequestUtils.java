package com.whaley.biz.setting.util;

import com.whaley.biz.sign.Sign;
import com.whaley.biz.sign.SignType;
import com.whaley.core.utils.MD5Util;
import com.whaleyvr.core.network.http.HttpManager;

/**
 * Created by dell on 2017/8/3.
 */

public class RequestUtils {

    public static String getPaySign(String... param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : param) {
            if(s!=null) {
                stringBuilder.append(s);
            }
        }
        stringBuilder.append(Sign.getSign(HttpManager.getInstance().isTest()?SignType.TYPE_TEST_WHALEYVR_PAY:SignType.TYPE_WHALEYVR_PAY));
        return MD5Util.getMD5String(stringBuilder.toString());
    }

    public static String getHistorySign(String... param) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : param) {
            if(s!=null) {
                stringBuilder.append(s);
            }
        }
        stringBuilder.append(Sign.getSign(HttpManager.getInstance().isTest()?SignType.TYPE_TEST_USER_HISTORY:SignType.TYPE_USER_HISTORY));
        return MD5Util.getMD5String(stringBuilder.toString());
    }

}
