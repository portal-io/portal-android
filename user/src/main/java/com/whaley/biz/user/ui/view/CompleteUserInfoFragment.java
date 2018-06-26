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
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.model.UserModel;
import com.whaley.biz.user.ui.presenter.CompleteUserInfoPresenter;
import com.whaley.biz.user.ui.widgets.EditLayout;
import com.whaley.core.appcontext.Starter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */
@Route(path = "/user/ui/completeuserinfo")
public class CompleteUserInfoFragment extends BaseMVPFragment<CompleteUserInfoPresenter> implements CompleteUserInfoView {


    public static void goPage(Starter starter, UserModel userModel) {
        Intent intent = TitleBarActivity.createIntent(starter, CompleteUserInfoFragment.class);
        intent.putExtra(CompleteUserInfoPresenter.KEY_LOGIN_USERBEAN, userModel);
        starter.startActivity(intent);
    }

    @BindView(R2.id.et_nickname)
    EditLayout etNickname;
    @BindView(R2.id.et_password_regist)
    EditLayout etPasswordRegist;
    @BindView(R2.id.btn_register)
    TextView btnRegister;

    @OnClick(R2.id.btn_register)
    void onRegisterClick() {
        getPresenter().onClickRegistButton();
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setLeftIcon(0);
            getTitleBar().setTitleText(getString(R.string.user_regist_setting));
        }
        etNickname.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onNickNameChanged(s.toString());
            }
        });
        etPasswordRegist.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onPasswordChanged(s.toString());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_complete_user_info;
    }

    @Override
    public void enableRegisterButton() {
        btnRegister.setEnabled(true);
    }

    @Override
    public void diableRegisterButton() {
        btnRegister.setEnabled(false);
    }

}
