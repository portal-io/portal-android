package com.whaleyvr.biz.unity.vrone;

/**
 * Created by dell on 2017/12/7.
 */

public interface EventListener {

    void onStarted();
    void onPaused();
    void onBufferingUpdate(long progress);
    void onBuffering(String speed);
    void onBufferingOff();
    void onDefinitionChange(String definition);
    void onCompleted();
    void onPrepared(long duration, long progress);
    void onProgress(long progress);
    void onPreparing();
    void onError(String errorMsg);
    void onToast(String toastMsg , String toastColor, boolean isTemporary);
    void onClearToast();
    void onBackPressed();

}
