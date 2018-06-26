package com.whaleyvr.biz.download.db;

import com.whaleyvr.biz.download.DownloadType;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

public class DownloadDatabseManager extends DownloadDatabaseManager<DownloadBean, String> {

    public static DownloadDatabseManager sInstance;

    public synchronized static DownloadDatabseManager getInstance() {
        if (sInstance == null) {
            sInstance = new DownloadDatabseManager();
        }
        return sInstance;
    }

    @Override
    public AbstractDao<DownloadBean, String> getAbstractDao() {
        return getSession().getDownloadBeanDao();
    }


    public List<DownloadBean> queryAllList(){
//        return getQueryBuilder().where(DownloadBeanDao.Properties.Type.eq(DownloadType.DOWNLOAD_TYPE_VIDEO)).orderDesc(DownloadBeanDao.Properties.DownloadDate).list();
        return getQueryBuilder().orderDesc(DownloadBeanDao.Properties.DownloadDate).list();
    }


    public boolean deleteListByIds(List<String> ids){
        String[] array=new String[ids.size()];
        ids.toArray(array);
        return deleteByKeyInTx(array);
    }

}
