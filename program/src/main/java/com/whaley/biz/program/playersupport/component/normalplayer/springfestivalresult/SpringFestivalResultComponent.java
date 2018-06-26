package com.whaley.biz.program.playersupport.component.normalplayer.springfestivalresult;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Created by dell on 2018/1/24.
 */

public class SpringFestivalResultComponent extends BaseComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SpringFestivalResultUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new SpringFestivalResultController();
    }
}
