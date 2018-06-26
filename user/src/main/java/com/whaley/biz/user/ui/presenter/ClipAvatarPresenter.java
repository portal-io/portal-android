package com.whaley.biz.user.ui.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.whaley.biz.common.interactor.observer.UpdateUIObserver;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.user.interactor.ClipAvatar;
import com.whaley.biz.user.ui.repository.ClipAvatarRepository;
import com.whaley.biz.user.ui.view.ClipAvatarView;
import com.whaley.core.inject.annotation.Repository;
import com.whaley.core.inject.annotation.UseCase;
import com.whaley.core.utils.FileUtil;

import java.io.File;

import io.reactivex.annotations.NonNull;

/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public class ClipAvatarPresenter extends BasePagePresenter<ClipAvatarView> {

    public final static String KEY_PHOTO_FILE = "photo_file";
    public final static String KEY_BITMAP_ORI = "bitmap_ori";
    public final static String KEY_FROM_GALLERY = "from_gallery";

    public final static String CLIPTED_IMAGE_PATH = "/cliped_image.jpg";
    @Repository
    ClipAvatarRepository clipAvatarRepository;


    @UseCase
    ClipAvatar clipAvatar;

    public ClipAvatarPresenter(ClipAvatarView view) {
        super(view);
    }

    @Override
    public void onCreate(Bundle arguments, Bundle saveInstanceState) {
        super.onCreate(arguments, saveInstanceState);
        clipAvatarRepository.setPhotoUri((Uri) arguments.get(KEY_BITMAP_ORI));
        clipAvatarRepository.setPhotoFile((File) arguments.get(KEY_PHOTO_FILE));
        clipAvatarRepository.setFromGallery(arguments.getBoolean(KEY_FROM_GALLERY));
        if (!getUIView().getAttachActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).exists()) {
            getUIView().getAttachActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).mkdirs();
        }
        clipAvatarRepository.setSaveBitmapPath(getUIView().getAttachActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString() + CLIPTED_IMAGE_PATH);
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        clipAvatar.execute(new UpdateUIObserver<Bitmap>(getUIView(), true) {
            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                super.onNext(bitmap);
                getUIView().showBitmap(bitmap);
            }
        });
    }

    public void onCancelClick() {
        finish();
    }

    public void onConfirmClick() {
        FileUtil.saveBitmap(getUIView().getClipedBitmap(), clipAvatarRepository.getSaveBitmapPath());
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(UserInfoPresenter.KEY_RESULT_PATH, clipAvatarRepository.getSaveBitmapPath());
        intent.putExtras(bundle);
        getUIView().getAttachActivity().setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
