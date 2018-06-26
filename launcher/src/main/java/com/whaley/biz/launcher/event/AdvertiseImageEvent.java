package com.whaley.biz.launcher.event;

import android.graphics.Bitmap;

/**
 * Created by dell on 2017/9/14.
 */

public class AdvertiseImageEvent {

    private Bitmap bitmap;

    public AdvertiseImageEvent(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
