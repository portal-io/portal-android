package com.whaleyvr.biz.unity.model;

import java.util.List;

/**
 * Created by dell on 2017/12/12.
 */

public class DramaInfo {

    private String info;
    private String currentNode;
    private List<String> nodeTrack;

    public List<String> getNodeTrack() {
        return nodeTrack;
    }

    public void setNodeTrack(List<String> nodeTrack) {
        this.nodeTrack = nodeTrack;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String currentNode) {
        this.currentNode = currentNode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
