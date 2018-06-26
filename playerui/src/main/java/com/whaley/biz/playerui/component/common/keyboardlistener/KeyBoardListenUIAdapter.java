package com.whaley.biz.playerui.component.common.keyboardlistener;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/6 20:03.
 */

public class KeyBoardListenUIAdapter extends BaseUIAdapter<KeyBoardListenController> {

    private KeyboardListenerUtil keyboardListenerUtil;

    @Override
    protected void onViewCreated(View view) {
        if (keyboardListenerUtil != null) {
            keyboardListenerUtil.unbind();
        }
        keyboardListenerUtil = KeyboardListenerUtil.bind(getActivity(), new KeyboardListenerUtil.IKeyBoardVisibleListener() {
            @Override
            public void onSoftKeyBoardVisible(boolean visible, int visibleHeight) {
                getController().onSoftKeyBoardVisible(visible, visibleHeight);
            }
        });
    }

    @Override
    public void destroy() {
        if (keyboardListenerUtil != null) {
            keyboardListenerUtil.unbind();
        }
    }

    @Override
    protected View initView(ViewGroup parent) {
        return new FrameLayout(parent.getContext());
    }

}
