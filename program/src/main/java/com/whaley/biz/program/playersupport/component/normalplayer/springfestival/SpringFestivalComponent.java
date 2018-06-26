package com.whaley.biz.program.playersupport.component.normalplayer.springfestival;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2018/1/23.
 */

public class SpringFestivalComponent extends BaseComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SpringFestivalUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new SpringFestivalController();
    }

}
