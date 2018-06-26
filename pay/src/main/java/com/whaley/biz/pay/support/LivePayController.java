package com.whaley.biz.pay.support;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.common.response.CMSResponse;
import com.whaley.biz.pay.interactor.FreeViewCheck;
import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.FreeViewModel;
import com.whaley.biz.pay.model.MediaResultInfo;
import com.whaley.biz.pay.model.PackageModel;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.Debug;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/9/7 18:57.
 */

public class LivePayController extends PayController<LivePayUIAdapter> {

    FreeViewCheck freeViewCheck;

    String GOOD_TYPE_LIVE = "live";

    Disposable freeViewCheckDisposable;

    private boolean isTestModel = false;

    private int playTime = -1;

    public LivePayController(int prepapreStartPlayPriority) {
        super(prepapreStartPlayPriority);
        freeViewCheck = new FreeViewCheck();
    }


    @Override
    protected String getGoodsType() {
        return GOOD_TYPE_LIVE;
    }

    @Override
    protected String getPromptText(int seconds) {
        PayDetailModel payDetailModel = getPayDetailModel(getPlayerController().getRepository().getCurrentPlayData());
        if (payDetailModel == null)
            return super.getPromptText(seconds);
        CouponModel packageCouponModel = payDetailModel.getCouponDto();
        if (payDetailModel.getContentPackageQueryDtos() != null && payDetailModel.getContentPackageQueryDtos().size() > 0) {
            PackageModel packageModel = payDetailModel.getContentPackageQueryDtos().get(0);
            if (packageModel != null) {
                packageCouponModel = packageModel.getCouponDto();
            }
        }
        if (packageCouponModel == null) {
            return super.getPromptText(seconds);
        }
        String price = packageCouponModel.getPrice();
        int fen;
        try {
            fen = Integer.valueOf(price);
        } catch (Exception e) {
            fen = 0;
        }
        price = fromFenToYuan(fen);
        return String.format("试看%s，%s元购买观看券可观看完整直播", formatSecond(seconds), price);
    }

    @Override
    protected void onNeedPayError() {
        super.onNeedPayError();
        PayDetailModel payDetailModel = getPayDetailModel(getPlayerController().getRepository().getCurrentPlayData());
        if (payDetailModel == null)
            return;
        String price = payDetailModel.getCouponDto().getPrice();
        if (payDetailModel.getContentPackageQueryDtos() != null
                && payDetailModel.getContentPackageQueryDtos().size() > 0
                && payDetailModel.getContentPackageQueryDtos().get(0) != null
                && payDetailModel.getContentPackageQueryDtos().get(0).getType() == 0) {
            price = payDetailModel.getContentPackageQueryDtos().get(0).getPrice();
        }
        int fen;
        try {
            fen = Integer.valueOf(price);
        } catch (Exception e) {
            fen = 0;
        }
        price = fromFenToYuan(fen);
        String needPayBuyStr = "购买观看券 ¥" + price;
        String needPayNameStr = "直播试看已结束，请付费观看";
        if (payDetailModel.getEndTime() > 0) {
            int time = (int) (payDetailModel.getEndTime()
                    - System.currentTimeMillis()) / 1000;
            if (time > 0) {
                needPayNameStr = String.format(Locale.CHINESE, "本场直播%s结束，请付费观看",
                        getFormatWholeTime(time));
            }
        }
        getUIAdapter().updateNeedPayText(needPayNameStr, needPayBuyStr);
    }

    @Override
    protected void checkProgress() {
        if(!getPlayerController().isPaused()) {
            playTime++;
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            playData.putCustomData("key_play_free_time", playTime);
            if (playTime*1000 >= maxPlayProgress) {
                showNeedPayUI();
                getPlayerController().pause();
            }
        }
    }

    @Override
    protected void checkDevice() {
        if(!getPlayerController().isPaused()) {
            super.checkDevice();
        }
    }

    @Override
    protected void reportDevice() {
        if(!getPlayerController().isPaused()) {
            super.reportDevice();
        }
    }

