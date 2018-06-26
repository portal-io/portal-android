package com.whaley.biz.user.utils;

import android.text.TextUtils;

/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */

public class StringUtils {

    public static String getPhone(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.length() > 7) {
            String prePhone = phone.substring(0, 3);
            String subPhone = phone.substring(7);
            phone = prePhone.concat("****").concat(subPhone);
        }
        return phone;
    }
}
