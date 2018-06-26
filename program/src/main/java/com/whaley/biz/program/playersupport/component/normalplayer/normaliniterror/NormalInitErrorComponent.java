package com.whaley.biz.program.playersupport.component.normalplayer.normaliniterror;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorComponent;
import com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorController;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;

/**
 * Created by YangZhi on 2017/8/24 21:36.
 */

public class NormalInitErrorComponent extends InitErrorComponent{

    @Override
    protected BaseController onCreateController() {
        return new InitErrorController(){
            @Override
            protected void onError(PlayerException e) {
                if(e.getErrorCode() == ProgramErrorConstants.ERROR_PAY_NEED_PAY){
                    return;
                }
                super.onError(e);
            }
        };
    }

}
