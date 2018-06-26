package com.whaley.biz.livegift.support;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.livegift.GiftConstants;
import com.whaley.biz.livegift.R;
import com.whaley.biz.livegift.interactor.GetGiftTempInfo;
import com.whaley.biz.livegift.interactor.GetMemberTemplate;
import com.whaley.biz.livegift.model.GiftModel;
import com.whaley.biz.livegift.model.GiftNoticeModle;
import com.whaley.biz.livegift.model.GiftTempInfoModel;
import com.whaley.biz.livegift.model.LiveDetailsModel;
import com.whaley.biz.livegift.model.MemberModel;
import com.whaley.biz.livegift.model.MemberTemplateModel;
import com.whaley.biz.livegift.model.UserCostModel;
import com.whaley.biz.livegift.model.UserCostResultModel;
import com.whaley.biz.livegift.model.UserModel;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;
import com.whaley.core.utils.StrUtil;
import com.whaley.core.widget.viewholder.IViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: qxw
 * Date:2017/10/12
 * Introduction:
 */

public class LiveGiftSendController extends BaseController<LiveGiftSendUIAdapter> implements BIConstants, GiftConstants {


    public static final int GIFT_COLUMN_LANDSCAPE = 7;
    public static final int GIFT_COLUMN_PORTRAIT = 8;

    Disposable giftTempInfoDisposable, memberTemplateDisposable;


    private boolean isHaveMember;

    private GiftModel giftModel;

    private Toast mToast = null;


    public void onGiftClick(GiftModel giftModel) {
        this.giftModel = giftModel;
        getUIAdapter().updateSend(giftModel != null);
    }

    public List<GiftModel> giftModels;

    private List<MemberModel> memberModels;

    private MemberModel memberSelected;
    private int giftColumn = GIFT_COLUMN_PORTRAIT / 2;
    private int giftPageSize = GIFT_COLUMN_PORTRAIT;

    private int whaleyMoney = -1;

    public String getWhaleyMoney() {
        if (whaleyMoney != -1) {
            return whaleyMoney + getActivity().getString(R.string.whaley_money);
        } else {
            obtainWhaleyMoney();
            return "";
        }
    }

    public void setLandscapeGift() {
        giftColumn = GIFT_COLUMN_LANDSCAPE;
        giftPageSize = GIFT_COLUMN_LANDSCAPE;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case EVENT_LOGIN_SUCCESS:
                obtainWhaleyMoney();
                break;
            case "/setting/event/user_amount_update":
                whaleyMoney = event.getObject();
                if (getUIAdapter() != null)
                    getUIAdapter().updateWhaleyMoney(getWhaleyMoney());
                break;
        }
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

    public int getGiftColumn() {
        return giftColumn;
    }

    public List<MemberModel> getMemberModels() {
        return memberModels;
    }

    public void onSetectMemberClick(IViewHolder iViewHolder, int pos) {
        MemberModel memberModel = memberModels.get(pos);
        memberModel.setPos(pos);
        String selectedCode = getOldSelected();
        if (selectedCode != null && selectedCode.equals(memberModel.getMemberCode())) {
            memberSelected = null;
            getUIAdapter().updateMember(pos, false);
            getUIAdapter().updateSendText(getActivity().getString(R.string.send_gift));
        } else {
            memberSelected = memberModel;
            getUIAdapter().updateMember(pos, true);
            getUIAdapter().updateSendText(String.format(getActivity().getString(R.string.reward_gift), memberModel.getMemberName()));
        }
    }

    public String getOldSelected() {
        if (memberSelected != null) {
            return memberSelected.getMemberCode();
        }
        return null;
    }

    public MemberModel getOldMemberSelected() {
        return memberSelected;
    }

