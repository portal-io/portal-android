package com.whaley.biz.program.ui.player;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.orhanobut.logger.Logger;
import com.whaley.biz.common.ui.CommonActivity;
import com.whaley.biz.playerui.PlayerView;
import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.program.R;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.playersupport.event.NewPlayerPageEvent;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.uiframe.BaseFragment;
import com.whaley.core.utils.DisplayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by YangZhi on 2017/8/2 15:07.
 */
@Route(path = ProgramRouterPath.PLAYER)
public class PlayerFragment extends BaseFragment {
    static final String VIDEO = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
    static final String LIVE = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    static final String VIDEO_PARSER = "http://vr.moguv.com/play/5aaf4e4e68994fe4a4b65bdbd35cd24a&flag=.ganalyze";
    static final String renderType = "360_3D_UD";

    public static final String KEY_PARAM_DATAS = "key_param_datas";

    public static final String KEY_PARAM_CONTINUE = "key_param_continue";

    PlayerView playerView;

    IPlayerController playerController;

    public static List<PlayData> PLAYDATAS;

    private boolean isContinue;

    private int playerPos;

    public static void goPage(Starter starter, List<PlayData> playDatas) {
        Intent intent = CommonActivity.createIntent(starter, null, "/program/ui/player");
        PlayerFragment.PLAYDATAS = playDatas;
        CommonActivity.goPage(starter, 0, intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_player;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (playerView != null) {
            playerView.setPlayerView();
        }
    }

    @Override
    protected void setViews(View view, @Nullable Bundle savedInstanceState) {
        super.setViews(view, savedInstanceState);
        if (getArguments() != null) {
            playerPos = getArguments().getInt(ProgramConstants.KEY_PAYER_POS);
        }
        Log.d("OnCreateView");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        if (isDefaultLandScape()) {
            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        playerView = onCreatePlayerView(view);
        playerView.setViewPrepareListener(new PlayerView.ViewPrepareListener() {
            @Override
            public void onViewPrepared(IPlayerController playerController) {
                PlayerFragment.this.onViewPrepared(playerController);
            }
        });
        updatePlayerViewSize();
    }

    protected boolean isDefaultLandScape() {
        return true;
    }

    protected void onViewPrepared(IPlayerController playerController) {
        PlayerFragment.this.playerController = playerController;
        List<PlayData> playDataList =
                PlayerFragment.PLAYDATAS;
        if (playDataList == null) {
            finish();
            return;
        }
        if (PLAYDATAS.size() == 1) {
            PlayData playData = PLAYDATAS.get(0);
            boolean isContinue = playData.getBooleanCustomData(PlayerDataConstants.PLAYER_IS_CONTINUE);
            if (isContinue) {
                this.isContinue = true;
                playerController.setNewPlayDataContinue(playData);
                return;
            }
        }
        playerController.setNewPlayData(PLAYDATAS, playerPos);
    }


    public PlayerView onCreatePlayerView(View view) {
        return (PlayerView) view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("OnDestoryView");

    }

    @Override
    public boolean onBackPressed() {
        if (playerController != null) {
            BackPressEvent backPressEvent = new BackPressEvent();
            playerController.getEventBus().post(backPressEvent);
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        if (playerView != null) {
            playerView.destory(isContinue);
        }
        PLAYDATAS = null;
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.finish();

    }

    @Subscribe
    public void onNewPlayerPageEvent(NewPlayerPageEvent newPlayerPageEvent) {
        finish();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (playerView != null) {
            updatePlayerViewSize();
        }
    }

    private void updatePlayerViewSize(){
        int[] screenSize = getScreenSize();
        View realPlayerView = playerView.getRealPlayerView();
        ViewGroup.LayoutParams layoutParams = realPlayerView.getLayoutParams();
        if (layoutParams.width != screenSize[0] || layoutParams.height!=screenSize[1]) {
            layoutParams.width = screenSize[0];
            layoutParams.height = screenSize[1];
            realPlayerView.requestLayout();
        }
    }


    public static int[] getScreenSize() {
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        WindowManager windowManager = (WindowManager) AppContextProvider.getInstance().getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method e = c.getMethod("getRealMetrics", new Class[]{DisplayMetrics.class});
            e.invoke(display, new Object[]{displayMetrics});
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
        } catch (Exception var7) {
            Logger.e(var7, "getDpi", new Object[0]);
        }
        int[] size = new int[]{width, height};
        return size;
    }
}
