package com.whaley.biz.program.playersupport.component.liveplayer.initblurbackground;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.image.Transformation;
import com.whaley.core.utils.FastBlur;

/**
 * Created by YangZhi on 2017/8/8 19:16.
 */

public class InitBlurBackgroundUIAdapter extends InitBackgroundUIAdapter<InitBlurBackgroundController> {

    ImageView ivBlur;

    View inflatedView;

    ViewStub viewStub;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_live_player_init_bg;
    }

    @Override
    protected void checkInflatedView() {
        if (inflatedView == null) {
            inflatedView = viewStub.inflate();
            ivBlur = (ImageView) inflatedView.findViewById(R.id.iv_blur);
            ivBlur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getController().onClick();
                }
            });
        }
    }

    @Override
    public View getRootView() {
        return inflatedView == null ? super.getRootView() : inflatedView;
    }

    @Override
    protected void onViewCreated(View view) {
        viewStub = (ViewStub) view;
    }

    public void loadBlurImage(String url) {
        if (ivBlur == null)
            return;
        ImageLoader.with(getActivity())
                .load(url)
                .maxSize(100)
                .transform(new BlurTransformation(url))
                .placeholder(0)
                .error(0)
                .diskCacheStrategy(ImageRequest.DISK_SOURCE)
                .centerCrop()
                .into(ivBlur);
    }

    @Override
    protected void show() {
        checkInflatedView();
        inflatedView.setVisibility(View.VISIBLE);
        loadBlurImage(getController().getImageUrl());
    }

    @Override
    protected void hide() {
        if (inflatedView == null)
            return;
        inflatedView.setVisibility(View.GONE);
        ivBlur.setImageResource(0);
//        ImageLoader.clearView(ivBlur);
    }

    @Override
    public void destroy() {
        super.destroy();
//        if (ivBlur != null) {
//            ivBlur.setImageResource(0);
//            ImageLoader.clearView(ivBlur);
//        }
    }

    @Override
    protected long getRootAnimDuration() {
        return 500;
    }

    static class BlurTransformation implements Transformation {

        final String url;

        public BlurTransformation(String url) {
            this.url = url;
        }

        @Override
        public Bitmap transform(Bitmap resource, int outHeigt, int outWidth) {
            if (resource.isRecycled())
                return resource;
            Bitmap bitmap = resource;
            Bitmap blurBitmap = FastBlur.doBlur(bitmap, 8, false);
            if (blurBitmap != null) {
                return blurBitmap;
            }
            return resource;
        }

        @Override
        public String getKey() {
            return url + "_Blur_size_+" + 100 + "_radio_" + 8;
        }
    }
}
