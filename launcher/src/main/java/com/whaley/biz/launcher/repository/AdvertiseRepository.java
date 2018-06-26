package com.whaley.biz.launcher.repository;

import android.graphics.Bitmap;

import com.whaley.biz.common.repository.MemoryRepository;

/**
 * Created by dell on 2017/9/5.
 */

public class AdvertiseRepository extends MemoryRepository {

    private String path;

    private Bitmap bitmap;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
