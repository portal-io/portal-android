package com.whaleyvr.biz.unity.vrone;

import android.util.Log;

import com.snailvr.tools.ToolBase;

/**
 * Created by dell on 2017/12/5.
 */

public class SplitPlayerTool extends ToolBase implements EventListener{

    private static SplitPlayerTool sInstance = null;

    public static SplitPlayerTool getInstance() {
        if (sInstance == null) {
            sInstance = new SplitPlayerTool();
        }
        return sInstance;
    }

    private EventListener eventListener;

    public void setEventListener(EventListener eventListener){
        this.eventListener = eventListener;
    }

    public EventListener getEventListener(){
        return eventListener;
    }

    //=========================================unity调用native接口===================================================

    @Override
    public void onStarted(){
        //播放状态上报
        if(eventListener!=null){
            eventListener.onStarted();
        }
    }

    @Override
    public void onPaused(){
        //暂停状态上报
        if(eventListener!=null){
            eventListener.onPaused();
        }
    }

    @Override
    public void onBufferingUpdate(long progress){
        //缓冲进度上报，返回参数为缓冲值,单位毫秒
        if(eventListener!=null){
            eventListener.onBufferingUpdate(progress);
        }
    }

    @Override
    public void onBuffering(String speed){
        //缓冲速度上报，返回参数为128kb/s，诸如此类
        if(eventListener!=null){
            eventListener.onBuffering(speed);
        }
    }

    @Override
    public void onBufferingOff(){
        //结束缓冲
        if(eventListener!=null){
            eventListener.onBufferingOff();
        }
    }

    @Override
    public void onDefinitionChange(String definition){
        //切换清晰度，返回参数为“ST”、“HD”，诸如此类
        if(eventListener!=null){
            eventListener.onDefinitionChange(definition);
        }
    }

    @Override
    public void onCompleted(){
        //播放完成上报
        if(eventListener!=null){
            eventListener.onCompleted();
        }
    }

    @Override
    public void onPrepared(long duration, long progress){
        //视频准备完毕，开始播放，返回总时长和当前进度，单位毫秒);
        if(eventListener!=null){
            eventListener.onPrepared(duration, progress);
        }
    }

    @Override
    public void onProgress(long progress){
        //播放进度上报，返回当前进度，单位毫秒
        if(eventListener!=null){
            eventListener.onProgress(progress);
        }
    }

    @Override
    public void onPreparing(){
        //视频正在准备中
        if(eventListener!=null){
            eventListener.onPreparing();
        }
    }

    @Override
    public void onError(String errorMsg){
        //播放错误，返回错误信息
        if(eventListener!=null){
            eventListener.onError(errorMsg);
        }
    }

    @Override
    public void onToast(String toastMsg , String toastColor, boolean isTemporary){
        //其他提示消息，返回具体toast文字、颜色以及是否是临时显示 true 临时显示指定时间比如1.5秒 false 永久显示
        Log.d("unity","toastMsg : " + toastMsg + " toastColor : " + toastColor + " isTemporary : " + isTemporary);
        if(eventListener!=null){
            eventListener.onToast(toastMsg, toastColor, isTemporary);
        }
    }

    @Override
    public void onClearToast() {
        if(eventListener!=null){
            eventListener.onClearToast();
        }
    }

    @Override
    public void onBackPressed() {
        if(eventListener!=null){
            eventListener.onBackPressed();
        }
    }

    //=========================================native调用unity接口===================================================

    public static final String PREFIX = "split_";
    public static final String NEXT_DEFINITION = PREFIX + "next_definition";
    public static final String START = PREFIX + "start";
    public static final String PAUSE = PREFIX + "pause";
    public static final String RESTART = PREFIX + "restart";
    public static final String DESTROY = PREFIX + "destroy";
    public static final String CHANGE_PROGRESS = PREFIX + "change_progress";
    public static final String CLICK_HOT_SPOT = PREFIX + "click_hot_spot";

    public void nextDefinition(){
        SendMessageToUnity(NEXT_DEFINITION);// 切换下个清晰度
    }

    public void start(){
        SendMessageToUnity(START);// 开始播放
    }

    public void pause(){
        SendMessageToUnity(PAUSE);//暂停播放
    }

    public void reStart(){
        SendMessageToUnity(RESTART);//重新播放
    }

    public void destroy(boolean isBack){
        SendMessageToUnity(DESTROY, String.valueOf(isBack));//退出unity分屏模式
    }

    public void changeProgress(String progress){
        SendMessageToUnity(CHANGE_PROGRESS, progress);//改变播放进度
    }

    public void clickHotSpot(int type){
        SendMessageToUnity(CLICK_HOT_SPOT, String.valueOf(type));//点击热区 0 down 1 up 2 cancel
    }

}
