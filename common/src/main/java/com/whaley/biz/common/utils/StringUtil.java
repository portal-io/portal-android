package com.whaley.biz.common.utils;

/**
 * Author: qxw
 * Date:2017/9/5
 * Introduction:
 */

public class StringUtil {
    
    public static String setResolveString(String resolve) {
        String temp[] = null;
        try {
            temp = resolve.split("_");
        } catch (Exception e) {

        }
        return temp[0];
    }

    /**
     * 电视剧转化
     *
     * @param replaceString
     * @return
     */
    public static String getReplaceEpisode(String replaceString) {
        try {
            return replaceString.replace("[", " 第").replace("]", "集");
        } catch (Exception e) {

        }
        return replaceString;
    }
}
