package com.whaley.biz.launcher.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.launcher.model.UpdateModel;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.GsonUtil;

/**
 * Author: qxw
 * Date:2017/9/18
 * Introduction:
 */

public class SharedPreferencesUtil implements CommonConstants {
    public static final String KEY_UNREAD_MSG_UPDATE = "key_unread_msg_update";
    public static final String PRE_SHOW_TIPS = "pre_show_tips";
    public static final String PRE_APP_UPDATE = "pre_app_update";
    public static final String PRE_DEVICE_ID_UPDATE = "pre_device_id_update";

    public static final String PRE_DEVICE_ID_UPDATE_PARAM = "pre_device_id_update_param";

    public static void setUnreadData(String accountId) {
        SharedPreferences preferences = getContext().getSharedPreferences(
                KEY_UNREAD_MSG_UPDATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int count = getUnreadData(accountId) + 1;
        editor.putInt(accountId, count);
        editor.commit();
    }

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

    public static void setUpdate(boolean isUpdate) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_APP_UPDATE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PRE_DEVICE_ID_UPDATE, isUpdate);
        editor.commit();
    }

    public static boolean getUpdate() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_APP_UPDATE,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PRE_DEVICE_ID_UPDATE, false);
    }

    public static String getUpDateData() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_APP_UPDATE,
                Context.MODE_PRIVATE);
        return preferences.getString(PRE_DEVICE_ID_UPDATE_PARAM, "");
    }

    public static void setUpDateData(String param) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_APP_UPDATE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PRE_DEVICE_ID_UPDATE_PARAM, param);
        editor.commit();
    }

    public static void deleteUpdateData() {
        setUpDateData("");
    }

    /**
     * 保存开机屏内容（以作跳转）
     */
    public static void setSplashData(String keyString, String param) {
        SharedPreferences preferences = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(keyString + KEY_SPLASH_PARAM, param);
        editor.commit();
    }

    /**
     * 获取开机屏的内容
     */
    public static String getSplashParam(String keyString) {
        SharedPreferences preference = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        return preference.getString(keyString + KEY_SPLASH_PARAM, "");
    }

    public static void deleteAllSplashLastUpdate() {
        deleteSplashLastUpdate(KEY_BOOT);
        deleteSplashLastUpdate(KEY_POSTER);
        deleteSplashLastUpdate(KEY_CONTENT);
    }

    public static void deleteSplashLastUpdate(String keyString) {
        SharedPreferences preferences = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }

    public static void setSplashLastUpdate(String keyString, String url) {
        SharedPreferences preferences = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(keyString + KEY_SPLASH_PATH, url);
        editor.commit();
    }

    public static void setSplashType(String keyString, int type) {
        SharedPreferences preferences = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(keyString + KEY_SPLASH_TYPE, type);
        editor.commit();
    }

    public static int getSplashType(String keyString) {
        SharedPreferences preference = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        return preference.getInt(keyString + KEY_SPLASH_TYPE, 0);
    }

    public static void setSplashTime(String keyString, long time) {
        SharedPreferences preferences = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(keyString + KEY_SPLASH_TIME, time);
        editor.commit();
    }

    public static long getSplashTime(String keyString) {
        SharedPreferences preference = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        return preference.getLong(keyString + KEY_SPLASH_TIME, 0);
    }

    /**
     * 获取开机屏网络图片地址
     */
    public static String getSplashUrl(String keyString) {
        SharedPreferences preference = getContext().getSharedPreferences(keyString,
                Context.MODE_PRIVATE);
        return preference.getString(keyString + KEY_SPLASH_PATH, "");
    }

    private static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }
}
