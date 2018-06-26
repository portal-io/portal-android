package com.whaley.biz.livegift;

import android.support.v4.util.LruCache;

import com.whaley.biz.livegift.model.GiftModel;
import com.whaley.biz.livegift.model.MemberModel;

import java.util.List;

/**
 * Author: qxw
 * Date:2017/10/17
 * Introduction:
 */

public class GiftMemoryManager {

//    static LruCache<String, GiftMemoryManager> managers = new LruCache<>(5);
//
//    LruCache<String, MemberModel> oldMemberSelected = new LruCache<>(5);
//    LruCache<String, List<GiftModel>> giftTemp = new LruCache<>(5);
//    LruCache<String, List<MemberModel>> memberTemp = new LruCache<>(5);
//
//    String code;
//
//    private GiftMemoryManager(String code) {
//        this.code = code;
//    }
//
//    public static GiftMemoryManager getManager(String code) {
//        GiftMemoryManager mannage = managers.get(getKey(code));
//        if (mannage == null) {
//            mannage = new GiftMemoryManager(code);
//            managers.put(getKey(code), mannage);
//        }
//        return mannage;
//    }
//
//    public void putSelected(MemberModel member) {
//        oldMemberSelected.put(getKey(), member);
//    }
//
//    public void putGiftModel(List<GiftModel> giftModels) {
//        giftTemp.put(getKey(), giftModels);
//    }
//
//
//    public void putMemberTemp(List<MemberModel> memberModels) {
//        memberTemp.put(getKey(), memberModels);
//    }
//
//    public MemberModel getOldSelected() {
//        return oldMemberSelected.get(getKey());
//    }
//
//    public void removeOldSelected() {
//        oldMemberSelected.remove(getKey());
//    }
//
//    public List<MemberModel> getMemberTemp() {
//        return memberTemp.get(getKey());
//    }
//
//    public List<GiftModel> getGiftTemp() {
//        return giftTemp.get(getKey());
//    }
//
//    private String getKey() {
//        return getKey(code);
//    }
//
//    private static String getKey(String code) {
//        return code + "";
//    }
}
