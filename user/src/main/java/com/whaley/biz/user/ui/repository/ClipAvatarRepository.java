package com.whaley.biz.user.ui.repository;

import android.graphics.Bitmap;
import android.net.Uri;

import com.whaley.biz.common.repository.MemoryRepository;

import java.io.File;

/**
 * Author: qxw
 * Date:2017/8/26
 * Introduction:
 */

public class ClipAvatarRepository extends MemoryRepository implements ClipAvatarService {

    Bitmap bitmap;
    File photoFile;
    Uri photoUri;
    boolean fromGallery = false;
    String saveBitmapPath;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public File getPhotoFile() {
        return photoFile;
    }

    @Override
    public void setPhotoFile(File photoFile) {
        this.photoFile = photoFile;
    }

    @Override
    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    @Override
    public boolean isFromGallery() {
        return fromGallery;
    }

    public void setFromGallery(boolean fromGallery) {
        this.fromGallery = fromGallery;
    }

    public String getSaveBitmapPath() {
        return saveBitmapPath;
    }

    public void setSaveBitmapPath(String saveBitmapPath) {
        this.saveBitmapPath = saveBitmapPath;
    }


}
