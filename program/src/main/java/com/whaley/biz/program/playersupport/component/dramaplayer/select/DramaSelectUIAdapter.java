package com.whaley.biz.program.playersupport.component.dramaplayer.select;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.model.NodeModel;
import com.whaley.core.image.ImageLoader;
import com.whaley.core.image.ImageRequest;
import com.whaley.core.utils.DisplayUtil;

import java.util.List;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;


/**
 * Created by dell on 2017/11/13.
 */

public class DramaSelectUIAdapter extends BaseUIAdapter<DramaSelectController> {
    ImageView ivMiddle;
    ImageView ivLeft;
    ImageView ivRight;
    final static int HALF_SCREEN_IMAGE_SIZE = DisplayUtil.convertDIP2PX(100);
    final static int FULL_SCREEN_IMAGE_SIZE = DisplayUtil.convertDIP2PX(150);
    final static int HALF_SCREEN_TWO_MARGIN_SIZE = DisplayUtil.convertDIP2PX(45);
    final static int HALF_SCREEN_THREE_MARGIN_SIZE = DisplayUtil.convertDIP2PX(48);
    final static int FULL_SCREEN_TWO_MARGIN_SIZE = DisplayUtil.convertDIP2PX(87);
    final static int FULL_SCREEN_THREE_MARGIN_SIZE = DisplayUtil.convertDIP2PX(70);
    final static int HALF_SCREEN_THREE_BOTTOM_SIZE = DisplayUtil.convertDIP2PX(170);
    final static int FULL_SCREEN_THREE_BOTTOM_SIZE = DisplayUtil.convertDIP2PX(56);

    ImageRequest.RequestManager requestManager;
    boolean isThreeSelected;
    public void show() {
        if (getRootView().getVisibility() != View.VISIBLE)
            getRootView().setVisibility(View.VISIBLE);
    }

    public void hide() {
        if (getRootView().getVisibility() != View.GONE)
            getRootView().setVisibility(View.GONE);
    }

    public void setData(List<NodeModel> nodeModelList) {
        if (nodeModelList == null) {
            return;
        }
        if (nodeModelList.size() == 2) {
            isThreeSelected = false;
            ivMiddle.setAlpha(0f);
            ivMiddle.setClickable(false);
            setImagePosition();
            loadImage(ivLeft, nodeModelList.get(0));
            loadImage(ivRight, nodeModelList.get(1));
        }
        if (nodeModelList.size() >= 3) {
            isThreeSelected = true;
            ivMiddle.setAlpha(1f);
            ivMiddle.setClickable(true);
            setImagePosition();
            loadImage(ivLeft, nodeModelList.get(0));
            loadImage(ivMiddle, nodeModelList.get(1));
            loadImage(ivRight, nodeModelList.get(2));
        }
    }

    private void setImagePosition() {
        int marginSize = getController().isFullScreen ?
                (isThreeSelected ? FULL_SCREEN_THREE_MARGIN_SIZE : FULL_SCREEN_TWO_MARGIN_SIZE) :
                (isThreeSelected ? HALF_SCREEN_THREE_MARGIN_SIZE : HALF_SCREEN_TWO_MARGIN_SIZE);
        ViewGroup.MarginLayoutParams leftlayoutParams = (ViewGroup.MarginLayoutParams) ivLeft.getLayoutParams();
        setImageSize(leftlayoutParams);
        leftlayoutParams.leftMargin = marginSize;
        ivLeft.requestLayout();
        ViewGroup.MarginLayoutParams rightlayoutParams = (ViewGroup.MarginLayoutParams) ivRight.getLayoutParams();
        setImageSize(rightlayoutParams);
        rightlayoutParams.rightMargin = marginSize;
        ivLeft.requestLayout();
        if (isThreeSelected) {
            ViewGroup.MarginLayoutParams middlelayoutParams = (ViewGroup.MarginLayoutParams) ivMiddle.getLayoutParams();
            setImageSize(middlelayoutParams);
            middlelayoutParams.bottomMargin = getController().isFullScreen ? FULL_SCREEN_THREE_BOTTOM_SIZE : HALF_SCREEN_THREE_BOTTOM_SIZE;
            ivMiddle.requestLayout();
        }
    }

    private void setImageSize(ViewGroup.MarginLayoutParams leftlayoutParams) {
        int imageSize = getController().isFullScreen ? FULL_SCREEN_IMAGE_SIZE : HALF_SCREEN_IMAGE_SIZE;
        leftlayoutParams.width = imageSize;
        leftlayoutParams.height = imageSize;
    }

    private void loadImage(ImageView imageView, NodeModel nodeModel) {
        imageView.setTag(imageView.getId(), nodeModel);
        requestManager.load(nodeModel.getSmallPic()).placeholder(0).setScaleType(ImageView.ScaleType.FIT_CENTER).into(imageView);
        reathAnimations(imageView);
    }

    @Override
    protected View initView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_drama_select, parent, false);
    }

    @Override
    protected void onViewCreated(View view) {
        requestManager = ImageLoader.with(getActivity());
        ivMiddle = (ImageView) view.findViewById(R.id.iv_middle);
        ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        ivMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivMiddle.getAlpha() == 1f) {
                    getController().onNodeSelected((NodeModel) ivMiddle.getTag(ivMiddle.getId()));
                }
            }
        });
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onNodeSelected((NodeModel) ivLeft.getTag(ivLeft.getId()));
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getController().onNodeSelected((NodeModel) ivRight.getTag(ivRight.getId()));
            }
        });
    }

    @Override
    public void destroy() {
        hide();
        if(ivLeft!=null){
            AdditiveAnimator.cancelAnimations(ivLeft);
        }
        if(ivRight!=null){
            AdditiveAnimator.cancelAnimations(ivRight);
        }
        if(ivMiddle!=null){
            AdditiveAnimator.cancelAnimations(ivMiddle);
        }
        requestManager = null;
    }


    public void switchScreen(boolean isFullScreen) {
        int imageSize = isFullScreen ? FULL_SCREEN_IMAGE_SIZE : HALF_SCREEN_IMAGE_SIZE;
        int marginSize = isFullScreen ? (isThreeSelected ? FULL_SCREEN_THREE_MARGIN_SIZE : FULL_SCREEN_TWO_MARGIN_SIZE) :
                (isThreeSelected ? HALF_SCREEN_THREE_MARGIN_SIZE : HALF_SCREEN_TWO_MARGIN_SIZE);
        showAnimations(ivLeft, imageSize).leftMargin(marginSize).start();
        showAnimations(ivRight, imageSize).rightMargin(marginSize).start();
        if (isThreeSelected)
            showAnimations(ivMiddle, imageSize).bottomMargin(isFullScreen ? FULL_SCREEN_THREE_BOTTOM_SIZE : HALF_SCREEN_THREE_BOTTOM_SIZE).start();
    }

    private AdditiveAnimator showAnimations(final View view, int imageSize) {
        AdditiveAnimator.cancelAnimations(view);
        return AdditiveAnimator.animate(view)
                .setStartDelay(500)
                .setDuration(500)
                .height(imageSize)
                .width(imageSize)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if (!wasCancelled)
                            reathAnimations(view);
                    }
                });
    }

    private void reathAnimations(final View view) {
        AdditiveAnimator.cancelAnimations(view);
        AdditiveAnimator.animate(view)
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .scale(1.04f)
                .then()
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .scale(1f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        if(!wasCancelled){
                            reathAnimations(view);
                        }
                    }
                })
                .start();

    }
}
