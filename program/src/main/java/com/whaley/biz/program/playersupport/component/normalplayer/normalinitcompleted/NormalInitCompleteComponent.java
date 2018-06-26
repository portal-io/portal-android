package com.whaley.biz.program.playersupport.component.normalplayer.normalinitcompleted;

import com.whaley.biz.playerui.component.BaseComponent;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.BaseUIAdapter;
import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedController;
import com.whaley.biz.playerui.component.simpleplayer.initCompleted.InitCompletedUIAdapter;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.PlayerType;
import com.whaley.biz.program.model.SeriesModel;
import com.whaley.biz.program.playersupport.event.ChangeSerieEvent;

/**
 * Created by yangzhi on 2017/9/17.
 */

public class NormalInitCompleteComponent extends BaseComponent {

    @Override
    protected BaseUIAdapter onCreateUIAdapter() {
        return new InitCompletedUIAdapter();
    }

    @Override
    protected BaseController onCreateController() {
        return new InitCompletedController() {
            @Override
            protected String getNextPlayDataTitle(PlayData nextPlayData) {
                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                if (playData != null && playData.getType() == PlayerType.TYPE_MORETV_TV) {
                    return ((SeriesModel) playData.getCustomData(PlayerDataConstants.TV_NEXT_PLAY_SERIES)).getDisplayName();
                }
                return super.getNextPlayDataTitle(nextPlayData);
            }

            @Override
            protected boolean hasNext() {
                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                if (playData != null && playData.getType() == PlayerType.TYPE_MORETV_TV) {
                    return playData.getCustomData(PlayerDataConstants.TV_NEXT_PLAY_SERIES) != null;
                }
                return super.hasNext();
            }

            @Override
            protected void playNext() {
                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                if (playData != null && playData.getType() == PlayerType.TYPE_MORETV_TV) {
                    ChangeSerieEvent changeSerieEvent = new ChangeSerieEvent();
                    changeSerieEvent.setChangeToNext(true);
                    emitEvent(changeSerieEvent);
                    return;
                }
                super.playNext();
            }
        };
    }
}
