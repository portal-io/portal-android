package com.whaley.biz.setting.util;

/**
 * Created by dell on 2017/8/3.
 */

public class RedemptionErrorUtil {

    public static String getErrorSting(String subCode,String msg) {
        switch (subCode){
            case "033":
                return "兑换码错误，请检查输入";
            case "037":
            case "044":
                return "该兑换码已经过了有效兑换期:(";
            case "035":
                return "该兑换码已被兑换过，如果非本\n人操作请联系客服";
            case "034":
                return "这个兑换码之前兑换过啦:p";
            case "036":
                return "你已经拥有相同的观看券了，这个兑换码还可以送给朋友哦:p";
        }
        return msg;
    }

}
