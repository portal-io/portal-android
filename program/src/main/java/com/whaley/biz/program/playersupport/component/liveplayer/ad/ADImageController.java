package com.whaley.biz.program.playersupport.component.liveplayer.ad;

import android.os.Bundle;

import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.ActivityPauseEvent;
import com.whaley.biz.playerui.event.ActivityResumeEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.constants.ProgramRouterPath;
import com.whaley.biz.program.playersupport.model.ADModel;
import com.whaley.biz.program.uiview.model.PageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.Starter;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Author: qxw
 * Date:2018/3/26
 * Introduction:
 */

public class ADImageController extends BaseController<ADImageUIAdapter> {

    private List<ADModel> adModels;
    private boolean isHaveAD;
    private long waitTime;
    private long hideTime;
    private int animationType;
    private boolean isShowLivegift;
    private boolean isShowUI;
    static final int ANIMATION_START_TYPE = 1;
    static final int ANIMATION_END_TYPE = 2;
    static final int ANIMATION_HIDE_TYPE = 3;
    static final int ANIMATION_WAIT_START_TYPE = 4;
    static final int ANIMATION_JUMP_TYPE = 5;


    public int getDateSize() {
        return adModels.size();
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        PlayData playData = videoPreparedEvent.getPlayData();
        boolean isMultiPosition = playData.getBooleanCustomData(PlayerDataConstants.MULTI_POSITION_TYPE);
        if (isMultiPosition) {
            adModels = playData.getCustomData(PlayerDataConstants.LIVE_AD_TYPE);
            if (adModels != null && adModels.size() > 0) {
                isHaveAD = true;
                if (getUIAdapter() != null) {
                    isShowUI = true;
                    getUIAdapter().updateAD(adModels);
                }
            }
        }
    }


    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if (isHaveAD && !isShowUI) {
            isShowUI = true;
            getUIAdapter().updateAD(adModels);
        }
    }

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (animationType == ANIMATION_END_TYPE) {
            waitTime++;
            if (waitTime >= 20) {
                waitTime = 0;
                getUIAdapter().hideUI();
                animationType = ANIMATION_HIDE_TYPE;
            }
        }
        if (animationType == ANIMATION_HIDE_TYPE) {
            hideTime++;
            if (hideTime >= 120) {
                hideTime = 0;
                if (!isShowLivegift) {
                    getUIAdapter().showAnimationsStart();
                    animationType = ANIMATION_START_TYPE;
                } else {
                    animationType = ANIMATION_WAIT_START_TYPE;
                }
            }
        }
    }

    @Subscribe
    public void onActivityResumeEvent(ActivityResumeEvent activityResumeEvent){
        if (isHaveAD && isShowUI&&!isShowLivegift){
            animationType = ANIMATION_START_TYPE;
            getUIAdapter().showAnimationsStart();
        }

    }
    @Subscribe
    public void onActivityPauseEvent(ActivityPauseEvent activityPauseEvent){
        if (isHaveAD && isShowUI){
            animationType = ANIMATION_JUMP_TYPE;
            waitTime = 0;
            hideTime = 0;
        }
    }
    @Subscribe
    public void onMouleEvent(ModuleEvent moduleEvent) {
        switch (moduleEvent.getEventName()) {
            case "/program/event/showlivegift":
                isShowLivegift = true;
                if (isHaveAD) {
                    getUIAdapter().hideUI();
                }
                break;
            case "/livegift/event/gifthide":
                isShowLivegift = false;
                if (isHaveAD) {
                    if (animationType == ANIMATION_WAIT_START_TYPE) {
                        getUIAdapter().showAnimationsStart();
                        animationType = ANIMATION_START_TYPE;
                    } else {
                        if (animationType != ANIMATION_HIDE_TYPE) {
                            getUIAdapter().showUI();
                        }

                    }
                }
                break;
            case "/program/event/showcameralist":
                if (isHaveAD)
                    getUIAdapter().showCamera();
                break;
            case "/program/event/hidecameralist":
                if (isHaveAD)
                    getUIAdapter().hideCamera();
                break;
        }
    }



    public void onClickListener(int pos) {
        if (pos < adModels.size()) {
            PageModel pageModel = new PageModel();
            Bundle bundle = new Bundle();
            pageModel.setRouterPath(ProgramRouterPath.WEB_OUTSIDE);
            bundle.putString(ProgramConstants.WEBVIEW_URL, adModels.get(pos).getUrl());
            pageModel.setBundle(bundle);
            GoPageUtil.goPage((Starter) getActivity(), pageModel);
        }
    }

    public void adAnimationEnd() {
        waitTime = 0;
        animationType = ANIMATION_END_TYPE;
    }
}
