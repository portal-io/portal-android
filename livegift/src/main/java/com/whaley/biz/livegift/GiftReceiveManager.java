package com.whaley.biz.livegift;

import com.whaley.biz.livegift.model.GiftNoticeModle;

import java.util.ArrayDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author: qxw
 * Date:2017/10/20
 * Introduction:
 */

public class GiftReceiveManager {
    LinkedBlockingQueue selfGiftQueue;
    LinkedBlockingQueue otherGiftQueue;


    private boolean isHaveSelfGift;
    private boolean isHaveOtherGift;

    public GiftReceiveManager() {
        selfGiftQueue = new LinkedBlockingQueue();
        otherGiftQueue = new LinkedBlockingQueue();
    }


    public void addSelfGift(GiftNoticeModle giftNoticeModle) {
        selfGiftQueue.offer(giftNoticeModle);
        isHaveSelfGift = true;
    }

    public void addOtherGift(GiftNoticeModle giftNoticeModle) {
        otherGiftQueue.offer(giftNoticeModle);
        isHaveOtherGift = true;
    }

    public boolean isHaveGift() {
        return isHaveOtherGift || isHaveSelfGift;
    }

    public GiftNoticeModle poll() {
        if (isHaveSelfGift) {
            Object selfObject = selfGiftQueue.poll();
            if (selfObject != null) {
                return (GiftNoticeModle) selfObject;
            } else {
                isHaveSelfGift = false;
            }
        }
        if (isHaveOtherGift) {
            Object otherObject = otherGiftQueue.poll();
            if (otherObject != null) {
                return (GiftNoticeModle) otherObject;
            } else {
                isHaveOtherGift = false;
            }
        }
        return null;
    }


}
