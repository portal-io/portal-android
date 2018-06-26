package com.whaley.biz.framerate.frametest;

import android.content.Context;

/**
 * Created by caojianglong on 16-8-20.
 */
public interface IFrameTest {

    void setMediaUrl(String url);

    float getMediaInfo(Context context);

    void startFrameTest(Context context);

    void setProgressUpdate(FrameTestImpl.ProgressUpdate mProgressUpdate);

    boolean isPlaying();

    void shutTest();
}
