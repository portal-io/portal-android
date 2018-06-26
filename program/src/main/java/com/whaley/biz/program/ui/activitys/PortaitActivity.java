package com.whaley.biz.program.ui.activitys;

import android.content.Intent;
import android.os.Bundle;

import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.core.appcontext.Starter;

/**
 * Created by YangZhi on 2017/9/6 20:19.
 */

public class PortaitActivity extends PlayerActivity {

    public static Intent createIntent(Starter starter, String fragmentClazzName) {
        Intent intent = new Intent(starter.getAttatchContext(), PortaitActivity.class);
        intent.putExtra(STR_FRAGMENT_CLASS_NAME, fragmentClazzName);
        return intent;
    }
}
