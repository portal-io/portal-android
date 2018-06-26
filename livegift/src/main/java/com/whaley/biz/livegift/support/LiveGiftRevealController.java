package com.whaley.biz.livegift.support;


import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.livegift.GiftConstants;
import com.whaley.biz.livegift.GiftSocketBizManager;
import com.whaley.biz.livegift.model.GiftNoticeModle;
import com.whaley.biz.livegift.model.GiftState;
import com.whaley.biz.livegift.model.UserModel;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.HideEvent;
import com.whaley.biz.playerui.event.KeyboardVisibleChangeEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.ShowEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.OnBackMainThread;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author: qxw
 * Date:2017/10/20
 * Introduction:
 */

public class LiveGiftRevealController extends BaseController<LiveGiftRevealUIAdapter> implements GiftConstants {

    private String attachedId;
    //    private boolean isGiftAttached;
    GiftSocketBizManager giftSocketBizManager;

//    GiftReceiveManager giftReceiveManager;

    UserModel userModel;
    LinkedBlockingQueue otherGiftQueue;
    LinkedBlockingQueue selfGiftQueue;

    GiftState giftState1 = new GiftState();

    GiftState giftState2 = new GiftState();

    boolean isGiftOpened;

    boolean isHideGift;

    boolean isPoll;

    int ceshi = 0;

    boolean isLandScape;

    @Subscribe
    public void onPollingEvent(PollingEvent pollingEvent) {
//        if (isGiftOpened) {
//            if (ceshi < 5) {
//                ceshi++;
//            } else {
//                ceshi = 0;
//                giftSocketBizManager.ceshi();
//            }
//        }
    }

    public int getGiftOrdinaryHeight() {
        if (isLandScape) {
            return DisplayUtil.convertDIP2PX(52);
        } else {
            return DisplayUtil.convertDIP2PX(320);
        }
    }

    public int getGiftKeyboardHeight() {
        if (isLandScape) {
            return DisplayUtil.convertDIP2PX(20);
        } else {
            return DisplayUtil.convertDIP2PX(142);
        }
    }


    public int getGiftTempHeight() {
        if (isLandScape) {
            return DisplayUtil.convertDIP2PX(52);
        } else {
            return DisplayUtil.convertDIP2PX(142);
        }
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if (isGiftOpened) {
            openGift();
            if(giftState1.getGiftNoticeModle()!=null){
                getUIAdapter().showGift(false);
            }
            if(giftState2.getGiftNoticeModle()!=null){
                getUIAdapter().showGift(true);
            }
        } else {
            closeGift();
        }
    }

    private void closeGift() {

    }

    private void openGift() {
        isGiftOpened = true;
        if (!isViewCreated()) {
            return;
        }
        attachGift();
    }

