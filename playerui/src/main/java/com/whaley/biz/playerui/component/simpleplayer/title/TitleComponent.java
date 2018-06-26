package com.whaley.biz.playerui.component.simpleplayer.title;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by YangZhi on 2017/8/3 19:20.
 */

public class TitleComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new TitleController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new TitleUIAdapter();
    }
}
