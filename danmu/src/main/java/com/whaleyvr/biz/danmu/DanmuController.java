package com.whaleyvr.biz.danmu;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.animation.LinearInterpolator;

import com.whaley.biz.common.CommonConstants;
import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.sign.SignType;
import com.whaley.biz.sign.SignUtil;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.repository.IRepositoryManager;
import com.whaley.core.repository.RepositoryManager;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaley.core.utils.NetworkUtils;
import com.whaleyvr.biz.danmu.model.BannedModel;
import com.whaleyvr.biz.danmu.model.CommentDataBean;
import com.whaleyvr.biz.danmu.model.DmComitModel;
import com.whaleyvr.biz.danmu.model.DmDataBean;
import com.whaleyvr.biz.danmu.model.DmModel;
import com.whaleyvr.biz.danmu.model.NoticeModel;
import com.whaleyvr.biz.danmu.model.UserModel;
import com.whaleyvr.biz.danmu.repository.DanmuRepository;
import com.whaleyvr.biz.danmu.response.AuthResponse;
import com.whaleyvr.biz.danmu.response.ComeInReponse;
import com.whaleyvr.biz.danmu.response.DmComitResponse;
import com.whaleyvr.biz.danmu.interactor.UseAuth;
import com.whaleyvr.biz.danmu.interactor.ComeIn;
import com.whaleyvr.biz.danmu.interactor.ComitDm;
import com.whaleyvr.biz.danmu.interactor.GuestAuth;
import com.whaleyvr.core.network.socketio.EventListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

import static com.whaleyvr.biz.danmu.Constants.*;

public class DanmuController implements CommonConstants {

    private static final long CACHE_DANMU_SENED_TIME = 1000 * 40;

    private static final int MAX_SEND_SIZE_ONCE = 50;

    private DanmuListener danmuListener;


    private ComitDm comitDmUseCase;

    private DanmuRepository repository;

    private IRepositoryManager repositoryManager;

    private ValueAnimator danmuAnimator;
    private DanMuSocketBizManager danMuSocketBizManager;

