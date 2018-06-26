package com.whaley.biz.setting.db;

import com.whaley.core.appcontext.AppContextProvider;
import com.whaleyvr.core.local.db.AbstractDatabaseManager;
import com.whaleyvr.core.local.db.DaoSessionClearListener;

public abstract class LocalVideoDatabaseManager<M, K> extends AbstractDatabaseManager<M, K> {

    public LocalVideoDatabaseManager(){
        initOpenHelper(AppContextProvider.getInstance().getContext(),new LocalVideoDBOpenHelperCreator(),new LocalVideoDaoSessionCreator(), DaoSessionClearListener.getInstance());
    }


    protected DaoSession getSession(){
        return (DaoSession) getDaoSession();
    }


    protected LocalVideoOpenHelper getOpenHelper(){
        return (LocalVideoOpenHelper)getHelper();
    }
}
