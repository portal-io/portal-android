package com.whaleyvr.biz.download.db;

import android.database.sqlite.SQLiteDatabase;

import com.whaleyvr.core.local.db.ClearListener;
import com.whaleyvr.core.local.db.DaoSessionClearListener;
import com.whaleyvr.core.local.db.DaoSessionCreator;

import org.greenrobot.greendao.AbstractDaoSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DownloadDaoSessionCreator implements DaoSessionCreator {
    private static AbstractDaoSession daoSession;
    @Override
    public AbstractDaoSession onCreateDaoSession(SQLiteDatabase database) {
        if(daoSession==null) {
            daoSession = new DaoMaster(database).newSession();
            DaoSessionClearListener.getInstance().setClearListener(new ClearListener() {
                @Override
                public void onClear() {
                    DownloadDaoSessionCreator.this.onClear(daoSession);
                }
            });
        }
        return daoSession;
    }


    private void onClear(AbstractDaoSession daoSession){
        try {
            Class clazz=daoSession.getClass();
            Method method=clazz.getDeclaredMethod("clear");
            method.invoke(daoSession);
        }catch (NoSuchMethodException e){
            //
        }catch (InvocationTargetException e){
            //
        }catch (IllegalAccessException e){
            //
        }catch (Exception e){
            //
        }
    }

}
