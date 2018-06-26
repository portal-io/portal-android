package com.whaley.biz.program.playersupport.component.normalplayer.normaltitle;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/9.
 */

public class NormalTitleComponent extends BaseComponent{

    @Override
    protected BaseController onCreateController() {
        return new NormalTitleController();
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new NormalTitleUIAdapter();
    }

}
