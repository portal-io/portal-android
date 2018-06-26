package com.whaley.biz.playerui.component.common.keyboardlistener;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.KeyboardVisibleChangeEvent;

/**
 * Created by YangZhi on 2017/9/6 20:07.
 */

public class KeyBoardListenController extends BaseController<KeyBoardListenUIAdapter> {

    public void onSoftKeyBoardVisible(boolean visible, int visibleHeight) {
        KeyboardVisibleChangeEvent event = new KeyboardVisibleChangeEvent(visible, visibleHeight);
        emitEvent(event);
    }
}
