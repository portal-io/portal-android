package com.whaley.biz.program.playersupport.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YangZhi on 2017/8/22 14:51.
 */

public class CameraModel {

    private String camera;

    private String serverRenderType;

    private boolean isPublic;

    private String url;

    private List<DefinitionModel> definitionModelList=new ArrayList<>();

    public List<DefinitionModel> getDefinitionModelList() {
        return definitionModelList;
    }

    public void setDefinitionModelList(List<DefinitionModel> definitionModelList) {
        this.definitionModelList = definitionModelList;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getCamera() {
        return camera;
    }

    public void setServerRenderType(String serverRenderType) {
        this.serverRenderType = serverRenderType;
    }

    public String getServerRenderType() {
        return serverRenderType;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
