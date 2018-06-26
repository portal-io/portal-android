package com.whaley.biz.user.ui.repository;

import android.net.Uri;

import java.io.File;

/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public interface ClipAvatarService {
    void setPhotoFile(File photoFile);

    boolean isFromGallery();

    Uri getPhotoUri();

    File getPhotoFile();
}
