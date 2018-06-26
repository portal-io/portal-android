package com.whaley.biz.sign;

import com.whaley.core.debug.logger.Log;
import com.whaley.core.utils.MD5Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/11/9.
 */

public class SignUtil {

    public static String generateSign(Map<String, String> map, boolean isContainskey, boolean isOrder) {
        StringBuilder signature = new StringBuilder();
        List arrayList = new ArrayList(map.entrySet());
        if (isOrder) {
            Collections.sort(arrayList, new Comparator() {
                public int compare(Object arg1, Object arg2) {
                    Map.Entry obj1 = (Map.Entry) arg1;
                    Map.Entry obj2 = (Map.Entry) arg2;
                    return (obj1.getKey()).toString().compareTo(obj2.getKey().toString());
                }
            });
        }
        if (isContainskey) {
            for (Iterator iter = arrayList.iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                signature.append(key);
                signature.append("=");
                signature.append(value);
                signature.append("&");
            }
            signature.delete(signature.length() - 1, signature.length());
        } else {
            for (Iterator iter = arrayList.iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String value = (String) entry.getValue();
                signature.append(value);
            }
        }
        return signature.toString();
    }


    public static SignBuilder builder() {
        return new SignBuilder();
    }

    public static class SignBuilder {

        private Map<String, String> signMap = new LinkedHashMap<>();
        private int signType;

        public SignBuilder put(String key, String value) {
            signMap.put(key, value);
            return this;
        }

        public SignBuilder signType(int signType) {
            this.signType = signType;
            return this;
        }

        public String getSign() {
            boolean isContainskey = (signType == SignType.TYPE_SHOW || signType == SignType.TYPE_WHALEYVR);
            boolean isOrder = (signType == SignType.TYPE_SHOW || signType == SignType.TYPE_WHALEYVR || signType == SignType.TYPE_CURRENCY || signType == SignType.TYPE_TEST_CURRENCY);
            String origin_signature = SignUtil.generateSign(signMap, isContainskey, isOrder) + Sign.getSign(signType);
            String signature = MD5Util.getMD5String(origin_signature);
            Log.d("origin_signature=" + origin_signature + " ,signature=" + signature);
            return signature;
        }
    }

}
