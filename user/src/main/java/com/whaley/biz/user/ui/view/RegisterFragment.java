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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.RegisterUserPresenter;
import com.whaley.biz.user.ui.widgets.EditLayout;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaleyvr.core.network.http.CookieManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/2
 * Introduction:
 */
@Route(path = "/user/ui/register")
public class RegisterFragment extends BaseMVPFragment<RegisterUserPresenter> implements RegisterView {

    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, RegisterFragment.class);
    }


    @BindView(R2.id.et_phone)
    EditLayout etPhone;
    @BindView(R2.id.et_sms_code)
    EditLayout etSMSCode;
    @BindView(R2.id.btn_fetch_code)
    Button btnFetchCode;
    @BindView(R2.id.layout_sms_code)
    RelativeLayout layoutSmsCode;
    @BindView(R2.id.et_validate_code)
    EditLayout etValidateCode;
    @BindView(R2.id.iv_valide_image)
    ImageView ivValideImage;
    @BindView(R2.id.layout_validate)
    RelativeLayout layoutValidate;
    @BindView(R2.id.layout_phone)
    LinearLayout layoutPhone;
    @BindView(R2.id.btn_next)
    TextView btnNext;
    @BindView(R2.id.tv_third_title)
    TextView tvThirdTitle;
    @BindView(R2.id.start_qq)
    ImageView startQq;
    @BindView(R2.id.start_weixin)
    ImageView startWeixin;
    @BindView(R2.id.start_weibo)
    ImageView startWeibo;
    @BindView(R2.id.layout_login_enter)
    LinearLayout layoutLoginEnter;
    @BindView(R2.id.tv_user_agreement)
    TextView tvUserAgreement;
    @BindView(R2.id.input_phone)
    RelativeLayout inputPhone;
    @BindView(R2.id.main_layout)
    RelativeLayout mainLayout;

    @OnClick(R2.id.iv_valide_image)
    void onValideImageClick() {
        loadValidateImage();
    }

    @OnClick(R2.id.btn_next)
    void onNextClick() {
        getPresenter().onNextClick();
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

    @OnClick(R2.id.btn_fetch_code)
    void onFetchCodeClick() {
        getPresenter().onFetchCodeClick();
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        changeUI();
        etPhone.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                getPresenter().onPhoneChanged(s.toString());
            }
        });
        etSMSCode.addTextChangedListener(new SimpleTextChangedListener() {

            @Override
            public void afterTextChanged(Editable s) {
                getPresenter().onSMSCodeChanged(s.toString());
            }
        });
        etValidateCode.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onImageCodeChanged(s.toString());
            }
        });

    }

    public void changeUI() {
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_regist_ori));
            getTitleBar().setRightText(getString(R.string.user_title_login));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    getPresenter().onClickLoginButton();
                }
            });
        }
        layoutLoginEnter.setVisibility(View.VISIBLE);
        tvUserAgreement.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_register;
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
    public void showUserNameError(String error) {

    }

    @Override
    public void showSmsCodeError(String error) {

    }

    @Override
    public void showValidateCodeError(String error) {

    }

    @Override
    public void clearError() {

    }

    @Override
    public void updateValidateCodeButton(long number) {
        btnFetchCode.setText(getString(R.string.user_btn_get_sms_code_waiting, number));
        btnFetchCode.setEnabled(false);
    }

    @Override
    public void resetSmsButton() {
        btnFetchCode.setText(getString(R.string.user_fetch_code));
        btnFetchCode.setEnabled(true);
    }

    @Override
    public void showValidateInput() {
        layoutValidate.setVisibility(View.VISIBLE);
        loadValidateImage();
    }

    private void loadValidateImage() {
        ImageLoader.with(this).load(getPresenter().getRegisterRepository().getImageCodeUrl()).cookie(CookieManager.getInstance().getCookieMap()).small().diskCacheStrategy(ImageRequest.DISK_NULL).skipMemoryCache(true).fitCenter().into(ivValideImage);
    }
}
