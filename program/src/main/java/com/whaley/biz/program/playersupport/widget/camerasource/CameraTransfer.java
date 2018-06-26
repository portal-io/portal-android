package com.whaley.biz.program.playersupport.widget.camerasource;

/**
 * Created by dell on 2017/7/18.
 */

public class CameraTransfer {

    public static String covert(String source){
        String camera;
        switch (source){
            case "SlideCamera":
                camera = "边裁视角";
                break;
            case "Spidercam":
                camera = "上帝视角";
                break;
            case "ArmRight":
                camera = "右球门";
                break;
            case "ArmLeft":
                camera = "左球门";
                break;
            case "AlternativeRight":
                camera = "右替补席";
                break;
            case "AlternativeLeft":
                camera = "左替补席";
                break;
            case "Studio":
                camera = "演播室";
                break;
            case "Public":
                camera = "主视角";
                break;
            default:
                camera = source;
                break;
        }
        return camera;
    }

}