    @Subscribe(priority = 1)
    public void onChangeProgress(BaseEvent baseEvent){
        if(baseEvent.getEventType().equals("changeVideoProgress")){
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            MediaResultInfo mediaResultInfo = baseEvent.getObject(MediaResultInfo.class);
            if(playData!=null&&playData.getId().equals(mediaResultInfo.getCode())) {
                playTime = (int)(maxPlayProgress/1000 -mediaResultInfo.getPlayTime());
                if(playTime<0)
                    playTime=0;
                playData.putCustomData("key_play_free_time", playTime);
            }
        }
    }

    public static String fromFenToYuan(final int fen) {
        String yuan = "";
        final int MULTIPLIER = 100;
        try {
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        } catch (Exception e) {
            //
        }
        return yuan;
    }

    /**
     * @param time
     * @return 返回整时长
     * @author qxw
     * @time 2017/6/27 11:34
     */
    public static String getFormatWholeTime(int time) {
        int hour = 0;
        int minute = time / 60;
        if (minute >= 60) {
            hour = minute / 60;
        }
        if (hour != 0) {
            return hour + "小时后";
        }
        if (minute <= 10) {
            return "快要";
        }
        if (minute <= 20) {
            return "10分钟后";
        }
        if (minute <= 30) {
            return "20分钟后";
        }
        if (minute <= 40) {
            return "30分钟后";
        }
        if (minute <= 50) {
            return "40分钟后";
        }
        return "50分钟后";
    }

    private boolean isTestModel() {
        Router.getInstance().buildExecutor("/setting/service/istestmodel").callback(new Executor.Callback<Boolean>() {
            @Override
            public void onCall(Boolean b) {
                isTestModel = b;
            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
                isTestModel = false;
            }
        }).excute();
        return isTestModel;
    }

    @Override
    protected boolean checkCanFree(final PayDetailModel payDetailModel) {
        if (payDetailModel.getFreeTime() <= 0) {
            return super.checkCanFree(payDetailModel);
        }
        if (Debug.isDebug() && isTestModel()) {
            return super.checkCanFree(payDetailModel);
        }
        FreeViewCheck.Param param = new FreeViewCheck.Param();
        param.setCode(payDetailModel.getCode());
        param.setDeciceno(AppUtil.getDeviceId());
        disposeFreeViewCheck();
        freeViewCheck.buildUseCaseObservable(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<CMSResponse<FreeViewModel>>() {
                    @Override
                    public void onFinalError(Throwable e) {
                        payDetailModel.setFreeTime(0);
                        LivePayController.super.checkCanFree(payDetailModel);
                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        payDetailModel.setFreeTime(0);
                        LivePayController.super.checkCanFree(payDetailModel);
                    }

                    @Override
                    public void onNoDataError() {
                        payDetailModel.setFreeTime(0);
                        LivePayController.super.checkCanFree(payDetailModel);
                    }

                    @Override
                    public void onNext(@NonNull CMSResponse<FreeViewModel> freeViewModelCMSResponse) {
                        FreeViewModel freeViewModel = freeViewModelCMSResponse.getData();
                        if (freeViewModel == null || !freeViewModel.isResult()) {
                            payDetailModel.setFreeTime(0);
                        }
                        if (LivePayController.super.checkCanFree(payDetailModel)) {
                            emitDecryptEvent();
                            PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                            playData.putCustomData("key_is_free_time", true);
                            prepareStartPlayEvent.setPlayData(playData);
                            prepareStartPlayEvent.setMaxPriority(89);
                            emitStickyEvent(prepareStartPlayEvent);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return false;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        disposeFreeViewCheck();
    }

    private void disposeFreeViewCheck() {
        if (freeViewCheckDisposable != null) {
            freeViewCheckDisposable.dispose();
            freeViewCheckDisposable = null;
        }
    }

    public void onNeedPayTipClick() {
        Router.getInstance().buildNavigation(SettingRouterPath.REDEMPTION)
                //設置 starter
                .setStarter((Starter) getActivity())
                //設置 requestCode
                .setRequestCode(0)
                .withString("key_login_tips", "您需要登录才能进行相关操作\n确定登录吗？")
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, RouterConstants.TITLE_BAR_ACTIVITY)
                .navigation();
    }

    @Subscribe
    public void onUpdateDanmuHeightEvent(ModuleEvent moduleEvent){
        if(moduleEvent.getEventName().equals("/danmu/event/updatedanmuheight")){
            int height = (int) moduleEvent.getParam();
            getUIAdapter().updatePromptMarginBottom(height);
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

}
