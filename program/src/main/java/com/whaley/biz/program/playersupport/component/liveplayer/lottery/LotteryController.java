package com.whaley.biz.program.playersupport.component.liveplayer.lottery;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.ResponseFunction;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.ErrorHandleUtil;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.biz.program.api.BoxApi;
import com.whaley.biz.program.constants.PlayerDataConstants;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.playersupport.event.LotteryFailEvent;
import com.whaley.biz.program.playersupport.event.LotterySuccessEvent;
import com.whaley.biz.program.playersupport.event.LotterySwitchEvent;
import com.whaley.biz.program.model.response.BoxAuthResponse;
import com.whaley.biz.program.model.response.BoxLotteryResponse;
import com.whaley.biz.program.model.response.BoxTimeResponse;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;
import com.whaley.core.utils.NetworkUtils;
import com.whaleyvr.core.network.http.HttpManager;
import com.whaleyvr.core.network.http.exception.NetworkErrorException;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangZhi on 2017/8/10 17:55.
 */

public class LotteryController extends ControlController<LotteryUIAdapter> {

    private static final int TOP_MARGIN_LANSCAPE = DisplayUtil.convertDIP2PX(151);

    private static final int TOP_MARGIN_PROTRAIT = DisplayUtil.convertDIP2PX(86);


    private boolean isAuth;

    private BoxApi boxApi;

    private Disposable countDownDisposable;

    private String attachedSid;

    private Disposable lotteryClickDisposable;

    private boolean isLotteryOpened;

    private boolean isAttached;
    private long countDown = -1;

    static final String SHOW_DANMU_EDIT_EVENT = "/program/event/showdanmuedit";

    static final String HIDE_DANMU_EDIT_EVENT = "/program/event/hidedanmuedit";

    static final String HIDE_GIFT_EDIT_EVENT = "/livegift/event/gifthide";

    static final String SHOW_GIFT_EDIT_EVENT = "/program/event/showlivegift";

    static final String SHOW_GIFT_EVENT = "/program/event/livegift";


    boolean isDanmuEditOnShow;

    boolean isUserClosed;

    boolean isGiftTempOnShow;

    boolean isLandScape;

    boolean isGift;

    boolean isLotteryable;

    CharSequence countDownStr;

    CountDownModel countDownModel;

    public LotteryController() {
        boxApi = HttpManager.getInstance().getRetrofit(BoxApi.class).create(BoxApi.class);
    }


    public boolean isRight() {
        if (isGift && isLandScape) {
            return true;
        } else {
            return false;
        }
    }

