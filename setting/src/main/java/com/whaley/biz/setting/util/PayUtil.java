package com.whaley.biz.setting.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.whaley.core.router.Router;

/**
 * Created by dell on 2017/10/16.
 */

public class PayUtil {

    public final static String STR_PARAM_OBJECT = "str_param_object";

    public static Fragment showCurrencyPayDialog(FragmentManager fm, String json) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag("currencyPay");
        if (null != fragment) {
            ft.remove(fragment);
        }
        Fragment f = Router.getInstance().buildObj("/pay/ui/currencypaydialog").getObj();
        Bundle bundle = new Bundle();
        bundle.putString(STR_PARAM_OBJECT, json);
        f.setArguments(bundle);
        ft.add(f, "currencyPay");
        ft.commitAllowingStateLoss();
        return f;
    }

}
