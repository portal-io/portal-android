package com.whaleyvr.biz.hybrid.model;

import java.util.List;

/**
 * Created by YangZhi on 2016/10/14 0:16.
 */
public class RequestPayload{

    private boolean shouldCache;

    private boolean signature;

    private RequestModel requestModel;

    public static class RequestModel{
        private String url;

        private String method;

        public boolean isUseJson() {
            return useJson;
        }

        public void setUseJson(boolean useJson) {
            this.useJson = useJson;
        }

        private boolean useJson;

        private List<String> params;

        private List<String> headers;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public List<String> getHeaders() {
            return headers;
        }

        public void setHeaders(List<String> headers) {
            this.headers = headers;
        }
    }

    public RequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public boolean isShouldCache() {
        return shouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
    }

    public boolean isSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

}
