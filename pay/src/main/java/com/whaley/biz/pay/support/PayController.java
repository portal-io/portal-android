package com.whaley.biz.pay.support;

import android.os.Bundle;

import com.whaley.biz.common.constants.RouterConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.interactor.observer.ErrorHandleObserver;
import com.whaley.biz.pay.CouponPackUtil;
import com.whaley.biz.pay.PayUtil;
import com.whaley.biz.pay.R;
import com.whaley.biz.pay.event.PayEvent;
import com.whaley.biz.pay.interactor.CheckDevice;
import com.whaley.biz.pay.interactor.GetCouponInfo;
import com.whaley.biz.pay.interactor.IsPayed;
import com.whaley.biz.pay.interactor.ReportDevice;
import com.whaley.biz.pay.model.CouponModel;
import com.whaley.biz.pay.model.CouponPackModel;
import com.whaley.biz.pay.model.PackageModel;
import com.whaley.biz.pay.model.PayEventModel;
import com.whaley.biz.pay.model.ThirdPayModel;
import com.whaley.biz.pay.model.user.UserModel;
import com.whaley.biz.pay.response.CouponInfoResponse;
import com.whaley.biz.pay.response.DeviceResponse;
import com.whaley.biz.playerui.component.BaseController;
import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.BlankShowEvent;
import com.whaley.biz.playerui.event.ErrorEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PollingEvent;
import com.whaley.biz.playerui.event.PrepareStartPlayEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.playerui.event.SwicthInitBgVisibleEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.GsonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/8/17 20:19.
 */

public class PayController<UIADAPTER extends PayController.IPayUIAdapter> extends BaseController<UIADAPTER> {

    private static final String LOGIN_SUCCESS_EVENT = "login_success";

    static List<String> PAYED_CACHE = new ArrayList<>();

    String GOOD_TYPE_RECORDED = "recorded";

    private IsPayed isPayed;

//    private int whaleyMoney = -1;

    private ReportDevice reportDevice;

    private CheckDevice checkDevice;

    private Disposable reportDisposable, checkDisposable;

    private UserModel loginUserModel;

    protected long maxPlayProgress = -1;

    private boolean isHasPrompt;

    private Disposable disposable;

    private GetCouponInfo getCouponInfo;

    private Disposable getCounponDisposable;

    private final int prepapreStartPlayPriority;

    private int count;

    private boolean isReportDevice;

    private boolean isPay;

    public PayController(int prepapreStartPlayPriority) {
        this.prepapreStartPlayPriority = prepapreStartPlayPriority;
        isPayed = new IsPayed(RepositoryManager.create(null), Schedulers.io(), AndroidSchedulers.mainThread());
        reportDevice = new ReportDevice();
        checkDevice = new CheckDevice();
        getCouponInfo = new GetCouponInfo();
    }

    protected PayDetailModel getPayDetailModel(PlayData playData) {
        Object object = playData.getCustomData("key_pay_detail_info");
        if (object == null)
            return null;
        String json = GsonUtil.getGson().toJson(object);
        return GsonUtil.getGson().fromJson(json, PayDetailModel.class);
    }

    @Subscribe
    public void onLoginSuccess(BaseEvent baseEvent) {
        String eventType = baseEvent.getEventType();
        switch (eventType) {
            case LOGIN_SUCCESS_EVENT:
                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                PayDetailModel payDetailModel = getPayDetailModel(playData);
                if (payDetailModel == null)
                    return;
                checkLogin();
                checkIsPay(payDetailModel, true);
                break;
            default:
                break;
        }
    }

    @Subscribe(priority = 10)
    public void onErrorEvent(ErrorEvent errorEvent) {
        if (errorEvent.getPlayerException() != null && errorEvent.getPlayerException() instanceof PayError) {
            onNeedPayError();
            return;
        }
        getUIAdapter().hidePrompt();
    }

    protected void onNeedPayError() {
        getUIAdapter().showNeedPay();
        SwicthInitBgVisibleEvent swicthInitBgVisibleEvent = new SwicthInitBgVisibleEvent();
        swicthInitBgVisibleEvent.setVisible(true);
        emitEvent(swicthInitBgVisibleEvent);
        RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();
        restTouchDurationEvent.setRegistTouchDuration(false);
        emitEvent(restTouchDurationEvent);
    }

