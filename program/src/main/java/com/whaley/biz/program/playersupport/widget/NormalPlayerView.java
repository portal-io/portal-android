package com.whaley.biz.program.playersupport.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.whaley.biz.playerui.PlayerView;
import com.whaley.biz.playerui.component.common.backpress.BackPressController;
import com.whaley.biz.playerui.component.common.halfswitchscreen.HalfSwitchScreenComponent;

/**
 * Created by YangZhi on 2017/8/22 11:32.
 */

public class NormalPlayerView extends PlayerView {

    protected BackPressController.SwitchScreenHandle switchScreenHandle;

    public NormalPlayerView(Context context) {
        this(context, null);
    }

    public NormalPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    protected void init(Context context) {
        getComponentManager().registAll(NormalComponentsUtil.getNormalComponent(new BackPressController.SwitchScreenHandle() {
            @Override
            public boolean shouldSwitchOnBack() {
                if (switchScreenHandle != null)
                    return switchScreenHandle.shouldSwitchOnBack();
                return false;
            }
        }));
    }

    public void registHalfSwitchComponent() {
        // 半屏和全屏切换的组件
        HalfSwitchScreenComponent halfSwitchScreenComponent = new HalfSwitchScreenComponent();
        regist(halfSwitchScreenComponent);
        switchScreenHandle = (BackPressController.SwitchScreenHandle) halfSwitchScreenComponent.getController();
    }

}
