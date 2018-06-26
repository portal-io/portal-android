package com.whaleyvr.biz.parser.model;

/**
 * Created by dell on 2016/7/28.
 */
public class VideoAlgorithm {

    public static final int OCT = 1;
    public static final int SPHERE = 2;

    public static String covert(int algorithm){
        String type = "";
        switch (algorithm){
            case OCT:
                type = "oct";
                break;
            case SPHERE:
                type = "sphere";
                break;
        }
        return type;
    }

}
