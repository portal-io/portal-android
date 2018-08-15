package com.whaleyvr.biz.unity;

import com.whaley.core.appcontext.AppContextProvider;

import xiaofei.library.hermes.Hermes;
import xiaofei.library.hermes.HermesService;

/**
 * Created by dell on 2017/8/14.
 */

public class MessageInit {

    public static void init(){
        Hermes.init(AppContextProvider.getInstance().getContext());
        Hermes.register(MessageManager.class);
        Hermes.connect(AppContextProvider.getInstance().getContext(), HermesService.HermesService1.class);
    }

}
