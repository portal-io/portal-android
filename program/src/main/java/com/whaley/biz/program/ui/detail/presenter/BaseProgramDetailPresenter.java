package com.whaley.biz.program.ui.detail.presenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.biz.playerui.playercontroller.PlayerController;
import com.whaley.biz.program.playersupport.event.ActivityResultEvent;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.playersupport.event.ChangeSerieEvent;
import com.whaley.biz.program.ui.detail.ProgramDetailView;
import com.whaley.core.appcontext.AppContextProvider;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by YangZhi on 2017/8/23 14:07.
 */

public class BaseProgramDetailPresenter<T extends ProgramDetailView> extends BasePagePresenter<T> {

    public static final int REQUEST_CODE_DOWNLOAD = 1;
    public final static int EVENT_ADD_DOWNLOAD = 1;

    public static final String KEY_EVENT_DOWNLOAD = "download_program";
    public static final String KEY_EVENT_PAY = "pay_program";
    public static final String KEY_EVENT_GET_COLLECTION = "get_collection_program";
    public static final String KEY_EVENT_FOLLOW = "follow_program";
    public static final String KEY_EVENT_POSTER = "poster_program";
    public static final String KEY_EVENT_EVENT_FOLLOW = "event_follow_program";
    public static final String KEY_EVENT_SHARE = "share_program";
    public static final String KEY_EVENT_COLLECTION = "collection_program";
    IPlayerController playerController;

    private int playerPos;

    public BaseProgramDetailPresenter(T view) {
        super(view);
    }

    public void onPlayerViewPrepared(IPlayerController playerController) {
        this.playerController = playerController;
    }

    @Override
    public void onViewCreated(Bundle arguments, Bundle saveInstanceState) {
        super.onViewCreated(arguments, saveInstanceState);
        if (arguments != null) {
            playerPos = arguments.getInt(ProgramConstants.KEY_PAYER_POS);
        }
    }

    @Override
    public void onViewDestroyed() {
        super.onViewDestroyed();
        this.playerController = null;
    }

    @Override
    public boolean onBackPressed() {
        if (playerController != null) {
            BackPressEvent backPressEvent = new BackPressEvent();
            playerController.getEventBus().post(backPressEvent);
            return true;
        }
        return super.onBackPressed();
    }


    public void changeSeries(int index) {
        if (playerController != null) {
            ChangeSerieEvent changeSerieEvent = new ChangeSerieEvent();
            changeSerieEvent.setSerie(index);
            playerController.getEventBus().post(changeSerieEvent);
        }
    }


    public void onDownloadClick() {
        if (playerController != null) {
            ModuleEvent moduleEvent = new ModuleEvent(KEY_EVENT_DOWNLOAD, null);
            playerController.getEventBus().post(moduleEvent);
        }
    }

    public void collection() {
        if (playerController != null) {
            ModuleEvent moduleEvent = new ModuleEvent(KEY_EVENT_COLLECTION, null);
            playerController.getEventBus().post(moduleEvent);
        }
    }

    public void btnPayClick() {
        if (playerController != null) {
            ModuleEvent moduleEvent = new ModuleEvent(KEY_EVENT_PAY, null);
            playerController.getEventBus().post(moduleEvent);
        }
    }


    public void onShareClick() {
        if (playerController != null) {
            ModuleEvent moduleEvent = new ModuleEvent(KEY_EVENT_SHARE, null);
            playerController.getEventBus().post(moduleEvent);
        }
    }

    public void onFollowClick() {
        if (playerController != null) {
            ModuleEvent moduleEvent = new ModuleEvent(KEY_EVENT_FOLLOW, null);
            playerController.getEventBus().post(moduleEvent);
        }
    }

    public void onPosterUserClick() {
        if (playerController != null) {
            ModuleEvent moduleEvent = new ModuleEvent(KEY_EVENT_POSTER, null);
            playerController.getEventBus().post(moduleEvent);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_DOWNLOAD) {
            if (grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onDownloadClick();
            } else {
                if (getUIView() != null) {
                    getUIView().showToast("权限已被限制，如需下载请到设置中重新设置权限");
                }
            }
        }
    }

    public int getPlayerPos() {
        return playerPos;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CommonConstants.SHARE_REQUEST_CODE && !data.getBooleanExtra(CommonConstants.SHARE_PARAM_OUTSIDE, true))
            return;
        EventBus eventBus = PlayerController.getInstance(AppContextProvider.getInstance().getContext()).getEventBus();
        if(eventBus==null){
            return;
        }
        if (!eventBus.hasSubscriberForEvent(ActivityResultEvent.class)) {
            return;
        }
        eventBus.post(new ActivityResultEvent());
    }

}