    public List<List<GiftModel>> getGiftModels() {
        List<List<GiftModel>> lists = new ArrayList<>();
        int num = giftModels.size() / giftPageSize;
        if (num == 0) {
            lists.add(giftModels);
        } else {
            for (int i = 0; i < num; i++) {
                lists.add(giftModels.subList(i * giftPageSize, (i + 1) * giftPageSize));
            }
            if (num * giftPageSize < giftModels.size()) {
                lists.add(giftModels.subList(num * giftPageSize, giftModels.size()));
            }
        }
        return lists;
    }

    public boolean isHaveMember() {
        return isHaveMember;
    }

    @Subscribe
    public void onLiveGiftEvent(ModuleEvent moduleEvent) {
        switch (moduleEvent.getEventName()) {
            case SHOW_LIVE_GIFT_EVENT:
                restTouchDuration(false);
                show();
                break;
        }
    }

    private void show(){
        getUIAdapter().show();
    }

    private void hide(){
        getUIAdapter().hide();
    }

    private void restTouchDuration(boolean isRegistTouchDuration) {
        RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();
        restTouchDurationEvent.setRegistTouchDuration(isRegistTouchDuration);
        restTouchDurationEvent.setChangingRegistTouchDuration(true);
        emitStickyEvent(restTouchDurationEvent);
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        PlayData playData = videoPreparedEvent.getPlayData();
        Object object = playData.getCustomData("key_live_info");
        if (object == null)
            return;
        String json = GsonUtil.getGson().toJson(object);
        LiveDetailsModel liveDetail = GsonUtil.getGson().fromJson(json, LiveDetailsModel.class);
        if (liveDetail == null) {
            return;
        }
        if (!(liveDetail.getIsGift() == 1 && !StrUtil.isEmpty(liveDetail.getGiftTemplate()))) {
            return;
        }
        obtainWhaleyMoney();
        isHaveMember = liveDetail.getIsTip() == 1 && !StrUtil.isEmpty(liveDetail.getMemberTemplate());
        if (isHaveMember) {
            getMember(playData.getId());
        }
        getLiveGift(liveDetail.getGiftTemplate());
    }

    public void obtainWhaleyMoney() {
        Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
            @Override
            public void onCall(UserModel userModel) {
                Router.getInstance().buildExecutor("/pay/service/useramount").callback(new Executor.Callback<Integer>() {

                    @Override
                    public void onCall(Integer money) {
                        whaleyMoney = money;
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                }).excute();
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();

    }

    public String getLiveCode() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData != null) {
            return playData.getId();
        }
        return "";
    }

    public String getLiveTitle() {
        return getPlayerController().getRepository().getCurrentPlayData().getTitle();
    }

