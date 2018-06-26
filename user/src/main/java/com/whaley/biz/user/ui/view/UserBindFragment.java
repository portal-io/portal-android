package com.whaley.biz.user.ui.view;

import android.content.Intent;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.presenter.RegisterUserPresenter;
import com.whaley.core.appcontext.Starter;


/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */
@Route(path = "/user/ui/userbind")
public class UserBindFragment extends RegisterFragment {

    public static void goPage(Starter starter, UserModel userModel) {
        Intent intent = TitleBarActivity.createIntent(starter, UserBindFragment.class);
        intent.putExtra(RegisterUserPresenter.KEY_LOGIN_USERBEAN, userModel);
        intent.putExtra(RegisterUserPresenter.KEY_THIRD_BIND, true);
        starter.startActivity(intent);
    }


    @Override
    public void changeUI() {
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_regist_bind));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
    }
}
