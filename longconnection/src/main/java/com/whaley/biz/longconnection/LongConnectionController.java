package com.whaley.biz.longconnection;


import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.biz.common.interactor.UseCaseParam;
import com.whaley.biz.longconnection.interactor.ComeIn;
import com.whaley.biz.longconnection.interactor.GuestAuth;
import com.whaley.biz.longconnection.interactor.UseAuth;
import com.whaley.biz.longconnection.model.AuthResponse;
import com.whaley.biz.longconnection.model.ComeInReponse;
import com.whaley.biz.longconnection.model.LongConnectionLoginData;
import com.whaley.biz.longconnection.model.UserModel;
import com.whaley.core.debug.logger.Log;
import com.whaley.core.router.Executor;
import com.whaley.core.router.Router;
import com.whaley.core.utils.AppUtil;
import com.whaley.core.utils.GsonUtil;
import com.whaleyvr.core.network.socketio.EventListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Author: qxw
 * Date:2017/10/18
 * Introduction:
 */

public class LongConnectionController {

    List<LongConnectionListener> listeners = new ArrayList<>();
    private String code;
    private String title;
    private String uid;


    private boolean isVerifyToken = false;

    private ComeInReponse comeInReponseCache;

    SocketManager socketManager;
    Disposable disposable;
    Map<String, EventListener> pendingRegistEvents = new HashMap<>();
    List<String> pendingRegistEventList = new ArrayList<>();

    public String getUid() {
        return uid;
    }

    public int getRoomID() {
        if (comeInReponseCache != null) {
            return comeInReponseCache.getRoomdata().getRoomid();
        } else {
            return -1;
        }
    }


