package com.whaleyvr.biz.parser.model;

/**
 * Created by dell on 2016/7/28.
 */
public class VideoBitType {

    /**
     *{
     *  1:还没用
     *  2:高清
     *  3:超清
     *  4:4k
     *}
     */
    public static final int ST = 1;
    public static final int SD = 2;
    public static final int HD = 3;
    public static final int SDA = 4;
    public static final int SDB = 5;
    public static final int TDA = 6;
    public static final int TDB = 7;

    public static String covert(int bitType){
        String type = "";
        switch (bitType){
            case ST:
                type = "ST";
                break;
            case SD:
                type = "SD";
                break;
            case HD:
                type = "HD";
                break;
            case SDA:
                type = "SDA";
                break;
            case SDB:
                type = "SDB";
                break;
            case TDA:
                type = "TDA";
                break;
            case TDB:
                type = "TDB";
                break;
        }
        return type;
    }

}
