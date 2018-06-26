package com.whaley.biz.playerui.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.whaley.biz.playerui.playercontroller.IPlayerController;

/**
 * Created by YangZhi on 2017/8/1 10:51.
 */

public interface Component {

    void init(Context context, IPlayerController playerController);

    void destroy();

    UIAdapter getUIAdapter();

    Controller getController();

    interface UIAdapter<CONTROLLER extends Controller>{
        View onCreateView(ViewGroup parent);

        View getRootView();

        CONTROLLER getController();

        void destroy();

    }

    interface Controller<UIADAPTER extends UIAdapter>{

        void onViewCreated(UIADAPTER adapter);

        UIADAPTER getUIAdapter();

        void registEvents();

        void unRegistEvents();

        void destroy();
    }
}
