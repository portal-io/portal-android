package com.whaley.biz.playerui.event;

import com.whaley.biz.playerui.exception.PlayerException;

/**
 * Created by YangZhi on 2017/8/24 15:50.
 */

public class ErrorEvent extends PriorityEvent{


    private PlayerException playerException;

    public void setPlayerException(PlayerException playerException) {
        this.playerException = playerException;
    }

    public PlayerException getPlayerException() {
        return playerException;
    }
}
