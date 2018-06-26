package com.whaley.biz.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.whaley.biz.common.CommonConstants;
import com.whaley.core.appcontext.AppContextProvider;

import java.util.Map;

public class SharedPreferencesUtil implements CommonConstants {

    public final static int DEFINITION_LEVEL_ST = 0;
    public final static int DEFINITION_LEVEL_SD = 1;

    public static void setFirstInstall(boolean isFirst) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_INSTALL,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(PRE_FIRST_INSTALL, isFirst);
        editor.commit();
    }

    public static boolean isFirstInstall() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_INSTALL,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PRE_FIRST_INSTALL, true);
    }


    public static void setWifiOnly(boolean status) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_WIFI_SWITCH,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(PRE_WIFI_SWITCH, status);
        editor.commit();
    }

    public static boolean getWifiOnly() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_WIFI_SWITCH,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PRE_WIFI_SWITCH, true);
    }

    public static boolean getShowTips() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_SHOW_TIPS,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PRE_SHOW_TIPS, false);
    }

    public static void setShowTips(boolean isShowTips) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_SHOW_TIPS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PRE_SHOW_TIPS, isShowTips);
        editor.commit();
    }

    public static void setDefinitionLevel(int level) {
        SharedPreferences preferences = getContext().getSharedPreferences(
                LEVEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LEVEL, level);
        editor.commit();
    }

    public static int getDefinitionLevel() {
        SharedPreferences preferences = getContext().getSharedPreferences(
                LEVEL, Context.MODE_PRIVATE);
        return preferences.getInt(LEVEL, DEFINITION_LEVEL_ST);
    }

    /**
     * 下载开机屏图片保存图片地址以及网络图片地址
     *
     * @throws Exception
     */
//    public static void setSplashLastUpdate(String url, String path)
//            throws Exception {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_SPLASH_UPDATE,
//                Context.MODE_PRIVATE);
//        Editor editor = preferences.edit();
//        editor.putString(KEY_SPLASH_URL, url);
//        editor.putString(KEY_SPLASH_PATH, path);
//        editor.commit();
//    }


    /**
     * 获取开机屏本地存储地址
     */
    public static String getSplashPath() {
        SharedPreferences preference = getContext().getSharedPreferences(PRE_SPLASH_UPDATE,
                Context.MODE_PRIVATE);
        return preference.getString(KEY_SPLASH_PATH, "");
    }



//    public static boolean getFirstDetected() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        return preferences.getBoolean(KEY_FIRST_DETECT, true);
//    }
//
//    public static void setFirstDetected() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        Editor editor = preferences.edit();
//        editor.putBoolean(KEY_FIRST_DETECT, false);
//        editor.commit();
//    }
//
//    public static boolean getFirstVrTip() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        return preferences.getBoolean(KEY_FIRST_VR, true);
//    }
//
//    public static void setFirstVrTip() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        Editor editor = preferences.edit();
//        editor.putBoolean(KEY_FIRST_VR, false);
//        editor.commit();
//    }
//
//    public static boolean getFirstCameraTip() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        return preferences.getBoolean(KEY_FIRST_CAMERA, true);
//    }
//
//    public static void setFirstCameraTip() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        Editor editor = preferences.edit();
//        editor.putBoolean(KEY_FIRST_CAMERA, false);
//        editor.commit();
//    }
//
//    public static void setFirstLoad() {
//        SharedPreferences preferences = getContext().getSharedPreferences(PRE_FIRST_LOAD,
//                Context.MODE_PRIVATE);
//        Editor editor = preferences.edit();
//        editor.putBoolean(KEY_FIRST_LOAD, false);
//        editor.commit();
//    }

    public static boolean getVisit(int page) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_PAGE_VISIT,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(String.valueOf(page), false);
    }

    public static void setVisit(int page) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_PAGE_VISIT,
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean(String.valueOf(page), true);
        editor.commit();
    }

    public static String getLastRedeem(String account_id, String code) {
        SharedPreferences preferences = getContext()
                .getSharedPreferences(PRE_LAST_REDEEM, Context.MODE_PRIVATE);
        return preferences.getString(account_id + "&&" + code, "");
    }

    public static void setLastRedeem(String lastRedeem, String key) {
        SharedPreferences preferences = getContext()
                .getSharedPreferences(PRE_LAST_REDEEM, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(key, lastRedeem);
        editor.commit();
    }

    public static void setLastRedeem(String lastRedeem, String account_id, String code) {
        SharedPreferences preferences = getContext()
                .getSharedPreferences(PRE_LAST_REDEEM, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(account_id + "&&" + code, lastRedeem);
        editor.commit();
    }

    public static Map<String, ?> getAllRedeem() {
        SharedPreferences preferences = getContext()
                .getSharedPreferences(PRE_LAST_REDEEM, Context.MODE_PRIVATE);
        return preferences.getAll();
    }


    /**
     * 存储支付模式
     *
     * @param payMethod
     */
    public static void setPayMethod(String payMethod) {
        SharedPreferences preferences = getContext().getSharedPreferences(
                PRE_PAY_METHOD, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString(KEY_PAY_METHOD, payMethod);
        editor.commit();
    }

    public static String getPayMethod() {
        SharedPreferences preference = getContext().getSharedPreferences(PRE_PAY_METHOD,
                Context.MODE_PRIVATE);
        return preference.getString(KEY_PAY_METHOD, "");
    }

    private static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }

}