    protected void onHidePayError() {
        getUIAdapter().hideNeedPay();
        SwicthInitBgVisibleEvent swicthInitBgVisibleEvent = new SwicthInitBgVisibleEvent();
        swicthInitBgVisibleEvent.setVisible(false);
        emitEvent(swicthInitBgVisibleEvent);
    }

    @Subscribe
    public void onPayEvent(ModuleEvent moduleEvent) {
        if ("pay_program".equals(moduleEvent.getEventName())) {
            onPayClick();
        }
    }

    @Subscribe
    public void onRedmptionSuccess(BaseEvent event) {
        if (event.getEventType().equals("redemtion_success")) {
            String code = event.getObject();
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            if (playData == null) {
                return;
            }
            if (playData.getId().equals(code)) {
                onPaySuccess(playData, playData.getId());
            }
        }
    }


    @Subscribe
    public void onPayResult(PayEvent payEvent) {
        PayEventModel payEventModel = payEvent.getObject();
        if (!payEventModel.isPayed()) {
            return;
        }
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData == null) {
            return;
        }
        PayDetailModel payDetailModel = getPayDetailModel(playData);
        if (payDetailModel == null) {
            return;
        }
        CouponModel packageCouponModel = null;
        if (payDetailModel.getContentPackageQueryDtos() != null && payDetailModel.getContentPackageQueryDtos().size() > 0) {
            PackageModel packageModel = payDetailModel.getContentPackageQueryDtos().get(0);
            if (packageModel != null) {
                packageCouponModel = packageModel.getCouponDto();
            }
        }
        CouponModel couponModel = payDetailModel.getCouponDto();
        String payGoodNo = payEventModel.getGoodsNo();
        boolean isSameCoupon = couponModel != null && payGoodNo.equals(couponModel.getCode());
        boolean isSamePackage = packageCouponModel != null && payGoodNo.equals(packageCouponModel.getCode());
        if (!isSameCoupon && !isSamePackage) {
            return;
        }
        onPaySuccess(playData, payDetailModel.getCode());
    }


    private void onPaySuccess(PlayData playData, String code) {
        isHasPrompt = false;
        isPay = true;
        emitPayEvent(true, code);
        playData.putCustomData("key_is_free_time", false);
        getPlayerController().restError();
        onHidePayError();
        getUIAdapter().hidePrompt();
        if (getPlayerController().getRepository().isVideoPrepared()) {
            getPlayerController().start();
        } else {
            getPlayerController().reStartPlay(true);
        }
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        getUIAdapter().changeNeedPayOnLandScape();
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        getUIAdapter().changeNeedPayOnPortrait();
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        getUIAdapter().hideNeedPay();
    }

    @Subscribe(priority = 90)
    public void onPrepareStartPlayEvent(PrepareStartPlayEvent prepareStartPlayEvent) {
        if (prepareStartPlayEvent.getMaxPriority() < 90)
            return;
        PlayData playData = prepareStartPlayEvent.getPlayData();
        PayDetailModel payDetailModel = getPayDetailModel(playData);
        if (payDetailModel == null)
            return;
        if (!checkIsNeedToPay(payDetailModel))
            return;
        if (!checkLogin()) {
            emitPayEvent(false, payDetailModel.getCode());
            // 未登录的直接检查是否有试看时间
            if (!checkCanFree(payDetailModel)) {
                // 如果没有试看时间则截断事件
                getEventBus().cancelEventDelivery(prepareStartPlayEvent);
                return;
            }
            playData.putCustomData("key_is_free_time", true);
            emitDecryptEvent();
            // 有试看时间则继续传递
            return;
        }
        // 已登录的用户检查是否已支付
        if (checkIsPay(payDetailModel, false)) {
            emitDecryptEvent();
            return;
        }
        getEventBus().cancelEventDelivery(prepareStartPlayEvent);
    }

    private CouponPackModel getCoupon(PayDetailModel payDetailModel) {
        CouponPackModel couponPackModel = CouponPackUtil.getCouponPackModel(payDetailModel.getCouponDto()
                , payDetailModel.getContentPackageQueryDtos());
        return couponPackModel;
    }

    @Subscribe
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        if (isHasPrompt) {
            getUIAdapter().showPrompt();
        }
    }

    @Subscribe
    public void onPolling(PollingEvent pollingEvent) {
        if (!getPlayerController().isStarted()) {
            return;
        }
        count++;
        if (count >= 10) {
            count = 0;
            if (isReportDevice) {
                checkDevice();
            } else if (isPay) {
                reportDevice();
            }
        }
        if (!isHasPrompt) {
            return;
        }
        checkProgress();
    }

    protected void checkProgress() {
        long currentProgress = getPlayerController().getCurrentProgress();
        if (currentProgress >= maxPlayProgress) {
            showNeedPayUI();
            getPlayerController().pause();
        }
    }

    private boolean checkLogin() {
        Router.getInstance().buildExecutor("/user/service/checklogin")
                .callback(new Executor.Callback<UserModel>() {
                    @Override
                    public void onCall(UserModel userModel) {
                        loginUserModel = userModel;
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                }).excute();
        return loginUserModel != null;
    }

    private boolean checkIsNeedToPay(PayDetailModel payDetailModel) {
        boolean isNeedToPay = payDetailModel.getIsChargeable() == 1;
        if (!isNeedToPay) {
            //
        }
        return isNeedToPay;
    }

    protected String getGoodsType() {
        return GOOD_TYPE_RECORDED;
    }

    private boolean checkIsPay(final PayDetailModel payDetailModel, final boolean isAfterLogin) {
        final IsPayed.GoodsPayedParam goodsPayedParam = new IsPayed.GoodsPayedParam();
        goodsPayedParam.setGoodsType(getGoodsType());
        goodsPayedParam.setGoodsNo(payDetailModel.getCode());
        if (PAYED_CACHE.contains(getCacheKey(goodsPayedParam))) {
            isPay = true;
            if (isAfterLogin) {
                onPaySuccess(getPlayerController().getRepository().getCurrentPlayData(), payDetailModel.getCode());
            } else {
                emitPayEvent(true, payDetailModel.getCode());
            }
            return true;
        }
        dispose();
        disposable = isPayed.buildUseCaseObservable(goodsPayedParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean isPayed) {
                        emitPayEvent(isPayed, payDetailModel.getCode());
                        if (!isPayed) {
//                            updateUserJingbiPrice();
                            if (isAfterLogin) {
                                getCouponInfo();
                                return;
                            }
                            // 未支付的用户则检查是否有试看时间
                            if (checkCanFree(payDetailModel)) {
                                emitDecryptEvent();
                                PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                                PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                                playData.putCustomData("key_is_free_time", true);
                                prepareStartPlayEvent.setPlayData(playData);
                                prepareStartPlayEvent.setMaxPriority(89);
                                emitStickyEvent(prepareStartPlayEvent);
                            }
                        } else {
                            isPay = true;
                            String key = getCacheKey(goodsPayedParam);
                            if (!PAYED_CACHE.contains(key)) {
                                PAYED_CACHE.add(key);
                            }
                            if (isAfterLogin) {
                                onPaySuccess(getPlayerController().getRepository().getCurrentPlayData(), payDetailModel.getCode());
                                return;
                            }
                            if (getPlayerController().getRepository().isVideoPrepared()) {
                                getPlayerController().start();
                                return;
                            }
                            // 已支付的用户则继续传递事件
                            emitDecryptEvent();
                            PrepareStartPlayEvent prepareStartPlayEvent = new PrepareStartPlayEvent();
                            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                            playData.putCustomData("key_is_free_time", false);
                            prepareStartPlayEvent.setPlayData(playData);
                            prepareStartPlayEvent.setMaxPriority(89);
                            emitStickyEvent(prepareStartPlayEvent);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return false;
    }

//    //获取当前用户
//    private void updateUserJingbiPrice() {
//        Router.getInstance().buildExecutor("/pay/service/useramount").callback(new Executor.Callback<Integer>() {
//
//            @Override
//            public void onCall(Integer money) {
//                whaleyMoney = money;
//            }
//
//            @Override
//            public void onFail(Executor.ExecutionError executionError) {
//
//            }
//        }).excute();
//    }

    private void emitPayEvent(boolean isPay, String code) {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.putCustomData("key_is_pay", isPay);
        PayEventModule payModule = new PayEventModule();
        payModule.setPay(isPay);
        payModule.setCode(code);
        ModuleEvent payEvent = new ModuleEvent("key_pay", GsonUtil.getGson().toJson(payModule));
        emitEvent(payEvent);
    }

    private String getCacheKey(IsPayed.GoodsPayedParam goodsPayedParam) {
        return loginUserModel.getAccount_id() + goodsPayedParam.getGoodsNo() + goodsPayedParam.getGoodsType();
    }

    protected boolean checkCanFree(PayDetailModel payDetailMode) {
        if (!checkFreeTime(payDetailMode)) {
            showNeedPayUI();
            return false;
        }
        return true;
    }

    private boolean checkFreeTime(PayDetailModel payDetailModel) {
        if (payDetailModel.getFreeTime() > 0) {
            maxPlayProgress = payDetailModel.getFreeTime() * 1000;
            isHasPrompt = true;
            showPayPrompt(payDetailModel.getFreeTime());
            return true;
        }
        isHasPrompt = false;
        maxPlayProgress = 0;
        return false;
    }

    /**
     * 发送解密地址事件
     */
    protected void emitDecryptEvent() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.putCustomData("key_need_decrypt_url", true);
    }

    /**
     * 显示支付提示
     */
    private void showPayPrompt(int seconds) {
        String promptText = getPromptText(seconds);
        getUIAdapter().changePromptText(promptText);
    }

    protected String getPromptText(int seconds) {
        return String.format(getContext().getString(R.string.text_try_the_show), formatSecond(seconds));
    }


    protected String formatSecond(int seconds) {
        int minute = seconds / 60;
        int second = seconds % 60;
        StringBuilder stringBuilder = new StringBuilder("");
        if (minute > 0) {
            stringBuilder.append(minute).append("分钟");
        }
        if (second > 0) {
            stringBuilder.append(second).append("秒");
        }
        return stringBuilder.toString();
    }

    /**
     * 显示需要支付UI
     */
    protected void showNeedPayUI() {
        getPlayerController().setError(new PayError());
    }


    public void onRetryWatchClick() {
        getPlayerController().replay();
    }


    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        dispose();
    }

    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
        disposeGetCoupon();
        disposeReport();
        disposeCheck();
    }

    public void onPayClick() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        PayDetailModel payDetailModel = getPayDetailModel(playData);
        if (payDetailModel == null)
            return;
        CouponPackModel couponPackModel = getCoupon(payDetailModel);
        if (couponPackModel.couponModelList == null || couponPackModel.couponModelList.size() == 0) {
            return;
        }
        ThirdPayModel thirdPayModel = new ThirdPayModel();
        thirdPayModel.setPacksCoupons(couponPackModel.couponModelList);
        thirdPayModel.setCode(payDetailModel.getCode());
