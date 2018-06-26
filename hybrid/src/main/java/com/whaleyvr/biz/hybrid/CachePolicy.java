package com.whaleyvr.biz.hybrid;

/**
 * Created by YangZhi on 2016/11/4 9:58.
 */

public class CachePolicy {
    public static final int CACHE_POLICY_NONE=0;

    public static final int CACHE_POLICY_DISK_AND_REFRESH=1;

    public static final int CACHE_POLICY_DISK=2;

    private int cachePolicy;

    private String[] ignoreCacheFileNames;

    private String[] needCachesHosts;


    private CachePolicy(int cachePolicy){
        this.cachePolicy=cachePolicy;
    }

    public static CachePolicy create(int cachePolicy){
        return new CachePolicy(cachePolicy);
    }

    public CachePolicy ignore(String[] ignoreCacheFileNames){
        this.ignoreCacheFileNames=ignoreCacheFileNames;
        return this;
    }

    public CachePolicy needCachesHosts(String[] needCachesHosts){
        this.needCachesHosts=needCachesHosts;
        return this;
    }

    public int getCachePolicy() {
        return cachePolicy;
    }

    public String[] getIgnoreCacheFileNames() {
        return ignoreCacheFileNames;
    }

    public String[] getNeedCachesHosts() {
        return needCachesHosts;
    }

    public boolean isHasCachesHosts() {
        return needCachesHosts!=null&&needCachesHosts.length>0;
    }
}
