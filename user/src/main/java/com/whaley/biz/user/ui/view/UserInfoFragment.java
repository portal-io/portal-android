package com.whaley.biz.user.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SettingViewLayout;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.UserManager;
import com.whaley.biz.user.ui.presenter.UserInfoPresenter;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: qxw
 * Date:2017/8/3
 * Introduction:
 */
@Route(path = "/user/ui/userInfo")
public class UserInfoFragment extends BaseMVPFragment<UserInfoPresenter> implements UserInfoView {

    public static void goPage(Starter starter) {
        TitleBarActivity.goPage(starter, UserInfoFragment.class);
    }

    @BindView(R2.id.sv_avatar)
    SettingViewLayout svAvatar;
    @BindView(R2.id.sv_name)
    SettingViewLayout svName;
    @BindView(R2.id.sv_phone)
    SettingViewLayout svPhone;
    @BindView(R2.id.start_qq)
    ImageView startQq;
    @BindView(R2.id.start_weixin)
    ImageView startWeixin;
    @BindView(R2.id.start_weibo)
    ImageView startWeibo;
    @BindView(R2.id.tv_third_title)
    TextView thirdTitle;
    @BindView(R2.id.btn_logout)
    TextView btnLogout;

    @OnClick(R2.id.sv_avatar)
    void onAvatarClick() {
        getPresenter().onAvatarClick();
    }

    @OnClick(R2.id.sv_name)
    void onNameClick() {
        getPresenter().onNameClick();
    }

    @OnClick(R2.id.sv_password)
    void onPasswordClick() {
        getPresenter().onPasswordClick();
    }

    @OnClick(R2.id.sv_phone)
    void onPhoneClick() {
        getPresenter().onPhoneClick();
    }


    @OnClick(R2.id.start_qq)
    void onQQClick() {
        getPresenter().onQQClick();
    }

    @OnClick(R2.id.start_weibo)
    void onWBClick() {
        getPresenter().onWBClick();
    }

    @OnClick(R2.id.start_weixin)
    void onWXClick() {
        getPresenter().onWXClick();
    }

    @OnClick(R2.id.btn_logout)
    void onLogoutClick() {
        getPresenter().onLogoutClick();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
            getTitleBar().setTitleText(getString(R.string.text_title_user_info));
        }
        thirdTitle.setText(R.string.text_title_third_bind);
        svAvatar.getIvRightPic().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().onClickAvatarImage();
            }
        });
        if (UserManager.getInstance().isLogin()) {
            btnLogout.setVisibility(View.VISIBLE);
        }else{
            btnLogout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAvatar(String imageUrl) {
        ImageLoader.with(this).load(imageUrl)
                .notInitBefore()
                .circle()
                .skipMemoryCache(false)
                .size(160, 160)
                .placeholder(R.mipmap.icon_manage_avatar).error(R.mipmap.icon_manage_avatar)
                .diskCacheStrategy(ImageRequest.DISK_RESULT).centerCrop().into(svAvatar.getIvRightPic());
    }

    @Override
    public void showNickname(String nickName) {
        svName.setText(nickName);
    }

    @Override
    public void showPhone(String phone) {
        svPhone.setText(phone);
    }

    @Override
    public void setBingQQ(boolean selected) {
        startQq.setSelected(selected);
    }

    @Override
    public void setBingWeixin(boolean selected) {
        startWeixin.setSelected(selected);
    }

    @Override
    public void setBingWeibo(boolean selected) {
        startWeibo.setSelected(selected);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
