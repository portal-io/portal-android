package com.whaley.biz.setting.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.whaley.core.appcontext.AppContextProvider;

/**
 * Author: qxw
 * Date:2017/9/15
 * Introduction:
 */

public class SharedPreferenceSettingUtil {
    private static final String PRE_TEST_MODEL_SWITCH = "pre_test_model_switch";

    public static void setTestModel(boolean status) {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_TEST_MODEL_SWITCH,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PRE_TEST_MODEL_SWITCH, status);
        editor.commit();
    }

    public static boolean getTestModel() {
        SharedPreferences preferences = getContext().getSharedPreferences(PRE_TEST_MODEL_SWITCH,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PRE_TEST_MODEL_SWITCH, false);
    }


    private static Context getContext() {
        return AppContextProvider.getInstance().getContext();
    }
}