    @Subscribe
    public void onDanmuEditVisibleChangeEvent(ModuleEvent event) {
        String eventName = event.getEventName();
        switch (eventName) {
            case SHOW_DANMU_EDIT_EVENT:
                isDanmuEditOnShow = true;
                getUIAdapter().hide(true);
                break;
            case HIDE_DANMU_EDIT_EVENT:
                isDanmuEditOnShow = false;
                if (!isHide() && isLotteryOpened()) {
                    getUIAdapter().show(true);
                }
                break;
            case SHOW_GIFT_EDIT_EVENT:
                isGiftTempOnShow = true;
                getUIAdapter().hide(true);
                break;
            case HIDE_GIFT_EDIT_EVENT:
                isGiftTempOnShow = false;
                if (!isHide() && isLotteryOpened()) {
                    getUIAdapter().show(true);
                }
                break;
            case SHOW_GIFT_EVENT:
                isGift = true;
                if (isLandScape) {
                    getUIAdapter().updateRightAlignment();
                    getUIAdapter().updateTopMargin(TOP_MARGIN_LANSCAPE);
                } else {
                    getUIAdapter().updateTopMargin(TOP_MARGIN_PROTRAIT);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        dettach();
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        PlayData playData = videoPreparedEvent.getPlayData();
        isLandScape = playData.getBooleanCustomData(PlayerDataConstants.ORIENTATION_IS_LANDSCAPE);
    }

    @Subscribe
    public void onLotterySwitchEvent(LotterySwitchEvent lotterySwitchEvent) {
        if (lotterySwitchEvent.isEnable()) {
            openLottery();
        } else {
            closeLottery();
        }
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if (isLotteryOpened()) {
            openLotteryUI();
        } else {
            closeLotteryUI();
        }
    }

    private void attach() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        String sid = playData.getId();
        if (isAttached && attachedSid != null && attachedSid.equals(sid)) {
            return;
        }
        dettach();
        attachedSid = sid;
        countDown(sid);
    }

    private void dettach() {
        stopCountDown();
        stopLotteryClick();
        attachedSid = null;
        isAttached = false;
    }


    public boolean isLotteryOpened() {
        return isLotteryOpened;
    }

    private void openLottery() {
        if (isUserClosed)
            return;
        if (isLotteryOpened()) {
            if (countDown != 0) {
                countDown(attachedSid);
            }
            return;
        }
        isLotteryOpened = true;
        if (!isViewCreated())
            return;
        attach();

    }

    private void openLotteryUI() {
        if (isDanmuEditOnShow || isGiftTempOnShow) {
            return;
        }
        showUI(true);
    }

    private void closeLottery() {
        if (!isLotteryOpened())
            return;
        isLotteryOpened = false;
        if (!isViewCreated())
            return;
        closeLotteryUI();
    }

    private void closeLotteryUI() {
        hideUI(true);
    }

    @Override
    protected void showUI(boolean anim) {
        if (isLotteryOpened) {
            super.showUI(anim);
        }
    }

    @Override
    protected void hideUI(boolean anim) {
        super.hideUI(anim);
    }

    private void goLogin() {
        TitleBarActivity.goPage((Starter) getActivity(), 0, "/user/ui/login");
    }


    private void showPayDialog() {
        Toast.makeText(getContext(), "试看时不能参与抽奖活动", Toast.LENGTH_SHORT).show();
//        DialogUtil.showDialog(getActivity(), "请购买该直播后参与宝箱抽奖活动", new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModuleEvent moduleEvent = new ModuleEvent("pay_program", null);
//                emitEvent(moduleEvent);
//            }
//        }, null);
    }

    public void onLotteryCloseClick() {
        isUserClosed = true;
        closeLottery();
    }

    /**
     * 当宝箱点击
     */
    public void onLotteryClick() {
        stopLotteryClick();
        lotteryClickDisposable = checkLogin()
                .map(new Function<UserModel, Boolean>() {
                    //检查是否处于倒计时
                    @Override
                    public Boolean apply(@NonNull UserModel userModel) throws Exception {
                        return countDownDisposable != null && !countDownDisposable.isDisposed();
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean isOnCountDown) throws Exception {
                        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
                        if (playData != null) {
                            boolean isFreeTime = playData.getBooleanCustomData(PlayerDataConstants.IS_FREE_TIME);
                            if (isFreeTime) {
                                showPayDialog();
                                return;
                            }
                        }
                        if (isOnCountDown) {
                            getUIAdapter().showToast("抽奖倒计时未结束");
                            return;
                        }
                        toLottery(attachedSid);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        showLoginDialog();
                    }
                });
    }

    private void stopLotteryClick() {
        if (lotteryClickDisposable != null) {
            lotteryClickDisposable.dispose();
            lotteryClickDisposable = null;
        }
    }

    private Observable<UserModel> checkLogin() {
        return Observable
                // 获取用户登录数据
                .create(new ObservableOnSubscribe<UserModel>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<UserModel> e) throws Exception {
                        Router.getInstance().buildExecutor("/user/service/checklogin")
                                .callback(new Executor.Callback<UserModel>() {
                                    @Override
                                    public void onCall(UserModel userModel) {
                                        if (!e.isDisposed()) {
                                            e.onNext(userModel);
                                        }
                                    }

                                    @Override
                                    public void onFail(Executor.ExecutionError executionError) {
                                        if (!e.isDisposed()) {
                                            e.onError(executionError);
                                        }
                                    }
                                })
                                .excute();
                    }
                });
    }

    private Observable<BoxAuthResponse> auth(UserModel userModel) {
        return boxApi.auth(userModel.getAccessTokenModel().getAccesstoken(), userModel.getDeviceId(), System.currentTimeMillis())
                .map(new ResponseFunction<BoxAuthResponse, BoxAuthResponse>())
                // 保存已登录
                .doOnNext(new Consumer<BoxAuthResponse>() {
                    @Override
                    public void accept(@NonNull BoxAuthResponse boxAuthResponse) throws Exception {
                        isAuth = true;
                    }
                });
    }

    private Observable<BoxTimeResponse> getBoxTime(final String sid) {
        return Observable
                // 判断是否登录宝箱
                .just(isAuth())
                // 获得宝箱倒计时时间的 Response
                .observeOn(Schedulers.io())
                .flatMap(new Function<Boolean, ObservableSource<BoxTimeResponse>>() {
                    @Override
                    public ObservableSource<BoxTimeResponse> apply(@NonNull Boolean isAuth) throws Exception {
                        if (isAuth) {
                            return boxTime(sid);
                        }
                        return checkLogin()
                                .observeOn(Schedulers.io())
                                .flatMap(new Function<UserModel, ObservableSource<BoxAuthResponse>>() {
                                    @Override
                                    public ObservableSource<BoxAuthResponse> apply(@NonNull UserModel userModel) throws Exception {
                                        return auth(userModel);
                                    }
                                })
                                .flatMap(new Function<BoxAuthResponse, ObservableSource<BoxTimeResponse>>() {
                                    @Override
                                    public ObservableSource<BoxTimeResponse> apply(@NonNull BoxAuthResponse boxAuthResponse) throws Exception {
                                        return boxTime(sid);
                                    }
                                });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<BoxTimeResponse>() {
                    @Override
                    public void accept(@NonNull BoxTimeResponse boxTimeResponse) throws Exception {
                        openLotteryUI();
                    }
                });
    }

    private Observable<BoxTimeResponse> boxTime(String sid) {
        return boxApi.time(System.currentTimeMillis(), BoxApi.ACTION_TYPE, sid)
                .map(new ResponseFunction<BoxTimeResponse, BoxTimeResponse>());
    }


    private void toLottery(final String sid) {
        if (!NetworkUtils.isNetworkAvailable()) {
            getUIAdapter().showToast("当前网络不可用，请检查网络后重试");
            return;
        }
        stopCountDown();
        countDownDisposable = lotteryCheckAuth(sid)
                .map(new Function<BoxLotteryResponse, LotteryCountDownModel>() {
                    @Override
                    public LotteryCountDownModel apply(@NonNull BoxLotteryResponse boxLotteryResponse) throws Exception {
                        LotteryCountDownModel lotteryCountDownModel = new LotteryCountDownModel();
                        lotteryCountDownModel.setSuccess(true);
                        LotterySuccessEvent event = new LotterySuccessEvent();
                        event.setTime(formatNextTime(boxLotteryResponse.getCountdown()));
                        event.setName(boxLotteryResponse.getPrizedata().getName());
                        event.setPicUrl(boxLotteryResponse.getPrizedata().getPicture());
                        lotteryCountDownModel.setLotterySuccessEvent(event);
                        lotteryCountDownModel.setCountDownSeconds(boxLotteryResponse.getCountdown());
                        return lotteryCountDownModel;
                    }
                })
                .onErrorReturn(new Function<Throwable, LotteryCountDownModel>() {
                    @Override
                    public LotteryCountDownModel apply(@NonNull Throwable throwable) throws Exception {
                        int countDownSeconds = 0;
                        String toast = null;
                        if (throwable instanceof StatusErrorThrowable) {
                            BoxLotteryResponse boxLotteryResponse = ((BoxLotteryResponse) ((StatusErrorThrowable) throwable).getData());
                            switch (boxLotteryResponse.getStatus()) {
                                case -2005:
                                    toast = "抽奖未开始";
                                    break;
                                case -2006:
                                    toast = "抽奖已结束";
                                    break;
                                default:
                                    countDownSeconds = boxLotteryResponse.getCountdown();
                                    break;
                            }
                        } else {
                            Throwable error = ErrorHandleUtil.getFormatThrowable(throwable);
                            if (error == null || !(error instanceof NetworkErrorException)) {
                                countDownSeconds = 10;
                            } else {
                                toast = error.getMessage();
                            }
                        }
                        LotteryCountDownModel lotteryCountDownModel = new LotteryCountDownModel();
                        lotteryCountDownModel.setThrowable(throwable);
                        lotteryCountDownModel.setSuccess(false);
                        lotteryCountDownModel.setFailTime(formatNextTime(countDownSeconds));
                        lotteryCountDownModel.setCountDownSeconds(countDownSeconds);
                        lotteryCountDownModel.setToast(toast);
                        return lotteryCountDownModel;
                    }
                })
                .flatMap(new Function<LotteryCountDownModel, ObservableSource<LotteryCountDownModel>>() {
                    @Override
                    public ObservableSource<LotteryCountDownModel> apply(@NonNull LotteryCountDownModel countDownModel) throws Exception {
                        return buildCountDown(countDownModel);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LotteryCountDownModel>() {
                    @Override
                    public void accept(@NonNull LotteryCountDownModel lotteryCountDownModel) throws Exception {
                        if (lotteryCountDownModel.isFinalNext()) {
                            stopCountDown();
                            countDown = lotteryCountDownModel.getCountDown();
                            isLotteryable = true;
                            getUIAdapter().updateToLotteryable();
                        } else {
                            isLotteryable = false;
                            countDown = lotteryCountDownModel.getCountDown();
                            countDownStr = lotteryCountDownModel.getCountDowncountDownText();
                            getUIAdapter().updateCountDown(countDownStr);
                        }
                        if (!lotteryCountDownModel.isFirstNext()) {
                            return;
                        }
                        if (lotteryCountDownModel.isSucess()) {
                            emitEvent(lotteryCountDownModel.getLotterySuccessEvent());
                            return;
                        }
                        if (lotteryCountDownModel.getToast() != null) {
                            getUIAdapter().showToast(lotteryCountDownModel.getToast());
                            return;
                        }
                        LotteryFailEvent lotteryFailEvent = new LotteryFailEvent();
                        lotteryFailEvent.setTime(lotteryCountDownModel.getFailTime());
                        emitEvent(lotteryFailEvent);
                    }
                });
    }

    private String formatNextTime(int countdown) {
        return "下次抽奖时间在" + getFormatTime(countdown) + "钟之后";
    }

    private void showLoginDialog() {
        DialogUtil.showDialog(getActivity(), "您需要登录才能进行相关操作\n确定登录吗", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLogin();
            }
        }, null);
    }

    private Observable<BoxLotteryResponse> lotteryCheckAuth(final String sid) {
        return Observable.just(isAuth())
                .flatMap(new Function<Boolean, ObservableSource<BoxLotteryResponse>>() {
                    @Override
                    public ObservableSource<BoxLotteryResponse> apply(@NonNull Boolean isAuth) throws Exception {
                        if (isAuth) {
                            return lottery(sid);
                        }
                        return checkLogin()
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnError(new Consumer<Throwable>() {
                                    @Override
                                    public void accept(@NonNull Throwable throwable) throws Exception {
                                        showLoginDialog();
                                    }
                                })
                                .observeOn(Schedulers.io())
                                .flatMap(new Function<UserModel, ObservableSource<BoxAuthResponse>>() {
                                    @Override
                                    public ObservableSource<BoxAuthResponse> apply(@NonNull UserModel userModel) throws Exception {
                                        return auth(userModel);
                                    }
                                })
                                .flatMap(new Function<BoxAuthResponse, ObservableSource<BoxLotteryResponse>>() {
                                    @Override
                                    public ObservableSource<BoxLotteryResponse> apply(@NonNull BoxAuthResponse boxAuthResponse) throws Exception {
                                        return lottery(sid);
                                    }
                                });
                    }
                });
    }

    private Observable<BoxLotteryResponse> lottery(String sid) {
        return boxApi.lottery(System.currentTimeMillis(), BoxApi.ACTION_TYPE, sid)
                .subscribeOn(Schedulers.io())
                .map(new ResponseFunction<BoxLotteryResponse, BoxLotteryResponse>());
    }

    private void countDown(final String sid) {
        stopCountDown();
        countDownDisposable = Observable
                // 检查网络
                .just(NetworkUtils.isNetworkAvailable())
                // 获取倒计时时间
                .flatMap(new Function<Boolean, ObservableSource<CountDownModel>>() {
                    @Override
                    public ObservableSource<CountDownModel> apply(@NonNull Boolean isNetworkAvailable) throws Exception {
                        if (countDown > 0) {
                            return buildCountDown((int) countDown);
                        }
                        if (!isNetworkAvailable) {
                            return buildCountDown(10);
                        }
                        return getBoxTime(sid)
                                .flatMap(new Function<BoxTimeResponse, ObservableSource<CountDownModel>>() {
                                    @Override
                                    public ObservableSource<CountDownModel> apply(@NonNull BoxTimeResponse boxTimeResponse) throws Exception {
                                        return buildCountDown(boxTimeResponse.getCountdown());
                                    }
                                })
                                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends CountDownModel>>() {
                                    @Override
                                    public ObservableSource<? extends CountDownModel> apply(@NonNull Throwable throwable) throws Exception {
                                        return buildCountDown(10);
                                    }
                                });
//                                .onErrorResumeNext();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // 更新 UI
                .subscribe(new Consumer<CountDownModel>() {
                    @Override
                    public void accept(@NonNull CountDownModel countDownModel) throws Exception {
                        if (countDownModel.isFinalNext()) {
                            countDown = countDownModel.getCountDown();
                            stopCountDown();
                            isLotteryable = true;
                            getUIAdapter().updateToLotteryable();
                        } else {
                            countDown = countDownModel.getCountDown();
                            isLotteryable = false;
                            countDownStr = countDownModel.getCountDowncountDownText();
                            getUIAdapter().updateCountDown(countDownStr);
                        }
                    }
                });
    }

    private void stopCountDown() {
        if (countDownDisposable != null) {
            countDownDisposable.dispose();
            countDownDisposable = null;
        }
    }

    private <R extends CountDownModel> Observable<R> buildCountDown(final long countdownTimeSeconds) {
        CountDownModel countDownModel = new CountDownModel();
        countDownModel.setCountDownSeconds(countdownTimeSeconds);
        return buildCountDown(countDownModel);
    }


    private <R extends CountDownModel> Observable<R> buildCountDown(final CountDownModel countDownModel) {
        return Observable.intervalRange(0, countDownModel.getCountDownSeconds() + 1, 0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, R>() {
                    @Override
                    public R apply(@NonNull Long duration) throws Exception {
                        CountDownModel countDown = countDownModel;
                        if (duration > 0) {
                            countDown.setFirstNext(false);
                        }
                        countDown.setCountDown(countDownModel.getCountDownSeconds() - duration);
                        countDown.setCountDownText(formatCountDown(countDown.getCountDown()));
                        if (countDown.getCountDown() <= 0) {
                            countDown.setFinalNext(true);
                        }
                        return (R) countDown;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    private SpannableString formatCountDown(long countDown) {
        String timeString = getFormatTime((int) countDown);
        SpannableString ss = new SpannableString("离下次抽奖\n还剩" + timeString);
        ss.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(com.whaley.biz.common.R.color.color14)), ss.length() - timeString.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }


    /**
     * @param time
     * @return 根据秒计算时长
     * @author qxw
     * @time 2017/2/17 20:03
     */
    public static String getFormatTime(int time) {
        if (time < 60) {
            return time + "秒";
        }
        int hour = 0;
        int minute = time / 60;
        int second = time % 60;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String timeString = minute + "分" + second + "秒";
        if (hour != 0) {
            timeString = hour + "时" + timeString;
        }
        return timeString;
    }


    public boolean isAuth() {
        return isAuth;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
        stopLotteryClick();
        stopCountDown();
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        onDispose();
    }

    class LotteryCountDownModel extends CountDownModel {
        private boolean isSucess;
        private LotterySuccessEvent lotterySuccessEvent;
        private String failTime;
        private String toast;


        public void setFailTime(String failTime) {
            this.failTime = failTime;
        }

        public String getFailTime() {
            return failTime;
        }

        public LotterySuccessEvent getLotterySuccessEvent() {
            return lotterySuccessEvent;
        }

        public void setLotterySuccessEvent(LotterySuccessEvent lotterySuccessEvent) {
            this.lotterySuccessEvent = lotterySuccessEvent;
        }

        public boolean isSucess() {
            return isSucess;
        }

        public void setSuccess(boolean sucess) {
            isSucess = sucess;
        }

        public void setToast(String toast) {
            this.toast = toast;
        }

        public String getToast() {
            return toast;
        }
    }

    class CountDownModel {
        private Throwable throwable;

        private long countDown;

        private CharSequence countDownText;

        private long countDownSeconds;

        private boolean isFirstNext = true;

        private boolean isFinalNext = false;

        public void setCountDown(long countDown) {
            this.countDown = countDown;
        }

        public void setCountDownText(CharSequence countDownText) {
            this.countDownText = countDownText;
        }

        public void setThrowable(Throwable throwable) {
            this.throwable = throwable;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public long getCountDown() {
            return countDown;
        }

        public CharSequence getCountDowncountDownText() {
            return countDownText;
        }

        public void setCountDownSeconds(long countDownSeconds) {
            this.countDownSeconds = countDownSeconds;
        }

        public long getCountDownSeconds() {
            return countDownSeconds;
        }

        public boolean isFirstNext() {
            return isFirstNext;
        }

        public void setFirstNext(boolean firstNext) {
            isFirstNext = firstNext;
        }

        public void setFinalNext(boolean finalNext) {
            isFinalNext = finalNext;
        }

        public boolean isFinalNext() {
            return isFinalNext;
        }
    }
}
