package com.whaley.biz.playerui.component;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;

import com.whaley.biz.playerui.event.Event;
import com.whaley.biz.playerui.event.ModuleEvent;
import com.whaley.biz.playerui.event.PreparingEvent;
import com.whaley.biz.playerui.event.ScreenChangedEvent;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.core.debug.logger.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhi on 2017/8/1 11:30.
 */

public abstract class BaseController<UIADAPTER extends Component.UIAdapter> implements Component.Controller<UIADAPTER> {

    private Context context;

    private Activity activity;

    private UIADAPTER adapter;

    private IPlayerController playerController;

    private List<RegistEvent> eventList = new ArrayList<>();

    private boolean isViewCreated;

    private boolean isLandScape;

    public void setPlayerController(IPlayerController playerController) {
        this.playerController = playerController;
    }

    public IPlayerController getPlayerController() {
        return playerController;
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
        this.activity = getActivity(context);
    }

    public Context getContext() {
        return context;
    }

    @Override
    public final void onViewCreated(UIADAPTER adapter) {
        this.adapter = adapter;
        isViewCreated = true;
        registEvents();
        onViewCreated();
    }

    public boolean isViewCreated() {
        return isViewCreated;
    }

    protected void onViewCreated() {
        checkInitOrientation();
    }

    public final void init() {
        if (!isHasUI()) {
            registEvents();
        }

        onInit();
    }

    protected boolean isHasUI(){
        return false;
    }

    @Subscribe(sticky = true)
    public void onPreparing(PreparingEvent preparingEvent){
        onDispose();
    }

    @Subscribe
    public void onScreenChanged(ScreenChangedEvent screenChangedEvent){
        if(isRegistOrientation()) {
            checkOrientation(screenChangedEvent.getRequestOrientation());
        }
    }


    protected void checkInitOrientation() {
        if (getActivity() != null) {
            int requestOrientation = getActivity().getRequestedOrientation();
            checkOrientation(requestOrientation);
        }
    }

    protected void checkOrientation(int orientation) {
        switch (orientation) {
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                isLandScape = true;
                onSwitchToLandscape();
                break;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                isLandScape = false;
                onSwitchToProtrait();
                break;
            default:
                isLandScape = false;
                onSwitchToOtherOrientation(orientation);
                break;
        }
    }

    protected void onSwitchToOtherOrientation(int orientation) {

    }

    protected void onSwitchToLandscape() {

    }

    protected void onSwitchToProtrait() {

    }

    protected boolean isRegistOrientation() {
        return false;
    }

    public boolean isLandScape() {
        return isLandScape;
    }


    protected void onDispose() {

    }

    protected void onInit() {

    }

    protected void emitEvent(String key, Object param) {
        ModuleEvent moduleEvent = new ModuleEvent(key, param);
        emitEvent(moduleEvent);
    }

    protected void emitEvent(Object event) {
        getPlayerController().getEventBus().post(event);
    }

    protected void emitStickyEvent(Object event) {
        getPlayerController().getEventBus().postSticky(event);
    }


    @Override
    public UIADAPTER getUIAdapter() {
        return adapter;
    }

    @Override
    public final void destroy() {
        activity = null;
        unRegistEvents();
        onDestory();
        adapter = null;
    }

    @Override
    public void registEvents(){
        if((!isHasUI()||isViewCreated())&&!getEventBus().isRegistered(this)) {
            getEventBus().register(this);
        }
    }

    @Override
    public void unRegistEvents(){
        if (getEventBus().isRegistered(this)) {
            getEventBus().unregister(this);
        }
        onDispose();
    }

    protected EventBus getEventBus() {
        return getPlayerController().getEventBus();
    }

    protected void onDestory() {

    }


    private Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context);
        } else if (context instanceof ContextWrapper) {
            Context context2 = ((ContextWrapper) context).getBaseContext();
            if (context2 instanceof Activity) {
                return ((Activity) context2);
            }
        }
        return null;
    }

    protected Activity getActivity() {
        return activity;
    }

    private static class RegistEvent {
        private String key;

        private Event event;

        public RegistEvent(String key, Event event) {
            this.key = key;
            this.event = event;
        }
    }
}