    private void getMember(final String code) {
        if (memberModels != null) {
            return;
        }
        GetMemberTemplate getMemberTemplate = new GetMemberTemplate();
        memberTemplateDisposable = getMemberTemplate.buildUseCaseObservable(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MemberTemplateModel>() {
                    @Override
                    public void onNext(@NonNull MemberTemplateModel memberTemplateModel) {
                        memberModels = memberTemplateModel.getMemberTemplateRelDtos();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getLiveGift(final String code) {
        if (giftModels != null) {
            sendEvent();
            return;
        }
        GetGiftTempInfo getGiftTempInfo = new GetGiftTempInfo();
        giftTempInfoDisposable = getGiftTempInfo.buildUseCaseObservable(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<GiftTempInfoModel>() {
                    @Override
                    public void onNext(@NonNull GiftTempInfoModel giftTempInfoModel) {
                        giftModels = giftTempInfoModel.getGiftList();
                        sendEvent();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void sendEvent() {
        ModuleEvent moduleEvent = new ModuleEvent(LIVE_GIFT_EVENT, null);
        emitStickyEvent(moduleEvent);
    }

    public boolean isLandScape() {
        return AppContextProvider.getInstance().getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public void onSendGift() {
        sendGift(giftModel);
    }

    private void sendGift(final GiftModel giftModel) {
        if (giftModel == null) {
            showToast("还未选择礼物");
            return;
        }
        if (!NetworkUtils.isNetworkAvailable()) {
            showToast("当前网络不可用");
            return;
        }
        if (giftModel.getPrice() > whaleyMoney) {
            showToast("账户鲸币余额不足");
            return;
        }
        String bizParams = getLiveCode();
        if (StrUtil.isEmpty(bizParams)) {
            return;
        }
        if (isHaveMember) {
            if (memberSelected != null) {
                bizParams = bizParams + "," + memberSelected.getMemberCode();
            }
        }
        final UserCostModel userCostModel = new UserCostModel();
        userCostModel.setBizParams(bizParams);
        userCostModel.setBuyParams(giftModel.getGiftCode());
        whaleyMoney = whaleyMoney - giftModel.getPrice();
        getUIAdapter().updateWhaleyMoney(getWhaleyMoney());
        Router.getInstance().buildExecutor(PAY_USERCOST_SERVICE).putObjParam(userCostModel).callback(new Executor.Callback<UserCostResultModel>() {
            @Override
            public void onCall(UserCostResultModel userCostResultModel) {
                if (!userCostResultModel.isResult()) {
                    if (userCostResultModel.getCode() == 400 && userCostResultModel.getSubCode() != null && userCostResultModel.getSubCode().equals("045")) {
                        showToast("账户鲸币余额不足");
                    }
                } else {
                    if (isLandScape()) {
                        showToast("礼物已送出");
                    }
                    GiftNoticeModle giftNoticeModle = new GiftNoticeModle();
                    giftNoticeModle.setSelf(true);
                    giftNoticeModle.setGiftCode(giftModel.getGiftCode());
                    giftNoticeModle.setGiftName(giftModel.getTitle());
                    giftNoticeModle.setGiftIcon(giftModel.getIcon());
                    giftNoticeModle.setGiftPic(giftModel.getPic());
                    if (isHaveMember && memberSelected != null) {
                        giftNoticeModle.setMemberCode(memberSelected.getMemberCode());
                        giftNoticeModle.setMemberName(memberSelected.getMemberName());
                    }
                    ModuleEvent moduleEvent = new ModuleEvent(HIDE_LIVE_SELF_GIFT_EVENT, giftNoticeModle);
                    emitEvent(moduleEvent);
                }
                onClickSendGift();
                onGiftClick(null);
                getUIAdapter().clearViewHolderSelected();
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {

            }
        }).excute();
    }

    private void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(AppContextProvider.getInstance().getContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void emitGiftHeight() {
        ModuleEvent moduleEvent = new ModuleEvent(UPDATE_GIFT_HEIGHT_EVENT, getUIAdapter().getGiftLayoutHeight());
        emitEvent(moduleEvent);
    }

    public void giftHide() {
        restTouchDuration(true);
        ModuleEvent moduleEvent = new ModuleEvent(HIDE_LIVE_GIFT_EVENT, null);
        emitEvent(moduleEvent);
    }

    public void onRechargeClick() {
        Bundle bundle = new Bundle();
        bundle.putString("key_login_tips", getActivity().getString(R.string.dialog_recharge));
        Router.getInstance().buildNavigation(CURRENCY)
                //設置 starter
                .setStarter((Starter) getActivity())
                //設置 requestCode
                .with(bundle)
                .navigation();
    }

    @Subscribe(priority = 10)
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        if (getUIAdapter() != null && getUIAdapter().isOnShow()) {
            giftHide();
            hide();
            getEventBus().cancelEventDelivery(backPressEvent);
        }

    }

    //==============================BI埋点====================================//

    /**
     * 点击发送礼物
     */
    private void onClickSendGift() {
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(GIFT)
                .setCurrentPageId(ROOT_LIVE_DETAILS)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, getLiveCode())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, getLiveTitle())
                .setNextPageId(ROOT_LIVE_DETAILS);
        Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
    }


}