//        thirdPayModel.setWhaleyMoney(whaleyMoney);
        String content = "";
        String fromType = "";
        if (playData.getType() == 2) {
            fromType = "live";
            content = AppContextProvider.getInstance().getContext().getString(R.string.pay_live_content);
        } else {
            fromType = "recorded";
            if (payDetailModel.getDisableTimeDate() > 0) {
                content = AppContextProvider.getInstance().getContext().getString(R.string.pay_recorded_content) +
                        PayUtil.getDateFromMileSeconds(payDetailModel.getDisableTimeDate());
            }
        }
        thirdPayModel.setContent(content);
//        thirdPayModel.setDisplayMode(displayMode);
        thirdPayModel.setType(fromType);
        Bundle bundle = new Bundle();
        bundle.putString("key_pay_data", GsonUtil.getGson().toJson(thirdPayModel));
        bundle.putString("key_login_tips", AppContextProvider.getInstance().getContext().getString(R.string.pay_dialog_buy_pay));
        Router.getInstance().buildNavigation("/pay/ui/paydialog")
                //設置 starter
                .setStarter((Starter) getActivity())
                //設置 requestCode
                .with(bundle)
                .withInt(RouterConstants.KEY_ACTIVITY_TYPE, 0)
                .navigation();
    }

    public void onBackClick() {
        BackPressEvent backPressEvent = new BackPressEvent();
        emitEvent(backPressEvent);
    }

    private void getCouponInfo() {
        final PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        PayDetailModel payDetailModel = getPayDetailModel(playData);
        if (payDetailModel == null)
            return;
        final List<PackageModel> packageModelList = payDetailModel.getContentPackageQueryDtos();
        if (packageModelList == null
                || packageModelList.size() == 0
                || packageModelList.get(0) == null
                || packageModelList.get(0).getType() == 0
                || packageModelList.get(0).getCouponDto() == null) {
            return;
        }
        disposeGetCoupon();
        getCounponDisposable = getCouponInfo
                .buildUseCaseObservable(packageModelList.get(0).getCouponDto().getCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<CouponInfoResponse>() {
                    @Override
                    public void onNext(@NonNull CouponInfoResponse couponInfoResponse) {
                        CouponModel couponModel = couponInfoResponse.getData();
                        packageModelList.get(0).setCouponDto(couponModel);
                    }

                    @Override
                    public void onFinalError(Throwable e) {

                    }

                    @Override
                    public void onStatusError(int status, String message) {

                    }

                    @Override
                    public void onNoDataError() {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    protected void reportDevice() {
        disposeReport();
        reportDisposable = reportDevice.buildUseCaseObservable(null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<DeviceResponse>() {
                    @Override
                    public void onNext(@NonNull DeviceResponse deviceResponse) {

                    }

                    @Override
                    public void onComplete() {
                        isReportDevice = true;
                    }

                    @Override
                    public void onFinalError(Throwable e) {

                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        isReportDevice = true;
                    }

                    @Override
                    public void onNoDataError() {

                    }
                });
    }

    protected void checkDevice() {
        disposeCheck();
        checkDisposable = checkDevice.buildUseCaseObservable(null).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ErrorHandleObserver<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        if (!aBoolean) {
                            onLogout();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onFinalError(Throwable e) {

                    }

                    @Override
                    public void onStatusError(int status, String message) {
                        onLogout();
                    }

                    @Override
                    public void onNoDataError() {

                    }
                });
    }

    private void onLogout() {
        isPay = false;
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        playData.putCustomData("key_is_pay", false);
        getPlayerController().pause();
        Router.getInstance().buildExecutor("/user/service/signout").callback(new Executor.Callback<Boolean>() {
            @Override
            public void onCall(Boolean aBoolean) {

            }

            @Override
            public void onFail(Executor.ExecutionError executionError) {
            }
        }).excute();
        getUIAdapter().showLogout();
    }

    @Subscribe
    public void onKickOut(BaseEvent baseEvent) {
        if (baseEvent.getEventType().equals("kickOut")) {
            isPay = false;
            PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
            playData.putCustomData("key_is_pay", false);
            Router.getInstance().buildExecutor("/user/service/signout").callback(new Executor.Callback<Boolean>() {
                @Override
                public void onCall(Boolean aBoolean) {

                }

                @Override
                public void onFail(Executor.ExecutionError executionError) {
                }
            }).excute();
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }

    private void disposeGetCoupon() {
        if (getCounponDisposable != null) {
            getCounponDisposable.dispose();
            getCounponDisposable = null;
        }
    }

    private void disposeReport() {
        if (reportDisposable != null) {
            reportDisposable.dispose();
            reportDisposable = null;
        }
    }

    private void disposeCheck() {
        if (checkDisposable != null) {
            checkDisposable.dispose();
            checkDisposable = null;
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


    public interface IPayUIAdapter<CONTROLLER extends Component.Controller> extends Component.UIAdapter<CONTROLLER> {
        void changeNeedPayOnLandScape();

        void changeNeedPayOnPortrait();

        void showNeedPay();

        void hideNeedPay();

        void showPrompt();

        void hidePrompt();

        void changePromptText(String promptText);

        void showLogout();
    }
}
