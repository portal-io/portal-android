package com.whaley.biz.launcher.activitys;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.snailvr.manager.R;
import com.whaley.biz.common.ui.BaseMVPActivity;
import com.whaley.biz.launcher.presenter.AdvertisePresenter;
import com.whaley.biz.launcher.view.AdvertiseView;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageLoaderCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dell on 2017/9/5.
 */

public class AdvertiseActivity extends BaseMVPActivity<AdvertisePresenter> implements AdvertiseView {

    public static void launch(Starter starter, String path) {
        Intent intent = new Intent();
        intent.setClass(starter.getAttatchContext(), AdvertiseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        intent.putExtras(bundle);
        starter.startActivity(intent);
    }

    @BindView(R.id.startup)
    ImageView startup;
    @BindView(R.id.num_text)
    TextView numText;

    ValueAnimator valueAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @OnClick(R.id.pass_layout)
    void onClick() {
        getPresenter().saveBI();
        getPresenter().startActivity();
    }

    @OnClick(R.id.startup)
    void startup() {
        getPresenter().onStartup();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher_advertise;
    }

    @Override
    protected void setViews(Bundle savedInstanceState) {
        super.setViews(savedInstanceState);
        Bitmap bitmap = getPresenter().getRepository().getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            startup.setImageBitmap(bitmap);
        } else {
            ImageLoader.with(this).load(getPresenter().getRepository().getPath())
                    .into(startup, null, new ImageLoaderCallback() {
                        @Override
                        public void onSuccess(String s, Bitmap bitmap, File file) {

                        }

                        @Override
                        public void onFailue(Throwable throwable) {

                        }

                        @Override
                        public void onProgressChanged(double v, double v1) {

                        }
                    });
        }
        updateTimer();
    }

    @Override
    public void updateTimer() {
        if (valueAnimator != null)
            valueAnimator.cancel();
        valueAnimator = ValueAnimator.ofFloat(0, 3f);
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                int countDownTime = 3 - (int) value;
                numText.setText(countDownTime + "秒");
                if (value == 3f) {
                    getPresenter().startActivity();
                }
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueAnimator != null)
            valueAnimator.cancel();
    }

}