    @Subscribe
    public void onKeyboardHide(KeyboardVisibleChangeEvent event) {
        if (!isGiftOpened) {
            return;
        }
        if (event.isVisible()) {
            if (isLandScape) {
                isHideGift = true;
                hideGift();
//                if (getUIAdapter() != null)
//                    getUIAdapter().hideGift2();
//                GiftState giftState = getGiftState(true);
//                if (giftState.poll()) {
//                    giftState.setTotal(giftState.getTotal() - giftState.getDuplicate());
//                    giftState.setDuplicate(0);
//                } else {
//                    giftState2 = new GiftState();
//                }
            } else {
                getUIAdapter().updateHeight(getGiftKeyboardHeight());
            }
        } else {
            if (isLandScape) {
                isHideGift = false;
                showGift();
            } else {
                getUIAdapter().updateHeight(getGiftOrdinaryHeight());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (EVENT_LOGIN_SUCCESS.equals(event.getEventType())) {
            getUserModel();
        }
    }

    private void getUserModel() {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel model) {
                userModel = model;
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void unRegistEvents() {
        super.unRegistEvents();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

//    @Subscribe(sticky = true)
//    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
//        PlayData playData = videoPreparedEvent.getPlayData();
//        boolean isGift = playData.getCustomData("key_gift_type");
//        if (isGift) {
//            openGift();
//            getUserModel();
//            return;
//        }
//    }

    @Subscribe
    public void onShowEvent(ShowEvent showEvent) {
        isHideGift = false;
        showGift();
    }

    @Subscribe
    public void onHideEvent(HideEvent hideEvent) {
        isHideGift = true;
        hideGift();
    }

    private void showGift(){
        getUIAdapter().showGiftTemp();
    }

    private void hideGift(){
        getUIAdapter().hideGiftTemp();
    }

    @Subscribe
    public void onLiveGiftEvent(ModuleEvent moduleEvent) {
        switch (moduleEvent.getEventName()) {
            case LIVE_GIFT_EVENT:
                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                isLandScape = playData.getBooleanCustomData(ORIENTATION_IS_LANDSCAPE);
                openGift();
                getUserModel();
                break;
            case SHOW_LIVE_GIFT_EVENT:
                if (isLandScape) {
                    isHideGift = true;
                    hideGift();
                } else {
                    getUIAdapter().updateHeight(getGiftTempHeight());
                }
                break;
            case HIDE_LIVE_GIFT_EVENT:
                if (isLandScape) {
                    isHideGift = false;
                    showGift();
                } else {
                    getUIAdapter().updateHeight(getGiftOrdinaryHeight());
                }
                break;
            case HIDE_LIVE_SELF_GIFT_EVENT:
                GiftNoticeModle giftNoticeModle = (GiftNoticeModle) moduleEvent.getParam();
                giftNoticeModle.setUid(userModel.getAccount_id());
                giftNoticeModle.setNickName(userModel.getNickname());
                giftNoticeModle.setUserHeadUrl(userModel.getAvatar());
                giftNoticeModle.saveTextContent();
                addGift(giftNoticeModle);
                break;
        }
    }


    private void attachGift() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData == null) {
            return;
        }
        if (!playData.getId().equals(attachedId)) {
            otherGiftQueue = new LinkedBlockingQueue();
            selfGiftQueue = new LinkedBlockingQueue();
            giftSocketBizManager = new GiftSocketBizManager(playData.getId(), playData.getTitle());
            giftSocketBizManager.setGiftSocketListener(new GiftSocketBizManager.GiftSocketListener() {
                @Override
                public void onGift(final GiftNoticeModle giftNoticeModle) {
                    if (userModel != null && giftNoticeModle.getUid().equals(userModel.getAccount_id())) {
                        return;
                    }
                    giftNoticeModle.saveTextContent();
                    OnBackMainThread.onBackMainThread(new OnBackMainThread.OnBackMainThreadListener() {
                        @Override
                        public void onBackMainThread() {
                            addGift(giftNoticeModle);
                        }
                    });

                }

                @Override
                public void onLogined() {
                    isPoll = true;
                }
            });
            giftSocketBizManager.join();
            giftSocketBizManager.onJoin();
            attachedId = playData.getId();
        }

    }

    /**
     * 收到新礼物添加进对于的列表
     *
     * @param giftNoticeModle
     */
    private void addGift(GiftNoticeModle giftNoticeModle) {
        if (giftState1.getGiftNoticeModle() == null) {
            giftState1.setGiftNoticeModle(giftNoticeModle);
            getUIAdapter().showGift(false);
            return;
        }
        if (giftState2.getGiftNoticeModle() == null) {
            if (compare(true, giftNoticeModle)) {
                return;
            }
            giftState2.setGiftNoticeModle(giftNoticeModle);
            getUIAdapter().showGift(true);
            return;
        }
        if (giftNoticeModle.isSelf()) {
            selfGiftQueue.offer(giftNoticeModle);
        } else {
            otherGiftQueue.offer(giftNoticeModle);
        }
    }

    /**
     * 连击加1
     *
     * @param isGift2
     */
    private void batter(boolean isGift2) {
        GiftState giftState = getGiftState(isGift2);
        giftState.setDuplicate(giftState.getDuplicate() + 1);
        if (getUIAdapter() != null) {
            getUIAdapter().batter(isGift2, giftState.getDuplicate());
        }
    }

    /**
     * 非自己的礼物列表获取下一个礼物
     *
     * @return
     */
    public GiftNoticeModle obtainOtherGift() {
        if (otherGiftQueue.size() == 0) {
            return null;
        }
        return (GiftNoticeModle) otherGiftQueue.poll();
    }

    /**
     * 自己的礼物列表获取下一个礼物
     *
     * @return
     */
    public GiftNoticeModle obtainSelfGift() {
        if (selfGiftQueue.size() == 0) {
            return null;
        }
        return (GiftNoticeModle) selfGiftQueue.poll();
    }

    /**
     * 获取显示礼物
     *
     * @param isGift2
     * @return
     */
    private GiftState getGiftState(boolean isGift2) {
        return isGift2 ? giftState2 : giftState1;
    }

    /**
     * 当前的礼物是否在另一个礼物条目中显示
     *
     * @param isGift2
     * @param giftNoticeModle
     * @return
     */
    private boolean compare(boolean isGift2, GiftNoticeModle giftNoticeModle) {
        return compare(isGift2, giftNoticeModle, 1);
    }

    /**
     * 当前的礼物是否在另一个礼物条目中显示
     *
     * @param isGift2
     * @param giftNoticeModle
     * @param num
     * @return
     */
    private boolean compare(boolean isGift2, GiftNoticeModle giftNoticeModle, int num) {
        GiftState giftState = getGiftState(!isGift2);
        if (giftState.getGiftNoticeModle() != null && giftState.getGiftNoticeModle().equals(giftNoticeModle) && (giftState.getState() == GiftState.SHOW_STATE || giftState.getState() == GiftState.STAY_STATE)) {
            giftState.addGift(num);
            return true;
        }
        return false;
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dettachGift();
    }


    private void dettachGift() {
        giftSocketBizManager.onAllQuit();
        attachedId = "";
    }

    /**
     * 向列表里取礼物
     *
     * @param isGift2
     * @param isSelf
     * @return
     */
    private GiftNoticeModle nextGift(boolean isGift2, boolean isSelf) {
        boolean isComplete = false;
        while (!isComplete) {
            GiftNoticeModle gift = isSelf ? obtainSelfGift() : obtainOtherGift();
            if (gift == null) {
                return null;
            }
            if (isGift2) {
                if (compare(true, gift)) {
                    continue;
                }
                return gift;
            } else {
                if (compare(true, gift)) {
                    continue;
                }
                return gift;
            }
        }
        return null;
    }

    /**
     * 当前是自己的礼物获取下一个礼物
     *
     * @param isGift2
     * @return
     */
    private boolean selfNextGift(boolean isGift2) {
        GiftState giftState = getGiftState(isGift2);
        if (giftState.poll()) {//有连击
            batter(isGift2);
            return false;
        }
        if (!nextGiftDeal(isGift2, true)) {
            return false;
        }
        if (giftState.isTemporary()) {
            GiftNoticeModle giftNoticeModle = giftState.getTemporaryModle();
            if (compare(isGift2, giftNoticeModle, giftState.getTemporaryNum())) {
                giftState.setTemporaryModle(null);
                giftState.setTemporaryNum(0);
                return nextOtherGift(isGift2);
            } else {
                giftState.replace();
                updateGift(isGift2);
                return false;
            }
        } else {
            return nextOtherGift(isGift2);
        }
    }

    /**
     * 当前是他人的礼物获取下一个礼物
     *
     * @param isGift2
     * @return
     */
    private boolean nextOtherGift(boolean isGift2) {
        GiftNoticeModle gift = nextGift(isGift2, false);
        if (gift == null) {
            return true;
        }
        GiftState giftState = getGiftState(isGift2);
        if (giftState.getGiftNoticeModle() != null && giftState.getGiftNoticeModle().equals(gift)) {
            batter(isGift2);
        } else {
            giftState.setGiftNoticeModle(gift);
            updateGift(isGift2);
        }
        return false;
    }

    /**
     * 对列表的下个礼物做处理
     *
     * @param isGift2
     * @param isSelf
     * @return
     */
    private boolean nextGiftDeal(boolean isGift2, boolean isSelf) {
        GiftNoticeModle gift = nextGift(isGift2, isSelf);
        if (gift == null) {
            return true;
        }
        GiftState state = getGiftState(isGift2);
        if (gift.equals(state.getGiftNoticeModle())) {
            batter(isGift2);
            return false;
        }
        if (isSelf) {
            state.jumpQueue(gift);
        } else {
            state.setGiftNoticeModle(gift);
        }
        updateGift(isGift2);
        return false;
    }

    private void updateGift(boolean isGift2) {
        if (getActivity() != null) {
            getUIAdapter().updateGift(isGift2);
        }
    }

    /**
     * 取下一个礼物
     *
     * @param isGift2
     * @return
     */

    public boolean nextGift(boolean isGift2) {
        GiftState giftState = getGiftState(isGift2);
        if (giftState.getGiftNoticeModle()!=null&&giftState.getGiftNoticeModle().isSelf()) {
            return selfNextGift(isGift2);
        }
        if (!nextGiftDeal(isGift2, true)) {
            return false;
        }
        if (giftState.poll()) {
            batter(isGift2);
            return false;
        }
        return nextOtherGift(isGift2);

    }


    public GiftNoticeModle getGiftMode(boolean isGift2) {
        if (isGift2) {
            return giftState2.getGiftNoticeModle();
        }
        return giftState1.getGiftNoticeModle();
    }

    public void setState(boolean isGift2, int state) {
        if (isGift2) {
            giftState2.setState(state);
            return;
        }
        giftState1.setState(state);
    }

    public void endGift(boolean isGift2) {
        GiftNoticeModle gift = obtainOtherGift();
        if (isGift2) {
            giftState2 = new GiftState();
            giftState2.setGiftNoticeModle(gift);
        } else {
            giftState1 = new GiftState();
            giftState1.setGiftNoticeModle(gift);
        }

        if (gift != null) {
            getUIAdapter().showGift(isGift2, 1000);
        }
    }
}