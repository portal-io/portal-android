package com.whaley.biz.playerui;

import android.content.Context;

import com.whaley.wvrplayer.vrplayer.external.VRMediaPlayer;

/**
 * Created by YangZhi on 2017/8/1 17:35.
 */

public class PlayerUIInitor {

    public static void init(Context context){
        VRMediaPlayer.initPlayerData(context.getApplicationContext());
    }
}
