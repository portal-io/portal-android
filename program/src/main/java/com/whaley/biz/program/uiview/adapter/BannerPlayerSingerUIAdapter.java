package com.whaley.biz.program.uiview.adapter;

import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.whaley.biz.program.R;
import com.whaley.biz.program.R2;
import com.whaley.biz.program.model.RecommendModel;
import com.whaley.biz.program.playersupport.component.bannerplayer.BannerTargetController;
import com.whaley.biz.program.uiview.BannerPlayerProviderSetter;
import com.whaley.biz.program.uiview.ImageloaderUser;
import com.whaley.biz.program.uiview.ClickableSimpleViewHolder;
import com.whaley.biz.program.uiview.VisibleSwitcher;
import com.whaley.biz.program.uiview.viewmodel.BannerPlayerSingerUIViewModel;
import com.whaley.biz.program.widget.TransImageView;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.widget.uiview.OnUIViewClickListener;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: qxw
 * Date: 2017/3/20
 */

public class BannerPlayerSingerUIAdapter extends BaseUIAdapter<BannerPlayerSingerUIAdapter.BannerPlayerSingerViewHolder, BannerPlayerSingerUIViewModel> implements ImageloaderUser, VisibleSwitcher{

    static final int WIDTH_PLAYER = DisplayUtil.screenW - 2 * DisplayUtil.convertDIP2PX(10);

    static final int HEIGHT_PLAYER = (int) (1f * WIDTH_PLAYER / 16 * 9);

    ImageRequest.RequestManager requestManager;


    @Override
    public void setRequestManager(ImageRequest.RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    @Override
    public void setOnUIViewClickListener(OnUIViewClickListener onUIViewClickListener) {
        super.setOnUIViewClickListener(onUIViewClickListener);
    }


    @Override
    public void onAttach() {
        super.onAttach();
        getViewHolder().bannerTargetController.attach();
    }

    @Override
    public void onDettatch() {
        super.onDettatch();
        getViewHolder().bannerTargetController.dettach();
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        cancelImageLoader(getViewHolder().ivBg);
        cancelImageLoader(getViewHolder().ivImg);
        setRequestManager(null);
    }

    @Override
    public BannerPlayerSingerViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new BannerPlayerSingerViewHolder(getLayoutInfalterFormParent(parent).inflate(R.layout.item_banner_player_singer, parent, false));

    }


    @Override
    protected void setViewClick() {
        getViewHolder().ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnUIViewClickListener() != null)
                    getOnUIViewClickListener().onViewClick(getViewHolder());
            }
        });
    }


    @Override
    public void changeVisible(boolean isVisible) {
        Log.d("BannerPlayerContainer", "changeVisible singleplayer adapter isvisible = " + isVisible);
        if (isVisible) {
            getViewHolder().bannerTargetController.changeVisible(true);
        } else {
            getViewHolder().bannerTargetController.changeVisible(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getViewHolder().bannerTargetController.destory();
    }

    @Override
    public boolean onInitViewHolder(BannerPlayerSingerViewHolder UIViewHolder, BannerPlayerSingerUIViewModel uiViewModel, int position) {
        ViewGroup.LayoutParams layoutParams = UIViewHolder.ivImg.getLayoutParams();
        layoutParams.height = HEIGHT_PLAYER;
        layoutParams = UIViewHolder.viewTarget.getLayoutParams();
        layoutParams.height = HEIGHT_PLAYER;
        UIViewHolder.getRootView().requestLayout();
        return true;
    }

    @Override
    public void onBindViewHolder(BannerPlayerSingerViewHolder UIViewHolder, final BannerPlayerSingerUIViewModel uiViewModel, int position) {
//        ClickableUIViewHolder.ivBg.setTrans(new RectF(LEFT,TOP,LEFT+WIDTH_BANNER_IMG,TOP+HEIGHT_BANNER_IMG));
        UIViewHolder.ivBg.setTrans(UIViewHolder.viewTarget);
//        getViewHolder().viewTouch.setClickable(true);
        AdditiveAnimator.cancelAnimations(getViewHolder().ivImg);
        getViewHolder().ivImg.setAlpha(1f);
        getViewHolder().ivImg.setVisibility(View.VISIBLE);
        requestManager.load(uiViewModel.getBgUrl()).centerCrop().medium().into(UIViewHolder.ivBg);
        requestManager.load(uiViewModel.getImgUrl()).centerCrop().big().into(UIViewHolder.ivImg);
        getViewHolder().bannerTargetController.setPlayData(((RecommendModel) uiViewModel.getSeverModel()).getPlayData());

    }

    public static class BannerPlayerSingerViewHolder extends ClickableSimpleViewHolder {
        @BindView(R2.id.iv_bg)
        public TransImageView ivBg;
        @BindView(R2.id.iv_img)
        public ImageView ivImg;
        @BindView(R2.id.view_target)
        public View viewTarget;

        public BannerTargetController bannerTargetController;

        BannerPlayerSingerViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            bannerTargetController = new BannerTargetController(viewTarget,WIDTH_PLAYER,HEIGHT_PLAYER, new BannerTargetController.CoverListener() {
                @Override
                public void showCover(boolean isAnim) {
                    if(isAnim) {
                        AdditiveAnimator.cancelAnimations(ivImg);
                        AdditiveAnimator.animate(ivImg).alpha(1f)
                                .addStartAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        ivImg.setVisibility(View.VISIBLE);
                                    }
                                })
                                .start();
                    }else {
                        ivImg.setAlpha(1f);
                        ivImg.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void hideCover() {
                    AdditiveAnimator.cancelAnimations(ivImg);
                    AdditiveAnimator.animate(ivImg).alpha(0f)
                            .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                                @Override
                                public void onAnimationEnd(boolean wasCancelled) {
                                    ivImg.setVisibility(View.GONE);
                                }
                            })
                            .start();
                }
            });
        }
    }

}
