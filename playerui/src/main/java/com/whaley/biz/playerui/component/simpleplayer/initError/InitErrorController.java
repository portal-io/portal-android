package com.whaley.biz.playerui.component.simpleplayer.initError;

import com.whaley.biz.playerui.component.common.init.InitController;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.exception.IJKPlayerException;
import com.whaley.biz.playerui.exception.PlayerException;

/**
 * Created by YangZhi on 2017/8/7 17:27.
 */

public class InitErrorController extends InitController<InitErrorUIAdapter>{


    @Override
    protected void onError(PlayerException e) {
        super.onError(e);
        if(e!=null&&e instanceof IJKPlayerException){
            updateErrorText("播放器异常 ("+e.getErrorCode()+")");
            return;
        }
        updateErrorText(e.getMessage()+" ("+e.getErrorCode()+")");
    }

    protected void updateErrorText(String errorText){
        getUIAdapter().updateErrorText(errorText);
    }


    public void onRetryClick(){
        getPlayerController().reStartPlay();
    }


}
