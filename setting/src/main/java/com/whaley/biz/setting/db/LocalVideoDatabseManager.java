package com.whaley.biz.setting.db;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public class LocalVideoDatabseManager extends LocalVideoDatabaseManager<LocalVideoBean, String> {

    public static LocalVideoDatabseManager sInstance;

    public synchronized static LocalVideoDatabseManager getInstance() {
        if (sInstance == null) {
            sInstance = new LocalVideoDatabseManager();
        }
        return sInstance;
    }

    @Override
    public AbstractDao<LocalVideoBean, String> getAbstractDao() {
        return getSession().getLocalVideoBeanDao();
    }


    public List<LocalVideoBean> queryAllList(){
        return getQueryBuilder().orderDesc(LocalVideoBeanDao.Properties.LastUpdateTime).list();
    }

    public List<LocalVideoBean> queryLocalList(){
        return getQueryBuilder().where(LocalVideoBeanDao.Properties.IsDowloading.eq(false)).orderDesc(LocalVideoBeanDao.Properties.LastUpdateTime).list();
    }

    public boolean deleteListByIds(List<String> ids){
        String[] array=new String[ids.size()];
        ids.toArray(array);
        return deleteByKeyInTx(array);
    }

}