    public DanmuController() {
        repository = new DanmuRepository();
        repositoryManager = RepositoryManager.create(getDanmuRepository());
        comitDmUseCase = new ComitDm(repositoryManager, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public DanmuRepository getDanmuRepository() {
        return repository;
    }

    public void init(String code, String title) {
        getDanmuRepository().setCode(code);
        getDanmuRepository().setTitle(title);
        danMuSocketBizManager = new DanMuSocketBizManager(code, title);
        danMuSocketBizManager.setDanMuSocketListener(new DanMuSocketBizManager.DanMuSocketListener() {
            @Override
            public void verifyToken() {
                getDanmuRepository().setLoginStatus(DanmuRepository.LOGINING);
            }

            @Override
            public void verifyTokened(String uid) {
                getDanmuRepository().setUid(uid);
            }

            @Override
            public void onLoginFailed() {
                getDanmuRepository().setLoginStatus(DanmuRepository.NOT_LOGIN);
            }

            @Override
            public void onLogined() {
                getDanmuRepository().setLoginStatus(DanmuRepository.LOGINED);

            }

            @Override
            public void onDanmaku(String danmaku) {
                DmDataBean danmuDataBean = GsonUtil.getGson().fromJson(danmaku, DmDataBean.class);
                if (danmuDataBean != null) {
                    CommentDataBean commentDataBean = danmuDataBean.getDanmakudata();
                    if (commentDataBean != null) {
                        toRefreshDM(commentDataBean);
                    }
                }
            }

            @Override
            public void onUserBanned(String userBanned) {
                BannedModel bannedModel = GsonUtil.getGson().fromJson(userBanned, BannedModel.class);
                if (bannedModel != null) {
                    toRefreshDM(bannedModel);
                }
            }

            @Override
            public void onNotice(String notice) {
                NoticeModel noticeModel = GsonUtil.getGson().fromJson(notice, NoticeModel.class);
                if (noticeModel != null) {
                    sendDanmuTop(noticeModel);
                }
            }

            @Override
            public void onDisNotice() {
                stopDanmuCountDown();
                if (danmuListener != null) {
                    danmuListener.clearMsgTop();
                }
            }
        });
        danMuSocketBizManager.join();
        danMuSocketBizManager.onJoin();
    }

    public void destroy() {
//        stopPolling();
        stopDanmuCountDown();
        if (danmuListener != null) {
            danmuListener = null;
        }
        if (danMuSocketBizManager != null) {
            danMuSocketBizManager.onAllQuit();
        }
//        if (getDanmuRepository().getDanmuSocketManager() != null) {
//            getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_DANMAKU);
//            getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_USERBANNED);
//            getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_NOTICE);
//            getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_DISNOTICE);
//            getDanmuRepository().getDanmuSocketManager().closeSocket();
//            getDanmuRepository().setDanmuSocketManager(null);
//        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(BaseEvent event) {
//        switch (event.getEventType()) {
//            case "login_success":
//                verifyToken();
//                break;
//        }
//    }

    public void setDanmuListener(DanmuListener danmuListener) {
        this.danmuListener = danmuListener;
    }

    public DanmuListener getDanmuListener() {
        return danmuListener;
    }

    private void sendDanMuList(List<DanMu> danMuList) {
        if (danmuListener != null)
            danmuListener.sendMsg(danMuList);
    }

    private void checkSenedDanMuTime() {
        long currentTime = System.currentTimeMillis();
        for (String key : getDanmuRepository().getSendedDanMuMap().keySet()) {
            DanMu danMu = getDanmuRepository().getSendedDanMuMap().get(key);
            long duration = currentTime - danMu.getRefreshTime();
            if (duration > CACHE_DANMU_SENED_TIME) {
                getDanmuRepository().getSendedDanMuMap().remove(key);
            }
        }
    }

    private void sendDanMu() {
        List<DanMu> danMuList = new ArrayList<>();
        for (int i = 0, j = MAX_SEND_SIZE_ONCE; i < j; i++) {
            DanMu danMu = getDanmuRepository().getDanMuQueue().poll();
            if (danMu != null) {
                if (!getDanmuRepository().getSendedDanMuMap().containsKey(danMu.getUid())) {
                    danMu.setContentSpannable(getContent(danMu));
                    danMuList.add(danMu);
                    getDanmuRepository().getSendedDanMuMap().put(danMu.getUid(), danMu);
                }
            } else {
                break;
            }
        }
        if (danMuList.size() > 0) {
            sendDanMuList(danMuList);
        }
    }

    private SpannableString getContent(DanMu danMu) {
        SpannableString ss;
        String name = danMu.nickname;
        String content = danMu.content;
        if (DANMU_TYPE_ADMINISTRATOR.equals(danMu.getType()) ||
                DANMU_TYPE_BANNED.equals(danMu.getType())) {
            ss = new SpannableString(content);
            ss.setSpan(new ForegroundColorSpan(Color.parseColor(danMu.getColor())), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss = new SpannableString(name + " " + content);
            ss.setSpan(new ForegroundColorSpan(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.danmu_color_2)), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(AppContextProvider.getInstance().getContext().getResources().getColor(R.color.danmu_color_1)), name.length() + 1, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

//    private void verifyToken() {
//        getDanmuRepository().setLoginStatus(DanmuRepository.LOGINING);
//        checkLogin().subscribe(new DisposableObserver<UserModel>() {
//            @Override
//            public void onNext(@NonNull UserModel userModel) {
//                UseCaseParam<UseAuth.AuthParam> useCaseParam = new UseCaseParam<>();
//                useCaseParam.setParam(new UseAuth.AuthParam(userModel.getAccessTokenBean().getAccesstoken(), userModel.getDeviceId(), AppUtil.getDeviceModel(), String.valueOf(System.currentTimeMillis() / 1000)));
//                verify(authUseCase.buildUseCaseObservable(useCaseParam));
//            }
//            @Override
//            public void onError(@NonNull Throwable e) {
//                UseCaseParam<GuestAuth.GusetAuthParam> useCaseParam = new UseCaseParam<>();
//                useCaseParam.setParam(new GuestAuth.GusetAuthParam(AppUtil.getDeviceId(), AppUtil.getDeviceModel(), String.valueOf(System.currentTimeMillis() / 1000)));
//                verify(guestAuthUseCase.buildUseCaseObservable(useCaseParam));
//            }
//            @Override
//            public void onComplete() {
//                //
//            }
//        });
//    }
//
//    private void verify(Observable<AuthResponse> observable) {
//        disposable = observable.flatMap(new Function<AuthResponse, ObservableSource<ComeInReponse>>() {
//                    @Override
//                    public ObservableSource<ComeInReponse> apply(@NonNull AuthResponse authResponse) throws Exception {
//                        getDanmuRepository().setUid(authResponse.getMemberdata().getUid());
//                        UseCaseParam<ComeIn.ComeInParam> useCaseParam = new UseCaseParam<>();
//                        useCaseParam.setParam(new ComeIn.ComeInParam(getDanmuRepository().getCode(), getDanmuRepository().getTitle(), String.valueOf(System.currentTimeMillis() / 1000)));
//                        return comeInUseCase.buildUseCaseObservable(useCaseParam);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<ComeInReponse>() {
//                    @Override
//                    public void onNext(@NonNull ComeInReponse comeInReponse) {
//                        getDanmuRepository().setLoginStatus(DanmuRepository.LOGINED);
//                        getDanmuRepository().setRoomid(comeInReponse.getRoomdata().getRoomid());
//                        NoticeModel noticeModel = comeInReponse.getNoticedata();
//                        if (noticeModel != null) {
//                            sendDanmuTop(noticeModel);
//                        }
//                        initSocketManager(comeInReponse);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        getDanmuRepository().setLoginStatus(DanmuRepository.NOT_LOGIN);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

//    private void initSocketManager(ComeInReponse data) {
//        DanmuLoginData danmuLoginData = new DanmuLoginData(getDanmuRepository().getUid(), String.valueOf(getDanmuRepository().getRoomid()), data.getRoomauth());
//        if (getDanmuRepository().getDanmuSocketManager() == null) {
//            DanmuSocketManager danmuSocketManager = new DanmuSocketManager(data.getMsgservice().getHost(), data.getMsgservice().getPort(), danmuLoginData);
//            getDanmuRepository().setDanmuSocketManager(danmuSocketManager);
//        } else {
//            getDanmuRepository().getDanmuSocketManager().setDanmuLoginData(danmuLoginData);
//        }
//        getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_DANMAKU);
//        getDanmuRepository().getDanmuSocketManager().registerEvent(EVENT_DANMAKU, new EventListener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject jsonObject = (JSONObject) args[0];
//                if (jsonObject != null) {
//                    DmDataBean danmuDataBean = GsonUtil.getGson().fromJson(jsonObject.toString(), DmDataBean.class);
//                    if (danmuDataBean != null) {
//                        CommentDataBean commentDataBean = danmuDataBean.getDanmakudata();
//                        if (commentDataBean != null) {
//                            toRefreshDM(commentDataBean);
//                        }
//                    }
//                }
//            }
//        });
//        getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_USERBANNED);
//        getDanmuRepository().getDanmuSocketManager().registerEvent(EVENT_USERBANNED, new EventListener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject jsonObject = (JSONObject) args[0];
//                if (jsonObject != null) {
//                    BannedModel bannedModel = GsonUtil.getGson().fromJson(jsonObject.toString(), BannedModel.class);
//                    if (bannedModel != null) {
//                        toRefreshDM(bannedModel);
//                    }
//                }
//            }
//        });
//        getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_NOTICE);
//        getDanmuRepository().getDanmuSocketManager().registerEvent(EVENT_NOTICE, new EventListener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject jsonObject = (JSONObject) args[0];
//                if (jsonObject != null) {
//                    NoticeModel noticeModel = GsonUtil.getGson().fromJson(jsonObject.toString(), NoticeModel.class);
//                    if (noticeModel != null) {
//                        sendDanmuTop(noticeModel);
//                    }
//                }
//            }
//        });
//        getDanmuRepository().getDanmuSocketManager().unRegisterEvent(EVENT_DISNOTICE);
//        getDanmuRepository().getDanmuSocketManager().registerEvent(EVENT_DISNOTICE, new EventListener() {
//            @Override
//            public void call(Object... args) {
//                Observable.create(new ObservableOnSubscribe<Boolean>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
//                        stopDanmuCountDown();
//                        if (danmuListener != null) {
//                            danmuListener.clearMsgTop();
//                        }
//                        emitter.onComplete();
//                    }
//                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
//            }
//        });
//        if (!getDanmuRepository().getDanmuSocketManager().isConnected()) {
//            getDanmuRepository().getDanmuSocketManager().connectSocket();
//        } else {
//            getDanmuRepository().getDanmuSocketManager().login();
//        }
//    }

    private void sendDanmuTop(NoticeModel noticeModel) {
        final DanMu danMu = new DanMu();
        danMu.setColor(noticeModel.getColor())
                .setContent(noticeModel.getMessage())
                .setUid(noticeModel.getDmid())
                .setTime(noticeModel.getResponse_dateline() * 1000)
                .setDuration(noticeModel.getDuration())
                .setRefreshTime(System.currentTimeMillis());
        if (danMu.getDuration() > 0 && !TextUtils.isEmpty(danMu.getContent())) {
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                    if (danmuListener != null) {
                        danmuListener.sendMsgTop(danMu);
                    }
                    startDanmuCountDown(danMu.getDuration());
                    emitter.onComplete();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        }
    }

    private void startDanmuCountDown(int duration) {
        stopDanmuCountDown();
        danmuAnimator = ValueAnimator.ofFloat(0f, 100f);
        danmuAnimator.setDuration(duration * 1000);
        danmuAnimator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (danmuListener != null) {
                    danmuListener.clearMsgTop();
                }
            }
        });
        danmuAnimator.setInterpolator(new LinearInterpolator());
        danmuAnimator.start();
    }

//    private void stopPolling() {
//        if (disposable != null) {
//            disposable.dispose();
//            disposable = null;
//        }
//    }

    private void stopDanmuCountDown() {
        if (danmuAnimator != null) {
            danmuAnimator.getListeners().clear();
            if (danmuAnimator.isStarted()) {
                danmuAnimator.cancel();
            }
        }
    }

    private void toRefreshDM(BannedModel bannedModel) {
        checkSenedDanMuTime();
        DanMu danMu = new DanMu();
        danMu.setUid(bannedModel.getUid() + bannedModel.getResponse_dateline())
                .setNickname(bannedModel.getNickname())
                .setPlayTime("")
                .setTime(bannedModel.getResponse_dateline() * 1000)
                .setColor("#FF3E3E")
                .setType(DANMU_TYPE_BANNED)
                .setRefreshTime(System.currentTimeMillis());
        String type = bannedModel.getType();
        StringBuilder sb = new StringBuilder();
        if (bannedModel.getUid().equals(getDanmuRepository().getUid())) {
            sb.append("你已被管理员");
            switch (type) {
                case "1":
                    sb.append("禁言").append(getBannedTime(bannedModel.getDuration()));
                    break;
                case "2":
                    sb.append("取消禁言");
                    break;
                case "3":
                    sb.append("拉入黑名单");
                    break;
                case "4":
                    sb.append("取消黑名单");
                    break;
                default:
                    return;
            }
        } else {
            sb.append("@" + bannedModel.getNickname() + " ");
            switch (type) {
                case "1":
                    sb.append("被禁言").append(getBannedTime(bannedModel.getDuration()));
                    break;
                case "2":
                    sb.append("被取消禁言");
                    break;
                default:
                    return;
            }
        }
        danMu.setContent(sb.toString());
        danMu.setRefreshTime(System.currentTimeMillis());
        if (!getDanmuRepository().getSendedDanMuMap().containsKey(danMu.getUid())) {
            try {
                getDanmuRepository().getDanMuQueue().put(danMu);
            } catch (InterruptedException e) {
                //
            }
        }
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                sendDanMu();
                emitter.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    private String getBannedTime(int duration) {
        if (duration < 0) {
            return "";
        } else {
            return formatMinute(duration * 1000);
        }
    }

    private static String formatMinute(long milliseconds) {
        int seconds = (int) (milliseconds / 1000);
        int minute = seconds / 60;
        if (minute > 0) {
            return minute + "分钟";
        }
        return seconds + "秒";
    }

    private void toRefreshDM(CommentDataBean commentDataBean) {
        checkSenedDanMuTime();
        DanMu danMu = new DanMu();
        danMu.setUid("" + commentDataBean.getDmid())
                .setContent(commentDataBean.getMessage())
                .setNickname(commentDataBean.getNickname())
                .setPlayTime("")
                .setTime(commentDataBean.getDateline() * 1000)
                .setColor(commentDataBean.getColor())
                .setType(commentDataBean.getType())
                .setRefreshTime(System.currentTimeMillis());
        danMu.setRefreshTime(System.currentTimeMillis());
        if (!getDanmuRepository().getSendedDanMuMap().containsKey(danMu.getUid())) {
            try {
                getDanmuRepository().getDanMuQueue().put(danMu);
            } catch (InterruptedException e) {
                //
            }
        }
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                sendDanMu();
                emitter.onComplete();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public Observable<DmComitResponse> sendNewDanMu(String content) {
        if (!getDanmuRepository().isSend()) {
            getDanmuRepository().setSend(true);
            return sendDanMuToServer(content);
        }
        return Observable.create(new ObservableOnSubscribe<DmComitResponse>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<DmComitResponse> e) throws Exception {
                e.onError(new DanmuException(DanmuException.MSG_OPERATION_FREQUENT));
            }
        });
    }

    private Observable<DmComitResponse> sendDanMuToServer(final String content) {
        final String errorMsg;
        if (TextUtils.isEmpty(content)) {
            getDanmuRepository().setSend(false);
            errorMsg = DanmuException.MSG_CONTENT_EMPTY;
        } else if (getDanmuRepository().getLoginStatus() == DanmuRepository.NOT_LOGIN) {
            getDanmuRepository().setSend(false);
            errorMsg = DanmuException.MSG_ERROR;
            danMuSocketBizManager.refreshToken();
        } else if (getDanmuRepository().getLoginStatus() == DanmuRepository.LOGINING) {
            getDanmuRepository().setSend(false);
            errorMsg = DanmuException.MSG_ERROR;
        } else if (!NetworkUtils.isNetworkAvailable()) {
            getDanmuRepository().setSend(false);
            errorMsg = DanmuException.MSG_NETWOTK_ERROR;
        } else {
            return checkLogin().flatMap(new Function<UserModel, ObservableSource<DmComitResponse>>() {
                @Override
                public ObservableSource<DmComitResponse> apply(@NonNull UserModel userModel) throws Exception {
                    int timestamp = (int) (System.currentTimeMillis() / 1000);
                    DmModel danmuModel = new DmModel();danmuModel.setRoomid(danMuSocketBizManager.getRoomId());
                    danmuModel.setMessage(content);
                    danmuModel.setTimestamp(timestamp);
                    String sign = SignUtil.builder().signType(SignType.TYPE_SHOW)
                            .put("roomid", "" + danMuSocketBizManager.getRoomId())
                            .put("message", content)
                            .put("timestamp", "" + timestamp)
                            .put(KEY_APP_NAME, VALUE_APP_NAME)
                            .put(KEY_APP_VERSION, VALUE_APP_VERSION_NAME)
                            .put(KEY_APP_VERSION_CODE, VALUE_APP_VERSION_CODE)
                            .put(KEY_SYSTEM_NAME, VALUE_SYSTEM_NAME)
                            .put(KEY_SYSTEM_VERSION, VALUE_SYSTEM_VERSION)
                            .getSign();
                    danmuModel.setSign(sign);
                    UseCaseParam<DmModel> useCaseParam = new UseCaseParam<>();
                    useCaseParam.setParam(danmuModel);
                    return comitDmUseCase.buildUseCaseObservable(useCaseParam)
                            .doOnNext(new Consumer<DmComitResponse>() {
                                @Override
                                public void accept(@NonNull DmComitResponse danmuComitResponse) throws Exception {
                                    getDanmuRepository().setSend(false);
                                    DmComitModel danmuComitModel = danmuComitResponse.getDanmuComitModel();
                                    DanMu danMu = new DanMu();
                                    danMu.setContent(danmuComitModel.getMessage());
                                    danMu.setNickname(danmuComitModel.getNickname());
                                    danMu.setPlayTime("");
                                    danMu.setUid("" + danmuComitModel.getDmid());
                                    danMu.setTime(danmuComitModel.getDateline() * 1000);
                                    danMu.setRefreshTime(System.currentTimeMillis());
                                    danMu.setLocal(true);
                                    if (!getDanmuRepository().getSendedDanMuMap().containsKey(danMu.getUid())) {
                                        List<DanMu> danMuList = new ArrayList<>();
                                        danMu.setContentSpannable(getContent(danMu));
                                        danMuList.add(danMu);
                                        sendDanMuList(danMuList);
                                        getDanmuRepository().getSendedDanMuMap().put(danMu.getUid(), danMu);
                                    }
                                }
                            })
                            .doOnError(new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    getDanmuRepository().setSend(false);
                                }
                            });
                }
            });
        }
        return Observable.create(new ObservableOnSubscribe<DmComitResponse>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<DmComitResponse> e) throws Exception {
                e.onError(new DanmuException(errorMsg));
            }
        });
    }

    public Observable<UserModel> checkLogin() {
        return Observable.create(new ObservableOnSubscribe<UserModel>() {
            @Override
            public void subscribe(final @NonNull ObservableEmitter<UserModel> e) throws Exception {
                Router.getInstance().buildExecutor("/user/service/checklogin").callback(new Executor.Callback<UserModel>() {
                    @Override
                    public void onCall(UserModel userModel) {
                        e.onNext(userModel);
                        e.onComplete();
                    }

                    @Override
                    public void onFail(Executor.ExecutionError executionError) {
                        e.onError(new DanmuException(DanmuException.MSG_NOT_LOGIN));
                    }
                }).excute();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

}
