package com.whaley.biz.program.playersupport.component.dramaplayer.initbackgroud;

import android.text.TextUtils;

import com.whaley.biz.playerui.component.simpleplayer.initbackground.InitBackgroundController;
import com.whaley.biz.playerui.event.CompletedEvent;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.NodeModel;

/**
 * Created by dell on 2017/11/14.
 */

public class DramaInitBackgroundController extends InitBackgroundController<DramaInitBackgroundUIAdapter>{

    @Override
    public void onCompletedEvent(CompletedEvent completedEvent) {
        NodeModel nodeModel = getPlayerController().getRepository().getCurrentPlayData().getCustomData(PlayerDataConstants.CURRENT_DRAMA_NODE);
        if(isLastNode(nodeModel) && TextUtils.isEmpty(nodeModel.getDefaultItem())){
            getUIAdapter().changeVisibleOnComplete();
        }
    }

    private boolean isLastNode(NodeModel nodeModel){
        boolean isLast = false;
        if(TextUtils.isEmpty(nodeModel.getChildrenCode())){
            isLast = true;
        }else{
            String[] childrens = nodeModel.getChildrenCode().split("-");
            if(childrens.length<=1){
                isLast = true;
            }
        }
        return isLast;
    }

}
