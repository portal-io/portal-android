package com.whaley.biz.user.ui.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Button;

import com.whaley.biz.common.ui.BaseMVPFragment;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.user.R;
import com.whaley.biz.user.R2;
import com.whaley.biz.user.interactor.ClipAvatar;
import com.whaley.biz.user.ui.presenter.ClipAvatarPresenter;
import com.whaley.biz.user.ui.widgets.CircleImageView;
import com.whaley.biz.user.ui.widgets.ClipImageLayout;
import com.whaley.core.appcontext.Starter;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public class ClipAvatarFragment extends BaseMVPFragment<ClipAvatarPresenter> implements ClipAvatarView {


    @BindView(R2.id.id_clipImageLayout)
    ClipImageLayout idClipImageLayout;


    @OnClick(R2.id.btn_cancel)
    void onCancelClick() {
        getPresenter().onCancelClick();

    }

    @OnClick(R2.id.btn_confirm)
    void onConfirmClick() {
        getPresenter().onConfirmClick();
    }

    public static void toClipAvatar(Starter starter, File photoFile, Uri photoUri, boolean fromGallery, int requestCode) {
        Intent intent = CommonActivity.createIntent(starter, ClipAvatarFragment.class.getName(), null);
        intent.putExtra(ClipAvatarPresenter.KEY_PHOTO_FILE, photoFile);
        intent.putExtra(ClipAvatarPresenter.KEY_BITMAP_ORI, photoUri);
        intent.putExtra(ClipAvatarPresenter.KEY_FROM_GALLERY, fromGallery);
        starter.startActivityForResult(intent, requestCode);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_clip_avatar;
    }

    @Override
    public void showBitmap(Bitmap bitmap) {
        idClipImageLayout.setImageBitmap(bitmap);
    }

    @Override
    public Bitmap getClipedBitmap() {
        return idClipImageLayout.clip();
    }

}
