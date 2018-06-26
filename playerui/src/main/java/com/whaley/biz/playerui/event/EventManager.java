package com.whaley.biz.playerui.event;

import com.whaley.core.debug.logger.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by YangZhi on 2017/8/1 14:40.
 */

public class EventManager {

    private static final String TAG = "EventManager";

    private final Map<String,SingleEventManager> singleEventManagerMap = new HashMap<>();

    private boolean isDestoryed;

    public void registEvent(Event event){
        if(event == null)
            return;
        String key = ((Class)event.getClass().getGenericSuperclass()).getName();
        registEvent(key,event);
    }

    public void registEvent(String key,Event event){
        if(isDestoryed)
            return;
        SingleEventManager singleEventManager = singleEventManagerMap.get(key);
        if(singleEventManager == null){
            singleEventManager = new SingleEventManager(key);
            singleEventManagerMap.put(key,singleEventManager);
        }
        singleEventManager.regist(event);
    }

    public void unRegist(Event event){
        if(event == null){
            return;
        }
        String key = ((Class)event.getClass().getGenericSuperclass()).getName();
        unRegist(key,event);
    }

    public void unRegist(String key,Event event){
        SingleEventManager singleEventManager = singleEventManagerMap.get(key);
        if(singleEventManager == null)
            return;
        singleEventManager.unRegist(event);
        if(singleEventManager.isCleared()){
            singleEventManagerMap.remove(key);
        }
    }


    public boolean emitEvent(Class<? extends Event> eventClazz,Object param){
        return emitEvent(eventClazz.getName(),param);
    }

    public boolean emitEvent(String key,Object param){
        SingleEventManager singleEventManager = singleEventManagerMap.get(key);
        if(singleEventManager==null)
            return true;
        return singleEventManager.emit(param);
    }

    public void reset(){
        for (String key : singleEventManagerMap.keySet()){
            singleEventManagerMap.get(key).reset();
        }
    }

    public void reset(String key){
        SingleEventManager singleEventManager = singleEventManagerMap.get(key);
        if(singleEventManager==null)
            return;
        singleEventManager.reset();
    }

    public void reset(Class<? extends Event> eventClazz){
        reset(eventClazz.getName());
    }

    public void clear(){
        singleEventManagerMap.clear();
    }

    public void destory(){
        isDestoryed = true;
        reset();
        clear();
    }


    private static class SingleEventManager{

        private String key;

        private final List<Event> eventList = new ArrayList<>();

        private Iterator<Event> currentIterator;

        private boolean isChanged;

        public SingleEventManager(String key){
            this.key = key;
        }

        public void regist(Event event){
            Log.d(TAG,"regist key = "+ key+" , event = " + event);
            eventList.add(event);
            isChanged = true;
        }

        public void unRegist(Event event){
            Log.d(TAG,"unRegist key = "+ key+" , event = " + event);
            eventList.remove(event);
        }

        public boolean emit(Object param){
            Log.d(TAG,"emit event key = "+ key+" , param = " + param);
            checkSort();
            if(currentIterator == null){
                currentIterator = eventList.iterator();
            }
            return emitNext(param);
        }

        private void checkSort(){
            if(isChanged){
                Collections.sort(eventList, new Comparator<Event>() {
                    @Override
                    public int compare(Event o1, Event o2) {
                        int x = o1.priority();
                        int y = o2.priority();
                        return (x <y ) ? 1 : ((x == y) ? 0 : -1);
                    }
                });
                isChanged = false;
            }
        }

        private boolean emitNext(Object param){
            Log.d(TAG,"emitNext key = "+ key+" , param = " + param + ",currentIterator = "+currentIterator);
            if(currentIterator == null)
                return true;
            if(currentIterator.hasNext()) {
                Event event = currentIterator.next();
//                if(!currentIterator.hasNext()){
//                    reset();
//                }
                if(event.emit(param)){
                    return emitNext(param);
                }else {
                    return false;
                }
            }
            reset();
            return true;
        }

        public void reset(){
            currentIterator = null;
            Log.d(TAG,"reset key = "+ key+",currentIterator = "+currentIterator);
        }

        public boolean isCleared(){
            return eventList.size()==0;
        }
    }
}
