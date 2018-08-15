package com.whaleyvr.biz.unity.vrone;

/**
 * Created by dell on 2017/12/5.
 */

public interface SplitView {

    void show();
    void hide();
    void setTitle(String title);
    void setPlayCount(String playCount);
    void updateCurrentTimeText(String text);
    void updateTotalTimeText(String text);
    void changeSeekMax(int max);
    void changeSeekProgress(int progress);
    void updateDefinitionText(String text);
    void changeWidgetClickable(boolean enable);
    void changeStartPauseBtn(boolean isStarted);
    void changeSeekSecondProgress(int secondProgress);
    void showToast(String text, String color);
    void clearToast();
    void onCompleted();

}
