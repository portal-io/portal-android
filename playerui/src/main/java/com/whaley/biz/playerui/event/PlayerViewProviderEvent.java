package com.whaley.biz.playerui.event;

import com.whaley.biz.playerui.PlayerView;

import java.util.concurrent.Executor;

/**
 * Created by yangzhi on 2017/9/4.
 */

public class PlayerViewProviderEvent {

    private String name;

    private PlayerViewProvider playerViewProvider;

    public PlayerViewProviderEvent(String name, PlayerViewProvider playerViewProvider) {
        this.name = name;
        this.playerViewProvider = playerViewProvider;
    }


    public PlayerViewProvider getPlayerViewProvider() {
        return playerViewProvider;
    }

    public String getName() {
        return name;
    }

    public interface PlayerViewProvider {
        PlayerView getPlayerView(int width,int height);
    }
}
