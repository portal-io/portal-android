package com.whaleyvr.biz.unity;

import xiaofei.library.hermes.annotation.ClassId;
import xiaofei.library.hermes.annotation.MethodId;

/**
 * Created by dell on 2016/11/23.
 */

@ClassId("ServerMessage")
public interface IServerMessage {

    @MethodId("sendEvent")
    void sendEvent(String eventName, String dataType, String json);

}
