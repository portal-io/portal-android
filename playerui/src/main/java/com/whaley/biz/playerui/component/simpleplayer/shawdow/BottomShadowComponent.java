package com.whaley.biz.playerui.component.simpleplayer.shawdow;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.R;
import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.component.common.control.ControlUIAdapter;
import com.whaley.core.utils.DisplayUtil;

/**
 * Created by YangZhi on 2017/8/2 19:44.
 */

public class BottomShadowComponent extends BaseComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new ControlUIAdapter() {
            final int height = DisplayUtil.convertDIP2PX(40);

            @Override
            public void show(boolean anim) {
                startAnim(0f,0f,1f);
            }

            @Override
            public void hide(boolean anim) {
                startAnim(0f,height,0f);
            }

            @Override
            protected View initView(ViewGroup parent) {
                View view = new View(parent.getContext());
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                layoutParams.gravity = Gravity.BOTTOM;
                view.setLayoutParams(layoutParams);
                view.setBackgroundResource(R.drawable.bg_player_controls_bottom2top_shadow);
                return view;
            }

            @Override
            protected void onViewCreated(View view) {

            }
        };
    }

    @Override
    protected BaseController onCreateController() {
        return new ControlController() {
        };
    }
}
