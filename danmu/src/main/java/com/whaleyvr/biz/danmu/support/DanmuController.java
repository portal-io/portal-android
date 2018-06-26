package com.whaleyvr.biz.danmu.support;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Toast;

import com.whaley.biz.common.model.bi.BIConstants;
import com.whaley.biz.common.model.bi.LogInfoParam;
import com.whaley.biz.playerui.component.common.control.ControlController;
import com.whaley.biz.playerui.event.BackPressEvent;
import com.whaley.biz.playerui.event.BlankClickEvent;
import com.whaley.biz.playerui.event.KeyboardVisibleChangeEvent;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.playerui.model.PlayData;
import com.whaley.core.router.Router;
import com.whaley.core.utils.DisplayUtil;
import com.whaleyvr.biz.danmu.DanMu;
import com.whaleyvr.biz.danmu.DanmuListener;
import com.whaleyvr.biz.danmu.response.DmComitResponse;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by yangzhi on 2017/9/4.
 */

public class DanmuController extends ControlController<DanmuUIAdapter> implements BIConstants {

    static final String OPEN_DANMU_EVENT = "/program/event/opendanmu";

    static final String CLOSE_DANMU_EVENT = "/program/event/closedanmu";

    static final String SHOW_DANMU_EDIT_EVENT = "/program/event/showdanmuedit";

    static final String HIDE_DANMU_EDIT_EVENT = "/program/event/hidedanmuedit";


    boolean isDanmuOpened;

    com.whaleyvr.biz.danmu.DanmuController danmuController;

    private boolean isDanmuAttached;

    private String attachedId;

    private List<DanMu> memoryDanmulist = new ArrayList<>();

    private Disposable disposable;

    private boolean isDanmuSingle;

    private DanMu topMsg;

    private List<DanMu> msgList;

    public DanmuController() {
        danmuController = new com.whaleyvr.biz.danmu.DanmuController();
    }

    @Override
    public void onPreparing(PreparingEvent preparingEvent) {
        super.onPreparing(preparingEvent);
        dettachDanmu();
    }

    private void attachDanmu() {
        PlayData playData = getPlayerController().getRepository().getCurrentPlayData();
        if (playData == null) {
            return;
        }
        if (!isDanmuAttached || !playData.getId().equals(attachedId)) {
            dettachDanmu();
            danmuController.setDanmuListener(new DanmuListener() {
                @Override
                public void sendMsg(List<DanMu> danMuList) {
                    msgList = danMuList;
                    memoryDanmulist.addAll(danMuList);
                    getUIAdapter().sendMsg(memoryDanmulist);
                    emitDanmuHeight();
                    if (isDanmuSingle) {
                        getUIAdapter().sendSingleMsg(danMuList.get(danMuList.size() - 1));
                    }
                }

                @Override
                public void sendMsgTop(DanMu danMu) {
                    topMsg = danMu;
                    getUIAdapter().changeTopMsg(danMu);
                    emitDanmuHeight();
                }

                @Override
                public void clearMsgTop() {
                    topMsg = null;
                    getUIAdapter().clearTopMsg();
                    emitDanmuHeight();
                }
            });
            danmuController.init(playData.getId(), playData.getTitle());
            isDanmuAttached = true;
            attachedId = playData.getId();
            openDanmuUI();
        }
    }

    private void dettachDanmu() {
        danmuController.destroy();
        isDanmuAttached = false;
    }

    @Override
    protected void onDestory() {
        super.onDestory();
        dettachDanmu();
        dispose();
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        if (isDanmuOpened()) {
            openDanmu();
//            if(topMsg!=null){
//                getUIAdapter().changeTopMsg(topMsg);
//            }else{
//                getUIAdapter().clearTopMsg();
//            }
//            if(msgList!=null&&!msgList.isEmpty()){
//                getUIAdapter().sendMsg(msgList);
//            }
        } else {
            closeDanmu();
        }
    }

    @Subscribe(priority = 1)
    public void onBlankClickEvent(BlankClickEvent event) {
        if (getUIAdapter().isEditVisible()) {
            ModuleEvent moduleEvent = new ModuleEvent(HIDE_DANMU_EDIT_EVENT, null);
            emitEvent(moduleEvent);
            getEventBus().cancelEventDelivery(event);
        }
    }

    @Subscribe(priority = 5)
    public void onBackPressEvent(BackPressEvent backPressEvent) {
        if (getUIAdapter().isEditVisible()) {
            ModuleEvent moduleEvent = new ModuleEvent(HIDE_DANMU_EDIT_EVENT, null);
            emitEvent(moduleEvent);
            getEventBus().cancelEventDelivery(backPressEvent);
        }
    }

    @Subscribe
    public void onKeyboardHide(KeyboardVisibleChangeEvent event) {
        if (event.isVisible()) {
            if (isDanmuOpened) {
                getUIAdapter().showSingleDanmu(DisplayUtil.convertDIP2PX(50));
                isDanmuSingle = true;
            }
        } else {
            ModuleEvent moduleEvent = new ModuleEvent(HIDE_DANMU_EDIT_EVENT, null);
            emitEvent(moduleEvent);
        }
    }

