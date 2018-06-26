package com.whaley.biz.playerui.utils;

/**
 * Created by YangZhi on 2017/8/2 19:23.
 */

public class StringFormatUtil {

    public static String formatTime(long duration) {
        duration = duration/1000;
        int min = (int) (1f * duration / 60);
        int second = (int) duration - min * 60;
        boolean isMinBelowTen = min < 10;
        boolean isSecondBelowTen = second < 10;
        return (isMinBelowTen ? ("0" + min) : min) + ":" + (isSecondBelowTen ? ("0" + second) : second);
    }
}
