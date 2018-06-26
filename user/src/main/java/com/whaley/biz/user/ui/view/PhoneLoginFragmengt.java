package com.whaley.biz.user.ui.view;

import android.content.Intent;

import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.core.appcontext.Starter;

/**
 * Author: qxw
 * Date:2017/8/8
 * Introduction:
 */

public class PhoneLoginFragmengt extends RegisterFragment {
    public static void goPage(Starter starter) {
        Intent intent = TitleBarActivity.createIntent(starter, PhoneLoginFragmengt.class);
        starter.startActivity(intent);
    }

    @Override
    public void changeUI() {
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_safe_login));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
    }
}
