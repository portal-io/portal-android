package com.whaley.biz.program.playersupport.component.splitplayer.initerror;

import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.program.playersupport.component.normalplayer.normaliniterror.NormalInitErrorComponent;

/**
 * Created by dell on 2017/10/30.
 */

public class SplitInitErrorComponent extends NormalInitErrorComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new SplitInitErrorUIAdapter();
    }

}
