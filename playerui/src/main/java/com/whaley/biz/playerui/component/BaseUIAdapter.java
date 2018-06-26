package com.whaley.biz.playerui.component;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by YangZhi on 2017/8/1 11:33.
 */

public abstract class BaseUIAdapter<CONTROLLER extends Component.Controller> implements Component.UIAdapter<CONTROLLER>{

    private CONTROLLER controller;

    private Context context;

    private View rootView;

    private Activity activity;

    public void setContext(Context context) {
        this.context = context;
        this.activity = getActivity(context);
    }

    public Context getContext() {
        return context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setController(CONTROLLER controller) {
        this.controller = controller;
    }

    @Override
    public final View onCreateView(ViewGroup parent) {
        if(rootView == null) {
            View view = initView(parent);
            this.rootView = view;
            onViewCreated(view);
            return view;
        }else{
            return getRootView();
        }
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    protected abstract View initView(ViewGroup parent);

    protected abstract void onViewCreated(View view);

    @Override
    public CONTROLLER getController() {
        return controller;
    }

    private Activity getActivity(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context);
        } else if (context instanceof ContextWrapper) {
            Context context2 = ((ContextWrapper) context).getBaseContext();
            if (context2 instanceof Activity) {
                return ((Activity) context2);
            }
        }
        return null;
    }


}
