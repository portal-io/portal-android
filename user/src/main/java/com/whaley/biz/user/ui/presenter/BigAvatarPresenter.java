package com.whaley.biz.user.ui.presenter;

import android.os.Bundle;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.common.ui.view.BasePageView;
import com.whaley.biz.user.ui.repository.BigAvatarRepoditoy;
import com.whaley.biz.user.ui.view.BigAvatarView;
import com.whaley.core.inject.annotation.Repository;

/**
 * Author: qxw
 * Date:2017/8/27
 * Introduction:
 */

public class BigAvatarPresenter extends BasePagePresenter<BigAvatarView> {
    public final static String KEY_AVATAR_URL = "avatar_url";
    @Repository
    BigAvatarRepoditoy bigAvatarRepoditoy;


    public BigAvatarPresenter(BigAvatarView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        bigAvatarRepoditoy.setAvatarUrl(arguments.getString(KEY_AVATAR_URL));
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        getUIView().showAvatar(bigAvatarRepoditoy.getAvatarUrl());
    }

}
