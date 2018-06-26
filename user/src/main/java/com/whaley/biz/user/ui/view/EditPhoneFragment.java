package com.whaley.biz.user.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.widget.SimpleTextChangedListener;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.EditPhonePresenter;
import com.whaley.biz.user.ui.widgets.EditLayout;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/4
 * Introduction:
 */
@Route(path = "/user/ui/editPhone")
public class EditPhoneFragment extends BaseMVPFragment<EditPhonePresenter> implements EditPhoneView {

    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, EditPhoneFragment.class);
    }

    @BindView(R2.id.tv_ori_phone)
    TextView tvOriPhone;
    @BindView(R2.id.et_new_phone)
    EditLayout etNewPhone;
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


    @OnClick(R2.id.btn_fetch_code)
    void onFetchCodeClick() {
        getPresenter().onClickSmsButton();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_edit_phone;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.user_title_edit_phone));
            getTitleBar().setRightText(getString(R.string.user_text_save));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener() {
                @Override
                public void onRightClick(View view) {
                    getPresenter().onClickSaveButton();
                }
            });

        }
        etSmsCode.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                getPresenter().onSmsCodeChanged(s.toString());
            }
        });
        etNewPhone.addTextChangedListener(new SimpleTextChangedListener() {
            @Override
            public void afterTextChanged(Editable s) {
                getPresenter().onNewPhoneChanged(s.toString());
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


    @Override
    public void showPhone(String phone) {
        tvOriPhone.setText(phone);
    }

    @Override
    public void setSmsButtonEnable(boolean enable) {
        btnFetchCode.setEnabled(enable);
    }

    @Override
    public void updateValidateCodeButton(long time) {
        btnFetchCode.setText(getString(R.string.user_btn_get_sms_code_waiting, time));
        btnFetchCode.setEnabled(false);
    }

    @Override
    public void resetSmsButton() {
        btnFetchCode.setText(getString(R.string.user_fetch_code));
    }

    @Override
    public void showValidateInput() {
        layoutValidate.setVisibility(View.VISIBLE);
        loadValidateImage();
    }

    @Override
    public void showReLoginDialog() {
        DialogUtil.showDialog(getContext(), getResources().getString(R.string.user_edit_phone_success), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().updatePhone();
            }
        });
    }

    private void loadValidateImage() {
        ImageLoader.with(this).load(getPresenter().getEditPhoneRepository().getImageCodeUrl()).small().diskCacheStrategy(ImageRequest.DISK_NULL).skipMemoryCache(true).fitCenter().into(ivValideImage);
    }
}
