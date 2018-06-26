package com.whaley.biz.program.playersupport.component.liveplayer.initerror;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorController;
import com.whaley.biz.playerui.exception.PlayerException;
import com.whaley.biz.program.playersupport.exception.ProgramErrorConstants;

/**
 * Created by YangZhi on 2017/8/8 19:45.
 */

public class InitErrorComponent extends com.whaley.biz.playerui.component.simpleplayer.initError.InitErrorComponent{

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitErrorUIAdapter();
    }

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

            @Override
            protected void updateErrorText(String errorText) {
                errorText = errorText+"\n点击屏幕重新加载";
                super.updateErrorText(errorText);
            }
        };
    }
}
