package com.whaley.biz.common.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yangzhi on 16/8/10.
 */
public class EventController {

    public static void postEvent(Object event){
        EventBus.getDefault().post(event);
    };

    public static void postStickyEvent(Object event){
        EventBus.getDefault().postSticky(event);
    };

    public static void removeStickyEvent(Object event){
        EventBus.getDefault().removeStickyEvent(event);
    };

    public static void cancelEventDelivery(Object event){
        EventBus.getDefault().cancelEventDelivery(event);
    }

    public static void regist(Object subscriber){
        if(!EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().register(subscriber);
    }

    public static void unRegist(Object subscriber){
        if(EventBus.getDefault().isRegistered(subscriber))
            EventBus.getDefault().unregister(subscriber);
    }

    public static boolean hasSubscriberForEvent(Class eventClass){
        return EventBus.getDefault().hasSubscriberForEvent(eventClass);
    };

}
