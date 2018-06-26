package com.whaley.biz.user.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.FindPasswordPresenter;
import com.whaley.biz.user.ui.widgets.EditLayout;
import com.whaley.core.appcontext.Starter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/7
 * Introduction:
 */

public class FindPasswordFragment extends BaseMVPFragment<FindPasswordPresenter> implements FindPasswordView {


    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, FindPasswordFragment.class);
    }

    @BindView(R2.id.et_user_name)
    EditLayout etUserName;
    @BindView(R2.id.et_validate_code)
    EditLayout etValidateCode;
    @BindView(R2.id.iv_valide_image)
    ImageView ivValideImage;
    @BindView(R2.id.layout_validate)
    RelativeLayout layoutValidate;
    @BindView(R2.id.et_sms_code)
    EditLayout etSmsCode;
    @BindView(R2.id.btn_fetch_code)
    Button btnFetchCode;
    @BindView(R2.id.layout_sms_code)
    RelativeLayout layoutSmsCode;
    @BindView(R2.id.layout_login)
    LinearLayout layoutLogin;
    @BindView(R2.id.btn_next)
    TextView btnNext;
    @BindView(R2.id.tv_connect_us)
    TextView tvConnectUs;
    @BindView(R2.id.layout_validate_phone)
    RelativeLayout layoutValidatePhone;
    @BindView(R2.id.et_password1)
    EditLayout etPassword1;
    @BindView(R2.id.et_password2)
    EditLayout etPassword2;
    @BindView(R2.id.btn_save)
    TextView btnSave;
    @BindView(R2.id.layout_input_new_pwd)
    LinearLayout layoutInputNewPwd;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;

    @OnClick(R2.id.btn_next)
    public void next() {
        getPresenter().onNextButtonClicked();
    }

    @OnClick(R2.id.btn_fetch_code)
    public void clickFetchCode() {
        getPresenter().onClickFetchCodeButton();
    }

    @OnClick(R2.id.tv_connect_us)
    void clickConnectUs() {
        getPresenter().onClickConnectUs();
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_find_pwd));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener(){
                @Override
                public void onRightClick(View view) {
                    getPresenter().onRightClick();
                }
            });
        }

        etUserName.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onPhoneChanged(s.toString());
            }
        });
        etSmsCode.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onSMSChanged(s.toString());
            }
        });
        etValidateCode.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onValidateChanged(s.toString());
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_pwd;
    }

    @Override
    public void enableSmsButton() {
        btnFetchCode.setEnabled(true);
    }

    @Override
    public void disableSmsButton() {
        btnFetchCode.setEnabled(false);
    }

    @Override
    public void enableNextButton() {
        btnNext.setEnabled(true);
    }

    @Override
    public void disableNextButton() {
        btnNext.setEnabled(false);
    }

    @Override
    public void updateValidateCodeButton(long number) {
        btnFetchCode.setText(getString(R.string.user_btn_get_sms_code_waiting, number));
        btnFetchCode.setEnabled(false);
    }

    @Override
    public void resetSmsButton() {
        btnFetchCode.setText(getString(R.string.user_fetch_code));
    }

    @Override
    public void showNext() {
        getTitleBar().setTitleText(getString(R.string.user_again_pwd));
        getTitleBar().setRightText(getString(R.string.user_text_save));
        layoutInputNewPwd.setVisibility(View.VISIBLE);
        layoutValidatePhone.setVisibility(View.GONE);
    }
}
