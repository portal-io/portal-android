package com.whaleyvr.biz.download.db;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaleyvr.core.local.db.AbstractDatabaseManager;
import com.whaleyvr.core.local.db.DaoSessionClearListener;

public abstract class DownloadDatabaseManager<M, K> extends AbstractDatabaseManager<M, K> {

    public DownloadDatabaseManager(){
        initOpenHelper(AppContextProvider.getInstance().getContext(),new DownloadDBOpenHelperCreator(),new DownloadDaoSessionCreator(), DaoSessionClearListener.getInstance());
    }


    protected DaoSession getSession(){
        return (DaoSession) getDaoSession();
    }


    protected DownloadOpenHelper getOpenHelper(){
        return (DownloadOpenHelper)getHelper();
    }
}