    public LongConnectionController(String code, String title) {
        this.code = code;
        this.title = title;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case "login_success":
                verifyToken();
                break;
        }
    }

    public void join(String eventType, EventListener eventListener) {
        if (socketManager == null) {
            pendingRegistEvents.put(eventType, eventListener);
            if (!isVerifyToken) {
                isVerifyToken();
                verifyToken();
            }
        } else {
            socketManager.registerEvent(eventType, eventListener);
            if (!isVerifyToken) {
                isVerifyToken();
                loginSocket();
            }
        }
        pendingRegistEventList.add(eventType);
    }

    private void isVerifyToken() {
        isVerifyToken = true;
        EventController.regist(this);
    }

    public void quit(String eventType) {
        if (socketManager != null) {
            socketManager.unRegisterEvent(eventType);
        }
        if (pendingRegistEventList.size() == 0) {
            return;
        }
        pendingRegistEventList.remove(eventType);
        if (pendingRegistEventList.size() == 0) {
            onTimer();
        }
    }

    public void allQuit() {
        for (String eventType : pendingRegistEventList) {
            if (socketManager == null) {
                break;
            }
            socketManager.unRegisterEvent(eventType);
        }
        pendingRegistEventList.clear();
        onTimer();
    }


    private void onTimer() {
        destroy();
    }

    public void destroy() {
        EventController.unRegist(this);
        if (disposable != null) {
            disposable.dispose();
        }
        if (socketManager != null) {
            socketManager.closeSocket();
        }
        socketManager = null;
        isVerifyToken = false;
        CacheManager.lruCache.remove(code);
    }

    public void refreshToken() {
        isVerifyToken = true;
        verifyToken();
    }

    private void verifyToken() {
        for (LongConnectionListener listener : listeners) {
            listener.verifyToken();
        }
        checkLogin().subscribe(new DisposableObserver<UserModel>() {
            @Override
            public void onNext(@NonNull UserModel userModel) {
                UseCaseParam<UseAuth.AuthParam> useCaseParam = new UseCaseParam<>();
                useCaseParam.setParam(new UseAuth.AuthParam(userModel.getAccessTokenBean().getAccesstoken(), userModel.getDeviceId(), AppUtil.getDeviceModel(), String.valueOf(System.currentTimeMillis() / 1000)));
                UseAuth useAuth = new UseAuth();
                verify(useAuth.buildUseCaseObservable(useCaseParam));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                UseCaseParam<GuestAuth.GusetAuthParam> useCaseParam = new UseCaseParam<>();
                useCaseParam.setParam(new GuestAuth.GusetAuthParam(AppUtil.getDeviceId(), AppUtil.getDeviceModel(), String.valueOf(System.currentTimeMillis() / 1000)));
                GuestAuth guestAuth = new GuestAuth();
                verify(guestAuth.buildUseCaseObservable(useCaseParam));
            }

            @Override
            public void onComplete() {
                //
            }
        });
    }

    private void verify(Observable<AuthResponse> observable) {
        disposable = observable.flatMap(new Function<AuthResponse, ObservableSource<ComeInReponse>>() {
            @Override
            public ObservableSource<ComeInReponse> apply(@NonNull AuthResponse authResponse) throws Exception {
                uid = authResponse.getMemberdata().getUid();
                for (LongConnectionListener listener : listeners) {
                    listener.verifyTokened(uid);
                }
//                if (comeInReponseCache != null) {
//                    return Observable.just(comeInReponseCache);
//                }
                UseCaseParam<ComeIn.ComeInParam> useCaseParam = new UseCaseParam<>();
                useCaseParam.setParam(new ComeIn.ComeInParam(code, title, String.valueOf(System.currentTimeMillis() / 1000)));
                ComeIn comeIn = new ComeIn();
                return comeIn.buildUseCaseObservable(useCaseParam);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ComeInReponse>() {
                    @Override
                    public void onNext(@NonNull ComeInReponse comeInReponse) {
                        comeInReponseCache = comeInReponse;
                        for (LongConnectionListener listener : listeners) {
                            listener.onLogined(GsonUtil.getGson().toJson(comeInReponse.getNoticedata()));
                        }
                        if (socketManager == null) {
                            initSocketManager();
                        } else {
                            loginSocket();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isVerifyToken = false;
                        for (LongConnectionListener listener : listeners) {
                            listener.onLoginFailed();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void initSocketManager() {
        LongConnectionLoginData longConnectionLoginData = new LongConnectionLoginData(uid, String.valueOf(comeInReponseCache.getRoomdata().getRoomid()), comeInReponseCache.getRoomauth());
        socketManager = new SocketManager(comeInReponseCache.getMsgservice().getHost(), comeInReponseCache.getMsgservice().getPort(), longConnectionLoginData);
        Iterator<String> iterator = pendingRegistEvents.keySet().iterator();
        while (iterator.hasNext()) {
            String eventType = iterator.next();
            EventListener eventListener = pendingRegistEvents.get(eventType);
            socketManager.registerEvent(eventType, eventListener);
        }
        pendingRegistEvents.clear();
        socketManager.connectSocket();
    }

    private void loginSocket() {
        LongConnectionLoginData longConnectionLoginData = new LongConnectionLoginData(uid, String.valueOf(comeInReponseCache.getRoomdata().getRoomid()), comeInReponseCache.getRoomauth());
        socketManager.setDanmuLoginData(longConnectionLoginData);
        if (socketManager.isConnected()) {
            socketManager.login();
        } else {
            socketManager.connectSocket();
        }
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
                        e.onError(executionError);
                    }
                }).excute();
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    public void addListener(LongConnectionListener longConnectionListener) {
        listeners.add(longConnectionListener);
        if (comeInReponseCache != null) {
            longConnectionListener.verifyTokened(uid);
            longConnectionListener.onLogined(GsonUtil.getGson().toJson(comeInReponseCache.getNoticedata()));
        }
    }

    public void removeListener(LongConnectionListener longConnectionListener) {
        listeners.remove(longConnectionListener);
    }


    public interface LongConnectionListener {
        void verifyToken();

        void verifyTokened(String uid);

        void onLogined(String notice);

        void onLoginFailed();
    }
}
