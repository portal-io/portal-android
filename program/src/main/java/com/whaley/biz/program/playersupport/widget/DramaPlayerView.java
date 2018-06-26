package com.whaley.biz.program.playersupport.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.whaley.biz.playerui.component.common.backpress.BackPressController;

/**
 * Created by dell on 2017/11/13.
 */

public class DramaPlayerView extends NormalPlayerView {

    public DramaPlayerView(Context context) {
        super(context, null);
    }

    public DramaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DramaPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        getComponentManager().registAll(DramaComponentsUtil.getDramaComponent(new BackPressController.SwitchScreenHandle() {
            @Override
            public boolean shouldSwitchOnBack() {
                if (switchScreenHandle != null)
                    return switchScreenHandle.shouldSwitchOnBack();
                return false;
            }
        }));
    }

}
