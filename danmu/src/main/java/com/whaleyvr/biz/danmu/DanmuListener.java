package com.whaleyvr.biz.danmu;

import java.util.List;

/**
 * Created by dell on 2017/7/13.
 */

public interface DanmuListener {

    void sendMsg(List<DanMu> danMuList);

    void sendMsgTop(DanMu danMu);

    void clearMsgTop();

}
