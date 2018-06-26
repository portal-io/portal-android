package com.whaley.biz.program.model;

import java.io.Serializable;

/**
 * Created by dell on 2017/12/26.
 */

public class XyzModel implements Serializable {
    /**
     * x : 0.65
     * y : 0.65
     * z : 1
     */

    private String x;
    private String y;
    private String z;

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }
}
