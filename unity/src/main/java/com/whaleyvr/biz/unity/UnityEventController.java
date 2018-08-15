package com.whaleyvr.biz.unity;

import com.whaley.biz.common.event.BaseEvent;
import com.whaley.biz.common.event.EventController;
import com.whaley.core.router.Router;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by dell on 2017/8/31.
 */

public class UnityEventController {

    static final String EVENT_LOGIN_SUCCESS = "login_success";

    private volatile static UnityEventController instance;

    private UnityEventController() {
        EventController.regist(this);
    }

    public static void init() {
        if (instance == null) {
            synchronized (UnityEventController.class) {
                if (instance == null) {
                    instance = new UnityEventController();
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        switch (event.getEventType()) {
            case EVENT_LOGIN_SUCCESS:
                Router.getInstance().buildExecutor("/unity/service/login").excute();
                break;
        }
    }

}
