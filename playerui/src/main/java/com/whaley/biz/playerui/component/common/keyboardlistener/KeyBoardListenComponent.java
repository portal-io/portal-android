package com.whaley.biz.playerui.component.common.keyboardlistener;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/9/6 20:02.
 */

public class KeyBoardListenComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new KeyBoardListenController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new KeyBoardListenUIAdapter();
    }
}
