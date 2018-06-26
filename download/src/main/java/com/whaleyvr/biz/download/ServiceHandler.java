package com.whaleyvr.biz.download;

public interface ServiceHandler {
    <R extends ServiceManager>  R getServiceManager(Class clazz);
}
