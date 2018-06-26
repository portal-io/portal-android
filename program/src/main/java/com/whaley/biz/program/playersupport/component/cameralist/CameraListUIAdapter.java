package com.whaley.biz.program.playersupport.component.cameralist;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.biz.program.R;
import com.whaley.biz.program.playersupport.model.CameraModel;
import com.whaley.biz.program.playersupport.widget.camerasource.SourceLayout;
import com.whaley.core.utils.DisplayUtil;

import java.util.Map;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;

/**
 * Created by YangZhi on 2017/8/21 15:53.
 */

public class CameraListUIAdapter extends ControlUIAdapter<CameraListController> {

    ViewStub viewStub;

    SourceLayout sourceLayout;

    boolean isSourceLayoutOnShow;

    private int rightMargin;

    private int bottomMargin;

    public CameraListUIAdapter(int rightMargin, int bottomMargin) {
        this.rightMargin = rightMargin;
        this.bottomMargin = bottomMargin;
    }

    @Override
    public void show(boolean anim) {
        if (sourceLayout == null) {
            return;
        }
        if(!isSourceLayoutOnShow){
            return;
        }
        AdditiveAnimator.animate(sourceLayout)
                .alpha(1f)
                .addStartAction(new Runnable() {
                    @Override
                    public void run() {
                        sourceLayout.setVisibility(View.VISIBLE);
                        getController().showCamera();
                    }
                })
                .start();
    }

    @Override
    public void hide(boolean anim) {
        if (sourceLayout == null)
            return;
        AdditiveAnimator.animate(sourceLayout)
                .alpha(0f)
                .addEndAction(new AdditiveAnimator.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(boolean wasCancelled) {
                        sourceLayout.setVisibility(View.GONE);
                        getController().hideCamera();
                    }
                })
                .start();
    }


    public void toggleCameraList(){
        if(isSourceLayoutOnShow){
            hideCameraList();
        }else {
            showCameraList();
        }
    }


    public void showCameraList() {
        isSourceLayoutOnShow = true;
        if (sourceLayout == null) {
            return;
        }
        show(true);
    }

    public void hideCameraList(){
        isSourceLayoutOnShow = false;
        if (sourceLayout == null) {
            return;
        }
        hide(true);
    }

    public void setCameraList(Map<String, CameraModel> cameraListData) {
        if (sourceLayout == null) {
            sourceLayout = (SourceLayout) viewStub.inflate();
            sourceLayout.setVisibility(View.GONE);
            sourceLayout.setListener(new SourceLayout.OnSelectListener() {
                @Override
                public void onSelected(String source) {
                    getController().onCameraSelected(source);
                }
            });
        }
        sourceLayout.setData(cameraListData);
    }

    public void setCameraSource(String source){
        if (sourceLayout != null) {
            sourceLayout.select(source);
        }
    }

    @Override
    public View getRootView() {
        return sourceLayout == null ? super.getRootView() : sourceLayout;
    }

    @Override
    protected View initView(ViewGroup parent) {
        viewStub = new ViewStub(parent.getContext(), R.layout.layout_player_camera_source);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(DisplayUtil.convertDIP2PX(130), ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        layoutParams.bottomMargin = bottomMargin;
        layoutParams.rightMargin = rightMargin;
        viewStub.setLayoutParams(layoutParams);
        return viewStub;
    }

    @Override
    protected void onViewCreated(View view) {

    }

    @Override
    public void destroy() {
        super.destroy();
        if(sourceLayout!=null) {
            AdditiveAnimator.cancelAnimations(sourceLayout);
        }
    }
}
