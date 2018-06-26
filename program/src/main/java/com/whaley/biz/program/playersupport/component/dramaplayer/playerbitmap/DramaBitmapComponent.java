package com.whaley.biz.program.playersupport.component.dramaplayer.playerbitmap;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2017/11/23.
 */

public class DramaBitmapComponent extends BaseComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }

    @Override
    protected BaseController onCreateController() {
        return new DramaBitmapController();
    }
}