    @Subscribe
    public void onModuleEvent(ModuleEvent event) {
        String eventName = event.getEventName();
        switch (eventName) {
            case OPEN_DANMU_EVENT:
                openDanmu();
                emitDanmuHeight();
                break;
            case CLOSE_DANMU_EVENT:
                closeDanmu();
                emitDanmuHeight();
                break;
            case SHOW_DANMU_EDIT_EVENT:
                showDanmuEdit();
                break;
            case HIDE_DANMU_EDIT_EVENT:
                hideDanmuEdit();
                if (isDanmuSingle) {
                    getUIAdapter().hideSingleDanmu();
                }
                isDanmuSingle = false;
                break;
            case "/livegift/event/updategiftheight":
                if (isDanmuOpened) {
                    getUIAdapter().showSingleDanmu((int) event.getParam());
                    isDanmuSingle = true;
                }
                break;
            case "/livegift/event/gifthide":
                if (isDanmuSingle) {
                    getUIAdapter().hideSingleDanmu();
                }
                isDanmuSingle = false;
            default:
                break;
        }
    }


    @Subscribe(priority = 1)
    public void onRestTouchDurationEvent(RestTouchDurationEvent event) {
        if (getUIAdapter().isEditVisible()) {
            event.setRegistTouchDuration(false);
        }
    }

    private void showDanmuEdit() {
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getUIAdapter().showEdit();
        RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();
        restTouchDurationEvent.setRegistTouchDuration(false);
        emitEvent(restTouchDurationEvent);
    }

    private void hideDanmuEdit() {
        getUIAdapter().hideEdit();
        RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();
        restTouchDurationEvent.setRegistTouchDuration(true);
        emitEvent(restTouchDurationEvent);
    }


    private void openDanmu() {
        isDanmuOpened = true;
        if (!isViewCreated()) {
            return;
        }
        attachDanmu();
    }

    private void closeDanmu() {
        isDanmuOpened = false;
        if (!isViewCreated()) {
            return;
        }
        dettachDanmu();
        closeDanmuUI();
    }

    private void openDanmuUI() {
        if (isHide()) {
            return;
        }
        showUI(true);
    }

    private void closeDanmuUI() {
        hideUI(true);
    }

    public boolean isDanmuOpened() {
        return isDanmuOpened;
    }

    @Override
    protected void showUI(boolean anim) {
        if (isDanmuOpened) {
            super.showUI(anim);
        }
    }

    @Override
    protected void hideUI(boolean anim) {
        if (!isDanmuSingle)
            super.hideUI(anim);
    }

    public void onSendDanmuClick() {
        danmuController.sendNewDanMu(getUIAdapter().getEditText())
                .subscribeWith(new DisposableObserver<DmComitResponse>() {
                    @Override
                    public void onNext(@NonNull DmComitResponse dmComitResponse) {
                        onClickSendDanmu();
                        Toast.makeText(getContext(), "已发送", Toast.LENGTH_SHORT).show();
                        getUIAdapter().changeSendBtnEnable(false);
                        getUIAdapter().updateEditText("");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if(e!=null && !TextUtils.isEmpty(e.getMessage())) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void setEditTextObservable(Observable<Editable> observable) {
        dispose();
        disposable = observable
                .doOnNext(new Consumer<Editable>() {
                    @Override
                    public void accept(@NonNull Editable editable) throws Exception {
                        getUIAdapter().changeSendBtnEnable(false);
                    }
                })
                .filter(new Predicate<Editable>() {
                    @Override
                    public boolean test(@NonNull Editable editable) throws Exception {
                        return editable.length() > 0;
                    }
                })
                .doOnNext(new Consumer<Editable>() {
                    @Override
                    public void accept(@NonNull Editable editable) throws Exception {
                        getUIAdapter().changeSendBtnEnable(true);
                    }
                })
                .filter(new Predicate<Editable>() {
                    @Override
                    public boolean test(@NonNull Editable editable) throws Exception {
                        return editable.length() > 40;
                    }
                })
                .doOnNext(new Consumer<Editable>() {
                    @Override
                    public void accept(@NonNull Editable editable) throws Exception {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "弹幕最多可输入40字符", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .map(new Function<Editable, CharSequence>() {
                    @Override
                    public CharSequence apply(@NonNull Editable editable) throws Exception {
                        return editable.subSequence(0, 40);
                    }
                })
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(@NonNull CharSequence charSequence) throws Exception {
                        getUIAdapter().updateEditText(charSequence);
                    }
                });
    }


    private void dispose() {
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }


    public void emitDanmuHeight() {
        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        if (getUIAdapter() == null)
                            return;
                        ModuleEvent moduleEvent = new ModuleEvent("/danmu/event/updatedanmuheight", getUIAdapter().getDanmuLayoutHeight());
                        emitEvent(moduleEvent);
                    }
                });
    }

    //==============================BI埋点====================================//

    /**
     * 点击发送弹幕
     */
    private void onClickSendDanmu() {
        LogInfoParam.Builder builder = LogInfoParam.createBuilder()
                .setEventId(DANMU)
                .setCurrentPageId(ROOT_LIVE_DETAILS)
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_SID, getPlayerController().getRepository().getCurrentPlayData().getId())
                .putCurrentPagePropKeyValue(CURRENT_PROP_VIDEO_NAME, getPlayerController().getRepository().getCurrentPlayData().getTitle())
                .setNextPageId(ROOT_LIVE_DETAILS);
        Router.getInstance().buildExecutor("/datastatistics/service/saveloginfo").putObjParam(builder.build()).excute();
    }

}
