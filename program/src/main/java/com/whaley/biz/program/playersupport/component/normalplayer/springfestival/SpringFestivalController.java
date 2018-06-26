package com.whaley.biz.program.playersupport.component.normalplayer.springfestival;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.exception.NotLoggedInErrorException;
import com.whaley.biz.common.exception.ResponseErrorException;
import com.whaley.biz.common.exception.StatusErrorThrowable;
import com.whaley.biz.common.interactor.CommonCMSFunction;
import com.whaley.biz.common.interactor.CommonFunction;
import com.whaley.biz.common.ui.TitleBarActivity;
import com.whaley.biz.common.utils.DialogUtil;
import com.whaley.biz.common.utils.ErrorHandleUtil;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.VideoPreparedEvent;
import com.whaley.biz.program.api.SpringFestivalApi;
import com.whaley.biz.program.constants.ProgramConstants;
import com.whaley.biz.program.model.BoxLeftCountModel;
import com.whaley.biz.program.model.DrawCardModel;
import com.whaley.biz.program.model.FestivalModel;
import com.whaley.biz.program.model.response.BoxLeftCountResponse;
import com.whaley.biz.program.model.response.DrawCardResponse;
import com.whaley.biz.program.model.response.FestivalResponse;
import com.whaley.biz.program.model.user.UserModel;
import com.whaley.biz.program.playersupport.event.CardResultExitEvent;
import com.whaley.biz.program.playersupport.event.CardSuccessEvent;
import com.whaley.biz.program.utils.FormatPageModel;
import com.whaley.biz.program.utils.GoPageUtil;
import com.whaley.core.appcontext.Starter;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.MD5Util;
import com.whaley.core.utils.NetworkUtils;
import com.whaleyvr.core.network.http.HttpManager;
import com.whaleyvr.core.network.http.exception.NetworkErrorException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dell on 2018/1/23.
 */

public class SpringFestivalController extends ControlController<SpringFestivalUIAdapter> {

    private SpringFestivalApi springFestivalApi;

    private Disposable countDownDisposable;

    private Disposable clickDisposable;

    private Disposable initDisposable;

    private Disposable lotteryClickDisposable;

    private Disposable checkDisposable;

    private Disposable getLeftDisposable;

    private long countDown = -1;

    boolean isChecked;

    boolean isLotteryOpened;

    CharSequence countDownStr;

    int leftCount;

    boolean inited;

    boolean isAttached;

    private final static int LOTTERY_INTERVAL = 60;

    FestivalModel festivalModel;

    public SpringFestivalController() {
        springFestivalApi = HttpManager.getInstance().getRetrofit(SpringFestivalApi.class).create(SpringFestivalApi.class);
    }

