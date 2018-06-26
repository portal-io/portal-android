package com.whaley.biz.program.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */

public class SharedPreferencesUtil {
    public static final String KEY_UNREAD_MSG_UPDATE = "key_unread_msg_update";
    public static final String PRE_SHOW_TIPS = "pre_show_tips";

    public static int getUnreadData(String accountId) {
        SharedPreferences preference = getContext().getSharedPreferences(KEY_UNREAD_MSG_UPDATE,
                Context.MODE_PRIVATE);
        return preference.getInt(accountId, 0);
    }

    public static void resetUnreadData(String accountId) {
        SharedPreferences preferences = getContext().getSharedPreferences(
                KEY_UNREAD_MSG_UPDATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(accountId, 0);
        editor.commit();
    }


    private static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }
}
