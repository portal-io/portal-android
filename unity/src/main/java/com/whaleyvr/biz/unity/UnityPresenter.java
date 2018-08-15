package com.whaleyvr.biz.unity;

import android.os.Handler;

import com.whaley.biz.common.ui.BasePagePresenter;
import com.whaley.core.appcontext.AppContextProvider;
import com.whaley.core.utils.ScreenSwitchUtils;

/**
 * Created by dell on 2017/8/16.
 */

public class UnityPresenter extends BasePagePresenter<UnityView> {

    private ScreenSwitchUtils instance;
    private ScreenSwitchUtils.OnSensorListener onSensorListener;
    private boolean isClick;

    public UnityPresenter(UnityView view) {
        super(view);
    }

    public void initSersorEventListener() {
        instance = new ScreenSwitchUtils(AppContextProvider.getInstance().getContext());
        instance.setOnSensorListener(getOnSersorEventListener());
    }

    public void stopSensorListener() {
        if (instance != null) {
            instance.stop();
        }
    }

    public void startSensorListener() {
        if (instance != null) {
            instance.start();
        }
    }

    public void destroytSersorEventListener(){
        if (instance != null) {
            instance.setOnSensorListener(null);
            instance = null;
        }
    }

    ScreenSwitchUtils.OnSensorListener getOnSersorEventListener() {
        if (onSensorListener == null) {
            onSensorListener = new ScreenSwitchUtils.OnSensorListener() {
                @Override
                public void onSensorHorizontal() {
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            enterUnity();
                        }
                    }, 500);
                }
            };
        }
        return onSensorListener;
    }

    public void enterUnity(){
        if (!isClick) {
            isClick = true;
            if (instance != null) {
                instance.stop();
            }
            MessageControl.getInstance().startUnityLauncher();
            finish();
        }
    }

}
