package com.whaley.biz.program.utils;

import java.math.BigDecimal;

/**
 * Author: qxw
 * Date: 2017/2/17
 */

public class StringUtil {

    /**
     * @param playCount
     * @return 分割万数字
     * @author qxw
     * @time 2017/2/17 20:03
     */

    public static String getCuttingCount(int playCount) {
        if (playCount >= 10000) {
            int million = playCount / 10000;
            int thousand = (playCount % 10000) / 1000;
            if (thousand > 0) {
                return million + "." + thousand + "万";
            } else {
                return million + "万";
            }
        } else {
            return playCount + "";
        }
    }

    /**
     * @param time
     * @return 根据秒计算时长
     * @author qxw
     * @time 2017/2/17 20:03
     */
    public static String getFormatTime(int time) {
        if (time < 60) {
            return time + "秒";
        }
        int hour = 0;
        int minute = time / 60;
        int second = time % 60;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String timeString = minute + "分" + second + "秒";
        if (hour != 0) {
            timeString = hour + "时" + timeString;
        }
        return timeString;
    }


    /**
     * @param time
     * @return 返回整时长
     * @author qxw
     * @time 2017/6/27 11:34
     */
    public static String getFormatWholeTime(int time) {
        int hour = 0;
        int minute = time / 60;
        if (minute >= 60) {
            hour = minute / 60;
        }
        if (hour != 0) {
            return hour + "小时后";
        }
        if (minute <= 10) {
            return "快要";
        }
        if (minute <= 20) {
            return "10分钟后";
        }
        if (minute <= 30) {
            return "20分钟后";
        }
        if (minute <= 40) {
            return "30分钟后";
        }
        if (minute <= 50) {
            return "40分钟后";
        }
        return "50分钟后";
    }

    /**
     * 阿拉伯数字转化为中文数字
     *
     * @param intInput
     * @return
     */
    public static String ToCH(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) // 個
        {
            sd += GetCH(intInput);
            return sd;
        } else if (si.length() == 2)// 十
        {
            if (si.substring(0, 1).equals("1"))
                sd += "十";
            else
                sd += (GetCH(intInput / 10) + "十");
            sd += ToCH(intInput % 10);
        } else if (si.length() == 3)// 百
        {
            sd += (GetCH(intInput / 100) + "百");
            if (String.valueOf(intInput % 100).length() < 2)
                sd += "零";
            sd += ToCH(intInput % 100);
        } else if (si.length() == 4)// 千
        {
            sd += (GetCH(intInput / 1000) + "千");
            if (String.valueOf(intInput % 1000).length() < 3)
                sd += "零";
            sd += ToCH(intInput % 1000);
        } else if (si.length() == 5)// 萬
        {
            sd += (GetCH(intInput / 10000) + "萬");
            if (String.valueOf(intInput % 10000).length() < 4)
                sd += "零";
            sd += ToCH(intInput % 10000);
        }

        return sd;
    }

    private static String GetCH(int input) {
        String sd = "";
        switch (input) {
            case 1:
                sd = "一";
                break;
            case 2:
                sd = "二";
                break;
            case 3:
                sd = "三";
                break;
            case 4:
                sd = "四";
                break;
            case 5:
                sd = "五";
                break;
            case 6:
                sd = "六";
                break;
            case 7:
                sd = "七";
                break;
            case 8:
                sd = "八";
                break;
            case 9:
                sd = "九";
                break;
            default:
                break;
        }
        return sd;
    }


    public static String getSegmentationString(String segmentation) {
        String temp[] = null;
        try {
            temp = segmentation.split("_");
            return temp[0];
        } catch (Exception e) {

        }
        return segmentation;
    }


    /**
     * 转化金钱数目 ===  分>元
     *
     * @param fen
     * @return
     */
    public static String fromFenToYuan(final String fen) {
        String yuan = "";
        final int MULTIPLIER = 100;
        try {
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        } catch (Exception e) {
            //
        }
        return yuan;
    }

    public static String fromFenToYuan(final int fen) {
        String yuan = "";
        final int MULTIPLIER = 100;
        try {
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
        } catch (Exception e) {
            //
        }
        return yuan;
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
