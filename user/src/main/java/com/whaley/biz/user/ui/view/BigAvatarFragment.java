package com.whaley.biz.user.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.widget.SimpleTitleBarListener;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.ui.presenter.BigAvatarPresenter;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.Animator;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import butterknife.BindView;


/**
 * Author: qxw
 * Date:2017/8/27
 * Introduction:
 */

public class BigAvatarFragment extends BaseMVPFragment<BigAvatarPresenter> implements BigAvatarView {

    @BindView(R2.id.iv_avatar)
    ImageView ivAvatar;

    public static void goPage(Starter starter, String imageUrl) {
        Intent intent = TitleBarActivity.createIntent(starter, BigAvatarFragment.class);
        intent.putExtra(BigAvatarPresenter.KEY_AVATAR_URL, imageUrl);
        starter.startActivity(intent);
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getTitleBar() != null) {
            getTitleBar().setTitleText(getString(R.string.text_big_avatar));
            getTitleBar().setTitleBarListener(new SimpleTitleBarListener());
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ivAvatar.getLayoutParams();
        if (lp == null) {
            lp = new FrameLayout.LayoutParams(DisplayUtil.screenW, DisplayUtil.screenW);
            lp.gravity = Gravity.CENTER;
        } else {
            lp.width = DisplayUtil.screenW;
            lp.height = DisplayUtil.screenW;
        }
        ivAvatar.setLayoutParams(lp);
    }

    @Override
    public void showAvatar(String imageUrl) {
        ImageLoader.with(this).load(imageUrl)
                .notInitBefore()
                .skipMemoryCache(true)
                .placeholder(R.mipmap.icon_manage_avatar).error(R.mipmap.icon_manage_avatar)
                .animate(new Animator() {
                    @Override
                    public void animate(View view) {
                        //nothing to do
                    }
                })
                .diskCacheStrategy(ImageRequest.DISK_NULL).centerCrop().into(ivAvatar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_big_avatar;
    }

}
