package com.whaley.biz.common.utils;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.whaley.core.debug.Debug;

/**
 * Created by YangZhi on 2017/9/17 19:39.
 */

public class LeakCanaryUtil {

    private static RefWatcher refWatcher;

    public static void install(Application application){
        if(!Debug.isDebug()) {
            refWatcher = RefWatcher.DISABLED;
            return;
        }
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        enableStrictMode();
        refWatcher = LeakCanary.install(application);
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    private static void enableStrictMode(){
        if(Debug.isDebug()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
//                    .penaltyDeath() //
                    .build());
        }
    }
}
