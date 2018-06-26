package com.whaley.biz.pay;

import com.ipaynow.plugin.api.IpaynowPlugin;
import com.whaley.core.appcontext.AppContextProvider;

/**
 * Created by dell on 2017/9/21.
 */

public class PayPlugin {

    private volatile static PayPlugin instance;

    private IpaynowPlugin mIpaynowplugin;

    private PayPlugin() {
        mIpaynowplugin = IpaynowPlugin.getInstance().init(AppContextProvider.getInstance().getContext());// 1.插件初始化
        mIpaynowplugin.unCkeckEnvironment();// 无论微信、qq安装与否，网关页面都显示渠道按钮。
    }

    public static PayPlugin getInstance() {
        if (instance == null) {
            synchronized (PayPlugin.class) {
                if (instance == null) {
                    instance = new PayPlugin();
                }
            }
        }
        return instance;
    }

    public IpaynowPlugin getmIpaynowplugin() {
        return mIpaynowplugin;
    }

    public void recycle(){
        mIpaynowplugin.setCallResultReceiver(null);
    }

}
