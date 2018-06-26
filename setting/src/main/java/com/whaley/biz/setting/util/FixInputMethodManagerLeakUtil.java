package com.whaley.biz.setting.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 *
 * 解决系统级 InputMethodManager 内存泄露工具
 *
 * Created by yangzhi on 2017/9/19.
 */

public class FixInputMethodManagerLeakUtil {


    public static void fixInputMethodManagerLeak(Context destContext) {

//        if (SDK_INT < KITKAT || SDK_INT > 22) {
//            return;
//        }

        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object obj_get = null;
        for (int i = 0; i < arr.length; i++) {
            String param = arr[i];
            try {
                f = imm.getClass().getDeclaredField(param);
                if (f == null) {
                    return;
                }
                f.setAccessible(true);
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    }
                }
            } catch (Throwable t) {
//                t.printStackTrace();
            }
        }
    }

    public static void fixTextLineCacheLeak() {

//        if (SDK_INT > LOLLIPOP) {
//            return;
//        }
        try {
            Field textLineCached;

            textLineCached = Class.forName("android.text.TextLine").getDeclaredField("sCached");
            if (textLineCached == null) {
                return;
            }
            textLineCached.setAccessible(true);

            // Get reference to the TextLine sCached array.
            Object cached = textLineCached.get(null);
            if (cached != null) {
                // Clear the array.
                for (int i = 0, size = Array.getLength(cached); i < size; i++) {
                    Array.set(cached, i, null);
                }
            }
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }
}
