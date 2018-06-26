package com.whaley.biz.sign;


import android.content.Context;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by dell on 2017/2/13.
 */

public class Sign {

    public static String getSign(int signType) {
        switch (signType) {
            case SignType.TYPE_PAY:
                return com.whaley.lib.sign.Sign.getPaySign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_SHOW:
                return com.whaley.lib.sign.Sign.getShowSign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_WHALEYVR:
                return com.whaley.lib.sign.Sign.getWhaleyvrSign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_TEST_USER_HISTORY:
                return com.whaley.lib.sign.Sign.getTestUserHistorySign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_USER_HISTORY:
                return com.whaley.lib.sign.Sign.getUserHistorySign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_BI:
                return com.whaley.lib.sign.Sign.getBISign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_TEST_WHALEYVR_PAY:
                return com.whaley.lib.sign.Sign.getTestWhaleyvrThirdPaySign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_WHALEYVR_PAY:
                return com.whaley.lib.sign.Sign.getWhaleyvrThirdPaySign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_TEST_CURRENCY:
                return com.whaley.lib.sign.Sign.getTestWhaleyvrThirdPaySign(AppContextProvider.getInstance().getContext());
            case SignType.TYPE_CURRENCY:
                return com.whaley.lib.sign.Sign.getWhaleyvrThirdPaySign(AppContextProvider.getInstance().getContext());
            default:
                return "error";
        }
    }
}
