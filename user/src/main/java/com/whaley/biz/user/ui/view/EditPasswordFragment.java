package com.whaley.biz.user.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.EditPasswordPresenter;
import com.whaley.biz.user.ui.widgets.EditLayout;
import com.whaley.core.appcontext.Starter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: qxw
 * Date:2017/8/5
 * Introduction:
 */
@Route(path = "/user/ui/editPassword")
public class EditPasswordFragment extends BaseMVPFragment<EditPasswordPresenter> implements EditPasswordView {


    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, EditPasswordFragment.class);
    }

    @BindView(R2.id.et_ori_password)
    EditLayout etOriPassword;
    @BindView(R2.id.et_password1)
    EditLayout etPassword1;
    @BindView(R2.id.et_password2)
    EditLayout etPassword2;

    @OnClick(R2.id.btn_forget_pwd)
    void onForgetPwdClick(){
        getPresenter().onForgetPwdClick();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_edit_password;
    }


    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_title_edit_password));
            getTitleBar().setRightText(getString(R.string.user_text_save));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    getPresenter().onClickSaveButton();
                }
            });
        }

        etOriPassword.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onPasswordOriTextChanged(s.toString());
            }
        });

        etPassword1.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onPassword1Changed(s.toString());
            }
        });

        etPassword2.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onPassword2Changed(s.toString());
            }
        });
    }
}
