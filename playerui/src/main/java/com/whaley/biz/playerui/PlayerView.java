package com.whaley.biz.playerui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.whaley.biz.playerui.component.Component;
import com.whaley.biz.playerui.event.EventManager;
import com.whaley.biz.playerui.event.RestTouchDurationEvent;
import com.whaley.biz.playerui.playercontroller.IPlayerController;
import com.whaley.biz.playerui.playercontroller.PlayerController;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by YangZhi on 2017/8/1 10:39.
 */

public class PlayerView extends FrameLayout {

    private IPlayerController playerController;

    private ComponentManager componentManager;

//    private EventBus eventManager;

    private boolean isBuildComponents;

    private ViewPrepareListener viewPrepareListener;

    private View realPlayerView;

    public PlayerView(Context context) {
        this(context, null);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        eventManager = new EventManager();
        playerController = PlayerController.getInstance(getContext().getApplicationContext());
        componentManager = new ComponentManager(getContext(), playerController);
        realPlayerView = playerController.onCreatePlayerView(this);
        addView(realPlayerView);
        setPlayerView();
        setBackgroundColor(Color.BLACK);
        addLifeCycleFragment(playerController.getEventBus());
    }

    private void addLifeCycleFragment(EventBus eventBus) {
        Activity activity = getActivity();
        if (activity != null) {
            if (activity instanceof FragmentActivity) {
                FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
                Fragment fragment = fm.findFragmentByTag("PlayerLifeCycle");
                LifeCycleFragment lifeCycleFragment;
                if (fragment != null) {
                    lifeCycleFragment = (LifeCycleFragment) fragment;
                } else {
                    lifeCycleFragment = new LifeCycleFragment();
                }
                lifeCycleFragment.setEventBus(eventBus);
                if (!lifeCycleFragment.isAdded()) {
                    fm.beginTransaction()
                            .add(lifeCycleFragment, "PlayerLifeCycle")
                            .commitAllowingStateLoss();
                }
                return;
            }
        }
    }

    public void setPlayerView() {
//        playerController.setEventBus(eventManager);
        playerController.setPlayerView(realPlayerView);
    }


    public View getRealPlayerView() {
        return realPlayerView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        checkBuildComponents();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        checkBuildComponents();
        super.onLayout(changed, left, top, right, bottom);

    }

    RestTouchDurationEvent restTouchDurationEvent = new RestTouchDurationEvent();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                restTouchDurationEvent.setRegistTouchDuration(false);
                getPlayerController().getEventBus().post(restTouchDurationEvent);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                restTouchDurationEvent.setRegistTouchDuration(true);
                getPlayerController().getEventBus().post(restTouchDurationEvent);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void destory(boolean isContinueDestory) {
//        eventManager.destory();
        getComponentManager().unRegistAll();
        playerController.setPlayerView(null);
        if (!isContinueDestory) {
            playerController.release();
        }
    }

    public void destory() {
//        eventManager.destory();
        getComponentManager().unRegistAll();
//        removeAllViews();
        playerController.setPlayerView(null);
        playerController.release();
    }

    public ComponentManager getComponentManager() {
        return componentManager;
    }


    public void regist(Component component) {
        getComponentManager().regist(component);
    }

    protected void checkBuildComponents() {
        if (isComponentsRegisted() && !isBuildComponents()) {
            rebuildComponents();
            isBuildComponents = true;
            if (getViewPrepareListener() == null) {
                return;
            }
            getViewPrepareListener().onViewPrepared(getPlayerController());
        }
    }

    private boolean isComponentsRegisted() {
        return true;
    }

    public void rebuildComponents() {
        for (Component component : componentManager.getComponents()) {
            buildUIComponent(component);
        }
    }

    public void buildUIComponent(Component component) {
        Component.UIAdapter uiAdapter = component.getUIAdapter();
        if (uiAdapter == null) {
            return;
        }
        View view = uiAdapter.onCreateView(this);
        if (view == null) {
            return;
        }
        if(view.getParent()!=null && view.getParent() instanceof ViewGroup){
            ((ViewGroup) view.getParent()).removeView(view);
        }
        addView(view);
        Component.Controller controller = component.getController();
        if (controller != null) {
            controller.onViewCreated(uiAdapter);
        }
    }

    private boolean isBuildComponents() {
        return isBuildComponents;
    }

    public IPlayerController getPlayerController() {
        return playerController;
    }

    public void setViewPrepareListener(ViewPrepareListener viewPrepareListener) {
        this.viewPrepareListener = viewPrepareListener;
    }

    public ViewPrepareListener getViewPrepareListener() {
        return viewPrepareListener;
    }

    public interface ViewPrepareListener {
        void onViewPrepared(IPlayerController playerController);
    }

    protected Activity getActivity() {
        Context context = getContext();
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


    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        realPlayerView.setVisibility(visibility);
    }
}
