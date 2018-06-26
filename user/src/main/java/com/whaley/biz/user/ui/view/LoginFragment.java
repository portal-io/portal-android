package com.whaley.biz.user.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.LoginPresenter;
import com.whaley.biz.user.ui.widgets.EditLayout;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.utils.KeyboardUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date: 2017/7/17
 */
@Route(path = "/user/ui/login")
public class LoginFragment extends BaseMVPFragment<LoginPresenter> implements LoginView {

    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, LoginFragment.class);
    }

    public static void goPage(Starter starter, String path, int requestCode) {
        Intent intent = TitleBarActivity.createIntent(starter, LoginFragment.class);
        intent.putExtra(LoginPresenter.STR_PATH, path);
        TitleBarActivity.goPage(starter, requestCode, intent);
    }

    @BindView(R2.id.et_user_name)
    EditLayout etUserName;
    @BindView(R2.id.et_password)
    EditLayout etPassword;
    @BindView(R2.id.btn_login)
    TextView btnLogin;


    @OnClick(R2.id.btn_forget_pwd)
    void onForgetPwdClick() {
        getPresenter().onForgetPwdClick();
    }

    @OnClick(R2.id.btn_safe_login)
    void onSafeLonginClick() {
        getPresenter().onSafeLonginClick();
    }

    @OnClick(R2.id.btn_login)
    void onLoginOnClick() {
        KeyboardUtil.hideKeyBoard(etPassword.getEditTextView());
        getPresenter().onLoginOnClick();
    }

    @OnClick(R2.id.start_qq)
    void onThirdQQClick() {
        getPresenter().onThirdQQClick();
    }

    @OnClick(R2.id.start_weixin)
    void onThirdWXClick() {
        getPresenter().onThirdWXClick();
    }

    @OnClick(R2.id.start_weibo)
    void onThirdWBClick() {
        getPresenter().onThirdWBClick();
    }


    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_title_login));
            getTitleBar().setRightText(getString(R.string.user_title_register));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    getPresenter().onRegisterClick();
                }
            });
        }
        etUserName.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                getPresenter().onUserNameChanged(s.toString());
            }
        });
        etPassword.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onPasswordChanged(s.toString());
            }
        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_login;
    }

    @Override
    public void enableLoginButton() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void disableLoginButton() {
        btnLogin.setEnabled(false);
    }
}
