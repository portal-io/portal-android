package com.whaley.biz.program.playersupport.component.normalplayer.userhistory;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;

/**
 * Author: qxw
 * Date:2017/9/12
 * Introduction:
 */

public class UserHistoryComponent extends BaseComponent {

    boolean isDrama;

    public UserHistoryComponent() {
        this(false);
    }

    public UserHistoryComponent(boolean isDrama) {
        this.isDrama = isDrama;
    }

    @Override
    protected BaseController onCreateController() {
        return new UserHistoryController(isDrama);
    }

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return null;
    }
}