    @Subscribe(sticky = true)
    public void onVideoPrepared(VideoPreparedEvent videoPreparedEvent) {
        Router.getInstance().buildExecutor("/launch/service/getfestival")
                .notTransCallbackData()
                .notTransParam()
                .callback(new Executor.Callback<Boolean>() {
                    @Override
                    public void onCall(Boolean aBoolean) {
                        if (aBoolean)
                            checkValidate();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
    }

    @Override
    protected boolean isRegistOrientation() {
        return true;
    }

    private void checkValidate() {
        if (isChecked) {
            return;
        }
        isChecked = true;
        stopCheck();
        checkDisposable = springFestivalApi.findEventDetail()
                .map(new CommonCMSFunction<FestivalResponse, FestivalModel>())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FestivalModel>() {
                    @Override
                    public void onNext(@NonNull FestivalModel model) {
                        festivalModel = model;
                        modifyFestival(festivalModel);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        if (e instanceof StatusErrorThrowable || e instanceof ResponseErrorException) {
                            festivalModel = null;
                            modifyFestival(festivalModel);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void modifyFestival(FestivalModel festivalModel) {
        Router.getInstance().buildExecutor("/launch/service/modifyfestival").putObjParam(festivalModel)
                .notTransCallbackData()
                .callback(new Executor.Callback<Boolean>() {
                    @Override
                    public void onCall(Boolean aBoolean) {
                        ModuleEvent moduleEvent = new ModuleEvent("event/chunjie/activity", aBoolean);
                        if (aBoolean) {
                            emitEvent(moduleEvent);
                            attach();
                        }
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {

                    }
                })
                .excute();
    }

    private void attach() {
        isAttached = true;
        if (countDown > 0) {
            countDown();
        } else {
            initLottery();
        }
    }

    private void dettach() {
        stopGetLeft();
        stopCheck();
        stopClick();
        stopCountDown();
        stopLotteryClick();
        stopInitLottery();
    }

    private void openLotteryUI() {
        if (!isLotteryOpened) {
            isLotteryOpened = true;
            showUI(true);
        }
    }

    @Override
    protected void showUI(boolean anim) {
        if (isAttached && isLotteryOpened && isLandScape()) {
            super.showUI(anim);
        }
    }

    @Override
    protected void hideUI(boolean anim) {
        if (isAttached) {
            super.hideUI(anim);
        }
    }

    @Override
    protected void onSwitchToLandscape() {
        super.onSwitchToLandscape();
        showUI(true);
    }

    @Override
    protected void onSwitchToProtrait() {
        super.onSwitchToProtrait();
        hideUI(true);
    }

    private void goLogin() {
        TitleBarActivity.goPage((Starter) getActivity(), 0, "/user/ui/login");
    }

    /**
     * 当宝箱点击
     */
    public void onLotteryClick() {
        stopLotteryClick();
        lotteryClickDisposable = checkLogin()
                .doOnNext(new Consumer<UserModel>() {
                    @Override
                    public void accept(@NonNull UserModel userModel) throws Exception {
                        if (!isInited()) {
                            throw new NotInitedException("Not Inited");
                        }
                    }
                })
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
                        if (isOnCountDown) {
//                            getUIAdapter().showToast("抽奖倒计时未结束");
                            showDialog();
                            return;
                        }
                        if (leftCount <= 0) {
//                            getUIAdapter().showToast("今天的机会已经用尽");
                            showDialog();
                            return;
                        }
                        toLottery();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof NotLoggedInErrorException) {
                            showLoginDialog();
                        } else if (throwable instanceof NotInitedException) {
                            initLottery();
                        }
                    }
                });
    }

    private void showDialog() {
        if (getUIAdapter() != null) {
            getUIAdapter().showDialog();
        }
    }

    public void goWeb() {
        if (festivalModel != null) {
            GoPageUtil.goPage((Starter) getActivity(), FormatPageModel.getWebModel(festivalModel.getUrl(), festivalModel.getDisplayName()));
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
                                            e.onError(new NotLoggedInErrorException(executionError.getMessage()));
                                        }
                                    }
                                })
                                .excute();
                    }
                });
    }

    private Observable<Integer> getLeftCount() {
        return checkLogin()
                .observeOn(Schedulers.io())
                .flatMap(new Function<UserModel, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull UserModel userModel) throws Exception {
                        return springFestivalApi.getLeftCount(userModel.getAccount_id(), userModel.getAccessTokenModel().getAccesstoken())
                                .map(new CommonCMSFunction<BoxLeftCountResponse, BoxLeftCountModel>())
                                .map(new Function<BoxLeftCountModel, Integer>() {
                                    @Override
                                    public Integer apply(@NonNull BoxLeftCountModel boxLeftCountModel) throws Exception {
                                        leftCount = boxLeftCountModel.getLeftCount();
                                        return leftCount;
                                    }
                                });
                    }
                });
    }

    private void initLottery() {
        stopInitLottery();
        initDisposable = getLeftCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {
                        inited = true;
                        openLotteryUI();
                        if (integer > 0) {
                            countDown();
                        } else {
                            getUIAdapter().updateCountDown("今天的机会已\n经用完");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof NotLoggedInErrorException) {
                            leftCount = 0;
                            getUIAdapter().updateCountDown("登录后\nN张福卡可领取");
                            openLotteryUI();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<DrawCardModel> lottery() {
        return checkLogin()
                .observeOn(Schedulers.io())
                .flatMap(new Function<UserModel, ObservableSource<DrawCardModel>>() {
                    @Override
                    public ObservableSource<DrawCardModel> apply(@NonNull UserModel userModel) throws Exception {
                        String ts = String.valueOf(System.currentTimeMillis());
                        String sign = MD5Util.getMD5String(SpringFestivalApi.EVENT_CODE + ts + userModel.getAccount_id());
                        return springFestivalApi.drawCard(userModel.getAccount_id(), userModel.getAccessTokenModel().getAccesstoken(),
                                userModel.getMobile(), SpringFestivalApi.EVENT_CODE, ts, sign)
                                .map(new CommonFunction<DrawCardResponse, DrawCardModel>());
                    }
                });
    }

    private void toLottery() {
        if (!NetworkUtils.isNetworkAvailable()) {
            getUIAdapter().showToast("当前网络不可用，请检查网络后重试");
            return;
        }
        if (clickDisposable != null && !clickDisposable.isDisposed()) {
            return;
        }
        stopClick();
        clickDisposable = lottery()
                .observeOn(Schedulers.io())
                .map(new Function<DrawCardModel, LotteryCountDownModel>() {
                    @Override
                    public LotteryCountDownModel apply(@NonNull DrawCardModel drawCardModel) throws Exception {
                        LotteryCountDownModel lotteryCountDownModel = new LotteryCountDownModel();
                        lotteryCountDownModel.setSuccess(true);
                        CardSuccessEvent event = new CardSuccessEvent();
                        event.setFinished(drawCardModel.getFinished());
                        event.setName(drawCardModel.getRelCardName());
                        event.setTotleName(drawCardModel.getRelCardsName());
//                        event.setLeftCount(--leftCount);
                        event.setType(drawCardModel.getRelCardGrpType());
                        event.setTotalCount(drawCardModel.getRelCardsCnt());
                        lotteryCountDownModel.setCardSuccessEvent(event);
                        lotteryCountDownModel.setCountDownSeconds(LOTTERY_INTERVAL);
                        return lotteryCountDownModel;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, LotteryCountDownModel>() {
                    @Override
                    public LotteryCountDownModel apply(@NonNull Throwable throwable) throws Exception {
                        String toast = null;
                        LotteryCountDownModel lotteryCountDownModel = new LotteryCountDownModel();
                        if (throwable instanceof StatusErrorThrowable) {
                            StatusErrorThrowable statusErrorThrowable = (StatusErrorThrowable) throwable;
                            switch (statusErrorThrowable.getStatus()) {
                                case 500:
                                    toast = "系统异常";
                                    break;
                                case 507:
                                    leftCount = 0;
                                    toast = "今天的机会已经用完";
                                    break;
                                case 510:
                                    leftCount = 0;
                                    toast = "活动已结束";
                                    break;
                            }
                            lotteryCountDownModel.setCode(statusErrorThrowable.getStatus());
                        } else {
                            Throwable error = ErrorHandleUtil.getFormatThrowable(throwable);
                            if (error == null || !(error instanceof NetworkErrorException)) {
                                //
                            } else {
                                toast = error.getMessage();
                            }
                        }
                        lotteryCountDownModel.setThrowable(throwable);
                        lotteryCountDownModel.setSuccess(false);
                        lotteryCountDownModel.setFailTime(formatNextTime(LOTTERY_INTERVAL));
                        lotteryCountDownModel.setCountDownSeconds(LOTTERY_INTERVAL);
                        lotteryCountDownModel.setToast(toast);
                        return lotteryCountDownModel;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LotteryCountDownModel>() {
                    @Override
                    public void onNext(@NonNull LotteryCountDownModel lotteryCountDownModel) {
                        reFetchLeftCount(lotteryCountDownModel);
//                        if(!lotteryCountDownModel.isSucess){
//                            getUIAdapter().showToast(lotteryCountDownModel.getToast());
//                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        resetCountDown();
                        dispose();
                    }

                    @Override
                    public void onComplete() {
                        //
                    }
                });
    }

    private void reFetchLeftCount(final LotteryCountDownModel lotteryCountDownModel) {
        stopGetLeft();
        getLeftDisposable = getLeftCount()
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(@NonNull Integer integer) {
                        handleResult(lotteryCountDownModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        --leftCount;
                        handleResult(lotteryCountDownModel);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void handleResult(LotteryCountDownModel lotteryCountDownModel) {
        if (lotteryCountDownModel.getToast() != null) {
            getUIAdapter().showToast(lotteryCountDownModel.getToast());
        }
        if (lotteryCountDownModel.isSucess()) {
            if (leftCount <= 0) {
                getUIAdapter().updateCountDown("今天的机会已\n经用完");
            } else {
                getUIAdapter().resetLotteryable();
            }
            lotteryCountDownModel.getCardSuccessEvent().setLeftCount(leftCount);
            emitEvent(lotteryCountDownModel.getCardSuccessEvent());
            stopCountDown();
        } else {
            if (lotteryCountDownModel.getCode() == 510) {
                modifyFestival(null);
                getUIAdapter().removeBox();
                stopCountDown();
            } else if (leftCount <= 0) {
                getUIAdapter().updateCountDown("今天的机会已\n经用完");
                stopCountDown();
            } else {
                resetCountDown();
            }
        }
        stopClick();
    }

    private void resetCountDown() {
        countDown = LOTTERY_INTERVAL;
        countDown();
    }

    private boolean isInited() {
        return inited;
    }

    @Subscribe
    public void onExitCardResult(CardResultExitEvent cardResultExitEvent) {
        if (isAttached && leftCount > 0) {
            resetCountDown();
        }
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

    private void countDown() {
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
                        return buildCountDown(LOTTERY_INTERVAL);
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
                            getUIAdapter().updateToLotteryable();
                        } else {
                            countDown = countDownModel.getCountDown();
                            countDownStr = countDownModel.getCountDowncountDownText();
                            getUIAdapter().updateCountDown(countDownStr);
                        }
                    }
                });
    }

    private void stopGetLeft() {
        if (getLeftDisposable != null) {
            getLeftDisposable.dispose();
            getLeftDisposable = null;
        }
    }

    private void stopLotteryClick() {
        if (lotteryClickDisposable != null) {
            lotteryClickDisposable.dispose();
            lotteryClickDisposable = null;
        }
    }

    private void stopCheck() {
        if (checkDisposable != null) {
            checkDisposable.dispose();
            checkDisposable = null;
        }
    }

    private void stopInitLottery() {
        if (initDisposable != null) {
            initDisposable.dispose();
            initDisposable = null;
        }
    }

    private void stopCountDown() {
        if (countDownDisposable != null) {
            countDownDisposable.dispose();
            countDownDisposable = null;
        }
    }

    private void stopClick() {
        if (clickDisposable != null) {
            clickDisposable.dispose();
            clickDisposable = null;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case ProgramConstants.EVENT_LOGIN_SUCCESS:
                if (isAttached) {
                    if (getUIAdapter() != null) {
                        getUIAdapter().resetLotteryable();
                    }
                    initLottery();
                }
                break;
            case "activityShareCall":
                if (isAttached && event.getObject() != null) {
                    leftCount = event.getObject();
                }
                break;
        }
    }

    private SpannableString formatCountDown(long countDown) {
        String timeString = getFormatTime((int) countDown);
        SpannableString ss = new SpannableString("离下次送福卡\n还剩" + timeString);
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

    @Override
    protected void onDestory() {
        super.onDestory();
        dettach();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    class LotteryCountDownModel extends CountDownModel {
        private boolean isSucess;
        private CardSuccessEvent cardSuccessEvent;
        private String failTime;
        private String toast;
        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setFailTime(String failTime) {
            this.failTime = failTime;
        }

        public String getFailTime() {
            return failTime;
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

        public CardSuccessEvent getCardSuccessEvent() {
            return cardSuccessEvent;
        }

        public void setCardSuccessEvent(CardSuccessEvent cardSuccessEvent) {
            this.cardSuccessEvent = cardSuccessEvent;
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

    public static class NotInitedException extends Exception {

        public NotInitedException(String msg) {
            super(msg);
        }
    }

    @Override
    public void registEvents() {
        super.registEvents();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

}

